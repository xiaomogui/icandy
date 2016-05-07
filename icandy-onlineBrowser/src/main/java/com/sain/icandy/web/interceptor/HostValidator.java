package com.sain.icandy.web.interceptor;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.sain.jfinal.ext.render.SpiderRender;

public class HostValidator extends Validator {
	@SuppressWarnings("unused")
	private static final Pattern hostPattern = Pattern.compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))(:[\\d]{1,5})?");

	protected void validate(Controller c) {
		String host = c.getPara("host");
		if (StringUtils.isEmpty(host)) {
			addError("hostMsg", "请输入正确格式的IP及其端口，80端口可不写");
		}
//		Matcher m = hostPattern.matcher(host);
//		if(!m.matches()){
//			addError("hostMsg", "请输入正确格式的IP及其端口，80端口可不写");
//		}
	}

	protected void handleError(Controller c) {
		String errMsg = StringUtils.defaultString(c.getAttrForStr("hostMsg"));
		c.render(SpiderRender.error(errMsg));
	}
}