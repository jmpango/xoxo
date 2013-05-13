package org.saraka.ui.server.dao;

import java.util.ArrayList;
import java.util.List;

import org.saraka.ui.model.Comment;
import org.saraka.ui.model.Rate;
import org.saraka.ui.model.Usage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UsageDAO extends BaseDAO {
	private static final String USAGE_TABLE = "buddy_usage";

	public UsageDAO(Context context) {
		super(context);
	}

	public Usage getBuddyUsage(int buddyId) {
		Usage usage = null;
		SQLiteDatabase db = this.dbOpen();
		Cursor cursor = db.query(USAGE_TABLE, new String[] { "id", "page_hits",
				"call_hits", "url_hits", "email_hits","comment_hits","rate_hits", "buddy_id" },
				"buddy_id = ?", new String[] { String.valueOf(buddyId) }, null,
				null, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				usage = new Usage(Integer.parseInt(cursor.getString(0)),
						Integer.parseInt(cursor.getString(1)),
						Integer.parseInt(cursor.getString(2)),
						Integer.parseInt(cursor.getString(3)),
						Integer.parseInt(cursor.getString(4)),
						Integer.parseInt(cursor.getString(7)),
						Integer.parseInt(cursor.getString(5)),
						Integer.parseInt(cursor.getString(6)));
			}
		}

		cursor.close();
		this.dbClose(db);

		return usage;
	}

	public void savePageHit(int buddyId) {
		Usage usage = getBuddyUsage(buddyId);
		if (usage == null) {
			ContentValues values = new ContentValues();
			values.put("page_hits", String.valueOf(1));
			values.put("call_hits", String.valueOf(0));
			values.put("url_hits", String.valueOf(0));
			values.put("email_hits", String.valueOf(0));
			values.put("comment_hits", String.valueOf(0));
			values.put("rate_hits", String.valueOf(0));
			values.put("buddy_id", String.valueOf(buddyId));

			SQLiteDatabase db = this.dbOpen();
			db.insert(USAGE_TABLE, null, values);

			values.clear();
			values = null;

			this.dbClose(db);
		} else {
			ContentValues values = new ContentValues();
			values.put("page_hits", String.valueOf(usage.getPageHits() + 1));

			SQLiteDatabase db = this.dbOpen();
			db.update(USAGE_TABLE, values, "buddy_id = ?",
					new String[] { String.valueOf(buddyId) });
			values.clear();
			values = null;

			this.dbClose(db);
		}
	}

	public void saveCallHit(int buddyId) {
		Usage usage = getBuddyUsage(buddyId);
		if (usage == null) {
			ContentValues values = new ContentValues();
			values.put("page_hits", String.valueOf(0));
			values.put("call_hits", String.valueOf(1));
			values.put("url_hits", String.valueOf(0));
			values.put("email_hits", String.valueOf(0));
			values.put("comment_hits", String.valueOf(0));
			values.put("rate_hits", String.valueOf(0));
			values.put("buddy_id", String.valueOf(buddyId));

			SQLiteDatabase db = this.dbOpen();
			db.insert(USAGE_TABLE, null, values);

			values.clear();
			values = null;

			this.dbClose(db);
		} else {
			ContentValues values = new ContentValues();
			values.put("call_hits", String.valueOf(usage.getCallHits() + 1));

			SQLiteDatabase db = this.dbOpen();
			db.update(USAGE_TABLE, values, "buddy_id = ?",
					new String[] { String.valueOf(buddyId) });
			values.clear();
			values = null;

			this.dbClose(db);
		}
	}

	public void saveUrlHit(int buddyId) {
		Usage usage = getBuddyUsage(buddyId);
		if (usage == null) {
			ContentValues values = new ContentValues();
			values.put("page_hits", String.valueOf(0));
			values.put("call_hits", String.valueOf(0));
			values.put("url_hits", String.valueOf(1));
			values.put("email_hits", String.valueOf(0));
			values.put("comment_hits", String.valueOf(0));
			values.put("rate_hits", String.valueOf(0));
			values.put("buddy_id", String.valueOf(buddyId));

			SQLiteDatabase db = this.dbOpen();
			db.insert(USAGE_TABLE, null, values);

			values.clear();
			values = null;

			this.dbClose(db);
		} else {
			ContentValues values = new ContentValues();
			values.put("url_hits", String.valueOf(usage.getUrlHits() + 1));

			SQLiteDatabase db = this.dbOpen();
			db.update(USAGE_TABLE, values, "buddy_id = ?",
					new String[] { String.valueOf(buddyId) });
			values.clear();
			values = null;

			this.dbClose(db);
		}
	}

	public void saveEmailHit(int buddyId) {
		Usage usage = getBuddyUsage(buddyId);
		if (usage == null) {
			ContentValues values = new ContentValues();
			values.put("page_hits", String.valueOf(0));
			values.put("call_hits", String.valueOf(0));
			values.put("url_hits", String.valueOf(0));
			values.put("email_hits", String.valueOf(1));
			values.put("comment_hits", String.valueOf(0));
			values.put("rate_hits", String.valueOf(0));
			values.put("buddy_id", String.valueOf(buddyId));

			SQLiteDatabase db = this.dbOpen();
			db.insert(USAGE_TABLE, null, values);

			values.clear();
			values = null;

			this.dbClose(db);
		} else {
			ContentValues values = new ContentValues();
			values.put("email_hits", String.valueOf(usage.getEmailHits() + 1));

			SQLiteDatabase db = this.dbOpen();
			db.update(USAGE_TABLE, values, "buddy_id = ?",
					new String[] { String.valueOf(buddyId) });
			values.clear();
			values = null;

			this.dbClose(db);
		}
	}

	public void clearUsage() {
		SQLiteDatabase db = this.dbOpen();
		db.delete(USAGE_TABLE, null, null);
		db.close();
	}

	public List<Usage> getAllUsage() {
		List<Usage> usageList = new ArrayList<Usage>();

		String sql = "SELECT * FROM " + USAGE_TABLE;
		SQLiteDatabase db = this.dbOpen();
		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.moveToFirst()) {
			do {
				Usage usage = new Usage();
				usage.setId(Integer.parseInt(cursor.getString(0)));
				usage.setPageHits(Integer.parseInt(cursor.getString(1)));
				usage.setCallHits(Integer.parseInt(cursor.getString(2)));
				usage.setUrlHits(Integer.parseInt(cursor.getString(3)));
				usage.setEmailHits(Integer.parseInt(cursor.getString(4)));
				usage.setCommentHits(Integer.parseInt(cursor.getString(5)));
				usage.setRateHits(Integer.parseInt(cursor.getString(6)));
				usage.setBuddyId(Integer.parseInt(cursor.getString(7)));

				usageList.add(usage);
			} while (cursor.moveToNext());
		}

		cursor.close();
		this.dbClose(db);

		return usageList;
	}

	public List<Comment> getComments() {
		// TODO Auto-generated method stub
		return null;
	}

	public Comment getCommentByBuddyId(int buddyId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Rate> getRate() {
		// TODO Auto-generated method stub
		return null;
	}

	public Rate getRateByBuddyId(int buddyId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveComment(Comment comment) {
		// TODO Auto-generated method stub
		
	}

	public void saveRate(Rate rate) {
		// TODO Auto-generated method stub
		
	}

	public void clearRate() {
		// TODO Auto-generated method stub
		
	}

	public void clearComment() {
		// TODO Auto-generated method stub
		
	}

	public void saveCommentHit(int buddyId) {
		Usage usage = getBuddyUsage(buddyId);
		if (usage == null) {
			ContentValues values = new ContentValues();
			values.put("page_hits", String.valueOf(0));
			values.put("call_hits", String.valueOf(0));
			values.put("url_hits", String.valueOf(0));
			values.put("email_hits", String.valueOf(0));
			values.put("comment_hits", String.valueOf(1));
			values.put("rate_hits", String.valueOf(0));
			values.put("buddy_id", String.valueOf(buddyId));

			SQLiteDatabase db = this.dbOpen();
			db.insert(USAGE_TABLE, null, values);

			values.clear();
			values = null;

			this.dbClose(db);
		} else {
			ContentValues values = new ContentValues();
			values.put("comment_hits", String.valueOf(usage.getCommentHits() + 1));

			SQLiteDatabase db = this.dbOpen();
			db.update(USAGE_TABLE, values, "buddy_id = ?",
					new String[] { String.valueOf(buddyId) });
			values.clear();
			values = null;

			this.dbClose(db);
		}
		
	}

	public void saveRateHit(int buddyId) {
		Usage usage = getBuddyUsage(buddyId);
		if (usage == null) {
			ContentValues values = new ContentValues();
			values.put("page_hits", String.valueOf(0));
			values.put("call_hits", String.valueOf(0));
			values.put("url_hits", String.valueOf(0));
			values.put("email_hits", String.valueOf(0));
			values.put("comment_hits", String.valueOf(0));
			values.put("rate_hits", String.valueOf(1));
			values.put("buddy_id", String.valueOf(buddyId));

			SQLiteDatabase db = this.dbOpen();
			db.insert(USAGE_TABLE, null, values);

			values.clear();
			values = null;

			this.dbClose(db);
		} else {
			ContentValues values = new ContentValues();
			values.put("rate_hits", String.valueOf(usage.getRateHits() + 1));

			SQLiteDatabase db = this.dbOpen();
			db.update(USAGE_TABLE, values, "buddy_id = ?",
					new String[] { String.valueOf(buddyId) });
			values.clear();
			values = null;

			this.dbClose(db);
		}
	}

}
