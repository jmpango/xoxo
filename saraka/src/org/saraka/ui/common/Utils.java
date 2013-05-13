package org.saraka.ui.common;

import org.saraka.ui.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;


public final class Utils {
	
	private Utils(){}
	
	public static final String LISTING = "listing";
	public static final String PAGE_NAME = "pageName";
	public static final String DEFAULT_XML_EXTENTION = "Listing.xml";
	public static final String DEFAULT_BUDDY = "buddy";
	public static final String BUDDY_NAMES_XML = "appNames.xml";
	public static final String NO_RESULT_FOUND = "No search Found";
	public static final String DEFAULT_APP_EMAIL = "info@ugbuddy.ug";
	public static final String BUDDY_LISTING = "buddyListing";
	public static final String KEY_SHOW_WELCOME = "show_welcome";
	
	public static final String SERVER_URL = "http://10.0.2.2:8000/sarakaPortal/mobileApi/list/";
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "ugbuddy";
	
	
	public static final String CONVERT_FIRST_LETTER_CAPS(String letter){
		return letter.substring(0,1).toUpperCase()+ letter.substring(1, letter.length());
	}
	
	public static final String REMOVE_WHITESPACES(String word){
		return word.replaceAll("\\s+", "");
	}
	
	public static void d(String tag, String message){
		Log.d(tag, message);
	}
	
	public static void showToast(Activity activity, final String msg) {
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void showPopDialog(Activity activity, final String msg){
		AlertDialog.Builder pop = new AlertDialog.Builder(activity);
        pop.setTitle("Saraka Notifier");
        pop.setMessage(msg);
        pop.setIcon(R.drawable.ic_info);
        pop.setNeutralButton("Close", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface arg0, int arg1) {
           }
          });pop.show();
	}
}
