package com.markchung.HouseAssist;

import java.text.NumberFormat;

import com.markchung.HouseAssist.R;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class InterestView {
	private EditText edit_rate;
	private EditText edit_begin;
	private EditText edit_end;
	private CheckBox checkbox;
	private View mView;

	InterestView(View root) {
		mView = root.findViewById(R.id.onoff_view);
		edit_rate = (EditText) mView.findViewById(R.id.edit_rate);
		edit_begin = (EditText) mView.findViewById(R.id.edit_begin);
		edit_end = (EditText) mView.findViewById(R.id.edit_end);
		checkbox = (CheckBox) root.findViewById(R.id.checkbox);
		if (checkbox.isChecked()) {
			mView.setVisibility(View.VISIBLE);
		} else {
			mView.setVisibility(View.GONE);
		}
	}

	public void Load(SharedPreferences settings, String tag) {
		checkbox.setChecked(settings.getBoolean(tag, false));
		setRate(settings.getFloat(tag + "_rate", -1));
		setEnd(settings.getInt(tag + "_end", -1));
	}
	public void Save(SharedPreferences.Editor edit,String tag)
	{
		edit.putBoolean(tag, checkbox.isChecked());
		edit.putFloat(tag+"_rate", ParseValueFloat(edit_rate.getText().toString()));
		edit.putInt(tag+"_end", ParseValue(edit_end.getText().toString()));		
	}
	
	void getRatePlan(InterestItem t) throws NumberFormatException {
		t.enable = isEnable();
		if (t.enable) {
			t.rate = this.getRate();
			// t.begin = this.getBegin();
			t.end = this.getEnd();
		}
	}

	void getSaveRatePlan(InterestItem t) {
		t.enable = checkbox.isChecked();
		t.rate = ParseValueDouble(edit_rate.getText().toString());
		t.end = ParseValue(edit_end.getText().toString());
	}

	void setRatePlan(InterestItem plan) {
		checkbox.setChecked(plan.enable);
		if (plan.rate > 0) {
			this.setRate(plan.rate);
		} else {
			this.edit_rate.setText("");
		}
		if (plan.end > 0) {
			this.setEnd(plan.end);
		} else {
			this.edit_end.setText("");
		}
	}

	boolean isEnable() {
		return checkbox.isChecked();
	}

	public CheckBox getCheckBox() {
		return checkbox;
	}

	EditText getBeginView() {
		return edit_begin;
	}

	void setBegin(int value) {
		edit_begin.setText(Integer.toString(value));
	}

	void setEnd(int value) {
		if(value>0){
		edit_end.setText(Integer.toString(value));
		}else{
			edit_end.setText("");
		}
	}

	EditText getEndView() {
		return edit_end;
	}

	int getEnd() throws NumberFormatException {
		try {
			return Integer.parseInt(edit_end.getText().toString());
		} catch (NumberFormatException e) {
			edit_end.requestFocus();
			throw e;
		}
	}

	private void setRate(double d) {
		if (d < 0) {
			edit_rate.setText("");
		} else {
			NumberFormat nr = NumberFormat.getNumberInstance();
			nr.setMaximumFractionDigits(3);
			// DecimalFormat nr = new DecimalFormat("0.000");
			edit_rate.setText(nr.format(d));
		}
	}

	private double getRate() throws NumberFormatException {
		try {
			return Double.parseDouble(edit_rate.getText().toString());
		} catch (NumberFormatException e) {
			edit_rate.requestFocus();
			throw e;
		}
	}

	public void UpdateVisibility() {
		if (checkbox.isChecked()) {
			mView.setVisibility(View.VISIBLE);
		} else {
			mView.setVisibility(View.GONE);
		}
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
	static float ParseValueFloat(String buf){
		if(buf.length()!=0){
			return Float.parseFloat(buf);
		}else{
			return -1;
		}
	}
}
