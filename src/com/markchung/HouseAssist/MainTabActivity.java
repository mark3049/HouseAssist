package com.markchung.HouseAssist;

import com.markchung.HouseAssist.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainTabActivity extends TabActivity {
	public static TabHost tabHost;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabs);
		tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this,ConverterActivity.class);
		spec = tabHost
                .newTabSpec("conver")
                .setIndicator(getString(R.string.tab_converter))                        
                .setContent(intent);
        tabHost.addTab(spec);

		intent = new Intent().setClass(this,Activity_Evaluate.class);
		spec = tabHost
                .newTabSpec("conver")
                .setIndicator(getString(R.string.tab_evaluate))                        
                .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this,MainActivity.class);
		spec = tabHost
                .newTabSpec("loan")
                .setIndicator(getString(R.string.tab_loan))                        
                .setContent(intent);
        tabHost.addTab(spec);
        
		intent = new Intent().setClass(this,CompassActivity.class);
		spec = tabHost
                .newTabSpec("compass")
                .setIndicator(getString(R.string.tab_compass))                        
                .setContent(intent);
        tabHost.addTab(spec);
        
	}

}
