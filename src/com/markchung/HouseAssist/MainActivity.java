package com.markchung.HouseAssist;

import java.text.NumberFormat;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.markchung.HouseAssist.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private LinearLayout m_shortResult;
	private Button m_Calculate;
	private Button m_result_clean;
	private Button m_btn_detail;
	private EditText m_edit_amount;
	private Spinner m_spinner_unit;
	public static final String TAG = "HouseAssist";
	public static final String myAdID = "a1502374da40dc1";
	public static final String myTestDevice = "BA76119486D364D047D0C789B4F61E46";
	private PlanView m_plan;
	private LoanPlan m_lastPlan;
	private AdView adView;

	static public final AdView CreateAdRequest(Activity activity,
			LinearLayout view) {
		AdView adview = new AdView(activity, AdSize.BANNER, MainActivity.myAdID);
		view.addView(adview);
		AdRequest adRequest = new AdRequest();
		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
		adRequest.addTestDevice(MainActivity.myTestDevice);
		adview.loadAd(adRequest);
		return adview;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Debug.startMethodTracing(TAG);
		setContentView(R.layout.activity_main);
		adView = CreateAdRequest(this, (LinearLayout) findViewById(R.id.adview));

		m_shortResult = (LinearLayout) this.findViewById(R.id.ResultListView);
		m_shortResult.setVisibility(View.GONE);

		m_Calculate = (Button) this.findViewById(R.id.button_calculate);
		m_result_clean = (Button) this.findViewById(R.id.button_clean);
		m_btn_detail = (Button) m_shortResult
				.findViewById(R.id.resultshort_button);

		m_plan = new PlanView(this, findViewById(R.id.main_plan));

		m_edit_amount = (EditText) findViewById(R.id.edit_amount);

		m_spinner_unit = (Spinner) findViewById(R.id.spinner_unit);

		m_Calculate.setOnClickListener(this);
		m_result_clean.setOnClickListener(this);
		m_btn_detail.setOnClickListener(this);

		if (savedInstanceState == null) {
			SharedPreferences settings = getSharedPreferences(TAG, 0);
			m_edit_amount.setText(settings.getString("edit_amount", ""));
			m_spinner_unit.setSelection(settings.getInt("spinner_unit", 0));

			Plan plan = new Plan();
			plan.period = settings.getInt("edit_period", 20);
			plan.type = settings.getInt("spinner_type", 0);

			plan.grace.enable = settings.getBoolean("grace", false);
			plan.grace.rate = settings.getFloat("grace_rate", -1);
			plan.grace.end = settings.getInt("grace_end", -1);

			plan.interest1.rate = settings.getFloat("interest1_rate", -1);
			plan.interest1.end = settings.getInt("interest1_end", -1);

			plan.interest2.enable = settings.getBoolean("interest2", false);
			plan.interest2.rate = settings.getFloat("interest2_rate", -1);
			plan.interest2.end = settings.getInt("interest2_end", -1);

			plan.interest3.enable = settings.getBoolean("interest3", false);
			plan.interest3.rate = settings.getFloat("interest3_rate", -1);
			plan.interest3.end = settings.getInt("interest3_end", -1);
			plan.interest1.enable = true;
			m_plan.setPlan(plan);
		}
	}

	@Override
	protected void onDestroy() {
		adView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		Plan plan = new Plan();

		m_plan.GetSavePaln(plan);
		SharedPreferences settings = getSharedPreferences(TAG, 0);
		SharedPreferences.Editor edit = settings.edit();

		edit.putString("edit_amount", m_edit_amount.getText().toString());
		edit.putInt("spinner_unit", m_spinner_unit.getSelectedItemPosition());
		edit.putInt("edit_period", plan.period);
		edit.putInt("spinner_type", plan.type);

		edit.putBoolean("grace", plan.grace.enable);
		edit.putFloat("grace_rate", (float) plan.grace.rate);
		edit.putInt("grace_end", plan.grace.end);

		edit.putFloat("interest1_rate", (float) plan.interest1.rate);
		edit.putInt("interest1_end", plan.interest1.end);

		edit.putBoolean("interest2", plan.interest2.enable);
		edit.putFloat("interest2_rate", (float) plan.interest2.rate);
		edit.putInt("interest2_end", plan.interest2.end);

		edit.putBoolean("interest3", plan.interest3.enable);
		edit.putFloat("interest3_rate", (float) plan.interest3.rate);
		edit.putInt("interest3_end", plan.interest3.end);
		edit.commit();
		// Debug.stopMethodTracing();
		super.onStop();

	}

	private void InsertResult(Schedule result) {
		m_shortResult.setVisibility(View.VISIBLE);
		int i;
		double maxPay, minPay, interest, amount;
		maxPay = Double.MIN_VALUE;
		minPay = Double.MAX_VALUE;
		amount = 0;
		double[] t = result.payment;
		int len = t.length;
		for (i = 0; i < len; ++i) {
			maxPay = Math.max(maxPay, t[i]);
			minPay = Math.min(minPay, t[i]);
			amount += t[i];
		}
		t = result.interest;
		interest = 0;
		for (i = 0; i < len; ++i) {
			interest += t[i];
		}
		NumberFormat nr = NumberFormat.getNumberInstance();
		nr.setParseIntegerOnly(true);
		nr.setMaximumFractionDigits(0);
		TextView view = (TextView) m_shortResult
				.findViewById(R.id.resultshort_payment);
		if ((maxPay - minPay) < 1) {
			view.setText(nr.format(maxPay + 0.5));
		} else {
			view.setText(String.format("%s - %s", nr.format(maxPay + 0.5),
					nr.format(minPay + 0.5)));
		}
		view = (TextView) m_shortResult.findViewById(R.id.resultshort_amount);
		view.setText(nr.format(amount + 0.5));
		view = (TextView) m_shortResult.findViewById(R.id.resultshort_interest);
		view.setText(nr.format(interest + 0.5));
	}

	private void ClearResult() {
		m_shortResult.setVisibility(View.GONE);
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * getMenuInflater().inflate(R.menu.activity_main, menu); return true; }
	 */
	private int getAmount() {
		int unit = m_spinner_unit.getSelectedItemPosition();
		double amount = 0;
		try {
			amount = Double.parseDouble(m_edit_amount.getText().toString());
			if (amount <= 0) {
				m_edit_amount.requestFocus();
				return -1;
			}
		} catch (NumberFormatException ex) {
			m_edit_amount.requestFocus();
			return -1;
		}
		if (unit == 1) {
			amount *= 1000;
		} else if (unit == 2) {
			amount *= 10000;
		} else if (unit == 3) {
			amount *= 1000000;
		}
		return (int) (amount + 0.5);
	}

	// private boolean fill_plan(Plan plan){
	// return false;
	// }
	private class CalculateTask extends AsyncTask<LoanPlan, Void, Schedule> {

		@Override
		protected void onPostExecute(Schedule result) {
			InsertResult(result);
			myDialog.dismiss();
			myDialog = null;
			InputMethodManager imm = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
			imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			// MainActivity.this.getWindow().setSoftInputMode(
			// WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			Toast.makeText(MainActivity.this,
					MainActivity.this.getString(R.string.msgCalculated),
					Toast.LENGTH_SHORT).show();
		}

		@Override
		protected Schedule doInBackground(LoanPlan... params) {
			return params[0].calculate();
		}

	}

	private ProgressDialog myDialog = null;

	private void showError() {
		InputMethodManager imm = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
		imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		Toast.makeText(this, this.getString(R.string.msgEdit_field_isNull),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		if (v == m_Calculate) {
			if (myDialog != null)
				return;

			CharSequence title = this.getString(R.string.dialog_title_wait);
			CharSequence message = getString(R.string.dialog_body_calcelate);
			myDialog = ProgressDialog.show(this, title, message, true, true);
			ClearResult();

			int amount = getAmount();
			if (amount <= 0) {
				myDialog.dismiss();
				myDialog = null;
				// myDialog.cancel();
				showError();
				return;
			}
			Plan plan = new Plan();
			if (!m_plan.GetPlan(plan)) {
				myDialog.dismiss();
				myDialog = null;
				// myDialog.cancel();
				showError();
				return;
			}
			this.m_lastPlan = new LoanPlan(amount, plan);
			new CalculateTask().execute(m_lastPlan);

		} else if (v == m_result_clean) {
			ClearResult();
		} else if (v == this.m_btn_detail) {
			if (m_lastPlan == null)
				return;
			Intent intent = new Intent();
			intent.setClass(this, DetailActivity.class);
			Bundle b = new Bundle();
			m_lastPlan.putBundle(b);
			intent.putExtras(b);
			this.startActivity(intent);
		}
	}

}
