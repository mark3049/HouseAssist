package com.markchung.HouseAssist;

import java.text.NumberFormat;

import com.google.ads.AdView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class Activity_Evaluate extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	EditText m_arce;
	EditText m_amount;
	EditText m_unit_price;
	EditText m_affiliated;
	Button m_calculate;
	RadioButton m_sel_unit_price;
	RadioButton m_sel_amount;
	private AdView adView;
	ColorStateList defColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate);
		adView = MainActivity.CreateAdRequest(this,
				(LinearLayout) findViewById(R.id.adview));

		m_arce = (EditText) findViewById(R.id.eval_edit_area);
		m_affiliated = (EditText) findViewById(R.id.eval_edit_affiliated);
		m_unit_price = (EditText) findViewById(R.id.eval_edit_unit_price);
		m_amount = (EditText) findViewById(R.id.eval_edit_amount);
		m_calculate = (Button) findViewById(R.id.eval_calcelate);
		m_sel_unit_price = (RadioButton) findViewById(R.id.eval_radiobutton_unit_price);
		m_sel_amount = (RadioButton) findViewById(R.id.eval_radiobutton_amount);

		m_calculate.setOnClickListener(this);
		m_sel_unit_price.setOnCheckedChangeListener(this);
		m_sel_amount.setOnCheckedChangeListener(this);
		defColor = m_amount.getTextColors();
		boolean isamount = false;
		if (savedInstanceState == null) {
			SharedPreferences sets = getSharedPreferences(MainActivity.TAG, 0);
			isamount = sets.getBoolean("eval_isamount", false);
			m_arce.setText(sets.getString("eval_arce", ""));
			m_affiliated.setText(sets.getString("eval_affiliated", "0"));
			m_unit_price.setText(sets.getString("eval_unit_price", ""));
			m_amount.setText(sets.getString("eval_amount", ""));
		}
		if (isamount) {
			m_sel_amount.setChecked(true);
		} else {
			m_sel_unit_price.setChecked(true);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		adView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		SharedPreferences settings = getSharedPreferences(MainActivity.TAG, 0);
		SharedPreferences.Editor edit = settings.edit();
		edit.putBoolean("eval_isamount", m_sel_amount.isChecked());
		edit.putString("eval_arce", m_arce.getText().toString());
		edit.putString("eval_affiliated", m_affiliated.getText().toString());
		edit.putString("eval_unit_price", m_unit_price.getText().toString());
		edit.putString("eval_amount", m_amount.getText().toString());
		edit.commit();
		super.onStop();
	}

	private double getValue(EditText v) throws Exception {
		try {
			return Double.parseDouble(v.getText().toString());
		} catch (Exception e) {
			v.requestFocus();
			throw e;
		}
	}

	@Override
	public void onClick(View arg0) {

		double amount, arce, unit_price, affiliate;
		NumberFormat nr = NumberFormat.getNumberInstance();
		nr.setMaximumFractionDigits(3);
		try {
			arce = getValue(this.m_arce);
			try {
				affiliate = getValue(this.m_affiliated);
			} catch (Exception e1) {
				this.m_affiliated.setText("0");
				affiliate = 0;
			}
			if (!m_sel_amount.isChecked()) {
				unit_price = getValue(this.m_unit_price);
				amount = affiliate + (unit_price * arce);
				this.m_amount.setText(nr.format(amount));
			} else {
				amount = getValue(this.m_amount);
				unit_price = (amount - affiliate) / arce;
				m_unit_price.setText(nr.format(unit_price));
			}
			Toast.makeText(this, this.getString(R.string.msgCalculated),
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(this, this.getString(R.string.msgEdit_field_isNull),
					Toast.LENGTH_SHORT).show();
			return;
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		if (!arg0.isChecked())
			return;
		if (arg0 == m_sel_unit_price) {
			m_sel_amount.setChecked(false);
			m_amount.setEnabled(false);
			m_unit_price.setEnabled(true);
			m_amount.setTextColor(getResources().getColor(R.color.result));
			m_unit_price.setTextColor(defColor);
		} else {
			m_sel_unit_price.setChecked(false);
			m_amount.setEnabled(true);
			m_unit_price.setEnabled(false);
			m_unit_price.setTextColor(getResources().getColor(R.color.result));
			m_amount.setTextColor(defColor);
		}

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.option_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.menu_item_clear){
			this.m_affiliated.setText("0");
			this.m_amount.setText("");
			this.m_unit_price.setText("");
			this.m_arce.setText("");			
		}
		return true;
	}
}
