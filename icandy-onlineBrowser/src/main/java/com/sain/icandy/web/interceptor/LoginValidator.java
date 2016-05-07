package com.sain.icandy.web.interceptor;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.sain.common.SysKit;
import com.sain.jfinal.ext.render.SpiderRender;

public class LoginValidator extends Validator {
	protected void validate(Controller c) {
		validateRequiredString("username", "unMsg", "请输入用户名");
		validateRequiredString("password", "pwdMsg", "请输入密码");
	}

	protected void handleError(Controller c) {
		String errMsg = "";
		errMsg += StringUtils.defaultString(c.getAttrForStr("unMsg"));
		errMsg += StringUtils.defaultString(c.getAttrForStr("pwdMsg"));
		if (SysKit.isAjaxInvoie(c.getRequest())) {
			c.render(SpiderRender.error(errMsg));
		} else {
			c.keepPara("username");
			c.setAttr("errMsg", errMsg);
			c.render("login.ftl");
		}
	}
}