package sql;

import java.lang.ref.Cleaner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * 本类为数据库连接管理类 初始化时建立数据库连接池，当有数据库操作时将数据库连接从池中取走，操作完毕将连接返回连接池
 * getConnection（）方法获得自动确认的数据库连接 getTransactionConnection()方法获得事务连接
 * releaseConnection方法用于释放连接。当连接池中数量超过正常数量时结束连接，小于正常连接数时返回连接池
 */
public class ConnectPool {
	private static LinkedList<Connection> connectionPool = new LinkedList<>();

	static {
		for (int i = 0; i < 5; i++) {
			Connection conn = null;
			do {
				conn = new ConnectPool().new Connect().getConnection();
				if (conn ==null){
					Logger.getGlobal().info("连接拥挤，等待中");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}while (conn == null);

			connectionPool.add(conn);
		}
	}


	public Connection getConnection() {
		if (connectionPool.isEmpty()) {
			return new Connect().getConnection();
		} else
			return connectionPool.removeFirst();
	}

	public Connection getTransactionConnection() throws SQLException {
		Connection conn = getConnection();
		conn.setAutoCommit(false);
		return conn;
	}

	@Override
	public void finalize() {
		disposeAll();
	}
	public void disposeAll() {
		Connection conn = null;
		while ((conn = connectionPool.removeFirst()) != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("close connection failed!");
			}
		}
	}


	public void releaseConnection(Connection conn) {
		try {
			if (connectionPool.size() < 5) {
				conn.setAutoCommit(true);
				connectionPool.add(conn);
			} else
				conn.close();
		} catch (SQLException e) {
			System.out.println("release connection failed!");
		}
	}

	class Connect {
		private final String VER = "com.mysql.cj.jdbc.Driver";
		private final String URL = "jdbc:mysql://127.0.0.1:3306/BookLibrary?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false";
		private final String USER = "root";
		private final String PASS = "wuxiaoqi889900";

		Connection conn = null;

		public Connection getConnection() {
			try {
				Class.forName(VER);
			} catch (ClassNotFoundException e) {
				System.out.println("驱动驱动！说了几百遍驱动没加载");
				e.printStackTrace();
			}
			try {
				conn = DriverManager.getConnection(URL, USER, PASS);
			} catch (SQLException e) {
				//System.out.println("数据库连接失败 检查一下是不是名字打错了");
				Logger.getGlobal().info("呀 连的人太多啦 一会儿再试哟！");

			}

			return (conn);
		}
	}
}
