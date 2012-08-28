package com.markchung.HouseAssist;

public class Schedule{
	double[] payment; 
	double[] interest;
	double [] principal;
	public Schedule(double[] principal,double[] interest,double[] payment){
		this.payment = payment;
		this.interest = interest;
		this.principal = principal;		
	}
}
