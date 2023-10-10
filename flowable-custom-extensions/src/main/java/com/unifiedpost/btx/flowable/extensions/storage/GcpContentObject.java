/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.storage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.flowable.content.api.ContentObject;

public class GcpContentObject implements ContentObject {

	protected final String id;
	protected final String bucketName;

	public GcpContentObject(String id /*, Gcpclient gcpClient*/, String bucketName) {
		this.id = id;
		this.bucketName = bucketName;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public long getContentLength() {
		// StorageClient ...
		return LOREM.getBytes(StandardCharsets.UTF_8).length;
	}

	@Override
	public InputStream getContent() {
		// StorageClient - get file from bucket
		return new ByteArrayInputStream(LOREM.getBytes(StandardCharsets.UTF_8));
	}

	private static final String LOREM =
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n";
}
