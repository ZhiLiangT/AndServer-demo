package com.tzl.androidwebserver;


import java.io.File;
import java.io.IOException;

import com.tzl.androidwebserver.utils.NanoHTTPD;

import android.app.Activity;
import android.os.Bundle;

public class NanoActivity extends Activity {
	
	private NanoHTTPD sever = null;
	//private NanoHTTPD sever1=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nano);
		init();
	}
	private void init() {
		try {
			sever = new NanoHTTPD(8088, new File("/storage/emulated/0/db/"));
			//sever1= new NanoHTTPD(8087, new File("/storage/emulated/0/haha/"));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	public void clear(){
		
		if (sever != null) {
			sever.stop();
			sever = null;
		}
//		if (sever1 != null) {
//			sever1.stop();
//			sever1 = null;
//		}
	}
}
