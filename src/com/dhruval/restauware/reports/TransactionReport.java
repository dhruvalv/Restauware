package com.dhruval.restauware.reports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dhruval.restauware.utilities.RestauwareUtility;

public class TransactionReport {

	private BufferedWriter out;
	private String txnTable;
	private String style = "<style>#report {    font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif;    border-collapse: collapse;    width: 100%;}#report td, #report th {    border: 1px solid #ddd;    padding: 8px;}#report tr:nth-child(even){background-color: #f2f2f2;}#report tr:hover {background-color: #ddd;}#report th {    padding-top: 12px;    padding-bottom: 12px;    text-align: left;    background-color: #4CAF50;    color: white;}</style>";
	private String filePath="";
	private RestauwareUtility utility = new RestauwareUtility();
	
	public BufferedWriter createReport() throws IOException{
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
		filePath = utility.getRepPath();
		File file = new File(filePath+"Transaction_"+dateFormat.format(date) + ".html");
		out = new BufferedWriter(new FileWriter(file));
		out.write("<html><head><title>"+dateFormat.format(date)+"</title>"+style+"<head><body bgcolor = 'white'>");
		return out;
	}
	
	public void transactionTableHeader()throws IOException{
		txnTable = "<table id = report><caption>Today's Transactions</caption><tr><th>Description</th><th>Amount</th><th>Operation</th><th>Time</th></tr>";
	}
	
	public void transactionTableContent(BufferedWriter o,String line)throws IOException{
		this.out = o;
		txnTable = txnTable + line;
	}
	
	public void transactionTableComplete()throws IOException{
		txnTable = txnTable + "</table>";
		out.write(txnTable);
		addExtraNewLines();
		closeBufferedWriter();
		new RestauwareUtility().showAleart("Transaction Report generated successfully.");
	}
	
	public void addExtraNewLines() throws IOException{
		out.write("<br><br><br>");
	}
	
	public void closeBufferedWriter() throws IOException{
		out.write("</body></html>");
		out.close();
	}
}
