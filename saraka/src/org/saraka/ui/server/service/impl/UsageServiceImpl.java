package org.saraka.ui.server.service.impl;

import java.util.List;

import org.saraka.ui.model.Usage;
import org.saraka.ui.server.dao.UsageDAO;
import org.saraka.ui.server.service.UsageService;

import android.content.Context;

public class UsageServiceImpl implements UsageService{
	private UsageDAO usageDAO;
	
	public UsageServiceImpl(Context context){
		usageDAO = new UsageDAO(context);
	}
	
	@Override
	public Usage getBuddyUsage(int buddyId) {
		return usageDAO.getBuddyUsage(buddyId);
	}

	@Override
	public void savePageHit(int buddyId) {
		usageDAO.savePageHit(buddyId);
	}

	@Override
	public void saveCallHit(int buddyId) {
		usageDAO.saveCallHit(buddyId);
	}

	@Override
	public void saveUrlHit(int buddyId) {
		usageDAO.saveUrlHit(buddyId);
	}

	@Override
	public void saveEmailHit(int buddyId) {
		usageDAO.saveEmailHit(buddyId);
	}

	@Override
	public void clearUsage() {
		usageDAO.clearUsage();
	}

	@Override
	public List<Usage> getAllUsage() {
		return usageDAO.getAllUsage();
	}

}
