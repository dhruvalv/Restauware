package com.dhruval.restauware.forms;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.dhruval.restauware.dao.Customer;
import com.dhruval.restauware.dao.Menu;
import com.dhruval.restauware.dao.Staff;
import com.dhruval.restauware.dao.User;
import com.dhruval.restauware.db.DBConnection;
import com.dhruval.restauware.reports.AttendanceReport;
import com.dhruval.restauware.reports.ExpenseReport;
import com.dhruval.restauware.reports.InventoryReport;
import com.dhruval.restauware.reports.OpeningBalReport;
import com.dhruval.restauware.reports.OrdersReport;
import com.dhruval.restauware.reports.TransactionReport;
import com.dhruval.restauware.utilities.RestauwareUtility;

public class AdminHome {

	private JFrame adminHome;
	private JTextField fnameTF, mnameTF, lnameTF, mobileTF, teleTF, idTF, refdByTF, custFnameTF, custLnameTF,
			custEmailTF, custContactTF, menuItemTF, itemPriceTF, itemCodeTF;
	private JTable table;
	private JRadioButton maleRadio, femaleRadio;
	private JScrollPane scrollPane_1, scrollPane_2;
	private JComboBox menuItemCombo, dobDD, dobMM, dobYY, categoryCombo, proofCombo, custDDCombo, custMMCombo,
			custYYCombo, dojDD, dojMM, dojYY, categoryTab, orderStatCombo, orderDateCombo;
	private JTextArea addressTA, custAddTA;
	private Staff staff;
	private Menu menu;
	private User user;
	private String admin;
	private RestauwareUtility utility;
	private JTextField inventItemTF;
	private JTextField inventQtyTF;
	private JTextField discTF;
	private JTextField txDescTF;
	private JTextField txShrtNmTF;
	private JTextField txPercTF;
	private DBConnection db;
	private JComboBox repCombo;
	private JComboBox repFrDD;
	private JComboBox repFrMM;
	private JComboBox repFrYY;
	private JComboBox repToDD;
	private JComboBox repToMM;
	private JComboBox repToYY;
	private JRadioButton repTodayRadio;
	private JRadioButton repDateRngRadio;
	private BufferedWriter out;
	private JLabel toDateLbl;
	private JLabel fromDateLbl;
	private JPanel searchByPanel;
	private JComboBox orderNoCombo;
	private JScrollPane viewRefCustScrollPan;
	private JScrollPane itemScrollPan;
	private JScrollPane discScrollPan;
	private JScrollPane taxScrollPan;
	private JScrollPane storeCloseScrollPan;
	private static JButton viewRecEODRepBtn;
	private JScrollPane viewRecEODRepScroll;
	private static JButton viewDiscBtn;
	private static JButton viewTaxBtn;
	private static JButton viewMenuBtn;
	private static JButton viewStaffBtn;
	private static JButton viewRefCustBtn;
	private static JButton viewItemBtn;
	private static Logger log = Logger.getLogger(AdminHome.class.getName());
	private JTextField emailAddTF;
	private JTextField storeName;
	private JTextField storeAdd;
	private JButton addBillHeaders;
	private JScrollPane viewBillHeaderScroll;
	private JButton fetchDatesBtn;
	private JButton fetchOrdersBtn;
	private static JButton viewBillHeadersBtn;
	private JTextField line1TF;
	private JTextField line2TF;
	private JButton addBillFooter;
	private static JButton viewBillFootersBtn;
	private JScrollPane viewBillFooterScroll;
	private static JButton viewSenderEODRepBtn;
	private JScrollPane viewSenderEODRepScroll;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminHome window = new AdminHome();
					window.adminHome.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AdminHome() {

	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public AdminHome(User user) {
		this.user = user;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		log.info("Started");
		staff = new Staff();
		utility = new RestauwareUtility();
		utility.createDirectory();
		adminHome = new JFrame();
		adminHome.setSize(930, 630);
		adminHome.setResizable(false);
		adminHome.setTitle("Admin's Home");
		adminHome.getContentPane().setFont(new Font("SansSerif", Font.PLAIN, 11));
		adminHome.getContentPane().setBackground(SystemColor.activeCaption);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - adminHome.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - adminHome.getHeight()) / 2);
		adminHome.setLocation(x, y);
		adminHome.getContentPane().setLayout(null);

		JPanel headerPanel = new JPanel();
		headerPanel.setBounds(10, 11, 904, 63);
		headerPanel.setBorder(new LineBorder(SystemColor.textInactiveText, 2, true));
		adminHome.getContentPane().add(headerPanel);
		headerPanel.setLayout(null);

		try {
			db = new DBConnection();
			admin = db.getAdminName(user);
		} catch (Exception e2) {
			utility.showAleart("Error, while fetching an Admin due to : " + e2.getMessage());
			log.error("Exception", e2);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e1) {
				log.error("Exception", e1);
			}
		}

		JLabel adminName = new JLabel("Welcome " + admin + ",");
		adminName.setFont(new Font("Tahoma", Font.BOLD, 15));
		adminName.setBounds(10, 11, 394, 14);
		headerPanel.add(adminName);

		String lastlogintime = "Welcome";

		try {
			db = new DBConnection();
			lastlogintime = db.getLastLogin(user);
		} catch (Exception e1) {
			utility.showAleart("Error, while fetching last login due to : " + e1.getMessage());
			log.error("Exception", e1);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e1) {
				log.error("Exception", e1);
			}
		}
		JLabel lastLogin = new JLabel("Last Login : " + lastlogintime);
		lastLogin.setBounds(550, 11, 280, 14);
		headerPanel.add(lastLogin);

		JLabel logoutLbl = new JLabel("Logout");
		logoutLbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		logoutLbl.setBounds(797, 38, 127, 14);
		headerPanel.add(logoutLbl);

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
					log.info(new Date().toString() + " > AdminHome logging off.");
					adminHome.dispose();
					Welcome w = new Welcome(0);
				}

			}
		});

		user.setLastLogin(new java.util.Date());
		updateLastLogin(user);

		JTabbedPane bodyTab = new JTabbedPane(JTabbedPane.TOP);
		bodyTab.setBounds(10, 85, 904, 515);
		bodyTab.setBorder(new LineBorder(SystemColor.textInactiveText, 2, true));
		adminHome.getContentPane().add(bodyTab);

		JTabbedPane mgmtTab = new JTabbedPane(JTabbedPane.TOP);
		bodyTab.addTab("Management", null, mgmtTab, null);

		JTabbedPane staffTab = new JTabbedPane(JTabbedPane.TOP);
		mgmtTab.addTab("Staff", null, staffTab, null);

		ButtonGroup bg = new ButtonGroup();

		String pattern = "dd-MMM-yyyy";
		String dateInString = new SimpleDateFormat(pattern).format(new Date());

		JPanel newStaffPane = new JPanel();
		staffTab.addTab("Add New", null, newStaffPane, null);
		newStaffPane.setLayout(null);

		JLabel lblNewLabel_2 = new JLabel("First Name :");
		lblNewLabel_2.setBounds(20, 63, 75, 14);
		newStaffPane.add(lblNewLabel_2);

		fnameTF = new JTextField();
		fnameTF.setBounds(101, 60, 170, 20);
		newStaffPane.add(fnameTF);
		fnameTF.setColumns(10);

		JLabel lblMiddleName = new JLabel("Middle Name :");
		lblMiddleName.setBounds(308, 63, 87, 14);
		newStaffPane.add(lblMiddleName);

		mnameTF = new JTextField();
		mnameTF.setBounds(393, 60, 170, 20);
		newStaffPane.add(mnameTF);
		mnameTF.setColumns(10);

		JLabel lblLastName = new JLabel("Last Name :");
		lblLastName.setBounds(20, 94, 75, 14);
		newStaffPane.add(lblLastName);

		lnameTF = new JTextField();
		lnameTF.setBounds(101, 91, 170, 20);
		newStaffPane.add(lnameTF);
		lnameTF.setColumns(10);

		JLabel lblDateOfBirth = new JLabel("Date of Birth :");
		lblDateOfBirth.setBounds(308, 122, 87, 14);
		newStaffPane.add(lblDateOfBirth);

		dobDD = new JComboBox();
		dobDD.setModel(new DefaultComboBoxModel(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09",
				"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26",
				"27", "28", "29", "30", "31" }));
		dobDD.setBounds(393, 119, 39, 20);
		newStaffPane.add(dobDD);

		dobMM = new JComboBox();
		dobMM.setModel(new DefaultComboBoxModel(
				new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" }));
		dobMM.setBounds(442, 119, 49, 20);
		newStaffPane.add(dobMM);

		dobYY = new JComboBox();
		dobYY.setModel(new DefaultComboBoxModel(new String[] { "1970", "1971", "1972", "1973", "1974", "1975", "1976",
				"1977", "1978", "1979", "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1989",
				"1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002",
				"2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015",
				"2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028",
				"2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036", "2037", "2038", "2039", "2040", "2041",
				"2042", "2043", "2044", "2045", "2046", "2047", "2048", "2049", "2050" }));
		dobYY.setBounds(501, 119, 62, 20);
		newStaffPane.add(dobYY);

		JLabel lblAddress = new JLabel("Address :");
		lblAddress.setBounds(20, 119, 75, 14);
		newStaffPane.add(lblAddress);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(101, 122, 170, 66);
		newStaffPane.add(scrollPane);

		addressTA = new JTextArea();
		scrollPane.setViewportView(addressTA);

		JLabel lblNewLabel_3 = new JLabel("Referred by :");
		lblNewLabel_3.setBounds(308, 35, 87, 14);
		newStaffPane.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("Mobile No :");
		lblNewLabel_4.setBounds(308, 156, 75, 14);
		newStaffPane.add(lblNewLabel_4);

		mobileTF = new JTextField();
		mobileTF.setBounds(393, 153, 170, 20);
		newStaffPane.add(mobileTF);
		mobileTF.setColumns(10);

		JLabel lblNewLabel_5 = new JLabel("Telephone No :");
		lblNewLabel_5.setBounds(308, 187, 87, 14);
		newStaffPane.add(lblNewLabel_5);

		teleTF = new JTextField();
		teleTF.setBounds(393, 184, 170, 20);
		newStaffPane.add(teleTF);
		teleTF.setColumns(10);

		JLabel lblIdProof = new JLabel("ID Proof :");
		lblIdProof.setBounds(20, 202, 75, 14);
		newStaffPane.add(lblIdProof);

		proofCombo = new JComboBox();
		proofCombo.setModel(new DefaultComboBoxModel(
				new String[] { "-- Select --", "PAN", "Adhar", "Driving License", "Voter Card", "Passport", "Other" }));
		proofCombo.setBounds(101, 199, 170, 20);
		newStaffPane.add(proofCombo);

		JLabel lblNewLabel_6 = new JLabel("ID No :");
		lblNewLabel_6.setBounds(20, 233, 75, 14);
		newStaffPane.add(lblNewLabel_6);

		idTF = new JTextField();
		idTF.setBounds(101, 230, 170, 20);
		newStaffPane.add(idTF);
		idTF.setColumns(10);

		refdByTF = new JTextField();
		refdByTF.setBounds(393, 32, 170, 20);
		newStaffPane.add(refdByTF);
		refdByTF.setColumns(10);

		JLabel lblNewLabel_7 = new JLabel("Category :");
		lblNewLabel_7.setBounds(20, 32, 75, 14);
		newStaffPane.add(lblNewLabel_7);

		categoryCombo = new JComboBox();
		categoryCombo.setModel(
				new DefaultComboBoxModel(new String[] { "-- Select --", "Chef", "Cleaner", "Manager", "Waiter" }));
		categoryCombo.setBounds(101, 29, 170, 20);
		newStaffPane.add(categoryCombo);

		JButton btnNewButton = new JButton("Save");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (newStaffvalidated(categoryCombo, refdByTF, fnameTF, mnameTF, lnameTF, addressTA, mobileTF, teleTF,
						idTF, proofCombo) && numberValidated(mobileTF, teleTF)) {
					staff.setCategory(categoryCombo.getSelectedItem().toString());
					staff.setRefdby(refdByTF.getText().trim());
					staff.setFname(fnameTF.getText().trim());
					staff.setMname(mnameTF.getText().trim());
					staff.setLname(lnameTF.getText().trim());
					if (maleRadio.isSelected()) {
						staff.setGender("Male");
					} else {
						staff.setGender("Female");
					}
					staff.setAddress(addressTA.getText().trim());
					String dob = dobDD.getSelectedItem().toString() + "-" + dobMM.getSelectedItem().toString() + "-"
							+ dobYY.getSelectedItem().toString();
					staff.setDob(dob);
					staff.setMobile(Long.parseLong(mobileTF.getText().trim()));
					staff.setTelephone(
							Long.parseLong(teleTF.getText().trim().equals("") ? "0" : teleTF.getText().trim()));
					staff.setIdproof(proofCombo.getSelectedItem().toString());
					staff.setIdno(idTF.getText().trim());
					String doj = dojDD.getSelectedItem().toString() + "-" + dojMM.getSelectedItem().toString() + "-"
							+ dojYY.getSelectedItem().toString();

					staff.setDoj(doj);
					staff.setDol(null);
					try {
						db = new DBConnection();
						int result = db.addNewStaff(staff);
						if (result > 0) {
							clearAddNewStaffForm();
							utility.showAleart("Added Successfully..!");
							categoryCombo.requestFocusInWindow();
						}
					} catch (Exception e) {
						utility.showAleart("Error, while adding new staff due to : " + e.getMessage());
						log.error("Exception", e);
					} finally {
						try {
							db.closeConnection();
						} catch (SQLException e) {
							log.error("Exception", e);
						}
					}
				}
			}
		});

		btnNewButton.setBounds(101, 278, 89, 23);
		newStaffPane.add(btnNewButton);

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAddNewStaffForm();
			}
		});
		btnClear.setBounds(200, 278, 89, 23);
		newStaffPane.add(btnClear);

		JLabel lblNewLabel_9 = new JLabel("Gender :");
		lblNewLabel_9.setBounds(308, 94, 62, 14);
		newStaffPane.add(lblNewLabel_9);

		maleRadio = new JRadioButton("Male");
		maleRadio.setSelected(true);
		maleRadio.setBounds(393, 87, 67, 23);
		newStaffPane.add(maleRadio);

		femaleRadio = new JRadioButton("Female");
		femaleRadio.setBounds(462, 87, 109, 23);
		newStaffPane.add(femaleRadio);
		bg.add(maleRadio);
		bg.add(femaleRadio);

		JLabel lblDateOfJoining = new JLabel("Date Of Joining :");
		lblDateOfJoining.setBounds(308, 212, 96, 20);
		newStaffPane.add(lblDateOfJoining);

		dojDD = new JComboBox();
		dojDD.setModel(new DefaultComboBoxModel(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09",
				"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26",
				"27", "28", "29", "30", "31" }));
		dojDD.setBounds(393, 212, 39, 20);
		newStaffPane.add(dojDD);

		dojMM = new JComboBox();
		dojMM.setModel(new DefaultComboBoxModel(
				new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" }));
		dojMM.setBounds(442, 212, 49, 20);
		newStaffPane.add(dojMM);

		dojYY = new JComboBox();
		dojYY.setModel(new DefaultComboBoxModel(new String[] { "1970", "1971", "1972", "1973", "1974", "1975", "1976",
				"1977", "1978", "1979", "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1989",
				"1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002",
				"2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015",
				"2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028",
				"2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036", "2037", "2038", "2039", "2040", "2041",
				"2042", "2043", "2044", "2045", "2046", "2047", "2048", "2049", "2050" }));
		dojYY.setBounds(501, 212, 62, 20);
		newStaffPane.add(dojYY);

		JPanel modiStaffPane = new JPanel();
		staffTab.addTab("Modify", null, modiStaffPane, null);
		modiStaffPane.setLayout(null);

		categoryTab = new JComboBox();
		categoryTab.setModel(new DefaultComboBoxModel(new String[] { "Chef", "Cleaner", "Manager", "Waiter" }));
		categoryTab.setBounds(78, 11, 146, 20);
		modiStaffPane.add(categoryTab);

		JLabel lblNewLabel_8 = new JLabel("Category :");
		lblNewLabel_8.setBounds(10, 14, 72, 14);
		modiStaffPane.add(lblNewLabel_8);

		viewStaffBtn = new JButton("View");
		viewStaffBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				viewStaff();
			}
		});
		viewStaffBtn.setBounds(234, 10, 111, 23);
		modiStaffPane.add(viewStaffBtn);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setBounds(10, 42, 865, 374);
		modiStaffPane.add(scrollPane_1);

		JTabbedPane custTab = new JTabbedPane(JTabbedPane.TOP);
		mgmtTab.addTab("Customer", null, custTab, null);

		JPanel newCustTab = new JPanel();
		custTab.addTab("Add New", null, newCustTab, null);
		newCustTab.setLayout(null);

		JLabel lblNewLabel_13 = new JLabel("First Name :");
		lblNewLabel_13.setBounds(25, 25, 73, 14);
		newCustTab.add(lblNewLabel_13);

		custFnameTF = new JTextField();
		custFnameTF.setBounds(99, 22, 161, 20);
		newCustTab.add(custFnameTF);
		custFnameTF.setColumns(10);

		JLabel lblNewLabel_14 = new JLabel("Last Name :");
		lblNewLabel_14.setBounds(25, 50, 73, 14);
		newCustTab.add(lblNewLabel_14);

		custLnameTF = new JTextField();
		custLnameTF.setBounds(99, 50, 161, 20);
		newCustTab.add(custLnameTF);
		custLnameTF.setColumns(10);

		JLabel lblNewLabel_15 = new JLabel("Address :");
		lblNewLabel_15.setBounds(25, 115, 74, 14);
		newCustTab.add(lblNewLabel_15);

		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(99, 109, 161, 61);
		newCustTab.add(scrollPane_3);

		custAddTA = new JTextArea();
		scrollPane_3.setViewportView(custAddTA);

		JLabel lblNewLabel_16 = new JLabel("Birth Date :");
		lblNewLabel_16.setBounds(25, 184, 73, 14);
		newCustTab.add(lblNewLabel_16);

		custDDCombo = new JComboBox();
		custDDCombo.setModel(new DefaultComboBoxModel(new String[] { "01", "02", "03", "04", "05", "06", "07", "08",
				"09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25",
				"26", "27", "28", "29", "30", "31" }));
		custDDCombo.setBounds(98, 181, 41, 20);
		newCustTab.add(custDDCombo);

		custMMCombo = new JComboBox();
		custMMCombo.setModel(new DefaultComboBoxModel(
				new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" }));
		custMMCombo.setBounds(149, 181, 47, 20);
		newCustTab.add(custMMCombo);

		custYYCombo = new JComboBox();
		custYYCombo.setModel(new DefaultComboBoxModel(new String[] { "1970", "1971", "1972", "1973", "1974", "1975",
				"1976", "1977", "1978", "1979", "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988",
				"1989", "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001",
				"2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014",
				"2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027",
				"2028", "2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036", "2037", "2038", "2039", "2040",
				"2041", "2042", "2043", "2044", "2045", "2046", "2047", "2048", "2049", "2050" }));
		custYYCombo.setBounds(206, 181, 54, 20);
		newCustTab.add(custYYCombo);

		JLabel lblNewLabel_17 = new JLabel("Email :");
		lblNewLabel_17.setBounds(25, 222, 73, 14);
		newCustTab.add(lblNewLabel_17);

		custEmailTF = new JTextField();
		custEmailTF.setBounds(99, 219, 161, 20);
		newCustTab.add(custEmailTF);
		custEmailTF.setColumns(10);

		JLabel lblNewLabel_18 = new JLabel("Contact No :");
		lblNewLabel_18.setBounds(25, 250, 73, 14);
		newCustTab.add(lblNewLabel_18);

		custContactTF = new JTextField();
		custContactTF.setBounds(99, 247, 161, 20);
		newCustTab.add(custContactTF);
		custContactTF.setColumns(10);

		JLabel lblNewLabel_19 = new JLabel("Gender :");
		lblNewLabel_19.setBounds(25, 82, 46, 14);
		newCustTab.add(lblNewLabel_19);

		JRadioButton custMaleRdio = new JRadioButton("Male");
		custMaleRdio.setSelected(true);
		custMaleRdio.setBounds(99, 79, 65, 23);
		newCustTab.add(custMaleRdio);

		JRadioButton custFemaleRdio = new JRadioButton("Female");
		custFemaleRdio.setBounds(166, 79, 109, 23);
		newCustTab.add(custFemaleRdio);
		ButtonGroup bGp = new ButtonGroup();
		bGp.add(custMaleRdio);
		bGp.add(custFemaleRdio);

		JButton custSaveBtn = new JButton("Save");
		custSaveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (validateCustFields(custFnameTF.getText().trim(), custContactTF.getText().trim())) {
					Customer customer = new Customer();
					customer.setFname(custFnameTF.getText().trim());
					customer.setLname(custLnameTF.getText().trim());
					customer.setGender(custMaleRdio.isSelected() ? "Male" : "Female");
					customer.setAddress(custAddTA.getText().trim());
					customer.setDob(
							custDDCombo.getSelectedItem().toString() + "-" + custMMCombo.getSelectedItem().toString()
									+ "-" + custYYCombo.getSelectedItem().toString());
					customer.setEmail(custEmailTF.getText().trim());
					customer.setMobile(Long.parseLong(custContactTF.getText().trim()));

					try {
						db = new DBConnection();
						int result = db.addNewCustomer(customer);
						if (result > 0) {
							utility.showAleart("Added Successfully..!");
							clearCustomerForm();
							custFnameTF.requestFocusInWindow();
						}
					} catch (Exception e) {
						utility.showAleart("Error, while adding new customer due to : " + e.getMessage());
						log.error("Exception", e);
					} finally {
						try {
							db.closeConnection();
						} catch (SQLException e) {
							log.error("Exception", e);
						}
					}
				}
			}
		});
		custSaveBtn.setBounds(65, 288, 89, 23);
		newCustTab.add(custSaveBtn);

		JButton custClearBtn = new JButton("Clear");
		custClearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearCustomerForm();
			}
		});
		custClearBtn.setBounds(171, 288, 89, 23);
		newCustTab.add(custClearBtn);

		JPanel modifyCustTab = new JPanel();
		custTab.addTab("Modify", null, modifyCustTab, null);
		modifyCustTab.setLayout(null);

		viewRefCustScrollPan = new JScrollPane();
		viewRefCustScrollPan.setBounds(10, 45, 865, 371);
		viewRefCustScrollPan.setAutoscrolls(true);
		modifyCustTab.add(viewRefCustScrollPan);

		viewRefCustBtn = new JButton("View Customers");
		viewRefCustBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					db = new DBConnection();
					ResultSet rs = db.getAllCustomer();
					viewCustomerTable(rs);
				} catch (Exception e) {
					utility.showAleart("Error, while fetching customers due to : " + e.getMessage());
					log.error("Exception", e);
				} finally {
					try {
						db.closeConnection();
					} catch (SQLException e) {
						log.error("Exception", e);
					}
				}
			}
		});
		viewRefCustBtn.setBounds(10, 11, 149, 23);
		modifyCustTab.add(viewRefCustBtn);

		JTabbedPane inventTab = new JTabbedPane(JTabbedPane.TOP);
		mgmtTab.addTab("Inventory", null, inventTab, null);

		JPanel addInventPane = new JPanel();
		inventTab.addTab("New Item", null, addInventPane, null);
		addInventPane.setLayout(null);

		JLabel lblNewLabel_20 = new JLabel("Item :");
		lblNewLabel_20.setBounds(25, 29, 46, 14);
		addInventPane.add(lblNewLabel_20);

		inventItemTF = new JTextField();
		inventItemTF.setBounds(87, 26, 182, 20);
		addInventPane.add(inventItemTF);
		inventItemTF.setColumns(10);

		JButton inventAddBtn = new JButton("Add");
		inventAddBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (validateQtyFields(inventItemTF.getText().trim(), inventQtyTF.getText().trim())) {
					String item = inventItemTF.getText().trim();
					int qty = Integer.parseInt(inventQtyTF.getText().trim());
					try {
						db = new DBConnection();
						int result = db.addNewInventory(item, qty);
						if (result > 0) {
							utility.showAleart("Item Added Successfully..!");
							inventItemTF.setText("");
							inventQtyTF.setText("");
							inventItemTF.requestFocusInWindow();
						}
					} catch (Exception e1) {
						utility.showAleart(
								"Error, while Adding new Item into the inventory due to : " + e1.getMessage());
						log.error("Exception", e1);
					} finally {
						try {
							db.closeConnection();
						} catch (SQLException e1) {
							log.error("Exception", e1);
						}
					}
				}
			}
		});
		inventAddBtn.setBounds(87, 82, 86, 23);
		addInventPane.add(inventAddBtn);

		JButton inventClearBtn = new JButton("Clear");
		inventClearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inventItemTF.setText("");
			}
		});
		inventClearBtn.setBounds(185, 82, 84, 23);
		addInventPane.add(inventClearBtn);

		JLabel lblNewLabel_21 = new JLabel("Quantity :");
		lblNewLabel_21.setBounds(25, 54, 59, 14);
		addInventPane.add(lblNewLabel_21);

		inventQtyTF = new JTextField();
		inventQtyTF.setBounds(87, 51, 86, 20);
		addInventPane.add(inventQtyTF);
		inventQtyTF.setColumns(10);

		JPanel modifyInventPane = new JPanel();
		inventTab.addTab("Modify Item", null, modifyInventPane, null);
		modifyInventPane.setLayout(null);

		viewItemBtn = new JButton("View Items");
		viewItemBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					db = new DBConnection();
					ResultSet rs = db.getMainInventory();
					viewInventoryItemTable(rs);
				} catch (Exception e1) {
					utility.showAleart("Error, while fetching inventory items due to : " + e1.getMessage());
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
		viewItemBtn.setBounds(10, 11, 123, 23);
		modifyInventPane.add(viewItemBtn);

		itemScrollPan = new JScrollPane();
		itemScrollPan.setBounds(10, 45, 388, 371);
		modifyInventPane.add(itemScrollPan);
		Font f = new Font("Arial", Font.BOLD, 15);

		JTabbedPane menuTab = new JTabbedPane(JTabbedPane.TOP);
		mgmtTab.addTab("Menu", null, menuTab, null);

		JPanel addMenuPane = new JPanel();
		menuTab.addTab("Add New", null, addMenuPane, null);
		addMenuPane.setLayout(null);

		JLabel lblMenuItem = new JLabel("Item Name :");
		lblMenuItem.setBounds(36, 98, 97, 14);
		addMenuPane.add(lblMenuItem);

		menuItemTF = new JTextField();
		menuItemTF.setBounds(134, 95, 303, 20);
		addMenuPane.add(menuItemTF);
		menuItemTF.setColumns(10);

		JLabel lblNewLabel_10 = new JLabel("Price Per Item :");
		lblNewLabel_10.setBounds(36, 134, 97, 14);
		addMenuPane.add(lblNewLabel_10);

		itemPriceTF = new JTextField();
		itemPriceTF.setBounds(134, 131, 106, 20);
		addMenuPane.add(itemPriceTF);
		itemPriceTF.setColumns(10);

		JLabel lblNewLabel_11 = new JLabel("(e.g. 120.00)");
		lblNewLabel_11.setBounds(250, 134, 84, 14);
		addMenuPane.add(lblNewLabel_11);

		menuItemCombo = new JComboBox();
		menuItemCombo.setModel(new DefaultComboBoxModel(new String[] { "Food", "Drinks", "Desserts", "Other" }));
		menuItemCombo.setBounds(134, 33, 106, 20);
		addMenuPane.add(menuItemCombo);

		menuItemCombo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				updateItemCodeTF();
			}
		});

		JButton btnNewButton_1 = new JButton("Clear");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearMenuForm();
			}
		});
		btnNewButton_1.setBounds(245, 181, 89, 23);
		addMenuPane.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("Save");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (validateMenuFields(menuItemTF.getText().trim(), itemPriceTF.getText().trim())) {
					updateItemCodeTF();
					menu = new Menu();
					menu.setItemGrp(menuItemCombo.getSelectedItem().toString());
					menu.setMenuItem(menuItemTF.getText().trim());
					menu.setPrice(Float.parseFloat(itemPriceTF.getText().trim()));
					menu.setItemCode(Integer.parseInt(itemCodeTF.getText().trim()));

					try {
						db = new DBConnection();
						int result = db.addNewItem(menu);
						if (result > 0) {
							utility.showAleart("Added Successfully..");
							clearMenuForm(menuItemCombo.getSelectedIndex());
						}
					} catch (Exception e) {
						utility.showAleart("Error, while adding new menu item due to : " + e.getMessage());
						log.error("Exception", e);
					} finally {
						try {
							db.closeConnection();
						} catch (SQLException e) {
							log.error("Exception", e);
						}
					}
					menuItemTF.requestFocusInWindow();
				}
			}
		});
		btnNewButton_2.setBounds(134, 181, 89, 23);
		addMenuPane.add(btnNewButton_2);

		JLabel lblCategory = new JLabel("Category :");
		lblCategory.setBounds(36, 36, 97, 14);
		addMenuPane.add(lblCategory);

		JLabel lblNewLabel = new JLabel("Item Code :");
		lblNewLabel.setBounds(36, 67, 66, 14);
		addMenuPane.add(lblNewLabel);

		itemCodeTF = new JTextField();
		itemCodeTF.setEditable(false);
		itemCodeTF.setBounds(134, 64, 106, 20);
		addMenuPane.add(itemCodeTF);
		itemCodeTF.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setBounds(265, 67, 431, 14);
		addMenuPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_35 = new JLabel("Keep name short if possible as it will get printed on bill, e.g. Sandwich = S/W");
		lblNewLabel_35.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblNewLabel_35.setBounds(460, 98, 415, 14);
		addMenuPane.add(lblNewLabel_35);

		JPanel modifyMenuPane = new JPanel();
		menuTab.addTab("Modify", null, modifyMenuPane, null);
		modifyMenuPane.setLayout(null);

		JLabel lblNewLabel_12 = new JLabel("Category :");
		lblNewLabel_12.setBounds(10, 15, 74, 14);
		modifyMenuPane.add(lblNewLabel_12);

		JComboBox menuCateCombo = new JComboBox();
		menuCateCombo.setModel(new DefaultComboBoxModel(new String[] { "Food", "Drinks", "Desserts", "Other" }));
		menuCateCombo.setBounds(82, 12, 145, 20);
		modifyMenuPane.add(menuCateCombo);

		viewMenuBtn = new JButton("View");
		viewMenuBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					db = new DBConnection();
					ResultSet rs = db.getMenuByCategory((menuCateCombo.getSelectedItem().toString()));
					viewMenuTable(rs);
				} catch (Exception e) {
					utility.showAleart("Error, while fetching Menu category due to : " + e.getMessage());
					log.error("Exception", e);
				} finally {
					try {
						db.closeConnection();
					} catch (SQLException e) {
						log.error("Exception", e);
					}
				}
			}

		});
		viewMenuBtn.setBounds(249, 11, 89, 23);
		modifyMenuPane.add(viewMenuBtn);

		scrollPane_2 = new JScrollPane();
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setBounds(10, 50, 865, 366);
		modifyMenuPane.add(scrollPane_2);

		JTabbedPane taxndisc = new JTabbedPane(JTabbedPane.TOP);
		mgmtTab.addTab("Tax/Discount", null, taxndisc, null);

		JPanel panel_2 = new JPanel();
		taxndisc.addTab("Add New", null, panel_2, null);
		panel_2.setLayout(null);

		JLabel lblDiscountPercentage = new JLabel("Discount Percentage :");
		lblDiscountPercentage.setBounds(36, 57, 125, 14);
		panel_2.add(lblDiscountPercentage);

		discTF = new JTextField();
		discTF.setBounds(160, 54, 86, 20);
		panel_2.add(discTF);
		discTF.setColumns(10);

		JButton discAddBtn = new JButton("Add");
		discAddBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean validated = validateDiscount(discTF.getText().trim());
				if (validated) {
					addDiscount(discTF.getText().trim());
				}
			}
		});
		discAddBtn.setBounds(160, 83, 71, 23);
		panel_2.add(discAddBtn);

		JLabel lblNewLabel_25 = new JLabel("%");
		lblNewLabel_25.setBounds(247, 57, 46, 14);
		panel_2.add(lblNewLabel_25);

		JPanel panel_5 = new JPanel();
		panel_5.setBounds(25, 117, 350, 185);
		panel_2.add(panel_5);
		panel_5.setLayout(null);

		JLabel lblDefineNewTax = new JLabel("Define New Tax");
		lblDefineNewTax.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDefineNewTax.setBounds(105, 11, 163, 14);
		panel_5.add(lblDefineNewTax);

		JLabel lblTaxFullDescription = new JLabel("Tax Description :");
		lblTaxFullDescription.setBounds(10, 36, 106, 14);
		panel_5.add(lblTaxFullDescription);

		txDescTF = new JTextField();
		txDescTF.setBounds(115, 30, 204, 20);
		panel_5.add(txDescTF);
		txDescTF.setColumns(10);

		JLabel lblTaxShortName = new JLabel("Tax Short Name :");
		lblTaxShortName.setBounds(10, 65, 106, 14);
		panel_5.add(lblTaxShortName);

		txShrtNmTF = new JTextField();
		txShrtNmTF.setBounds(115, 59, 86, 20);
		panel_5.add(txShrtNmTF);
		txShrtNmTF.setColumns(10);

		JLabel lblNewLabel_26 = new JLabel("e.g. CGST @9%");
		lblNewLabel_26.setBounds(213, 62, 106, 14);
		panel_5.add(lblNewLabel_26);

		JLabel lblNewLabel_27 = new JLabel("Tax :");
		lblNewLabel_27.setBounds(51, 90, 54, 14);
		panel_5.add(lblNewLabel_27);

		txPercTF = new JTextField();
		txPercTF.setBounds(115, 87, 86, 20);
		panel_5.add(txPercTF);
		txPercTF.setColumns(10);

		JButton taxAddBtn = new JButton("Add");
		taxAddBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String taxDesc = txDescTF.getText().trim();
				String taxShortName = txShrtNmTF.getText().trim();
				String taxPerc = txPercTF.getText().trim();
				if (validateTaxFields(taxDesc, taxShortName, taxPerc)) {
					int result = defineNewTax(taxDesc, taxShortName, taxPerc);
					if (result > 0) {
						utility.showAleart("Tax added in the system.");
						clearTaxFields();
					}
				}
			}
		});
		taxAddBtn.setBounds(125, 118, 68, 23);
		panel_5.add(taxAddBtn);

		JLabel lblNoteTaxShort = new JLabel("Note: Tax Short Name along with Tax % will get printed on bill.");
		lblNoteTaxShort.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblNoteTaxShort.setBounds(10, 155, 340, 14);
		panel_5.add(lblNoteTaxShort);

		JLabel label = new JLabel("%");
		label.setBounds(202, 90, 46, 14);
		panel_5.add(label);

		JLabel lblNewLabel_28 = new JLabel("Define New Discount");
		lblNewLabel_28.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_28.setBounds(131, 32, 162, 14);
		panel_2.add(lblNewLabel_28);

		JPanel panel_3 = new JPanel();
		taxndisc.addTab("Modify", null, panel_3, null);
		panel_3.setLayout(null);

		JPanel panel_6 = new JPanel();
		panel_6.setBounds(10, 11, 328, 405);
		panel_3.add(panel_6);
		panel_6.setLayout(null);

		discScrollPan = new JScrollPane();
		discScrollPan.setBounds(0, 60, 328, 279);
		panel_6.add(discScrollPan);

		viewDiscBtn = new JButton("View Discount");
		viewDiscBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					db = new DBConnection();
					ResultSet rs = db.getDiscounts();
					viewDiscountTable(rs);
				} catch (Exception e1) {
					utility.showAleart("Error, while fetching Discount details due to : " + e1.getMessage());
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
		viewDiscBtn.setBounds(91, 26, 150, 23);
		panel_6.add(viewDiscBtn);

		JPanel panel_7 = new JPanel();
		panel_7.setBounds(365, 11, 510, 405);
		panel_3.add(panel_7);
		panel_7.setLayout(null);

		taxScrollPan = new JScrollPane();
		taxScrollPan.setBounds(10, 60, 490, 279);
		panel_7.add(taxScrollPan);

		viewTaxBtn = new JButton("View Tax");
		viewTaxBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					db = new DBConnection();
					ResultSet rs = db.getTaxes();
					viewTaxTable(rs);
				} catch (Exception e1) {
					utility.showAleart("Error, while fetching Discount details due to : " + e1.getMessage());
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
		viewTaxBtn.setBounds(204, 26, 106, 23);
		panel_7.add(viewTaxBtn);

		JTabbedPane emailTab = new JTabbedPane(JTabbedPane.TOP);
		mgmtTab.addTab("EOD Email", null, emailTab, null);

		JPanel panel_8 = new JPanel();
		emailTab.addTab("Add New", null, panel_8, null);
		panel_8.setLayout(null);

		JLabel lblEodReceiverEmail = new JLabel("EOD Report Receiver Email Id :");
		lblEodReceiverEmail.setBounds(22, 28, 173, 14);
		panel_8.add(lblEodReceiverEmail);

		emailAddTF = new JTextField();
		emailAddTF.setBounds(200, 25, 227, 20);
		panel_8.add(emailAddTF);
		emailAddTF.setColumns(10);

		JLabel lblNewLabel_22 = new JLabel("Note : Make sure Email Address is correct to receiev EOD reports.");
		lblNewLabel_22.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblNewLabel_22.setBounds(22, 106, 423, 14);
		panel_8.add(lblNewLabel_22);

		JButton addEODRecEmail = new JButton("Add");
		addEODRecEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean validated = validateEmailAdd(emailAddTF.getText().trim());
				if (validated) {
					addNewEmailAddress(emailAddTF.getText().trim());
				}
			}
		});
		addEODRecEmail.setBounds(257, 63, 89, 23);
		panel_8.add(addEODRecEmail);

		JPanel panel_9 = new JPanel();
		emailTab.addTab("Modify", null, panel_9, null);
		panel_9.setLayout(null);

		viewRecEODRepBtn = new JButton("View Receivers of EOD Reports");
		viewRecEODRepBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					db = new DBConnection();
					ResultSet rs = db.getToEmail();
					viewEODRepReceiver(rs);
				} catch (Exception e1) {
					utility.showAleart("Error, while fetching Email Addess details due to : " + e1.getMessage());
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
		viewRecEODRepBtn.setBounds(47, 31, 222, 23);
		panel_9.add(viewRecEODRepBtn);

		viewRecEODRepScroll = new JScrollPane();
		viewRecEODRepScroll.setBounds(26, 65, 519, 165);
		panel_9.add(viewRecEODRepScroll);
		
		viewSenderEODRepBtn = new JButton("View Sender of EOD Report");
		viewSenderEODRepBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					db = new DBConnection();
					ResultSet rs = db.getfromEmail();
					viewEODSenderReceiver(rs);
				} catch (Exception e1) {
					utility.showAleart("Error, while fetching sender Email Addess details due to : " + e1.getMessage());
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
		viewSenderEODRepBtn.setBounds(47, 251, 222, 23);
		panel_9.add(viewSenderEODRepBtn);
		
		viewSenderEODRepScroll = new JScrollPane();
		viewSenderEODRepScroll.setBounds(26, 292, 519, 71);
		panel_9.add(viewSenderEODRepScroll);

		JTabbedPane billheadfoot = new JTabbedPane(JTabbedPane.TOP);
		mgmtTab.addTab("Bill Header/Footer", null, billheadfoot, null);

		JPanel panel_10 = new JPanel();
		billheadfoot.addTab("Add New", null, panel_10, null);
		panel_10.setLayout(null);

		JLabel lblStoreNameOn = new JLabel("Store Name on Bill :");
		lblStoreNameOn.setBounds(31, 46, 122, 14);
		panel_10.add(lblStoreNameOn);

		storeName = new JTextField();
		storeName.setBounds(153, 43, 227, 20);
		panel_10.add(storeName);
		storeName.setColumns(10);

		JLabel lblNewLabel_32 = new JLabel("Address on Bill :");
		lblNewLabel_32.setBounds(43, 85, 100, 14);
		panel_10.add(lblNewLabel_32);

		storeAdd = new JTextField();
		storeAdd.setBounds(153, 82, 583, 20);
		panel_10.add(storeAdd);
		storeAdd.setColumns(10);

		addBillHeaders = new JButton("Add");
		addBillHeaders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean validated = validateBillHeaders(storeName.getText().trim(), storeAdd.getText().trim());
				if (validated) {
					addBillHeaders(storeName.getText().trim(), storeAdd.getText().trim());
				}
			}
		});
		addBillHeaders.setBounds(153, 113, 89, 23);
		panel_10.add(addBillHeaders);
		
		JLabel lblNewLabel_33 = new JLabel("Bill Header");
		lblNewLabel_33.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_33.setBounds(180, 18, 100, 14);
		panel_10.add(lblNewLabel_33);
		
		JLabel lblBillFooter = new JLabel("Bill Footer");
		lblBillFooter.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblBillFooter.setBounds(180, 206, 100, 14);
		panel_10.add(lblBillFooter);
		
		line1TF = new JTextField();
		line1TF.setBounds(153, 231, 227, 20);
		panel_10.add(line1TF);
		line1TF.setColumns(10);
		
		line2TF = new JTextField();
		line2TF.setBounds(153, 267, 227, 20);
		panel_10.add(line2TF);
		line2TF.setColumns(10);
		
		JLabel lblNewLabel_34 = new JLabel("Line 1 :");
		lblNewLabel_34.setBounds(86, 234, 46, 14);
		panel_10.add(lblNewLabel_34);
		
		JLabel lblLine = new JLabel("Line 2 :");
		lblLine.setBounds(86, 270, 46, 14);
		panel_10.add(lblLine);
		
		addBillFooter = new JButton("Add");
		addBillFooter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean validated = validateBillFooters(line1TF.getText().trim(), line2TF.getText().trim());
				if (validated) {
					addBillFooters(line1TF.getText().trim(), line2TF.getText().trim());
				}
			}
		});
		addBillFooter.setBounds(153, 303, 89, 23);
		panel_10.add(addBillFooter);

		JPanel panel_11 = new JPanel();
		billheadfoot.addTab("Modify", null, panel_11, null);
		panel_11.setLayout(null);

		viewBillHeadersBtn = new JButton("View BillHeaders");
		viewBillHeadersBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					db = new DBConnection();
					ResultSet rs = db.getBillheaders();
					viewBillHeaderTable(rs);
				} catch (Exception e1) {
					utility.showAleart("Error, while fetching BillHeaders Addess details due to : " + e1.getMessage());
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
		viewBillHeadersBtn.setBounds(41, 11, 222, 23);
		panel_11.add(viewBillHeadersBtn);

		viewBillHeaderScroll = new JScrollPane();
		viewBillHeaderScroll.setBounds(10, 45, 744, 138);
		panel_11.add(viewBillHeaderScroll);
		
		viewBillFootersBtn = new JButton("View Bill Footers");
		viewBillFootersBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					db = new DBConnection();
					ResultSet rs = db.getBillFooters();
					viewBillFooterTable(rs);
				} catch (Exception e1) {
					utility.showAleart("Error, while fetching BillFooter details due to : " + e1.getMessage());
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
		viewBillFootersBtn.setBounds(41, 203, 222, 23);
		panel_11.add(viewBillFootersBtn);
		
		viewBillFooterScroll = new JScrollPane();
		viewBillFooterScroll.setBounds(10, 239, 744, 138);
		panel_11.add(viewBillFooterScroll);

		JTabbedPane orderHisTab = new JTabbedPane(JTabbedPane.TOP);
		bodyTab.addTab("Order History", null, orderHisTab, null);

		JPanel panel_4 = new JPanel();
		orderHisTab.addTab("All Orders", null, panel_4, null);
		panel_4.setLayout(null);

		searchByPanel = new JPanel();
		searchByPanel.setBounds(10, 11, 371, 120);
		panel_4.add(searchByPanel);
		searchByPanel.setLayout(null);

		JLabel lblNewLabel_23 = new JLabel("Order Status :");
		lblNewLabel_23.setBounds(21, 25, 91, 14);
		searchByPanel.add(lblNewLabel_23);

		JLabel lblNewLabel_24 = new JLabel("Order Date :");
		lblNewLabel_24.setBounds(21, 50, 77, 14);
		searchByPanel.add(lblNewLabel_24);

		orderStatCombo = new JComboBox();
		orderStatCombo.setModel(new DefaultComboBoxModel(new String[] { "-- Select --", "Cancelled", "Served" }));
		orderStatCombo.setBounds(109, 22, 101, 20);
		searchByPanel.add(orderStatCombo);

		orderDateCombo = new JComboBox();
		orderDateCombo.setModel(new DefaultComboBoxModel(new String[] { "-- Select --" }));
		orderDateCombo.setBounds(109, 47, 101, 20);
		searchByPanel.add(orderDateCombo);

		orderNoCombo = new JComboBox();
		orderNoCombo.setModel(new DefaultComboBoxModel(new String[] { "-- Select --" }));
		orderNoCombo.setBounds(109, 72, 101, 20);
		searchByPanel.add(orderNoCombo);

		orderStatCombo.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				orderDateCombo.removeAllItems();
				orderDateCombo.addItem("-- Select --");

				orderNoCombo.removeAllItems();
				orderNoCombo.addItem("-- Select --");
			}
		});

		orderDateCombo.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				orderNoCombo.removeAllItems();
				orderNoCombo.addItem("-- Select --");
			}
		});

		JButton viewOrdBtn = new JButton("View");
		viewOrdBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (validateOrdHistFields(orderStatCombo, orderDateCombo, orderNoCombo)) {
					viewOrderDetails(orderStatCombo.getSelectedItem().toString(),
							orderDateCombo.getSelectedItem().toString(), orderNoCombo.getSelectedItem().toString());
				}
			}
		});
		viewOrdBtn.setBounds(231, 71, 130, 23);
		searchByPanel.add(viewOrdBtn);

		JLabel lblNewLabel_30 = new JLabel("Order No :");
		lblNewLabel_30.setBounds(21, 75, 77, 14);
		searchByPanel.add(lblNewLabel_30);

		fetchDatesBtn = new JButton("Fetch Dates");
		fetchDatesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (orderStatCombo.getSelectedIndex() != 0) {
					boolean loaded = loadOrderDate(orderStatCombo.getSelectedItem().toString());
					if(loaded)
					utility.showAleart("Fetched Order Dates.");
				} else {
					utility.showAleart("Please Choose Status");
				}
			}
		});
		fetchDatesBtn.setBounds(231, 21, 130, 23);
		searchByPanel.add(fetchDatesBtn);

		fetchOrdersBtn = new JButton("Fetch Orders");
		fetchOrdersBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (orderDateCombo.getSelectedIndex() != 0) {
					boolean loaded = loadOrderNo(orderDateCombo.getSelectedItem().toString(),
							orderStatCombo.getSelectedItem().toString());
					if (loaded)
						utility.showAleart("Fetched Orders");
				}
			}
		});
		fetchOrdersBtn.setBounds(231, 46, 130, 23);
		searchByPanel.add(fetchOrdersBtn);

		JPanel reports = new JPanel();
		bodyTab.addTab("Reports", null, reports, null);
		reports.setLayout(null);

		JLabel lblNewLabel_29 = new JLabel("Report :");
		lblNewLabel_29.setBounds(25, 30, 61, 14);
		reports.add(lblNewLabel_29);

		repCombo = new JComboBox();
		repCombo.setModel(new DefaultComboBoxModel(new String[] { "-- Select --", "Attendance", "Expenses", "Inventory",
				"Opening Balance", "Orders", "Transactions" }));
		repCombo.setBounds(85, 27, 203, 20);
		reports.add(repCombo);

		repCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (repCombo.getSelectedItem().toString().equals("Orders")) {
					toDateLbl.setVisible(false);
					repToDD.setVisible(false);
					repToMM.setVisible(false);
					repToYY.setVisible(false);
					fromDateLbl.setText("Date");
				} else {
					toDateLbl.setVisible(true);
					repToDD.setVisible(true);
					repToMM.setVisible(true);
					repToYY.setVisible(true);
					fromDateLbl.setText("From :");
				}
			}
		});

		fromDateLbl = new JLabel("From :");
		fromDateLbl.setBounds(25, 103, 61, 14);
		reports.add(fromDateLbl);

		repFrDD = new JComboBox();
		repFrDD.setModel(new DefaultComboBoxModel(new String[] { "DD", "01", "02", "03", "04", "05", "06", "07", "08",
				"09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25",
				"26", "27", "28", "29", "30", "31" }));
		repFrDD.setBounds(85, 100, 51, 20);
		reports.add(repFrDD);

		repFrMM = new JComboBox();
		repFrMM.setModel(new DefaultComboBoxModel(new String[] { "MM", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
				"Aug", "Sep", "Oct", "Nov", "Dec" }));
		repFrMM.setBounds(146, 100, 61, 20);
		reports.add(repFrMM);

		repFrYY = new JComboBox();
		repFrYY.setModel(new DefaultComboBoxModel(new String[] { "YY", "2017", "2018", "2019", "2020", "2021", "2022",
				"2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033", "2034", "2035",
				"2036", "2037", "2038", "2039", "2040", "2041", "2042", "2043", "2044", "2045", "2046", "2047", "2048",
				"2049", "2050" }));
		repFrYY.setBounds(220, 100, 68, 20);
		reports.add(repFrYY);

		repToDD = new JComboBox();
		repToDD.setModel(new DefaultComboBoxModel(new String[] { "DD", "01", "02", "03", "04", "05", "06", "07", "08",
				"09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25",
				"26", "27", "28", "29", "30", "31" }));
		repToDD.setBounds(85, 135, 51, 20);
		reports.add(repToDD);

		repToMM = new JComboBox();
		repToMM.setModel(new DefaultComboBoxModel(new String[] { "MM", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
				"Aug", "Sep", "Oct", "Nov", "Dec" }));
		repToMM.setBounds(146, 135, 61, 20);
		reports.add(repToMM);

		repToYY = new JComboBox();
		repToYY.setModel(new DefaultComboBoxModel(new String[] { "YY", "2017", "2018", "2019", "2020", "2021", "2022",
				"2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033", "2034", "2035",
				"2036", "2037", "2038", "2039", "2040", "2041", "2042", "2043", "2044", "2045", "2046", "2047", "2048",
				"2049", "2050" }));
		repToYY.setBounds(220, 135, 68, 20);
		reports.add(repToYY);

		toDateLbl = new JLabel("To :");
		toDateLbl.setBounds(25, 138, 46, 14);
		reports.add(toDateLbl);

		JLabel lblNewLabel_31 = new JLabel("Period :");
		lblNewLabel_31.setBounds(25, 63, 61, 14);
		reports.add(lblNewLabel_31);

		repTodayRadio = new JRadioButton("Today Till Now");
		repTodayRadio.setBounds(85, 59, 114, 23);
		reports.add(repTodayRadio);

		repDateRngRadio = new JRadioButton("Date Range");
		repDateRngRadio.setSelected(true);
		repDateRngRadio.setBounds(202, 59, 109, 23);
		reports.add(repDateRngRadio);

		repTodayRadio.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (repTodayRadio.isSelected()) {
					repFrDD.setEnabled(false);
					repFrMM.setEnabled(false);
					repFrYY.setEnabled(false);
					repToDD.setEnabled(false);
					repToMM.setEnabled(false);
					repToYY.setEnabled(false);
				}

			}
		});

		repDateRngRadio.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (repDateRngRadio.isSelected()) {
					repFrDD.setEnabled(true);
					repFrMM.setEnabled(true);
					repFrYY.setEnabled(true);
					repToDD.setEnabled(true);
					repToMM.setEnabled(true);
					repToYY.setEnabled(true);
				}

			}
		});

		ButtonGroup repBg = new ButtonGroup();
		repBg.add(repTodayRadio);
		repBg.add(repDateRngRadio);

		JButton repGenerateBtn = new JButton("Generate");
		repGenerateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int repName = repCombo.getSelectedIndex();
				int fromDD = repFrDD.getSelectedIndex();
				int fromMM = repFrMM.getSelectedIndex();
				int fromYY = repFrYY.getSelectedIndex();
				int toDD = repToDD.getSelectedIndex();
				int toMM = repToMM.getSelectedIndex();
				int toYY = repToYY.getSelectedIndex();
				if (validateReportsFields(repName, fromDD, fromMM, fromYY, toDD, toMM, toYY,
						repDateRngRadio.isSelected())) {
					String message = "";
					if (repDateRngRadio.isSelected() && !repCombo.getSelectedItem().equals("Orders")) {
						message = "You wish to generate " + repCombo.getSelectedItem().toString() + " report from "
								+ repFrDD.getSelectedItem().toString() + "-" + repFrMM.getSelectedItem().toString()
								+ "-" + repFrYY.getSelectedItem().toString() + " to "
								+ repToDD.getSelectedItem().toString() + "-" + repToMM.getSelectedItem().toString()
								+ "-" + repToYY.getSelectedItem().toString();
					} else if (repDateRngRadio.isSelected() && repCombo.getSelectedItem().equals("Orders")) {
						message = "You wish to generate " + repCombo.getSelectedItem().toString() + " report for "
								+ repFrDD.getSelectedItem().toString() + "-" + repFrMM.getSelectedItem().toString()
								+ "-" + repFrYY.getSelectedItem().toString();
					} else {
						message = "You wish to generate today's " + repCombo.getSelectedItem().toString() + " report.";
					}
					int ans = utility.showConfirmation(message, "Please confirm !");
					if (ans == 0) {
						String report = repCombo.getSelectedItem().toString();
						boolean rangedRep = repDateRngRadio.isSelected();
						String fromDate = repFrYY.getSelectedItem().toString() + "-0" + repFrMM.getSelectedIndex() + "-"
								+ repFrDD.getSelectedItem().toString();
						String toDate = repToYY.getSelectedItem().toString() + "-0" + repToMM.getSelectedIndex() + "-"
								+ repToDD.getSelectedItem().toString();
						switch (report) {
						case "Attendance":
							generateAttendanceReport(fromDate, toDate, rangedRep);
							break;
						case "Expenses":
							generateExpenseReport(fromDate, toDate, rangedRep);
							break;
						case "Inventory":
							generateInventoryReport(fromDate, toDate, rangedRep);
							break;
						case "Opening Balance":
							generateOpeningBalReport(fromDate, toDate, rangedRep);
							break;
						case "Orders":
							generateOrdersReport(fromDate, rangedRep);
							break;
						case "Transactions":
							generateTxnReport(fromDate, toDate, rangedRep);
							break;
						}
						clearReportsFields();
					}
				}
			}

		});
		repGenerateBtn.setBounds(135, 182, 89, 23);
		reports.add(repGenerateBtn);

		JPanel storeOps = new JPanel();
		bodyTab.addTab("Store Operations", null, storeOps, null);
		storeOps.setLayout(null);

		JButton forceReopenBtn = new JButton("Force Reopen Store");
		forceReopenBtn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		forceReopenBtn.setBounds(30, 28, 176, 41);
		storeOps.add(forceReopenBtn);

		JButton resetOpeningBal = new JButton("Reset Opening Bal");
		resetOpeningBal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetOpeningBal();
			}
		});
		resetOpeningBal.setFont(new Font("Tahoma", Font.PLAIN, 12));
		resetOpeningBal.setBounds(30, 80, 176, 41);
		storeOps.add(resetOpeningBal);

		JButton btnNewButton_3 = new JButton("Store Closing History");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					db = new DBConnection();
					ResultSet rs = db.getStoreClosedDetails();
					viewStoreClosedTable(rs);
				} catch (Exception e1) {
					utility.showAleart("Error, while fetching Store closed details due to : " + e1.getMessage());
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
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_3.setBounds(712, 28, 160, 41);
		storeOps.add(btnNewButton_3);

		storeCloseScrollPan = new JScrollPane();
		storeCloseScrollPan.setBounds(712, 80, 160, 366);
		storeOps.add(storeCloseScrollPan);

		forceReopenBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int ans = utility.showConfirmation("You wish to force reopen the store which is closed by Manager?",
						"Are you sure?");
				if (ans == 0) {
					try {
						db = new DBConnection();
						int result = db.forceReopenStore();
						if (result > 0) {
							utility.showAleart("Store Reopend Successfully.");
						} else {
							utility.showAleart("Store is already open. No need to reopen it.");
						}
					} catch (Exception e) {
						utility.showAleart("Error, while reopening the store due to : " + e.getMessage());
						log.error("Exception", e);
					} finally {
						try {
							db.closeConnection();
						} catch (SQLException e) {
							log.error("Exception", e);
						}
					}
				}
			}
		});

		// adminHome.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		adminHome.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		adminHome.setVisible(true);
		log.info("completed");
	}

	protected void addNewEmailAddress(String emailAdd) {
		log.info("Started");
		try {
			db = new DBConnection();
			int no = db.addNewEmailAdd(emailAdd);
			if (no > 0) {
				utility.showAleart("New Email Address : " + emailAdd + " added successfully..!");
				emailAddTF.setText("");
				emailAddTF.requestFocusInWindow();
			}
		} catch (Exception e) {
			utility.showAleart("Error, while adding new email address due to : " + e.getMessage());
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

	protected boolean validateEmailAdd(String email) {
		if (email.equals("")) {
			utility.showAleart("Email Address cannot be blank !");
			return false;
		}
		return true;
	}

	private void viewStaffTable(ResultSet rs) throws SQLException {
		log.info("Started");
		String[] cols = { "Staff No", "Category", "Referred By", "FirstName", "MiddleName", "LastName", "Gender",
				"Address", "Birth Date", "Mobile No", "Tel No", "ID Proof", "ID No", "Date Of Joining",
				"Date Of Leaving" };

		int totalRows = 0;
		while (rs.next()) {
			totalRows++;
		}
		rs.beforeFirst();
		String[][] data = new String[totalRows][15];
		/*
		 * { { "--", "--", "--", "--", "--", "--", "--", "--", "--", "--", "--",
		 * "--", "--", "--" }, { "--", "--", "--", "--", "--", "--", "--", "--",
		 * "--", "--", "--", "--", "--", "--" }, { "--", "--", "--", "--", "--",
		 * "--", "--", "--", "--", "--", "--", "--", "--", "--" }, { "--", "--",
		 * "--", "--", "--", "--", "--", "--", "--", "--", "--", "--", "--",
		 * "--" }, { "--", "--", "--", "--", "--", "--", "--", "--", "--", "--",
		 * "--", "--", "--", "--" }, { "--", "--", "--", "--", "--", "--", "--",
		 * "--", "--", "--", "--", "--", "--", "--" }, { "--", "--", "--", "--",
		 * "--", "--", "--", "--", "--", "--", "--", "--", "--", "--" }, { "--",
		 * "--", "--", "--", "--", "--", "--", "--", "--", "--", "--", "--",
		 * "--", "--" }, { "--", "--", "--", "--", "--", "--", "--", "--", "--",
		 * "--", "--", "--", "--", "--" }, { "--", "--", "--", "--", "--", "--",
		 * "--", "--", "--", "--", "--", "--", "--", "--" }, { "--", "--", "--",
		 * "--", "--", "--", "--", "--", "--", "--", "--", "--", "--", "--" }, {
		 * "--", "--", "--", "--", "--", "--", "--", "--", "--", "--", "--",
		 * "--", "--", "--" }, { "--", "--", "--", "--", "--", "--", "--", "--",
		 * "--", "--", "--", "--", "--", "--" }};
		 */
		int i = 0;
		while (rs.next()) {
			for (int j = 1; j <= 15; j++) {
				if (j == 1)
					data[i][j - 1] = String.valueOf(rs.getInt(j));

				if ((j >= 2 && j <= 9) || j >= 12 && j <= 13)
					data[i][j - 1] = rs.getString(j);

				if (j >= 10 && j <= 11)
					data[i][j - 1] = String.valueOf(rs.getLong(j));

				if (j >= 14 && j <= 15)
					data[i][j - 1] = rs.getString(j);

			}
			i++;
		}

		DefaultTableModel model = new DefaultTableModel(data, cols) {
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

		scrollPane_1.setViewportView(table);

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				Point p = me.getPoint();

				if (me.getClickCount() == 2) {
					int row = table.getSelectedRow();
					int totCol = table.getColumnCount();
					if (row != -1) {
						Staff s = new Staff();
						String dob[] = new String[3];
						String doj[] = new String[3];
						s.setStaffNo(Integer.parseInt(model.getValueAt(row, 0).toString()));
						s.setCategory(model.getValueAt(row, 1).toString());
						s.setRefdby(model.getValueAt(row, 2).toString());
						s.setFname(model.getValueAt(row, 3).toString());
						s.setMname(model.getValueAt(row, 4).toString());
						s.setLname(model.getValueAt(row, 5).toString());
						s.setGender(model.getValueAt(row, 6).toString());
						s.setAddress(model.getValueAt(row, 7).toString());
						dob = model.getValueAt(row, 8).toString().split("-");
						s.setMobile(Long.parseLong(model.getValueAt(row, 9).toString()));
						s.setTelephone(Long.parseLong(model.getValueAt(row, 10).toString()));
						s.setIdproof(model.getValueAt(row, 11).toString());
						s.setIdno(model.getValueAt(row, 12).toString());
						doj = model.getValueAt(row, 13).toString().split("-");
						// System.out.println(dob[0] + "" + dob[1] + "" +
						// dob[2]);
						new ModifyStaff(s, dob, doj);
					}
				}
			}
		});
		log.info("Completed");
	}

	private void viewMenuTable(ResultSet rs) throws SQLException {
		log.info("Started");
		String[] cols = { "Item Code", "Category", "Item Name", "Price" };
		int totalRows = 0;
		while (rs.next()) {
			totalRows++;
		}
		rs.beforeFirst();
		String[][] data = new String[totalRows][4];

		int i = 0;
		while (rs.next()) {
			for (int j = 1; j <= 4; j++) {

				if ((j >= 1 && j <= 2))
					data[i][j - 1] = rs.getString(j);

				if (j == 3)
					data[i][j - 1] = rs.getString(j);

				if (j == 4)
					data[i][j - 1] = String.valueOf(rs.getFloat(j));

			}
			i++;
		}

		DefaultTableModel model = new DefaultTableModel(data, cols) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);
		scrollPane_2.setViewportView(table);

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				if (me.getClickCount() == 2) {
					int row = table.getSelectedRow();
					int totCol = table.getColumnCount();
					if (row != -1) {
						// "Category", "Item Code", "Item Name", "Price" };
						String itemCode = model.getValueAt(row, 0).toString();
						String itemCat = model.getValueAt(row, 1).toString();
						String itemName = model.getValueAt(row, 2).toString();
						float price = Float.parseFloat(model.getValueAt(row, 3).toString());
						ModifyMenu m = new ModifyMenu(itemCode, itemCat, itemName, price);
					}
				}
			}
		});
		log.info("Completed");
	}

	private void viewOrderTable(ResultSet rs) throws SQLException {
		log.info("Started");
		String[] cols = { "No", "Item Name", "Quanity", "Price", "Amount" };
		int totalRows = 0;
		while (rs.next()) {
			totalRows++;
		}
		rs.beforeFirst();
		String[][] data = new String[totalRows][5];

		int i = 0;
		while (rs.next()) {
			for (int j = 1; j <= 4; j++) {
				if ((j >= 1 && j <= 2))
					data[i][j - 1] = rs.getString(j);

				if (j == 3)
					data[i][j - 1] = String.valueOf(rs.getFloat(j));

				if (j == 4)
					data[i][j - 1] = ((rs.getBoolean(j))) ? "Yes" : "No";

			}
			i++;
		}

		DefaultTableModel model = new DefaultTableModel(data, cols) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);
		scrollPane_2.setViewportView(table);

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				if (me.getClickCount() == 2) {

				}
			}
		});
		log.info("Completed");
	}

	public void updateLastLogin(User user) {
		log.info("Started");
		try {
			db = new DBConnection();
			db.updateUserLastLogin(user);
		} catch (Exception e) {
			utility.showAleart("Error, while updating last login due to : " + e.getMessage());
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

	private void clearMenuForm(int index) {
		itemCodeTF.setText("");
		menuItemTF.setText("");
		itemPriceTF.setText("");
		menuItemCombo.setSelectedIndex(index);
	}

	private void clearMenuForm() {
		itemCodeTF.setText("");
		menuItemTF.setText("");
		itemPriceTF.setText("");
		menuItemCombo.setSelectedIndex(0);
	}

	private void clearAddNewStaffForm() {
		fnameTF.setText("");
		refdByTF.setText("");
		lnameTF.setText("");
		mnameTF.setText("");
		addressTA.setText("");
		mobileTF.setText("");
		teleTF.setText("");
		dobDD.setSelectedIndex(0);
		dobMM.setSelectedIndex(0);
		dobYY.setSelectedIndex(0);
		categoryCombo.setSelectedIndex(0);
		proofCombo.setSelectedIndex(0);
		idTF.setText("");

	}

	private void clearCustomerForm() {
		custFnameTF.setText("");
		custLnameTF.setText("");
		custAddTA.setText("");
		custEmailTF.setText("");
		custContactTF.setText("");

		custDDCombo.setSelectedIndex(0);
		custMMCombo.setSelectedIndex(0);
		custYYCombo.setSelectedIndex(0);
	}

	protected boolean newStaffvalidated(JComboBox categoryCombo, JTextField refdByTF2, JTextField fnameTF2,
			JTextField mnameTF2, JTextField lnameTF2, JTextArea addressTA2, JTextField mobileTF2, JTextField teleTF2,
			JTextField idTF2, JComboBox idProof) {
		log.info("Started");
		if (categoryCombo.getSelectedIndex() == 0) {
			utility.showAleart("Please choose Categoty !");
			return false;
		} else if (refdByTF2.getText().trim().equals("")) {
			utility.showAleart("Referred By Cannot be blank !");
			return false;
		} else if (fnameTF2.getText().trim().equals("")) {
			utility.showAleart("First Name Cannot be blank !");
			return false;
		} else if (lnameTF2.getText().trim().equals("")) {
			utility.showAleart("Last Name Cannot be blank !");
			return false;
		} else if (addressTA2.getText().trim().equals("")) {
			utility.showAleart("Address Cannot be blank !");
			return false;
		} else if (mobileTF2.getText().trim().equals("")) {
			utility.showAleart("Mobile No. Cannot be blank !");
			return false;
		} else if (idTF2.getText().trim().equals("")) {
			utility.showAleart("ID No. Cannot be blank !");
			return false;
		} else if (idProof.getSelectedIndex() == 0) {
			utility.showAleart("Please choose ID Proof !");
			return false;
		}
		log.info("Completed");
		return true;
	}

	protected boolean numberValidated(JTextField mobileTF2, JTextField teleTF2) {
		log.info("Started");
		// TODO Auto-generated method stub
		String mobile = mobileTF2.getText().trim();
		String tele = teleTF2.getText().trim();
		if (!mobile.matches("\\d*")) {
			utility.showAleart("Mobile No. must contain only Digits");
			return false;
		} else if (!tele.equals("")) {
			if (!tele.matches("\\d*")) { // ---> .*\\d+.*
				utility.showAleart("Telephone No. must contain only Digits");
				return false;
			}
		} else if (mobile.length() != 10) {
			utility.showAleart("Mobile No. must have 10 Digits");
			return false;
		}
		log.info("Completed");
		return true;
	}

	public void viewStaff() {
		log.info("Started");
		try {
			db = new DBConnection();
			ResultSet rs = db.getStaffByCategoty(categoryTab.getSelectedItem().toString());
			viewStaffTable(rs);
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

	private boolean validateDiscount(String disc) {
		log.info("Started");
		boolean result = true;
		if (disc.equals("")) {
			utility.showAleart("Percentage cannot be blank.");
			return false;
		}
		if (!disc.matches("^([+-]?(\\d+\\.)?\\d+)$") || !disc.matches(".*\\d+.*")) {
			utility.showAleart("Percentage must be number or decimal number.");
			return false;
		}
		log.info("Completed");
		return result;
	}

	private void addDiscount(String disc) {
		log.info("Started");
		try {
			db = new DBConnection();
			int no = db.addNewDiscount(Float.parseFloat(disc));
			if (no > 0) {
				utility.showAleart("New Discount " + disc + "% added successfully..!");
				discTF.setText("");
				discTF.requestFocusInWindow();
			}
		} catch (Exception e) {
			utility.showAleart("Error, while adding new discount due to : " + e.getMessage());
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

	private void updateItemCodeTF() {
		log.info("Started");
		try {
			db = new DBConnection();
			ResultSet rs = db.getNextItemCode();
			while (rs.next()) {
				itemCodeTF.setText(String.valueOf(rs.getInt(1) + 1));
			}

		} catch (Exception e) {
			utility.showAleart("Error, while fetching item code due to : " + e.getMessage());
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

	private boolean validateTaxFields(String taxDesc, String taxShortName, String taxPerc) {
		log.info("Started");
		if (taxDesc.equals("")) {
			utility.showAleart("Tax Description cannot be blank.");
			return false;
		}
		if (taxDesc.length() > 50) {
			utility.showAleart("Description should not exceed 50 characters.");
			return false;
		}
		if (taxShortName.equals("")) {
			utility.showAleart("Tax Short Name cannot be blank.");
			return false;
		}
		if (taxShortName.length() > 10) {
			utility.showAleart("Tax Short Name should not exceed 10 characters.");
			return false;
		}
		if (taxPerc.equals("")) {
			utility.showAleart("Tax Percentage cannot be blank.");
			return false;
		}
		if (!taxPerc.matches("^([+-]?(\\d+\\.)?\\d+)$") || !taxPerc.matches(".*\\d+.*")) {
			utility.showAleart("Tax Percentage be a number or decimal number.");
			return false;
		}
		log.info("Completed");
		return true;
	}

	private void clearTaxFields() {
		txDescTF.setText("");
		txShrtNmTF.setText("");
		txPercTF.setText("");
		txDescTF.requestFocusInWindow();
	}

	private int defineNewTax(String taxDesc, String taxShortName, String taxPerc) {
		log.info("Started");
		int result = 0;
		try {
			db = new DBConnection();
			result = db.addNewlyDefinedTax(taxDesc, taxShortName, taxPerc);
		} catch (Exception e) {
			utility.showAleart("Error, while adding new tax due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception", e);
			}
		}
		log.info("completed");
		return result;
	}

	private boolean validateQtyFields(String item, String qty) {
		log.info("Started");
		if (item.equals("")) {
			utility.showAleart("Item Name cannot be blank.");
			return false;
		}
		if (qty.equals("")) {
			utility.showAleart("Quantity cannot be blank.");
			return false;
		}
		if (!qty.matches("\\d*")) {
			utility.showAleart("Quantity must be in numbers");
			return false;
		}
		log.info("Completed");
		return true;
	}

	private boolean validateMenuFields(String menu, String price) {
		log.info("Started");
		if (menu.equals("")) {
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

	private boolean validateReportsFields(int repName, int fromDD, int fromMM, int fromYY, int toDD, int toMM, int toYY,
			boolean repDateRngelected) {
		log.info("Started");
		if (repName == 0) {
			utility.showAleart("Please choose Report.");
			return false;
		}
		if (repDateRngelected) {
			if (fromDD == 0 || fromMM == 0 || fromYY == 0) {
				utility.showAleart("Please choose Valid From Date.");
				return false;
			}
			if (repName != 5) {
				if (toDD == 0 || toMM == 0 || toYY == 0) {
					utility.showAleart("Please choose Valid To Date.");
					return false;
				}
			}
		}
		log.info("Completed");
		return true;
	}

	private void clearReportsFields() {
		repCombo.setSelectedIndex(0);
		repFrDD.setSelectedIndex(0);
		repFrMM.setSelectedIndex(0);
		repFrYY.setSelectedIndex(0);
		repToDD.setSelectedIndex(0);
		repToMM.setSelectedIndex(0);
		repToYY.setSelectedIndex(0);
		repDateRngRadio.setSelected(true);
		repCombo.requestFocusInWindow();

	}

	private void generateTxnReport(String fromDate, String toDate, boolean rangedRep) {
		log.info("Started");
		try {
			db = new DBConnection();
			TransactionReport report = new TransactionReport();
			out = report.createReport();
			ResultSet rs = null;
			String line = "";
			report.transactionTableHeader();
			if (rangedRep) {
				rs = db.getRangedTransactions(fromDate, toDate);
			} else {
				rs = db.getTodaysTransactions();
			}
			while (rs.next()) {
				line = line + "<tr><td>" + rs.getString(1) + "</td><td>" + rs.getFloat(2) + "</td><td>"
						+ (rs.getInt(3) == 1 ? "Deposited" : "Withdrawn") + "</td><td>" + rs.getTimestamp(4)
						+ "</td></tr>";
			}
			report.transactionTableContent(out, line);
			report.transactionTableComplete();
			out.close();
		} catch (Exception e) {
			utility.showAleart("Error, while generating Opening Balance report due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (Exception e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
	}

	private void generateOpeningBalReport(String fromDate, String toDate, boolean rangedRep) {
		// TODO Auto-generated method stub
		log.info("Started");
		try {
			db = new DBConnection();
			OpeningBalReport report = new OpeningBalReport();
			out = report.createReport();
			ResultSet rs = null;
			String line = "";
			report.openingBalTableHeader();
			if (rangedRep) {
				rs = db.getRangedOpeningBal(fromDate, toDate);
			} else {
				rs = db.getTodaysOpeningBal();
			}
			while (rs.next()) {
				line = line + "<tr><td>" + rs.getTimestamp(1) + "</td><td>" + rs.getFloat(2) + "</td></tr>";
			}
			report.openingBalTableContent(out, line);
			report.openingBalTableComplete();
			out.close();
		} catch (Exception e) {
			utility.showAleart("Error, while generating Opening Balance report due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (Exception e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
	}

	private void generateOrdersReport(String date, boolean rangedRep) {
		log.info("Started");
		try {
			db = new DBConnection();
			OrdersReport report = new OrdersReport();
			out = report.createReport();
			ResultSet rs = null;
			String line = "";
			ArrayList<Integer> servedOrderNos = new ArrayList<Integer>();
			if (rangedRep) {
				rs = db.getServedOrderNoByDate(date);
				while (rs.next()) {
					servedOrderNos.add(rs.getInt(1));
				}
				report.servedOrderTableHeader();
				for (int i = 0; i < servedOrderNos.size(); i++) {
					rs = db.getServedOrderDetailsByDate(servedOrderNos.get(i), date);
					while (rs.next()) {
						line = line + "<tr>" + "<td>" + rs.getInt(1) + "</td>" + "<td>" + rs.getString(2) + "</td>"
								+ "<td>" + rs.getFloat(3) + "</td>" + "<td>" + rs.getFloat(4) + "</td>" + "<td>"
								+ rs.getFloat(5) + "</td>" + "<td>" + rs.getFloat(6) + "</td>" + "<td>" + rs.getFloat(7)
								+ "</td>" + "<td>" + rs.getTimestamp(8) + "</td>" + "<td>" + rs.getTimestamp(9)
								+ "</td>" + "<td>" + rs.getString(10) + "</td>" + "</tr>";
					}
				}

			} else {
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
			}
			report.servedOrderTableContent(out, line);
			report.servedOrderTableComplete();

			// Cancelled Orders.
			line = "";
			ArrayList<Integer> cancelledOrderNos = new ArrayList<Integer>();
			if (rangedRep) {
				rs = db.getCancelledOrderNoByDate(date);
				while (rs.next()) {
					cancelledOrderNos.add(rs.getInt(1));
				}
				report.cancelledOrderTableHeader();
				for (int i = 0; i < cancelledOrderNos.size(); i++) {
					rs = db.getCancelledOrderByDate(cancelledOrderNos.get(i), date);
					while (rs.next()) {
						line = line + "<tr>" + "<td>" + rs.getInt(1) + "</td>" + "<td>" + rs.getString(2) + "</td>"
								+ "<td>" + rs.getFloat(3) + "</td>" + "<td>" + rs.getFloat(4) + "</td>" + "<td>"
								+ rs.getFloat(5) + "</td>" + "<td>" + rs.getTimestamp(6) + "</td>" + "<td>"
								+ rs.getTimestamp(7) + "</td>" + "<td>" + rs.getString(8) + "</td>" + "</tr>";
					}
				}

			} else {
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
			}
			report.cancelledOrderTableContent(out, line);
			report.cancelledOrderTableComplete();
			out.close();
		} catch (Exception e) {
			utility.showAleart("Error, while generating Order report due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (Exception e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
	}

	private void generateInventoryReport(String fromDate, String toDate, boolean rangedRep) {
		log.info("Started");
		try {
			db = new DBConnection();
			InventoryReport report = new InventoryReport();
			out = report.createReport();
			report.inventoryTableHeader();
			ResultSet rs = null;
			if (rangedRep) {
				rs = db.getRangedInventory(fromDate, toDate);
			} else {
				rs = db.getTodaysInventory();
			}
			String line = "";
			while (rs.next()) {
				line = line + "<tr><td>" + rs.getString(1) + "</td><td>" + rs.getInt(2) + "</td></tr>";
			}
			report.inventoryTableContent(out, line);
			report.inventoryTableComplete();
			out.close();
		} catch (Exception e) {
			utility.showAleart("Error, while generating Inventory report due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (Exception e) {
				utility.showAleart(e.getMessage());
			}
		}
		log.info("Completed");
	}

	private void generateExpenseReport(String fromDate, String toDate, boolean rangedRep) {
		log.info("Started");
		try {
			db = new DBConnection();
			ExpenseReport report = new ExpenseReport();
			out = report.createReport();
			report.expensesTableHeader();
			ResultSet rs = null;
			if (rangedRep) {
				rs = db.getRangedExpenseRpt(fromDate, toDate);
			} else {
				rs = db.getTodaysExpense();
			}
			String line = "";
			while (rs.next()) {
				line = line + "<tr><td>" + rs.getInt(1) + "</td><td>" + rs.getString(2) + "</td><td>" + rs.getFloat(3)
						+ "</td><td>" + rs.getTimestamp(4) + "</td></tr>";
			}
			report.expensesTableContent(out, line);
			report.expensesTableComplete();
			out.close();
		} catch (Exception e) {
			utility.showAleart("Error, while generating Expense report due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (Exception e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
	}

	private void generateAttendanceReport(String fromDate, String toDate, boolean rangedRep) {
		log.info("Started");
		try {
			db = new DBConnection();
			AttendanceReport report = new AttendanceReport();
			out = report.createReport();
			report.attendanceTableHeader();
			ResultSet rs = null;
			if (rangedRep) {
				rs = db.getRangedAttendanceRpt(fromDate, toDate);
			} else {
				rs = db.getTodaysAttendance();
			}
			String line = "";
			String duration = null;
			LocalTime inTime = null;
			LocalTime outTime = null;
			String inT = null;
			String outT = null;
			while (rs.next()) {
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
			out.close();
		} catch (Exception e) {
			utility.showAleart("Error, while generating Attendance report due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (Exception e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");

	}

	private void resetOpeningBal() {
		log.info("Started");
		int ans = utility.showConfirmation("Opening Balance will be removed for today's date", "Are you sure?");
		if (ans == 0) {
			try {
				db = new DBConnection();
				int res = db.resetOpeningBal();
				if (res > 0) {
					utility.showAleart("Opening Balance has been reset.");
				}
			} catch (Exception e) {
				utility.showAleart("Error, while resetting Opening Balance due to : " + e.getMessage());
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

	private boolean loadOrderDate(String orderStatus) {
		boolean loaded = false;
		log.info("Started");
		DefaultComboBoxModel model = (DefaultComboBoxModel) orderDateCombo.getModel();
		int flag = 0;
		ResultSet res = null;
		try {
			db = new DBConnection();
			res = db.getOrderDatesByStatus(orderStatus);
			while (res.next()) {
				if (model.getIndexOf(res.getString(1)) == -1) {
					orderDateCombo.addItem(res.getString(1));
				}
				flag = 1;
				loaded = true;
			}
			if (flag == 0) {
				utility.showAleart("No Dates Found for the given status");
				int dateCount = orderDateCombo.getItemCount();
				for (int i = 1; i < dateCount; i++) {
					orderDateCombo.removeItemAt(i);
				}
				orderDateCombo.setSelectedIndex(0);

				int orderNoCount = orderNoCombo.getItemCount();
				for (int i = 1; i < orderNoCount; i++) {
					orderNoCombo.removeItemAt(i);
				}
				orderNoCombo.setSelectedIndex(0);
			}
		} catch (Exception e) {
			utility.showAleart("Error, while fetching Order Date due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception", e);
			}
		}
		log.info("Completed");
		return loaded;
	}

	private boolean loadOrderNo(String orderDate, String orderStatus) {
		boolean result = false;
		log.info("Started");
		DefaultComboBoxModel model = (DefaultComboBoxModel) orderNoCombo.getModel();
		int flag = 0;
		ResultSet res = null;
		try {
			db = new DBConnection();
			res = db.getOrderNosByDate(orderDate, orderStatus);
			while (res.next()) {
				if (model.getIndexOf(res.getString(1)) == -1) {
					orderNoCombo.addItem(res.getString(1));
				}
				flag = 1;
				result = true;
			}
			if (flag == 0) {
				utility.showAleart("No Orders Found for the given date and status");
				int dateCount = orderDateCombo.getItemCount();
				for (int i = 1; i < dateCount; i++) {
					orderDateCombo.removeItemAt(i);
				}
				orderDateCombo.setSelectedIndex(0);

				int orderNoCount = orderNoCombo.getItemCount();
				for (int i = 1; i < orderNoCount; i++) {
					orderNoCombo.removeItemAt(i);
				}
				orderNoCombo.setSelectedIndex(0);
			}
		} catch (Exception e) {
			utility.showAleart("Error, while fetching Order Nos due to : " + e.getMessage());
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

	private boolean validateOrdHistFields(JComboBox orderStatCombo, JComboBox orderDateCombo, JComboBox orderNoCombo) {
		log.info("Started");
		if (orderStatCombo.getSelectedIndex() == 0) {
			utility.showAleart("Invalid Order Status");
			return false;
		}
		if (orderDateCombo.getSelectedIndex() == 0) {
			utility.showAleart("Invalid Order Date");
			return false;
		}
		if (orderNoCombo.getSelectedIndex() == 0) {
			utility.showAleart("Invalid Order No");
			return false;
		}
		log.info("Completed");
		return true;
	}

	private void viewOrderDetails(String ordStatus, String ordDate, String ordNo) {
		log.info("Started");
		if (ordStatus.equals("Cancelled")) {
			new ViewCancelledOrder(Integer.parseInt(ordNo), ordDate);
		} else if (ordStatus.equals("Served")) {
			float disApplied = 0.0f;
			try {
				db = new DBConnection();
				ResultSet rs = db.getDiscApplied(Integer.parseInt(ordNo), ordDate);
				while (rs.next()) {
					disApplied = rs.getFloat(1);
				}

			} catch (Exception e) {
				utility.showAleart("Error, while fetching the discount for the Served Order :" + ordNo + ",\nDue to :"
						+ e.getMessage());
				log.error("Exception", e);
			} finally {
				try {
					db.closeConnection();
				} catch (SQLException e) {
					log.error("Exception", e);
				}
			}
			new GenerateBill(Integer.parseInt(ordNo), String.valueOf(disApplied), true, ordDate);
		}
		log.info("Completed");
	}

	private void viewCustomerTable(ResultSet rs) throws SQLException {
		log.info("Started");
		String[] cols = { "Cust Id", "First Name", "Last Name", "Gender", "Address", "DOB", "Email", "Mobile",
				"Created On" };
		int totalRows = 0;
		while (rs.next()) {
			totalRows++;
		}
		rs.beforeFirst();
		String[][] data = new String[totalRows][9];

		int i = 0;
		while (rs.next()) {
			for (int j = 1; j <= 9; j++) {
				if (j == 1)
					data[i][j - 1] = String.valueOf(rs.getInt(j));

				if (j >= 2 && j <= 7)
					data[i][j - 1] = rs.getString(j);

				if (j == 8)
					data[i][j - 1] = String.valueOf(rs.getLong(j));

				if (j == 9)
					data[i][j - 1] = String.valueOf(rs.getTimestamp(j));

			}
			i++;
		}

		DefaultTableModel model = new DefaultTableModel(data, cols) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);

		viewRefCustScrollPan.setViewportView(table);

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				Point p = me.getPoint();

				if (me.getClickCount() == 2) {
					int row = table.getSelectedRow();
					int totCol = table.getColumnCount();
					if (row != -1) {
						// "Cust Id", "First Name", "Last Name", "Gender",
						// "Address", "DOB","Email","Mobile",
						Customer customer = new Customer();
						customer.setCustId(Integer.parseInt(model.getValueAt(row, 0).toString()));
						customer.setFname(model.getValueAt(row, 1).toString());
						customer.setLname(model.getValueAt(row, 2).toString());
						customer.setGender(model.getValueAt(row, 3).toString());
						customer.setAddress(model.getValueAt(row, 4).toString());
						customer.setDob(model.getValueAt(row, 5).toString());
						customer.setEmail(model.getValueAt(row, 6).toString());
						customer.setMobile(Long.parseLong(model.getValueAt(row, 7).toString()));
						customer.setCreatedOn(model.getValueAt(row, 8).toString());
						ModifyCustomer m = new ModifyCustomer(customer);
					}
				}
			}
		});
		log.info("Completed");
	}

	public void clickviewRefCustBtn() {
		viewRefCustBtn.doClick();
	}

	public void clickviewStaffBtn() {
		viewStaffBtn.doClick();
	}

	public void clickviewItemBtn() {
		viewItemBtn.doClick();
	}

	public void clickviewMenuBtn() {
		viewMenuBtn.doClick();
	}

	public void clickviewDiscBtn() {
		viewDiscBtn.doClick();
	}

	public void clickviewTaxBtn() {
		viewTaxBtn.doClick();
	}

	public void clickviewRecEODREp() {
		viewRecEODRepBtn.doClick();
	}
	

	public void clickviewSendEODREp() {
		viewSenderEODRepBtn.doClick();
	}

	public void clickviewBillHeaderBtn() {
		viewBillHeadersBtn.doClick();
	}
	
	public void clickviewBillFooterBtn() {
		viewBillFootersBtn.doClick();
	}

	private boolean validateCustFields(String fname, String contact) {
		log.info("Started");
		if (fname.equals("")) {
			utility.showAleart("First Name cannot be blank.");
			return false;
		}
		if (!contact.matches("\\d*") || contact.equals("")) {
			utility.showAleart("Contact No must be a number. (Assign as 0 if you do not know)");
			return false;
		}
		log.info("Completed");
		return true;
	}

	private void viewInventoryItemTable(ResultSet rs) throws SQLException {
		log.info("Started");
		String[] cols = { "Item ID", "Item Name", "Quantity" };
		int totalRows = 0;
		while (rs.next()) {
			totalRows++;
		}
		rs.beforeFirst();
		String[][] data = new String[totalRows][3];

		int i = 0;
		while (rs.next()) {
			for (int j = 1; j <= 3; j++) {
				if (j == 1)
					data[i][j - 1] = String.valueOf(rs.getInt(j));

				if (j == 2)
					data[i][j - 1] = rs.getString(j);

				if (j == 3)
					data[i][j - 1] = String.valueOf(rs.getInt(j));
			}
			i++;
		}

		DefaultTableModel model = new DefaultTableModel(data, cols) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);

		itemScrollPan.setViewportView(table);

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				Point p = me.getPoint();

				if (me.getClickCount() == 2) {
					int row = table.getSelectedRow();
					int totCol = table.getColumnCount();
					if (row != -1) {
						int itemid = Integer.parseInt(model.getValueAt(row, 0).toString());
						String itemName = model.getValueAt(row, 1).toString();
						int qty = Integer.parseInt(model.getValueAt(row, 2).toString());
						ModifyInventoryItem m = new ModifyInventoryItem(itemName, qty, itemid);
					}
				}
			}
		});
		log.info("Completed");
	}

	private void viewDiscountTable(ResultSet rs) throws SQLException {
		log.info("Started");
		String[] cols = { "Discount ID", "Discount Value" };
		int totalRows = 0;
		while (rs.next()) {
			totalRows++;
		}
		rs.beforeFirst();
		String[][] data = new String[totalRows][2];

		int i = 0;
		while (rs.next()) {
			for (int j = 1; j <= 2; j++) {
				if (j == 1)
					data[i][j - 1] = String.valueOf(rs.getInt(j));

				if (j == 2)
					data[i][j - 1] = String.valueOf(rs.getFloat(j));
			}
			i++;
		}

		DefaultTableModel model = new DefaultTableModel(data, cols) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);

		discScrollPan.setViewportView(table);

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				Point p = me.getPoint();

				if (me.getClickCount() == 2) {
					int row = table.getSelectedRow();
					int totCol = table.getColumnCount();
					if (row != -1) {
						int disId = Integer.parseInt(model.getValueAt(row, 0).toString());
						float disValue = Float.parseFloat(model.getValueAt(row, 1).toString());
						ModifyDiscount m = new ModifyDiscount(disId, disValue);
					}
				}
			}
		});
		log.info("Completed");
	}

	private void viewTaxTable(ResultSet rs) throws SQLException {
		log.info("Started");
		String[] cols = { "Tax ID", "Tax Description", "Tax Short Name", "Tax Percentage" };
		int totalRows = 0;
		while (rs.next()) {
			totalRows++;
		}
		rs.beforeFirst();
		String[][] data = new String[totalRows][4];

		int i = 0;
		while (rs.next()) {
			for (int j = 1; j <= 4; j++) {
				if (j == 1)
					data[i][j - 1] = String.valueOf(rs.getInt(j));

				if (j >= 2 && j <= 3)
					data[i][j - 1] = rs.getString(j);

				if (j == 4)
					data[i][j - 1] = String.valueOf(rs.getFloat(j));
			}
			i++;
		}

		DefaultTableModel model = new DefaultTableModel(data, cols) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);

		taxScrollPan.setViewportView(table);

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				Point p = me.getPoint();
				if (me.getClickCount() == 2) {
					int row = table.getSelectedRow();
					int totCol = table.getColumnCount();
					if (row != -1) {
						int id = Integer.parseInt(model.getValueAt(row, 0).toString());
						String fullName = model.getValueAt(row, 1).toString();
						String ShortName = model.getValueAt(row, 2).toString();
						float taxPerc = Float.parseFloat(model.getValueAt(row, 3).toString());
						ModifyTax m = new ModifyTax(id, fullName, ShortName, taxPerc);
					}
				}
			}
		});
		log.info("Completed");
	}

	private void viewStoreClosedTable(ResultSet rs) throws SQLException {
		log.info("Started");
		String[] cols = { "Closing Date & Time" };
		int totalRows = 0;
		while (rs.next()) {
			totalRows++;
		}
		rs.beforeFirst();
		String[][] data = new String[totalRows][1];

		int i = 0;
		while (rs.next()) {
			for (int j = 1; j <= 1; j++) {
				String dateTime = String.valueOf(rs.getTimestamp(j));
				dateTime = dateTime.replaceAll(" ", "      ");
				data[i][j - 1] = dateTime;
			}
			i++;
		}

		DefaultTableModel model = new DefaultTableModel(data, cols) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);

		storeCloseScrollPan.setViewportView(table);
		log.info("Completed");
	}

	private void viewEODRepReceiver(ResultSet rs) throws SQLException {
		log.info("Started");
		String[] cols = { "ID", "Email Address" };
		int totalRows = 0;
		while (rs.next()) {
			totalRows++;
		}
		rs.beforeFirst();
		String[][] data = new String[totalRows][2];

		int i = 0;
		while (rs.next()) {
			for (int j = 1; j <= 2; j++) {
				if (j == 1)
					data[i][j - 1] = String.valueOf(rs.getInt(j));
				if (j == 2)
					data[i][j - 1] = rs.getString(j);
			}
			i++;
		}

		DefaultTableModel model = new DefaultTableModel(data, cols) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);

		viewRecEODRepScroll.setViewportView(table);

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				Point p = me.getPoint();
				if (me.getClickCount() == 2) {
					int row = table.getSelectedRow();
					int totCol = table.getColumnCount();
					if (row != -1) {
						int id = Integer.parseInt(model.getValueAt(row, 0).toString());
						String emailAdd = model.getValueAt(row, 1).toString();
						ModifyEODEmailReceiver m = new ModifyEODEmailReceiver(id, emailAdd);
					}
				}
			}
		});
		log.info("Completed");
	}
	
	private void viewEODSenderReceiver(ResultSet rs) throws SQLException {
		log.info("Started");
		String[] cols = { "ID", "Email Address","Password" };
		int totalRows = 0;
		while (rs.next()) {
			totalRows++;
		}
		rs.beforeFirst();
		String[][] data = new String[totalRows][3];

		int i = 0;
		while (rs.next()) {
			for (int j = 1; j <= 3; j++) {
				if (j == 1)
					data[i][j - 1] = String.valueOf(rs.getInt(j));
				if (j == 2)
					data[i][j - 1] = rs.getString(j);
				if (j == 3)
					data[i][j - 1] = rs.getString(j);
			}
			i++;
		}

		DefaultTableModel model = new DefaultTableModel(data, cols) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);

		viewSenderEODRepScroll.setViewportView(table);

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				Point p = me.getPoint();
				if (me.getClickCount() == 2) {
					int row = table.getSelectedRow();
					int totCol = table.getColumnCount();
					if (row != -1) {
						int id = Integer.parseInt(model.getValueAt(row, 0).toString());
						String emailAdd = model.getValueAt(row, 1).toString();
						String emailPass = model.getValueAt(row, 2).toString();
						ModifyEODSender m = new ModifyEODSender(id, emailAdd,emailPass);
					}
				}
			}
		});
		log.info("Completed");
	}
	
	

	private void viewBillFooterTable(ResultSet rs) throws SQLException {
		log.info("Started");
		String[] cols = { "ID", "Line 1", "Line 2", "Added on" };
		int totalRows = 0;
		while (rs.next()) {
			totalRows++;
		}
		rs.beforeFirst();
		String[][] data = new String[totalRows][4];

		int i = 0;
		while (rs.next()) {
			for (int j = 1; j <= 4; j++) {
				if (j == 1)
					data[i][j - 1] = String.valueOf(rs.getInt(j));
				if (j == 2)
					data[i][j - 1] = rs.getString(j);
				if (j == 3)
					data[i][j - 1] = rs.getString(j);
				if (j == 4)
					data[i][j - 1] = String.valueOf(rs.getTimestamp(j));
			}
			i++;
		}

		DefaultTableModel model = new DefaultTableModel(data, cols) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);

		viewBillFooterScroll.setViewportView(table);

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				if (me.getClickCount() == 2) {
					int row = table.getSelectedRow();
					int totCol = table.getColumnCount();
					if (row != -1) {
						int id = Integer.parseInt(model.getValueAt(row, 0).toString());
						String line1 = model.getValueAt(row, 1).toString();
						String line2 = model.getValueAt(row, 2).toString();
						ModifyBillFooter m = new ModifyBillFooter(id, line1, line2);
					}
				}
			}
		});
		log.info("Completed");
	}
	
	private void viewBillHeaderTable(ResultSet rs) throws SQLException {
		log.info("Started");
		String[] cols = { "ID", "Store Name", "Store Address", "Added on" };
		int totalRows = 0;
		while (rs.next()) {
			totalRows++;
		}
		rs.beforeFirst();
		String[][] data = new String[totalRows][4];

		int i = 0;
		while (rs.next()) {
			for (int j = 1; j <= 4; j++) {
				if (j == 1)
					data[i][j - 1] = String.valueOf(rs.getInt(j));
				if (j == 2)
					data[i][j - 1] = rs.getString(j);
				if (j == 3)
					data[i][j - 1] = rs.getString(j);
				if (j == 4)
					data[i][j - 1] = String.valueOf(rs.getTimestamp(j));
			}
			i++;
		}

		DefaultTableModel model = new DefaultTableModel(data, cols) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);

		viewBillHeaderScroll.setViewportView(table);

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				Point p = me.getPoint();
				if (me.getClickCount() == 2) {
					int row = table.getSelectedRow();
					int totCol = table.getColumnCount();
					if (row != -1) {
						int id = Integer.parseInt(model.getValueAt(row, 0).toString());
						String name = model.getValueAt(row, 1).toString();
						String add = model.getValueAt(row, 2).toString();
						ModifyBillHeader m = new ModifyBillHeader(id, name, add);
					}
				}
			}
		});
		log.info("Completed");
	}

	private boolean validateBillHeaders(String name, String add) {
		log.info("Started");
		if (name.equals("")) {
			utility.showAleart("Name cannot be blank.");
			return false;
		}
		if (name.length() > 50) {
			utility.showAleart("Name should not exceed 50 characters as it will get printed on receipt.");
			return false;
		}
		if (add.equals("")) {
			utility.showAleart("Address cannot be blank.");
			return false;
		}
		if (add.length() > 150) {
			utility.showAleart("Address should not exceed 150 characters as it will get printed on receipt");
			return false;
		}
		log.info("Completed");
		return true;
	}

	private void addBillHeaders(String name, String add) {
		log.info("Started");
		try {
			db = new DBConnection();
			int no = db.addNewBillheaders(name, add);
			if (no > 0) {
				utility.showAleart("New Bill Header added successfully..!");
				storeName.setText("");
				storeAdd.setText("");
				storeName.requestFocusInWindow();
			}
		} catch (Exception e) {
			utility.showAleart("Error, while adding new Bill header due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error("Exception", e);
			}
		}
		log.info("Completed");

	}
	
	private boolean validateBillFooters(String line1, String line2) {
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
	
	private void addBillFooters(String line1, String line2) {
		log.info("Started");
		try {
			db = new DBConnection();
			int no = db.addNewBillFooter(line1, line2);
			if (no > 0) {
				utility.showAleart("New Bill Footer added successfully..!");
				line1TF.setText("");
				line2TF.setText("");
				line1TF.requestFocusInWindow();
			}
		} catch (Exception e) {
			utility.showAleart("Error, while adding new Bill Footer due to : " + e.getMessage());
			log.error("Exception", e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error("Exception", e);
			}
		}
		log.info("Completed");

	}
}
