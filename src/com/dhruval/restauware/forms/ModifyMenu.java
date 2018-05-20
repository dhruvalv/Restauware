package com.dhruval.restauware.forms;

import java.awt.Color;
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

import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;
import java.awt.event.ActionEvent;

public class ModifyMenu {

	private JFrame frame;
	private JTextField itemCodeTF;
	private JTextField itemNameTF;
	private JTextField itemPriceTF;
	private RestauwareUtility utility;
	private DBConnection db;
	private static Logger log = Logger.getLogger(ModifyMenu.class.getName());

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModifyMenu window = new ModifyMenu("", "", "", 0.0f);
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
	public ModifyMenu(String code, String cat, String name, float price) {
		initialize(code, cat, name, price);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String code, String cat, String name, float price) {
		log.info("Started");
		utility = new RestauwareUtility();
		frame = new JFrame("Modify Menu Item : " + name);
		frame.setResizable(false);
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setSize(527, 229);
		frame.getContentPane().setFont(new Font("SansSerif", Font.PLAIN, 11));
		frame.getContentPane().setBackground(SystemColor.activeCaption);
		frame.getContentPane().setLayout(null);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		log.info(new Date().toString()+" > ModifyMenuForm Loaded");
		
		JLabel lblNewLabel = new JLabel("Item Code : ");
		lblNewLabel.setBounds(64, 32, 76, 14);
		frame.getContentPane().add(lblNewLabel);

		itemCodeTF = new JTextField();
		itemCodeTF.setEditable(false);
		itemCodeTF.setBounds(151, 29, 155, 20);
		frame.getContentPane().add(itemCodeTF);
		itemCodeTF.setColumns(10);
		itemCodeTF.setText(code);

		JLabel lblNewLabel_1 = new JLabel("Item Category :");
		lblNewLabel_1.setBounds(44, 57, 100, 14);
		frame.getContentPane().add(lblNewLabel_1);

		JComboBox catCombo = new JComboBox();
		catCombo.setModel(new DefaultComboBoxModel(new String[] { "Food", "Drinks", "Desserts", "Other" }));
		catCombo.setBounds(151, 54, 155, 20);
		frame.getContentPane().add(catCombo);
		catCombo.setSelectedItem(cat);

		JLabel lblItemName = new JLabel("Menu Item Name :");
		lblItemName.setBounds(32, 82, 100, 14);
		frame.getContentPane().add(lblItemName);

		itemNameTF = new JTextField();
		itemNameTF.setBounds(151, 79, 314, 20);
		frame.getContentPane().add(itemNameTF);
		itemNameTF.setColumns(10);
		itemNameTF.setText(name);

		JLabel lblNewLabel_2 = new JLabel("Price :");
		lblNewLabel_2.setBounds(90, 107, 46, 14);
		frame.getContentPane().add(lblNewLabel_2);

		itemPriceTF = new JTextField();
		itemPriceTF.setBounds(151, 104, 92, 20);
		frame.getContentPane().add(itemPriceTF);
		itemPriceTF.setColumns(10);
		itemPriceTF.setText(String.valueOf(price));

		JButton updateBtn = new JButton("Update");
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (validateFields(itemNameTF.getText().trim(), itemPriceTF.getText().trim())) {
					String itemName = itemNameTF.getText().trim();
					String itemCat = catCombo.getSelectedItem().toString();
					String itemCode = itemCodeTF.getText().trim();
					float price = Float.parseFloat(itemPriceTF.getText().trim());
					boolean updated = updateMenuItem(itemName, itemCat, itemCode, price);
					if (updated) {
						utility.showAleart("Menu Item updated successfully..!");
						frame.dispose();
						new AdminHome().clickviewMenuBtn();
					} else {
						utility.showAleart("Failed to update Menu Item..!");
					}
				}
			}
		});
		updateBtn.setBounds(151, 145, 89, 23);
		frame.getContentPane().add(updateBtn);

		JButton delBtn = new JButton("Delete");
		delBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ans = utility.showConfirmation("You wish to Delete menu item?", "Are you sure?");
				if(ans == 0){
					boolean done = deleteMenuItem(code);
					if(done){
						utility.showAleart("Menu Item deleted successfully..!");
						frame.dispose();
						new AdminHome().clickviewMenuBtn();
					}else{
						utility.showAleart("Failed to delete Menu Item..!");
					}
				}
			}
		});
		delBtn.setBounds(264, 145, 89, 23);
		frame.getContentPane().add(delBtn);
		log.info("Completed");
	}

	private boolean validateFields(String name, String price) {
		log.info("Started");
		if (name.equals("")) {
			utility.showAleart("Menu Item Name cannot be blank.");
			return false;
		}
		if (price.equals("")) {
			utility.showAleart("Price cannot be blank.");
			return false;
		}
		if (!price.matches("^([+-]?(\\d+\\.)?\\d+)$") || !price.matches(".*\\d+.*")) {
			utility.showAleart("Price must be number or decimal number.");
			return false;
		}
		log.info("Completed");
		return true;
	}

	private boolean updateMenuItem(String itemName, String itemCat, String itemCode, float price) {
		log.info("Started");
		boolean done = false;
		try {
			db = new DBConnection();
			int no = db.updateMenuById(itemCat, itemName, itemCode, price);
			if(no > 0){
				done = true;
			}
		} catch (Exception e) {
			utility.showAleart("Error while updating menu item due to : " + e.getMessage());
			log.error("Exception",e);
		}finally{
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception",e);
			}
		}
		log.info("Completed");
		return done;
	}
	
	private boolean deleteMenuItem(String code) {
		log.info("Started");
		boolean done = false;
		try {
			db = new DBConnection();
			int no = db.deleteMenuById(code);
			if(no > 0){
				done = true;
			}
		} catch (Exception e) {
			utility.showAleart("Error while deleting menu item due to : " + e.getMessage());
			log.error("Exception",e);
		}finally{
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
