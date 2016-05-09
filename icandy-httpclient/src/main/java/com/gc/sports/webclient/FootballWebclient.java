package com.gc.sports.webclient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FootballWebclient {

	private static final Logger LOGGER = LoggerFactory.getLogger(FootballWebclient.class);

	private static int SOCKET_TIMEOUT = 20000;
	private static int CONNECT_TIMEOUT = 20000;
	private static int CONNECT_REQUEST_TIMEOUT = 20000;
	private static int MAX_TOTAL = 200;
	private static int REQUEST_RETRY = 3;

	private static String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 8.0; Windows XP)";

	public String hgHost;

	private String hgUsername;
	private String hgPasswd;

	private String uid = "";
	private String mtype = "4";
	private String langx = "zh-cn";

	private CloseableHttpClient httpClient = null;

	public FootballWebclient(String hgUsername, String hgPasswd, String hgHost) {
		this.hgUsername = hgUsername;
		this.hgPasswd = hgPasswd;
		this.hgHost = hgHost;

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

		//另外设置http client的重试次数，默认是3次；当前是禁用掉（如果项目量不到，这个默认即可）
		DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(REQUEST_RETRY, true);

		//设置重定向策略  
		LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();

		// 创建HttpClientBuilder
		// HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// httpClient = httpClientBuilder.setDefaultRequestConfig(defaultRequestConfig).build();
		httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).setConnectionManager(conMgr).setRetryHandler(retryHandler).setRedirectStrategy(redirectStrategy).build();

	}

	public boolean login(){
		// return login(hgUsername, hgPasswd);

		String content = loginByPost(hgUsername, hgPasswd);
		LOGGER.info("皇冠体育登录数据： " + content);
		// String[] infoArr = StringUtils.split(content, '|');
		String[] infoArr = content.split("\\|");
		if(infoArr != null && infoArr.length >= 3){
			String statu = infoArr[0];
			if(statu != null && "200".equals(statu)){
				uid = infoArr[3];
				mtype = infoArr[4];
				langx = infoArr[5];
				return true;
			}
		}
		return false;
	}

	public String testLogin(){

		String content = loginByPost(hgUsername, hgPasswd);

		if(!StringUtils.isEmpty(content)){
			// String[] infoArr = StringUtils.split(content, '|');
			String[] infoArr = content.split("\\|");
			if(infoArr != null && infoArr.length >= 3){
				String statu = infoArr[0];
				if(statu != null && "200".equals(statu)){
					uid = infoArr[3];
					mtype = infoArr[4];
					langx = infoArr[5];
					return "<font color=\"#00ff00\">可登陆</font> {账号: " + hgUsername + ", 地址: " + hgHost + ", UID: " + uid + "}";
				} else if(statu != null && "error".equals(statu)){ // error|105|您输入的帐号或密码不正确。 请重新登入。|||
					return "<font color=\"#ff0000\">帐号或密码不正确</font> {账号: " + hgUsername + ", 密码: " + hgPasswd + "}";
				}
			} else if(infoArr != null && infoArr.length >= 3){
				return "访问次数过多，被限制访问";
			} else if(infoArr != null && infoArr.length >= 3){
				return "许久未重置密码";
			}
		}

		return "皇冠地址: " + hgHost + "不能访问";
	}

	private String loginByPost(String username, String passwd) {
		HttpPost httpPost = new HttpPost(hgHost + "");

		List<Header> headerList = new ArrayList<Header>();
		headerList.add(new BasicHeader("User-Agent", USER_AGENT));
		headerList.add(new BasicHeader("Connection", "close")); // 短链接
		httpPost.setHeaders(headerList.toArray(new Header[]{}));

		HttpEntity httpEntity = null;
		try {
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("passwd", passwd));
			params.add(new BasicNameValuePair("langx", "zh-cn"));
			HttpEntity entity = new UrlEncodedFormEntity(params);
			httpPost.setEntity(entity);

			HttpResponse httpResponse = httpClient.execute(httpPost);

			StatusLine statusLine = httpResponse.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if(statusCode == 200){
				httpEntity = httpResponse.getEntity();
				if(httpEntity != null){
					/*
					 * 200|100||r443dv71h5m13936461l4679030|4|zh-cn
					 * error|103|您输入的帐号或密码不正确。 请重新登入。|||
					 * statu=arr[0]; msg=arr[1]; code_message=arr[2]; uid=arr[3]; mtype=arr[4]; langx=arr[5];
					 */
					String content = EntityUtils.toString(httpEntity, "UTF-8");
					return content;
				}
			}
		} catch (ClientProtocolException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return "";
		} catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return "";
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

		return "";
	}

	public String httpGet(String uri, List<NameValuePair> params){
		if(uid == null || "".equals(uid)){
			boolean loginResult = this.login();
			if(loginResult == false){
				return null;
			}
		}

		HttpGet httpGet = new HttpGet(hgHost + uri);

		List<Header> headerList = new ArrayList<Header>();
		headerList.add(new BasicHeader("User-Agent", USER_AGENT));
		headerList.add(new BasicHeader("Connection", "close")); // 短链接
		httpGet.setHeaders(headerList.toArray(new Header[]{}));

		HttpEntity httpEntity = null;
		try {
			if(params == null){
				params = new ArrayList<>();
			}
			params.add(new BasicNameValuePair("uid", uid));
			params.add(new BasicNameValuePair("mtype", mtype));
			params.add(new BasicNameValuePair("langx", langx));
			String paramsStr = EntityUtils.toString(new UrlEncodedFormEntity(params));

			httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + paramsStr));

			HttpResponse httpResponse = httpClient.execute(httpGet);

			StatusLine statusLine = httpResponse.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if(statusCode == 200){
				httpEntity = httpResponse.getEntity();
				if(httpEntity != null){
					String content = EntityUtils.toString(httpEntity, "UTF-8");
					LOGGER.info("皇冠体育数据： " + content);
					boolean checkResponse = this.checkResponse(content);
					if(checkResponse == true){
						return content;
					}
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

	public String httpGetWithoutMtype(String uri, List<NameValuePair> params){
		if(uid == null || "".equals(uid)){
			boolean loginResult = this.login();
			if(loginResult == false){
				return null;
			}
		}

		HttpGet httpGet = new HttpGet(hgHost + uri);

		List<Header> headerList = new ArrayList<Header>();
		headerList.add(new BasicHeader("User-Agent", USER_AGENT));
		headerList.add(new BasicHeader("Connection", "close")); // 短链接
		httpGet.setHeaders(headerList.toArray(new Header[]{}));

		HttpEntity httpEntity = null;
		try {
			if(params == null){
				params = new ArrayList<>();
			}
			params.add(new BasicNameValuePair("uid", uid));
			params.add(new BasicNameValuePair("langx", langx));
			String paramsStr = EntityUtils.toString(new UrlEncodedFormEntity(params));

			httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + paramsStr));

			HttpResponse httpResponse = httpClient.execute(httpGet);

			StatusLine statusLine = httpResponse.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if(statusCode == 200){
				httpEntity = httpResponse.getEntity();
				if(httpEntity != null){
					String content = EntityUtils.toString(httpEntity, "UTF-8");
					LOGGER.info("皇冠体育数据： " + content);
					boolean checkResponse = this.checkResponse(content);
					if(checkResponse == true){
						return content;
					}
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

	/**
	 * 皇冠体育抓去和足球相关的数据.
	 * 
	 * @param rtype
	 * @param page_no
	 * @param league_id
	 * @param hot_game
	 * @return
	 */
	@Deprecated
	public String ftBrowseBodyVar(String rtype, String page_no, String league_id, String hot_game){
		if(uid == null || "".equals(uid)){
			boolean loginResult = this.login();
			if(loginResult == false){
				return null;
			}
		}

		HttpGet httpGet = new HttpGet(hgHost + "");

		List<Header> headerList = new ArrayList<Header>();
		headerList.add(new BasicHeader("User-Agent", USER_AGENT));
		headerList.add(new BasicHeader("Connection", "close")); // 短链接
		httpGet.setHeaders(headerList.toArray(new Header[]{}));

		HttpEntity httpEntity = null;
		try {
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("uid", uid));
			params.add(new BasicNameValuePair("mtype", mtype));
			params.add(new BasicNameValuePair("langx", langx));
			params.add(new BasicNameValuePair("rtype", rtype));
			params.add(new BasicNameValuePair("page_no", page_no));
			params.add(new BasicNameValuePair("league_id", league_id));
			params.add(new BasicNameValuePair("hot_game", hot_game));
			String paramsStr = EntityUtils.toString(new UrlEncodedFormEntity(params));

			httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + paramsStr));

			HttpResponse httpResponse = httpClient.execute(httpGet);

			StatusLine statusLine = httpResponse.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if(statusCode == 200){
				httpEntity = httpResponse.getEntity();
				if(httpEntity != null){
					String content = EntityUtils.toString(httpEntity, "UTF-8");
					LOGGER.info("皇冠体育数据： " + content);
					boolean checkResponse = this.checkResponse(content);
					if(checkResponse == true){
						return content;
					}
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

	private boolean checkResponse(String content){
		// 检测登陆状态
		if(content == null || "".equals(content) || content.contains("logout_warn.html")){
			this.login();
			LOGGER.info("皇冠体育尝试登录 retry login");
			return false;
		}

		// 检测是否需要域名变更
		Document document = Jsoup.parse(content);//
		Element newdomainForm = document.getElementById("newdomain");
		if(newdomainForm != null){
			String newHgHost = newdomainForm.attr("action");
			if(newHgHost != null && !"".equals(newHgHost)){
				this.hgHost = newHgHost.trim();
			}
			return false;
		}

		return true;
	}

	public String getHgUsername() {
		return hgUsername;
	}

	public String getHgPasswd() {
		return hgPasswd;
	}

	public String getUid() {
		return uid;
	}
}
