package com.sain.icandy.web.interceptor;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.sain.jfinal.ext.render.SpiderRender;

public class ModifyPwdValidator extends Validator {
	protected void validate(Controller c) {
		validateRequiredString("oldpassword", "oldMsg", "请输入旧密码");
		validateRequiredString("newpassword", "newMsg", "请输入新密码");
	}

	protected void handleError(Controller c) {
		String errMsg = "";
		errMsg += StringUtils.defaultString(c.getAttrForStr("oldMsg"));
		errMsg += StringUtils.defaultString(c.getAttrForStr("newMsg"));
		c.render(SpiderRender.error(errMsg));
	}
}