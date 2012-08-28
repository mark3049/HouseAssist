package com.markchung.HouseAssist;

import android.content.SharedPreferences;
import android.os.Bundle;


public class InterestPlan {
	private int period;
	private int type;
	InterestItem grace;
	InterestItem interest1;
	InterestItem interest2;
	InterestItem interest3;
	InterestPlan(){
		grace = new InterestItem();
		interest1 = new InterestItem();
		interest2 = new InterestItem();
		interest3 = new InterestItem();
		period = -1;
		type = 0;
		interest1.enable = true;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public InterestPlan(Bundle b){
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
	
	public int getGrace(){
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
	public void save(SharedPreferences.Editor edit){
		edit.putInt("edit_period", getPeriod());
		edit.putInt("spinner_type", getType());

		edit.putBoolean("grace", grace.enable);
		edit.putFloat("grace_rate", (float) grace.rate);
		edit.putInt("grace_end", grace.end);

		edit.putFloat("interest1_rate", (float) interest1.rate);
		edit.putInt("interest1_end", interest1.end);

		edit.putBoolean("interest2", interest2.enable);
		edit.putFloat("interest2_rate", (float) interest2.rate);
		edit.putInt("interest2_end", interest2.end);

		edit.putBoolean("interest3", interest3.enable);
		edit.putFloat("interest3_rate", (float) interest3.rate);
		edit.putInt("interest3_end", interest3.end);		
	}
	public void load(SharedPreferences settings){
		setPeriod(settings.getInt("edit_period", 20));
		setType(settings.getInt("spinner_type", 0));

		grace.enable = settings.getBoolean("grace", false);
		grace.rate = settings.getFloat("grace_rate", -1);
		grace.end = settings.getInt("grace_end", -1);

		interest1.rate = settings.getFloat("interest1_rate", -1);
		interest1.end = settings.getInt("interest1_end", -1);

		interest2.enable = settings.getBoolean("interest2", false);
		interest2.rate = settings.getFloat("interest2_rate", -1);
		interest2.end = settings.getInt("interest2_end", -1);

		interest3.enable = settings.getBoolean("interest3", false);
		interest3.rate = settings.getFloat("interest3_rate", -1);
		interest3.end = settings.getInt("interest3_end", -1);
		interest1.enable = true;

	}
}

