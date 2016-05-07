package com.sain.icandy.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.sain.common.Constants;
import com.sain.common.SysKit;
import com.sain.icandy.model.User;
import com.sain.jfinal.ext.render.SpiderRender;

public class AuthInterceptor implements Interceptor {
	private static Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

	@Override
	public void intercept(Invocation inv) {
		Controller ctl = inv.getController();

		String loginPath = "/login";
		// 判断用户是否登录
		User user = (User) ctl.getSessionAttr(Constants.SESSION_KEY_USER);
		if (user == null) {
			if (SysKit.isAjaxInvoie(ctl.getRequest())) {
				ctl.render(SpiderRender.error("用户登录已超时").statusCode("301"));
				return;
			}
			ctl.redirect(loginPath);
			return;
		} else {
			try {
				inv.invoke();
			} catch (Exception e) {
				logger.error("发生错误", e);
				if (SysKit.isAjaxInvoie(ctl.getRequest())) {
					ctl.render(SpiderRender.error(e.getMessage()));
					return;
				} else {
					ctl.setAttr("errMsg", e.getMessage());
					ctl.render("/common/500.ftl");
				}
			}
		}
	}
}
