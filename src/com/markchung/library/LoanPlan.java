package com.markchung.library;


import android.os.Bundle;

public class LoanPlan {
	int m_amount;
	int period;
	int loan_type;
	InterestItem grace;
	InterestItem interest1;
	InterestItem interest2;
	InterestItem interest3;
	private Schedule m_lastResult;
	
	public void putBundle(Bundle b){
		b.putInt("Amount",this.m_amount);
		b.putInt("Period", this.period);
		b.putInt("Type",this.loan_type);
		this.grace.putBundle("Grace", b);
		interest1.putBundle("IRate1", b);
		interest2.putBundle("IRate2", b);
		interest3.putBundle("IRate3", b);		

	}
	public LoanPlan(Bundle b){
		m_amount = b.getInt("Amount");
		period = b.getInt("Period");
		loan_type = b.getInt("Type");
		grace = new InterestItem("Grace",b);
		interest1 = new InterestItem("IRate1", b,true);
		interest2 = new InterestItem("IRate2", b);
		interest3 = new InterestItem("IRate3", b);
	}
	public LoanPlan(){
		grace = new InterestItem();
		interest1 = new InterestItem(true);
		interest2 = new InterestItem();
		interest3 = new InterestItem();
		m_amount = -1;
		period = -1;
		loan_type = 0;
		//interest1.setEnable(true);		
	}
	Schedule getSchedule(){
		if(m_lastResult==null){
			return calculate();
		}
		return m_lastResult;
	}
	public Schedule calculate(){
		double [] payment = new double[period*12];
		double [] interest = new double[period*12];
		double [] principal = new double [period*12];
		double [] rates = Interests();
		if(loan_type==0){
			doAmortization(m_amount,payment,interest,principal,rates,getGrace());
		}else{
			doLinear(m_amount,payment,interest,principal,rates,getGrace());
		}
		m_lastResult = new Schedule(principal,interest,payment);
		return m_lastResult;
	}
	private int getGrace(){
		if(!grace.isEnable()) return 0;
		else return grace.getEnd();
	}
	private double [] Interests(){
		int period_m = period*12; 
		double [] interests = new double[period_m];
		//
		// 利率
		int index = 0;
		int end;
		double rate;
		if (grace.isEnable()) {
			end = grace.getEnd();
			rate = grace.getRate();
			for (; index < end && index < period_m; ++index) {
				interests[index] = rate;
			}
		}
		end = interest1.getEnd();
		rate = interest1.getRate();
		for (; index < end && index < period_m; ++index) {
			interests[index] = rate;
		}
		if (interest2.isEnable()) {
			end = interest2.getEnd();
			rate = interest2.getRate();
			for (; index < end && index < period_m; ++index) {
				interests[index] = rate;
			}
			if (interest3.isEnable()) {
				end = interest3.getEnd();
				rate = interest3.getRate();
				for (; index < period_m; ++index) {
					interests[index] = rate;
				}
			}
		}
		for (; index < period_m; ++index) {
			interests[index] = rate;
		}		
		return interests;
	}	
	/**
	 * PMT 公式
	 * @param rate 年利率
	 * @param nper 期數
	 * @param pv 總額
	 * @return 每月攤付本息
	 */
	private static double PMT(double rate,int nper,double pv)
	{
		double mr = rate/12.0;
		double t = Math.pow(1+mr, nper);
		double r = (t*mr)/(t-1);
		return pv*r;
	}
	private static void doAmortization(double amount, double[] payment,
			double[] interest, double[] principal, double[] rates, int grace_end) {
		int i = 0;
		int len = payment.length;
		double r;
		for (i = 0; i < grace_end && i<len; ++i) {
			r = rates[i] / 100.0;
			interest[i] = amount * (r / 12.0);
			payment[i] = interest[i];
			principal[i] = 0;
		}
		for (; i < len; ++i) {
			r = rates[i] / 100.0;
			payment[i] = PMT(r, len - i, amount);
			interest[i] = amount * (r / 12.0);
			principal[i] = payment[i] - interest[i];
			amount -= principal[i];
		}
		//if (Math.abs(amount) > 0.5)
		//	Log.e(TAG, String.format("doAmortization amount=%d", amount));
	}

	private static void doLinear(double amount, double[] payment, double[] interest,
			double[] principal, double[] rates, int grace_end) {
		int i = 0;
		int len = payment.length;
		double r;
		for (i = 0; i < grace_end && i<len; ++i) {
			r = rates[i] / 100.0;
			interest[i] = (int) ((amount * (r / 12.0)) + 0.5);
			payment[i] = interest[i];
			principal[i] = 0;
		}
		double pay = amount / (len - grace_end);
		for (; i < len; ++i) {
			r = rates[i] / 100.0;
			interest[i] = (int) ((amount * (r / 12.0)) + 0.5);
			principal[i] = pay;
			payment[i] = principal[i] + interest[i];
			amount -= principal[i];
		}
	}

}
