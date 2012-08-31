package com.markchung.library;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
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
        if (savedInstanceState == null) {
        	SharedPreferences sets = getSharedPreferences(MainActivity.TAG, 0);
        	int index = sets.getInt("TabIndex", 0);
        	tabHost.setCurrentTab(index);
        }        
	}
	@Override
	protected void onStop() {
		SharedPreferences settings = getSharedPreferences(MainActivity.TAG, 0);
		SharedPreferences.Editor edit = settings.edit();
		int index = tabHost.getCurrentTab();
		edit.putInt("TabIndex", index);
		edit.commit();
		super.onStop();
	}

}
