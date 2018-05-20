package com.dhruval.restauware.utilities;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import org.apache.log4j.Logger;

import com.dhruval.restauware.db.DBConnection;

public class RestauwareUtility {
	private static String repPath;
	private String path;
	private String year;
	private String month;
	private String date;
	private DBConnection db;
	private static Logger log = Logger.getLogger(RestauwareUtility.class.getName());

	public void showAleart(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

	public int showConfirmation(String message, String title) {
		return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
	}

	public void showTimerAlert(String message, String title) {
		final JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.DEFAULT_OPTION, null, new Object[] {}, null);

		final JDialog dialog = new JDialog();
		dialog.setTitle(title);
		dialog.setModal(true);
		dialog.setSize(250, 150);
		dialog.setContentPane(optionPane);
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - dialog.getWidth()) / 2;
		final int y = (screenSize.height - dialog.getHeight()) / 2;
		dialog.setLocation(x, y);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.pack();

		Timer timer = new Timer(2000, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				dialog.dispose();
			}
		});
		timer.setRepeats(false);
		timer.start();
		dialog.setVisible(true);
	}

	public String getReportPath() {
		log.info("Started");
		String path = "C:\\Users\\Public\\";
		int flag = 0;
		try {
			db = new DBConnection();
			ResultSet rs = db.getFilePath("REPORT");
			while (rs.next()) {
				flag = 1;
				path = rs.getString(1);
				break;
			}
			if (flag == 0) {
				showAleart("No file path found in the system.");
			}

		} catch (Exception e) {
			showAleart("Error, while fetching Report File path due to : " + e.getMessage());
			log.error("Exception",e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception",e);
			}
		}
		log.info("Completed");
		return path;
	}

		public String getReceiptPath() {
		log.info("Started");
		String path = "C:\\Users\\Public\\";
		int flag = 0;
		try {
			db = new DBConnection();
			ResultSet rs = db.getFilePath("RECEIPT");
			while (rs.next()) {
				flag = 1;
				path = rs.getString(1);
				break;
			}
			if (flag == 0) {
				showAleart("No file path found in the system.");
			}
		} catch (Exception e) {
			showAleart("Error, while fetching Receipt File path due to : " + e.getMessage());
			log.error("Exception",e);
		}finally{
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception",e);
			}
		}
		log.info("Completed");
		return path;
	}

	public String getLog4jPath() {
		
		String path = "C:\\Users\\Public\\";
		int flag = 0;
		try {
			db = new DBConnection();
			ResultSet rs = db.getFilePath("LOG4J");
			while (rs.next()) {
				flag = 1;
				path = rs.getString(1);
				break;
			}
			if (flag == 0) {
				showAleart("No file path found in the system.");
			}
		} catch (Exception e) {
			showAleart("Error, while fetching log4j file path due to : " + e.getMessage());
			log.error("Exception",e);
		} finally{
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception",e);
			}
		}
		
		return path;
	}

	public boolean createDirectory() {
		log.info("Started");
		boolean created = false;
		path = getReportPath();
		year = new SimpleDateFormat("yyyy").format(new Date());
		month = new SimpleDateFormat("MM").format(new Date());
		date = new SimpleDateFormat("dd").format(new Date());
		try {
			File yrDir = new File(path + "\\" + year);
			if (!yrDir.exists()) {
				yrDir.mkdir();
			}

			File monDir = new File(path + "\\" + year + "\\" + month);
			if (!monDir.exists()) {
				monDir.mkdir();
			}

			File dayDir = new File(path + "\\" + year + "\\" + month + "\\" + date);
			if (!dayDir.exists()) {
				dayDir.mkdir();
			}
			repPath = path + year + "\\" + month + "\\" + date + "\\";
			created = true;
		} catch (Exception e) {
			showAleart("Error, creating directory due to : " + e.getMessage());
			log.error("Exception",e);
		}
		log.info("Comleted");
		return created;
	}

	public boolean createDirectory(String path) {
		log.info("Started");
		boolean created = false;
		year = new SimpleDateFormat("yyyy").format(new Date());
		month = new SimpleDateFormat("MM").format(new Date());
		date = new SimpleDateFormat("dd").format(new Date());
		try {
			File yrDir = new File(path + "\\" + year);
			if (!yrDir.exists()) {
				yrDir.mkdir();
			}

			File monDir = new File(path + "\\" + year + "\\" + month);
			if (!monDir.exists()) {
				monDir.mkdir();
			}

			File dayDir = new File(path + "\\" + year + "\\" + month + "\\" + date);
			if (!dayDir.exists()) {
				dayDir.mkdir();
			}
			repPath = path + year + "\\" + month + "\\" + date + "\\";
			created = true;
		} catch (Exception e) {
			showAleart("Error while creating directory with path due to : " + e.getMessage());
			log.error("Exception",e);
		}
		log.info("Completed");
		return created;
	}

	public String getRepPath() {
		return repPath;
	}

}
