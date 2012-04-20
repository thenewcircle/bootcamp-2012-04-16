package com.marakana.android.yamba;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * The Status provider contains information about Yamba status messages.
 */
public final class StatusContract {

	private StatusContract() {}
	
    /** The authority for the contacts provider */
	public static final String AUTHORITY = "com.marakana.android.yamba.provider";

    /** A content:// style uri to the authority for this table */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/status");
	
    /** The MIME type of {@link #CONTENT_URI} providing a directory of status messages. */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.marakana.status";

    /** The MIME type of a {@link #CONTENT_URI} a single status message. */
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.marakana.status";
    
	/**
	 * Column definitions for status information.
	 */
	public final static class Columns implements BaseColumns {
		private Columns() {}
		
		/**
         * The name of the user who posted the status message
         * <P>Type: TEXT</P>
		 */
		public static final String USER = "user";
		
		/**
         * The status message content
         * <P>Type: TEXT</P>
		 */
		public static final String MESSAGE = "message";
		
		/**
         * The date the message was posted, in milliseconds since the epoch
         * <P>Type: INTEGER (long)</P>
		 */
		public static final String CREATED_AT = "createdAt";
		
        /**
         * The default sort order for this table, reverse chronological order
         */
        public static final String DEFAULT_SORT_ORDER = CREATED_AT + " DESC";

	}

}
