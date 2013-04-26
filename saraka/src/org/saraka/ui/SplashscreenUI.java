package org.saraka.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SplashscreenUI extends Activity{
	private long seconds = 0;
	private long splashTime = 5000;
	private boolean isSplashActive = true;
	private boolean splashPaused = false;
	private Thread splashThread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreenui_layout);
		
		if (getIntent().getBooleanExtra("EXIT", false)) {
			SplashscreenUI.this.finish();
		} else {
			
			splashThread = new Thread() {
				public void run() {
					try {
						while (isSplashActive && seconds < splashTime) {
							if (!splashPaused)
								seconds += 100;
							sleep(100);
						}
					} catch (Exception e) {
					} finally {
						Intent intent = new Intent(SplashscreenUI.this, DashboardUI.class);
						SplashscreenUI.this.finish();
						startActivity(intent);
					}
				}
			};
			
			splashThread.start();
			
		}
	}
}
