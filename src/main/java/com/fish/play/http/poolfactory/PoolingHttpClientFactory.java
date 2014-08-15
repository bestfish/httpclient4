package com.fish.play.http.poolfactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * 生成池化的 CloseableHttpClient
 * 
 * @author changliang
 * 
 */
public class PoolingHttpClientFactory {

	public static CloseableHttpClient createPoolingHttpClient(ConnectionKeepAliveStrategy keepAliveStrategy,
			PoolingHttpClientConnectionManager connManager, Runnable idleConnectionMonitor, long initialDelay,
			long delay, RequestConfig requestConfig) {

		ScheduledExecutorService schedule = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
		schedule.scheduleWithFixedDelay(idleConnectionMonitor, initialDelay, delay, TimeUnit.MILLISECONDS);

		return HttpClients.custom().setKeepAliveStrategy(keepAliveStrategy).setConnectionManager(connManager)
				.setDefaultRequestConfig(requestConfig).build();

	}

}
