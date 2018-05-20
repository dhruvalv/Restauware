package com.dhruval.restauware.reports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dhruval.restauware.utilities.RestauwareUtility;

public class CreatEODReport {

	private BufferedWriter out;
	private String expenseTable, inventoryTable, attendanceTable, transactionsTable, servedOrderTable,
			cancelledOrderTable;
	private String style = "<style>#report {    font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif;    border-collapse: collapse;    width: 100%;}#report td, #report th {    border: 1px solid #ddd;    padding: 8px;}#report tr:nth-child(even){background-color: #f2f2f2;}#report tr:hover {background-color: #ddd;}#report th {    padding-top: 12px;    padding-bottom: 12px;    text-align: left;    background-color: #4CAF50;    color: white;}</style>";
	private String filePath = "";
	private RestauwareUtility utility = new RestauwareUtility();
	private String totalBusinessTable;

	public BufferedWriter createReport() throws IOException {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
		filePath = utility.getRepPath();
		File file = new File(filePath + "EOD_" + dateFormat.format(date) + ".html");
		out = new BufferedWriter(new FileWriter(file));
		out.write("<html><head><title>" + dateFormat.format(date) + "</title>" + style
				+ "<head><body bgcolor = 'white'>");
		return out;
	}

	public boolean openingBalanceTable(BufferedWriter o, String line) throws IOException {
		this.out = o;
		String table = "<table id = report><caption>Today's Opening Balance</caption><tr><th>Opening Balance</th></tr><tr><td>";
		table = table + line + "</td></tr></table>";
		out.write(table);
		addExtraNewLines();
		return true;
	}

	public void expensesTableHeader() throws IOException {
		expenseTable = "<table id = report><caption>Today's Expenses</caption><tr><th>Expense No</th><th>Reason</th><th>Amount</th><th>Time</th></tr>";
	}

	public void expensesTableContent(BufferedWriter o, String line) throws IOException {
		this.out = o;
		expenseTable = expenseTable + line;
	}

	public void expensesTableComplete() throws IOException {
		expenseTable = expenseTable + "</table>";
		out.write(expenseTable);
		addExtraNewLines();
	}

	public void inventoryTableHeader() throws IOException {
		inventoryTable = "<table id = report><caption>Today's Inventory</caption><tr><th>Item</th><th>Quantity</th></tr>";
	}

	public void inventoryTableContent(BufferedWriter o, String line) throws IOException {
		this.out = o;
		inventoryTable = inventoryTable + line;
	}

	public void inventoryTableComplete() throws IOException {
		inventoryTable = inventoryTable + "</table>";
		out.write(inventoryTable);
		addExtraNewLines();
	}

	public void attendanceTableHeader() throws IOException {
		attendanceTable = "<table id = report><caption>Today's Attendance</caption><tr><th>Category</th><th>Name</th><th>Recorded-InTime</th><th>Recorded-OutTime</th><th>Hours Worked</th><th>Availability</th><th>Actual-InTime</th><th>Actual-OutTime</th></tr>";
	}

	public void attendanceTableContent(BufferedWriter o, String line) throws IOException {
		this.out = o;
		attendanceTable = attendanceTable + line;
	}

	public void attendanceTableComplete() throws IOException {
		attendanceTable = attendanceTable + "</table>";
		out.write(attendanceTable);
		addExtraNewLines();
	}

	public void transactionTableHeader() throws IOException {
		transactionsTable = "<table id = report><caption>Today's Transactions</caption><tr><th>Description</th><th>Amount</th><th>Operation</th><th>Time</th></tr>";
	}

	public void transactionTableContent(BufferedWriter o, String line) throws IOException {
		this.out = o;
		transactionsTable = transactionsTable + line;
	}

	public void transactionTableComplete() throws IOException {
		transactionsTable = transactionsTable + "</table>";
		out.write(transactionsTable);
		addExtraNewLines();
	}

	public void servedOrderTableHeader() throws IOException {
		servedOrderTable = "<table id = report><caption>Today's Served Orders</caption><tr><th>Order No</th><th>Item</th><th>Quantity</th><th>Price per Item</th><th>Amount</th><th>Discount Applied</th><th>Bill Amount</th><th>Order Placed at</th><th>Bill Generated at</th><th>Paid By</th></tr>";
	}

	public void servedOrderTableContent(BufferedWriter o, String line) throws IOException {
		this.out = o;
		servedOrderTable = servedOrderTable + line;
	}

	public void servedOrderTableComplete() throws IOException {
		servedOrderTable = servedOrderTable + "</table>";
		out.write(servedOrderTable);
		addExtraNewLines();
	}

	public void cancelledOrderTableHeader() throws IOException {
		cancelledOrderTable = "<table id = report><caption>Today's Cancelled Orders</caption><tr><th>Order No</th><th>Item</th><th>Quantity</th><th>Price per Item</th><th>Amount</th><th>Order Placed at</th><th>Order Cancelled at</th><th>Cancellation Reason</th></tr>";
	}

	public void cancelledOrderTableContent(BufferedWriter o, String line) throws IOException {
		this.out = o;
		cancelledOrderTable = cancelledOrderTable + line;
	}

	public void cancelledOrderTableComplete() throws IOException {
		cancelledOrderTable = cancelledOrderTable + "</table>";
		out.write(cancelledOrderTable);
		addExtraNewLines();
	}

	public void totalBusinessTableHeader() throws IOException {
		totalBusinessTable = "<table id = report><caption>Today's Total Business</caption><tr><th>Total Deposit</th><th>Total Withdrawal</th><th>Business done</th></tr>";
	}

	public void totalBusinessTableContent(BufferedWriter o, String line) throws IOException {
		this.out = o;
		totalBusinessTable = totalBusinessTable + line;
	}

	public void totalBusinessTableComplete() throws IOException {
		totalBusinessTable = totalBusinessTable + "</table>";
		out.write(totalBusinessTable);
		addExtraNewLines();
	}

	public void addExtraNewLines() throws IOException {
		out.write("<br><br><hr style='height:2px;border:none;color:#333;background-color:#333;'><br><br>");
	}

	public void closeBufferedWriter() throws IOException {
		out.write("</body></html>");
		out.close();
	}
}
