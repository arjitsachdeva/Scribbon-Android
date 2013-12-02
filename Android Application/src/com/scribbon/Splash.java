package com.scribbon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		Thread timer = new Thread(){
			public void run(){
				try{
					// Splash screen showing delay
					sleep(3000);
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
				finally{
					Intent ourMenu = new Intent(getApplicationContext(), DashboardActivity.class);
					startActivity(ourMenu);
				}
			}
		};
		timer.start();
	}
}
