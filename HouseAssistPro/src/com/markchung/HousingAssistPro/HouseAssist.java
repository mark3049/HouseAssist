package com.markchung.HousingAssistPro;

import com.markchung.library.*;

import android.os.Bundle;
import android.widget.TabHost;

public class HouseAssist extends com.markchung.library.MainTabActivity {

	public static final String TAG = "HousingAssistPro";

	public static TabHost tabHost;
	
	public void onCreate(Bundle savedInstanceState) {
		MainTabActivity.TAG = TAG;
		super.onCreate(savedInstanceState);
	}
}
