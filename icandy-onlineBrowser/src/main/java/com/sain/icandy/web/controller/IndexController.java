package com.sain.icandy.web.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import com.gc.sports.webclient.ProxyAccessWebclient.ProxyAccessWebclientResponse;
import com.sain.icandy.servicee.ProxyAccessServicee;


public class IndexController extends BaseController {
	public void index() {
		render("index.ftl");
	}

	public void proxyAccess() {
		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();
		Enumeration<String> headerNames = request.getHeaderNames();
		List<Header> headerList = new ArrayList<Header>();
		while(headerNames.hasMoreElements()){
			String headerName = headerNames.nextElement();
			if(!"host".equals(headerName.toLowerCase())){
				String headerValue = request.getHeader(headerName);

				headerList.add(new BasicHeader(headerName, headerValue));
			}
		}

		// Cookie[] cookies = request.getCookies();

		String link = getPara("link");
		ProxyAccessWebclientResponse proxyAccessWebclientResponse = ProxyAccessServicee.service.access(link, headerList);
		String htmlContent = "";
		if(proxyAccessWebclientResponse != null){
			htmlContent = proxyAccessWebclientResponse.getContent();
			Header[] headers = proxyAccessWebclientResponse.getHeaders();
			int headersLength;
			if(headers != null && (headersLength = headers.length) > 0){
				for(int i = 0; i < headersLength; i++){
					Header header = headers[i];
					response.setHeader(header.getName(), header.getValue());
				}
			}
		}
		setAttr("html", htmlContent);
		keepPara();
		render("proxyAccess.ftl");
	}
}
