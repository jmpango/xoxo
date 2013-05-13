package org.saraka.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.saraka.ui.common.Utils;
import org.saraka.ui.model.Buddy;
import org.saraka.ui.server.service.SearchService;
import org.saraka.ui.server.service.impl.SearchServiceImpl;
import org.saraka.ui.server.uibinders.ListingUIBinder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ListingUI extends BaseUI implements OnItemClickListener,
		TextWatcher {

	private ListView listView;
	private String pageTitle;
	private List<Buddy> listingList;
	private List<Buddy> tempListingList;
	private ListingSearchAsyncTask listingSearchAsyncTask;
	private SearchService searchService;
	private boolean isUpdateCalled = false;

	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listingui_layout);

		Utils.d(TAG, "Listing onCreate()");
		Bundle bundle = this.getIntent().getExtras();
		this.pageTitle = bundle.getString(Utils.PAGE_NAME).toUpperCase();
		this.tempListingList = bundle
				.getParcelableArrayList(Utils.BUDDY_LISTING);
		Collections.sort(tempListingList);
		
		this.listingList = tempListingList;
		setupUi();
	}

	private void setupUi() {
		setupActionbarUi();
		
		this.listView = (ListView) findViewById(R.id.listing_list_view);
		this.listView.setOnItemClickListener(this);
		listView.setAdapter(new ListingUIBinder(ListingUI.this, tempListingList));
	}

	private void setupActionbarUi() {
		this.listSearchBox = (EditText) findViewById(R.id.list_search_box);
		this.backButton = (ImageButton) findViewById(R.id.nav_back_button);
		this.pageLabel = (TextView) findViewById(R.id.nav_page_label);
		this.globalSearchBox = (TextView) findViewById(R.id.search_box);
		this.globalSearchButton = (ImageButton) findViewById(R.id.quick_search_btn);
		this.homeButton = (ImageButton) findViewById(R.id.home_btn);
		pageLabel.setText(pageTitle + " LISTING");		
		
		listSearchBox.addTextChangedListener(this);
		
		homeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				backHomeButtonClickHandler();
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

	private void backHomeButtonClickHandler() {
		//TODO Implement this
	}
	
	private void globalSearchClickHandler() {
		//TODO Implement this
	}

	private void backButtonClickHandler() {
		//TODO implement this.
	}
	
	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (listingSearchAsyncTask != null) {
			listingSearchAsyncTask.cancel(true);
			this.listingSearchAsyncTask = null;
		}

		this.listingSearchAsyncTask = new ListingSearchAsyncTask();
		listingSearchAsyncTask.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Buddy buddy = (Buddy) tempListingList.get(position);
		Intent intent = new Intent(ListingUI.this, DetailUI.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable(Utils.DEFAULT_BUDDY, buddy);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private class ListingSearchAsyncTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			tempListingList = new ArrayList<Buddy>();
			if (searchService == null) {
				searchService = new SearchServiceImpl();
			}
			tempListingList = searchService.listingSearch(listSearchBox
					.getText().toString(), listingList);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (tempListingList.size() == 0)
				Utils.showToast(ListingUI.this, Utils.NO_RESULT_FOUND);
			listView.setAdapter(new ListingUIBinder(ListingUI.this,
					tempListingList));
		}
	}
	
	@Override
	protected void onDestroy() {
		ListingUI.this.finish();
		super.onDestroy();
	}
	
	@Override
	protected void onResumeFragments() {
		if(isUpdateCalled){
			isUpdateCalled = false;
		}
		super.onResumeFragments();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about_update:
			Intent intent = new Intent(ListingUI.this, UpdateUI.class);
			isUpdateCalled = true;
			startActivity(intent);
			break;

		default:
			break;
		}
		return false;
	}
}
