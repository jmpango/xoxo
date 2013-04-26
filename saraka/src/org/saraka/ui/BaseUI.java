package org.saraka.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class BaseUI extends FragmentActivity{
	public static final int DIALOG_WELCOME = 1;
	public static final int DIALOG_EMAIL = 2;
	public static final int DIALOG_COMMENT = 3;
	public static final int DIALOG_RATE = 4;
	
	public static final String TAG = "Saraka";
	
	public EditText listSearchBox;
	public ImageButton backButton;
	public TextView pageLabel;
	public TextView globalSearchBox;
	public ImageButton globalSearchButton;
	public ImageButton homeButton;
	
	@SuppressWarnings("deprecation")
	protected Dialog onCreateDialog(int id){
		switch (id) {
		case DIALOG_WELCOME:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(R.drawable.ic_logo);
			builder.setTitle(R.string.welcome_title);
			builder.setMessage(Html.fromHtml(getString(R.string.welcome_msg)));
			builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			return builder.create();
			case DIALOG_EMAIL:
				return null;
			case DIALOG_COMMENT:
				return null;
			case DIALOG_RATE:
				return null;
		default:
			return super.onCreateDialog(id);
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_layout, menu);
		return true;
	}

}
