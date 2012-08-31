package com.markchung.library;

public class Schedule{
	private double[] m_payments; 
	private double[] m_interests;
	private double [] m_principal;
	private double maxPay, minPay;
	
	public double [] getPayment(){
		return m_payments;
	}
	public double [] getInterest() { return m_interests;}
	public double [] getPrincipal(){ return m_principal;}
	public double getPayment(int index){
		return m_payments[index];
	}
	public double getInterest(int index){
		return m_interests[index];
	}
	public double getPrincipal(int index){
		return m_principal[index];
	}
	public int getQuantity(){
		return m_payments.length;
	}
	public double getMaxPay(){
		return maxPay;
	}
	public double getMinPay(){
		return minPay;
	}
	public double getInterests(){
		int len = m_interests.length;
		double sum = 0;
		for(int i=0;i<len;++i){
			sum += m_interests[i];
		}
		return sum;	
	}
	public double getPayments(){
		int len = m_payments.length;
		double sum = 0;
		for(int i=0;i<len;++i){
			sum += m_payments[i];
		}
		return sum;	
	}
	public Schedule(double[] principal,double[] interest,double[] payment){
		this.m_payments = payment;
		this.m_interests = interest;
		this.m_principal = principal;	

		maxPay = Double.MIN_VALUE;
		minPay = Double.MAX_VALUE;
	
		for (int i = 0; i < m_payments.length; ++i) {
			maxPay = Math.max(maxPay, m_payments[i]);
			minPay = Math.min(minPay, m_payments[i]);
		}
	}
	public static Schedule toYear(Schedule org){
		int len = org.getQuantity();
		int nr = (len+11)/12;
		
		double [] payment = new double [nr];
		double [] interest = new double [nr];
		double [] principal = new double [nr];
		int i,j,index;
		for(i=0,index=0;i<nr;++i){
			payment[i] = interest[i]= principal[i]= 0;
			for(j=0;j<12&&index<len;++j,++index){
				payment[i]+=org.m_payments[index];
				interest[i]+=org.m_interests[index];
				principal[i]+=org.m_principal[index];				
			}
			payment[i]/=j;
			interest[i]/=j;
			principal[i]/=j;
		}		
		return new Schedule(principal,interest,payment);
	}
}
