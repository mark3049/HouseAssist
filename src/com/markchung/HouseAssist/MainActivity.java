package com.markchung.HouseAssist;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.markchung.HouseAssist.R;
import com.markchung.HouseAssist.Plan.Schedule;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private LinearLayout m_shortResult;
	private Button m_Calculate;
	private Button m_result_clean;
	private Button m_btn_detail;
	// private EditText m_edit_amount;
	// private Spinner m_spinner_unit;
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

		m_plan = new PlanView(this, findViewById(R.id.loanPlanView));

		m_Calculate.setOnClickListener(this);
		m_result_clean.setOnClickListener(this);
		m_btn_detail.setOnClickListener(this);

		if (savedInstanceState == null) {
			SharedPreferences settings = getSharedPreferences(TAG, 0);
			m_plan.Load(settings);
		}
	}

	@Override
	protected void onDestroy() {
		adView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		SharedPreferences settings = getSharedPreferences(TAG, 0);
		SharedPreferences.Editor edit = settings.edit();
		m_plan.Save(edit);
		edit.commit();
		// Debug.stopMethodTracing();
		super.onStop();

	}

	private void InsertResult(Schedule result) {
		m_shortResult.setVisibility(View.VISIBLE);
		NumberFormat nr = NumberFormat.getNumberInstance();
		nr.setParseIntegerOnly(true);
		nr.setMaximumFractionDigits(0);
		TextView view = (TextView) m_shortResult
				.findViewById(R.id.resultshort_payment);
		if ((result.getMaxPay() - result.getMinPay()) < 1) {
			view.setText(nr.format(result.getMaxPay() + 0.5));
		} else {
			view.setText(String.format("%s - %s",
					nr.format(result.getMaxPay() + 0.5),
					nr.format(result.getMinPay() + 0.5)));
		}
		view = (TextView) m_shortResult.findViewById(R.id.resultshort_amount);
		view.setText(nr.format(result.getPayments() + 0.5));
		view = (TextView) m_shortResult.findViewById(R.id.resultshort_interest);
		view.setText(nr.format(result.getInterests() + 0.5));
	}

	private void ClearResult() {
		m_lastPlan = null;
		m_shortResult.setVisibility(View.GONE);
	}

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

			LoanPlan plan = new LoanPlan();
			if (!m_plan.GetPlan(plan)) {
				myDialog.dismiss();
				myDialog = null;
				// myDialog.cancel();
				showError();
				return;
			}
			this.m_lastPlan = plan;
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.option_main, menu);
		return true;
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		boolean flag = this.m_lastPlan != null;

		menu.findItem(R.id.menu_item_sendmail).setEnabled(flag);
		menu.findItem(R.id.menu_item_sendexcel).setEnabled(flag);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.menu_item_clear:
			ClearResult();
			m_plan.CleanForm();
			break;
		case R.id.menu_item_sendmail:
			sendToMail();
			break;
		case R.id.menu_item_sendexcel:
			sendToExcel();
			break;
		}
		return true;
	}

	private void sendToMail() {
		Resources resources = this.getResources();
		final Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("text/text");
		emailIntent.putExtra(
				Intent.EXTRA_SUBJECT,
				resources.getString(R.string.app_name) + " - "
						+ resources.getString(R.string.export));
		StringBuilder sb = new StringBuilder();
		new ScheduleExport(resources, this.m_lastPlan).append(sb);
		emailIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
		startActivity(Intent.createChooser(emailIntent,
				resources.getString(R.string.sendEmail)));
	}

	private void sendToExcel() {
		final Intent emailIntent = new Intent(Intent.ACTION_SEND);
		Resources resources = this.getResources();
		try {
			File file = createTmpFile();
			new ScheduleExport(this.getResources(), this.m_lastPlan).save(file);
			emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
			emailIntent.setType("text/csv");
			emailIntent.putExtra(
					Intent.EXTRA_SUBJECT,
					resources.getString(R.string.app_name) + " - "
							+ resources.getString(R.string.export));
			
			StringBuilder sb = new StringBuilder();
			new ScheduleExport(resources, this.m_lastPlan).append_info(sb);
			emailIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
			
			startActivity(Intent.createChooser(emailIntent, this.getResources()
					.getString(R.string.sendExcel)));

		} catch (IOException e) {
			Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT)
					.show();
		}
	}

	private File createTmpFile() throws IOException {
		String fileName = "HosingAssist.csv";// csvScheduleCreator.getFileName();
//		File externalStorageDirectory = Environment
//				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//		File file = new File(externalStorageDirectory, fileName);
		File file =  this.getFileStreamPath(fileName);
		Log.d(TAG, file.toString());
		file.deleteOnExit();
		return file;
	}
}
