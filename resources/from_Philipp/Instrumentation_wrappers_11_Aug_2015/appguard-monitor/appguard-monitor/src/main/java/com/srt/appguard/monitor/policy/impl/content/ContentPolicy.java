package com.srt.appguard.monitor.policy.impl.content;

import android.content.ContentResolver;
import android.database.MatrixCursor;
import android.net.Uri;

import com.srt.appguard.monitor.MonitorConfig;
import com.srt.appguard.monitor.exception.MonitorException;
import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.log.Meta;
import com.srt.appguard.monitor.log.Meta.MetaType;
import com.srt.appguard.monitor.policy.Policy;
import com.srt.appguard.monitor.policy.annotation.MapSignatures;

import java.io.FileNotFoundException;

public abstract class ContentPolicy extends Policy {

	protected boolean readAllowed = true;
	protected boolean writeAllowed = true;

	protected ContentPolicy() {
	}

	@Override
	protected void loadConfig(final MonitorConfig config, boolean defaultValue) {
		readAllowed = config.getPermissionStatus(getReadPermission(), defaultValue);
		writeAllowed = config.getPermissionStatus(getWritePermission(), defaultValue);
	}

	protected boolean isContentUri(Uri uri) {
		return "content".equals(uri.getScheme());
	}

	protected boolean isProtectedAuthority(String authority) {
		for (final String name : getProtectedAuthorities()) {
			if (name.equalsIgnoreCase(authority)) {
				return true;
			}
		}

		return false;
	}
	
	protected boolean isProtectedUri(Uri uri) {
		if (uri == null) {
			return false;
		}

		return isProtectedAuthority(uri.getAuthority());
	}

	protected boolean isReadAllowed(Uri uri) {
		if (!isProtectedUri(uri) || getReadPermission() == null) {
			return true;
		}

		if (readAllowed) {
			Logger.allow(getReadPermission(), new Meta(MetaType.URI, uri.toString()));
		} else {
			Logger.deny(getReadPermission(), new Meta(MetaType.URI, uri.toString()));
		}
		
		return readAllowed;
	}
	
	protected boolean isWriteAllowed(Uri uri) {
		if (!isProtectedUri(uri)) {
			return true;
		}

		if (writeAllowed) {
			Logger.allow(getWritePermission(), new Meta(MetaType.URI, uri.toString()));
		} else {
			Logger.deny(getWritePermission(), new Meta(MetaType.URI, uri.toString()));
		}

		return writeAllowed;
	}

	@Override
	public String[] getPermissions() {
		final String readPermission = getReadPermission();
		final String writePermission = getWritePermission();
		if (readPermission != null && writePermission != null && !readPermission.equals(writePermission)) {
			return new String[] { readPermission, writePermission };	
		} else if(writePermission != null) {
			return new String[] { writePermission };
		} else if(readPermission != null) {
			return new String[] { readPermission };
		} else {
			return new String[0];
		}
	}

	protected abstract String[] getProtectedAuthorities();
	
	protected abstract String getReadPermission();
	protected abstract String getWritePermission();


	/* Read operations */
	@MapSignatures({
		"Landroid/content/ContentResolver;->query(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)",
		"Landroid/content/ContentProviderClient;->query(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)"
		//"Landroid/content/ContentProviderProxy;->query(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)",
		//"Landroid/content/ContentProviderProxy;->query(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/ICancellationSignal;)"
	})
	public void android_content_ContentResolver__query(final Object _this, final Uri uri, final String[] columnNames) throws Exception {
		if (isContentUri(uri) && !isReadAllowed(uri)) {
			Object returnValue = null;
			if (columnNames != null) {
				returnValue = new MatrixCursor(columnNames);
			}
			throw new MonitorException(returnValue);
		}
	}

	@MapSignatures({
		"Landroid/content/ContentResolver;->registerContentObserver(Landroid/net/Uri;ZLandroid/database/ContentObserver;)"
	})
	public void android_content_ContentResolver__registerContentObserver(final ContentResolver _this, final Uri uri) throws Exception {
		if (isContentUri(uri) && !isReadAllowed(uri)) {
			throw new MonitorException();
		}
	}

	@MapSignatures({
		"Landroid/content/ContentResolver;->openInputStream(Landroid/net/Uri;)"
	})
	public void android_content_ContentResolver__openInputStream(final ContentResolver _this, final Uri uri) throws Exception {
		if (isContentUri(uri) && !isReadAllowed(uri)) {
			throw new FileNotFoundException();
		}
	}


	/* Write operations */
	@MapSignatures({
		"Landroid/content/ContentResolver;->insert(Landroid/net/Uri;Landroid/content/ContentValues;)",
		"Landroid/content/ContentProviderClient;->insert(Landroid/net/Uri;Landroid/content/ContentValues;)"
		//"Landroid/content/ContentProviderProxy;->insert(Landroid/net/Uri;Landroid/content/ContentValues;)"
	})
	public void android_content_ContentResolver__insert(final Object _this, final Uri uri) throws Exception {
		if (isContentUri(uri) && !isWriteAllowed(uri)) {
			// expects the uri of the newly inserted row as return value
			// lets try null :)
			throw new MonitorException();
		}
	}

	@MapSignatures({
		"Landroid/content/ContentResolver;->update(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)",
		"Landroid/content/ContentProviderClient;->update(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)"
		//"Landroid/content/ContentProviderProxy;->update(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)"
	})
	public void android_content_ContentResolver__update(final Object _this, final Uri uri) throws Exception {
		if (isContentUri(uri) && !isWriteAllowed(uri)) {
			// returns number of rows updated
			throw new MonitorException(0);
		}
	}

	@MapSignatures({
		"Landroid/content/ContentResolver;->delete(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)",
		"Landroid/content/ContentProviderClient;->delete(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)"
		//"Landroid/content/ContentProviderProxy;->delete(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)"
	})
	public void android_content_ContentResolver__delete(final Object _this, final Uri uri) throws Exception {
		if (isContentUri(uri) && !isWriteAllowed(uri)) {
			// returns number of rows deleted
			throw new MonitorException(0);
		}
	}

	@MapSignatures({
		"Landroid/content/ContentResolver;->bulkInsert(Landroid/net/Uri;[Landroid/content/ContentValues;)",
		"Landroid/content/ContentProviderClient;->bulkInsert(Landroid/net/Uri;[Landroid/content/ContentValues;)"
		//"Landroid/content/ContentProviderProxy;->bulkInsert(Landroid/net/Uri;[Landroid/content/ContentValues;)"
	})
	public void android_content_ContentResolver__bulkInsert(final Object _this, final Uri uri) throws Exception {
		if (isContentUri(uri) && !isWriteAllowed(uri)) {
			// return number of newly created rows
			throw new MonitorException(0);
		}
	}

	
	/* Read/Write operations */
	@MapSignatures({
		"Landroid/content/ContentResolver;->openFileDescriptor(Landroid/net/Uri;Ljava/lang/String;)"
	})
	public void android_content_ContentResolver__openFileDescriptor(final ContentResolver _this, final Uri uri, final String mode) throws Exception {
		if (isContentUri(uri)) {
			// check mode to determine whether we are reading or writing
			if (!isReadAllowed(uri) ||
				(mode != null && (mode.contains("w") || mode.contains("t")) && !isWriteAllowed(uri))) {
				throw new FileNotFoundException();
			}
		}
	}

	@MapSignatures({
		"Landroid/content/ContentProviderClient;->openFile(Landroid/net/Uri;Ljava/lang/String;)"
		//"Landroid/content/ContentProviderProxy;->openFile(Landroid/net/Uri;Ljava/lang/String;)"
	})
	public void android_content_ContentProviderClient__openFile(final Object _this, final Uri uri, final String mode) throws Exception {
		if (isContentUri(uri)) {
			// check mode to determine whether we are reading or writing
			if (!isReadAllowed(uri) ||
				(mode != null && (mode.contains("w") || mode.contains("t")) && !isWriteAllowed(uri))) {
				throw new FileNotFoundException();
			}
		}
	}

	/*
	@MapSignatures({
		"Landroid/content/ContentResolver;->applyBatch(Ljava/lang/String;Ljava/util/ArrayList;)",
	})
	public void android_content_ContentResolver__applyBatch(final ContentResolver _this, final String authority, final ArrayList<?> operations) throws Exception {
		if (isContentUri(uri) && !isWriteAllowed(uri)) {
			final int numOperations = operations != null ? operations.size() : 0;
			throw new MonitorException(new ContentProviderResult[numOperations]);
		}
	}
	*/
}
