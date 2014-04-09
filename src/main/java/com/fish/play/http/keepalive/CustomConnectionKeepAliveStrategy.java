package com.fish.play.http.keepalive;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
/**
 * <ul>
 * <li>
 * 默认实现类为{@link org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy DefaultConnectionKeepAliveStrategy},若server返回header头中指定
 * timeout属性, 那么connection keepalive时间按照这个值设定, 否则为-1,表示永远存活.
 * </li>
 * <li>
 * 该类保留了timeout处理方法, 并提供参数{@link com.fish.play.http.keepalive.CustomConnectionKeepAliveStrategy#keepAliveDuration keepAliveDuration}进行灵活配置
 * </li>
 * </ul>
 * 
 * 
 * @author changliang
 *
 */
public class CustomConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {
	private long keepAliveDuration = 5 * 1000;
	
	public CustomConnectionKeepAliveStrategy() {
	}
	
	/**
	 * 
	 * @param keepAliveDuration 连接存活毫秒数
	 */
	public CustomConnectionKeepAliveStrategy(long keepAliveDuration) {
		this.keepAliveDuration = keepAliveDuration;
	}

	public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
		final HeaderElementIterator it = new BasicHeaderElementIterator(
				response.headerIterator(HTTP.CONN_KEEP_ALIVE));
		while (it.hasNext()) {
			final HeaderElement he = it.nextElement();
			final String param = he.getName();
			final String value = he.getValue();
			if (value != null && param.equalsIgnoreCase("timeout")) {
				try {
					return Long.parseLong(value) * 1000;
				} catch (final NumberFormatException ignore) {
					return keepAliveDuration;
				}
			}
		}
		
		
		return keepAliveDuration;
	}

	public void setKeepAliveDuration(long keepAliveDuration) {
		this.keepAliveDuration = keepAliveDuration;
	}
}
