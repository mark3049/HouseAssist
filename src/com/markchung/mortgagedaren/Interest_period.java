package com.markchung.mortgagedaren;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class Interest_period {
	private EditText edit_rate;
	private EditText edit_begin;
	private EditText edit_end;
	private CheckBox checkbox;
	private View mView;

	Interest_period(View root) {
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

	void getRatePlan(InterestItem t) throws NumberFormatException{
		t.enable = isEnable();
		if (t.enable) {
			t.rate = this.getRate();
			//t.begin = this.getBegin();
			t.end = this.getEnd();
		}
	}
	void getSaveRatePlan(InterestItem t){
		t.enable = checkbox.isChecked();
		t.rate = InterestItem.ParseValueDouble(edit_rate.getText().toString());
		t.end = InterestItem.ParseValue(edit_end.getText().toString());		
	}
	void setRatePlan(InterestItem plan){
		checkbox.setChecked(plan.enable);
		if(plan.rate>0) this.setRate(plan.rate);
		if(plan.rate>0)this.setEnd(plan.end);
	}

	boolean isEnable() {
		return checkbox.isChecked();
	}
	public CheckBox getCheckBox(){
		return checkbox;
	}
	private void setChecked(boolean checked) {
		checkbox.setChecked(checked);
	}

	private void setBegin(int value) {
		edit_begin.setText(Integer.toString(value));
	}
	
	EditText getBeginView(){
		return edit_begin;
	}

	private void setBegin(String value) {
		edit_begin.setText("-");
	}

	void setEnd(int value) {
		edit_end.setText(Integer.toString(value));
	}

	private void setEnd(String value) {
		edit_end.setText(value);
	}

	EditText getEndView() {
		return edit_end;
	}

	private String getEndString() {
		return edit_end.getText().toString();
	}

	private int getEnd() throws NumberFormatException {
		try {
			return Integer.parseInt(edit_end.getText().toString());
		} catch (NumberFormatException e) {
			edit_end.requestFocus();
			throw e;
		}
	}

	private void setRate(double d) {
		edit_rate.setText(Double.toString(d));
	}

	private void setRate(String value) {
		edit_rate.setText(value);
	}

	private double getRate() throws NumberFormatException {
		try {
			return Double.parseDouble(edit_rate.getText().toString());
		} catch (NumberFormatException e) {
			edit_rate.requestFocus();
			throw e;
		}
	}

	private String getRateString() {
		return edit_rate.getText().toString();
	}

	private void setPerset(double rate, int begin, int end) {
		edit_rate.setText(Double.toString(rate));
		edit_begin.setText(begin);
		edit_end.setText(end);
	}

	public void UpdateVisibility() {
		if (checkbox.isChecked()) {
			mView.setVisibility(View.VISIBLE);
		} else {
			mView.setVisibility(View.GONE);
		}
	}
}
