package com.fish.play.http.client.imp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import com.fish.play.http.client.CustomHttpClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/**
 * In this environment, connection can not be reused. All configuration is default.
 * 
 * @author changliang
 * 
 */
public class DefaultHttpClientImpl implements CustomHttpClient {
	private static final Log log = LogFactory.getLog(DefaultHttpClientImpl.class);
	private RequestConfig requestConfig;

	private ResponseHandler<String> responseHandler;
	private int connectTimeout = 5 * 1000;
	private int socketTimeout = 10 * 1000;
	private CloseableHttpClient httpclient;
	public void init() {
		requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
		httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
		responseHandler = new ResponseHandler<String>() {
			public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
					HttpEntity entity = response.getEntity();
					Charset charset = ContentType.getOrDefault(entity).getCharset();
					if (charset == null) {
						charset = Charset.forName("UTF-8");
					}
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}
		};
	}

	public String post(String uri, List<NameValuePair> list) {

		return post(uri, list, null);
	}
	
	

	public String post(String uri, List<NameValuePair> list, List<Header> headers) {
		String responseBody = null;
		HttpPost httpPost = new HttpPost(uri);
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, Consts.UTF_8);
		httpPost.setEntity(entity);
		if (headers != null) {
			for (Header header : headers) {
				httpPost.addHeader(header);
			}
		}
		try {
			responseBody = httpclient.execute(httpPost, responseHandler);
		} catch (Exception e) {
			log.error("default httpclient post() execute exception, " + e);
		} 

		return responseBody;
	}



	public String get(String uri) {
		return get(uri, null);
	}
	
	public String get(String uri, List<Header> headers) {
		String responseBody = null;

		HttpGet httpGet = new HttpGet(uri);
		
		if (headers != null) {
			for (Header header : headers) {
				httpGet.addHeader(header);
			}
		}
		try {
			responseBody = httpclient.execute(httpGet, responseHandler);
		} catch (Exception e) {
			log.error("default httpclient get() execute exception, " + e);
		}
		return responseBody;
	}

	public void closeHttpClient() {
		try {
			httpclient.close();
		} catch (IOException e) {
			log.error("default httpclient close exception, " + e);
		}
	}
	
	public String getTotalPoolStats() {
		return null;
	}
	
	public String getPoolStats(String hostname, int port) {
		return null;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

}