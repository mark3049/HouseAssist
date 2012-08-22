package com.markchung.HouseAssist;

import com.markchung.HouseAssist.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PlanView implements OnCheckedChangeListener, OnFocusChangeListener {
	private EditText m_edit_period;
	private Interest_period m_grace;
	private Interest_period m_interest1;
	private Interest_period m_interest2;
	private Interest_period m_interest3;
	private Spinner m_spinner_type;

	PlanView(Context context, View view) {
		m_edit_period = (EditText) view.findViewById(R.id.edit_period);
		m_grace = new Interest_period(
				view.findViewById(R.id.linearLayout_grace));

		m_interest1 = new Interest_period(
				view.findViewById(R.id.linearLayout_interest1));
		m_interest2 = new Interest_period(
				view.findViewById(R.id.linearLayout_interest2));
		m_interest3 = new Interest_period(
				view.findViewById(R.id.linearLayout_interest3));

		m_spinner_type = (Spinner) view.findViewById(R.id.spinner_types);
		m_interest1.getCheckBox().setChecked(true);
		m_interest1.getCheckBox().setEnabled(false);
		m_grace.getCheckBox().setText(context.getString(R.string.grace_period));

		m_grace.getCheckBox().setOnCheckedChangeListener(this);
		m_interest1.getCheckBox().setOnCheckedChangeListener(this);
		m_interest2.getCheckBox().setOnCheckedChangeListener(this);
		m_interest3.getCheckBox().setOnCheckedChangeListener(this);
		m_grace.getEndView().setOnFocusChangeListener(this);
		m_interest1.getEndView().setOnFocusChangeListener(this);
		m_interest2.getEndView().setOnFocusChangeListener(this);
		m_grace.getBeginView().setText("0");
		// m_interest3.getEndView().setOnFocusChangeListener(this);
		m_edit_period.setOnFocusChangeListener(this);
	}

	boolean GetPlan(Plan plan) {
		// ¶U´Ú´Á­­
		try {
			plan.period = Integer.parseInt(m_edit_period.getText().toString());
			if (plan.period <= 0) {
				m_edit_period.requestFocus();
				return false;
			}
		} catch (NumberFormatException e) {
			m_edit_period.requestFocus();
			return false;
		}
		// plan.period *= 12;
		plan.type = this.m_spinner_type.getSelectedItemPosition();
		try {
			m_grace.getRatePlan(plan.grace);
			m_interest1.getRatePlan(plan.interest1);
			m_interest2.getRatePlan(plan.interest2);
			m_interest3.getRatePlan(plan.interest3);
		} catch (NumberFormatException e) {
			// Log.e(TAG,e.toString());
			return false;
		}
		return true;
	}

	void GetSavePaln(Plan plan) {
		plan.period = InterestItem.ParseValue(m_edit_period.getText()
				.toString());
		plan.type = m_spinner_type.getSelectedItemPosition();
		m_grace.getSaveRatePlan(plan.grace);
		m_interest1.getSaveRatePlan(plan.interest1);
		m_interest2.getSaveRatePlan(plan.interest2);
		m_interest3.getSaveRatePlan(plan.interest3);
		// return plan;
	}

	void setPlan(Plan plan) {
		if (plan.period > 0) {
			m_edit_period.setText(Integer.toString(plan.period));
		} else {
			m_edit_period.setText("");
		}
		this.m_spinner_type.setSelection(plan.type);

		this.m_grace.setRatePlan(plan.grace);
		this.m_interest1.setRatePlan(plan.interest1);
		this.m_interest2.setRatePlan(plan.interest2);
		this.m_interest3.setRatePlan(plan.interest3);
		if (plan.period > 0) {
			updateBegin(plan.period * 12);
			parsePeriod(plan.period * 12);
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		boolean enable;
		if (view == m_interest2.getCheckBox()) {
			m_interest2.UpdateVisibility();
			enable = m_interest2.isEnable();
			if (!enable) {
				m_interest3.getCheckBox().setChecked(false);
			}
			m_interest3.getCheckBox().setEnabled(enable);
		} else if (view == m_interest3.getCheckBox()) {
			m_interest3.UpdateVisibility();
		} else if (view == m_grace.getCheckBox()) {
			m_grace.UpdateVisibility();
		}
		try {
			int period = getPeriod() * 12;
			updateBegin(period);
			// parsePeriod(period);
		} catch (NumberFormatException e) {

		}
	}

	private int getPeriod() {
		return Integer.parseInt(m_edit_period.getText().toString());
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus)
			return;

		try {
			int period = Integer.parseInt(m_edit_period.getText().toString());
			period *= 12;
			if (v == this.m_edit_period) {
				parsePeriod(period);
				return;
			}
			TextView beginView;
			EditText endView;

			if (v == this.m_grace.getEndView()) {
				endView = m_grace.getEndView();
				beginView = m_interest1.getBeginView();
			} else if (v == this.m_interest1.getEndView()) {
				endView = m_interest1.getEndView();
				beginView = m_interest2.getBeginView();
			} else if (v == this.m_interest2.getEndView()) {
				endView = m_interest2.getEndView();
				beginView = m_interest3.getBeginView();
			} else {
				return;
			}
			int end = Integer.parseInt(endView.getText().toString());
			beginView.setText(Integer.toString(Math.min(period, end + 1)));

		} catch (NumberFormatException e) {

		}
	}

	void updateBegin(int period) {
		try {
			if (m_grace.isEnable()) {
				m_grace.setBegin(0);
				m_interest1.setBegin(m_grace.getEnd());
			} else {
				m_interest1.setBegin(0);
			}
			if (m_interest2.isEnable()) {
				m_interest2
						.setBegin(Math.min(m_interest1.getEnd() + 1, period));
				if (m_interest3.isEnable()) {
					m_interest3.setBegin(Math.min(m_interest2.getEnd() + 1,
							period));
				}
			}

		} catch (NumberFormatException e) {

		}
	}

	private void parsePeriod(int period) {
		if (this.m_interest3.isEnable()) {
			m_interest3.setEnd(period);
		} else if (this.m_interest2.isEnable()) {
			m_interest2.setEnd(period);
		} else {
			m_interest1.setEnd(period);
		}

	}
}
