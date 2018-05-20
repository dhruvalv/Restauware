package com.dhruval.restauware.print;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.dhruval.restauware.dao.ItemData;
import com.dhruval.restauware.dao.PrintableObject;
import com.dhruval.restauware.forms.GenerateBill;
import com.dhruval.restauware.utilities.RestauwareUtility;

public class FinalReceipt {

	private BufferedWriter out;
	private String receiptTable;
	private String style = "<style>#body td { height:4mm; border:none; border-left: 1px solid} tr {font-weight:bold} #footer td {border:none;}</style>";
	private String filePath = "";
	private RestauwareUtility utility = new RestauwareUtility();
	private PrintableObject po;
	private int orderNo;
	private String storeName = "";
	private String storeAddress = "";
	private String discount = "";
	private String total = "";
	private File file;
	private String line1 = "";
	private String line2 = "";
	private static Logger log = Logger.getLogger(FinalReceipt.class.getName());

	public FinalReceipt(PrintableObject po, int orderNo) {
		log.info("Started");
		storeName = GenerateBill.restauName;
		storeAddress = GenerateBill.restauAdd;
		line1 = GenerateBill.line1;
		line2 = GenerateBill.line2;
		this.po = po;
		this.orderNo = orderNo;
		if (po.getDiscount() == null) {
			this.discount = "0.0";
		} else {
			this.discount = po.getDiscount();
		}
		this.total = po.getTotal(); // amount after discount
		try {
			createReport();
			receiptTableHeader();
			receiptTableContent();
			receiptTableComplete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("Completed");
	}

	public BufferedWriter createReport() throws IOException {
		log.info("Started");
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
		filePath = utility.getReceiptPath();
		file = new File(filePath + "Receipt_" + dateFormat.format(date) + "_OrderNo_" + orderNo + ".html");
		out = new BufferedWriter(new FileWriter(file));
		out.write("<html><head>" + style + "</head>");
		log.info("Completed");
		return out;
	}

	public void receiptTableHeader() throws IOException {
		log.info("Started");
		receiptTable = "<body><table id=header border=1 style='width:105mm; height:18mm; table-layout:fixedl; border-collapse: collapse;font-family:Courier New, Courier, monospace; font-size:80%'><tr><th colspan=4>"
				+ storeName + "</th></tr><tr><th colspan=4>" + storeAddress
				+ "</th></tr><tr><td style='border:none;'>Bill No:</td><td style='border:none;'>" + po.getBillNo()
				+ "</td><td style='border:none;'>Bill Date:</td><td style='border:none;'>" + po.getBillDateTime()
				+ "</td></th></tr></table><table id=body border=1 style='width:105mm; height:104mm; table-layout:fixedl; border-collapse: collapse;font-family:Courier New, Courier, monospace; font-size:90%'><tr><th style='width:73mm; height:4mm;'>Item</th><th style='width:8mm;'>Qty</th><th style='width:12mm;'>Price</th><th style='width:12mm;'>Amount</th></tr>";
		log.info("Completed");
	}

	public void receiptTableContent() throws IOException {
		log.info("Started");
		String line = "";
		for (int i = 0; i < po.getItem().size(); i++) {
			ItemData itemData = po.getItem().get(i);
			String item = itemData.getItem();
			int qty = itemData.getQty();
			float price = itemData.getPrice();
			float amt = itemData.getAmt();
			line = line + "<tr><td>" + item + "</td><td style='text-align:center;'>" + qty
					+ "</td><td style='text-align:center;'>" + price + "</td><td style='text-align:center;'>" + amt
					+ "</td></tr>";
		}
		for (int i = 0; i < 20 - po.getItem().size(); i++) {
			line = line + "<tr><td></td><td></td><td></td><td></td></tr>";
		}
		receiptTable = receiptTable + line;
		log.info("Completed");
	}

	public void receiptTableComplete() throws IOException {
		log.info("Started");
		ArrayList<String> list = po.getTaxes();
		int listSize = list.size();

		receiptTable = receiptTable
				+ "</table><table id=footer border=1 style='width:105mm; height:14mm; table-layout:fixedl; border-collapse: collapse;font-family:Courier New, Courier, monospace; font-size:80%'>";
		if (discount != "0.0") {
			String discountSplit[] = po.getDiscount().split("=");
			receiptTable = receiptTable
					+ "<tr><td style='text-align:right;'  >Sub Total:</td><td style='text-align:center;'>"
					+ po.getSubTotal() + "</td></tr><tr><td style='text-align:right;'>" + discountSplit[0]
					+ "</td><td style='text-align:center;'>" + discountSplit[1]
					+ "</td></tr><tr><td style='text-align:right;'>Total : </td><td style='text-align:center;'>" + total
					+ "</td></tr>";

			for (int i = 0; i < list.size(); i++) {
				String tax = list.get(i);
				String taxSplit[] = new String[2];
				taxSplit = tax.split("=");
				receiptTable = receiptTable + "<tr><td style='text-align:right;'>" + taxSplit[0]
						+ "</td><td style='text-align:center;'>" + taxSplit[1] + "</td></tr>";
			}

			receiptTable = receiptTable + "<tr><th style='text-align:right;'>Round off Total : </th><th>"
					+ po.getRoundoffTotal() + "</th></tr></table>";
		} else {
			receiptTable = receiptTable
					+ "<tr><td style='text-align:right;'  >Sub Total:</td><td style='text-align:center;'>"
					+ po.getSubTotal() + "</td></tr>";

			for (int i = 0; i < list.size(); i++) {
				String tax = list.get(i);
				String taxSplit[] = new String[2];
				taxSplit = tax.split("=");
				receiptTable = receiptTable + "<tr><td style='text-align:right;'>" + taxSplit[0]
						+ "</td><td style='text-align:center;'>" + taxSplit[1] + "</td></tr>";
			}

			receiptTable = receiptTable + "<tr><th style='text-align:right;'>Round off Total : </th><th>"
					+ po.getRoundoffTotal() + "</th></tr></table>";
		}
		if (line2.trim().equals("") || line2 == null) {
			receiptTable = receiptTable
					+ "<table id=footerlines border=1 style='width:105mm; height:5mm; table-layout:fixedl; border-collapse: collapse;font-family:Courier New, Courier, monospace; font-size:75%'>"
					+ "<tr><th>" + line1 + "</th></tr>";
		} else {
			receiptTable = receiptTable
					+ "<table id=footerlines border=1 style='width:105mm; height:5mm; table-layout:fixedl; border-collapse: collapse;font-family:Courier New, Courier, monospace; font-size:75%'>"
					+ "<tr><th>" + line1 + "<br>" + line2 + "</th></tr>";
		}

		out.write(receiptTable);
		out.write("</table></body></html>");
		out.close();
		Desktop.getDesktop().open(file);
		log.info("Completed");
	}
}
