package org.saraka.ui.server.service.impl;

import java.util.List;

import org.saraka.ui.model.Comment;
import org.saraka.ui.model.Rate;
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

	@Override
	public List<Comment> getComments() {
		return usageDAO.getComments();
	}

	@Override
	public Comment getCommentByBuddyId(int buddyId) {
		return usageDAO.getCommentByBuddyId(buddyId);
	}

	@Override
	public List<Rate> getRate() {
		return usageDAO.getRate();
	}

	@Override
	public Rate getRateByBuddyId(int buddyId) {
		return usageDAO.getRateByBuddyId(buddyId);
	}

	@Override
	public void saveComment(Comment comment) {
		usageDAO.saveComment(comment);
	}

	@Override
	public void saveRate(Rate rate) {
		usageDAO.saveRate(rate);
	}

	@Override
	public void clearRate() {
		usageDAO.clearRate();
	}

	@Override
	public void clearComment() {
		usageDAO.clearComment();
	}

	@Override
	public void saveCommentHit(int buddyId) {
		usageDAO.saveCommentHit(buddyId);
	}

	@Override
	public void saveRateHit(int buddyId) {
		usageDAO.saveRateHit(buddyId);
	}

}
