package com.markchung.library;

import android.os.Bundle;

public class InterestItem {
	private boolean enable;
	private double rate;
	private int end;

	public InterestItem(boolean flag) {
		enable = flag;
		rate = -1;
		end = -1;
	}

	public InterestItem() {
		enable = false;
		rate = -1;
		end = -1;
	}

	public InterestItem(String tag, Bundle b) {
		enable = b.getBoolean(tag + "_enable");
		rate = b.getDouble(tag + "_rate");
		end = b.getInt(tag + "_end");
	}

	public InterestItem(String tag, Bundle b, boolean flag) {
		if (!flag) {
			enable = b.getBoolean(tag + "_enable");
		} else {
			enable = true;
		}
		rate = b.getDouble(tag + "_rate");
		end = b.getInt(tag + "_end");
	}

	public void putBundle(String tag, Bundle b) {
		b.putBoolean(tag + "_enable", this.enable);
		b.putDouble(tag + "_rate", rate);
		b.putInt(tag + "_end", end);
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
}
