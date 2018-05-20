package com.dhruval.restauware.dao;

import java.util.ArrayList;
import java.util.Map;

public class PrintableObject {
	private String billNo;
	private String billDateTime;
	private ArrayList<ItemData> item;
	private String subTotal;
	private ArrayList<String> taxes;
	private String discount;
	private String total;
	private String roundoffTotal;
	
	
	
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getBillDateTime() {
		return billDateTime;
	}
	public void setBillDateTime(String billDateTime) {
		this.billDateTime = billDateTime;
	}
	public ArrayList<ItemData> getItem() {
		return item;
	}
	public void setItem(ArrayList<ItemData> item) {
		this.item = item;
	}
	public String getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getRoundoffTotal() {
		return roundoffTotal;
	}
	public void setRoundoffTotal(String roundoffTotal) {
		this.roundoffTotal = roundoffTotal;
	}
	public ArrayList<String> getTaxes() {
		return taxes;
	}
	public void setTaxes(ArrayList<String> taxes) {
		this.taxes = taxes;
	}
	
	
}
