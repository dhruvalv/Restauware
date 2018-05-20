package com.dhruval.restauware.forms;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.dhruval.restauware.db.DBConnection;
import com.dhruval.restauware.utilities.RestauwareUtility;

public class ChangePass {

	private JFrame frame;
	private RestauwareUtility utility;
	private JTextField usernameTF;
	private JPasswordField oldpassTF;
	private JPasswordField newPassTF;
	private DBConnection db;
	private static Logger log = Logger.getLogger(ChangePass.class.getName());
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChangePass window = new ChangePass("Dhruval");
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ChangePass() {

	}

	public ChangePass(String username) {
		initialize(username);
	}

	private void initialize(String username) {
		log.info("Started");
		frame = new JFrame("Chaning Password..");
		frame.setSize(304, 201);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		utility = new RestauwareUtility();
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Username :");
		lblNewLabel.setBounds(48, 26, 70, 14);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Old Password :");
		lblNewLabel_1.setBounds(31, 57, 87, 14);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("New Password :");
		lblNewLabel_2.setBounds(24, 85, 94, 14);
		frame.getContentPane().add(lblNewLabel_2);

		usernameTF = new JTextField();
		usernameTF.setBounds(121, 23, 105, 20);
		frame.getContentPane().add(usernameTF);
		usernameTF.setText(username);
		usernameTF.setEditable(false);
		usernameTF.setColumns(10);

		oldpassTF = new JPasswordField();
		oldpassTF.setBounds(121, 54, 105, 20);
		frame.getContentPane().add(oldpassTF);

		newPassTF = new JPasswordField();
		newPassTF.setBounds(121, 82, 105, 20);
		frame.getContentPane().add(newPassTF);

		JButton changeBtn = new JButton("Change");
		changeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				log.info("Changed Clicked");
				if (validateChangePassForm(oldpassTF.getText().trim(), newPassTF.getText().trim())) {
					boolean result = validateOldPassword(username, oldpassTF.getText().trim());
					if (result) {
						boolean set = setNewPassword(username, newPassTF.getText().trim());
						if (set) {
							utility.showAleart("Great ! Password has been changed.!");
							frame.dispose();
							new Welcome(0);
						} else {
							utility.showAleart("Sorry! could not change the password.!");
							clearForm();
						}
					} else {
						utility.showAleart("Old Password doesnot match with username!");
						frame.dispose();
						clearForm();
					}
				}
			}
		});
		changeBtn.setBounds(124, 113, 89, 23);
		frame.getContentPane().add(changeBtn);
		frame.setVisible(true);
		log.info("Completed");
	}

	private boolean validateChangePassForm(String oldPass, String newPass) {
		log.info("Started");
		if (oldPass.equals("")) {
			utility.showAleart("Old Password Cannot be blank!");
			return false;
		}
		if (newPass.equals("")) {
			utility.showAleart("New Password Cannot be blank!");
			return false;
		}
		log.info("Completed");
		return true;
	}

	private boolean validateOldPassword(String user, String oldpass) {
		log.info("Started");
		boolean result = false;
		try {
			db = new DBConnection();
			result = db.validateOldPass(user, oldpass);
		} catch (Exception e) {
			utility.showAleart("Error, while validating old password due to : "+e.getMessage());
			log.error("Exception",e);
		}finally{
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception",e);
			}
		}
		log.info("Completed");
		return result;
	}

	private boolean setNewPassword(String username, String newpass) {
		log.info("Started");
		boolean result = false;
		try {
			db = new DBConnection();
			result = db.setNewPassword(username, newpass);
		} catch (Exception e) {
			utility.showAleart("Error, while setting new password due to : "+e.getMessage());
			log.error("Exception",e);
		} finally{
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception",e);
			}
		}
		log.info("Completed");
		return result;
	}

	private void clearForm() {
		oldpassTF.setText("");
		newPassTF.setText("");
	}
}
