package org.saraka.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.saraka.ui.common.DashboardCategoryDialog;
import org.saraka.ui.common.SharedPreferencesCompat;
import org.saraka.ui.common.Utils;
import org.saraka.ui.model.Buddy;
import org.saraka.ui.model.DashBoard;
import org.saraka.ui.model.DashboardCategory;
import org.saraka.ui.server.service.BuddyService;
import org.saraka.ui.server.service.DashboardCategoryPositiveListener;
import org.saraka.ui.server.service.DashboardCategoryService;
import org.saraka.ui.server.service.DashboardService;
import org.saraka.ui.server.service.SearchService;
import org.saraka.ui.server.service.impl.BuddyServiceImpl;
import org.saraka.ui.server.service.impl.DashboardCategoryServiceImpl;
import org.saraka.ui.server.service.impl.DashboardServiceImpl;
import org.saraka.ui.server.service.impl.SearchServiceImpl;
import org.saraka.ui.server.uibinders.DashboardUIBinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
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

public class DashboardUI extends BaseUI implements OnItemClickListener,
		TextWatcher, DashboardCategoryPositiveListener {

	private SharedPreferences sharedPrefs;
	private ListView dashboardListView;
	private List<DashBoard> dashboards;
	private List<DashBoard> tempDashboards;
	private String[] dashboardCategories;

	private DashboardService dashboardService;
	private DashboardCategoryService dashboardCategoryService;
	private BuddyService buddyService;
	private SearchService searchService;

	private DashboardSearchAsyncTask dashboardSearchAsyncTask;
	private boolean isUpdateCalled = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboardui_layout);

		Utils.d(TAG, "Dashboard onCreate()");
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		dashboardService = new DashboardServiceImpl(this);
		tempDashboards = dashboardService.getAllDashboards();
		Collections.sort(tempDashboards);

		dashboards = tempDashboards;
		setupUi();
	}

	@SuppressWarnings("deprecation")
	private void setupUi() {
		setupActionbarUi();
		setupFooterBarUi();

		if (sharedPrefs.getBoolean(Utils.KEY_SHOW_WELCOME, true)) {
			showDialog(DIALOG_WELCOME);
			SharedPreferencesCompat.apply(sharedPrefs.edit().putBoolean(
					Utils.KEY_SHOW_WELCOME, false));
		}

		dashboardListView = (ListView) findViewById(R.id.dashboard_list_view);
		dashboardListView.setAdapter(new DashboardUIBinder(DashboardUI.this,
				tempDashboards));
		dashboardListView.setOnItemClickListener(this);
	}

	private void setupFooterBarUi() {

	}

	private void setupActionbarUi() {
		listSearchBox = (EditText) findViewById(R.id.list_search_box);
		backButton = (ImageButton) findViewById(R.id.nav_back_button);
		pageLabel = (TextView) findViewById(R.id.nav_page_label);
		globalSearchBox = (TextView) findViewById(R.id.search_box);
		globalSearchButton = (ImageButton) findViewById(R.id.quick_search_btn);
		homeButton = (ImageButton) findViewById(R.id.home_btn);

		listSearchBox.addTextChangedListener(this);

		homeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.showToast(DashboardUI.this, "Already Home.");
			}
		});

		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				backButtonClickHandler();
			}
		});

		pageLabel.setText("Dashboard");

		globalSearchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				globalSearchClickHandler();
			}
		});

	}

	private void globalSearchClickHandler() {
		//TODO implement this.
	}

	private void backButtonClickHandler() {
		//TODO implement this.
	}

	private class DashboardSearchAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			if (tempDashboards.size() == 0)
				Utils.showToast(DashboardUI.this, Utils.NO_RESULT_FOUND);
			Collections.sort(tempDashboards);
			dashboardListView.setAdapter(new DashboardUIBinder(
					DashboardUI.this, tempDashboards));

			try {
				this.finalize();
				this.cancel(true);
			} catch (Throwable e) {
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			tempDashboards = new ArrayList<DashBoard>();
			if (searchService == null) {
				searchService = new SearchServiceImpl();
			}

			tempDashboards = searchService.dashboardSearch(listSearchBox
					.getText().toString(), dashboards);
			return null;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		DashBoard selectedDashboard = tempDashboards.get(position);
		dashboardCategories = null;

		dashboardCategoryService = new DashboardCategoryServiceImpl(
				DashboardUI.this);

		List<DashboardCategory> dCategoryList = dashboardCategoryService
				.getAllDashboardCategoryByDashboardId(selectedDashboard.getId());

		if (!dCategoryList.isEmpty()) {
			dashboardCategories = new String[dCategoryList.size()];
			for (int i = 0; i < dCategoryList.size(); i++) {
				dashboardCategories[i] = dCategoryList.get(i).getCategoryName();
			}
			FragmentManager manager = getSupportFragmentManager();
			DashboardCategoryDialog alert = new DashboardCategoryDialog(
					dashboardCategories, selectedDashboard.getName()
							+ " Listing", "view");

			Bundle bundle = new Bundle();
			bundle.putInt("position", position);
			alert.setArguments(bundle);
			alert.show(manager, "alert_dialog_radio");
		} else {
			Utils.showToast(DashboardUI.this,
					"No sub listing found. Update Saraka");
		}
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
		dashboardSearchAsyncTask = new DashboardSearchAsyncTask();
		dashboardSearchAsyncTask.execute();
	}

	@Override
	public void onPositiveClick(int position) {
		if (dashboardCategories != null) {
			String categoryName = dashboardCategories[position];
			DashboardCategory dashboardCategory = dashboardCategoryService
					.getDashboardCategoryByName(categoryName);
			buddyService = new BuddyServiceImpl(DashboardUI.this);

			List<Buddy> buddies = buddyService
					.getAllBuddiesByDashboardCategoryId(dashboardCategory
							.getId());
			Collections.sort(buddies);

			if (!buddies.isEmpty()) {
				Intent intent = new Intent(DashboardUI.this, /* ListingUI.class */
						null);
				Bundle bundle = new Bundle();
				bundle.putString(Utils.PAGE_NAME, dashboardCategories[position]);
				bundle.putParcelableArrayList(Utils.BUDDY_LISTING,
						(ArrayList<? extends Parcelable>) buddies);
				intent.putExtras(bundle);
				startActivity(intent);
			}

		}
	}
	
	@Override
	protected void onDestroy() {
		DashboardUI.this.finish();
		super.onDestroy();
	}

	@Override
	protected void onResumeFragments() {
		if(isUpdateCalled){
			tempDashboards = dashboardService.getAllDashboards();
			Collections.sort(tempDashboards);
			dashboards = tempDashboards;
			dashboardListView.setAdapter(new DashboardUIBinder(DashboardUI.this,
					tempDashboards));
			isUpdateCalled = false;
		}
		super.onResumeFragments();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about_update:
			Intent intent = new Intent(DashboardUI.this, UpdateUI.class);
			isUpdateCalled = true;
			startActivity(intent);
			break;

		default:
			break;
		}
		return false;
	}

}
