package com.sain.icandy.web.controller;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.core.ActionException;
import com.jfinal.core.Controller;
import com.jfinal.render.RenderFactory;
import com.sain.common.DateKit;

public abstract class BaseController extends Controller {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	public Date getParaToDate(String name, String pattern) {
		return toDate(getPara(name), pattern, null);
	}

	public Date getParaToDate(String name, String pattern, Date defaultValue) {
		return toDate(getPara(name), pattern, defaultValue);
	}

	private Date toDate(String value, String pattern, Date defaultValue) {
		try {
			if (value == null || "".equals(value.trim()))
				return defaultValue;
			if (StringUtils.isEmpty(pattern)) {
				pattern = "yyyy-MM-dd";
			}
			return DateKit.parseDate(value.trim(), pattern);
		} catch (Exception e) {
			throw new ActionException(404, RenderFactory.me().getErrorRender(404),
					"Can not parse the parameter \"" + value + "\" to Date value.");
		}
	}
}
