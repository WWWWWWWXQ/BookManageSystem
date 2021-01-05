package com.wxq.web.entity;

import com.wxq.web.constant.ConstantValue;
import lombok.Data;

/**
 * 主要改的一个是User的构造函数 默认在注册的时候 没必要制定借书数 应该默认为0
 */
@Data
public class User {
	private String userName;
	//用户类型 学生为stu，教师为tea
	private String type;
	//当前借阅数量（未还）
	private int bookNum;
	//欠费总额
	private double costAmount;

	public User(String userName, String type, int bookNum, double costAmount) {
		super();
		this.userName = userName;
		this.type = type;
		this.bookNum = bookNum;
		this.costAmount = costAmount;
	}

	public User(String userName, String type){
		super();
		this.userName=userName;
		this.type=type;
		this.bookNum = 0;
		this.costAmount = 0;

	}

	@Override
	public String toString() {
		return 	type
				+ " " + userName
				+ ", 已借阅 " + bookNum
				+ " 本书, 共花费 ¥" + costAmount;
	}

	//是否欠费
	public boolean isOweFee() {
		if (costAmount != 0) {
			return true;
		} else
			return false;
	}

//是否达到最大借书数量
	public boolean isMaxBorrowed() {
		if (type.equals("stu")) {
			return bookNum >= ConstantValue.STUBOOKNUM;
		} else
			return bookNum >= ConstantValue.TEABOOKNUM;
	}

//借书成功Num+1
	public void addBookNum() {
		bookNum++;
	}

//还书num-1
	public void minusBookNum() {
		bookNum--;
	}

	public double getCostAmount() {
		return costAmount;
	}

	public void setCostAmount(double costAmount) {
		this.costAmount = costAmount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getBookNum() {
		return bookNum;
	}

	public void setBookNum(int bookNum) {
		this.bookNum = bookNum;
	}
}
