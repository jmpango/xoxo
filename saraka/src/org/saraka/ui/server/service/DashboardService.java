package org.saraka.ui.server.service;

import java.util.List;

import org.saraka.ui.model.DashBoard;

public interface DashboardService {
	
	public DashBoard getDashboardById(int id);
	public List<DashBoard> getAllDashboards();
	public int getDashboardsCount();
	public void addDashboard(DashBoard dashboard);
	public int updateDashboard(DashBoard dashboard);
	public void deleteDashboard(DashBoard dashboard);
	public String getUpdateDate();
	public void setUpdateDate(String newUpdateDate);
}
