package com.markchung.mortgagedaren;

import android.os.Bundle;

public class LoanPlan {
	private int m_amount;
	private Plan m_plan;

	public void putBundle(Bundle b){
		b.putInt("Amount",this.m_amount);
		this.m_plan.putBundle(b);
	}
	public LoanPlan(Bundle b){
		m_amount = b.getInt("Amount");
		m_plan = new Plan(b);
	}

	LoanPlan(int amount,Plan plan){
		m_plan = plan;
		m_amount = amount;
	}
	public Schedule calculate(){
		return calculate(m_amount, m_plan);
	}
	public static Schedule calculate(int amount,Plan plan){
		Schedule result;
		double [] payment = new double[plan.period*12];
		double [] interest = new double[plan.period*12];
		double [] principal = new double [plan.period*12];
		double [] rates = plan.Interests();
		if(plan.type==0){
			doAmortization(amount,payment,interest,principal,rates,plan.getGrace());
		}else{
			doLinear(amount,payment,interest,principal,rates,plan.getGrace());
		}
		result = new Schedule(principal,interest,payment);
		return result;
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
