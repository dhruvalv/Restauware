package com.dhruval.restauware.reports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dhruval.restauware.utilities.RestauwareUtility;

public class OrdersReport {

	private BufferedWriter out;
	private String servedOrderTable,cancelledOrderTable;
	private String style = "<style>#report {    font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif;    border-collapse: collapse;    width: 100%;}#report td, #report th {    border: 1px solid #ddd;    padding: 8px;}#report tr:nth-child(even){background-color: #f2f2f2;}#report tr:hover {background-color: #ddd;}#report th {    padding-top: 12px;    padding-bottom: 12px;    text-align: left;    background-color: #4CAF50;    color: white;}</style>";
	private String filePath="";
	private RestauwareUtility utility = new RestauwareUtility();
	
	public BufferedWriter createReport() throws IOException{
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
		filePath =utility.getRepPath();
		File file = new File(filePath+"Orders_"+dateFormat.format(date) + ".html");
		out = new BufferedWriter(new FileWriter(file));
		out.write("<html><head><title>"+dateFormat.format(date)+"</title>"+style+"<head><body bgcolor = 'white'>");
		return out;
	}
	
	public void servedOrderTableHeader()throws IOException{
		servedOrderTable = "<table id = report><caption>Served Orders</caption><tr><th>Order No</th><th>Item</th><th>Quantity</th><th>Price per Item</th><th>Amount</th><th>Discount Applied</th><th>Bill Amount</th><th>Order Placed at</th><th>Bill Generated at</th><th>Paid By</th></tr>";
	}
	

	public void  servedOrderTableContent(BufferedWriter o,String line)throws IOException{
		this.out = o;
		servedOrderTable =  servedOrderTable + line;
	}
	
	public void  servedOrderTableComplete()throws IOException{
		servedOrderTable =  servedOrderTable + "</table>";
		out.write(servedOrderTable);
		addExtraNewLines();
	}
	
	public void cancelledOrderTableHeader()throws IOException{
		cancelledOrderTable = "<table id = report><caption>Cancelled Orders</caption><tr><th>Order No</th><th>Item</th><th>Quantity</th><th>Price per Item</th><th>Amount</th><th>Order Placed at</th><th>Order Cancelled at</th><th>Cancellation Reason</th></tr>";
	}
	

	public void  cancelledOrderTableContent(BufferedWriter o,String line)throws IOException{
		this.out = o;
		cancelledOrderTable =  cancelledOrderTable + line;
	}
	
	public void cancelledOrderTableComplete()throws IOException{
		cancelledOrderTable =  cancelledOrderTable + "</table>";
		out.write(cancelledOrderTable);
		addExtraNewLines();
		closeBufferedWriter();
		new RestauwareUtility().showAleart("Order Report generated successfully.");
	}

	public void addExtraNewLines() throws IOException{
		out.write("<br><br><hr style='height:2px;border:none;color:#333;background-color:#333;'><br><br>");
	}

	public void closeBufferedWriter() throws IOException {
		out.write("</body></html>");
		out.close();
	}
}
