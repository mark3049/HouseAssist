package com.markchung.HouseAssist;

import android.os.Bundle;

class InterestItem{
	boolean enable;
	double rate;
	int end;
	InterestItem(){
		enable = false;
		rate = -1;
		end = -1;
	}
	InterestItem(String tag,Bundle b){
		enable = b.getBoolean(tag+"_enable");
		rate = b.getDouble(tag+"_rate");
		end = b.getInt(tag+"_end");
		
	}
	void putBundle(String tag,Bundle b){
		b.putBoolean(tag+"_enable", this.enable);
		b.putDouble(tag+"_rate", rate);
		b.putInt(tag+"_end", end);		
	}
	static int ParseValue(String buf){
		if(buf.length()!=0){
			return Integer.parseInt(buf);
		}else{
			return -1;
		}
	}
	static double ParseValueDouble(String buf){
		if(buf.length()!=0){
			return Double.parseDouble(buf);
		}else{
			return -1;
		}
	}
}
class Schedule{
	double[] payment; 
	double[] interest;
	double [] principal;
	public Schedule(double[] principal,double[] interest,double[] payment){
		this.payment = payment;
		this.interest = interest;
		this.principal = principal;		
	}
}
public class Plan {
	int period;
	int type;
	InterestItem grace;
	InterestItem interest1;
	InterestItem interest2;
	InterestItem interest3;
	Plan(){
		grace = new InterestItem();
		interest1 = new InterestItem();
		interest2 = new InterestItem();
		interest3 = new InterestItem();
		period = 20;
		type = 0;
		interest1.enable = true;
	}
	public Plan(Bundle b){
		period = b.getInt("Period");
		type = b.getInt("Type");
		grace = new InterestItem("Grace",b);
		interest1 = new InterestItem("IRate1", b);
		interest2 = new InterestItem("IRate2", b);
		interest3 = new InterestItem("IRate3", b);
	}
	public void putBundle(Bundle b){
		b.putInt("Period", this.period);
		b.putInt("Type",this.type);
		this.grace.putBundle("Grace", b);
		interest1.putBundle("IRate1", b);
		interest2.putBundle("IRate2", b);
		interest3.putBundle("IRate3", b);		
	}
	
	int getGrace(){
		if(!grace.enable) return 0;
		else return grace.end;
	}
	double [] Interests(){
		int period_m = period*12; 
		double [] interests = new double[period_m];
		//
		// §Q²v
		int index = 0;
		int end;
		double rate;
		if (grace.enable) {
			end = grace.end;
			rate = grace.rate;
			for (; index < end && index < period_m; ++index) {
				interests[index] = rate;
			}
		}
		end = interest1.end;
		rate = interest1.rate;
		for (; index < end && index < period_m; ++index) {
			interests[index] = rate;
		}
		if (interest2.enable) {
			end = interest2.end;
			rate = interest2.rate;
			for (; index < end && index < period_m; ++index) {
				interests[index] = rate;
			}
			if (interest3.enable) {
				end = interest3.end;
				rate = interest3.rate;
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
}

