package com.sain.icandy.servicee;

import java.util.List;

import org.apache.http.Header;

import com.gc.sports.webclient.ProxyAccessWebclient;
import com.gc.sports.webclient.ProxyAccessWebclient.ProxyAccessWebclientResponse;
import com.jfinal.aop.Enhancer;

public class ProxyAccessServicee {
	public static final ProxyAccessServicee service = Enhancer.enhance(ProxyAccessServicee.class);

	public String access(String link) {
		// ProxyAccessWebclient proxyAccessWebclient = ProxyAccessWebclientPool.FOOTBALLWEBCLIENT_POOL.poll();
		ProxyAccessWebclient proxyAccessWebclient = new ProxyAccessWebclient();
		ProxyAccessWebclientResponse proxyAccessWebclientResponse = proxyAccessWebclient.httpGet(null, link);
		String content = proxyAccessWebclientResponse == null ? "" : proxyAccessWebclientResponse.getContent();
		return content;
	}

	public ProxyAccessWebclientResponse access(String link, List<Header> headerList) {
		// ProxyAccessWebclient proxyAccessWebclient = ProxyAccessWebclientPool.FOOTBALLWEBCLIENT_POOL.poll();
		ProxyAccessWebclient proxyAccessWebclient = new ProxyAccessWebclient();
		ProxyAccessWebclientResponse proxyAccessWebclientResponse = proxyAccessWebclient.httpGet(null, headerList, link);
		return proxyAccessWebclientResponse;
	}

}
