package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import book.BookManager;
import dao.BookDao;
import entity.Book;
import entity.BorrowRecord;
import entity.User;
import exception.BookNotFoundException;
import exception.BookNotInstoreException;
import exception.SQLUpdateException;
import exception.UserMaxBorrowedException;
import exception.UserNotFoundException;
import exception.UserOweFeeException;
import sql.ConnectPool;

/**
 * 逻辑处理层
 */
public class BookServiceImp implements BookService {
	private BookDao bookDao;
	private ConnectPool connectPool = new ConnectPool();

	public BookServiceImp(BookDao bookDao) {
		this.bookDao = bookDao;
	}


	/**
	 * 对于传入的Book列表进行数据持久化的操作
	 * @param books
	 * @return
	 */
	@Override
	public boolean addBooks(List<Book> books) {
		Connection conn = connectPool.getConnection();
		try {
			for (Book book : books){
				if (bookDao.insertBook(conn, book)){
					System.out.println("《" + book.getBookName()+"》录入成功");
				}else {
					connectPool.releaseConnection(conn);
					System.out.println("《" + book.getBookName()+"》录入失败");
				}
			}
			connectPool.releaseConnection(conn);
		}catch (SQLUpdateException e){
			System.out.println("数据库操作 " + e.getMessage() + " 失败");
			return false;
		}
		return true;
	}

	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	@Override
	public boolean addUser(User user) {
		Connection conn = connectPool.getConnection();
		try {
			if (bookDao.insertUser(conn, user)) {
				connectPool.releaseConnection(conn);
				System.out.println("恭喜 " + user.getUserName()+ " 成为本图书馆用户");
				return true;
			} else {
				connectPool.releaseConnection(conn);
				System.out.println("抱歉 " +user.getUserName() + " 加入失败");
				return false;
			}
		} catch (SQLUpdateException e) {
			System.out.println("数据库操作 " + e.getMessage() + " 失败");
			return false;
		}
	}

	/**
	 * 通过用户名查询借阅记录
	 * @param userName
	 * @return
	 */
	@Override
	public List<BorrowRecord> getBorrowRecordByName(String userName) {
		Connection conn = null;
		try {
			conn = connectPool.getTransactionConnection();
			return bookDao.getBorrowRecordByUserId(conn, bookDao.getUserIdByName(conn,userName));
		} catch (UserNotFoundException | SQLException | SQLUpdateException userNotFoundException) {
			Logger.getGlobal().info(userNotFoundException.getMessage());
		}
		return null;
	}


	@Override
	public synchronized LinkedList<Book> getAllBooks() {
		Connection conn = connectPool.getConnection();
		return bookDao.getAllBooks(conn);

	}

	@Override
	public synchronized LinkedList<User> getAllUsers() {
		Connection conn = connectPool.getConnection();

		return bookDao.getAllUsers(conn);

	}

	@Override
	public String getUserTypeByName(String userName) {
		Connection conn = connectPool.getConnection();
		try {
			return bookDao.getUserTypeById(conn,bookDao.getUserIdByName(conn,userName));
		} catch (SQLUpdateException | UserNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	//查询用户  抛出无此用户异常
	 @Override
	public User getUserByName(String userName) throws UserNotFoundException {
		Connection conn = connectPool.getConnection();
		User user;
		try {
			user = bookDao.getUserByName(conn, userName);
			return user;
		} catch (SQLUpdateException e) {
			System.out.println("数据库操作 " + e.getMessage() + " 失败");
			return null;
		}
	}

	@Override
	public String getBookNameByBookId(String bookId) {
		Connection conn = connectPool.getConnection();
		String bookName = null;
		try {
			bookName = bookDao.getBookByBookId(conn,bookId).getBookName();
		} catch (BookNotFoundException | SQLUpdateException e) {
		}

		return bookName;
	}


	//按书名查询图书
	@Override
	public List<Book> getBooksByBookName(String bookName) {
		Connection conn = connectPool.getConnection();
		List<Book> list = bookDao.getBooksByBookName(conn, bookName);

		return list;
	}


	/**
	 * 有欠费不能借书，需先调用paybookCost方法还清欠款 达到最大借书量不能借书 更新图书表中的borrower与is_instore属性 更新借阅表
	 * 更新用户表中的book_num属性+1 数据库操作中发生异常时，进行回滚rollback（）
	 * 
	 * @see service.BookService#borrowBook(entity.User, java.lang.String)
	 */
	@Override
	public synchronized boolean borrowBook(User user, String bookId) {
		Connection conn = null;
		try {
			if (user.isOweFee())
				throw new UserOweFeeException();
			if (user.isMaxBorrowed())
				throw new UserMaxBorrowedException();
			conn = connectPool.getTransactionConnection();
			Book book = bookDao.getBookByBookId(conn, bookId);
			String userId=bookDao.getUserIdByName(conn,user.getUserName());
			if (book.isInStore()
					&& !bookDao.isBorrowRecordExsist(conn
					,bookId
					,Integer.parseInt(userId))){
				try {
					user.addBookNum();
					book.setInStore(false);
					//String userId = bookDao.getUserIdByName(conn, user.getUserName());
					bookDao.updateBookBorrow(conn, user.getUserName(), bookId);
					bookDao.insertBorrowRecord(conn, userId, bookId);
					bookDao.updateUser(conn, user);
					conn.commit();
				} catch (Exception e) {
					conn.rollback();
					user.minusBookNum();
					book.setInStore(true);
					Logger.getGlobal().info("呀！添加失败了 " +e.getMessage());
					return false;
				}
				return true;
			} else
				throw new BookNotInstoreException();
		} catch (UserOweFeeException e) {
			System.out.println("借记记录有点问题啊");
			return false;
		} catch (UserMaxBorrowedException e) {
			System.out.println("书都借到上限了 看了吗？看完了还不还？");
			return false;
		} catch (BookNotFoundException e) {
			System.out.println(e.getMessage());
			return false;
		} catch (BookNotInstoreException | UserNotFoundException e) {
			return false;
		} catch (SQLException e) {
			Logger.getGlobal().info(e.getMessage());
			return false;
		} catch (SQLUpdateException e) {
			System.out.println("数据库操作 " + e.getMessage() + " 失败");
			return false;
		}finally {
			connectPool.releaseConnection(conn);
		}
	}

	/**
	 * 还书
	 * @param user
	 * @param bookId
	 * @return
	 */
	@Override
	public synchronized boolean returnBook(User user, String bookId) {
		Connection conn = null;
		try {
			conn = connectPool.getTransactionConnection();
			Book book = bookDao.getBookByBookId(conn, bookId);

			if (bookDao.isBorrowRecordExsist(conn,bookId
					,Integer.parseInt(bookDao.getUserIdByName(conn,user.getUserName())))
					&& bookDao.getBorrowRecordByBookId(conn,bookId).getReturnTime()==null){
				try{
					bookDao.updateBookReturn(conn,bookId);
					book.setInStore(true);
					if(bookDao.updateBorrowRecordReturnTime(conn,bookId))
						System.out.println(user.getUserName() + "归还了《" + book.getBookName()+"》");
					double payment =bookDao.caculatePayment(conn,bookId,user.getUserName());
					System.out.println("借阅费用: ¥"+payment );
					user.setCostAmount(payment);
					bookDao.updateBorrowRecordPayment(conn,bookId,payment);
					user.minusBookNum();
					bookDao.updateUser(conn,user);
					conn.commit();
				}catch (Exception e) {
					conn.rollback();
					user.minusBookNum();
					System.out.println("还书失败了：" + e.getMessage());
					e.printStackTrace();
					return false;
				}
			}
			else {
				System.out.println("无此借阅记录");
				return false;
			}
		} catch (SQLException | UserNotFoundException | SQLUpdateException | BookNotFoundException e) {
			Logger.getGlobal().info("还书书的时候出错啦" + e.getMessage());

		}finally {
			connectPool.releaseConnection(conn);
		}

		return false;
	}


	@Override
	public synchronized boolean payBookCost(User user) {
		Connection conn = connectPool.getConnection();
		try {
			if (bookDao.updateUser(conn, user)) {
				connectPool.releaseConnection(conn);
				if (user.getCostAmount()==0.0)
					System.out.println("\n借阅记录良好诶！继续保持");
				else
					System.out.println("\n现在作业都来不及写了，有时间再还吧");
				return true;
			} else {
				connectPool.releaseConnection(conn);
				System.out.println("付款失败，下次再试吧");
				return false;
			}
		} catch (SQLUpdateException e) {
			System.out.println("数据库操作 " + e.getMessage() + " 失败");
			return false;
		}
	}

	@Override
	public boolean isInitialized(List<Book> books, Book book) {
		Connection conn = connectPool.getConnection();
		if (books.contains(book)){
			System.out.println("「"+book+"」已经录入了");
			return false;
		}
		connectPool.releaseConnection(conn);
		return true;
	}
	/*@Override
	public boolean isInitialized(List<Book> books) {
		Connection conn = connectPool.getConnection();
		boolean flag =true;
		if (bookDao.getAllBooks(conn).size()!=books.size()) {
			try {
				bookDao.truncateTable(conn, "book");
			}catch (SQLUpdateException sqle) {Logger.getGlobal().info(sqle.getMessage());}
			return false;
		}
		try {
			for (Book book : books)
				bookDao.getBookByBookId(conn, String.valueOf(book.getBookId()));
		} catch (SQLUpdateException | BookNotFoundException e) {
			flag = false;
			try {
				bookDao.truncateTable(conn, "book");
			}catch (SQLUpdateException sqle) {}
		}
		return flag;
	}*/


	/*
	没时间写了 放在最后面
	 */
	@Override
	public boolean deleteBook(String bookId) {
		Connection conn = connectPool.getConnection();
		bookDao.deleteBookByBookId(conn,bookId);
		//connectPool.releaseConnection(conn);
		return false;
	}

	@Override
	public synchronized boolean addBook(Book book) {
		Connection conn = connectPool.getConnection();
		try {
			if (bookDao.insertBook(conn,book)){
				System.out.println("《" + book.getBookName()+"》录入成功");
			}else {

				System.out.println("《" + book.getBookName()+"》录入失败");
			}
		} catch (SQLUpdateException e) {
		}
		return false;
	}
	/*public synchronized void releaseConnect(){

		Connection connection = connectPool.getConnection();
		connectPool.releaseConnection(connection);

	}*/

}
