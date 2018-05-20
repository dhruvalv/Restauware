package com.dhruval.restauware.email;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.dhruval.restauware.db.DBConnection;
import com.dhruval.restauware.utilities.RestauwareUtility;

public class SendingEODReport {

	private String fileName = "";
	private String fullName = "";
	private RestauwareUtility utility;
	private DBConnection db;
	private String username;
	private String password;
	private String toList = "";
	private String bodyText = "Dear Admin,\n\nEnclosed is EOD report sent by your store manager as a part of EOD process.\n\nRegards,\nStore.";
	static Logger log = Logger.getLogger(SendingEODReport.class.getName());

	public SendingEODReport() {
		utility = new RestauwareUtility();
		utility.createDirectory();
	}

	private boolean getFiles() {
		log.info("Started");
		boolean fileFound = false;

		fileName = "EOD_" + new SimpleDateFormat("ddMMyyyy").format(new Date());
		fullName = utility.getRepPath();
		fullName = fullName.replaceAll("\\\\", "/");

		File folder = new File(fullName);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> files = new ArrayList<>();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String getName = listOfFiles[i].getName();
				if (getName.startsWith(fileName)) {
					files.add(getName.substring(getName.lastIndexOf("_") + 1, getName.indexOf('.')));
					fileFound = true;
				}
			}
		}
		if (fileFound) {
			Collections.sort(files);
			Collections.reverse(files);
			fullName = fullName + fileName + "_" + files.get(0) + ".html"; // 
		}
		log.info("Completed");
		return fileFound;
	}

	public boolean sendingReports() {
		log.info("Started");
		boolean fileFound = getFiles();
		boolean sent = false;
		if (fileFound) {
			boolean authCred = getAuthCread();
			if (authCred) {
				boolean toList = getToEmailList();
				if (toList) {
					sent = sendReport();
				} else {
					utility.showAleart("No receiver email address is available in the system.");
				}
			} else {
				utility.showAleart("No User available in the system to send email");
			}
		} else {
			utility.showAleart("No File found for attachment");
		}
		log.info("Completed");
		return sent;
	}

	private boolean getToEmailList() {
		log.info("Started");
		boolean res = false;
		try {
			db = new DBConnection();
			ResultSet rs = db.getToEmailList();
			while (rs.next()) {
				toList = toList + rs.getString(1);
				res = true;
				break;
			}
		} catch (Exception e) {
			utility.showAleart("Error while getting receivers ids to send email, due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception",e);
			}
		}
		log.info("Completed");
		return res;
	}

	private boolean getAuthCread() {
		log.info("Started");
		boolean res = false;
		try {
			db = new DBConnection();
			ResultSet rs = db.getfromEmailAuth();
			while (rs.next()) {
				username = rs.getString(1);
				password = rs.getString(2);
				res = true;
				break;
			}
		} catch (Exception e) {
			utility.showAleart("Error while getting user to send email, due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception",e);
			}
		}
		log.info("Completed");
		return res;
	}

	private boolean sendReport() {
		log.info("Started");
		boolean res = false;
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			InternetAddress fromAddress = new InternetAddress(username);

			db = new DBConnection();
			ResultSet rs = db.getToEmailList();
			int size = 0;
			while (rs.next()) {
				size++;
			}
			rs.beforeFirst();
			
			String to[] = new String[size];
			for (int i = 0; rs.next(); i++) {
				to[i] = rs.getString(1);
			}
			
			InternetAddress[] address = new InternetAddress[to.length];
			for (int i = 0; i < to.length; i++) {
				address[i] = new InternetAddress(to[i]);
			}

			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(fromAddress);
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject("EOD Report for the Date : " + new SimpleDateFormat("dd-MMM-yyyy").format(new Date()));
			msg.setSentDate(new Date());

			// Set the email msg text.
			MimeBodyPart messagePart = new MimeBodyPart();
			messagePart.setText(bodyText);

			// Set the email attachment file
			FileDataSource fileDataSource = new FileDataSource(fullName);

			MimeBodyPart attachmentPart = new MimeBodyPart();
			attachmentPart.setDataHandler(new DataHandler(fileDataSource));
			attachmentPart.setFileName(fileDataSource.getName());

			// Create Multipart E-Mail.
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messagePart);
			multipart.addBodyPart(attachmentPart);

			msg.setContent(multipart);
			Transport.send(msg);
			res = true;
		} catch (Exception e) {
			utility.showAleart("Error while sending EOD report, due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception",e);
			}
		}
		log.info("Completed");
		return res;
	}
}