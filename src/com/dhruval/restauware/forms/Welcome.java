package com.dhruval.restauware.forms;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.dhruval.restauware.dao.User;
import com.dhruval.restauware.db.DBConnection;
import com.dhruval.restauware.schedular.Schedular;
import com.dhruval.restauware.utilities.LogLoading;
import com.dhruval.restauware.utilities.RestauwareUtility;

public class Welcome {

	private JFrame frmWelcomeToRestauware;
	private JTextField usernameTF;
	private JPasswordField passwordTF;
	private JRadioButton isAdminRdio;
	private JLabel message;
	private JLabel changePass;
	private RestauwareUtility utility;
	static Logger log = Logger.getLogger(Welcome.class.getName());
	private static Schedular st;
	private static Timer time;
	private DBConnection db;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Welcome window = new Welcome(0);
					window.frmWelcomeToRestauware.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Welcome(int tp) {
		initialize(tp);
	}
	
	public Welcome() {
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(int tp) {
		new LogLoading();
		log.info("Started");
		frmWelcomeToRestauware = new JFrame();
		frmWelcomeToRestauware.setSize(440, 230);
		utility = new RestauwareUtility();
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frmWelcomeToRestauware.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frmWelcomeToRestauware.getHeight()) / 2);
		frmWelcomeToRestauware.setLocation(x, y);
		frmWelcomeToRestauware.getContentPane().setFont(new Font("SansSerif", Font.PLAIN, 11));
		frmWelcomeToRestauware.getContentPane().setBackground(SystemColor.activeCaption);
		frmWelcomeToRestauware.setResizable(false);
		frmWelcomeToRestauware.setTitle("Welcome to Restauware");

		frmWelcomeToRestauware.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmWelcomeToRestauware.getContentPane().setLayout(null);

		JLabel lblWelcome = new JLabel("Welcome, login to proceed..");
		lblWelcome.setBounds(10, 11, 215, 23);
		frmWelcomeToRestauware.getContentPane().add(lblWelcome);

		JLabel lblNewLabel = new JLabel("Username :");
		lblNewLabel.setBounds(104, 45, 68, 14);
		frmWelcomeToRestauware.getContentPane().add(lblNewLabel);

		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setBounds(104, 74, 68, 14);
		frmWelcomeToRestauware.getContentPane().add(lblPassword);

		usernameTF = new JTextField();
		usernameTF.setBounds(182, 40, 126, 23);
		frmWelcomeToRestauware.getContentPane().add(usernameTF);
		usernameTF.setColumns(10);

		usernameTF.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				message.setVisible(false);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				message.setVisible(false);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				message.setVisible(false);
			}
		});

		passwordTF = new JPasswordField();
		passwordTF.setBounds(182, 70, 126, 23);

		passwordTF.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				message.setVisible(false);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				message.setVisible(false);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				message.setVisible(false);
			}
		});

		frmWelcomeToRestauware.getContentPane().add(passwordTF);

		JButton loginBtn = new JButton("Login");
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				User user = new User();
				user.setUsername(usernameTF.getText().trim());
				user.setPassword(passwordTF.getText().trim());
				user.setAdmin(isAdminRdio.isSelected());
				if (isAdminRdio.isSelected()) {
					try {
						db = new DBConnection();
						if (db.validateLogin(user)) {
							new AdminHome(user);
							frmWelcomeToRestauware.dispose();
						} else {
							message.setVisible(true);
						}
					} catch (Exception e) {
						utility.showAleart("Error, while validating admin user login due to : "+e.getMessage());
						log.error("Exception",e);
					}finally{
						try {
							db.closeConnection();
						} catch (SQLException e) {
							log.error("Exception",e);
						}
					}

				} else {
					try {
						db = new DBConnection();
						if (db.validateLogin(user)) {
							if (!isStoreClosed()) {
								new ManagerHome(user);
								frmWelcomeToRestauware.dispose();
							} else {
								utility.showAleart("Store is closed for the current date. Please contact Admin.");
							}
						} else {
							message.setVisible(true);
						}
					} catch (Exception e) {
						utility.showAleart("Error, while validating manager user login due to : "+e.getMessage());
						log.error("Exception",e);
					}finally{
						try {
							db.closeConnection();
						} catch (SQLException e) {
							log.error("Exception",e);
						}
					}
				}

			}

		});
		loginBtn.setBounds(202, 127, 89, 23);
		frmWelcomeToRestauware.getContentPane().add(loginBtn);

		isAdminRdio = new JRadioButton("Admin");
		isAdminRdio.setBackground(SystemColor.activeCaption);
		isAdminRdio.setBounds(216, 97, 109, 23);
		frmWelcomeToRestauware.getContentPane().add(isAdminRdio);

		message = new JLabel("Invalid Credential..!!");
		message.setForeground(new Color(72, 61, 139));
		message.setFont(new Font("Tahoma", Font.BOLD, 11));
		message.setBounds(179, 15, 129, 14);
		message.setVisible(false);
		frmWelcomeToRestauware.getContentPane().add(message);

		changePass = new JLabel("change password?");
		changePass.setFont(new Font("Tahoma", Font.PLAIN, 11));
		changePass.setBounds(202, 161, 123, 14);
		frmWelcomeToRestauware.getContentPane().add(changePass);

		changePass.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		changePass.addMouseListener(new MouseListener() {

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
				changePass.setForeground(Color.BLACK);
				changePass.setFont(new Font("Tahoma", Font.PLAIN, 11));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				changePass.setForeground(Color.RED);
				changePass.setFont(new Font("Tahoma", Font.BOLD, 11));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

				if (usernameTF.getText().trim().equals("")) {
					utility.showAleart("Enter your username and then click me!");
				} else {
					frmWelcomeToRestauware.dispose();
					new ChangePass(usernameTF.getText().trim());
				}

			}

		});
		frmWelcomeToRestauware.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmWelcomeToRestauware.setVisible(true);
		startTheSchedular(120000);
		log.info("Completed");
	}

	public static void startTheSchedular(int milisec) {
		log.info("Started");
		time = new Timer();
		st = new Schedular();
		time.schedule(st, 0, milisec);
		log.info("Completed");
	}

	private boolean isStoreClosed() {
		log.info("Started");
		boolean closed = false;	
		try {
			db = new DBConnection();
			ResultSet rs = db.isStoreClosed();
			while (rs.next()) {
				closed = true;
			}
		} catch (Exception e) {
			utility.showAleart("Error, while verify store closed due to  : "+e.getMessage());
			log.error("Exception",e);
		} finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception",e);
			}
		}
		log.info("Completed");
		return closed;
	}

}
