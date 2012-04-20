package com.marakana.android.yamba;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class StatusProvider extends ContentProvider {
	
	private static final String TAG = "StatusProvider";
	
	private TimelineHelper dbHelper;

	private static final String DB_NAME = "timeline.db";
	private static final int DB_VERSION = 3;
	private static final String T_TIMELINE = "timeline";
	
	// Constants to help differentiate between the URI requests
	private static final int STATUS_DIR = 1;
	private static final int STATUS_ITEM = 2;
	
	private static final UriMatcher uriMatcher;
	
	// Static initializer, allocating a UriMatcher object. A URI ending in "/status" is a
	// request for all statuses, and a URI ending in "/status/<id>" refers to a single status.
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(StatusContract.AUTHORITY, "status", STATUS_DIR);
		uriMatcher.addURI(StatusContract.AUTHORITY, "status/#", STATUS_ITEM);
	}
	
	// Helper class for opening, creating, and upgrading the database
	private class TimelineHelper extends SQLiteOpenHelper {
		
		private static final String DB_CREATE
			= "create table " + T_TIMELINE + " ( "
			+ StatusContract.Columns._ID + " integer primary key, "
			+ StatusContract.Columns.USER + " text, "
			+ StatusContract.Columns.MESSAGE + " text, "
			+ StatusContract.Columns.CREATED_AT + " integer "
			+ ");" ;

		public TimelineHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}
	
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "Creating database");
			db.execSQL(DB_CREATE);
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "Upgrading database from version " + oldVersion + " to version  " + newVersion);
			
			// In a real app, you'd do a proper upgrade rather than dropping the table like this.
			db.execSQL("drop table if exists " + T_TIMELINE);
			onCreate(db);
		}
	}
	
	// Identify the MIME types we provide for a given URI
	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case STATUS_DIR:
			return StatusContract.CONTENT_TYPE;
		case STATUS_ITEM:
			return StatusContract.CONTENT_ITEM_TYPE;
		default:
            return null;
        }
	}

	@Override
	public boolean onCreate() {
		Context context = getContext();
		// The onCreate() method runs in the looper thread. We don't want to block it,
		// so we won't invoke getWritableDatabase() here -- it could cause on upgrade
		// of an existing database, which would be time consuming.
		// Instead, we invoke getWritableDatabase() in the CRUD methods, because
		// Android automatically invokes them in worker threads in the CP process.
		// The client might block when invoking a CRUD method, but that's its problem;
		// the client should ideally invoke them from its own worker thread.
		dbHelper = new TimelineHelper(context);
		return (dbHelper == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection,
						String selection, String[] selectionArgs,
						String sort) {
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		// A convenience class to help build the query
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		qb.setTables(T_TIMELINE);
		
		switch (uriMatcher.match(uri)) {
		case STATUS_DIR:
			break;
		case STATUS_ITEM:
			// If this is a request for an individual status, limit the result set to that ID
			qb.appendWhere(StatusContract.Columns._ID + "=" + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		
		// Use our default sort order if none was specified
		String orderBy = TextUtils.isEmpty(sort)
					   ? StatusContract.Columns.DEFAULT_SORT_ORDER
					   : sort;
		
		// Query the underlying database
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
		
		// Notify the context's ContentResolver if the cursor result set changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		
		// Return the cursor to the result set
		return c;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		Uri result = null;
		
		// Validate the requested Uri
        if (uriMatcher.match(uri) != STATUS_DIR) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowID = db.insert(T_TIMELINE, null, initialValues);
        
        if (rowID > 0) {
	        // Return a URI to the newly created row on success
	        result = ContentUris.withAppendedId(StatusContract.CONTENT_URI, rowID);
	        
	        // Notify the Context's ContentResolver of the change
	        getContext().getContentResolver().notifyChange(result, null);
        }
        return result;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count;
		
		switch (uriMatcher.match(uri)) {
		case STATUS_DIR:
			count = db.delete(T_TIMELINE, where, whereArgs);
			break;
		case STATUS_ITEM:
			String segment = uri.getLastPathSegment();
			String whereClause = StatusContract.Columns._ID + "=" + segment 
							   + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : "");
			count = db.delete(T_TIMELINE, whereClause, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		
		if (count > 0) {
	    	// Notify the Context's ContentResolver of the change
	    	getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count;
		
		switch (uriMatcher.match(uri)) {
		case STATUS_DIR:
			count = db.update(T_TIMELINE, values, where, whereArgs);
			break;
		case STATUS_ITEM:
			String segment = uri.getLastPathSegment();
			String whereClause = StatusContract.Columns._ID + "=" + segment 
							   + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : "");
			count = db.update(T_TIMELINE, values, whereClause, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		
		if (count > 0) {
	    	// Notify the Context's ContentResolver of the change
	    	getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

}
