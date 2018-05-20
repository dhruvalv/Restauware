package com.dhruval.restauware.dao;

public class Menu {
	private String menuItem;
	private int itemCode;
	private float price;
	private boolean isJain;
	private String itemGrp;
	
	public Menu(){
		
	}

	public int getItemCode() {
		return itemCode;
	}

	public void setItemCode(int itemCode) {
		this.itemCode = itemCode;
	}

	public String getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(String menuItem) {
		this.menuItem = menuItem;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public boolean isJain() {
		return isJain;
	}

	public void setJain(boolean isJain) {
		this.isJain = isJain;
	}

	public String getItemGrp() {
		return itemGrp;
	}

	public void setItemGrp(String itemGrp) {
		this.itemGrp = itemGrp;
	}
	
}
