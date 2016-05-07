package com.sain.jfinal.ext.xss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.handler.Handler;

/**
 * 统一XSS处理
 * @author L.cm
 * @date 2014-5-5 上午9:11:10
 */
public class XssHandler extends Handler
{

	// 排除的url，使用的target.startsWith匹配的
	private String exclude;

	public XssHandler(String exclude)
	{
		this.exclude = exclude;
	}

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled)
	{
		if (target.indexOf(".") > 0 || (StringUtils.isNotBlank(target) && target.startsWith(exclude)))
		{
			next.handle(target, request, response, isHandled);
		}
		else
		{
			request = new HttpServletRequestWrapper(request);
			next.handle(target, request, response, isHandled);
		}
	}
}
