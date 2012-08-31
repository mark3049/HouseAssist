package com.markchung.library;

import java.text.NumberFormat;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ResultListAdapter extends BaseAdapter {

	private LayoutInflater minflater;
	private NumberFormat m_nr;
	private double[] m_sum;
	private Schedule result;

	public ResultListAdapter(Context context, Schedule result, boolean isYear) {
		minflater = LayoutInflater.from(context);
		calculate_total(result);
		if (!isYear) {
			this.result = result;
		}else{
			this.result = Schedule.toYear(result);
		}
		m_nr = NumberFormat.getNumberInstance();
		m_nr.setParseIntegerOnly(true);
		m_nr.setMaximumFractionDigits(0);

	}

	private void calculate_total(Schedule result) {
		int len = result.getQuantity();
		m_sum = new double[] { 0, 0, 0 };
		for (int i = 0; i < len; ++i) {
			m_sum[0] += result.getPrincipal(i);
			m_sum[1] += result.getInterest(i);
			m_sum[2] += result.getPayment(i);
		}
	}

	@Override
	public int getCount() {
		if (result == null)
			return 1;

		return result.getQuantity() + 1;
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
		if (position >= result.getQuantity()) {
			holder.index.setText("");
			holder.principal.setText(m_nr.format(m_sum[0] + 0.5));
			holder.interest.setText(m_nr.format(m_sum[1] + 0.5));
			holder.payment.setText(m_nr.format(m_sum[2] + 0.5));
		} else {
			int i = position;
			holder.index.setText(Integer.toString(position+1));
			holder.principal.setText(m_nr.format(result.getPrincipal(i) + 0.5));
			holder.interest.setText(m_nr.format(result.getInterest(i) + 0.5));
			holder.payment.setText(m_nr.format(result.getPayment(i) + 0.5));
		}
		if((position&0x01) == 0){
			convertView.setBackgroundResource(R.color.odd);
		}else{
			convertView.setBackgroundResource(R.color.even);
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
