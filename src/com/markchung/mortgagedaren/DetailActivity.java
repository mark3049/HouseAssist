package com.markchung.mortgagedaren;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class DetailActivity extends Activity implements OnItemSelectedListener  {
	private LoanPlan plan;
	private ProgressDialog myDialog;
	private ListView m_view;
	private View m_title;
	private Button m_back;
	private Spinner m_type;
	int m_lastType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		m_title = (View) this.findViewById(R.id.detail_title);
		fill_title(m_title);
		m_view = (ListView) this.findViewById(R.id.activity_detail_list);
		m_type = (Spinner) findViewById(R.id.detail_type);
		m_lastType = m_type.getSelectedItemPosition();
		Bundle extras = this.getIntent().getExtras();
		if(extras!=null){
			plan = new LoanPlan(extras);
			doUpdateList();
		}
		m_back = (Button) findViewById(R.id.button_back);
		
		m_back.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();				
			}
			
		});
		m_type.setOnItemSelectedListener(this);
		//ResultListAdapter adapter = new ResultListAdapter(this,null);
		//view.setAdapter(adapter);
	}
	private void doUpdateList(){
		CharSequence title = this.getString(R.string.dialog_title_wait);
		CharSequence message = getString(R.string.dialog_body_calcelate);
		myDialog = ProgressDialog.show(this, title, message, true, true);
		new CalculateTask().execute(plan);
	}
	private void fill_title(View view){
		TextView t = (TextView) view.findViewById(R.id.item_index);
		t.setText(getString(R.string.result_nr));
		t = (TextView) view.findViewById(R.id.item_principal);
		t.setText(getString(R.string.result_principal));
		t = (TextView) view.findViewById(R.id.item_interest);
		t.setText(getString(R.string.result_interest));
		t = (TextView) view.findViewById(R.id.item_payment);
		t.setText(getString(R.string.result_payment));

	}
	private class CalculateTask extends AsyncTask<LoanPlan, Void, Schedule> {

		@Override
		protected void onPostExecute(Schedule result) {
			boolean isYear = m_type.getSelectedItemPosition()==0;
			ResultListAdapter adapter = new ResultListAdapter(DetailActivity.this,result,isYear);
			m_view.setAdapter(adapter);
			myDialog.dismiss();
			myDialog = null;
		}

		@Override
		protected Schedule doInBackground(LoanPlan... params) {
			return params[0].calculate();
		}

	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if(m_lastType != m_type.getSelectedItemPosition()){
			m_lastType = m_type.getSelectedItemPosition();
			doUpdateList();
		}
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
