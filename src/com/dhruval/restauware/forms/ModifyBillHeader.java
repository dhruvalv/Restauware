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

public class ModifyBillHeader {

	private JFrame frame;
	private RestauwareUtility utility;
	private JTextField restaunameTF;
	private JTextField restauaddTF;
	private DBConnection db;
	private static Logger log = Logger.getLogger(ModifyBillHeader.class.getName());

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModifyBillHeader window = new ModifyBillHeader(0,"","");
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
	public ModifyBillHeader(int id,String restauname,String restauadd) {
		initialize(id,restauname,restauadd);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(int id,String restauname,String restauadd) {
		log.info("Started");
		utility = new RestauwareUtility();
		frame = new JFrame("Modifyind Bill Headers ");
		frame.setSize(619, 228);
		frame.getContentPane().setFont(new Font("SansSerif", Font.PLAIN, 11));
		frame.getContentPane().setBackground(SystemColor.activeCaption);
		frame.getContentPane().setLayout(null);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	
		
		JLabel lblNewLabel = new JLabel("Restaurant Name :");
		lblNewLabel.setBounds(28, 36, 122, 14);
		frame.getContentPane().add(lblNewLabel);
		
		restaunameTF = new JTextField();
		restaunameTF.setBounds(146, 33, 154, 20);
		frame.getContentPane().add(restaunameTF);
		restaunameTF.setColumns(10);
		restaunameTF.setText(restauname);
		
		JLabel lblNewLabel_1 = new JLabel("Restaurant Address :");
		lblNewLabel_1.setBounds(28, 64, 111, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		restauaddTF = new JTextField();
		restauaddTF.setBounds(146, 60, 415, 20);
		frame.getContentPane().add(restauaddTF);
		restauaddTF.setColumns(10);
		restauaddTF.setText(restauadd);
		
		JLabel lblNewLabel_2 = new JLabel("Note : Restaurant Name & Address will get printed on bill");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblNewLabel_2.setBounds(28, 141, 481, 14);
		frame.getContentPane().add(lblNewLabel_2);
		
		JButton updateBtn = new JButton("Update");
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ans = utility.showConfirmation("You wish to Update?", "Are you sure?");
				if(ans == 0){
					if(validateFields(restauaddTF.getText().trim(),restaunameTF.getText().trim())){
						String restAdd = restauaddTF.getText().trim();
						String restName = restaunameTF.getText().trim();
						boolean done = updateBillHead(id,restName,restAdd);
						if(done){
							utility.showAleart("BillHeader updated successfully..!");
							frame.dispose();
							new AdminHome().clickviewBillHeaderBtn();
						}else{
							utility.showAleart("Failed to update BillHeader..!");
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
					boolean done = deleteBillHeader(id);
					if(done){
						utility.showAleart("BillHeader deleted successfully..!");
						frame.dispose();
						new AdminHome().clickviewBillHeaderBtn();
					}else{
						utility.showAleart("Failed to delete BillHeader..!");
					}
				}
			}
		});
		delBtn.setBounds(261, 91, 89, 23);
		frame.getContentPane().add(delBtn);
		log.info("Completed");
	}
	
	private boolean validateFields(String add, String name) {
		log.info("Started");
		if (add.equals("")) {
			utility.showAleart("Address cannot be blank.");
			return false;
		}
		if (name.equals("")) {
			utility.showAleart("Name cannot be blank.");
			return false;
		}
		if (add.length() > 150) {
			utility.showAleart("Address Length must not be greater than 150 characters as it will get printed on Receipt.");
			return false;
		}
		if (name.length() > 50) {
			utility.showAleart("Name Length must not be greater than 50 characters as it will get printed on Receipt.");
			return false;
		}
		log.info("Completed");
		return true;
	}
	

	private boolean updateBillHead(int id, String name, String add) {
		log.info("Started");
		boolean done = false;
		try {
			db = new DBConnection();
			int no = db.updateBillheadersById(id, name, add);
			if (no > 0) {
				done = true;
			}
		} catch (Exception e) {
			utility.showAleart("Error while updating BillHeader value due to : " + e.getMessage());
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
	
	private boolean deleteBillHeader(int id) {
		log.info("Started");
		boolean done = false;
		try {
			db = new DBConnection();
			int no = db.deleteBillheadersById(id);
			if (no > 0) {
				done = true;
			}
		} catch (Exception e) {
			utility.showAleart("Error while deleting Bill Header value due to : " + e.getMessage());
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
