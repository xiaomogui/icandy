package com.sain.icandy.servicee;

import com.gc.sports.webclient.ProxyAccessWebclient;
import com.jfinal.aop.Enhancer;

public class ProxyAccessServicee {
	public static final ProxyAccessServicee service = Enhancer.enhance(ProxyAccessServicee.class);

	public String access(String link) {
		// ProxyAccessWebclient proxyAccessWebclient = ProxyAccessWebclientPool.FOOTBALLWEBCLIENT_POOL.poll();
		ProxyAccessWebclient proxyAccessWebclient = new ProxyAccessWebclient();
		String content = proxyAccessWebclient.httpGet(null, link);
		return content;
	}

}
