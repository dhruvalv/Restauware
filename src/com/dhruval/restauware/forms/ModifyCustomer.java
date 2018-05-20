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

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.dhruval.restauware.dao.Customer;
import com.dhruval.restauware.db.DBConnection;
import com.dhruval.restauware.utilities.RestauwareUtility;

public class ModifyCustomer {

	private JFrame frame;
	private JTextField fnameTF;
	private JTextField lnameTF;
	private JTextField emailTF;
	private JTextField contactTF;
	private JTextField custIdTF;
	private RestauwareUtility utility;
	private JTextField createdOnTF;
	private DBConnection db;
	private static Logger log = Logger.getLogger(ModifyCustomer.class.getName());

	/**
	 * Launch the application.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModifyCustomer window = new ModifyCustomer(new Customer());
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ModifyCustomer() {
		// initialize();
	}

	public ModifyCustomer(Customer cust) {
		initialize(cust);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Customer cust) {
		log.info("Started");
		utility = new RestauwareUtility();
		frame = new JFrame("Modify Customer : " + cust.getFname() + " " + cust.getLname());
		frame.setResizable(false);
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setSize(399, 456);
		frame.getContentPane().setFont(new Font("SansSerif", Font.PLAIN, 11));
		frame.getContentPane().setBackground(SystemColor.activeCaption);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel label = new JLabel("First Name :");
		label.setBounds(57, 58, 73, 14);
		frame.getContentPane().add(label);

		fnameTF = new JTextField();
		fnameTF.setColumns(10);
		fnameTF.setText(cust.getFname());
		fnameTF.setBounds(131, 55, 161, 20);
		frame.getContentPane().add(fnameTF);

		JLabel label_1 = new JLabel("Last Name :");
		label_1.setBounds(57, 83, 73, 14);
		frame.getContentPane().add(label_1);

		lnameTF = new JTextField();
		lnameTF.setColumns(10);
		lnameTF.setText(cust.getLname());
		lnameTF.setBounds(131, 83, 161, 20);
		frame.getContentPane().add(lnameTF);

		JLabel label_2 = new JLabel("Gender :");
		label_2.setBounds(57, 115, 46, 14);
		frame.getContentPane().add(label_2);

		JRadioButton maleRdio = new JRadioButton("Male");
		maleRdio.setBackground(SystemColor.activeCaption);
		maleRdio.setBounds(131, 112, 65, 23);
		frame.getContentPane().add(maleRdio);

		JRadioButton femaleRdio = new JRadioButton("Female");
		femaleRdio.setBackground(SystemColor.activeCaption);
		femaleRdio.setBounds(198, 112, 109, 23);
		frame.getContentPane().add(femaleRdio);

		ButtonGroup bg = new ButtonGroup();
		bg.add(maleRdio);
		bg.add(femaleRdio);
		if (cust.getGender().equals("Male"))
			maleRdio.setSelected(true);
		else
			femaleRdio.setSelected(true);

		JLabel label_3 = new JLabel("Address :");
		label_3.setBounds(57, 148, 74, 14);
		frame.getContentPane().add(label_3);

		JLabel label_4 = new JLabel("Birth Date :");
		label_4.setBounds(57, 217, 73, 14);
		frame.getContentPane().add(label_4);

		String dob = cust.getDob();
		String dobParts[] = { "01", "Jan", "1970" };
		if (null != dob) {
			dobParts = dob.split("-");
		}

		JComboBox ddCombo = new JComboBox();
		ddCombo.setModel(new DefaultComboBoxModel(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09",
				"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26",
				"27", "28", "29", "30", "31" }));
		ddCombo.setBounds(130, 214, 41, 20);
		ddCombo.setSelectedItem(dobParts[0]);
		frame.getContentPane().add(ddCombo);

		JComboBox mmCombo = new JComboBox();
		mmCombo.setModel(new DefaultComboBoxModel(
				new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" }));
		mmCombo.setBounds(181, 214, 47, 20);
		mmCombo.setSelectedItem(dobParts[1]);
		frame.getContentPane().add(mmCombo);

		JComboBox yyCombo = new JComboBox();
		yyCombo.setModel(new DefaultComboBoxModel(new String[] { "1970", "1971", "1972", "1973", "1974", "1975", "1976",
				"1977", "1978", "1979", "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1989",
				"1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002",
				"2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015",
				"2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028",
				"2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036", "2037", "2038", "2039", "2040", "2041",
				"2042", "2043", "2044", "2045", "2046", "2047", "2048", "2049", "2050" }));
		yyCombo.setBounds(238, 214, 54, 20);
		yyCombo.setSelectedItem(dobParts[2]);
		frame.getContentPane().add(yyCombo);

		JLabel label_5 = new JLabel("Email :");
		label_5.setBounds(57, 255, 73, 14);
		frame.getContentPane().add(label_5);

		emailTF = new JTextField();
		emailTF.setColumns(10);
		emailTF.setBounds(131, 252, 161, 20);
		emailTF.setText(cust.getEmail());
		frame.getContentPane().add(emailTF);

		JLabel label_6 = new JLabel("Contact No :");
		label_6.setBounds(57, 283, 73, 14);
		frame.getContentPane().add(label_6);

		contactTF = new JTextField();
		contactTF.setColumns(10);
		contactTF.setBounds(131, 280, 161, 20);
		contactTF.setText(String.valueOf(cust.getMobile()));
		frame.getContentPane().add(contactTF);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(131, 148, 161, 52);
		frame.getContentPane().add(scrollPane_1);

		JTextArea addTA = new JTextArea();
		addTA.setText(cust.getAddress());
		scrollPane_1.setViewportView(addTA);

		JButton updateBtn = new JButton("Update");
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (validateFields(fnameTF.getText().trim(), contactTF.getText().trim())) {
					Customer updatedCust = new Customer();
					updatedCust.setAddress(addTA.getText().trim());
					String dob = ddCombo.getSelectedItem().toString() + "-" + mmCombo.getSelectedItem().toString()
							+ "--" + yyCombo.getSelectedItem().toString();
					updatedCust.setDob(dob);
					updatedCust.setEmail(emailTF.getText().trim());
					updatedCust.setFname(fnameTF.getText().trim());
					updatedCust.setLname(lnameTF.getText().trim());
					updatedCust.setGender(maleRdio.isSelected() ? "Male" : "Female");
					updatedCust.setMobile(Long.parseLong(contactTF.getText().trim()));
					updatedCust.setCustId(Integer.parseInt(custIdTF.getText().trim()));
					int sure = utility.showConfirmation("You wish to update?", "Are you sure?");
					if (sure == 0) {
						boolean updated = updateCustomer(updatedCust);
						if (updated) {
							utility.showAleart("Customer Updated Successfully...!");
							frame.dispose();
							new AdminHome().clickviewRefCustBtn();
						} else {
							utility.showAleart("Failed to update the customer..!");
						}
					}
				}
			}

		});
		updateBtn.setBounds(88, 362, 89, 23);
		frame.getContentPane().add(updateBtn);

		JButton delBtn = new JButton("Delete");
		delBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int ans = utility.showConfirmation("You wish to delete the customer?", "Are you sure?");
					if (ans == 0) {
						db = new DBConnection();
						int no = db.deleteCustomerById(cust.getCustId());
						if (no > 0) {
							utility.showAleart("Customer deleted successfully..!");
							frame.dispose();
							new AdminHome().clickviewRefCustBtn();
						} else {
							utility.showAleart("Failed to delete customer..!");
						}
					}
				} catch (Exception e) {
					utility.showAleart("Error while Deleting customer due to : " + e.getMessage());
					log.error("Exception",e);
				} finally {
					try {
						db.closeConnection();
					} catch (SQLException e) {
						log.error("Exception",e);
					}
				}
			}
		});
		delBtn.setBounds(203, 362, 89, 23);
		frame.getContentPane().add(delBtn);

		JLabel lblNewLabel = new JLabel("Customer Id :");
		lblNewLabel.setBounds(57, 33, 89, 14);
		frame.getContentPane().add(lblNewLabel);

		custIdTF = new JTextField();
		custIdTF.setEditable(false);
		custIdTF.setBounds(156, 30, 136, 20);
		custIdTF.setText(String.valueOf(cust.getCustId()));
		frame.getContentPane().add(custIdTF);
		custIdTF.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Created :");
		lblNewLabel_1.setBounds(57, 314, 73, 14);
		frame.getContentPane().add(lblNewLabel_1);

		createdOnTF = new JTextField();
		createdOnTF.setEditable(false);
		createdOnTF.setBounds(141, 311, 151, 20);
		createdOnTF.setText(cust.getCreatedOn());
		frame.getContentPane().add(createdOnTF);
		createdOnTF.setColumns(10);

		frame.setVisible(true);
		log.info("Completed");
	}

	private boolean validateFields(String fname, String contact) {
		log.info("Started");
		if (fname.equals("")) {
			utility.showAleart("First Name cannot be blank.");
			return false;
		}
		if (!contact.matches("\\d*") || contact.equals("")) {
			utility.showAleart("Contact No must be a number.");
			return false;
		}
		log.info("Completed");
		return true;
	}

	private boolean updateCustomer(Customer updatedCust) {
		log.info("Started");
		boolean result = false;
		try {
			db = new DBConnection();
			int updated = db.updateCustomer(updatedCust);
			if (updated > 0) {
				result = true;
			}
		} catch (Exception e) {
			utility.showAleart("Error while updating the customer due to : " + e.getMessage());
			log.error("Exception",e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception",e);
			}
		}
		log.info("Completed");
		return result;
	}
}
