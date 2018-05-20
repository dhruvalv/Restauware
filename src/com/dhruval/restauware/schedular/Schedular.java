package com.dhruval.restauware.schedular;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.dhruval.restauware.db.DBConnection;
import com.dhruval.restauware.forms.ManagerHome;
import com.dhruval.restauware.utilities.RestauwareUtility;

public class Schedular extends TimerTask {

	private static RestauwareUtility utility = new RestauwareUtility();;
	private DBConnection db;
	private static boolean done = true;
	private static Logger log = Logger.getLogger(Schedular.class.getName());

	public Schedular() {
		
	}

	public void run() {
		log.info("started");
		try {
			String dateInString = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date());
			String splitDate[] = dateInString.split(" ");
			String hour = splitDate[1].substring(0, splitDate[1].indexOf(":"));
			if (hour.equals("23")) {
				String min = splitDate[1].substring(splitDate[1].indexOf(":") + 1, splitDate[1].lastIndexOf(":"));
				int mi = Integer.parseInt(min);
				if (min.equals("45") || min.equals("46")) {
					utility.showAleart("Hurray Up. Reminder.\nPlease close the store its " + dateInString);
				} else if (min.equals("48") || min.equals("49")) {
					utility.showAleart("Hurray Up. Final Reminder, Please close the store its " + dateInString
							+ "\nIt will be closed automatically in couple of mins");
				} else if (mi > 50 && done) {
					done = false;
					utility.showTimerAlert("Closing the Store please stay away. It might takes few mins.\n",
							"Do not touch me!");
					performForceClose();
				}
			}
		} catch (Exception e) {
			utility.showAleart("Error, while running schedular");
			log.error("Exception", e);
		}
		log.info("completed");
	}

	private void performForceClose() {
		log.info("started");
		try {
			db = new DBConnection();
			db.removeAllLiveOrdersForceClose();
			ManagerHome m = new ManagerHome();
			boolean performed = m.eodActionPerformed(true);
			if (performed) {
				utility.showAleart("Store has been closed successfully..!");
				System.exit(0);
			} else {
				utility.showAleart("Unable to performed EOD. Please contact Admin.");
			}

		} catch (Exception e) {
			utility.showAleart("Error while force closing the store!");
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception", e);
			}
		}
		log.info("completed");
	}
}
