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

public class ModifyBillFooter {

	private JFrame frame;
	private RestauwareUtility utility;
	private JTextField line1TF;
	private JTextField line2TF;
	private DBConnection db;
	private static Logger log = Logger.getLogger(ModifyBillFooter.class.getName());

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModifyBillFooter window = new ModifyBillFooter(0,"","");
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
	public ModifyBillFooter(int id,String line1,String line2) {
		initialize(id,line1,line2);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(int id,String line1,String line2) {
		log.info("Started");
		utility = new RestauwareUtility();
		frame = new JFrame("Modifyind Bill Footer ");
		frame.setSize(464, 207);
		frame.getContentPane().setFont(new Font("SansSerif", Font.PLAIN, 11));
		frame.getContentPane().setBackground(SystemColor.activeCaption);
		frame.getContentPane().setLayout(null);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	
		
		JLabel lblNewLabel = new JLabel("Line 1 :");
		lblNewLabel.setBounds(28, 36, 122, 14);
		frame.getContentPane().add(lblNewLabel);
		
		line1TF = new JTextField();
		line1TF.setBounds(146, 33, 228, 20);
		frame.getContentPane().add(line1TF);
		line1TF.setColumns(10);
		line1TF.setText(line1);
		
		JLabel lblNewLabel_1 = new JLabel("Line 2 :");
		lblNewLabel_1.setBounds(28, 64, 111, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		line2TF = new JTextField();
		line2TF.setBounds(146, 60, 228, 20);
		frame.getContentPane().add(line2TF);
		line2TF.setColumns(10);
		line2TF.setText(line2);
		
		JLabel lblNewLabel_2 = new JLabel("Note : Line1 & Line 2 will get printed on bill");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblNewLabel_2.setBounds(28, 141, 481, 14);
		frame.getContentPane().add(lblNewLabel_2);
		
		JButton updateBtn = new JButton("Update");
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ans = utility.showConfirmation("You wish to Update?", "Are you sure?");
				if(ans == 0){
					if(validateFields(line1TF.getText().trim(),line2TF.getText().trim())){
						String line1 = line1TF.getText().trim();
						String line2 = line2TF.getText().trim();
						boolean done = updateBillFooter(id,line1,line2);
						if(done){
							utility.showAleart("BillFooter updated successfully..!");
							frame.dispose();
							new AdminHome().clickviewBillFooterBtn();
						}else{
							utility.showAleart("Failed to update BillFooter..!");
						}
					}
				}
			}
		});
		updateBtn.setBounds(146, 91, 89, 23);
		frame.getContentPane().add(updateBtn);
		
		JButton delBtn = new JButton("Delete");
		delBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ans = utility.showConfirmation("You wish to Delete ?", "Are you sure?");
				if(ans == 0){
					boolean done = deleteBillFooter(id);
					if(done){
						utility.showAleart("BillFooter deleted successfully..!");
						frame.dispose();
						new AdminHome().clickviewBillFooterBtn();
					}else{
						utility.showAleart("Failed to delete BillFooter..!");
					}
				}
			}
		});
		delBtn.setBounds(261, 91, 89, 23);
		frame.getContentPane().add(delBtn);
		log.info("Completed");
	}
	
	private boolean validateFields(String line1, String line2) {
		log.info("Started");
		if (line1.equals("")) {
			utility.showAleart("Line1 cannot be blank.");
			return false;
		}
		
		if (line1.length() > 60) {
			utility.showAleart("Line 1 Length must not be greater than 60 characters as it will get printed on Receipt.");
			return false;
		}
		if (line2.length() > 60) {
			utility.showAleart("Line 2 Length must not be greater than 60 characters as it will get printed on Receipt.");
			return false;
		}
		log.info("Completed");
		return true;
	}
	

	private boolean updateBillFooter(int id, String line1, String line2) {
		log.info("Started");
		boolean done = false;
		try {
			db = new DBConnection();
			int no = db.updateBillFooterById(id, line1, line2);
			if (no > 0) {
				done = true;
			}
		} catch (Exception e) {
			utility.showAleart("Error while updating BillFooter value due to : " + e.getMessage());
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
	
	private boolean deleteBillFooter(int id) {
		log.info("Started");
		boolean done = false;
		try {
			db = new DBConnection();
			int no = db.deleteBillFooterById(id);
			if (no > 0) {
				done = true;
			}
		} catch (Exception e) {
			utility.showAleart("Error while deleting Bill Footer value due to : " + e.getMessage());
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
