package com.dhruval.restauware.forms;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import com.dhruval.restauware.db.DBConnection;
import com.dhruval.restauware.utilities.RestauwareUtility;

public class CancelOrder {

	private JFrame frame;
	private RestauwareUtility utility;
	private DBConnection db;
	private static Logger log = Logger.getLogger(CancelOrder.class.getName());

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CancelOrder window = new CancelOrder(1);
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
	public CancelOrder(int orderNo) {
		initialize(orderNo);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(int orderNo) {
		log.info("Started");
		frame = new JFrame("Order No : "+orderNo+" Cancellation");
		frame.setSize(370, 272);
		frame.setResizable(false);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Reason :");
		lblNewLabel.setBounds(21, 29, 65, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Customer dispute", "Customer left without Informing", "Cancelled after 5mins", "Others"}));
		comboBox.setBounds(110, 26, 226, 20);
		frame.getContentPane().add(comboBox);
		
		JLabel lblExplaination = new JLabel("Explaination :");
		lblExplaination.setBounds(21, 67, 84, 14);
		frame.getContentPane().add(lblExplaination);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(110, 71, 226, 118);
		frame.getContentPane().add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		JButton btnCancel = new JButton("Cancel Order");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				log.info("Cancel Clicked");
				try{
					String reason = comboBox.getSelectedItem().toString() + "-> "+textArea.getText().trim();
					db = new DBConnection();
					int result = db.cancelOrderByNo(3, orderNo, reason);
					if(result > 0){
						new RestauwareUtility().showAleart("Order No :"+orderNo +" has been cancelled successfully..!");
						frame.dispose();
						new ManagerHome().clickrefreshAllBtn();
					}
				}catch(Exception e1){
					utility.showAleart("Error, while cancelling order due to : "+e1.getMessage());
					log.error("Exception",e1);
				}finally {
					try {
						db.closeConnection();
					} catch (SQLException e1) {
						log.error("Exception",e1);
					}
				}
			}
		});
		btnCancel.setBounds(109, 200, 134, 23);
		frame.getContentPane().add(btnCancel);
		frame.setVisible(true);
		log.info("Completed");
	}
}
