package org.saraka.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.saraka.ui.common.Utils;
import org.saraka.ui.model.Buddy;
import org.saraka.ui.model.BuddyLocation;
import org.saraka.ui.model.BuddySearchTag;
import org.saraka.ui.model.DashBoard;
import org.saraka.ui.model.DashboardCategory;
import org.saraka.ui.model.Usage;
import org.saraka.ui.server.service.BuddyService;
import org.saraka.ui.server.service.DashboardCategoryService;
import org.saraka.ui.server.service.DashboardService;
import org.saraka.ui.server.service.UsageService;
import org.saraka.ui.server.service.impl.BuddyServiceImpl;
import org.saraka.ui.server.service.impl.DashboardCategoryServiceImpl;
import org.saraka.ui.server.service.impl.DashboardServiceImpl;
import org.saraka.ui.server.service.impl.UsageServiceImpl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpdateUI extends BaseUI {
	private TextView updateDownloadText, updateProgressText;
	private ProgressBar updateProgressBar;
	private Button startDownloadBtn, updateBackBtn;
	private DashboardService dashboardService;
	private BuddyService buddyService;
	private UsageService usageService;
	private DashboardCategoryService dashboardCategoryService;
	private int incremental = 0;
	private static InputStream is = null;
	private static JSONObject jObj = null;
	private static String json = "";
	private boolean noErrorThrown = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updateui_layout);

		updateDownloadText = (TextView) findViewById(R.id.update_download_text);
		updateProgressText = (TextView) findViewById(R.id.update_progress_text);

		updateProgressBar = (ProgressBar) findViewById(R.id.update_progressbar);
		startDownloadBtn = (Button) findViewById(R.id.update_download_btn);
		updateBackBtn = (Button) findViewById(R.id.update_back_btn);

		updateDownloadText.setVisibility(View.GONE);

		dashboardService = new DashboardServiceImpl(this);
		dashboardCategoryService = new DashboardCategoryServiceImpl(this);

		startDownloadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isNetworkAvailable()) {
					new DownloadTaskHandler().execute();
				} else {
					Utils.showToast(UpdateUI.this,
							"No network connection found ..Connect and Try Again. ");
				}
			}
		});

		updateBackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateUI.this.finish();
				UpdateUI.this.onBackPressed();
			}
		});
	}

	private boolean isNetworkAvailable() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] activeNetworkInfo = connectivityManager
				.getAllNetworkInfo();

		for (NetworkInfo ni : activeNetworkInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}

		return haveConnectedWifi || haveConnectedMobile;
	}

	private class DownloadTaskHandler extends AsyncTask<Void, Integer, Void> {
		@Override
		protected Void doInBackground(Void... args) {
			try {
				submitUsage();
				downloadBuddySearchTags();
				downloadBuddyLocations();
				downloadBuddies();
				downloadDashboardCategory();
				downloadDashboard();
			} catch (final Exception e) {
				noErrorThrown = true;
				runOnUiThread(new Runnable() {
					public void run() {
						incremental = 0;
						updateDownloadText.setVisibility(View.GONE);
						publishProgress(incremental);
						Utils.showPopDialog(UpdateUI.this, "" + e.getMessage());
					}
				});

				return null;
			}
			return null;
		}

		private void submitUsage() throws IllegalStateException, IOException,
				JSONException {
			List<Usage> usages = usageService.getAllUsage();
			incremental = 0;
			incremental += 5;
			publishProgress(incremental);
			if (!usages.isEmpty()) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				JSONObject jsonObj = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				for (Usage usage : usages) {
					JSONObject childObj = new JSONObject();
					childObj.put("pageHit", usage.getPageHits() + "");
					childObj.put("callHit", usage.getCallHits() + "");
					childObj.put("urlHit", usage.getUrlHits() + "");
					childObj.put("emailHit", usage.getEmailHits() + "");
					childObj.put("buddyId", usage.getBuddyId() + "");

					jsonArray.put(childObj);
				}
				jsonObj.put("usages", jsonArray);

				params.add(new BasicNameValuePair("hits", jsonObj.toString()));
				makeHttpRequest(Utils.SERVER_URL + "usage", "POST", params);
				incremental += 10;
				publishProgress(incremental);
				usageService.clearUsage();
				incremental += 1;
			}
		}

		private void downloadBuddySearchTags() throws IllegalStateException,
				IOException, JSONException {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			incremental += 6;
			String updateDate = buddyService.getBuddySearchTagUpdateDate();
			String[] split = updateDate.split("\\s+");
			updateDate = split[0];

			JSONObject jsonObject = makeHttpRequest(Utils.SERVER_URL
					+ "buddysearchtag/" + updateDate, "GET", params);
			System.out.println("NNOOO SearchTag ---- ");
			incremental += 5;
			publishProgress(incremental);
			try {
				int sucess = jsonObject.getInt("sucess");
				if (sucess == 1) {
					String nextUpdateDate = jsonObject
							.getString("lastupdatedate");

					JSONArray buddySearchArray = jsonObject
							.getJSONArray("buddysearchtags");

					for (int i = 0; i < buddySearchArray.length(); i++) {
						JSONObject jsonBuddySearchtag = buddySearchArray
								.getJSONObject(i);
						BuddySearchTag buddySearchTag = new BuddySearchTag(
								jsonBuddySearchtag.getInt("id"),
								jsonBuddySearchtag.getString("search_value"),
								jsonBuddySearchtag.getString("buddy_id"));

						if (buddyService.getBuddySearchTagById(buddySearchTag
								.getId()) != null) {
							buddyService.updateBuddySearchTag(buddySearchTag);
						} else {
							buddyService.addBuddySearchTag(buddySearchTag);
						}

					}

					JSONArray buddySearchTagDeleteArray = jsonObject
							.getJSONArray("deleteBuddysearchtags");
					for (int i = 0; i < buddySearchTagDeleteArray.length(); i++) {
						JSONObject jsonDeleteBuddySearchTag = buddySearchTagDeleteArray
								.getJSONObject(i);
						BuddySearchTag buddySearchTag = new BuddySearchTag();
						buddySearchTag.setId(jsonDeleteBuddySearchTag
								.getInt("id"));

						buddyService.deleteBuddySearchTag(buddySearchTag);
					}
					buddyService.setBuddySearchTagUpdateDate(nextUpdateDate);
				}
			} catch (JSONException e) {
			}
			incremental += 5;
			publishProgress(incremental);
		}

		private void downloadBuddyLocations() throws IllegalStateException,
				IOException, JSONException {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			incremental += 6;
			publishProgress(incremental);

			String updateDate = buddyService.getBuddyLocationUpdateDate();
			String[] split = updateDate.split("\\s+");
			updateDate = split[0];

			JSONObject jsonObject = makeHttpRequest(Utils.SERVER_URL
					+ "buddylocations/" + updateDate, "GET", params);

			incremental += 5;
			publishProgress(incremental);
			try {
				int sucess = jsonObject.getInt("sucess");
				if (sucess == 1) {
					String nextUpdateDate = jsonObject
							.getString("lastupdatedate");

					JSONArray buddyLocationArray = jsonObject
							.getJSONArray("buddylocations");

					for (int i = 0; i < buddyLocationArray.length(); i++) {
						JSONObject jsonBuddyLocation = buddyLocationArray
								.getJSONObject(i);
						BuddyLocation buddyLocation = new BuddyLocation(
								jsonBuddyLocation.getInt("id"),
								jsonBuddyLocation.getString("location_name"),
								jsonBuddyLocation.getString("buddy_id"));

						if (buddyService.getBuddyLocationById(buddyLocation
								.getId()) != null) {
							buddyService.updateBuddyLocation(buddyLocation);
						} else {
							buddyService.addBuddyLocation(buddyLocation);
						}

					}

					JSONArray buddyLocationDeleteArray = jsonObject
							.getJSONArray("deleteBuddylocations");
					for (int i = 0; i < buddyLocationDeleteArray.length(); i++) {
						JSONObject jsonDeleteBuddyLocation = buddyLocationDeleteArray
								.getJSONObject(i);
						BuddyLocation buddyLocation = new BuddyLocation();
						buddyLocation.setId(jsonDeleteBuddyLocation
								.getInt("id"));

						buddyService.deleteBuddyLocation(buddyLocation);
					}
					buddyService.setBuddyLocationUpdateDate(nextUpdateDate);
				}
			} catch (JSONException e) {
			}
			incremental += 6;
			publishProgress(incremental);
		}

		private void downloadBuddies() throws IllegalStateException,
				IOException, JSONException {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			incremental += 6;
			publishProgress(incremental);

			String updateDate = buddyService.getUpdateDate();
			String[] split = updateDate.split("\\s+");
			updateDate = split[0];

			JSONObject jsonObject = makeHttpRequest(Utils.SERVER_URL
					+ "buddies/" + updateDate, "GET", params);

			incremental += 5;
			publishProgress(incremental);
			try {
				int sucess = jsonObject.getInt("sucess");
				if (sucess == 1) {
					String nextUpdateDate = jsonObject
							.getString("lastupdatedate");

					JSONArray buddyArray = jsonObject.getJSONArray("buddies");

					for (int i = 0; i < buddyArray.length(); i++) {
						JSONObject jsonBuddyCategory = buddyArray
								.getJSONObject(i);
						Buddy buddy = new Buddy(jsonBuddyCategory.getInt("id"),
								jsonBuddyCategory.getString("name"),
								jsonBuddyCategory.getString("tagline"),
								jsonBuddyCategory.getString("email"),
								jsonBuddyCategory.getString("telphone"),
								jsonBuddyCategory.getString("url"),
								jsonBuddyCategory.getString("fax"),
								jsonBuddyCategory.getString("address"),
								jsonBuddyCategory
										.getString("dashboard_category_id"));

						if (buddyService.getBuddyById(buddy.getId()) != null) {
							buddyService.updateBuddy(buddy);
						} else {
							buddyService.addBuddy(buddy);
						}

					}

					JSONArray buddyDeleteArray = jsonObject
							.getJSONArray("deleteBuddies");
					for (int i = 0; i < buddyDeleteArray.length(); i++) {
						JSONObject jsonDeleteBuddy = buddyDeleteArray
								.getJSONObject(i);
						Buddy buddy = new Buddy();
						buddy.setId(jsonDeleteBuddy.getInt("id"));

						buddyService.deleteBuddy(buddy);
					}
					buddyService.setUpdateDate(nextUpdateDate);
				}
			} catch (JSONException e) {
			}
			incremental += 6;
			publishProgress(incremental);
		}

		private void downloadDashboardCategory() throws IllegalStateException,
				IOException, JSONException {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			incremental += 6;
			publishProgress(incremental);

			String updateDate = dashboardCategoryService.getUpdateDate();
			String[] split = updateDate.split("\\s+");
			updateDate = split[0];

			JSONObject jsonObject = makeHttpRequest(Utils.SERVER_URL
					+ "dashboardcategory/" + updateDate, "GET", params);

			incremental += 5;
			publishProgress(incremental);
			try {
				int sucess = jsonObject.getInt("sucess");
				if (sucess == 1) {
					String nextUpdateDate = jsonObject
							.getString("lastupdatedate");

					JSONArray dashboardCategoryArray = jsonObject
							.getJSONArray("dashboardCategories");

					for (int i = 0; i < dashboardCategoryArray.length(); i++) {
						JSONObject jsonDashboardCategory = dashboardCategoryArray
								.getJSONObject(i);
						DashboardCategory dashboardCategory = new DashboardCategory(
								jsonDashboardCategory.getInt("id"),
								jsonDashboardCategory.getString("cname"),
								jsonDashboardCategory.getString("dashboard_id"));

						if (dashboardCategoryService
								.getDashboardCategoryById(dashboardCategory
										.getId()) != null) {
							dashboardCategoryService
									.updateDashboardCategory(dashboardCategory);
						} else {
							dashboardCategoryService
									.addDashboardCategory(dashboardCategory);
						}

					}

					JSONArray dashboardCatDeleteArray = jsonObject
							.getJSONArray("deleteDashboardCategory");
					for (int i = 0; i < dashboardCatDeleteArray.length(); i++) {
						JSONObject jsonDeleteDashboardCat = dashboardCatDeleteArray
								.getJSONObject(i);
						DashboardCategory dashboardCategory = new DashboardCategory(
								jsonDeleteDashboardCat.getInt("id"), "", "");

						dashboardCategoryService
								.deleteDashboardCategory(dashboardCategory);
					}

					dashboardCategoryService.setUpdateDate(nextUpdateDate);
				}
			} catch (JSONException e) {
			}
			incremental += 6;
			publishProgress(incremental);
		}

		private void downloadDashboard() throws IllegalStateException,
				IOException, JSONException {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			incremental = +6;
			publishProgress(incremental);

			String updateDate = dashboardService.getUpdateDate();
			String[] split = updateDate.split("\\s+");
			updateDate = split[0];

			JSONObject jsonObject = makeHttpRequest(Utils.SERVER_URL
					+ "dashboard/" + updateDate, "GET", params);

			incremental += 5;
			publishProgress(incremental);
			try {
				int sucess = jsonObject.getInt("sucess");
				if (sucess == 1) {
					String nextUpdateDate = jsonObject
							.getString("lastupdatedate");

					JSONArray dashboardArray = jsonObject
							.getJSONArray("dashboards");

					for (int i = 0; i < dashboardArray.length(); i++) {
						JSONObject jsonDashboard = dashboardArray
								.getJSONObject(i);
						DashBoard dashboard = new DashBoard(
								jsonDashboard.getInt("id"),
								jsonDashboard.getString("dname"),
								jsonDashboard.getString("tagline"));

						if (dashboardService
								.getDashboardById(dashboard.getId()) != null) {
							dashboardService.updateDashboard(dashboard);
						} else {
							dashboardService.addDashboard(dashboard);
						}

					}

					JSONArray dashboardDeleteArray;

					dashboardDeleteArray = jsonObject
							.getJSONArray("deleteDashboard");
					for (int i = 0; i < dashboardDeleteArray.length(); i++) {
						JSONObject jsonDeleteDashboard = dashboardDeleteArray
								.getJSONObject(i);
						DashBoard dashboard = new DashBoard(
								jsonDeleteDashboard.getInt("id"), "", "");

						dashboardService.deleteDashboard(dashboard);
					}

					dashboardService.setUpdateDate(nextUpdateDate);

				}
			} catch (JSONException e) {
			}
			incremental += 6;
			publishProgress(incremental);
		}

		@Override
		protected void onPostExecute(Void result) {
			startDownloadBtn.setEnabled(true);
			if (!noErrorThrown) {
				noErrorThrown = false;
				runOnUiThread(new Runnable() {
					public void run() {
						incremental = 0;
						updateDownloadText.setVisibility(View.GONE);
						publishProgress(incremental);
						Utils.showPopDialog(UpdateUI.this,
								"Saraka Update Completed.");
					}
				});
			}
			try {
				this.finalize();
				this.cancel(true);
			} catch (Throwable e) {
			}
		}

		@Override
		protected void onPreExecute() {
			usageService = new UsageServiceImpl(UpdateUI.this);
			buddyService = new BuddyServiceImpl(UpdateUI.this);
			super.onPreExecute();
		}

		protected void onProgressUpdate(Integer... values) {
			if (values[0] <= 100) {
				updateProgressText.setText("Progress: "
						+ Integer.toString(values[0]) + "%");
				updateProgressBar.setProgress(values[0]);
			}
		}

	}

	public JSONObject makeHttpRequest(String url, String method,
			List<NameValuePair> params) throws IllegalStateException,
			IOException, JSONException {
		if (method == "POST") {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			httpPost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse httpResponse = httpClient.execute(httpPost);

			if (httpResponse != null) {
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			}

		} else if (method == "GET") {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = null;
			try {
				httpResponse = httpClient.execute(httpGet);
			} catch (Exception e) {
				System.out.println("SearchTag ---- 2");
			}

			if (httpResponse != null) {
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			} else {
				System.out.println("SearchTag ---- 3 ");
			}

		}

		if (is != null) {

			BufferedReader reader;
			reader = new BufferedReader(
					new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			json = sb.toString();

			jObj = new JSONObject(json);
		}
		return jObj;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

}
