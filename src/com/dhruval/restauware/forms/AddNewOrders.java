package com.dhruval.restauware.forms;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.dhruval.restauware.dao.Orders;
import com.dhruval.restauware.db.DBConnection;
import com.dhruval.restauware.utilities.RestauwareUtility;

public class AddNewOrders {

	private JFrame frame;
	private JTextField itemCodeTF;
	private JComboBox qtyCombo, itemNameCombo;
	private int orderNo = 0;
	private JTable table;
	private RestauwareUtility utility;
	private StringBuffer itemBuff, qtyBuff, priceBuff, amtBuff;
	private int orderSrNo;
	private DBConnection db;
	private JScrollPane scrollPane;
	private JLabel lblTotalAmount, totAmt;
	private DefaultTableModel model;
	private JLabel label;
	private static Logger log = Logger.getLogger(AddNewOrders.class.getName());

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddNewOrders window = new AddNewOrders();
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
	public AddNewOrders() {
		initialize();
	}

	public AddNewOrders(int orderNo) {
		this.orderNo = orderNo;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		log.info("started");
		utility = new RestauwareUtility();
		frame = new JFrame("New Order No : " + orderNo);
		frame.getContentPane().setFont(new Font("Tahoma", Font.BOLD, 19));
		frame.setBounds(100, 100, 800, 514);
		frame.setResizable(false);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 11, 764, 41);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		JLabel lblItem = new JLabel("Item :");
		lblItem.setBounds(10, 11, 46, 14);
		panel_1.add(lblItem);

		itemCodeTF = new JTextField();
		itemCodeTF.setBounds(49, 8, 58, 20);
		panel_1.add(itemCodeTF);
		itemCodeTF.setColumns(10);
		itemCodeTF.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				itemNameCombo.setSelectedIndex(0);
			}
		});

		JButton addItem = new JButton("Add");
		addItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					log.info("Add Clicked");
					if (validatedItemCode()) {
						int flag = addNewOrder(Integer.parseInt(itemCodeTF.getText().trim()),
								Float.parseFloat(qtyCombo.getSelectedItem().toString()));
						if (flag == 1) {
							int rows = model.getRowCount();
							float totalAmount = 0.0f;
							for (int i = 0; i < rows; i++) {
								if (null != model.getValueAt(i, 3))
									totalAmount = totalAmount + Float.parseFloat(model.getValueAt(i, 3).toString());
								else
									break;
							}
							totAmt.setText(String.valueOf(totalAmount));
						} else {
							utility.showAleart("Invalid Item Code.!");
						}
					}
					qtyCombo.setSelectedIndex(0);
					itemNameCombo.setSelectedIndex(0);
					itemCodeTF.setText("");
					itemCodeTF.requestFocusInWindow();
				} catch (Exception e) {
					utility.showAleart("Error, while adding new order due to : "+e.getMessage());
					log.error("Exception "+e);
				}

			}
		});
		addItem.setBounds(602, 7, 69, 23);
		panel_1.add(addItem);

		JLabel lblNewLabel = new JLabel("Qty :");
		lblNewLabel.setBounds(511, 11, 46, 14);
		panel_1.add(lblNewLabel);

		qtyCombo = new JComboBox();
		qtyCombo.setModel(new DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
		qtyCombo.setBounds(546, 8, 46, 20);
		panel_1.add(qtyCombo);

		itemNameCombo = new JComboBox();
		itemNameCombo.setModel(new DefaultComboBoxModel(new String[] { "-- Select --" }));
		itemNameCombo.setBounds(117, 8, 384, 20);
		panel_1.add(itemNameCombo);

		JButton doneBtn = new JButton("Done");
		doneBtn.setBounds(681, 7, 68, 23);
		panel_1.add(doneBtn);
		doneBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				log.info("Done Clicked");
				frame.dispose();
				new ManagerHome().clickrefreshAllBtn();
			}
		});

		try {
			db = new DBConnection();
			ResultSet rs = db.getAllMenuItems();
			while (rs.next()) {
				itemNameCombo.addItem(rs.getString(1));
			}
		} catch (Exception e1) {
			utility.showAleart("Error, while fetching all Menu Items due to : " + e1.getMessage());
			log.error("Exception: "+e1);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e1) {
				log.error("Exception",e1);
			}
		}

		itemNameCombo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (itemNameCombo.getSelectedIndex() != 0) {
					try {
						db = new DBConnection();
						ResultSet rs = db.getItemCodeByItemName(itemNameCombo.getSelectedItem().toString());
						while (rs.next()) {
							itemCodeTF.setText(String.valueOf(rs.getInt(1)));
						}
					} catch (Exception e1) {
						utility.showAleart("Error, while fetching Item Code by name due to : " + e1.getMessage());
						log.error("Exception: "+e1);
					} finally {
						try {
							db.closeConnection();
						} catch (SQLException e1) {
							log.error("Exception",e1);
						}
					}
				} else {
					// itemCodeTF.setText("");
				}
			}
		});

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 63, 488, 404);
		frame.getContentPane().add(scrollPane);

		lblTotalAmount = new JLabel("Total Amount :");
		lblTotalAmount.setBounds(508, 63, 93, 14);
		frame.getContentPane().add(lblTotalAmount);

		totAmt = new JLabel("0.0");
		totAmt.setFont(new Font("Tahoma", Font.BOLD, 15));
		totAmt.setBounds(561, 88, 213, 62);
		frame.getContentPane().add(totAmt);

		label = new JLabel("Rs.");
		label.setFont(new Font("Tahoma", Font.BOLD, 15));
		label.setBounds(508, 88, 48, 62);
		frame.getContentPane().add(label);
		frame.setVisible(true);
		log.info("completed");
	}

	private int addNewOrder(int itemCode, float qty) {
		log.info("stared");
		int flag = 0;
		try {
			db = new DBConnection();
			ResultSet rs = db.getItemNPriceByCode(itemCode);
			Orders order = new Orders();
			order.setOrderNo(orderNo);
			while (rs.next()) {
				flag = 1;
				order.setItem(rs.getString(1));
				order.setPrice(rs.getFloat(2));
			}
			order.setQty(qty);
			order.setAmt(qty * order.getPrice());
			order.setStatus(1);
			if (flag == 1) {
				int result = db.addNewOrder(order);
				if (result > 0) {
					ResultSet res = db.getOrderByNo(order.getOrderNo(), "curdate()");
					viewOrderTable(res);
				}
			}
		} catch (Exception e) {
			utility.showAleart("Error, while adding new order due to : " + e.getMessage());
			log.error("Exception: "+e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception",e);
			}
		}
		log.info("completed");
		return flag;
	}

	private void updateQty(int itemCode, float qty) {
		log.info("started");
		try {
			db = new DBConnection();
			ResultSet rs = db.getItemNPriceByCode(itemCode);
			Orders order = new Orders();
			order.setOrderNo(orderNo);
			while (rs.next()) {
				order.setItem(rs.getString(1));
				order.setPrice(rs.getFloat(2));
			}
			order.setQty(qty);
			order.setAmt(qty * order.getPrice());
			order.setStatus(1);
			int result = db.addNewOrder(order);
			if (result > 0) {
				ResultSet res = db.getOrderByNo(order.getOrderNo(), "curdate()");
				viewOrderTable(res);
			}
		} catch (Exception e) {
			utility.showAleart("Error, while updating qty due to : "+e.getMessage());
			log.error("Exception: "+e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception",e);
			}
		}
		log.info("completed");
	}

	private void viewOrderTable(ResultSet rs) throws SQLException {
		log.info("started");
		String[] cols = { "Item", "Quantity", "Price", "Amount" };
		String[][] data = new String[300][4];

		int i = 0;
		while (rs.next()) {
			for (int j = 1; j <= 4; j++) {
				if (j == 1)
					data[i][j - 1] = rs.getString(j);

				if (j >= 2 && j <= 4)
					data[i][j - 1] = String.valueOf(rs.getFloat(j));
			}
			i++;
		}

		model = new DefaultTableModel(data, cols) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setFillsViewportHeight(true);
		table.getColumnModel().getColumn(0).setPreferredWidth(250);
		scrollPane.setViewportView(table);
		log.info("completed");
	}

	private boolean validatedItemCode() {
		log.info("started");
		if (itemCodeTF.getText().trim().equals("")) {
			utility.showAleart("Please choose item or enter code");
			return false;
		}
		if (!itemCodeTF.getText().trim().matches(("\\d*"))) {
			utility.showAleart("Please enter valid Item Code");
			return false;
		}
		log.info("completed");
		return true;
	}
}
