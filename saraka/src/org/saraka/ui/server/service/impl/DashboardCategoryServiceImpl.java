package org.saraka.ui.server.service.impl;

import java.util.List;

import org.saraka.ui.model.DashboardCategory;
import org.saraka.ui.server.dao.DashboardCategoryDAO;
import org.saraka.ui.server.service.DashboardCategoryService;

import android.content.Context;

public class DashboardCategoryServiceImpl implements DashboardCategoryService{
	private DashboardCategoryDAO dashboardCategoryDAO;
	
	public DashboardCategoryServiceImpl(Context context){
		dashboardCategoryDAO = new DashboardCategoryDAO(context);
	}

	@Override
	public DashboardCategory getDashboardCategoryById(int id) {
		return dashboardCategoryDAO.getDashboardCategoryById(id);
	}

	@Override
	public List<DashboardCategory> getAllDashboardCategoryByDashboardId(
			int dashboardId) {
		return dashboardCategoryDAO.getAllDashboardCategoriesByDashboardId(dashboardId);
	}

	@Override
	public int getDashboardCategoryCount() {
		return dashboardCategoryDAO.getDashboardCategoryCount();
	}

	@Override
	public void addDashboardCategory(DashboardCategory dashboardCategory) {
		dashboardCategoryDAO.addDashboardCategory(dashboardCategory);
	}

	@Override
	public int updateDashboardCategory(DashboardCategory dashboardCategory) {
		return dashboardCategoryDAO.updateDashboardCategory(dashboardCategory);
	}

	@Override
	public void deleteDashboardCategory(DashboardCategory dashboardCategory) {
		dashboardCategoryDAO.deleteDashboardCategory(dashboardCategory);
	}

	@Override
	public String getUpdateDate() {
		return dashboardCategoryDAO.getUpdateDate();
	}

	@Override
	public void setUpdateDate(String newUpdateDate) {
		dashboardCategoryDAO.setUpdateDate(newUpdateDate);
	}

	@Override
	public DashboardCategory getDashboardCategoryByName(String cateoryName) {
		return dashboardCategoryDAO.getDashboardCategoryByName(cateoryName);
	}

}
