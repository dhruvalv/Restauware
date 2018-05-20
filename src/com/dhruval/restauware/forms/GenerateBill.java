package com.dhruval.restauware.forms;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.dhruval.restauware.dao.ItemData;
import com.dhruval.restauware.dao.PrintableObject;
import com.dhruval.restauware.db.DBConnection;
import com.dhruval.restauware.print.FinalReceipt;
import com.dhruval.restauware.utilities.RestauwareUtility;

public class GenerateBill {

	private JFrame frmGeneratingBill;
	private int yaxis = 313;
	private float subTotal = 0.0f;
	private float discAmt = 0.0f;
	private float amtAfterTax = 0.0f;
	private RestauwareUtility utility;
	private JComboBox payByCombo;
	private float totalTaxApplied = 0.0f, finalAmt = 0.0f;
	private JTable table;
	private DefaultTableModel model;
	private JScrollPane billContentPane;
	private DBConnection db;
	private String[][] data;
	private JLabel billNum;
	private JLabel dateTime;
	private JLabel subTotalvalue;
	private Map<String, Float> taxes;
	private JLabel disValuelbl;
	private JLabel dis;
	private JLabel subTotallbl;
	private JLabel totalValue;
	private ArrayList<String> taxList = new ArrayList<String>();
	private float amtAfterDisc;
	private JButton updatePayByBtn;
	private static Logger log = Logger.getLogger(GenerateBill.class.getName());
	public static String restauName;
	public static String restauAdd;
	public static String line1="";
	public static String line2="";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GenerateBill window = new GenerateBill(18, "0.0", false, "");
					window.frmGeneratingBill.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GenerateBill() {
	}

	public GenerateBill(int orderNo, String discount, boolean reprint, String orderDate) {
		initialize(orderNo, discount, reprint, orderDate);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(int orderNo, String discount, boolean reprint, String orderDate) {
		log.info("Started");
		utility = new RestauwareUtility();
		frmGeneratingBill = new JFrame();
		frmGeneratingBill.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
		frmGeneratingBill.getContentPane().setBackground(UIManager.getColor("ScrollPane.background"));
		frmGeneratingBill.setResizable(false);
		frmGeneratingBill.setTitle("Bill For Order No : " + orderNo);
		frmGeneratingBill.setSize(450, 539);
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - frmGeneratingBill.getWidth()) / 2;
		final int y = (screenSize.height - frmGeneratingBill.getHeight()) / 2;
		frmGeneratingBill.setLocation(x, y + 10);
		frmGeneratingBill.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmGeneratingBill.getContentPane().setLayout(null);

		JPanel billHeader = new JPanel();
		billHeader.setBackground(UIManager.getColor("ScrollPane.background"));
		billHeader.setBounds(10, 11, 424, 93);
		frmGeneratingBill.getContentPane().add(billHeader);
		billHeader.setLayout(null);

		JLabel restauname = new JLabel("My Restaurant");
		restauname.setFont(new Font("Tahoma", Font.BOLD, 12));
		restauname.setBounds(162, 11, 242, 24);
		billHeader.add(restauname);

		JLabel restauadd = new JLabel("Address Of Restaurant");
		restauadd.setFont(new Font("Tahoma", Font.PLAIN, 10));
		restauadd.setBounds(10, 36, 394, 14);
		billHeader.add(restauadd);

		JLabel lblNewLabel = new JLabel("Bill No :");
		lblNewLabel.setBounds(10, 74, 54, 14);
		billHeader.add(lblNewLabel);

		JLabel bilDateTime = new JLabel("DateTime :");
		bilDateTime.setBounds(229, 74, 74, 14);
		billHeader.add(bilDateTime);

		billNum = new JLabel("0");
		billNum.setBounds(62, 74, 165, 14);
		billHeader.add(billNum);

		dateTime = new JLabel("0/0/0");
		dateTime.setBounds(290, 74, 134, 14);
		billHeader.add(dateTime);
		
		payByCombo = new JComboBox();
		payByCombo.setModel(new DefaultComboBoxModel(new String[] { "Cash", "Card" }));
		payByCombo.setBounds(84, 480, 114, 20);
		frmGeneratingBill.getContentPane().add(payByCombo);


		try {
			db = new DBConnection();
			ResultSet rs = db.getBillheader();
			while (rs.next()) {
				restauname.setText(rs.getString(1));
				restauadd.setText(rs.getString(2));
				restauName = rs.getString(1).trim();
				restauAdd = rs.getString(2).trim();
			}
		} catch (Exception e) {
			utility.showAleart("Error, while fetching bill header due to : " + e.getMessage());
			log.error("Exception",e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e1) {
				log.error("Exception",e1);
			}
		}
		
		try {
			db = new DBConnection();
			ResultSet rs = db.getBillFooter();
			while (rs.next()) {
				line1 = rs.getString(1).trim();
				line2 = rs.getString(2).trim();
				if(line1 == null){
					line1 = "Thank You! Please Visit Again.!";
				}
				if(line2 == null){
					line2 = "";
				}
			}
		} catch (Exception e) {
			utility.showAleart("Error, while fetching bill footer due to : " + e.getMessage());
			log.error("Exception",e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e1) {
				log.error("Exception",e1);
			}
		}

		String bill = "";
		String pattern = "dd-MMM-yyyy HH:mm:ss";
		String dateInString = "";
		if (reprint) {
			String result = ""; // 2017-07-18 15:29:41
			try {
				db = new DBConnection();
				ResultSet rs = db.getOrderTimeStamp(orderNo, orderDate);
				while (rs.next()) {
					result = rs.getString(1);
				}
				
				DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
				Date date = inputFormat.parse(result);

				// Format date into output format
				DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
				String outputString = outputFormat.format(date);
				String arr1[] = result.split(" ");
				String arr2[] = arr1[0].split("-");
				billNum.setText(String.valueOf(orderNo) + "/" + arr2[1] + arr2[2]);
				dateTime.setText(outputString);
				
				int flag = 0;
				rs = null;
				rs = db.getOrderPaymentModeByNo(orderNo);
				while (rs.next()) {
					result = rs.getString(1);
					flag = 1;
				}
				if(flag == 1)
				payByCombo.setSelectedItem(result);				
				
			} catch (Exception e) {
				utility.showAleart("Error, while getting a Order TimeStamp,\nDue to : " + e.getMessage());
				log.error("Exception",e);
			} finally {
				try {
					db.closeConnection();
				} catch (SQLException e1) {
					log.error("Exception",e1);
				}
			}
		} else {
			bill = new SimpleDateFormat("MMdd").format(new Date());
			billNum.setText(String.valueOf(orderNo) + "/" + bill);
			dateInString = new SimpleDateFormat(pattern).format(new Date());
			dateTime.setText(dateInString);
		}

		JButton printBtn = new JButton("");
		if (reprint) {
			printBtn.setText("Reprint");
		} else {
			printBtn.setText("Print");
		}
		printBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int no = utility.showConfirmation("Are you sure you wish to generate bill?", "Bill");
				if (no == 0) {
					if (!reprint) {
						try {
							db = new DBConnection();
							int result = db.updateOrderByNo(Float.parseFloat(discount), 2, orderNo, finalAmt,
									payByCombo.getSelectedItem().toString());
							if (result > 0) {
								// print the Bill 2 copies
								printFinal(orderNo, discount, amtAfterDisc);
								frmGeneratingBill.dispose();
								new ManagerHome().clickrefreshAllBtn();
							}

						} catch (Exception e1) {
							utility.showAleart("Error, while printing bill due to : " + e1.getMessage());
							log.error("Exception",e1);
						} finally {
							try {
								db.closeConnection();
							} catch (SQLException e1) {
								log.error("Exception",e1);
							}
						}

					} else {
						printFinal(orderNo, discount, amtAfterDisc);
						frmGeneratingBill.dispose();
					}
				}
			}

		});
		printBtn.setBounds(338, 477, 89, 23);
		frmGeneratingBill.getContentPane().add(printBtn);

		billContentPane = new JScrollPane();
		billContentPane.setBounds(10, 115, 424, 187);
		frmGeneratingBill.getContentPane().add(billContentPane);
		billContentPane.setAutoscrolls(false);
		billContentPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		try {
			db = new DBConnection();
			ResultSet res = db.getOrderByNo(orderNo, orderDate);
			model = populateBillContent(res);
		} catch (Exception e) {
			utility.showAleart("Error, while fetching order details by no & date due to : " + e.getMessage());
			log.error("Exception",e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e1) {
				log.error("Exception",e1);
			}
		}
		if (model != null) {
			table = new JTable(model);
			table.getTableHeader().setReorderingAllowed(false);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setFillsViewportHeight(true);
			table.setShowGrid(true);
			// table.setBackground(new Color(240, 240, 240));
			billContentPane.setViewportView(table);
		}

		subTotallbl = new JLabel("Sub Total :");
		subTotallbl.setBounds(260, yaxis, 89, 14);
		frmGeneratingBill.getContentPane().add(subTotallbl);

		subTotalvalue = new JLabel(String.valueOf(String.format("%.02f", subTotal)));
		subTotalvalue.setBounds(354, yaxis, 89, 14);
		frmGeneratingBill.getContentPane().add(subTotalvalue);
		yaxis = yaxis + 15;

		amtAfterDisc = subTotal;
		// -- Discount -->
		float disValue = 0.0f;
		if (discount != "0.0") {
			JLabel label3 = new JLabel("=====================");
			label3.setBounds(260, yaxis, 234, 14);
			frmGeneratingBill.getContentPane().add(label3);
			yaxis = yaxis + 15;

			dis = new JLabel("Dis. @" + discount + "% :");
			dis.setBounds(260, yaxis, 82, 14);
			frmGeneratingBill.getContentPane().add(dis);

			discAmt = Float.parseFloat(discount);
			disValue = (subTotal * discAmt) / 100;
			disValuelbl = new JLabel(String.valueOf(String.format("%.02f", disValue)));
			disValuelbl.setBounds(354, yaxis, 80, 14);
			frmGeneratingBill.getContentPane().add(disValuelbl);
			yaxis = yaxis + 15;

			JLabel amtAfterTaxlbl = new JLabel("Total:");
			amtAfterTaxlbl.setBounds(260, yaxis, 82, 14);
			frmGeneratingBill.getContentPane().add(amtAfterTaxlbl);

			amtAfterDisc = subTotal - disValue;

			JLabel amtAfterTaxValue = new JLabel(String.valueOf(String.format("%.02f", amtAfterDisc)));
			amtAfterTaxValue.setBounds(354, yaxis, 80, 14);
			frmGeneratingBill.getContentPane().add(amtAfterTaxValue);
			yaxis = yaxis + 15;
		}
		// <-- Discount

		// -- TAX -- >
		taxes = getTaxes();
		if (!taxes.isEmpty()) {

			Set mapSet = (Set) taxes.entrySet();
			Iterator itr = mapSet.iterator();
			while (itr.hasNext()) {
				Map.Entry mapEntry = (Map.Entry) itr.next();
				String key = (String) mapEntry.getKey();
				float value = (Float) mapEntry.getValue();

				float taxAmt = (amtAfterDisc * value) / 100;

				JLabel taxName = new JLabel(key + " : ");
				taxName.setBounds(260, yaxis, 82, 14);
				frmGeneratingBill.getContentPane().add(taxName);

				JLabel taxValue = new JLabel(String.valueOf(String.format("%.02f", taxAmt)));
				// new JLabel(String.valueOf(taxAmt));
				taxValue.setBounds(354, yaxis, 80, 14);
				frmGeneratingBill.getContentPane().add(taxValue);
				yaxis = yaxis + 15;
				totalTaxApplied = totalTaxApplied + taxAmt;
				taxList.add(taxName.getText() + "=" + taxValue.getText());
			}
		}
		// < -- TAX --

		amtAfterTax = amtAfterDisc + totalTaxApplied;

		// Rounding Off -->

		finalAmt = getRoundOff(amtAfterTax);
		JLabel label2 = new JLabel("=====================");
		label2.setBounds(260, yaxis, 234, 14);
		frmGeneratingBill.getContentPane().add(label2);
		yaxis = yaxis + 15;

		JLabel total = new JLabel("Round off Total :");
		total.setFont(new Font("Tahoma", Font.BOLD, 13));
		total.setBounds(210, yaxis, 127, 14);
		frmGeneratingBill.getContentPane().add(total);
		totalValue = new JLabel(String.valueOf(String.format("%.02f", finalAmt)));
		totalValue.setFont(new Font("Tahoma", Font.BOLD, 13));
		totalValue.setBounds(347, yaxis, 80, 14);
		frmGeneratingBill.getContentPane().add(totalValue);
		// <--Rounding Off

		
		JLabel lblNewLabel_8 = new JLabel("Pay By :");
		lblNewLabel_8.setBounds(10, 482, 53, 14);
		frmGeneratingBill.getContentPane().add(lblNewLabel_8);

		if (reprint) {
			updatePayByBtn = new JButton("Update");
			updatePayByBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						db = new DBConnection();
						int result = db.updateOrderPaymentModeByNo(orderNo, payByCombo.getSelectedItem().toString());
						if (result > 0) {
							utility.showAleart("Payment mode for Order No : " + orderNo + " updated successfully.");
						} else {
							utility.showAleart("Unable to update the payment mode.");
						}
					} catch (Exception e1) {
						utility.showAleart("Error, while re-printing bill due to : " + e1.getMessage());
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
			updatePayByBtn.setBounds(210, 477, 89, 23);
			frmGeneratingBill.getContentPane().add(updatePayByBtn);
		}

		frmGeneratingBill.setVisible(true);
		log.info("Completed");
	}

	private Map<String, Float> getTaxes() {
		log.info("Started");
		Map<String, Float> taxes = new HashMap<String, Float>();
		try {
			db = new DBConnection();
			ResultSet rs = db.getAllTaxes();
			while (rs.next()) {
				taxes.put(rs.getString(1), rs.getFloat(2));
			}
		} catch (Exception e) {
			utility.showAleart("Error, while fetching tax details due to : " + e.getMessage());
			log.error("Exception",e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception",e);
			}
		}
		log.info("Completed");
		return taxes;
	}

	private float getRoundOff(float value) {
		log.info("Started");
		float x = value - (int) value;
		if (x < 0.50) {
			value = (int) value;
		} else {
			value = (int) value + 1;
		}
		log.info("Completed");
		return value;
	}

	private DefaultTableModel populateBillContent(ResultSet rs) throws Exception {
		log.info("Started");
		DefaultTableModel model = null;
		String[] cols = { "Item", "Quantity", "Price", "Amount" };
		int totalRows = 0;
		while (rs.next()) {
			totalRows++;
		}
		rs.beforeFirst();
		data = new String[totalRows][4];
		int i = 0;
		while (rs.next()) {
			for (int j = 1; j <= 4; j++) {
				if (j == 1)
					data[i][j - 1] = rs.getString(j);

				if (j == 2) {
					data[i][j - 1] = String.valueOf(rs.getInt(j));
				}

				if (j == 3) {
					data[i][j - 1] = String.valueOf(rs.getFloat(j));
				}

				if (j == 4) {
					data[i][j - 1] = String.valueOf(rs.getFloat(j));
					subTotal = subTotal + Float.parseFloat(data[i][j - 1]);
				}
			}
			i++;
		}
		model = new DefaultTableModel(data, cols) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		log.info("Completed");
		return model;
	}

	private void printFinal(int orderNo, String discount, float amtAfterDis) {
		log.info("Started");
		ArrayList<ItemData> itemDataList = new ArrayList<ItemData>();
		PrintableObject po = new PrintableObject();
		int rows = data.length;
		for (int i = 0; i < rows; i++) {
			ItemData itemData = new ItemData();
			itemData.setItem(data[i][0]);
			itemData.setQty(Integer.parseInt(data[i][1]));
			itemData.setPrice(Float.parseFloat(data[i][2]));
			itemData.setAmt(Float.parseFloat(data[i][3]));
			itemDataList.add(itemData);
		}
		po.setBillNo(billNum.getText());
		po.setBillDateTime(dateTime.getText());
		po.setItem(itemDataList);
		po.setSubTotal(subTotalvalue.getText());
		po.setTaxes(taxList);
		po.setTotal(String.valueOf(amtAfterDisc));
		if (discount != "0.0") {
			po.setDiscount(dis.getText() + "=" + disValuelbl.getText());
		}
		po.setRoundoffTotal(totalValue.getText());
		new FinalReceipt(po, orderNo);
		log.info("Completed");
	}
}
