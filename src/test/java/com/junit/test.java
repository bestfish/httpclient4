package com.junit;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fish.play.http.client.imp.DefaultHttpClientImpl;
import com.fish.play.http.client.imp.PoolingHttpClientImpl;
import junit.framework.TestCase;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;


public class test extends TestCase{
	ExecutorService exec;
	CountDownLatch latch;
	PoolingHttpClientImpl customHttpClient;
	DefaultHttpClientImpl defaultHttpClient;
	@Override
	protected void setUp() throws Exception {
		exec = Executors.newFixedThreadPool(33);
		latch = new CountDownLatch(1000000);
		customHttpClient = new PoolingHttpClientImpl();
		customHttpClient.init();
		defaultHttpClient = new DefaultHttpClientImpl();
		defaultHttpClient.init();
	}
	
	public void testBatchHttpPost() throws Exception {
//		long start2 = System.currentTimeMillis();
//		for (int i = 0; i < 1000; i++) {
//			List<NameValuePair> list = new ArrayList<NameValuePair>();
//			list.add(new BasicNameValuePair("pin", "jd001"));
//			defaultHttpClient.post("http://pluginsoa.m.jd.com/memorials", list);
//		}
//		System.out.println("时间2： " + (System.currentTimeMillis() - start2));
		
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			exec.execute(new Runnable() {
				
				public void run() {
					List<NameValuePair> list = new ArrayList<NameValuePair>();
					list.add(new BasicNameValuePair("pin", "jd001"));
					String result = customHttpClient.post("http://pluginsoa.m.jd.com/memorials", list);
					latch.countDown();
					if (latch.getCount() % 10 == 0) {
						System.out.println("--"  + customHttpClient.getTotalPoolStats());
						System.out.println("++" + result);
					}
				}
			});
		}
		latch.await();
		exec.shutdown();
		System.out.println("时间1： " + (System.currentTimeMillis() - start));
		
	}
	
	public void testIt() throws Exception {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		
		String content = URLEncoder.encode("你在哪里开心呢?", "utf-8");
		
		
		list.add(new BasicNameValuePair("body", content));
		String s = customHttpClient.get("http://localhost/keyExist?body=你在哪里开心");
		System.out.println(s);
	}
	
	public void testIt2() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("p","110001"));
		params.add(new BasicNameValuePair("skus","1012026445"));
		params.add(new BasicNameValuePair("c3","1"));
		params.add(new BasicNameValuePair("lid","1"));
		params.add(new BasicNameValuePair("ec","utf-8"));
		params.add(new BasicNameValuePair("uuid","-1"));
		params.add(new BasicNameValuePair("lim","20"));
		List<Header> headers = new ArrayList<Header>();
		Header headerPin = new BasicHeader("pin","ahxm");
		Header headerConn = new BasicHeader("Connection", "close");
		headers.add(headerPin);
		headers.add(headerConn);
		String response = customHttpClient.post("http://diviner.jd.com/diviner", params,headers);
		System.out.println(response);
	}
	
}
