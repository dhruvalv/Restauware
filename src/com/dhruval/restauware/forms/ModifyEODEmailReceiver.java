package com.dhruval.restauware.forms;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.dhruval.restauware.db.DBConnection;
import com.dhruval.restauware.utilities.RestauwareUtility;

public class ModifyEODEmailReceiver {

	private JFrame frame;
	private RestauwareUtility utility;
	private JTextField emailAddTF;
	private DBConnection db;
	private static Logger log = Logger.getLogger(ModifyEODEmailReceiver.class.getName());

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModifyEODEmailReceiver window = new ModifyEODEmailReceiver(0,"");
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
	public ModifyEODEmailReceiver(int id,String emailid) {
		initialize(id,emailid);
	}
	
	public ModifyEODEmailReceiver() {
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(int id,String emailid) {
		log.info("Started");
		utility = new RestauwareUtility();
		frame = new JFrame("Modify : "+emailid);
		frame.setSize(530, 228);
		frame.getContentPane().setFont(new Font("SansSerif", Font.PLAIN, 11));
		frame.getContentPane().setBackground(SystemColor.activeCaption);
		frame.getContentPane().setLayout(null);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		
		JLabel lblNewLabel = new JLabel("Email Address :");
		lblNewLabel.setBounds(28, 36, 122, 14);
		frame.getContentPane().add(lblNewLabel);
		
		emailAddTF = new JTextField();
		emailAddTF.setBounds(146, 33, 309, 20);
		frame.getContentPane().add(emailAddTF);
		emailAddTF.setColumns(10);
		emailAddTF.setText(emailid);
		
		JLabel lblNewLabel_2 = new JLabel("Note : Make sure Email Address is correct to receiev EOD reports.");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblNewLabel_2.setBounds(28, 160, 481, 14);
		frame.getContentPane().add(lblNewLabel_2);
		
		JButton updateBtn = new JButton("Update");
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ans = utility.showConfirmation("You wish to update email address ?", "Are you sure?");
				if(ans == 0){
					if(validateFields(emailAddTF.getText().trim())){
						String emailAdd = emailAddTF.getText().trim();
						boolean done = updateEmailAdd(id,emailAdd);
						if(done){
							utility.showAleart("Email Address updated successfully..!");
							frame.dispose();
							new AdminHome().clickviewRecEODREp();
						}else{
							utility.showAleart("Failed to update EmailAdd..!");
						}
					}
				}
			}
		});
		updateBtn.setBounds(146, 81, 89, 23);
		frame.getContentPane().add(updateBtn);
		
		JButton delBtn = new JButton("Delete");
		delBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ans = utility.showConfirmation("You wish to Delete Email Address ?", "Are you sure?");
				if(ans == 0){
					boolean done = deleteEmailAdd(id);
					if(done){
						utility.showAleart("Email Address deleted successfully..!");
						frame.dispose();
						new AdminHome().clickviewRecEODREp();
					}else{
						utility.showAleart("Failed to delete Email Address..!");
					}
				}
			}
		});
		delBtn.setBounds(261, 81, 89, 23);
		frame.getContentPane().add(delBtn);
		log.info("Completed");
	}
	
	private boolean validateFields(String emailAdd) {
		log.info("Started");
		if (emailAdd.equals("")) {
			utility.showAleart("Email Address cannot be blank.");
			return false;
		}
		log.info("Completed");
		return true;
	}
	

	private boolean updateEmailAdd(int id, String emailAdd) {
		log.info("Started");
		boolean done = false;
		try {
			db = new DBConnection();
			int no = db.updateEmailAddById(id,emailAdd);
			if (no > 0) {
				done = true;
			}
		} catch (Exception e) {
			utility.showAleart("Error while updating Email Address value due to : " + e.getMessage());
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
	
	private boolean deleteEmailAdd(int id) {
		log.info("Started");
		boolean done = false;
		try {
			db = new DBConnection();
			int no = db.deleteEmailAddById(id);
			if (no > 0) {
				done = true;
			}
		} catch (Exception e) {
			utility.showAleart("Error while deleting Email Address value due to : " + e.getMessage());
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
