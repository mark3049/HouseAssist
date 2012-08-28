package com.markchung.HouseAssist;

import android.os.Bundle;


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
		period = -1;
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

