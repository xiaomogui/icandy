package com.sain.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.ocpsoft.prettytime.PrettyTime;

import com.jfinal.kit.StrKit;

public class SysKit {
	public static String maskStr(String str) {
		if (StrKit.isBlank(str))
			return str;

		if (str.length() < 3)
			return "***";

		return str.substring(0, 1) + "**" + str.substring(str.length() - 2);
	}

	public static boolean isAjaxInvoie(HttpServletRequest request) {
		String requestType = request.getHeader("X-Requested-With");
		return (requestType != null && requestType.equals("XMLHttpRequest"));
	}

	/**
	 * 获取ip
	 * 
	 * @param request
	 * @return
	 */
	public static String getIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Requested-For");
		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}

		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static List<Integer> getPageInfo(int cur, int pageCount, int num) {
		List<Integer> list = new ArrayList<Integer>();
		if (pageCount <= 0)
			return list;
		list.add(1);
		if (pageCount > num) {
			int n = num - 2;
			int ncur = cur - n / 2 + 1;
			if (ncur < 2) {
				ncur = 2;
			}
			if (ncur + n > pageCount) {
				ncur = pageCount - n;
			}
			for (int i = 0; i < n; i++) {
				list.add(ncur + i);
			}
			list.add(pageCount);
		} else if (pageCount >= 2) {
			for (int i = 2; i <= pageCount; i++) {
				list.add(i);
			}
		}
		return list;
	}

	public static String beautyTime(Date createTime) {
		PrettyTime pt = new PrettyTime(Locale.CHINA);
		return pt.format(createTime);
	}

	public static String getContextPath(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append(request.getScheme()).append("://").append(request.getServerName()).append(":").append(request.getServerPort()).append(request.getContextPath());
		String path = sb.toString();
		sb = null;
		return path;
	}

	/**
	 * 获取完整请求路径(含内容路径及请求参数)
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestURIWithParam(HttpServletRequest request) {
		return request.getRequestURI() + (request.getQueryString() == null ? "" : "?" + request.getQueryString());
	}

}
