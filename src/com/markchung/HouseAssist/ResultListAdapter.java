package com.markchung.HouseAssist;

import java.text.NumberFormat;

import com.markchung.HouseAssist.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ResultListAdapter extends BaseAdapter {

	private LayoutInflater minflater;
	private double[] m_payment;
	private double[] m_interest;
	private double[] m_principal;
	private NumberFormat m_nr;
	private double[] m_sum;

	public ResultListAdapter(Context context, Schedule result, boolean isYear) {
		minflater = LayoutInflater.from(context);
		calculate_total(result);
		if (!isYear) {
			m_payment = result.payment;
			m_interest = result.interest;
			m_principal = result.principal;
		}else{
			calculate_Year(result);
		}
		m_nr = NumberFormat.getNumberInstance();
		m_nr.setParseIntegerOnly(true);
		m_nr.setMaximumFractionDigits(0);

	}
	private void calculate_Year(Schedule result) {
		int len = result.payment.length;
		int nr = (len+11)/12;
		m_payment = new double [nr];
		m_interest = new double [nr];
		m_principal = new double [nr];
		int i,j,index;
		for(i=0,index=0;i<nr;++i){
			m_payment[i] = m_interest[i]=m_principal[i]= 0;
			for(j=0;j<12&&index<len;++j,++index){
				m_payment[i]+=result.payment[index];
				m_interest[i]+=result.interest[index];
				m_principal[i]+=result.principal[index];				
			}
			m_payment[i]/=j;
			m_interest[i]/=j;
			m_principal[i]/=j;
		}
	}
	
	private void calculate_total(Schedule result) {
		int len = result.payment.length;
		m_sum = new double[] { 0, 0, 0 };
		for (int i = 0; i < len; ++i) {
			m_sum[0] += result.principal[i];
			m_sum[1] += result.interest[i];
			m_sum[2] += result.payment[i];
		}
	}

	@Override
	public int getCount() {
		if (m_payment == null)
			return 1;

		return m_payment.length + 1;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = this.minflater.inflate(R.layout.resultlistitem, null);
			holder = new ViewHolder();
			holder.index = (TextView) convertView.findViewById(R.id.item_index);
			holder.principal = (TextView) convertView
					.findViewById(R.id.item_principal);
			holder.interest = (TextView) convertView
					.findViewById(R.id.item_interest);
			holder.payment = (TextView) convertView
					.findViewById(R.id.item_payment);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position >= m_payment.length) {
			holder.index.setText("");
			holder.principal.setText(m_nr.format(m_sum[0] + 0.5));
			holder.interest.setText(m_nr.format(m_sum[1] + 0.5));
			holder.payment.setText(m_nr.format(m_sum[2] + 0.5));
		} else {
			int i = position;
			holder.index.setText(Integer.toString(position+1));
			holder.principal.setText(m_nr.format(m_principal[i] + 0.5));
			holder.interest.setText(m_nr.format(m_interest[i] + 0.5));
			holder.payment.setText(m_nr.format(m_payment[i] + 0.5));
		}

		return convertView;
	}

	static class ViewHolder {
		TextView index;
		TextView principal;
		TextView interest;
		TextView payment;
	}

}
