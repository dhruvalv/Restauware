package com.dhruval.restauware.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.dhruval.restauware.dao.Customer;
import com.dhruval.restauware.dao.Menu;
import com.dhruval.restauware.dao.Orders;
import com.dhruval.restauware.dao.Staff;
import com.dhruval.restauware.dao.User;

public class DBConnection {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;

	public DBConnection() throws Exception {
		getDetails();
		initDB();
	}

	public void initDB() throws Exception {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(unicode, base, pass);
			statement = connect.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -- Staff Table -->
	public ResultSet getStaffCategories() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select distinct(category) from staff";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getStaffName() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select distinct(fname) from staff";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public int updateStaff(Staff staff) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "update staff set category=?,refdby=?,fname=?,mname=?,lname=?,gender=?,address=?,dob=?,mobile=?,tele=?,idproof=?,idno=?,doj=?,dol=? where staffNo =?;";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, staff.getCategory());
		preparedStatement.setString(2, staff.getRefdby());
		preparedStatement.setString(3, staff.getFname());
		preparedStatement.setString(4, staff.getMname());
		preparedStatement.setString(5, staff.getLname());
		preparedStatement.setString(6, staff.getGender());
		preparedStatement.setString(7, staff.getAddress());
		preparedStatement.setString(8, staff.getDob());
		preparedStatement.setLong(9, staff.getMobile());
		preparedStatement.setLong(10, staff.getTelephone());
		preparedStatement.setString(11, staff.getIdproof());
		preparedStatement.setString(12, staff.getIdno());
		preparedStatement.setString(13, staff.getDoj());
		preparedStatement.setString(14, staff.getDol());
		preparedStatement.setInt(15, staff.getStaffNo());
		result = preparedStatement.executeUpdate();
		return result;
	}

	public int deleteStaffByNo(int no) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "delete from staff where staffNo =?;";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, no);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public ResultSet getStaffByCategoty(String category) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select staffNo,category,refdby,fname,mname,lname,gender,address,dob,mobile,tele,idproof,idno,doj,dol from staff where category = ?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, category);
		result = preparedStatement.executeQuery();
		return result;
	}

	public int addNewStaff(Staff staff) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "insert into staff (category,refdby,fname,mname,lname,gender,address,dob,mobile,tele,idproof,idno,doj,dol) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, staff.getCategory());
		preparedStatement.setString(2, staff.getRefdby());
		preparedStatement.setString(3, staff.getFname());
		preparedStatement.setString(4, staff.getMname());
		preparedStatement.setString(5, staff.getLname());
		preparedStatement.setString(6, staff.getGender());
		preparedStatement.setString(7, staff.getAddress());
		preparedStatement.setString(8, staff.getDob());
		preparedStatement.setLong(9, staff.getMobile());
		preparedStatement.setLong(10, staff.getTelephone());
		preparedStatement.setString(11, staff.getIdproof());
		preparedStatement.setString(12, staff.getIdno());
		preparedStatement.setString(13, staff.getDoj());
		preparedStatement.setString(14, null);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public ResultSet getStaffNameByCategory(String cat) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select concat(fname,' ',lname) from staff where category =? order by fname";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, cat);
		result = preparedStatement.executeQuery();
		return result;
	}

	// <-- Staff Table --

	// -- Menu Table -->
	public ResultSet getMenuByItem(String item) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select * from menu where item=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, item);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getMenuByPrice() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select * from menu order by price";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getMenuByCategory(String group) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select * from menu where itemgrp = ?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, group);
		result = preparedStatement.executeQuery();
		return result;
	}

	public int updateMenuById(String cat, String name, String id, float price) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "update menu set itemgrp =?,item=?,price=? where itemcode=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, cat);
		preparedStatement.setString(2, name);
		preparedStatement.setFloat(3, price);
		preparedStatement.setString(4, id);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public int deleteMenuById(String id) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "delete from menu where itemcode=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, id);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public ResultSet getItemNPriceByCode(int itemCode) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select item,price from menu where itemCode = ?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, itemCode);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getAllMenuItems() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select item from menu order by item";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getMenuItemsByCategory(String cat) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select item from menu where itemgrp =? order by itemcode";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, cat);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getMenuItemByCode(int code) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select item from menu where itemCode = ?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, code);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getItemCodeByItemName(String item) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select itemcode from menu where item=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, item);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getNextItemCode() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select max(itemcode) from menu;";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public int addNewItem(Menu menu) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "insert into menu values(?,?,?,?)";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, menu.getItemCode());
		preparedStatement.setString(2, menu.getItemGrp());
		preparedStatement.setString(3, menu.getMenuItem());
		preparedStatement.setFloat(4, menu.getPrice());
		result = preparedStatement.executeUpdate();
		return result;
	}
	// <-- Menu Table --

	// -- Order Table -->
	public ResultSet getLatestOrderNo() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select max(orderno) from orders where substring(ordplaced,1,10) = curdate() ";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getLiveOrderNo() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select distinct(orderno) from orders where status = 1 and substring(ordplaced,1,10) = curdate() order by orderno";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public int removeAllLiveOrdersForceClose() throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "delete from orders where status = 1 and substring(ordplaced,1,10) =  curdate();";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public ResultSet getServedOrderNo() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select distinct(orderno) from orders where status = 2 and substring(ordplaced,1,10) = curdate() order by orderno";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getCancelledOrderNo() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select distinct(orderno) from orders where status = 3 and substring(ordplaced,1,10) = curdate() order by orderno";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getCancelledOrderDetails(int orderNo, String date) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "";
		if (date.equals("curdate()")) {
			query = "select item,sum(qty),price,sum(amt) from orders where orderno =? and substring(ordplaced,1,10) = curdate() group by item order by ordplaced";
		} else {
			query = "select item,sum(qty),price,sum(amt) from orders where orderno =? and substring(ordplaced,1,10) = '"
					+ date + "' group by item order by ordplaced";
		}
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, orderNo);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getOrderByNo(int no, String orderDate) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "";
		if (orderDate.equals("curdate()")) {
			query = "select item,sum(qty),price,sum(amt) from orders where orderno = ? and substring(ordplaced,1,10) = curdate() group by item order by ordplaced";
		} else {
			query = "select item,sum(qty),price,sum(amt) from orders where orderno = ? and substring(ordplaced,1,10) = '"
					+ orderDate + "' group by item order by ordplaced";
		}
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, no);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getCancellationReasonByOrderno(int orderno, String date) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "";
		if (date.equals("curdate()")) {
			query = "select distinct(canReson) from orders where status = 3 and orderNo =? and substring(ordplaced,1,10) = curdate() group by item order by ordplaced;";
		} else {
			query = "select distinct(canReson) from orders where status = 3 and orderNo =? and substring(ordplaced,1,10) = '"
					+ date + "' group by item order by ordplaced;";
		}
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, orderno);
		result = preparedStatement.executeQuery();
		return result;
	}

	public int updateOrderByNo(float disc, int status, int no, float total, String paidBy) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "update orders set discapplied = ?,status =?,totalAmt=?,paidBy=? where orderNo =? and substring(ordplaced,1,10) = curdate();";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setFloat(1, disc);
		preparedStatement.setInt(2, status);
		preparedStatement.setFloat(3, total);
		preparedStatement.setString(4, paidBy);
		preparedStatement.setInt(5, no);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public int cancelOrderByNo(int status, int no, String canReson) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "update orders set status =?,canReson=? where orderNo =? and substring(ordplaced,1,10) = curdate();";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, status);
		preparedStatement.setString(2, canReson);
		preparedStatement.setInt(3, no);
		result = preparedStatement.executeUpdate();

		return result;
	}

	public ResultSet getOrderByStatAndDate(int stat, String dat) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;

		String query = "select orderNo,item,sum(qty),price,sum(amt),discapplied,totalAmt,ordplaced,statChngd,canReson,paidBy from orders where status = ? and substring(ordplaced,1,10) = ? group by item order by ordplaced";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, stat);
		preparedStatement.setString(2, dat);
		result = preparedStatement.executeQuery();
		return result;
	}

	public int addNewOrder(Orders order) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "insert into orders (orderno,item,price,qty,amt,status) values(?,?,?,?,?,?);";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, order.getorderNo());
		preparedStatement.setString(2, order.getItem());
		preparedStatement.setFloat(3, order.getPrice());
		preparedStatement.setFloat(4, order.getQty());
		preparedStatement.setFloat(5, order.getAmt());
		preparedStatement.setInt(6, order.getStatus());
		result = preparedStatement.executeUpdate();
		return result;
	}

	public int deleteItemFromOrder(int orderNo, String item) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "delete from orders where orderno=? and item=? and substring(ordplaced,1,10) = curdate()";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, orderNo);
		preparedStatement.setString(2, item);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public int incrementOrderNo(int orderno) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "insert into orderno values (?);";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, orderno);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public ResultSet getOrderByStatus(String status, Date date) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select * from orders where status =? and ordplaced=?";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getOrderDates() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select distinct(substring(ordplaced,1,10)) from orders order by ordplaced desc";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getDiscApplied(int orderNo, String date) throws SQLException {
		ResultSet res = null;
		String query = "";
		if (date.equals("curdate()")) {
			query = "select distinct(discapplied) from orders where orderno = ?   and substring(ordplaced,1,10) = curdate()";
		} else {
			query = "select distinct(discapplied) from orders where orderno = ?   and substring(ordplaced,1,10) ='"
					+ date + "'";
		}
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, orderNo);
		res = preparedStatement.executeQuery();
		return res;
	}

	public ResultSet getOrderDatesByStatus(String orderStatus) throws SQLException {
		String status = "";
		if (orderStatus.equals("Cancelled")) {
			status = "status in (3)";
		} else if (orderStatus.equals("Served")) {
			status = "status in (2)";
		}
		ResultSet result = null;
		preparedStatement = null;
		String query = "select distinct(substring(ordplaced,1,10)) from orders where " + status
				+ " order by ordplaced desc";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getOrderNosByDate(String orderDate, String orderStatus) throws SQLException {
		String status = "";
		if (orderStatus.equals("Cancelled")) {
			status = "status in (3)";
		} else if (orderStatus.equals("Served")) {
			status = "status in (2)";
		}
		ResultSet result = null;
		preparedStatement = null;
		String query = "select distinct(orderno) from orders where " + status + " and substring(ordplaced,1,10) ='"
				+ orderDate + "' order by orderno";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getOrderTimeStamp(int orderNo, String date) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "";
		if (date.equals("curdate()")) {
			query = "select distinct(ordplaced) from orders where orderno=? and substring(ordplaced,1,10) =curdate()  order by ordplaced desc";
		} else {
			query = "select distinct(ordplaced) from orders where orderno=? and substring(ordplaced,1,10) ='" + date
					+ "' order by ordplaced desc";
		}
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, orderNo);
		result = preparedStatement.executeQuery();
		return result;
	}

	public int updateOrderPaymentModeByNo(int no, String paidBy) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "update orders set paidBy=? where orderNo =? and substring(ordplaced,1,10) = curdate();";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, paidBy);
		preparedStatement.setInt(2, no);

		result = preparedStatement.executeUpdate();
		return result;
	}

	public ResultSet getOrderPaymentModeByNo(int no) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select paidBy from orders where orderNo =? and substring(ordplaced,1,10) = curdate();";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, no);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getTodaysAllOrdersByPaidBy(String paidBy) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select totalAmt from orders where paidBy = ? and substring(ordplaced,1,10) = curdate() group by orderno;";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, paidBy);
		result = preparedStatement.executeQuery();
		return result;
	}

	// <-- Order Table --

	// -- Discounts Table -->

	public ResultSet getAllDiscounts() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select disPercantage from discounts order by disPercantage";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getDiscounts() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select * from discounts";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public int addNewDiscount(float disc) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "insert into discounts(disPercantage) values (?);";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setFloat(1, disc);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public int updateDiscountByCode(int code, float discValue) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "update discounts set disPercantage = ? where disCode = ?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setFloat(1, discValue);
		preparedStatement.setInt(2, code);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public int deleteDiscountByCode(int code) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "delete from discounts where disCode = ?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, code);
		result = preparedStatement.executeUpdate();
		return result;
	}

	// <-- Discounts Table --

	// -- Customer Table -->

	public int addNewCustomer(Customer customer) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "insert into customer(fname,lname,gender,address,dob,email,mobile) values(?,?,?,?,?,?,?)";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, customer.getFname());
		preparedStatement.setString(2, customer.getLname());
		preparedStatement.setString(3, customer.getGender());
		preparedStatement.setString(4, customer.getAddress());
		preparedStatement.setString(5, customer.getDob());
		preparedStatement.setString(6, customer.getEmail());
		preparedStatement.setLong(7, customer.getMobile());
		result = preparedStatement.executeUpdate();
		return result;
	}

	public int updateCustomer(Customer customer) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "update customer set fname=?,lname=?,gender=?,address=?,dob=?,email=?,mobile=? where custid=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, customer.getFname());
		preparedStatement.setString(2, customer.getLname());
		preparedStatement.setString(3, customer.getGender());
		preparedStatement.setString(4, customer.getAddress());
		preparedStatement.setString(5, customer.getDob());
		preparedStatement.setString(6, customer.getEmail());
		preparedStatement.setLong(7, customer.getMobile());
		preparedStatement.setInt(8, customer.getCustId());
		result = preparedStatement.executeUpdate();
		return result;
	}

	public int deleteCustomerById(int custId) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "delete from customer where custid=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, custId);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public ResultSet getAllCustomer() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select * from customer";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	// <-- Customer Table --

	// -- User Table -->

	public boolean validateLogin(User user) throws SQLException {
		boolean validated = false;

		if (user.isAdmin()) {
			String query = "select * from user where username=? and password=? and isadmin = 1";
			preparedStatement = connect.prepareStatement(query);
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getPassword());
			ResultSet result = preparedStatement.executeQuery();
			if (result.next()) {
				validated = true;
			}
		} else {
			String query = "select * from user where username=? and password=? and isadmin = 0";
			preparedStatement = connect.prepareStatement(query);
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getPassword());
			ResultSet result = preparedStatement.executeQuery();
			if (result.next()) {
				validated = true;
			}
		}

		return validated;
	}

	public int addUser(User user) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "insert into user values(?,?,?)";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, user.getUsername());
		preparedStatement.setString(2, user.getPassword());
		preparedStatement.setBoolean(3, user.isAdmin());
		result = preparedStatement.executeUpdate();
		return result;
	}

	public int updateUserLastLogin(User user) throws SQLException {
		int result = 0;
		preparedStatement = null;
		java.sql.Timestamp date = new java.sql.Timestamp(user.getLastLogin().getTime());
		String query = "update user set lastlogin = ? where username =? and password=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setTimestamp(1, date);
		preparedStatement.setString(2, user.getUsername());
		preparedStatement.setString(3, user.getPassword());
		result = preparedStatement.executeUpdate();
		return result;
	}

	public String getLastLogin(User user) throws SQLException {
		String result = "";

		preparedStatement = null;
		String query = "select lastlogin from user where username=? and password =?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, user.getUsername());
		preparedStatement.setString(2, user.getPassword());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			result = rs.getTimestamp(1).toString();
		}

		return result;
	}

	public String getAdminName(User user) throws SQLException {
		String result = "";

		preparedStatement = null;
		String query = "select name from user where username=? and password =?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, user.getUsername());
		preparedStatement.setString(2, user.getPassword());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			result = rs.getString(1);
		}

		return result;
	}

	public String getManagerName(User user) throws SQLException {
		String result = "";

		preparedStatement = null;
		String query = "select name from user where username=? and password =?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, user.getUsername());
		preparedStatement.setString(2, user.getPassword());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			result = rs.getString(1);
		}

		return result;
	}

	public boolean validateOldPass(String username, String oldPass) throws SQLException {
		boolean validated = false;

		String query = "select * from user where username=? and password=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, username);
		preparedStatement.setString(2, oldPass);
		ResultSet result = preparedStatement.executeQuery();
		if (result.next()) {
			validated = true;
		}

		return validated;
	}

	public boolean setNewPassword(String username, String newPass) throws SQLException {
		boolean set = false;

		String query = "update user set password = ? where username=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, newPass);
		preparedStatement.setString(2, username);
		int result = preparedStatement.executeUpdate();
		if (result > 0) {
			set = true;
		}
		return set;
	}

	// <-- User Table --

	// -- Openingbal Table -->
	public int addOpeningBal(float amt) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "insert into openingbal values(now(),?)";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setFloat(1, amt);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public int resetOpeningBal() throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "delete from openingbal where substring(dat_time,1,10)=curdate();";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeUpdate();
		if (result > 0) {
			query = "delete from transactions where substring(txnTime,1,10)=curdate() and substring(descriptn,1,7) = 'Opening' and operationType = 1;";
			preparedStatement = connect.prepareStatement(query);
			result = preparedStatement.executeUpdate();
		}
		return result;
	}

	public ResultSet getOpeningBal() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select amt from openingbal where substring(dat_time,1,10)=curdate();";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}
	// <-- Openingbal Table --

	// -- maininventory Table -->

	public int addNewInventory(String item, int qty) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "insert into maininventory (item,qty) values(?,?)";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, item);
		preparedStatement.setInt(2, qty);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public int updateInventoryItem(String item, int qty, int itemid) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "update maininventory set item=?,qty=? where itemid=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, item);
		preparedStatement.setInt(2, qty);
		preparedStatement.setInt(3, itemid);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public int deleteInventoryItem(int itemid) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "delete from maininventory where itemid=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, itemid);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public ResultSet getMainInventoryItems() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select item,qty from maininventory order by item";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getMainInventory() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select * from maininventory";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getInventoryQtyByItem(String itm) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select qty from maininventory where item=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, itm);
		result = preparedStatement.executeQuery();
		return result;
	}

	public int updateInventoryQtyByItem(String itm, int newQty) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "update maininventory set qty =? where item=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, newQty);
		preparedStatement.setString(2, itm);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public int resetInventory() throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "update maininventory set qty =0";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeUpdate();
		return result;
	}

	// <-- maininventory Table --

	private void getDetails() {
		pass = (char) firstNameCode + "" + (char) lastNameCode + "" + (char) middleNameCode + "" + (char) addCode + ""
				+ (char) dobCode + 123;
	}

	// -- Bill Header -->

	public ResultSet getBillheader() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select restauname,restauadd from billheader where dat_time = (select max(dat_time) from billheader)";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getBillheaders() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select * from billheader order by dat_time desc";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public int updateBillheadersById(int id, String restauname, String restauadd) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "update billheader set restauname=?, restauadd=? where billheadId=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, restauname);
		preparedStatement.setString(2, restauadd);
		preparedStatement.setInt(3, id);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public int deleteBillheadersById(int id) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "delete from billheader where billheadId=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, id);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public int addNewBillheaders(String restauname, String restauadd) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "insert into billheader(restauname,restauadd) values(?,?)";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, restauname);
		preparedStatement.setString(2, restauadd);
		result = preparedStatement.executeUpdate();
		return result;
	}

	// <-- Bill Header --

	// -- Bill Footer -->

	public ResultSet getBillFooter() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select line1,line2 from billfooter where dat_time = (select max(dat_time) from billfooter)";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getBillFooters() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select * from billfooter order by dat_time desc";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public int updateBillFooterById(int id, String line1, String line2) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "update billfooter set line1=?, line2=? where billfootId=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, line1);
		preparedStatement.setString(2, line2);
		preparedStatement.setInt(3, id);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public int deleteBillFooterById(int id) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "delete from billfooter where billfootId=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, id);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public int addNewBillFooter(String line1, String line2) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "insert into billfooter(line1,line2) values(?,?)";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, line1);
		preparedStatement.setString(2, line2);
		result = preparedStatement.executeUpdate();
		return result;
	}

	// <-- Bill Footer --

	// -- Attendance -->

	public int addorUpdateAttendance(String cat, String fnamelname, String inTime, String outTime, String availability)
			throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = null;
		if (availability.equals("A")) {
			query = "insert into attendance (category,fnamelname,availability) values (?,?,?);";
			preparedStatement = connect.prepareStatement(query);
			preparedStatement.setString(1, cat);
			preparedStatement.setString(2, fnamelname);
			preparedStatement.setString(3, availability);
		} else if (outTime == null) {
			query = "insert into attendance (category,fnamelname,inTime,availability) values (?,?,?,?);";
			preparedStatement = connect.prepareStatement(query);
			preparedStatement.setString(1, cat);
			preparedStatement.setString(2, fnamelname);
			preparedStatement.setString(3, inTime);
			preparedStatement.setString(4, availability);
		} else if (inTime == null) {
			query = "update attendance set outTime = ? where substring(intimeActual,1,10) = curdate()  and fnamelname =? and category=?";
			preparedStatement = connect.prepareStatement(query);
			preparedStatement.setString(1, outTime);
			preparedStatement.setString(2, fnamelname);
			preparedStatement.setString(3, cat);
		}
		result = preparedStatement.executeUpdate();

		return result;
	}

	public ResultSet getAttendanceOverview() throws SQLException {
		ResultSet res = null;
		String query = "select fnamelname,intime,outtime,availability from attendance where substring(intimeActual,1,10) = curdate() ";
		preparedStatement = connect.prepareStatement(query);
		res = preparedStatement.executeQuery();
		return res;
	}

	// <-- Attendance --

	// -- Taxes -->

	public int addNewlyDefinedTax(String taxDesc, String taxShrtNm, String taxPerc) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "insert into taxes (taxFullName,taxShortName,taxPercantage)values (?,?,?);";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, taxDesc);
		preparedStatement.setString(2, taxShrtNm);
		preparedStatement.setFloat(3, Float.parseFloat(taxPerc));
		result = preparedStatement.executeUpdate();
		return result;
	}

	public ResultSet getAllTaxes() throws SQLException {
		ResultSet res = null;
		String query = "select taxShortName,taxPercantage from taxes";
		preparedStatement = connect.prepareStatement(query);
		res = preparedStatement.executeQuery();
		return res;
	}

	public ResultSet getTaxes() throws SQLException {
		ResultSet res = null;
		String query = "select * from taxes";
		preparedStatement = connect.prepareStatement(query);
		res = preparedStatement.executeQuery();
		return res;
	}

	public int updateTaxById(int id, String taxFull, String taxShort, float taxPerc) throws SQLException {
		int res = 0;
		String query = "update taxes set taxFullName=?,taxShortName=?,taxPercantage=? where taxid=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, taxFull);
		preparedStatement.setString(2, taxShort);
		preparedStatement.setFloat(3, taxPerc);
		preparedStatement.setInt(4, id);
		res = preparedStatement.executeUpdate();
		return res;
	}

	public int deleteTaxById(int id) throws SQLException {
		int res = 0;
		String query = "delete from taxes where taxid=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, id);
		res = preparedStatement.executeUpdate();
		return res;
	}

	// <-- Taxes --

	// -- Expense Table-->

	public ResultSet getLastestExpNoForToday() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select max(expNo) from expenses where substring(expTime,1,10) = curdate()";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public int addNewExpense(int expNo, String reason, String amnt) throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "insert into expenses (expNo,reason,amount)values (?,?,?);";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, expNo);
		preparedStatement.setString(2, reason);
		preparedStatement.setFloat(3, Float.parseFloat(amnt));
		result = preparedStatement.executeUpdate();
		return result;
	}

	// <-- Expense Table --

	// -- Transactions Table -->
	public ResultSet getAllDepositAmount() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select amount from transactions where operationType = 1 and substring(txnTime,1,10) = curdate() group by descriptn ;";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getAllWithdrawnAmount() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select amount from transactions where operationType = 2 and substring(txnTime,1,10) = curdate() group by descriptn ;";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	// <-- Transactions Table --

	// -- EOD Queries-->
	public ResultSet getTodaysExpense() throws SQLException {
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select * from expenses where substring(expTime,1,10) = curdate();";
		preparedStatement = connect.prepareStatement(query);
		rs = preparedStatement.executeQuery();
		return rs;
	}

	public ResultSet getTodaysInventory() throws SQLException {
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select item,qty from maininventory order by item";
		preparedStatement = connect.prepareStatement(query);
		rs = preparedStatement.executeQuery();
		return rs;
	}

	public ResultSet getTodaysTransactions() throws SQLException {
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select * from transactions where substring(txnTime,1,10) = curdate() group by descriptn order by txnTime;";
		preparedStatement = connect.prepareStatement(query);
		rs = preparedStatement.executeQuery();
		return rs;
	}

	public ResultSet getTodaysAttendance() throws SQLException {
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select * from attendance where substring(intimeActual,1,10) = curdate() order by category";
		preparedStatement = connect.prepareStatement(query);
		rs = preparedStatement.executeQuery();
		return rs;
	}

	public ResultSet getServedOrderEODDetails(int orderNo) throws SQLException {
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select orderno,item,sum(qty),price,sum(amt),discapplied,totalAmt,ordplaced,statChngd,paidBy from orders where orderno = ? and status=2 and substring(ordplaced,1,10) = curdate() group by item order by ordplaced";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, orderNo);
		rs = preparedStatement.executeQuery();
		return rs;
	}

	public ResultSet getCancelledOrderEODDetails(int orderNo) throws SQLException {
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select orderno,item,sum(qty),price,sum(amt),ordplaced,statChngd,canReson from orders where orderno = ? and status=3 and substring(ordplaced,1,10) = curdate() group by item order by ordplaced";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, orderNo);
		rs = preparedStatement.executeQuery();
		return rs;
	}

	public int closeTheStore() throws SQLException {
		int result = 0;
		preparedStatement = null;
		String query = "insert into storeclosed values (now());";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeUpdate();
		return result;
	}

	public ResultSet isStoreClosed() throws SQLException {
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select * from storeclosed where substring(dat_time,1,10) = curdate()";
		preparedStatement = connect.prepareStatement(query);
		rs = preparedStatement.executeQuery();
		return rs;
	}

	public int forceReopenStore() throws SQLException {
		int rs = 0;
		preparedStatement = null;
		String query = "delete from storeclosed where substring(dat_time,1,10) = curdate()";
		preparedStatement = connect.prepareStatement(query);
		rs = preparedStatement.executeUpdate();
		return rs;
	}

	// <-- EOD Queries--

	// -- Reports Queries -->

	public ResultSet getRangedAttendanceRpt(String fromDate, String toDate) throws SQLException {
		// String fromDate = "YYYY-MM-DD";
		// String toDate = "YYYY-MM-DD";
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select * from attendance where intimeActual between '" + fromDate + " 00:00:00' and '" + toDate
				+ " 23:59:00' order by intimeActual; ";
		preparedStatement = connect.prepareStatement(query);
		rs = preparedStatement.executeQuery();
		return rs;
	}

	public ResultSet getRangedExpenseRpt(String fromDate, String toDate) throws SQLException {
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select * from expenses where expTime between '" + fromDate + " 00:00:00' and '" + toDate
				+ " 23:59:00' order by expTime; ";
		preparedStatement = connect.prepareStatement(query);
		rs = preparedStatement.executeQuery();
		return rs;
	}

	public ResultSet getRangedInventory(String fromDate, String toDate) throws SQLException {
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select * from maininventory order by item";
		preparedStatement = connect.prepareStatement(query);
		rs = preparedStatement.executeQuery();
		return rs;
	}

	public ResultSet getServedOrderNoByDate(String date) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select distinct(orderno) from orders where status = 2 and substring(ordplaced,1,10) = '" + date
				+ "' order by orderno";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getServedOrderDetailsByDate(int orderNo, String date) throws SQLException {
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select orderno,item,sum(qty),price,sum(amt),discapplied,totalAmt,ordplaced,statChngd,paidBy from orders where orderno = ? and status=2 and substring(ordplaced,1,10) = '"
				+ date + "' group by item order by ordplaced";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, orderNo);
		rs = preparedStatement.executeQuery();
		return rs;
	}

	public ResultSet getCancelledOrderNoByDate(String date) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select distinct(orderno) from orders where status = 3 and substring(ordplaced,1,10) = '" + date
				+ "' order by orderno";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getCancelledOrderByDate(int orderNo, String date) throws SQLException {
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select orderno,item,sum(qty),price,sum(amt),ordplaced,statChngd,canReson from orders where orderno = ? and status=3 and substring(ordplaced,1,10) = '"
				+ date + "' group by item order by ordplaced";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, orderNo);
		rs = preparedStatement.executeQuery();
		return rs;
	}

	public ResultSet getTodaysOpeningBal() throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select * from openingbal where substring(dat_time,1,10)=curdate();";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getRangedOpeningBal(String fromDate, String toDate) throws SQLException {
		ResultSet result = null;
		preparedStatement = null;
		String query = "select * from openingbal where dat_time between '" + fromDate + " 00:00:00' and '" + toDate
				+ " 23:59:00' order by dat_time";
		preparedStatement = connect.prepareStatement(query);
		result = preparedStatement.executeQuery();
		return result;
	}

	public ResultSet getRangedTransactions(String fromDate, String toDate) throws SQLException {
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select * from transactions where txnTime between '" + fromDate + " 00:00:00' and '" + toDate
				+ " 23:59:00' group by descriptn order by txnTime;";
		preparedStatement = connect.prepareStatement(query);
		rs = preparedStatement.executeQuery();
		return rs;
	}

	// <-- Reports Queries --

	// -- authcred Quries-->
	public ResultSet getAuthCread() throws SQLException {
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select * from authcred";
		preparedStatement = connect.prepareStatement(query);
		rs = preparedStatement.executeQuery();
		return rs;
	}
	// <-- authcred Quries--

	// -- toEmailList Quries-->
	public ResultSet getToEmailList() throws SQLException {
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select emailAdd from toEmailList";
		preparedStatement = connect.prepareStatement(query);
		rs = preparedStatement.executeQuery();
		return rs;
	}

	public ResultSet getToEmail() throws SQLException {
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select * from toEmailList";
		preparedStatement = connect.prepareStatement(query);
		rs = preparedStatement.executeQuery();
		return rs;
	}

	public int updateEmailAddById(int id, String emailAdd) throws SQLException {
		int rs = 0;
		preparedStatement = null;
		String query = "update toEmailList set emailAdd=? where emailid=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, emailAdd);
		preparedStatement.setInt(2, id);
		rs = preparedStatement.executeUpdate();
		return rs;
	}

	public int deleteEmailAddById(int id) throws SQLException {
		int rs = 0;
		preparedStatement = null;
		String query = "delete from toEmailList where emailid=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setInt(1, id);
		rs = preparedStatement.executeUpdate();
		return rs;
	}

	public int addNewEmailAdd(String emailAdd) throws SQLException {
		int rs = 0;
		preparedStatement = null;
		String query = "insert into toEmailList (emailAdd) values(?)";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, emailAdd);
		rs = preparedStatement.executeUpdate();
		return rs;
	}

	// <-- toEmailList Quries--

	// -- fromEmailList Quries-->
	public ResultSet getfromEmailAuth() throws SQLException {
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select emailAdd,emailPass from fromEmailList";
		preparedStatement = connect.prepareStatement(query);
		rs = preparedStatement.executeQuery();
		return rs;
	}

	public ResultSet getfromEmail() throws SQLException {
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select * from fromEmailList";
		preparedStatement = connect.prepareStatement(query);
		rs = preparedStatement.executeQuery();
		return rs;
	}

	public int updatefromEmailAddById(int id, String emailAdd, String emailPass) throws SQLException {
		int rs = 0;
		preparedStatement = null;
		String query = "update fromEmailList set emailAdd=?,emailPass=? where emailid=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, emailAdd);
		preparedStatement.setString(2, emailPass);
		preparedStatement.setInt(3, id);
		rs = preparedStatement.executeUpdate();
		return rs;
	}

	// <-- fromEmailList Quries--

	// -- repFilePath Quries-->
	public ResultSet getFilePath(String id) throws SQLException {
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select fullpath from filepath where id = ?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, id);
		rs = preparedStatement.executeQuery();
		return rs;
	}
	// <-- repFilePath Quries--

	// -- storeclosed Quries-->
	public ResultSet getStoreClosedDetails() throws SQLException {
		ResultSet rs = null;
		preparedStatement = null;
		String query = "select * from storeclosed order by dat_time desc";
		preparedStatement = connect.prepareStatement(query);
		rs = preparedStatement.executeQuery();
		return rs;
	}
	// <-- storeclosed Quries--

	public void closeConnection() throws SQLException {
		connect.close();
	}

	private int middleNameCode = 0000;
	private int firstNameCode = 0000;
	private int dobCode = 0000;
	private int addCode = 0000;
	private int addCode = 0000;
	private int lastNameCode = 0000;
	private String pass = "xxxxx";
	private String base = "xxxxx";
	private String unicode = "xxxxx";

}