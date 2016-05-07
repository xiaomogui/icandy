package com.sain.common;

import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class HtmlCrawler {
	private static Logger logger = LoggerFactory.getLogger(HtmlCrawler.class);

	public static JSONArray getJSONArray(String url, String encode) {
		return (JSONArray) getJSON(url, encode, null);
	}

	public static JSONArray getJSONArray(String url, String encode, List<Header> headers) {
		return (JSONArray) getJSON(url, encode, headers);
	}

	public static JSONObject getJSONObjectByGet(String url, String encode) {
		return (JSONObject) getJSON(url, encode, null);
	}

	// 得到JSONObject(Get方式)
	public static JSONObject getJSONObjectByGet(String url, String encode, List<Header> headers) {
		return (JSONObject) getJSON(url, encode, headers);
	}

	public static JSON getJSON(String url, String encode, List<Header> headers) {
		if ("".equals(url) || url == null) {
			return null;
		}
		CloseableHttpClient httpClient = HttpClients.createDefault();
		RequestConfig config = RequestConfig.custom().setConnectTimeout(20000).setSocketTimeout(20000).setRedirectsEnabled(true).build();
		try {
			// 利用URL生成一个HttpGet请求
			HttpGet httpGet = new HttpGet(url);

			httpGet.setConfig(config);
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");
			httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + encode);
			httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch");
			httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
			httpGet.setHeader("Cache-Control", "max-age=0");
			httpGet.setHeader("Connection", "keep-alive");
			httpGet.setHeader("Upgrade-Insecure-Requests", "1");
			if (headers != null) {
				for (Header h : headers)
					httpGet.addHeader(h);
			}
			CloseableHttpResponse httpResponse = null;
			try {
				// HttpClient发出一个HttpGet请求
				httpResponse = httpClient.execute(httpGet);
				// 得到httpResponse的状态响应码
				int statusCode = httpResponse.getStatusLine().getStatusCode();
				if (statusCode == HttpStatus.SC_OK) {
					// 得到httpResponse的实体数据
					HttpEntity httpEntity = httpResponse.getEntity();
					if (httpEntity != null) {
						try {
							Header[] h = httpResponse.getHeaders("Content-Encoding");
							if (h != null && h.length > 0 && h[0].getValue() != null && h[0].getValue().toLowerCase().indexOf("gzip") > -1) {
								String con = EntityUtils.toString(new GzipDecompressingEntity(httpEntity), encode);
								if (con.startsWith("[")) {
									return JSON.parseArray(con);
								} else {
									return JSON.parseObject(con);
								}
							} else {
								String con = EntityUtils.toString(httpEntity, encode);
								if (con.startsWith("[")) {
									return JSON.parseArray(con);
								} else {
									return JSON.parseObject(con);
								}
							}
						} catch (Exception e) {
							logger.error("访问【" + url + "】获取内容发生错误", e);
						}
					}
				}
			} catch (Exception e) {
				logger.error("访问【" + url + "】获取内容发生错误11", e);
			} finally {
				try {
					httpResponse.close();
				} catch (IOException e) {
				}
			}
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
			}
		}
		return null;
	}

	/**
	 * 抓取网页源代码
	 * 
	 * @param url
	 *            网页链接
	 * @return
	 */
	public static String getHtml(String url) {
		String html = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		RequestConfig config = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).setRedirectsEnabled(true).build();
		HttpGet httpget = new HttpGet(url);
		httpget.setConfig(config);
		httpget.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0)");
		httpget.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=gb2312");
		CloseableHttpResponse responce = null;
		try {
			responce = httpClient.execute(httpget);
			int resStatu = responce.getStatusLine().getStatusCode();
			if (resStatu == HttpStatus.SC_OK) {
				HttpEntity entity = responce.getEntity();
				if (entity != null) {
					html = EntityUtils.toString(entity);
				}
			}
		} catch (Exception e) {
			logger.error("访问【" + url + "】出现异常!", e);
		} finally {
			try {
				responce.close();
				httpClient.close();
			} catch (IOException e) {
			}
		}
		return html;
	}

}
