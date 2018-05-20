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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.dhruval.restauware.db.DBConnection;
import com.dhruval.restauware.utilities.RestauwareUtility;

public class ModifyInventoryItem {

	private JFrame frame;
	private JTextField nameTF;
	private JTextField qtyTF;
	private JTextField itemIdTF;
	private RestauwareUtility utility;
	private DBConnection db;
	private static Logger log = Logger.getLogger(ModifyInventoryItem.class.getName());

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModifyInventoryItem window = new ModifyInventoryItem("", 1, 1);
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
	public ModifyInventoryItem(String name, int qty, int itemid) {
		initialize(name, qty, itemid);
	}

	public ModifyInventoryItem() {
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String name, int qty, int itemId) {
		log.info("Started");
		utility = new RestauwareUtility();
		frame = new JFrame("Modify Item : " + name);
		frame.setResizable(false);
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setSize(463, 237);
		frame.getContentPane().setFont(new Font("SansSerif", Font.PLAIN, 11));
		frame.getContentPane().setBackground(SystemColor.activeCaption);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Item Name : ");
		lblNewLabel.setBounds(62, 72, 80, 14);
		frame.getContentPane().add(lblNewLabel);

		nameTF = new JTextField();
		nameTF.setBounds(147, 69, 247, 20);
		frame.getContentPane().add(nameTF);
		nameTF.setText(name);
		nameTF.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Quantity :");
		lblNewLabel_1.setBounds(62, 97, 80, 14);
		frame.getContentPane().add(lblNewLabel_1);

		qtyTF = new JTextField();
		qtyTF.setBounds(147, 94, 118, 20);
		frame.getContentPane().add(qtyTF);
		qtyTF.setText(String.valueOf(qty));
		qtyTF.setColumns(10);

		JButton updateBtn = new JButton("Update");
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (validateFields(nameTF.getText().trim(), qtyTF.getText().trim())) {
					try {
						int no = utility.showConfirmation("You wish to update ?", "Are you sure ?");
						if (no == 0) {
							db = new DBConnection();
							String itemName = nameTF.getText().trim();
							int qty = Integer.parseInt(qtyTF.getText().trim());
							int itemId = Integer.parseInt(itemIdTF.getText().trim());
							int updated = db.updateInventoryItem(itemName, qty, itemId);
							if (updated > 0) {
								utility.showAleart("Item updated successfully..!");
								frame.dispose();
								new AdminHome().clickviewItemBtn();
							} else {
								utility.showAleart("Failed to updated Item..!");
							}
						}

					} catch (Exception e1) {
						utility.showAleart("Error while updating inventory item due to : " + e1.getMessage());
						log.error("Exception",e1);
					} finally {
						try {
							db.closeConnection();
						} catch (SQLException e1) {
							log.error("Exception",e1);
						}
					}
				}
			}
		});
		updateBtn.setBounds(127, 133, 89, 23);
		frame.getContentPane().add(updateBtn);

		JButton delBtn = new JButton("Delete");
		delBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int ans = utility.showConfirmation("You wish to delete the item?", "Are you sure?");
					if (ans == 0) {
						db = new DBConnection();
						int no = db.deleteInventoryItem(Integer.parseInt(itemIdTF.getText().trim()));
						if (no > 0) {
							utility.showAleart("Item deleted successfully..!");
							frame.dispose();
							new AdminHome().clickviewItemBtn();
						} else {
							utility.showAleart("Failed to delete Item..!");
						}
					}
				} catch (Exception e1) {
					utility.showAleart("Error while deleting Inventory Item due to : " + e1.getMessage());
					log.error("Exception",e1);
				} finally {
					try {
						db.closeConnection();
					} catch (SQLException e1) {
						log.error("Exception",e1);
					}
				}
			}
		});
		delBtn.setBounds(243, 133, 89, 23);
		frame.getContentPane().add(delBtn);

		JLabel lblNewLabel_2 = new JLabel("Item Id :");
		lblNewLabel_2.setBounds(62, 47, 46, 14);
		frame.getContentPane().add(lblNewLabel_2);

		itemIdTF = new JTextField();
		itemIdTF.setEditable(false);
		itemIdTF.setBounds(147, 44, 80, 20);
		frame.getContentPane().add(itemIdTF);
		itemIdTF.setColumns(10);
		itemIdTF.setText(String.valueOf(itemId));
		frame.setVisible(true);
		log.info("Completed");
	}

	private boolean validateFields(String name, String qty) {
		log.info("Started");
		if (name.equals("")) {
			utility.showAleart("First Name cannot be blank.");
			return false;
		}
		if (!qty.matches("\\d*") || qty.equals("")) {
			utility.showAleart("Contact No must be a number.");
			return false;
		}
		log.info("Completed");
		return true;
	}
}
