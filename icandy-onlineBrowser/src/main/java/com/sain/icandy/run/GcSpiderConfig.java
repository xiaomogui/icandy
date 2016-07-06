package com.sain.icandy.run;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.kit.PropKit;
import com.jfinal.log.LogManager;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.FreeMarkerRender;
import com.sain.common.SysKit;
import com.sain.icandy.model._MappingKit;
import com.sain.icandy.timer.SysTimer;
import com.sain.jfinal.ext.MyFakeStaticHandler;
import com.sain.jfinal.ext.SessionHandler;
import com.sain.jfinal.ext.log.Slf4jLogFactory;
import com.sain.jfinal.ext.xss.XssHandler;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

public class GcSpiderConfig extends JFinalConfig {
	private static Logger logger = LoggerFactory.getLogger(GcSpiderConfig.class);

	@Override
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用PropKit.get(...)获取值
		PropKit.use("application.properties");
		me.setDevMode(PropKit.getBoolean("devMode", false));
		me.setFreeMarkerViewExtension("ftl");
		LogManager.me().setDefaultLogFactory(new Slf4jLogFactory());
	}

	@Override
	public void configHandler(Handlers me) {
		// 去除.html
		me.add(new MyFakeStaticHandler());
		// xss 过滤
		me.add(new XssHandler(""));
		// 去掉 jsessionid 防止找不到action
		me.add(new SessionHandler());
		me.add(new DruidStatViewHandler("/druid"));

		me.add(new ContextPathHandler("ctx"));// Context_Path 变量设置
	}

	@Override
	public void configInterceptor(Interceptors interceptors) {
		// 让 模版 可以使用session
		interceptors.addGlobalActionInterceptor(new SessionInViewInterceptor());
	}

	@Override
	public void configPlugin(Plugins plugins) {

		/*
		// 配置druid连接池
		DruidPlugin druidDefault = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim(), PropKit.get("driver"));
		// StatFilter提供JDBC层的统计信息
		druidDefault.addFilter(new StatFilter());
		// WallFilter的功能是防御SQL注入攻击
		WallFilter wallDefault = new WallFilter();
		wallDefault.setDbType("mysql");
		druidDefault.addFilter(wallDefault);

		druidDefault.setInitialSize(PropKit.getInt("db.default.poolInitialSize"));
		druidDefault.setMaxPoolPreparedStatementPerConnectionSize(PropKit.getInt("db.default.poolMaxSize"));
		druidDefault.setTimeBetweenConnectErrorMillis(PropKit.getInt("db.default.connectionTimeoutMillis"));
		druidDefault.setMinIdle(PropKit.getInt("db.default.minIdle"));
		druidDefault.setMaxActive(PropKit.getInt("db.default.maxActive"));
		druidDefault.setMaxWait(PropKit.getInt("db.default.maxWait"));
		druidDefault.setTimeBetweenEvictionRunsMillis(PropKit.getInt("db.default.timeBetweenEvictionRunsMillis"));
		druidDefault.setMinEvictableIdleTimeMillis(PropKit.getInt("db.default.minEvictableIdleTimeMillis"));
		druidDefault.setValidationQuery(PropKit.get("db.default.validationQuery"));
		druidDefault.setTestWhileIdle(PropKit.getBoolean("db.default.testWhileIdle"));
		druidDefault.setTestOnBorrow(PropKit.getBoolean("db.default.testOnBorrow"));
		druidDefault.setTestOnReturn(PropKit.getBoolean("db.default.testOnReturn"));
		plugins.add(druidDefault);

		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidDefault);
		_MappingKit.mapping(arp);
		arp.setShowSql(PropKit.getBoolean("db.show.sql", false));// 这句话就是ShowSql
		plugins.add(arp);
		*/

		plugins.add(new EhCachePlugin());

	}

	@Override
	public void configRoute(Routes routes) {
		routes.add(new ControllerRoute());
	}

	public void afterJFinalStart() {
		Properties p = loadPropertyFile("freemarker.properties");
		FreeMarkerRender.setProperties(p);
		FreeMarkerRender.getConfiguration().setServletContextForTemplateLoading(JFinal.me().getServletContext(), PropKit.get("base.view.path", "/WEB-INF/views"));
		try {
			@SuppressWarnings("deprecation")
			BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
			TemplateHashModel staticModels = wrapper.getStaticModels();
			TemplateHashModel statics = (TemplateHashModel) staticModels.get(SysKit.class.getName());
			FreeMarkerRender.getConfiguration().setSharedVariable("sysKit", statics);
		} catch (TemplateModelException e) {
			logger.error("设置freemarker共享变量发生错误", e);
		}

		// 启动系统定时器
		if(PropKit.getBoolean("system.timer.start", false)){
			new SysTimer().start();
		}
	}

	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目 运行此 main
	 * 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		JFinal.start("web", 8081, "/", 5);
	}
}
