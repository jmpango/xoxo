package org.saraka.ui;

import java.util.Date;

import org.saraka.ui.common.Utils;
import org.saraka.ui.model.Buddy;
import org.saraka.ui.model.Comment;
import org.saraka.ui.server.service.UsageService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class BaseUI extends FragmentActivity {
	public static final int DIALOG_WELCOME = 1;
	public static final int DIALOG_EMAIL = 2;
	public static final int DIALOG_COMMENT = 3;

	public static final int RADIO_DIALOG_DASHBOARDCATEGORY = 1;
	public static final int RADIO_DIALOG_CALL = 2;
	public static final int RADIO_DIALOG_RATE = 3;

	public static final String TAG = "Saraka";

	public EditText listSearchBox;
	public ImageButton backButton;
	public TextView pageLabel;
	public TextView globalSearchBox;
	public ImageButton globalSearchButton;
	public ImageButton homeButton;

	public Buddy detailedPageBuddy;
	public UsageService usageService;

	@SuppressWarnings("deprecation")
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_WELCOME:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(R.drawable.ic_logo);
			builder.setTitle(R.string.welcome_title);
			builder.setMessage(Html.fromHtml(getString(R.string.welcome_msg)));
			builder.setPositiveButton(getString(R.string.ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			return builder.create();
		case DIALOG_EMAIL:
			LayoutInflater inflater = LayoutInflater.from(this);
			View dialogview = inflater.inflate(R.layout.email_dialog_layout,
					null);
			AlertDialog.Builder emailbuilder = new AlertDialog.Builder(this);

			emailbuilder.setTitle("Email: " + detailedPageBuddy.getName());
			emailbuilder.setView(dialogview);
			return emailbuilder.create();
		case DIALOG_COMMENT:
			LayoutInflater commentInflater = LayoutInflater.from(this);
			View commentDialogview = commentInflater.inflate(R.layout.comment_dialog_layout,
					null);
			AlertDialog.Builder commentbuilder = new AlertDialog.Builder(this);

			commentbuilder.setTitle("Comment for: " + detailedPageBuddy.getName());
			commentbuilder.setView(commentDialogview);
			return commentbuilder.create();
		default:
			return super.onCreateDialog(id);
		}
	}

	@Override
	public void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DIALOG_EMAIL:
			final AlertDialog alertDialog = (AlertDialog) dialog;
			Button sendbutton = (Button) alertDialog
					.findViewById(R.id.email_btn_send);
			Button cancelbutton = (Button) alertDialog
					.findViewById(R.id.email_btn_cancel);
			final EditText emailSubject = (EditText) alertDialog
					.findViewById(R.id.txtemail_subject);
			final EditText emailMessage = (EditText) alertDialog
					.findViewById(R.id.txtemail_message);
			sendbutton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String subject = emailSubject.getText().toString();
					String message = emailMessage.getText().toString();
					String to = detailedPageBuddy.getEmail().trim();

					Intent email = new Intent(Intent.ACTION_SEND);
					email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
					email.putExtra(Intent.EXTRA_SUBJECT, subject);
					email.putExtra(Intent.EXTRA_TEXT, message);

					email.setType("message/rfc822");
					try {
						startActivity(Intent.createChooser(email,
								"Choose an Email client :"));
						Utils.showToast(BaseUI.this, "Email sent to "
								+ detailedPageBuddy.getEmail());
						usageService.saveEmailHit(detailedPageBuddy.getId());
					} catch (android.content.ActivityNotFoundException ex) {
						Utils.showToast(BaseUI.this,
								"There are no email clients installed.");
						alertDialog.dismiss();
					}

					alertDialog.dismiss();
				}
			});

			cancelbutton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
				}
			});
			break;
		case DIALOG_COMMENT:
			final AlertDialog commentAlert = (AlertDialog) dialog;
			Button saveCommentButton = (Button) commentAlert
					.findViewById(R.id.comment_btn_send);
			Button cancelCommentButton = (Button) commentAlert
					.findViewById(R.id.comment_btn_cancel);
			final EditText ownerTxt = (EditText) commentAlert
					.findViewById(R.id.txtowner);
			final EditText commentTxt = (EditText) commentAlert
					.findViewById(R.id.txtcomment);
			saveCommentButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String owner = ownerTxt.getText().toString();
					String comment = commentTxt.getText().toString();
					
					if(owner.equals("") || owner == null || comment.equals("") || comment == null ){
						commentAlert.dismiss();
						Utils.showToast(BaseUI.this, "Comment NOT saved. Both comment and your name are required.");
					}else{
						usageService.saveComment(new Comment(0, comment, owner, new Date() + "", detailedPageBuddy.getId()));
						usageService.saveCommentHit(detailedPageBuddy.getId());
						commentAlert.dismiss();
						Utils.showToast(BaseUI.this, "Thank you! Comment Saved.");
					}
				}
			});

			cancelCommentButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					commentAlert.dismiss();
				}
			});
			break;
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_layout, menu);
		return true;
	}
}