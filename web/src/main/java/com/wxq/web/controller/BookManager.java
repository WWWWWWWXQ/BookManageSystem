package com.wxq.web.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

import com.wxq.web.service.dto.UserDTO;
import com.wxq.web.exception.UserNotFoundException;
import com.wxq.web.service.BookService;

/**
 * 图书管理请求处理层，通过调用BookService提供的功能进行高层抽象管理
 * 采用单例模式
 * @see BookService
 */

public class BookManager {
	private static BookManager bookManager;
	private BookService bookService;

	private static final int numForTest = 30;
	private static final int MAXCAPACITY = 100;
	private static int CURRENTBOOKNUM=0;

	private static final String HEADSTAR ="******************************->| ";
	private static final String TAILSTAR =" |<-******************************";


	/**
	 * 登陆的用户对象保存在此Map中。本类中所有的操作所需UserDTO对象均从此Map获取
	 * 但其实在实现的时候没有注意到这一点 虽然操作上并无大碍
	 */
	private volatile Map<String, UserDTO> currentUserDTOs = new HashMap<>();

	//private ConcurrentHashMap<String, UserDTO> concurrentHashMap = new ConcurrentHashMap<>();


	/**
	 * 静态方法，用于初始化图书馆
	 */
	private final static List<Book> BOOK_DTOS = Arrays.asList(
			new Book(1,"Core Java VolumeI","Cay S. Horstmann", "人民邮电出版社"),
			new Book(2,"Spring实战","Craig Walls", "人民邮电出版社"),
			new Book(3,"深入理解Java虚拟机","周志明", "机械工业出版社"),
			new Book(4,"Spring 源码深度解析","郝佳", "中国工信出版集团"),
			new Book(5,"Swift 4","张益珲", "清华大学出版社"),
			new Book(6,"自在独行","贾平凹", "长江文艺出版社"),
			new Book(7,"三体","刘慈欣", "重庆出版社"),
			new Book(8, "Le Comte de Monte-Cristo", "Alexandre Dumas", "上海译文出出版社"),
			new Book(9,"习近平新时代中国特色社会主义思想学习纲领","中共中央宣传部","人民出版社")
	);

	private final static List<Book> BOOKS_OF_MINE = Arrays.asList(
			new Book(1,"论如何在世界边缘无限徘徊", "吴肖琪", "532激情出版社" )

	);

	/**
	 * 私有构造函数，用于单例模式
	 */
	private BookManager() {

	}

	/**
	 * 获取单例对象
	 * @return BookManager 单例实体
	 */
	public static BookManager getInstance() {
		synchronized (BookManager.class) {
			if (bookManager == null) {
				bookManager = new BookManager();
			}
			return bookManager;
		}

	}

	public void addBookList(){
		LinkedList<Book> bookInTable = bookService.getAllBooks();
		for (Book book : BOOKS_OF_MINE){
			if (bookInTable.contains(book)){
				System.out.println("「" + book + "」已经录入了");
				continue;
			}
			bookService.addBook(book);
		}
		LocalDateTime localDateTime = LocalDateTime.now();
		localDateTime.toString();
	}

	/**
	 * 判断是否已经被初始化了
	 * 并且将已存入的书本初始化表
	 */
	public void initializeBook(){
		/*if (bookService.isInitialized(books))
			System.out.println("书整理好了，可以开馆了");
		else {
			CURRENTBOOKNUM = books.size();
			bookService.addBooks(books);
		}*/
	}



	/**
	 * 登陆函数，接收给出的用户名，实现用户的登陆操作
	 * {@code currentUserDTOs} 用于判断用户是否登陆
	 * 调用 {@code bookService.getUserDTOByName()} 函数进行业务逻辑层的实现
	 * @param UserDTOName
	 * @return boolean
	 * @throws UserNotFoundException
	 */
	public boolean loginUserDTO(String UserDTOName) throws UserNotFoundException {
		if (!currentUserDTOs.containsKey(UserDTOName)) {
			currentUserDTOs.put(UserDTOName, bookService.getUserByName(UserDTOName));
			System.out.println("登陆中...");
			System.out.println(HEADSTAR + UserDTOName + " 登陆成功"+TAILSTAR);
			return true;
		} else {
			System.out.println("您已登陆，无需再次登陆");
			return false;
		}
	}

	/**
	 * 用户登出 直接从 {@code currentUserDTOs}中删除对应的KV对就行
	 * @param UserDTOName
	 */
	public void logoutUserDTO(String UserDTOName) {
		System.out.println( HEADSTAR + UserDTOName + " 退出系统" + TAILSTAR );
		currentUserDTOs.remove(UserDTOName);
	}

	/**
	 * 用过给出的用户名新建一个用户
	 * @param UserDTODTO
	 * @return
	 */
	public boolean registeUserDTO(UserDTO UserDTODTO)  {
		return bookService.addUserDTO(UserDTODTO);
	}

	/**
	 * 从currentUserDTOs中获取登陆用户，用于执行该用户相关操作
	 * @param UserDTOName
	 * @return UserDTO
	 * @throws UserDTONotLoginException
	 */
	public synchronized UserDTO getCurrentUserDTO(String UserDTOName) throws UserDTONotLoginException {
		UserDTO UserDTODTO = currentUserDTOs.get(UserDTOName);
		if (UserDTODTO == null) {
			throw new UserDTONotLoginException();
		} else
			return UserDTODTO;
	}


	/**
	 * 处理借书请求，{@code synchronized}关键词用于调用对象自身的内部锁
	 * 但是没时间写多线程了 所以自动调用wait和notifyAll会多少拖慢运行的速度的
	 * @param UserDTOName
	 * @param bookId
	 * @return
	 * @throws UserDTONotLoginException
	 */
	public synchronized boolean borrowBook(String UserDTOName, String bookId) throws UserDTONotLoginException {
		UserDTO UserDTODTO = getCurrentUserDTO(UserDTOName);
		return bookService.borrowBook(UserDT, bookId);
	}

	/**
	 * 处理还书请求，大致和借书一样
	 * @param UserDTOName
	 * @param bookId
	 * @return
	 * @throws UserDTONotLoginException
	 */
	public synchronized boolean returnBook(String UserDTOName, String bookId) throws UserDTONotLoginException {
		UserDTO UserDTODTO = getCurrentUserDTO(UserDTOName);
		return bookService.returnBook(UserDTODTO, bookId);

	}


	/**
	 * 查询当前用户的所有借阅记录
	 * @param UserDTOName
	 */
	public synchronized void  queryUserDTOBorrowRecord(String UserDTOName) {
		System.out.println("\n"+ HEADSTAR
				+ UserDTOName
				+" 的借阅记录 " + TAILSTAR);
		bookService.getBorrowRecordByName(UserDTOName).forEach(System.out::println);
	}

	/**
	 * 显示当前图书馆里所记录的所有图书
	 */
	public synchronized void displayAllBooks() {
		LinkedList<Book> books =bookService.getAllBooks();
		if (books.size()==0)
			System.out.println("还没有书呢");
		else{
			System.out.println(HEADSTAR
					+" 藏书目录 " + TAILSTAR);
			books.forEach(System.out::println);
		}
	}

	/**
	 * 显示当前图书馆里所记录的所有用户信息
	 */
	public synchronized void displayAllUserDTOs(){
		LinkedList<UserDTO> UserDTODTODTOS = bookService.getAllUserDTOs();
		if (UserDTODTODTOS.size()==0)
			System.out.println("目前还没有人注册呢");
		else {
			System.out.println("\n"+HEADSTAR+"已注册用户列表"+TAILSTAR);
			UserDTODTODTOS.forEach(System.out::println);
		}
	}


	/**
	 * 计算并且支付超出归还时间的罚金
	 * @param UserDTOName
	 * @return
	 * @throws UserDTONotLoginException
	 */
	public boolean payBookCost(String UserDTOName) throws UserDTONotLoginException {
		UserDTO UserDTODTO = getCurrentUserDTO(UserDTOName);
		synchronized (UserDTODTO) {
			return bookService.payBookCost(UserDTODTO);
		}
	}

	/**
	 * 在图书馆初始化的时候判断当前系统里面是否已经有用户注册
	 * @return
	 */
	public synchronized boolean isAnyBodyElse(){
		return bookService.getAllUserDTOs().size() > 0;
	}


	/**
	 * 判读指定用户是否已经注册
	 * 通过UserDTO_Name的unique索引进行判断
	 * @param UserDTOName
	 * @return
	 */
	public synchronized boolean isUserDTOExist(String UserDTOName){
		try {
			return (bookService.getUserDTOByName(UserDTOName) != null);
		} catch (UserDTONotFoundException UserDTONotFoundException) {
			return false;
		}
	}

	/**
	 * 通过给出的书本id搜索book名
	 * @param bookId
	 * @return
	 */
	public String queryBookNameByBookId(String bookId){
		return bookService.getBookNameByBookId(bookId);
	}

	/**
	 * 以下函数时间不够了 还没有在Test类里面进行业务模拟
	 * @param books
	 * @return
	 */

	public boolean addBookAsList(List<Book> books){
		return bookService.addBooks(books);
	}

	public boolean addBook(Book book) {
		LinkedList<Book> bookInTable = bookService.getAllBooks();
		if (bookInTable.contains(book)){
			System.out.println("「"+ book +"」已经录入了");
			return false;
		}
		bookService.addBook(book);
		return true;
	}

	public boolean deleteBook(String bookId) {
		return bookService.deleteBook(bookId);
	}

	//通过书名查询图书
	public void queryBook(String bookName) {
		bookService.getBooksByBookName(bookName).stream().forEach(System.out::println);
	}
	//查询用户信息
	public void queryUserDTO(String UserDTOName) throws UserDTONotFoundException {
		System.out.println(bookService.getUserDTOByName(UserDTOName));
	}

	public void currentBorrowTest(String UserDTOName, int bookId){
		try {
			if (borrowBook(UserDTOName, String.valueOf(bookId)))
				System.out.println(UserDTOName
						+ "借阅了《"
						+ queryBookNameByBookId(String.valueOf(bookId))
						+ "》\n");
			bookManager.displayAllBooks();
			Thread.sleep(1000);
		} catch (UserDTONotLoginException | InterruptedException e) {
		}
	}
	public void currentReturnTest(String UserDTOName, int bookId){
		String returnBookId = String.valueOf(bookId);
		try {
			if (returnBook(UserDTOName, returnBookId)) {
				System.out.println(UserDTOName + "归还了" + returnBookId +"\n");
				payBookCost(UserDTOName);
				Thread.sleep(1000);
			}
		} catch (UserDTONotLoginException | InterruptedException e) {
		}
	}

	public static void main(String[] args) {
		BookManager bookManager = BookManager.getInstance();

		Runnable runnable1 = () -> {
			try {
				bookManager.loginUserDTO("吴肖琪");
			} catch (UserDTONotFoundException UserDTONotFoundException) {
				Logger.getGlobal().info(UserDTONotFoundException.getMessage());
			}
			//int count = (int) (10 * Math.random());
			for (int i =0 ; i<5;i++) {
				bookManager.currentBorrowTest("吴肖琪", (int) (numForTest * Math.random()));
				bookManager.currentReturnTest("吴肖琪", (int) (numForTest * Math.random()));
			}
		};
		Runnable runnable2 = () -> {
			try {
				bookManager.loginUserDTO("whoever");
			} catch (UserDTONotFoundException UserDTONotFoundException) {
				Logger.getGlobal().info(UserDTONotFoundException.getMessage());
			}
			//int count = new Random(9).nextInt(5);
			for (int i =0 ; i<5;i++) {
				bookManager.currentBorrowTest("whoever", (int) (numForTest * Math.random()));
				bookManager.currentReturnTest("whoever", (int) (numForTest * Math.random()));
			}
		};
		new Thread(runnable1).start();
		new Thread(runnable2).start();


		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		bookManager.currentUserDTOs.forEach((k,v) ->
				System.out.println(k+ " " + v));
		bookManager.displayAllUserDTOs();
	}



}