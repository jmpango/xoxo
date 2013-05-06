package org.saraka.ui.server.service;

import java.util.List;

import org.saraka.ui.model.Buddy;
import org.saraka.ui.model.DashBoard;

public interface SearchService {
	public List<Buddy> listingSearch(String query, List<Buddy> items);
	public List<DashBoard> dashboardSearch(String query, List<DashBoard> items);
}
