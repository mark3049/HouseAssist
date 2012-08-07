package com.markchung.mortgagedaren;

import java.text.NumberFormat;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ConverterActivity extends Activity implements OnClickListener {

	static double nornal_rate[] = { 0.00001, // 平方公里
			0.0001, // 公頃
			0.000103, // 甲
			0.000247, // 英畝
			0.01, // 公畝
			0.3025, // 坪
			1, // 平方公尺
			10.7639, // 平方呎
			1550, // 平方吋
			10000 // 平方公分
	};
	static final double[][] rate = {
			{ 1, 100, 103, 247, 10000, 302500, 1000000, -1, -1, -1 },
			{ 0.01, 1, 1.03, 2.47, 100, 3025, 10000, 107639, 15500000,
					100000000 },
			{ 0.0097, 0.97, 1, 2.396, 96.99, 2934, 9699, 104399.07, 15033450,
					96990000 },
			{ 0.004, 0.405, 0.417, 1, 40.47, 1224.12, 4046.87, 43560, 6272640,
					-1 },
			{ 0.0001, 0.01, 0.0103, 0.02471, 1, 30.25, 100, 1076.39, 155000,
					10000 },
			{ 0.00003, 0.00033, 0.00034, 0.00082, 0.0331, 1, 3.31, 35.63,
					5130.2, 33100 },
			{ 0.00001, 0.0001, 0.000103, 0.000247, 0.01, 0.3025, 1, 10.7639,
					1550, 10000 },
			{ -1, 0.000009, 0.000009, 0.000022, 0.000928, 0.0281, 0.092899, 1,
					144, 928.993 },
			{ -1, -1, -1, -1, 0.000006, 0.00019, 0.000645, 0.006944, 1, 6.452 },
			{ -1, -1, -1, -1, 0.000001, 0.00003, 0.0001, 0.001076, 0.155, 1 } };
	private static final String TAG = "mortgagedaren";
	EditText m_result;
	Spinner m_target;
	Spinner m_source;
	EditText m_input;
	Button m_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_converter);
		m_input = (EditText) findViewById(R.id.editText1);
		m_result = (EditText) findViewById(R.id.conver_result);
		m_target = (Spinner) findViewById(R.id.spinner_target);
		m_source = (Spinner) findViewById(R.id.spinner_source);
		m_btn = (Button) findViewById(R.id.button_calculate);
		m_btn.setOnClickListener(this);
		if (savedInstanceState == null) {
			SharedPreferences settings = getSharedPreferences(TAG, 0);
			m_input.setText(settings.getString("ConverInput", ""));
			m_target.setSelection(settings.getInt("ConverTargetUnit", 5));
			m_source.setSelection(settings.getInt("ConverSourceUnit", 6));			
		}
	}
	@Override
	protected void onStop() {
		super.onStop();
		SharedPreferences settings = getSharedPreferences(TAG, 0);
		SharedPreferences.Editor edit = settings.edit();
		edit.putInt("ConverTargetUnit", m_target.getSelectedItemPosition());
		edit.putInt("ConverSourceUnit", m_source.getSelectedItemPosition());
		edit.putString("ConverInput", m_input.getText().toString());
		edit.commit();
	}
	
	@Override
	public void onClick(View v) {
		String buf = m_input.getText().toString();
		try {
			int i = m_source.getSelectedItemPosition();
			int j = m_target.getSelectedItemPosition();
			double value = Double.parseDouble(buf);
			double mm;
			if (rate[i][j] < 0) {
				mm = (value * nornal_rate[j]) / nornal_rate[i];
			} else {
				mm = value * rate[i][j];
			}
			m_result.setText(NumberFormat.getNumberInstance().format(mm));
		} catch (Exception e) {
			return;
		}
	}

}
