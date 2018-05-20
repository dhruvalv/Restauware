package com.dhruval.restauware.reports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dhruval.restauware.utilities.RestauwareUtility;

public class AttendanceReport {

	private BufferedWriter out;
	private String attendanceTable;
	private String style = "<style>#report {    font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif;    border-collapse: collapse;    width: 100%;}#report td, #report th {    border: 1px solid #ddd;    padding: 8px;}#report tr:nth-child(even){background-color: #f2f2f2;}#report tr:hover {background-color: #ddd;}#report th {    padding-top: 12px;    padding-bottom: 12px;    text-align: left;    background-color: #4CAF50;    color: white;}</style>";
	private String filePath = "";
	private RestauwareUtility utility = new RestauwareUtility();

	public BufferedWriter createReport() throws IOException {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
		filePath = utility.getRepPath();
		File file = new File(filePath + "Attendance_" + dateFormat.format(date) + ".html");
		out = new BufferedWriter(new FileWriter(file));
		out.write("<html><head><title>" + dateFormat.format(date) + "</title>" + style
				+ "<head><body bgcolor = 'white'>");
		return out;
	}

	public void attendanceTableHeader() throws IOException {
		attendanceTable = "<table id = report><caption>Attendance Report</caption><tr><th>Category</th><th>Name</th><th>Recorded-InTime</th><th>Recorded-OutTime</th><th>Hours Worked</th><th>Availability</th><th>Actual-InTime</th><th>Actual-OutTime</th></tr>";
	}

	public void attendanceTableContent(BufferedWriter o, String line) throws IOException {
		this.out = o;
		attendanceTable = attendanceTable + line;
	}

	public void attendanceTableComplete() throws IOException {
		attendanceTable = attendanceTable + "</table>";
		out.write(attendanceTable);
		addExtraNewLines();
		closeBufferedWriter();
		new RestauwareUtility().showAleart("Attendance Report generated successfully.");
	}

	public void addExtraNewLines() throws IOException {
		out.write("<br><br><br>");
	}

	public void closeBufferedWriter() throws IOException {
		out.write("</body></html>");
		out.close();
	}
}
