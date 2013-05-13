package org.saraka.ui;

import java.util.ArrayList;
import java.util.List;

import org.saraka.ui.common.SarakaDialog;
import org.saraka.ui.common.Utils;
import org.saraka.ui.model.Buddy;
import org.saraka.ui.model.BuddyLocation;
import org.saraka.ui.model.Rate;
import org.saraka.ui.server.service.BuddyService;
import org.saraka.ui.server.service.SarakaPositiveListener;
import org.saraka.ui.server.service.impl.BuddyServiceImpl;
import org.saraka.ui.server.service.impl.UsageServiceImpl;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

public class DetailUI extends BaseUI implements SarakaPositiveListener {
	private ImageButton callLancher;
	private ImageButton emailLancher;
	private ImageButton urlLancher;
	private ImageButton commentLancher;
	private ImageButton rateLancher;

	private TextView nameView;
	private TextView taglineView;
	private TextView addressView;
	private TextView telphoneView;
	private TextView faxView;
	private TextView urlView;
	private TextView emailView;
	private TextView locationView;

	private String[] tels, ratings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailui_layout);

		Utils.d(TAG, "Detail onCreate()");
		this.detailedPageBuddy = (Buddy) getIntent().getParcelableExtra(
				Utils.DEFAULT_BUDDY);
		usageService = new UsageServiceImpl(this);
		usageService.savePageHit(detailedPageBuddy.getId());

		setupUi();
	}

	private void setupUi() {
		setupActionbarUi();
		setupImageUi();
		setupIconLanchers();
		setupContentUi();
	}

	private void setupImageUi() {

	}

	private void setupContentUi() {
		nameView = (TextView) findViewById(R.id.details_name);
		taglineView = (TextView) findViewById(R.id.details_tagline);
		addressView = (TextView) findViewById(R.id.details_addresss);
		telphoneView = (TextView) findViewById(R.id.details_telphone);
		faxView = (TextView) findViewById(R.id.details_fax);
		urlView = (TextView) findViewById(R.id.details_url);
		emailView = (TextView) findViewById(R.id.details_email);
		locationView = (TextView) findViewById(R.id.details_location);

		nameView.setText(detailedPageBuddy.getName());
		taglineView.setText(detailedPageBuddy.getTagLine());
		addressView.setText(detailedPageBuddy.getAddress());
		telphoneView.setText("(+256) " + detailedPageBuddy.getTelphoneNos());

		if (detailedPageBuddy.getFax() != null)
			faxView.setText(detailedPageBuddy.getFax());
		else
			faxView.setVisibility(View.GONE);

		if (detailedPageBuddy.getUrl() != null)
			urlView.setText(detailedPageBuddy.getUrl());
		else
			urlView.setVisibility(View.GONE);

		if (detailedPageBuddy.getEmail() != null)
			emailView.setText(detailedPageBuddy.getEmail());
		else
			emailView.setVisibility(View.GONE);

		BuddyService buddyService = new BuddyServiceImpl(DetailUI.this);
		List<BuddyLocation> locations = buddyService
				.getAllBuddyLocationsByBuddyId(detailedPageBuddy.getId());

		if (!locations.isEmpty()) {
			String locationString = "";
			for (BuddyLocation location : locations) {
				locationString = locationString + location.getLocationName()
						+ ", ";
			}
			locationView.setText(locationString);
		} else
			locationView.setVisibility(View.GONE);

	}

	@SuppressWarnings("deprecation")
	private void setupIconLanchers() {
		callLancher = (ImageButton) findViewById(R.id.details_call_btn);
		emailLancher = (ImageButton) findViewById(R.id.details_email_btn);
		urlLancher = (ImageButton) findViewById(R.id.visit_website_btn);
		commentLancher = (ImageButton) findViewById(R.id.comment_btn);
		rateLancher = (ImageButton) findViewById(R.id.rateme_btn);

		if (detailedPageBuddy.getEmail() == null) {
			emailLancher.setVisibility(View.GONE);
		}

		if (detailedPageBuddy.getUrl() == null) {
			urlLancher.setVisibility(View.GONE);
		}

		tels = getBuddyTels();

		callLancher.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager manager = getSupportFragmentManager();
				SarakaDialog alert = new SarakaDialog(tels, "Select a Number",
						"Call", RADIO_DIALOG_CALL);
				Bundle bundle = new Bundle();
				bundle.putInt("position", 0);
				alert.setArguments(bundle);
				alert.show(manager, "alert_dialog_radio");
			}
		});

		emailLancher.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DIALOG_EMAIL);
			}
		});

		urlLancher.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://" + detailedPageBuddy.getUrl()));
				usageService.saveUrlHit(detailedPageBuddy.getId());
				startActivity(browserIntent);
			}
		});

		commentLancher.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DIALOG_COMMENT);
			}
		});

		rateLancher.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ratings = new String[] { "1", "2", "3", "4", "5" };

				FragmentManager manager = getSupportFragmentManager();
				SarakaDialog alert = new SarakaDialog(ratings, "RATE "
						+ detailedPageBuddy.getName(), "RATE",
						RADIO_DIALOG_RATE);
				Bundle bundle = new Bundle();
				bundle.putInt("position", 0);
				alert.setArguments(bundle);
				alert.show(manager, "alert_dialog_radio");
			}
		});
	}

	private String[] getBuddyTels() {
		String telNos = detailedPageBuddy.getTelphoneNos() + ",";
		String extractNmbrz = telNos.substring(telNos.indexOf(")") + 1,
				telNos.length()).trim();
		List<String> nos = new ArrayList<String>();

		int startChar = 0;
		int endChar = -1;

		for (int index = 0; index < extractNmbrz.length(); index++) {
			if (extractNmbrz.charAt(index) == ',') {
				endChar = index - 1;
				nos.add("+256"
						+ extractNmbrz.substring(startChar, endChar + 1).trim());
				startChar = index + 1;
			}
		}

		String[] telz = new String[nos.size()];
		for (int incremental = 0; incremental < nos.size(); incremental++) {
			telz[incremental] = nos.get(incremental);
		}

		return telz;
	}

	private void setupActionbarUi() {
		pageLabel = (TextView) findViewById(R.id.nav_page_label);
		globalSearchBox = (TextView) findViewById(R.id.search_box);
		globalSearchButton = (ImageButton) findViewById(R.id.quick_search_btn);
		homeButton = (ImageButton) findViewById(R.id.home_btn);
		backButton = (ImageButton) findViewById(R.id.nav_back_button);

		pageLabel.setText("Details");

		homeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				homeButtonClickHandler();
			}
		});

		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				backButtonClickHandler();
			}
		});

		globalSearchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				globalSearchClickHandler();
			}
		});

	}

	private void globalSearchClickHandler() {
		// TODO implement this.
	}

	private void backButtonClickHandler() {
		// TODO implement this.
	}

	private void homeButtonClickHandler() {
		// TODO implement this.
	}

	@Override
	public void onPositiveClick(int position, int name) {

		switch (name) {
		case RADIO_DIALOG_CALL:
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:" + tels[position]));
			usageService.saveCallHit(detailedPageBuddy.getId());
			startActivity(callIntent);
			break;
		case RADIO_DIALOG_RATE:
			String selectedRate = ratings[position];
			Rate rate = new Rate(0, Integer.parseInt(selectedRate), detailedPageBuddy.getId());
			usageService.saveRate(rate);
			usageService.saveRateHit(detailedPageBuddy.getId());
			Utils.showToast(DetailUI.this, "Thank you for rating me. "
					+ selectedRate);
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about_update:
			Intent intent = new Intent(DetailUI.this, UpdateUI.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return false;
	}

}
