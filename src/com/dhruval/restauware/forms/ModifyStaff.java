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

import com.dhruval.restauware.dao.Staff;
import com.dhruval.restauware.db.DBConnection;
import com.dhruval.restauware.utilities.RestauwareUtility;

public class ModifyStaff {

	private JFrame frame;
	private JTextField fnameTF;
	private JLabel lblLastName;
	private JTextField lnameTF;
	private JLabel lblNewLabel_2;
	private JTextField mnameTF;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JScrollPane scrollPane;
	private JTextField mobileTF;
	private JTextField teleTF;
	private JTextField idNoTF;
	private JTextField dojTF;
	private JTextField dolTF;
	private JTextField staffNoTF;
	private RestauwareUtility utility;
	private JTextField refdByTF;
	private static Logger log = Logger.getLogger(ModifyStaff.class.getName());
	private DBConnection db;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Staff s = new Staff("Admin", "Dhruval", "Dhruva;", "Dhruva;", "Dhruva;", "Male", "Malad", 98202, 0,
							"PAN", "asdasd", null, null);
					String dob[] = { "8", "Jul", "1990" };
					String doj[] = { "01", "07", "1990" };
					ModifyStaff window = new ModifyStaff(s, dob, doj);
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
	public ModifyStaff() {
		// initialize();
	}

	public ModifyStaff(Staff s, String dob[], String doj[]) {
		initialize(s, dob, doj);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Staff s, String dob[], String doj[]) {
		log.info("Started");
		utility = new RestauwareUtility();
		frame = new JFrame("Modify Staff : " + s.getFname() + " " + s.getLname());
		frame.setResizable(false);
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setSize(511, 557);
		frame.getContentPane().setFont(new Font("SansSerif", Font.PLAIN, 11));
		frame.getContentPane().setBackground(SystemColor.activeCaption);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Category :");
		lblNewLabel.setBounds(47, 62, 76, 14);
		frame.getContentPane().add(lblNewLabel);

		JComboBox catCombo = new JComboBox();
		catCombo.setModel(new DefaultComboBoxModel(
				new String[] { "-- Select --", "Admin", "Chef", "Cleaner", "Manager", "Waiter" }));
		catCombo.setBounds(140, 59, 108, 20);
		catCombo.setSelectedItem(s.getCategory());
		frame.getContentPane().add(catCombo);

		JLabel lblNewLabel_1 = new JLabel("First Name :");
		lblNewLabel_1.setBounds(47, 90, 76, 14);
		frame.getContentPane().add(lblNewLabel_1);

		fnameTF = new JTextField();
		fnameTF.setBounds(140, 87, 134, 20);
		fnameTF.setText(s.getFname());
		frame.getContentPane().add(fnameTF);
		fnameTF.setColumns(10);

		lblLastName = new JLabel("Last Name :");
		lblLastName.setBounds(47, 152, 76, 14);
		frame.getContentPane().add(lblLastName);

		lnameTF = new JTextField();
		lnameTF.setBounds(140, 149, 134, 20);
		lnameTF.setText(s.getLname());
		frame.getContentPane().add(lnameTF);
		lnameTF.setColumns(10);

		lblNewLabel_2 = new JLabel("Middle Name :");
		lblNewLabel_2.setBounds(47, 123, 83, 14);
		frame.getContentPane().add(lblNewLabel_2);

		mnameTF = new JTextField();
		mnameTF.setBounds(140, 118, 134, 20);
		mnameTF.setText(s.getMname());
		frame.getContentPane().add(mnameTF);
		mnameTF.setColumns(10);

		lblNewLabel_3 = new JLabel("Gender :");
		lblNewLabel_3.setBounds(47, 177, 76, 14);
		frame.getContentPane().add(lblNewLabel_3);

		JRadioButton maleRadio = new JRadioButton("Male");
		maleRadio.setBackground(SystemColor.activeCaption);
		maleRadio.setBounds(140, 173, 66, 23);
		frame.getContentPane().add(maleRadio);

		JRadioButton femaleRadio = new JRadioButton("Female");
		femaleRadio.setBackground(SystemColor.activeCaption);
		femaleRadio.setBounds(208, 173, 76, 23);
		frame.getContentPane().add(femaleRadio);

		ButtonGroup bGp = new ButtonGroup();
		bGp.add(maleRadio);
		bGp.add(femaleRadio);

		if (s.getGender().equals("Male"))
			maleRadio.setSelected(true);
		else
			femaleRadio.setSelected(true);

		lblNewLabel_4 = new JLabel("Address :");
		lblNewLabel_4.setBounds(47, 202, 66, 14);
		frame.getContentPane().add(lblNewLabel_4);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(140, 202, 134, 52);
		frame.getContentPane().add(scrollPane);

		JTextArea addressTA = new JTextArea();
		scrollPane.setViewportView(addressTA);
		addressTA.setText(s.getAddress());

		JLabel lblNewLabel_5 = new JLabel("Date Of Birth :");
		lblNewLabel_5.setBounds(47, 272, 76, 14);
		frame.getContentPane().add(lblNewLabel_5);

		JComboBox ddCombo = new JComboBox();
		ddCombo.setModel(new DefaultComboBoxModel(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09",
				"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26",
				"27", "28", "29", "30", "31" }));
		ddCombo.setBounds(140, 269, 44, 20);
		frame.getContentPane().add(ddCombo);

		ddCombo.setSelectedItem(dob[0]);

		JComboBox mmCombo = new JComboBox();
		mmCombo.setModel(new DefaultComboBoxModel(
				new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" }));
		mmCombo.setBounds(190, 269, 58, 20);
		frame.getContentPane().add(mmCombo);

		mmCombo.setSelectedItem(dob[1]);

		JComboBox yyCombo = new JComboBox();
		yyCombo.setModel(new DefaultComboBoxModel(new String[] {"1970", "1971", "1972", "1973", "1974", "1975", "1976", "1977", "1978", "1979", "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1989", "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036", "2037", "2038", "2039", "2040", "2041", "2042", "2043", "2044", "2045", "2046", "2047", "2048", "2049", "2050"}));
		yyCombo.setBounds(258, 269, 66, 20);
		frame.getContentPane().add(yyCombo);

		yyCombo.setSelectedItem(dob[2]);

		JLabel lblNewLabel_6 = new JLabel("Mobile :");
		lblNewLabel_6.setBounds(47, 305, 66, 14);
		frame.getContentPane().add(lblNewLabel_6);

		mobileTF = new JTextField();
		mobileTF.setBounds(140, 302, 134, 20);
		mobileTF.setText(String.valueOf(s.getMobile()));
		frame.getContentPane().add(mobileTF);
		mobileTF.setColumns(10);

		JLabel lblNewLabel_7 = new JLabel("Telephone :");
		lblNewLabel_7.setBounds(47, 330, 76, 14);
		frame.getContentPane().add(lblNewLabel_7);

		teleTF = new JTextField();
		teleTF.setBounds(140, 327, 134, 20);
		teleTF.setText(String.valueOf(s.getTelephone()));
		frame.getContentPane().add(teleTF);
		teleTF.setColumns(10);

		JLabel lblNewLabel_8 = new JLabel("Id Proof :");
		lblNewLabel_8.setBounds(47, 356, 66, 14);
		frame.getContentPane().add(lblNewLabel_8);

		JComboBox idProofCombo = new JComboBox();
		idProofCombo.setModel(new DefaultComboBoxModel(
				new String[] { "-- Select --", "PAN", "Adhar", "Driving License", "Voter Card", "Passport", "Other" }));
		idProofCombo.setBounds(140, 355, 108, 20);
		idProofCombo.setSelectedItem(s.getIdproof());
		frame.getContentPane().add(idProofCombo);

		JLabel lblNewLabel_9 = new JLabel("Id No :");
		lblNewLabel_9.setBounds(47, 386, 46, 14);
		frame.getContentPane().add(lblNewLabel_9);

		idNoTF = new JTextField();
		idNoTF.setBounds(140, 383, 134, 20);
		idNoTF.setText(s.getIdno());
		frame.getContentPane().add(idNoTF);
		idNoTF.setColumns(10);

		JLabel lblNewLabel_10 = new JLabel("Date of Joining :");
		lblNewLabel_10.setBounds(47, 415, 90, 14);
		frame.getContentPane().add(lblNewLabel_10);

		dojTF = new JTextField();
		dojTF.setBounds(147, 412, 107, 20);
		dojTF.setText(doj[0] + "-" + doj[1] + "-" + doj[2]);
		frame.getContentPane().add(dojTF);
		dojTF.setColumns(10);

		JLabel lblNewLabel_11 = new JLabel("Date of Leaving :");
		lblNewLabel_11.setBounds(47, 440, 100, 14);
		frame.getContentPane().add(lblNewLabel_11);

		dolTF = new JTextField();
		dolTF.setBounds(147, 437, 107, 20);
		frame.getContentPane().add(dolTF);
		dolTF.setColumns(10);

		JButton updateBtn = new JButton("Update");
		updateBtn.addActionListener(new ActionListener() {			

			public void actionPerformed(ActionEvent arg0) {
				int input = utility.showConfirmation("Are you sure you wish to update?", "Modifying.. !!");
				if (input == 0) {
					try {
						db = new DBConnection();
						Staff staff = new Staff();
						staff.setStaffNo(Integer.parseInt(staffNoTF.getText().trim()));
						staff.setCategory(catCombo.getSelectedItem().toString());
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
						String dob = ddCombo.getSelectedItem().toString() + "-" + mmCombo.getSelectedItem().toString()
								+ "-" + yyCombo.getSelectedItem().toString();
						staff.setDob(dob);
						staff.setMobile(Long.parseLong(mobileTF.getText().trim()));
						staff.setTelephone(
								Long.parseLong(teleTF.getText().trim().equals("") ? "0" : teleTF.getText().trim()));
						staff.setIdproof(idProofCombo.getSelectedItem().toString());
						staff.setIdno(idNoTF.getText().trim());
						staff.setDoj(dojTF.getText().trim());
						staff.setDol(dolTF.getText().trim());
						db.updateStaff(staff);
						utility.showAleart("Updated Successfully..");
						frame.dispose();
						new AdminHome().clickviewStaffBtn();
					} catch (Exception e) {
						utility.showAleart("Error, while modifying staff due to : "+e.getMessage());
						log.error("Exception",e);
					} finally{
						try {
							db.closeConnection();
						} catch (SQLException e) {
							log.error("Exception",e);
						}
					}

				}
			}
		});
		updateBtn.setBounds(147, 484, 89, 23);
		frame.getContentPane().add(updateBtn);

		JButton deleteBtn = new JButton("Delete");
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int input = utility.showConfirmation("Are you sure you wish to delete?", "Deleting.. !!");
				if (input == 0) {
					try {
						DBConnection db = new DBConnection();
						db.deleteStaffByNo(Integer.parseInt(staffNoTF.getText()));
						db.closeConnection();
						utility.showAleart("Deleted Successfully..");
						frame.dispose();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
		deleteBtn.setBounds(257, 484, 89, 23);
		frame.getContentPane().add(deleteBtn);

		JLabel lblNewLabel_12 = new JLabel("Modify Staff Details");
		lblNewLabel_12.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_12.setBounds(106, 11, 255, 40);
		frame.getContentPane().add(lblNewLabel_12);

		JLabel lblStaffNo = new JLabel("Staff No :");
		lblStaffNo.setBounds(300, 62, 68, 14);
		frame.getContentPane().add(lblStaffNo);

		staffNoTF = new JTextField();
		staffNoTF.setEditable(false);
		staffNoTF.setBounds(367, 59, 86, 20);
		staffNoTF.setText(String.valueOf(s.getStaffNo()));
		frame.getContentPane().add(staffNoTF);
		staffNoTF.setColumns(10);

		JLabel lblNewLabel_13 = new JLabel("Referred By:");
		lblNewLabel_13.setBounds(298, 90, 63, 14);
		frame.getContentPane().add(lblNewLabel_13);

		refdByTF = new JTextField();
		refdByTF.setBounds(367, 87, 109, 20);
		refdByTF.setText(s.getRefdby());
		frame.getContentPane().add(refdByTF);
		refdByTF.setColumns(10);
		frame.setVisible(true);
		log.info("Completed");
	}
}
