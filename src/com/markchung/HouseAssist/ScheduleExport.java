package com.markchung.HouseAssist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;

import com.markchung.HouseAssist.Plan.Schedule;

import android.content.res.Resources;

public class ScheduleExport {
	private Resources resources;
	private LoanPlan loan;
	private NumberFormat nr;

	public ScheduleExport(Resources res, LoanPlan plan) {
		this.resources = res;
		this.loan = plan;
		nr = NumberFormat.getNumberInstance();
		nr.setParseIntegerOnly(true);
		nr.setMaximumFractionDigits(0);
	}

	private char ln = '\n';
	private char tab = '\t';
	private char comma = ',';

	private String encode(int id) {
		return resources.getString(id);
	}

	private String value(double v) {
		return nr.format(v + 0.5);
	}

	private void appendHead(StringBuilder sb, String title, String value) {
		sb.append(title).append(tab).append(value).append(ln);
	}

	private void appendHead(StringBuilder sb, int id, double v) {
		sb.append(encode(id)).append(tab).append(value(v)).append(ln);
	}

	private void appendInterest(StringBuilder sb) {
		nr = NumberFormat.getNumberInstance();
		nr.setParseIntegerOnly(true);
		nr.setMaximumFractionDigits(0);
		int end = loan.period * 12;
		int begin = 0;
		if (loan.grace.isEnable()) {
			sb.append(encode(R.string.grace_period)).append(tab);
			sb.append(nr.format(loan.grace.getRate())).append('%').append(tab);
			sb.append(Integer.toString(begin)).append('~');
			sb.append(Integer.toString(loan.grace.getEnd())).append(ln);
			begin = loan.grace.getEnd() + 1;
		}
		sb.append(encode(R.string.interest_period)).append(tab);
		sb.append(nr.format(loan.interest1.getRate())).append('%').append(tab);
		sb.append(Integer.toString(begin)).append('~');
		if (!loan.interest2.isEnable()) {
			sb.append(Integer.toString(end)).append(ln);
		} else {
			sb.append(Integer.toString(loan.interest1.getEnd())).append(ln);
			begin = loan.interest1.getEnd() + 1;

			sb.append(encode(R.string.interest_period)).append(tab);
			sb.append(nr.format(loan.interest2.getRate())).append('%')
					.append(tab);
			sb.append(Integer.toString(begin)).append('~');
			if (!loan.interest3.isEnable()) {
				sb.append(Integer.toString(end)).append(ln);
			} else {
				sb.append(Integer.toString(loan.interest2.getEnd())).append(ln);
				begin = loan.interest2.getEnd() + 1;
				sb.append(encode(R.string.interest_period)).append(tab);
				sb.append(nr.format(loan.interest3.getRate())).append('%')
						.append(tab);
				sb.append(Integer.toString(begin)).append('~');
				sb.append(Integer.toString(end)).append(ln);
			}
		}

	}

	public void append(StringBuilder sb) {
		Schedule sc = loan.getSchedule();

		String[] array = resources.getStringArray(R.array.loan_types);
		appendHead(sb, encode(R.string.loan_type), array[loan.loan_type]);

		appendHead(sb, R.string.amount, loan.m_amount);
		// Interest Rate
		appendInterest(sb);

		if (sc.getMaxPay() - sc.getMinPay() > 1) {
			String buf = String.format("%s - %s", value(sc.getMaxPay()),
					value(sc.getMinPay()));
			appendHead(sb, encode(R.string.result_short_payment), buf);
		} else {
			appendHead(sb, R.string.result_short_payment, sc.getMaxPay());
		}
		appendHead(sb, R.string.result_short_interest, sc.getInterests());
		appendHead(sb, R.string.result_short_amount, sc.getPayments());

		sb.append(ln).append(ln);

		sb.append(encode(R.string.result_nr)).append(tab);
		sb.append(encode(R.string.result_principal)).append(tab);
		sb.append(encode(R.string.result_interest)).append(tab);
		sb.append(encode(R.string.result_payment)).append(ln);
		for (int i = 0; i < sc.getQuantity(); ++i) {
			sb.append(String.format("%4d", i + 1)).append(tab);
			sb.append(value(sc.getPrincipal(i))).append(tab);
			sb.append(value(sc.getInterest(i))).append(tab);
			sb.append(value(sc.getPayment(i))).append(ln);
		}
	}
	private String valueInt(double v)
	{
		return String.valueOf((int)(v+0.5));
	}
	public void save(File file) throws IOException {
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(
				file), "UTF-16LE");
		out.write('\uFEFF');
		out.write(encode(R.string.result_nr));
		out.write(tab);
		out.write(encode(R.string.result_principal));
		out.write(tab);
		out.write(encode(R.string.result_interest));
		out.write(tab);
		out.write(encode(R.string.result_payment));
		out.write(ln);
		Schedule sc = loan.getSchedule();
		for (int i = 0; i < sc.getQuantity(); ++i) {
			out.write(String.format("%d", i + 1));
			out.write(tab);
			out.write(valueInt(sc.getPrincipal(i)));
			out.write(tab);
			out.write(valueInt(sc.getInterest(i)));
			out.write(tab);
			out.write(valueInt(sc.getPayment(i)));
			out.write(ln);
		}

		out.flush();
		out.close();
	}
}
