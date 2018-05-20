package com.dhruval.restauware.forms;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.dhruval.restauware.db.DBConnection;
import com.dhruval.restauware.utilities.RestauwareUtility;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;
import java.awt.event.ActionEvent;

public class ModifyTax {

	private JFrame frame;
	private JTextField taxDescriptnTF;
	private JTextField taxShortTF;
	private JTextField taxPercTF;
	private RestauwareUtility utility;
	private DBConnection db;
	private static Logger log = Logger.getLogger(ModifyTax.class.getName());

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModifyTax window = new ModifyTax(0,"","",0.0f);
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
	public ModifyTax(int id,String fullName,String shortName,float discPerc) {
		initialize(id,fullName,shortName,discPerc);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(int id,String fullName,String shortName,float discPerc) {
		log.info("Started");
		utility = new RestauwareUtility();
		frame = new JFrame("Modify "+fullName);
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
		log.info(new Date().toString()+" > ModifyTaxForm Loaded");
		
		JLabel lblNewLabel = new JLabel("Tax Description :");
		lblNewLabel.setBounds(28, 36, 122, 14);
		frame.getContentPane().add(lblNewLabel);
		
		taxDescriptnTF = new JTextField();
		taxDescriptnTF.setBounds(146, 33, 309, 20);
		frame.getContentPane().add(taxDescriptnTF);
		taxDescriptnTF.setColumns(10);
		taxDescriptnTF.setText(fullName);
		
		JLabel lblNewLabel_1 = new JLabel("Tax Short Name :");
		lblNewLabel_1.setBounds(28, 64, 111, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		taxShortTF = new JTextField();
		taxShortTF.setBounds(146, 60, 166, 20);
		frame.getContentPane().add(taxShortTF);
		taxShortTF.setColumns(10);
		taxShortTF.setText(shortName);
		
		JLabel lblNewLabel_2 = new JLabel("Note : Tax Short Name will get printed on bill, so it must not be more than 10 character.");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblNewLabel_2.setBounds(28, 160, 481, 14);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Tax Percentage :");
		lblNewLabel_3.setBounds(28, 88, 111, 14);
		frame.getContentPane().add(lblNewLabel_3);
		
		taxPercTF = new JTextField();
		taxPercTF.setBounds(146, 85, 91, 20);
		frame.getContentPane().add(taxPercTF);
		taxPercTF.setColumns(10);
		taxPercTF.setText(String.valueOf(discPerc));
		
		JButton updateBtn = new JButton("Update");
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ans = utility.showConfirmation("You wish to update tax ?", "Are you sure?");
				if(ans == 0){
					if(validateFields(taxDescriptnTF.getText().trim(),taxShortTF.getText().trim(),taxPercTF.getText().trim())){
						String taxFullName = taxDescriptnTF.getText().trim();
						String taxShortName = taxShortTF.getText().trim();
						float taxPerc = Float.parseFloat(taxPercTF.getText().trim());
						boolean done = updateTax(id,taxFullName,taxShortName,taxPerc);
						if(done){
							utility.showAleart("Tax updated successfully..!");
							frame.dispose();
							new AdminHome().clickviewTaxBtn();
						}else{
							utility.showAleart("Failed to update Tax..!");
						}
					}
				}
			}
		});
		updateBtn.setBounds(146, 116, 89, 23);
		frame.getContentPane().add(updateBtn);
		
		JButton delBtn = new JButton("Delete");
		delBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ans = utility.showConfirmation("You wish to Delete tax ?", "Are you sure?");
				if(ans == 0){
					boolean done = deleteTax(id);
					if(done){
						utility.showAleart("Tax deleted successfully..!");
						frame.dispose();
						new AdminHome().clickviewTaxBtn();
					}else{
						utility.showAleart("Failed to delete Tax..!");
					}
				}
			}
		});
		delBtn.setBounds(261, 116, 89, 23);
		frame.getContentPane().add(delBtn);
		log.info("Completed");
	}
	
	private boolean validateFields(String fullName, String shortName, String taxPerc) {
		log.info("Started");
		if (fullName.equals("")) {
			utility.showAleart("Description cannot be blank.");
			return false;
		}
		if (shortName.equals("")) {
			utility.showAleart("Short Name cannot be blank.");
			return false;
		}
		if (shortName.length() > 10) {
			utility.showAleart("Short Name must not greater than 10 characters.\n (Keep is short E.g. CGST @9%)");
			return false;
		}
		if (!taxPerc.matches("^([+-]?(\\d+\\.)?\\d+)$") || !taxPerc.matches(".*\\d+.*")) {
			utility.showAleart("Tax Percentage must be number or decimal number.");
			return false;
		}
		log.info("Completed");
		return true;
	}
	

	private boolean updateTax(int id, String taxFullName, String taxShortName, float taxPerc) {
		log.info("Started");
		boolean done = false;
		try {
			db = new DBConnection();
			int no = db.updateTaxById(id, taxFullName, taxShortName, taxPerc);
			if (no > 0) {
				done = true;
			}
		} catch (Exception e) {
			utility.showAleart("Error while updating tax value due to : " + e.getMessage());
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
	
	private boolean deleteTax(int id) {
		log.info("Started");
		boolean done = false;
		try {
			db = new DBConnection();
			int no = db.deleteTaxById(id);
			if (no > 0) {
				done = true;
			}
		} catch (Exception e) {
			utility.showAleart("Error while deleting tax value due to : " + e.getMessage());
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
