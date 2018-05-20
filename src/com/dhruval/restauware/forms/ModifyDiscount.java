package com.dhruval.restauware.forms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.dhruval.restauware.db.DBConnection;
import com.dhruval.restauware.utilities.RestauwareUtility;

public class ModifyDiscount {

	private JFrame frame;
	private JTextField discValueTF;
	private RestauwareUtility utility;
	private DBConnection db;
	private static Logger log = Logger.getLogger(ModifyDiscount.class.getName());

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModifyDiscount window = new ModifyDiscount(0, 0.0f);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ModifyDiscount(int disCode, float discValue) {
		initialize(disCode, discValue);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(int disCode, float discValue) {
		log.info("Started");
		utility = new RestauwareUtility();
		frame = new JFrame("Modify " + discValue);
		frame.setResizable(false);
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setSize(333, 170);
		frame.getContentPane().setFont(new Font("SansSerif", Font.PLAIN, 11));
		frame.getContentPane().setBackground(SystemColor.activeCaption);
		frame.getContentPane().setLayout(null);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		log.info(new Date().toString()+" > Modify Discount Loaded");

		JLabel lblNewLabel = new JLabel("Discount Value :");
		lblNewLabel.setBounds(37, 41, 113, 14);
		frame.getContentPane().add(lblNewLabel);

		discValueTF = new JTextField();
		discValueTF.setBounds(148, 38, 101, 20);
		frame.getContentPane().add(discValueTF);
		discValueTF.setColumns(10);
		discValueTF.setText(String.valueOf(discValue));

		JButton updateBtn = new JButton("Update");
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (validateFields(discValueTF.getText().trim())) {
					int ans = utility.showConfirmation("You wish to updated discount ?", "Are you sure?");
					if (ans == 0) {
						boolean updated = updateDiscountValue(disCode, discValueTF.getText().trim());
						if (updated) {
							utility.showAleart("Discount updated successfully..!");
							frame.dispose();
							new AdminHome().clickviewDiscBtn();
						} else {
							utility.showAleart("Failed to update Discount..!");
						}
					}
				}
			}
		});
		updateBtn.setBounds(61, 78, 89, 23);
		frame.getContentPane().add(updateBtn);

		JButton delBtn = new JButton("Delete");
		delBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ans = utility.showConfirmation("You wish to delete discount ?", "Are you sure?");
				if (ans == 0) {
					boolean done = deleteDiscountValue(disCode);
					if(done){
						utility.showAleart("Discount deleted successfully..!");
						frame.dispose();
						new AdminHome().clickviewDiscBtn();
					}else{
						utility.showAleart("Failed to delete Discount..!");
					}
				
				}
			}
		});
		delBtn.setBounds(171, 78, 89, 23);
		frame.getContentPane().add(delBtn);
		log.info("Completed");
	}

	private boolean validateFields(String disValue) {
		log.info("Started");
		if (disValue.equals("")) {
			utility.showAleart("Discount cannot be blank.");
			return false;
		}
		if (!disValue.matches("^([+-]?(\\d+\\.)?\\d+)$") || !disValue.matches(".*\\d+.*")) {
			utility.showAleart("Discount must be number or decimal number.");
			return false;
		}
		log.info("Completed");
		return true;
	}

	private boolean updateDiscountValue(int disCode, String disValue) {
		log.info("Started");
		boolean done = false;
		try {
			db = new DBConnection();
			int no = db.updateDiscountByCode(disCode, Float.parseFloat(disValue));
			if (no > 0) {
				done = true;
			}
		} catch (Exception e) {
			utility.showAleart("Error while updating discount value due to : " + e.getMessage());
			log.error("Exception",e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception",e);
			}
		}
		log.info("Completed");
		return done;
	}

	private boolean deleteDiscountValue(int disCode) {
		log.info("Started");
		boolean done = false;
		try {
			db = new DBConnection();
			int no = db.deleteDiscountByCode(disCode);
			if (no > 0) {
				done = true;
			}
		} catch (Exception e) {
			utility.showAleart("Error while deleting discount value due to : " + e.getMessage());
			log.error("Exception",e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception",e);
			}
		}
		log.info("Completed");
		return done;
	}
}
