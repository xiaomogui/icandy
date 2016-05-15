package com.sain.icandy.web.controller;

import com.sain.icandy.servicee.ProxyAccessServicee;


public class IndexController extends BaseController {
	public void index() {
		render("index.ftl");
	}

	public void proxyAccess() {
		String link = getPara("link");
		String htmlContent = ProxyAccessServicee.service.access(link);
		setAttr("html", htmlContent);
		keepPara();
		render("proxyAccess.ftl");
	}
}
