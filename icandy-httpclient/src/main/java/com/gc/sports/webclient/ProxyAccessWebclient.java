package com.gc.sports.webclient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyAccessWebclient {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyAccessWebclient.class);

	private static int SOCKET_TIMEOUT = 20000;
	private static int CONNECT_TIMEOUT = 20000;
	private static int CONNECT_REQUEST_TIMEOUT = 20000;
	private static int MAX_TOTAL = 200;
	private static int REQUEST_RETRY = 3;

	private static String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 8.0; Windows XP)";

	public String host;

	private CloseableHttpClient httpClient = null;

	public ProxyAccessWebclient() {
		init();
	}

	public ProxyAccessWebclient(String host) {
		this.host = host;
		init();
	}

	private void init(){
		/*
		 * setSocketTimeout
		 * setConnectTimeout
		 * setConnectionRequestTimeout
		 */
		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT).build();

		PoolingHttpClientConnectionManager conMgr = new PoolingHttpClientConnectionManager();
		// 设置整个连接池最大连接数 根据自己的场景决定
		conMgr.setMaxTotal(MAX_TOTAL);
		// 是路由的默认最大连接（该值默认为2），限制数量实际使用DefaultMaxPerRoute并非MaxTotal。
		// 设置过小无法支持大并发(ConnectionPoolTimeoutException: Timeout waiting for connection from pool)，路由是对maxTotal的细分。
		//（目前只有一个路由，因此让他等于最大值）
		conMgr.setDefaultMaxPerRoute(conMgr.getMaxTotal());

		// 另外设置http client的重试次数，默认是3次；当前是禁用掉（如果项目量不到，这个默认即可）
		DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(REQUEST_RETRY, true);

		// 设置重定向策略  
		LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();

		// 自签名策略
		SSLConnectionSocketFactory sslsf = null;
		try {
			final SSLContextBuilder sSLContextBuilder = new SSLContextBuilder();
			sSLContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			sslsf = new SSLConnectionSocketFactory(sSLContextBuilder.build());
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		} catch (KeyStoreException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		} catch (KeyManagementException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}

		// 创建HttpClientBuilder
		// HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// httpClient = httpClientBuilder.setDefaultRequestConfig(defaultRequestConfig).build();
		httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).setConnectionManager(conMgr).setRetryHandler(retryHandler).setRedirectStrategy(redirectStrategy).setSSLSocketFactory(sslsf).build();

	}

	public String httpGet(String host, String uri, List<NameValuePair> params){
		return httpGet(params, host + uri);
	}

	public String httpGet(String uri, List<NameValuePair> params){
		return httpGet(params, host + uri);
	}

	public String httpGet(List<NameValuePair> params, String url){
		HttpGet httpGet = new HttpGet(url);

		List<Header> headerList = new ArrayList<Header>();
		headerList.add(new BasicHeader("User-Agent", USER_AGENT));
		headerList.add(new BasicHeader("Connection", "close")); // 短链接
		httpGet.setHeaders(headerList.toArray(new Header[]{}));

		HttpEntity httpEntity = null;
		try {
			if(params == null){
				params = new ArrayList<>();
			}
			String paramsStr = EntityUtils.toString(new UrlEncodedFormEntity(params));

			httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + paramsStr));

			HttpResponse httpResponse = httpClient.execute(httpGet);

			StatusLine statusLine = httpResponse.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if(statusCode == 200){
				httpEntity = httpResponse.getEntity();
				if(httpEntity != null){
					String content = EntityUtils.toString(httpEntity, "UTF-8");
					LOGGER.info("html内容： " + content);
					return content;
				}
			}
		} catch (ClientProtocolException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		} catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		} catch (URISyntaxException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		} finally {
			httpGet.abort();
			if(httpEntity != null){
				try {
					EntityUtils.consume(httpEntity);
				} catch (IOException e) {
					LOGGER.error(e.getLocalizedMessage(), e);
				}
			}
		}

		return null;
	}

	public String httpPost(String host, String uri, List<NameValuePair> params){
		return httpPost(params, host + uri);
	}

	public String httpPost(String uri, List<NameValuePair> params){
		return httpPost(params, host + uri);
	}

	public String httpPost(List<NameValuePair> params, String url){
		HttpPost httpPost = new HttpPost(url);

		List<Header> headerList = new ArrayList<Header>();
		headerList.add(new BasicHeader("User-Agent", USER_AGENT));
		headerList.add(new BasicHeader("Connection", "close")); // 短链接
		httpPost.setHeaders(headerList.toArray(new Header[]{}));

		HttpEntity httpEntity = null;
		try {
			if(params == null){
				params = new ArrayList<>();
			}
			HttpEntity entity = new UrlEncodedFormEntity(params);

			httpPost.setEntity(entity);

			HttpResponse httpResponse = httpClient.execute(httpPost);

			StatusLine statusLine = httpResponse.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if(statusCode == 200){
				httpEntity = httpResponse.getEntity();
				if(httpEntity != null){
					String content = EntityUtils.toString(httpEntity, "UTF-8");
					LOGGER.info("html内容： " + content);
					return content;
				}
			}
		} catch (ClientProtocolException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		} catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		} finally {
			httpPost.abort();
			if(httpEntity != null){
				try {
					EntityUtils.consume(httpEntity);
				} catch (IOException e) {
					LOGGER.error(e.getLocalizedMessage(), e);
				}
			}
		}

		return null;
	}

	public void close() {
		if (httpClient != null) {
			try {
				httpClient.close();
			} catch (IOException e) {
				LOGGER.error(e.getLocalizedMessage(), e);
			}
		}
	}

	public void restart() {

	}

}
