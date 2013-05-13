package org.saraka.ui.server.service;

import java.util.List;

import org.saraka.ui.model.Comment;
import org.saraka.ui.model.Rate;
import org.saraka.ui.model.Usage;

public interface UsageService {
	public Usage getBuddyUsage(int buddyId);
	public void savePageHit(int buddyId);
	public void saveCallHit(int buddyId);
	public void saveUrlHit(int buddyId);
	public void saveEmailHit(int buddyId);
	public void clearUsage();
	public List<Usage> getAllUsage();
	public List<Comment> getComments();
	public Comment getCommentByBuddyId(int buddyId);
	public List<Rate> getRate();
	public Rate getRateByBuddyId(int buddyId);
	public void saveComment(Comment comment);
	public void saveRate(Rate rate);
	public void clearRate();
	public void clearComment();
	public void saveCommentHit(int buddyId);
	public void saveRateHit(int buddyId);
	
}
