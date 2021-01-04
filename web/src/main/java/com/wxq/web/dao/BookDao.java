package com.wxq.web.dao;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.wxq.web.constant.ConstantValue;
import com.wxq.web.entity.Book;
import com.wxq.web.entity.BorrowRecord;
import com.wxq.web.entity.User;
import com.wxq.web.exception.BookNotFoundException;
import com.wxq.web.exception.SQLUpdateException;
import com.wxq.web.exception.UserNotFoundException;


public class BookDao {
    private PreparedStatement pstmt;


    public boolean insertUser(Connection conn,User user) throws SQLUpdateException {
        String sql = "insert into user (type,user_name,book_num,cost_amount) values (?,?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getType());
            pstmt.setString(2, user.getUserName());
            pstmt.setInt(3, user.getBookNum());
            pstmt.setDouble(4, user.getCostAmount());
            if (pstmt.executeUpdate()!=0) {
                return true ;
            }else {
                System.out.println("用户已经存在啦 因为user_name是unique的！");
                return false;
            }
        }catch (SQLException e) {
            throw new SQLUpdateException("插入用户的时候");
        }
    }

    public boolean insertBook(Connection conn, Book book) throws SQLUpdateException{
        String sql = "insert into book (book_name, author, publish_house, is_instore) values(?,?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,book.getBookName());
            pstmt.setString(2,book.getAuthor());
            pstmt.setString(3,book.getPublishHouse());
            pstmt.setInt(4,book.isInStore() ? 1 : 0);
            if (pstmt.executeUpdate()!=0) {
                return true ;
            }else {
                System.out.println("插入失败 正常来说应该不会遇到");
                return false;
            }
        }catch (SQLException e){
            throw new SQLUpdateException("插入书本的时候出问题啦");
        }
    }

    //借书时添加借阅记录
    public boolean insertBorrowRecord(Connection conn, String userId, String bookId)
            throws SQLUpdateException {
        String sql="insert into borrow (book_id,user_id,borrow_time, return_time, payment) values (?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookId);
            pstmt.setString(2, userId);
            pstmt.setTimestamp(3,Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setObject(4,null);
            pstmt.setDouble(5,0.0);
            if(pstmt.executeUpdate()!=0) {
                return true;
            }else
                throw new SQLUpdateException("执行失败");
        } catch (SQLException e) {
            throw new SQLUpdateException("插入借阅记录的时候出问题啦");
        }
        finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.out.println("SQL关闭语句出问题啦");
            }
        }
    }


    //借书时修改数据库中book实体的instore与borrower属性
    public boolean updateBookBorrow(Connection conn, String userName, String bookId)
            throws SQLUpdateException {
        String sql="update book set is_instore=0,borrower=? where book_id=?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userName);
            pstmt.setString(2, bookId);
            if(pstmt.executeUpdate()!=0) {
                return true;
            }else throw new SQLUpdateException("更新book表的时候出问题了");
        } catch (SQLException e) {
            throw new SQLUpdateException("更新book表的时候出问题了");
        }finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.out.println("SQL关闭语句出问题啦");
            }
        }
    }
    public boolean updateUser(Connection conn,User user) throws SQLUpdateException {
        String sql = "update user set book_num=?,cost_amount=? where user_name=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,user.getBookNum());
            pstmt.setDouble(2, user.getCostAmount());
            pstmt.setString(3,user.getUserName());
            pstmt.executeUpdate();
            pstmt.close();
            return true ;
        }catch (SQLException e) {
            throw new SQLUpdateException("更新user表的时候出问题啦");
        }
    }

    // 还书时修改数据库中book实体的instore与borrower属性
    public boolean updateBookReturn(Connection conn, String bookId)
            throws SQLUpdateException {
        String sql="update book set is_instore=1,borrower=? where book_id=?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, null);
            pstmt.setString(2, bookId);
            if(pstmt.executeUpdate()!=0) {
                return true;
            }else throw new SQLUpdateException("更新书本return_time的时候");
        } catch (SQLException e) {
            throw new SQLUpdateException("更新书本return_time的时候");
        }finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.out.println("SQL关闭语句出问题啦");
            }
        }
    }

    //还书时更新借阅记录
    /*在borrow表中找到包含bookId且return_time为null的记录，更新return_time为当前时间（LocalDateTime类型)，payment为经计算的借阅超期罚金
     *使用pstmt传递return_time参数 例：pstmt.setObject(1, LocalDateTime.now());
     *
     *
     */
    public boolean updateBorrowRecordReturnTime(Connection conn,String bookId)
            throws SQLUpdateException {
        String sql="update borrow set return_time=? where book_id=?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1,Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(2, bookId);
            if(pstmt.executeUpdate()!=0) {
                return true;
            }else throw new SQLUpdateException("更新借阅记录失败");
        } catch (SQLException e) {
            throw new SQLUpdateException("更新借阅记录失败");
        }finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.out.println("close语句失败");
            }
        }

    }
    public boolean updateBorrowRecordPayment(Connection conn,String bookId, double payment)
            throws SQLUpdateException {
        String sql="update borrow set payment=? where book_id=?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, payment);
            pstmt.setString(2, bookId);
            if(pstmt.executeUpdate()!=0) {
                return true;
            }else throw new SQLUpdateException("更新借阅记录失败");

        } catch (SQLException e) {
            throw new SQLUpdateException("更新借阅记录失败");
        }finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.out.println("close语句失败");
            }
        }

    }


    public LinkedList<User> getAllUsers(Connection conn){
        String sql = "select type, user_name, book_num, cost_amount from user";
        LinkedList<User> users = new LinkedList<>();
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getString(2)
                        ,rs.getString(1)
                        ,rs.getInt(3)
                        ,rs.getDouble(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public LinkedList<Book> getAllBooks(Connection conn){
        String sql = "select book_id, book_name, author, publish_house, is_instore, borrower from book";
        LinkedList<Book> books = new LinkedList<>();
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                books.add(new Book(rs.getInt(1)
                        ,rs.getString(2)
                        ,rs.getString(3)
                        ,rs.getString(4)
                        ,rs.getInt(5)== 1
                        ,rs.getString(6)));
            }
        } catch (SQLException e) {
            Logger.getGlobal().info(e.getMessage());
        }
        return books;
    }

    public LinkedList<BorrowRecord> getBorrowRecordByUserId(Connection conn, String userId){
        String sql = "select book_id, borrow_time, return_time, payment from borrow where user_id=?";
        LinkedList<BorrowRecord> borrowRecords = new LinkedList<>();
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(userId));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                borrowRecords.add(new BorrowRecord(rs.getInt(1)
                        ,Integer.parseInt(userId)
                        ,rs.getTimestamp(2).toLocalDateTime()
                        ,rs.getTimestamp(3) == null ? null : rs.getTimestamp(3).toLocalDateTime()
                        ,rs.getDouble(4) ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (borrowRecords.size() == 0){
            System.out.println("暂无任何借记记录");
        }
        return borrowRecords;
    }


    public BorrowRecord getBorrowRecordByBookId(Connection conn, String bookId) {
        String sql = "select user_id, borrow_time, return_time,payment from borrow where book_id=?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(bookId));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                //System.out.println(rs.getTimestamp(3).toLocalDateTime()==null);
                if (rs.getTimestamp(3)==null) {
                    return new BorrowRecord(Integer.parseInt(bookId)
                            , rs.getInt(1)
                            , rs.getTimestamp(2).toLocalDateTime()
                            , null
                            , rs.getDouble(4));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

        public User getUserByName(Connection conn,String userName)
            throws SQLUpdateException, UserNotFoundException {
        String sql = "select type,user_name,book_num,cost_amount from user where user_name=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userName);
            ResultSet rs=pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getString(2)
                        ,rs.getString(1)
                        ,rs.getInt(3)
                        ,rs.getDouble(4));
            }else throw new UserNotFoundException();
        } catch (SQLException  e ) {
            throw new SQLUpdateException("查询用户");
        }

    }

    public String getUserTypeById(Connection conn, String userId)
            throws SQLUpdateException, UserNotFoundException{
        String sql = "select type from user where user_id=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(userId));
            //pstmt.setString(1,userId);
            ResultSet rs=pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }else throw new UserNotFoundException();
        } catch (SQLException e) {
            throw new SQLUpdateException("query user type");
        }
    }

    public String getUserIdByName(Connection conn, String userName)
            throws SQLUpdateException, UserNotFoundException {
        String sql = "select user_id from user where user_name=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userName);
            ResultSet rs=pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }else throw new UserNotFoundException();
        } catch (SQLException e) {
            throw new SQLUpdateException("query UserId");
        }
    }

    public synchronized Book getBookByBookId(Connection conn, String bookId)
            throws BookNotFoundException, SQLUpdateException {
        String sql = "select * from book where book_id=?";
        try {
            pstmt= conn.prepareStatement(sql);
            pstmt.setString(1, bookId);
            ResultSet rs=pstmt.executeQuery();
            if (rs.next()) {
                //boolean inStore = rs.getInt(5) == 1 ? true : false;
                return new Book(rs.getInt(1)
                        , rs.getString(2)
                        , rs.getString(3)
                        , rs.getString(4)
                        , rs.getInt(5) == 1
                        , rs.getString(6));
            }else throw new BookNotFoundException("无此图书");
        } catch (SQLException e) {
            throw new SQLUpdateException("query Book");
        }finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.out.println("close statement failed!");
            }
        }
    }


    public boolean truncateTable(Connection conn, String tableName)
            throws SQLUpdateException {
        String sql = "truncate table ?";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,tableName);
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        }catch (SQLException e){
            throw new SQLUpdateException("truncate 表 "+tableName+" 失败");
        }
    }


    public boolean deleteBookByBookId(Connection conn, String bookId) {
        String sql = "delete from book where book_id=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,Integer.parseInt(bookId));
            if(pstmt.executeUpdate()!=0)
                return true;
        } catch (SQLException e) {
            Logger.getGlobal().info("删除书的时候出错啦" + e.getMessage());
        }
        return false;
    }

    public double caculatePayment(Connection conn,String bookId, String userNmae){
        String sql = "select borrow_time, return_time from borrow where book_id=?";
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,Integer.parseInt(bookId));
            ResultSet rs=pstmt.executeQuery();
            if (rs.next()) {
                long day = Duration
                        .between(rs.getTimestamp(2).toLocalDateTime()
                                ,rs.getTimestamp(1).toLocalDateTime())
                        .toDays();
                String type = getUserTypeById(conn, getUserIdByName(conn,userNmae));
                if (type.equals("tea"))
                    return day<ConstantValue.TEABORROWDAYS ? 0 : (day-ConstantValue.TEABORROWDAYS)*0.1;
                else if (type.equals("stu"))
                    return day<ConstantValue.STUBORROWDAYS ? 0 : (day-ConstantValue.STUBORROWDAYS)*0.1;
            }else throw new UserNotFoundException();
        } catch (SQLException | UserNotFoundException | SQLUpdateException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean isBorrowRecordExsist(Connection conn, String bookId,int userId){
        String sql = "select user_id, return_time from borrow where book_id=?";
        boolean flag = false;
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,Integer.parseInt(bookId));
            ResultSet rs=pstmt.executeQuery();
            while (rs.next()) {
                Timestamp timestamp =rs.getTimestamp(2);
                if (rs.getInt(1) == userId && timestamp==null)
                    flag =  true;
            }
        } catch (SQLException  e) {
            e.printStackTrace();
        }
        return flag;
    }

    //按照书名查询图书，返回同名图书的列表
    public List<Book> getBooksByBookName(Connection conn, String bookName) {
        // TODO Auto-generated method stub
        return null;
    }

}