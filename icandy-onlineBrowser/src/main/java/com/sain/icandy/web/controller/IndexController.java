package com.sain.icandy.web.controller;

public class IndexController extends BaseController {
	public void index() {
		render("index.ftl");
	}

	public void home() {
		render("home.ftl");
	}
}
