package com.sain.icandy.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.POST;
import com.sain.common.Constants;
import com.sain.icandy.model.User;
import com.sain.icandy.service.SendResultService;
import com.sain.icandy.service.UserService;
import com.sain.icandy.web.interceptor.AuthInterceptor;
import com.sain.icandy.web.interceptor.LoginValidator;
import com.sain.icandy.web.interceptor.ModifyPwdValidator;
import com.sain.jfinal.ext.render.SpiderRender;

/**
 * 原IndexController的备份
 * @author ddtt
 *
 */
@Before(AuthInterceptor.class)
public class LoginController extends BaseController {
	public void index() {
		List<Integer> list = new ArrayList<>();
		list.add(123);
		list.add(124);
		list.add(125);
		SendResultService.service.findExistIds(1, list);
		render("index.ftl");
	}

	@Clear(AuthInterceptor.class)
	@Before(GET.class)
	public void login() {
		render("login.ftl");
	}

	@Clear(AuthInterceptor.class)
	@Before({ LoginValidator.class, POST.class })
	public void loginAction() {
		String username = getPara("username");
		String password = getPara("password");
		String from = getPara("from");
		try {
			User user = UserService.service.login(username, password);
			setSessionAttr(Constants.SESSION_KEY_USER, user);
			if (StringUtils.equals(from, "dialog")) {
				render(SpiderRender.success("登录成功"));
			} else {
				redirect("/");
			}
		} catch (Exception e) {
			if (StringUtils.equals(from, "dialog")) {
				render(SpiderRender.error(e.getMessage()));
			} else {
				keepPara("username");
				setAttr("errMsg", e.getMessage());
				render("login.ftl");
			}
		}
	}

	public void home() {
		render("home.ftl");
	}

	public void logout() {
		removeSessionAttr(Constants.SESSION_KEY_USER);
		render("login.ftl");
	}

	@Before(GET.class)
	public void modifyPwd() {
		render("modifyPwd.ftl");
	}

	@Before({ ModifyPwdValidator.class, POST.class })
	public void modifyPwdAction() {
		User user = getSessionAttr(Constants.SESSION_KEY_USER);
		String oldpassword = getPara("oldpassword");
		String newpassword = getPara("newpassword");
		try {
			UserService.service.updatePwd(user, newpassword, oldpassword);
			render(SpiderRender.success("登录成功"));
		} catch (Exception e) {
			render(SpiderRender.error(e.getMessage()));
		}
	}

	@Clear(AuthInterceptor.class)
	public void loginDialog() {
		render("loginDialog.ftl");
	}
}
