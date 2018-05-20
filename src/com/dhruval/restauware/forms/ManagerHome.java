package com.dhruval.restauware.forms;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Label;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import com.dhruval.restauware.dao.User;
import com.dhruval.restauware.db.DBConnection;
import com.dhruval.restauware.email.SendingEODReport;
import com.dhruval.restauware.reports.CreatEODReport;
import com.dhruval.restauware.utilities.RestauwareUtility;

public class ManagerHome {

	private JFrame frmManagersHome;
	private JTextField openBalTF;
	private JTextField bodAvailQtyTF;
	private JTextField bodAddQtyTF;
	private JTextField bodQtyRemTF;
	private JTextField getTableNoTF;
	private JTable orderTable;
	private JComboBox getServingOrderNoCombo, getServedOrderNoCombo, getCancelledOrderNoCombo, bodItemCombo,
			attendCatCombo, attendNameCombo;
	private JButton newOrderBtn, opnBalSaveBtn;
	private JPanel orders;
	private RestauwareUtility utility = new RestauwareUtility();
	private JTextField timeTF;
	private JCheckBox absentCB;
	private JRadioButton inTimeRadio, outTimeRadio;
	private JPanel attendOV;
	private Label namelbl, intimelbl, outtimelbl, attendancelbl;
	private JTextField expAmtTF;
	private DBConnection db = null;
	private JTextField expNoTF;
	private int expNo = 1;
	private JTextField totalDepoTF;
	private JTextField totalWithdrawalTF;
	private JTextField availableBalTF;
	private JLabel businessasof;
	private static JButton refreshAllBtn;
	private static Logger log = Logger.getLogger(ManagerHome.class.getName());
	private JTextField cashTF;
	private JTextField cardTF;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManagerHome window = new ManagerHome();
					window.frmManagersHome.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public ManagerHome(User user) {
		initialize(user);
	}

	public ManagerHome() {

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(User user) {
		log.info("Started");
		frmManagersHome = new JFrame();
		frmManagersHome.setSize(800, 600);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frmManagersHome.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frmManagersHome.getHeight()) / 2);
		frmManagersHome.setLocation(x, y);
		frmManagersHome.setResizable(false);
		frmManagersHome.setTitle("Manager's Home");
		frmManagersHome.getContentPane().setFont(new Font("SansSerif", Font.PLAIN, 11));
		frmManagersHome.getContentPane().setBackground(SystemColor.activeCaption);
		frmManagersHome.getContentPane().setLayout(null);

		JTabbedPane mainTabPane = new JTabbedPane(JTabbedPane.TOP);
		mainTabPane.setBounds(10, 65, 774, 496);
		frmManagersHome.getContentPane().add(mainTabPane);
		bodItemCombo = new JComboBox();
		bodItemCombo.setModel(new DefaultComboBoxModel(new String[] { "-- Select --" }));
		bodAvailQtyTF = new JTextField();
		loadInventories();

		attendCatCombo = new JComboBox();
		attendNameCombo = new JComboBox();

		attendCatCombo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (attendCatCombo.getSelectedIndex() != 0) {
					populateNames(attendCatCombo.getSelectedItem().toString());
				} else {
					attendNameCombo.removeAllItems();
					attendNameCombo.addItem("-- Select --");
				}
			}
		});

		mainTabPane.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				if (index == 1) {
					try {
						updateOrderDropdowns();
					} catch (Exception e1) {
						utility.showAleart("Error, while refreshing orders dropdowns due to : " + e1.getMessage());
						log.error("Exception", e1);
					}
				}
			}
		});
		JPanel ops = new JPanel();
		mainTabPane.addTab("Operations", null, ops, null);
		ops.setLayout(null);

		JTabbedPane opsTab = new JTabbedPane(JTabbedPane.TOP);
		opsTab.setBounds(10, 11, 749, 446);
		ops.add(opsTab);

		JPanel bod = new JPanel();
		opsTab.addTab("BOD", null, bod, null);
		bod.setLayout(null);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 96, 639, 212);
		bod.add(panel_2);
		panel_2.setLayout(null);

		JLabel lblInventoryManagement = new JLabel("Inventory Management");
		lblInventoryManagement.setBounds(10, 11, 704, 14);
		panel_2.add(lblInventoryManagement);

		JLabel lblChooseItem = new JLabel("Choose Item :");
		lblChooseItem.setBounds(35, 47, 94, 14);
		panel_2.add(lblChooseItem);

		bodItemCombo.setBounds(124, 44, 224, 20);
		panel_2.add(bodItemCombo);

		bodItemCombo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				updateQtyTfs();
			}
		});
		JLabel lblAvaiableQuantity = new JLabel("Available Quantity :");
		lblAvaiableQuantity.setBounds(369, 46, 129, 14);
		panel_2.add(lblAvaiableQuantity);

		bodAvailQtyTF.setEditable(false);
		bodAvailQtyTF.setBounds(494, 44, 89, 20);
		panel_2.add(bodAvailQtyTF);
		bodAvailQtyTF.setColumns(10);

		JButton btnNewButton_1 = new JButton("Update");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (validateQtyField(bodAddQtyTF.getText().trim(), bodQtyRemTF.getText().trim(), bodItemCombo)) {
					int availQty = Integer.parseInt(bodAvailQtyTF.getText().trim());
					int add = Integer.parseInt(bodAddQtyTF.getText().trim());
					int remove = Integer.parseInt(bodQtyRemTF.getText().trim());
					calculateNewQty(availQty, add, remove);
					bodItemCombo.requestFocusInWindow();
				}
			}

		});
		btnNewButton_1.setBounds(125, 146, 89, 23);
		panel_2.add(btnNewButton_1);

		JLabel lblNewLabel = new JLabel("Add :");
		lblNewLabel.setBounds(79, 87, 38, 14);
		panel_2.add(lblNewLabel);

		bodAddQtyTF = new JTextField();
		bodAddQtyTF.setText("0");
		bodAddQtyTF.setBounds(125, 84, 89, 20);
		panel_2.add(bodAddQtyTF);
		bodAddQtyTF.setColumns(10);

		JLabel lblRemove = new JLabel("Remove :");
		lblRemove.setBounds(63, 115, 53, 14);
		panel_2.add(lblRemove);

		bodQtyRemTF = new JTextField();
		bodQtyRemTF.setText("0");
		bodQtyRemTF.setBounds(125, 115, 89, 20);
		panel_2.add(bodQtyRemTF);
		bodQtyRemTF.setColumns(10);

		JButton inventReset = new JButton("Reset All");
		inventReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetAllInventory();
			}
		});
		inventReset.setBounds(228, 146, 89, 23);
		panel_2.add(inventReset);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 11, 358, 74);
		bod.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblOpeningBalance = new JLabel("Opening Balance :");
		lblOpeningBalance.setBounds(10, 14, 104, 14);
		panel_1.add(lblOpeningBalance);

		openBalTF = new JTextField();
		openBalTF.setBounds(124, 11, 142, 20);
		panel_1.add(openBalTF);
		openBalTF.setColumns(10);

		JLabel lblRs = new JLabel("Rs.");
		lblRs.setBounds(274, 14, 46, 14);
		panel_1.add(lblRs);

		try {
			db = new DBConnection();
			ResultSet rs = db.getOpeningBal();
			while (rs.next()) {
				openBalTF.setText(String.valueOf(rs.getFloat(1)));
				openBalTF.setEditable(false);
			}
		} catch (Exception e) {
			utility.showAleart("Error, while fetching opening balance due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e1) {
				log.error("Exception", e1);
			}
		}

		opnBalSaveBtn = new JButton("Save");
		opnBalSaveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (validateOpenBalTF(openBalTF.getText().trim())) {
					if (openBalTF.isEditable()) {
						float opnBalAmt = Float.parseFloat(openBalTF.getText().trim());
						try {
							db = new DBConnection();
							int result = db.addOpeningBal(opnBalAmt);

							if (result > 0) {
								utility.showAleart("Opening Balance: " + opnBalAmt + " has been set successfully !");
								openBalTF.setEditable(false);
							}
						} catch (Exception e1) {
							utility.showAleart("Error, while setting an opening balance due to : " + e1.getMessage());
							log.error("Exception", e1);
						} finally {
							try {
								db.closeConnection();
							} catch (SQLException e1) {
								log.error("Exception", e1);
							}
						}
					} else {
						utility.showAleart("Opening Balance has already been set for today !");
					}
				}
			}
		});
		opnBalSaveBtn.setBounds(137, 42, 89, 23);
		panel_1.add(opnBalSaveBtn);

		JPanel eod = new JPanel();
		opsTab.addTab("EOD", null, eod, null);
		eod.setLayout(null);

		JButton closeStoreBtn = new JButton("Close the Store");
		closeStoreBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int res = utility.showConfirmation("Are you sure to close the Store?", "Closing Store..!!");
				if (res == 0) {
					boolean validated = validationBeforePerformEOD();
					if (validated) {
						boolean performed = eodActionPerformed(false);
						if (performed) {
							utility.showAleart("Store has been closed successfully..!");
							frmManagersHome.dispose();
							System.exit(0);
						} else {
							utility.showAleart("Unable to performed EOD. Please contact Admin.");
						}
					} else {
						utility.showAleart("One of the Above point were not performed.");
					}
				}
			}
		});
		closeStoreBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		closeStoreBtn.setBounds(187, 201, 215, 56);
		closeStoreBtn.setEnabled(false);
		eod.add(closeStoreBtn);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 724, 140);
		eod.add(scrollPane);

		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		textArea.setText(
				"Points to be considered before closing the store :\n" + "1) Opening Balance was set in the morning.\n"
						+ "2) Inventories are updated as per available in store.\n"
						+ "3) There are no orders currently being served.\n"
						+ "4) Attendance of all the staff including me have been recorded.\n"
						+ "5) All the type of expenses,if any, have been recorded.\n"
						+ "6) After closing the store, no new order will be served for today's date.\n"
						+ "7) The store cannot be reopenned once you close it by pressing below button.");

		JCheckBox agreementCB = new JCheckBox(
				"I am in agreement with all the above points and I am ready to close the store.");
		agreementCB.setBounds(10, 162, 518, 23);
		eod.add(agreementCB);

		agreementCB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (agreementCB.isSelected()) {
					closeStoreBtn.setEnabled(true);
				} else {
					closeStoreBtn.setEnabled(false);
				}
			}
		});

		orders = new JPanel();
		mainTabPane.addTab("Orders", null, orders, null);
		orders.setLayout(null);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(10, 27, 290, 306);
		orders.add(panel_3);
		panel_3.setLayout(null);

		JLabel lblTableNo = new JLabel("Order No:");
		lblTableNo.setBounds(10, 24, 54, 14);
		panel_3.add(lblTableNo);

		getTableNoTF = new JTextField();
		getTableNoTF.setBounds(74, 21, 86, 20);
		panel_3.add(getTableNoTF);
		getTableNoTF.setColumns(10);

		JLabel lblNewOrders = new JLabel("New Orders");
		lblNewOrders.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewOrders.setBounds(10, 0, 108, 14);
		panel_3.add(lblNewOrders);

		newOrderBtn = new JButton("NEW");
		newOrderBtn.setBounds(174, 20, 86, 23);
		panel_3.add(newOrderBtn);

		newOrderBtn.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {

			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				try {
					updateOrderDropdowns();
				} catch (Exception e1) {
					utility.showAleart("Error, while refreshing orders due to: " + e1.getMessage());
					log.error("Exception", e1);
				}
			}
		});

		JLabel servingOrders = new JLabel("Currently Serving");
		servingOrders.setFont(new Font("Tahoma", Font.BOLD, 11));
		servingOrders.setBounds(10, 81, 278, 14);
		panel_3.add(servingOrders);

		JLabel lblNewLabel_2 = new JLabel("Order No: ");
		lblNewLabel_2.setBounds(10, 106, 67, 14);
		panel_3.add(lblNewLabel_2);

		getServingOrderNoCombo = new JComboBox();
		getServingOrderNoCombo.setBounds(74, 103, 83, 20);
		panel_3.add(getServingOrderNoCombo);

		JButton updateServingBtn = new JButton("Update");
		updateServingBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (getServingOrderNoCombo.getSelectedItem() != null) {
					new UpdateOrder(Integer.parseInt(getServingOrderNoCombo.getSelectedItem().toString()));
				} else {
					utility.showAleart("No order is being served currently");
				}
			}
		});
		updateServingBtn.setBounds(174, 102, 86, 23);
		panel_3.add(updateServingBtn);

		JLabel cancelledOrders = new JLabel("Order Cancelled");
		cancelledOrders.setFont(new Font("Tahoma", Font.BOLD, 11));
		cancelledOrders.setBounds(10, 237, 278, 14);
		panel_3.add(cancelledOrders);

		JLabel lblNewLabel_6 = new JLabel("Order No:");
		lblNewLabel_6.setBounds(10, 262, 54, 14);
		panel_3.add(lblNewLabel_6);

		getCancelledOrderNoCombo = new JComboBox();
		getCancelledOrderNoCombo.setBounds(74, 259, 86, 20);
		panel_3.add(getCancelledOrderNoCombo);

		JButton viewCancelledBtn = new JButton("View");
		viewCancelledBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (getCancelledOrderNoCombo.getSelectedItem() != null) {
					new ViewCancelledOrder(Integer.parseInt(getCancelledOrderNoCombo.getSelectedItem().toString()),
							"curdate()");
				} else {
					utility.showAleart("No order is cancelled.");
				}
			}
		});
		viewCancelledBtn.setBounds(174, 258, 86, 23);
		panel_3.add(viewCancelledBtn);

		JLabel servedOrders = new JLabel("Order Served");
		servedOrders.setBounds(10, 161, 278, 14);
		panel_3.add(servedOrders);
		servedOrders.setFont(new Font("Tahoma", Font.BOLD, 11));

		JLabel lblNewLabel_4 = new JLabel("Order No: ");
		lblNewLabel_4.setBounds(10, 186, 67, 14);
		panel_3.add(lblNewLabel_4);

		getServedOrderNoCombo = new JComboBox();
		getServedOrderNoCombo.setBounds(74, 183, 83, 20);
		panel_3.add(getServedOrderNoCombo);

		JButton viewServedBtn = new JButton("View");
		viewServedBtn.setBounds(174, 182, 86, 23);
		panel_3.add(viewServedBtn);
		viewServedBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (getServedOrderNoCombo.getSelectedItem() != null) {
						int orderNo = Integer.parseInt(getServedOrderNoCombo.getSelectedItem().toString());
						String disApplied = "0.0";
						int flag = 0;
						db = new DBConnection();
						ResultSet rs = db.getDiscApplied(orderNo, "curdate()");
						while (rs.next()) {
							disApplied = rs.getString(1);
							flag = 1;
						}
						if (flag == 1) {
							new GenerateBill(orderNo, disApplied, true, "curdate()");
						}
					} else {
						utility.showAleart("No order is served.");
					}
				} catch (Exception e1) {
					utility.showAleart("Error, while clickling one view Served order due to : " + e1.getMessage());
					log.error("Exception", e1);
				} finally {
					try {
						db.closeConnection();
					} catch (SQLException e1) {
						log.error("Exception", e1);
					}
				}
			}
		});

		refreshAllBtn = new JButton("Refresh All Orders");
		refreshAllBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		refreshAllBtn.setBounds(310, 111, 241, 102);
		refreshAllBtn.setVisible(false);
		orders.add(refreshAllBtn);

		refreshAllBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					updateOrderDropdowns();
				} catch (Exception e1) {
					utility.showAleart("Error, while updating order dropdowns due to : " + e1.getMessage());
					log.error("Exception", e1);
				}
			}
		});

		JSeparator separator = new JSeparator();
		separator.setBounds(24, 84, 276, 11);
		orders.add(separator);

		newOrderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new AddNewOrders(Integer.parseInt(getTableNoTF.getText().trim()));
					frmManagersHome.setAlwaysOnTop(false);
				} catch (Exception e1) {
					utility.showAleart("Error, while creating new order due to : " + e1.getMessage());
					log.error("Exception", e1);
				}
			}
		});

		JPanel attendance = new JPanel();
		mainTabPane.addTab("Attendance", null, attendance, null);
		attendance.setLayout(null);

		JLabel lblCategory = new JLabel("Category : ");
		lblCategory.setBounds(25, 28, 77, 14);
		attendance.add(lblCategory);

		attendCatCombo.setModel(new DefaultComboBoxModel(new String[] { "-- Select --" }));
		attendCatCombo.setBounds(112, 25, 116, 20);
		attendance.add(attendCatCombo);

		JLabel lblName = new JLabel("Name :");
		lblName.setBounds(25, 63, 59, 14);
		attendance.add(lblName);

		attendNameCombo.setModel(new DefaultComboBoxModel(new String[] { "-- Select --" }));
		attendNameCombo.setBounds(112, 60, 116, 20);
		attendance.add(attendNameCombo);
		loadAttendacePage();

		timeTF = new JTextField();
		timeTF.setBounds(35, 152, 86, 20);
		attendance.add(timeTF);
		timeTF.setColumns(10);

		absentCB = new JCheckBox("Mark Absent");
		absentCB.setBounds(25, 96, 97, 23);
		attendance.add(absentCB);

		absentCB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (absentCB.isSelected()) {
					timeTF.setEditable(false);
				} else {
					timeTF.setEditable(true);
				}
			}
		});

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (validatedAttendanceForm(attendCatCombo.getSelectedItem().toString(),
						attendNameCombo.getSelectedItem().toString(), timeTF.getText().trim(), absentCB.isSelected())) {
					saveOrUpdateAttendace(attendCatCombo.getSelectedItem().toString(),
							attendNameCombo.getSelectedItem().toString(), timeTF.getText().trim(),
							absentCB.isSelected(), inTimeRadio.isSelected());
					clearAttendanceForm();
					attendOV.removeAll();
					attendanceOverview();
				}
			}

		});
		btnSave.setBounds(176, 151, 89, 23);
		attendance.add(btnSave);

		JLabel lblNewLabel_3 = new JLabel("HH.MM");
		lblNewLabel_3.setBounds(131, 155, 46, 14);
		attendance.add(lblNewLabel_3);

		inTimeRadio = new JRadioButton("In-Time");
		inTimeRadio.setSelected(true);
		inTimeRadio.setBounds(25, 122, 77, 23);
		attendance.add(inTimeRadio);

		outTimeRadio = new JRadioButton("Out-Time");
		outTimeRadio.setBounds(110, 122, 97, 23);
		attendance.add(outTimeRadio);

		ButtonGroup b = new ButtonGroup();
		b.add(inTimeRadio);
		b.add(outTimeRadio);

		attendOV = new JPanel();
		attendOV.setBounds(308, 11, 451, 446);
		attendance.add(attendOV);
		attendOV.setLayout(null);

		JPanel expenses = new JPanel();
		mainTabPane.addTab("Expenses", null, expenses, null);
		expenses.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 355, 226);
		expenses.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel_5 = new JLabel("Expense Reason :");
		lblNewLabel_5.setBounds(21, 57, 110, 14);
		panel.add(lblNewLabel_5);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(129, 51, 216, 63);
		panel.add(scrollPane_1);

		JTextArea expReasonTA = new JTextArea();
		scrollPane_1.setViewportView(expReasonTA);

		JLabel lblNewLabel_7 = new JLabel("Amount :");
		lblNewLabel_7.setBounds(52, 132, 67, 14);
		panel.add(lblNewLabel_7);

		expAmtTF = new JTextField();
		expAmtTF.setBounds(129, 129, 86, 20);
		panel.add(expAmtTF);
		expAmtTF.setColumns(10);

		JButton expSaveBtn = new JButton("Save");
		expSaveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String reason = expReasonTA.getText().trim();
				String amount = expAmtTF.getText().trim();
				if (validatedExpFields(reason, amount)) {
					boolean result = getLastestExpNo();
					if (result) {
						int save = saveExpense(expNo, reason, amount);
						if (save > 0) {
							utility.showAleart("Expense has been recorded successfully.");
							expReasonTA.setText("");
							expAmtTF.setText("");
							expReasonTA.requestFocusInWindow();
						} else {
							utility.showAleart("Expense was not recorded. Please contact Admin");
						}
					} else {
						utility.showAleart("Unable to generate exp no.");
					}
				}
			}
		});

		expSaveBtn.setBounds(126, 160, 89, 23);
		panel.add(expSaveBtn);

		expNoTF = new JTextField();
		expNoTF.setEditable(false);
		expNoTF.setBounds(129, 18, 86, 20);
		panel.add(expNoTF);
		expNoTF.setColumns(10);

		JPanel totBusiness = new JPanel();
		mainTabPane.addTab("Business", null, totBusiness, null);
		totBusiness.setLayout(null);

		JButton btnNewButton = new JButton("Business As of Now");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getAvailableBal();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton.setBounds(82, 171, 204, 38);
		totBusiness.add(btnNewButton);

		JLabel lblTotalDeposite = new JLabel("Total Deposit :");
		lblTotalDeposite.setBounds(61, 63, 103, 14);
		totBusiness.add(lblTotalDeposite);

		totalDepoTF = new JTextField();
		totalDepoTF.setEditable(false);
		totalDepoTF.setBounds(175, 60, 111, 20);
		totBusiness.add(totalDepoTF);
		totalDepoTF.setColumns(10);

		JLabel lblNewLabel_8 = new JLabel("Total Withdrawal :");
		lblNewLabel_8.setBounds(61, 94, 103, 14);
		totBusiness.add(lblNewLabel_8);

		totalWithdrawalTF = new JTextField();
		totalWithdrawalTF.setEditable(false);
		totalWithdrawalTF.setBounds(175, 91, 112, 20);
		totBusiness.add(totalWithdrawalTF);
		totalWithdrawalTF.setColumns(10);

		JLabel lblNewLabel_9 = new JLabel("Business Done :");
		lblNewLabel_9.setBounds(61, 122, 111, 14);
		totBusiness.add(lblNewLabel_9);

		availableBalTF = new JTextField();
		availableBalTF.setEditable(false);
		availableBalTF.setBounds(175, 119, 111, 20);
		totBusiness.add(availableBalTF);
		availableBalTF.setColumns(10);

		businessasof = new JLabel("Business as of :");
		businessasof.setBounds(61, 27, 225, 14);
		totBusiness.add(businessasof);
		
		JLabel lblNewLabel_10 = new JLabel("Cash :");
		lblNewLabel_10.setBounds(371, 66, 46, 14);
		totBusiness.add(lblNewLabel_10);
		
		cashTF = new JTextField();
		cashTF.setEditable(false);
		cashTF.setBounds(417, 63, 111, 20);
		totBusiness.add(cashTF);
		cashTF.setColumns(10);
		
		JLabel lblNewLabel_11 = new JLabel("Card :");
		lblNewLabel_11.setBounds(371, 94, 46, 14);
		totBusiness.add(lblNewLabel_11);
		
		cardTF = new JTextField();
		cardTF.setEditable(false);
		cardTF.setBounds(417, 91, 111, 20);
		totBusiness.add(cardTF);
		cardTF.setColumns(10);
		
		JLabel lblNewLabel_12 = new JLabel("Total Deposit = Opening Balance + All Orders Payment");
		lblNewLabel_12.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblNewLabel_12.setBounds(61, 245, 467, 14);
		totBusiness.add(lblNewLabel_12);
		
		JLabel lblTotalWithdrawal = new JLabel("Total Withdrawal = Sum of all the expenses");
		lblTotalWithdrawal.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblTotalWithdrawal.setBounds(61, 270, 467, 14);
		totBusiness.add(lblTotalWithdrawal);
		expNoTF.setVisible(false);

		JPanel panel_4 = new JPanel();
		panel_4.setBounds(10, 11, 774, 54);
		frmManagersHome.getContentPane().add(panel_4);
		panel_4.setLayout(null);

		String manager = "Manager";
		try {
			db = new DBConnection();
			manager = db.getAdminName(user);
		} catch (Exception e2) {
			utility.showAleart("Error, while getting an Admin user due to : " + e2.getMessage());
			log.error("Exception", e2);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e1) {
				log.error("Exception", e1);
			}
		}

		JLabel lblNewLabel_1 = new JLabel("Welcome " + manager + ",");
		lblNewLabel_1.setBounds(10, 11, 245, 14);
		panel_4.add(lblNewLabel_1);

		JLabel logoutLbl = new JLabel("Logout");
		logoutLbl.setBounds(673, 10, 91, 14);
		logoutLbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_4.add(logoutLbl);

		logoutLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		logoutLbl.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				logoutLbl.setForeground(Color.BLACK);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				logoutLbl.setForeground(Color.RED);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				int i = utility.showConfirmation("Are you Sure?", "Logging Off !!");
				if (i == 0) {
					log.info(new Date().toString() + " > ManagerHome logging off.");
					frmManagersHome.dispose();
					Welcome w = new Welcome(0);
				}

			}
		});

		frmManagersHome.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmManagersHome.setVisible(true);
		attendanceOverview();
		clickrefreshAllBtn();
		log.info("Completed");
	}

	private int getOrderNo() {
		log.info("Started");
		int result = 1;
		try {
			db = new DBConnection();
			ResultSet rs = db.getLatestOrderNo();
			while (rs.next()) {
				result = rs.getInt(1) + 1;
			}
		} catch (Exception e) {
			utility.showAleart("Error, while fetching latest order no due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
		return result;
	}

	private void getLiveOrderNos() {
		log.info("Started");
		getServingOrderNoCombo.removeAllItems();
		try {
			db = new DBConnection();
			ResultSet rs = db.getLiveOrderNo();
			while (rs.next()) {
				getServingOrderNoCombo.addItem(String.valueOf(rs.getInt(1)));
			}
		} catch (Exception e) {
			utility.showAleart("Error, while fetching currently served orders due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
	}

	private void getServedOrderNos() {
		log.info("Started");
		getServedOrderNoCombo.removeAllItems();
		try {
			db = new DBConnection();
			ResultSet rs = db.getServedOrderNo();
			while (rs.next()) {
				getServedOrderNoCombo.addItem(String.valueOf(rs.getInt(1)));
			}
		} catch (Exception e) {
			utility.showAleart("Error, while fetching served orders due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
	}

	private void getCancelledOrderNos() {
		log.info("Started");
		getCancelledOrderNoCombo.removeAllItems();
		try {
			db = new DBConnection();
			ResultSet rs = db.getCancelledOrderNo();
			while (rs.next()) {
				getCancelledOrderNoCombo.addItem(String.valueOf(rs.getInt(1)));
			}
		} catch (Exception e) {
			utility.showAleart("Error, while fetching cancelled orders due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
	}

	private void loadInventories() {
		log.info("Started");
		try {
			db = new DBConnection();
			ResultSet rs = db.getMainInventoryItems();
			while (rs.next()) {
				bodItemCombo.addItem(rs.getString(1));
			}
		} catch (Exception e) {
			utility.showAleart("Error, while fetching inventory items due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
	}

	private void updateQtyTfs() {
		log.info("Started");
		try {
			db = new DBConnection();
			ResultSet rs = db.getInventoryQtyByItem(bodItemCombo.getSelectedItem().toString());
			while (rs.next()) {
				bodAvailQtyTF.setText(String.valueOf(rs.getInt(1)));
			}

		} catch (Exception e) {
			utility.showAleart("Error, while fetching available qty of an inventory due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
	}

	private void calculateNewQty(int available, int add, int minus) {
		log.info("Started");
		int newQty = available + add - minus;
		try {
			db = new DBConnection();
			int result = db.updateInventoryQtyByItem(bodItemCombo.getSelectedItem().toString(), newQty);
			if (result > 0) {
				utility.showAleart("Quantity Updated Successfully..!");
				updateQtyTfs();
				clearInventForm();
			}
		} catch (Exception e) {
			utility.showAleart("Error, while updating inventory qty due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
	}

	private void resetAllInventory() {
		log.info("Started");
		int ans = utility.showConfirmation("This will make all the item's quantity as 0.", "Are you sure?");
		if (ans == 0) {
			try {
				db = new DBConnection();
				int result = db.resetInventory();
				if (result > 0) {
					utility.showAleart("Reset Done..!");
					updateQtyTfs();
					clearInventForm();
				}
			} catch (Exception e) {
				utility.showAleart("Error, while resetting all inventory due to : " + e.getMessage());
				log.error("Exception", e);
			} finally {
				try {
					db.closeConnection();
				} catch (SQLException e) {
					log.error("Exception", e);
				}
			}
		}
		log.info("Completed");
	}

	private void loadAttendacePage() {
		log.info("Started");
		try {
			db = new DBConnection();
			ResultSet rs = db.getStaffCategories();
			while (rs.next()) {
				attendCatCombo.addItem(rs.getString(1));
			}

			ResultSet rs1 = db.getStaffName();
			while (rs1.next()) {
				attendNameCombo.addItem(rs1.getString(1));
			}
		} catch (Exception e) {
			utility.showAleart("Error, while fetching attendance details due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
	}

	private void clearInventForm() {
		bodItemCombo.setSelectedItem(0);
		bodAddQtyTF.setText("0");
		bodQtyRemTF.setText("0");
	}

	private void updateOrderDropdowns() throws Exception {
		log.info("Started");
		getTableNoTF.setEditable(false);
		getTableNoTF.setText(String.valueOf(getOrderNo()));
		getLiveOrderNos();
		getServedOrderNos();
		getCancelledOrderNos();
		log.info("Completed");
	}

	private boolean validateOpenBalTF(String openBal) {
		log.info("Started");
		boolean result = true;
		if (openBal.equals("")) {
			utility.showAleart("Opening Balance cannot be blank.");
			return false;
		}
		if (!openBal.matches("^([+-]?(\\d+\\.)?\\d+)$") || !openBal.matches(".*\\d+.*")) {
			utility.showAleart("Opening Balance must be number or decimal number.");
			return false;
		}
		log.info("Completed");
		return result;
	}

	private void populateNames(String cat) {
		log.info("Started");
		try {
			attendNameCombo.removeAllItems();
			int flag = 0;
			db = new DBConnection();
			ResultSet rs = db.getStaffNameByCategory(cat);
			while (rs.next()) {
				flag = 1;
				attendNameCombo.addItem(rs.getString(1));
			}
			if (flag == 0) {
				utility.showAleart("Did not find anybody under " + cat + " category.");
			}
		} catch (Exception e) {
			utility.showAleart("Error, while fetching staff category due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
	}

	private boolean validatedAttendanceForm(String cat, String name, String time, boolean isAbsent) {
		log.info("Started");
		if (cat.equals("-- Select --")) {
			utility.showAleart("Please choose valid category");
			return false;
		}
		if (!time.matches("^(0[0-9]|1[0-9]|2[0-3]).[0-5][0-9]$") && !isAbsent) {
			utility.showAleart("Time must match HH.MM ie. 24 hr format not in am/pm(e.g. 20.10 or 09.05) format");
			return false;
		}
		log.info("Completed");
		return true;
	}

	private void saveOrUpdateAttendace(String cat, String fnamelname, String time, boolean isAbsent, boolean inTime) {
		log.info("Started");
		int no = 0;
		try {
			db = new DBConnection();
			if (isAbsent) {
				no = db.addorUpdateAttendance(cat, fnamelname, null, null, "A");
				if (no > 0) {
					utility.showAleart("Marked as Absent successfully.");
				}
			} else if (inTime) {
				no = db.addorUpdateAttendance(cat, fnamelname, time, null, "P");
				if (no > 0) {
					utility.showAleart("In-Time recorded successfully.");
				}
			} else {
				no = db.addorUpdateAttendance(cat, fnamelname, null, time, "P");
				if (no > 0) {
					utility.showAleart("Out-Time recorded successfully.");
				}
			}
		} catch (Exception e) {
			utility.showAleart("Error, while updating attendance due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
	}

	private void clearAttendanceForm() {
		// TODO Auto-generated method stub
		attendCatCombo.setSelectedIndex(0);
		attendNameCombo.setSelectedItem(0);
		absentCB.setSelected(false);
		inTimeRadio.setSelected(true);
		timeTF.setEditable(true);
		timeTF.setText("");
		attendCatCombo.requestFocusInWindow();
	}

	private void attendanceOverview() {
		log.info("Started");
		try {
			attendOV.setVisible(true);
			JLabel nameHeader = new JLabel("Name");
			nameHeader.setBounds(10, 11, 170, 14);
			attendOV.add(nameHeader);

			JLabel intimeHeader = new JLabel("In-Time");
			intimeHeader.setBounds(190, 11, 73, 14);
			attendOV.add(intimeHeader);

			JLabel outtimeheader = new JLabel("Out-Time");
			outtimeheader.setBounds(267, 11, 73, 14);
			attendOV.add(outtimeheader);

			JLabel attendanceHeader = new JLabel("Attendance");
			attendanceHeader.setBounds(350, 11, 91, 14);
			attendOV.add(attendanceHeader);
			db = new DBConnection();
			ResultSet rs = db.getAttendanceOverview();
			int yaxis = 11 + 19;
			while (rs.next()) {
				namelbl = new Label((rs.getString(1) == null) ? "--" : rs.getString(1));
				namelbl.setBounds(10, yaxis, 170, 14);
				intimelbl = new Label((rs.getString(2) == null) ? "--" : rs.getString(2));
				intimelbl.setBounds(190, yaxis, 73, 14);
				outtimelbl = new Label((rs.getString(3) == null) ? "--" : rs.getString(3));
				outtimelbl.setBounds(267, yaxis, 73, 14);
				attendancelbl = new Label((rs.getString(4) == null) ? "--" : rs.getString(4));
				attendancelbl.setBounds(350, yaxis, 91, 14);
				attendOV.add(namelbl);
				attendOV.add(intimelbl);
				attendOV.add(outtimelbl);
				attendOV.add(attendancelbl);
				yaxis = yaxis + 19;
			}
		} catch (Exception e) {
			utility.showAleart("Error, while rendering attendance due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
	}

	private boolean validatedExpFields(String reason, String amount) {
		log.info("Started");
		if (reason.equals("")) {
			utility.showAleart("Reason Cannot not be blank.");
			return false;
		}
		if (amount.equals("")) {
			utility.showAleart("Amount Cannot not be blank.");
			return false;
		}
		if (!amount.matches("^([+-]?(\\d+\\.)?\\d+)$") || !amount.matches(".*\\d+.*")) {
			utility.showAleart("Amount must be number or decimal number.");
			return false;
		}
		log.info("Completed");
		return true;
	}

	private int saveExpense(int expNo, String reason, String amount) {
		log.info("Started");
		int result = 0;
		try {
			db = new DBConnection();
			result = db.addNewExpense(expNo, reason, amount);
		} catch (Exception e) {
			utility.showAleart("Error, while saving an expense :" + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
		return result;
	}

	private boolean getLastestExpNo() {
		log.info("Started");
		boolean result = false;
		try {
			db = new DBConnection();
			ResultSet rs = db.getLastestExpNoForToday();
			while (rs.next()) {
				expNo = expNo + rs.getInt(1);
			}
			result = true;
		} catch (Exception e1) {
			utility.showAleart("Error, while fetching latest expense no due to : " + e1.getMessage());
			log.error("Exception", e1);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e1) {
				log.error("Exception", e1);
			}
		}
		log.info("Completed");
		return result;
	}

	private void getAvailableBal() {
		log.info("Started");
		float totalDep = 0.0f;
		float totalWithdr = 0.0f;
		float availableBal = 0.0f;
		String pattern = "dd-MMM-yyyy hh:mm:ss";
		String dateInString = new SimpleDateFormat(pattern).format(new Date());
		try {
			db = new DBConnection();
			ResultSet rs = db.getAllDepositAmount();
			while (rs.next()) {
				totalDep = totalDep + rs.getFloat(1);
			}
			totalDepoTF.setText(String.valueOf(totalDep));

			rs = db.getAllWithdrawnAmount();
			while (rs.next()) {
				totalWithdr = totalWithdr + rs.getFloat(1);
			}
			totalWithdrawalTF.setText(String.valueOf(totalWithdr));
			availableBal = totalDep - totalWithdr;

			availableBalTF.setText(String.valueOf(availableBal));
			businessasof.setText("Businees as of : " + dateInString);
			
			float totalPaidByCash = 0.0f;
			float totalPaidByCard = 0.0f;
			rs = db.getTodaysAllOrdersByPaidBy("Cash");
			while (rs.next()) {
				totalPaidByCash = totalPaidByCash + Float.parseFloat(rs.getString(1));
			}

			rs = db.getTodaysAllOrdersByPaidBy("Card");
			while (rs.next()) {
				totalPaidByCard = totalPaidByCard + Float.parseFloat(rs.getString(1));
			}
			
			cashTF.setText(String.valueOf(totalPaidByCash));
			cardTF.setText(String.valueOf(totalPaidByCard));

		} catch (Exception e) {
			utility.showAleart("Error, while fetching/calculating business as of now due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
	}

	private boolean validateQtyField(String addQty, String remQty, JComboBox bodItemCombo) {
		log.info("Started");
		if (bodItemCombo.getSelectedIndex() == 0) {
			utility.showAleart("Please select Item first");
			return false;
		}
		if (addQty.equals("")) {
			utility.showAleart("Adding Quantity cannot be not be blank.");
			return false;
		}
		if (remQty.equals("")) {
			utility.showAleart("Removing Quantity cannot be not be blank.");
			return false;
		}
		if (!addQty.matches("\\d*") || !remQty.matches("\\d*")) {
			utility.showAleart("Quantity must be in numbers");
			return false;
		}
		log.info("Completed");
		return true;
	}

	private boolean validationBeforePerformEOD() {
		log.info("Started");
		boolean validated = false;
		boolean openingBalSet = false;
		boolean noServingOrders = true;
		try {
			// checkOpeninBal is set or not
			db = new DBConnection();
			ResultSet rs = db.getOpeningBal();
			while (rs.next()) {
				openingBalSet = true;
				break;
			}

			// There are no orders being served
			rs = db.getLiveOrderNo();
			while (rs.next()) {
				noServingOrders = false;
			}
		} catch (Exception e) {
			utility.showAleart("Error, while fetching an opening balance due to : " + e.getMessage());
			log.error("Exception", e);
			return false;
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception", e);
			}
		}
		if (!openingBalSet) {
			utility.showAleart("Opening Balance is not set.");
		} else if (!noServingOrders) {
			utility.showAleart("Order(s) is/are being served.");
		}
		validated = openingBalSet && noServingOrders;
		log.info("Completed");
		return validated;
	}

	public boolean eodActionPerformed(boolean forceClose) {
		log.info("Started");
		boolean performed = false;
		final JOptionPane optionPane = new JOptionPane(
				"Please wait,Performing EOD..! \nIt might take few minutes..!\nHave patience :)",
				JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[] {}, null);

		final JDialog dialog = new JDialog();
		dialog.setTitle("Do not close/shutdown");
		dialog.setModal(true);
		dialog.setSize(250, 150);
		dialog.setContentPane(optionPane);
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - dialog.getWidth()) / 2;
		final int y = (screenSize.height - dialog.getHeight()) / 2;
		dialog.setLocation(x, y);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.pack();

		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
			@Override
			protected String doInBackground() throws InterruptedException {
				boolean done = performEOD(forceClose);
				return String.valueOf(done);
			}

			@Override
			protected void done() {
				dialog.dispose();

			}
		};
		worker.execute();
		dialog.setVisible(true);
		try {
			String done = worker.get();
			if (done.equals("true")) {
				performed = true;
			}
		} catch (Exception e1) {
			utility.showAleart("Error, while performing EOD due to : " + e1.getMessage());
			log.error("Exception", e1);
		}
		log.info("Completed");
		return performed;
	}

	public boolean performEOD(boolean forceClose) {
		log.info("Started");
		boolean performed = false;
		boolean updated = false;
		String line = "";
		BufferedWriter out = null;
		CreatEODReport report = null;
		try {
			// creating directory
			boolean created = utility.createDirectory();

			if (created) {
				report = new CreatEODReport();
				out = report.createReport();
				db = new DBConnection();

				// ------ Opening Balance
				ResultSet rs = db.getOpeningBal();
				float openBal = 0.0f;
				while (rs.next()) {
					line = line + String.valueOf(rs.getFloat(1));
					openBal = openBal + rs.getFloat(1);
				}
				updated = report.openingBalanceTable(out, line);

				// ------ Inventory
				line = "";
				rs = db.getTodaysInventory();
				report.inventoryTableHeader();
				while (rs.next()) {
					line = line + "<tr><td>" + rs.getString(1) + "</td><td>" + rs.getInt(2) + "</td></tr>";
				}
				report.inventoryTableContent(out, line);
				report.inventoryTableComplete();

				// ---- Expenses
				line = "";
				rs = db.getTodaysExpense();
				report.expensesTableHeader();
				float totalExp = 0.0f;
				while (rs.next()) {
					line = line + "<tr><td>" + rs.getInt(1) + "</td><td>" + rs.getString(2) + "</td><td>"
							+ rs.getFloat(3) + "</td><td>" + rs.getTimestamp(4) + "</td></tr>";
					totalExp = totalExp + rs.getFloat(3);
				}
				line = line + "<tr bgColor=yellow><td colspan=4><b>Total Expenses = " + totalExp + "</b></td></tr>";
				report.expensesTableContent(out, line);
				report.expensesTableComplete();

				// ------ Attendance
				line = "";
				rs = db.getTodaysAttendance();
				report.attendanceTableHeader();
				while (rs.next()) {
					String duration = null;
					LocalTime inTime = null;
					LocalTime outTime = null;
					String inT = null;
					String outT = null;
					if (rs.getString(3) != null && rs.getString(4) != null) {
						inT = rs.getString(3).replace('.', ':');
						outT = rs.getString(4).replace('.', ':');
						inTime = LocalTime.parse(inT);
						outTime = LocalTime.parse(outT);
						duration = Duration.between(outTime, inTime).toString().substring(3).replaceAll("-", ":");
					}
					line = line + "<tr><td>" + rs.getString(1) + "</td><td>" + rs.getString(2) + "</td><td>"
							+ rs.getString(3) + "</td><td>" + rs.getString(4) + "</td><td>" + duration + "</td><td>"
							+ rs.getString(5) + "</td><td>" + rs.getTimestamp(6) + "</td><td>" + rs.getTimestamp(7)
							+ "</td></tr>";

				}
				report.attendanceTableContent(out, line);
				report.attendanceTableComplete();

				// ------ Paid Orders
				line = "";
				ArrayList<Integer> servedOrderNos = new ArrayList<Integer>();
				rs = db.getServedOrderNo();
				while (rs.next()) {
					servedOrderNos.add(rs.getInt(1));
				}
				report.servedOrderTableHeader();
				for (int i = 0; i < servedOrderNos.size(); i++) {
					rs = db.getServedOrderEODDetails(servedOrderNos.get(i));
					while (rs.next()) {
						line = line + "<tr>" + "<td>" + rs.getInt(1) + "</td>" + "<td>" + rs.getString(2) + "</td>"
								+ "<td>" + rs.getFloat(3) + "</td>" + "<td>" + rs.getFloat(4) + "</td>" + "<td>"
								+ rs.getFloat(5) + "</td>" + "<td>" + rs.getFloat(6) + "</td>" + "<td>" + rs.getFloat(7)
								+ "</td>" + "<td>" + rs.getTimestamp(8) + "</td>" + "<td>" + rs.getTimestamp(9)
								+ "</td>" + "<td>" + rs.getString(10) + "</td>" + "</tr>";
					}
				}
				float totalPaidByCash = 0.0f;
				float totalPaidByCard = 0.0f;
				rs = db.getTodaysAllOrdersByPaidBy("Cash");
				while (rs.next()) {
					totalPaidByCash = totalPaidByCash + Float.parseFloat(rs.getString(1));
				}

				rs = db.getTodaysAllOrdersByPaidBy("Card");
				while (rs.next()) {
					totalPaidByCard = totalPaidByCard + Float.parseFloat(rs.getString(1));
				}

				line = line + "<tr bgcolor='yellow'>" + "<td colspan=5>Total Paid By Cash : " + totalPaidByCash
						+ "</td>" + "<td colspan=5>Total Paid By Card : " + totalPaidByCard + "</td></tr>";
				report.servedOrderTableContent(out, line);
				report.servedOrderTableComplete();

				// ------ Cancelled Orders
				line = "";
				ArrayList<Integer> cancelledOrderNos = new ArrayList<Integer>();
				rs = db.getCancelledOrderNo();
				while (rs.next()) {
					cancelledOrderNos.add(rs.getInt(1));
				}
				report.cancelledOrderTableHeader();
				for (int i = 0; i < cancelledOrderNos.size(); i++) {
					rs = db.getCancelledOrderEODDetails(cancelledOrderNos.get(i));
					while (rs.next()) {
						line = line + "<tr>" + "<td>" + rs.getInt(1) + "</td>" + "<td>" + rs.getString(2) + "</td>"
								+ "<td>" + rs.getFloat(3) + "</td>" + "<td>" + rs.getFloat(4) + "</td>" + "<td>"
								+ rs.getFloat(5) + "</td>" + "<td>" + rs.getTimestamp(6) + "</td>" + "<td>"
								+ rs.getTimestamp(7) + "</td>" + "<td>" + rs.getString(8) + "</td>" + "</tr>";
					}
				}
				report.cancelledOrderTableContent(out, line);
				report.cancelledOrderTableComplete();

				// ------ All Transactions
				line = "";
				rs = db.getTodaysTransactions();
				report.transactionTableHeader();
				while (rs.next()) {
					line = line + "<tr><td>" + rs.getString(1) + "</td><td>" + rs.getFloat(2) + "</td><td>"
							+ (rs.getInt(3) == 1 ? "Deposited" : "Withdrawn") + "</td><td>" + rs.getTimestamp(4)
							+ "</td></tr>";
				}
				report.transactionTableContent(out, line);
				report.transactionTableComplete();

				// -- Total Business
				float availableBal = openBal + totalPaidByCash - totalExp;
				ArrayList<Float> businessBal = getTodaysBusinessBal(forceClose);
				line = "";
				report.totalBusinessTableHeader();
				line = line + "<tr><td>" + businessBal.get(0) + "</td><td>" + businessBal.get(1) + "</td>" + "<td>"
						+ businessBal.get(2) + "</td></tr><tr bgColor=yellow><td colspan=3><br><b>Available Balance = "
						+ availableBal
						+ "</b><br><i>(Opening Balance + Order Paid by Cash - All Expenses)</i></td></tr>";
				report.totalBusinessTableContent(out, line);
				report.totalBusinessTableComplete();

				report.closeBufferedWriter();
				out.close();
				// sending email..
				boolean sendEmail = new SendingEODReport().sendingReports();
				if (sendEmail) {
					db = new DBConnection();
					int no = db.closeTheStore();
					if (no > 0) {
						performed = true;
					}
				} else {
					utility.showAleart("Could not send the report, so cannot close the store!");
				}
			} else {
				utility.showAleart("Failed to create directory, please contact admin!");
			}
		} catch (Exception e) {
			utility.showAleart("Error, while performing EOD due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (Exception e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
		return performed;
	}

	private ArrayList<Float> getTodaysBusinessBal(boolean forceClose) {
		log.info("Started");
		float totalDep = 0.0f;
		float totalWithdr = 0.0f;
		float totalBussiness = 0.0f;
		float availbleBal = 0.0f;
		ArrayList<Float> bal = new ArrayList<Float>();
		try {
			db = new DBConnection();
			ResultSet rs = db.getAllDepositAmount();
			while (rs.next()) {
				totalDep = totalDep + rs.getFloat(1);
			}
			if (!forceClose) {
				totalDepoTF.setText(String.valueOf(totalDep));
			}

			rs = db.getAllWithdrawnAmount();
			while (rs.next()) {
				totalWithdr = totalWithdr + rs.getFloat(1);
			}
			if (!forceClose) {
				totalWithdrawalTF.setText(String.valueOf(totalWithdr));
			}
			totalBussiness = totalDep - totalWithdr;
			bal.add(totalDep);
			bal.add(totalWithdr);
			bal.add(totalBussiness);
		} catch (Exception e) {
			utility.showAleart("Error, while getting today's total business due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
		return bal;
	}

	public void clickrefreshAllBtn() {
		refreshAllBtn.doClick();
	}
}
