package com.dhruval.restauware.forms;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.dhruval.restauware.db.DBConnection;
import com.dhruval.restauware.utilities.RestauwareUtility;

public class ViewCancelledOrder {

	private JFrame frame;
	private JTable table;
	private ResultSet rs;
	private DefaultTableModel model;
	private int flag = 0;
	private DBConnection db;
	private static Logger log = Logger.getLogger(ViewCancelledOrder.class.getName());
	private RestauwareUtility utility;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewCancelledOrder window = new ViewCancelledOrder(1,"");
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
	public ViewCancelledOrder(int orderNo,String date) {
		initialize(orderNo,date);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(int orderNo,String date) {
		log.info("Started");
		utility = new RestauwareUtility();
		frame = new JFrame("Order " + orderNo + " Cancelled");
		frame.setBounds(100, 100, 450, 464);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Order Details");
		lblNewLabel.setBounds(10, 11, 99, 14);
		frame.getContentPane().add(lblNewLabel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 27, 412, 225);
		frame.getContentPane().add(scrollPane);
		scrollPane.setAutoscrolls(false);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		JButton okBtn = new JButton("OK");
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		okBtn.setBounds(164, 392, 89, 23);
		frame.getContentPane().add(okBtn);

		JLabel lblNewLabel_1 = new JLabel("Cancellation Reason ");
		lblNewLabel_1.setBounds(10, 269, 119, 14);
		frame.getContentPane().add(lblNewLabel_1);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(146, 263, 278, 75);
		scrollPane_1.setAutoscrolls(false);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		frame.getContentPane().add(scrollPane_1);

		JTextArea cancellationReasonTA = new JTextArea();
		scrollPane_1.setViewportView(cancellationReasonTA);

		try {
			db = new DBConnection();
			rs = db.getCancelledOrderDetails(orderNo,date);
			if (rs.next()) {
				flag = 1;
				populateTable(rs);
			}
			rs = null;
			rs = db.getCancellationReasonByOrderno(orderNo,date);
			while (rs.next()) {
				cancellationReasonTA.setText(rs.getString(1));
			}
		} catch (Exception e) {
			utility.showAleart("Error, while fetching cancelled order details due to : "+e.getMessage());
			log.error("Exception",e);
		}finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				log.error("Exception",e);
			}
		}

		if (flag == 1) {
			table = new JTable(model);
			table.getTableHeader().setReorderingAllowed(false);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setFillsViewportHeight(true);
			table.setShowGrid(true);

			scrollPane.setViewportView(table);
		}
		frame.setVisible(true);
		log.info("Completed");

	}

	private void populateTable(ResultSet rs) throws Exception {
		log.info("Started");
		String[] cols = { "Item", "Quantity", "Price", "Amount" };
		int totalRows = 0;
		rs.beforeFirst();
		while (rs.next()) {
			totalRows++;
		}
		rs.beforeFirst();
		String[][] data = new String[totalRows][4];
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
		log.info("Completed");
	}
}
