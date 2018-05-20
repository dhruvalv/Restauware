package com.dhruval.restauware.dao;

import java.util.Date;

public class Staff {
	private int staffNo;
	private String category;
	private String refdby;
	private String fname;
	private String mname;
	private String lname;
	private String gender;
	private String address;
	private String dob;
	private long mobile;
	private long telephone;
	private String idproof;
	private String idno;
	private String doj;
	private String dol;

	public Staff() {

	}

	public Staff(String cat, String ref, String fname, String mname, String lname, String gen, String add, long mob,
			long tele, String idproof, String idno, String doj, String dol) {
		category = cat;
		refdby = ref;
		this.fname = fname;
		this.mname = mname;
		this.lname = lname;
		gender = gen;
		address = add;
		mobile = mob;
		telephone = tele;
		this.idproof = idproof;
		this.idno = idno;
		this.doj = doj;
		this.dol = dol;
	}

	
	public int getStaffNo() {
		return staffNo;
	}

	public void setStaffNo(int staffNo) {
		this.staffNo = staffNo;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getRefdby() {
		return refdby;
	}

	public void setRefdby(String refdby) {
		this.refdby = refdby;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public long getMobile() {
		return mobile;
	}

	public void setMobile(long mobile) {
		this.mobile = mobile;
	}

	public long getTelephone() {
		return telephone;
	}

	public void setTelephone(long telephone) {
		this.telephone = telephone;
	}

	public String getIdproof() {
		return idproof;
	}

	public void setIdproof(String idproof) {
		this.idproof = idproof;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getDoj() {
		return doj;
	}

	public void setDoj(String doj) {
		this.doj = doj;
	}

	public String getDol() {
		return dol;
	}

	public void setDol(String dol) {
		this.dol = dol;
	}

}
