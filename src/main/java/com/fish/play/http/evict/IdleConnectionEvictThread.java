package com.fish.play.http.evict;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpClientConnectionManager;

/**
 * connection eviction policy
 * @author changliang
 *
 */
public class IdleConnectionEvictThread implements Runnable {

	private HttpClientConnectionManager httpClientConnectionManager;
	private long idleTime;
	
	public IdleConnectionEvictThread() {
	}

	public IdleConnectionEvictThread(HttpClientConnectionManager httpClientConnectionManager) {
		this(httpClientConnectionManager, 0);
	}
	
	public IdleConnectionEvictThread(HttpClientConnectionManager httpClientConnectionManager, long idleTime) {
		this.httpClientConnectionManager = httpClientConnectionManager;
		this.idleTime = idleTime;
	}

	public void run() {
		httpClientConnectionManager.closeExpiredConnections();
		if (idleTime > 0) {
			httpClientConnectionManager.closeIdleConnections(idleTime, TimeUnit.MILLISECONDS);
		}
	}

	public void setIdleTime(long idleTime) {
		this.idleTime = idleTime;
	}


	public void setHttpClientConnectionManager(
			HttpClientConnectionManager httpClientConnectionManager) {
		this.httpClientConnectionManager = httpClientConnectionManager;
	}
	
}
