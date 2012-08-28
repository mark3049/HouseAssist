package com.markchung.HouseAssist;

import android.os.Bundle;

public class InterestItem{
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

