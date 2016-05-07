package com.sain.icandy.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.sain.icandy.exception.SpiderException;
import com.sain.icandy.model.Hgaccount;
import com.sain.icandy.service.HgaccountService;
import com.sain.icandy.web.interceptor.AuthInterceptor;
import com.sain.jfinal.ext.render.SpiderRender;

@Before(AuthInterceptor.class)
public class HgaccountController extends BaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(HgaccountController.class);

	public void index() {
		String hgusername = getPara("hgusername");
		Integer status = getParaToInt("status");
		setAttr("recordPage", HgaccountService.service.paginate(hgusername, status, getParaToInt("page", 1), 10));
		keepPara();
		render("list.ftl");
	}

	public void add() {
	}

	// @Before(HostValidator.class)
	public void addAction() {
		String hgUsername = getPara("hgusername");
		String hgPassword = getPara("hgpassword");
		String status = getPara("status");
		status = StringUtils.isEmpty(status) ? "" : status;
		Hgaccount h = new Hgaccount();
		h.setHgUsername(hgUsername);
		h.setHgPassword(hgPassword);
		h.setStatus(status);
		HgaccountService.service.save(h);
		render(SpiderRender.success("添加成功"));
	}

	public void resetStatus() {
		Integer id = getParaToInt(0);
		String status = getPara(1);
		if (id == null || id == 0 || StringUtils.isEmpty(status) || ("1".equals(status) && "2".equals(status))) {
			throw new SpiderException("参数不对");
		}
		Hgaccount h = HgaccountService.service.findById(id);
		if (h == null) {
			throw new SpiderException("皇冠账号不存在");
		}
		if (!Objects.equals(h.getStatus(), status)) {
			h.setStatus(status);
			HgaccountService.service.update(h);
		}
		render(SpiderRender.success("状态修改成功"));
	}

	public void delete() {
		HgaccountService.service.deleteById(getParaToInt(0));
		render(SpiderRender.success("皇冠账号删除成功"));
	}

	public void edit() {
		setAttr("item", HgaccountService.service.findById(getParaToInt()));
	}

	// @Before(HostValidator.class)
	public void update() {
		Hgaccount h = getModel(Hgaccount.class, "");
		Hgaccount old = HgaccountService.service.findById(h.getId());
		if (old == null) {
			throw new SpiderException("皇冠账号不存在");
		}
		old.setHgUsername(h.getHgUsername());
		old.setHgPassword(h.getHgPassword());
		old.setStatus(h.getStatus());
		HgaccountService.service.update(old);
		render(SpiderRender.success("修改成功"));
	}

	public void checkHgLogin() {
		Integer id = getParaToInt("id");
		if (id == null || id == 0) {
			throw new SpiderException("参数不对");
		}
		Hgaccount h = HgaccountService.service.findById(id);
		if (h == null) {
			throw new SpiderException("皇冠账号不存在");
		}

		String hgusername = h.getHgUsername();
		String hgpassword = h.getHgPassword();

		String testLoginStatus = "测试登录";

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			String host = "http://127.0.0.1:8080/soprtscenter";
			HttpPost httpPost = new HttpPost(host + "/sdcmanagero/testHgLogin.do");
			List<NameValuePair> params = new ArrayList<>();

			params.add(new BasicNameValuePair("hgusername", hgusername));
			params.add(new BasicNameValuePair("hgpassword", hgpassword));

			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				String e = EntityUtils.toString(response.getEntity());
				if (StringUtils.isNotEmpty(e)) {
					@SuppressWarnings("unchecked")
					Map<String, Object> json = JSONObject.parseObject(e, new HashMap<String, Object>().getClass());
					Integer code = (Integer) json.get("code");
					if(code == 200){
						testLoginStatus = (String) json.get("data");
					}
				}
			} catch (Exception e) {
				LOGGER.error("错误", e);
				testLoginStatus = "访问soprtscenter异常";
			} finally {
				response.close();
			}

		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			testLoginStatus = "访问soprtscenter异常";
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				LOGGER.error("关闭httpclient发生错误", e);
			}
		}
		Map<String, Object> json = new HashMap<>();
		json.put("testLoginStatus", testLoginStatus);

		renderText(JSONObject.toJSONString(json));
	}

	public void refreshHgaccount() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			String host = "http://127.0.0.1:8080/soprtscenter";
			HttpPost httpPost = new HttpPost(host + "/sdcmanagero/refreshHgaccount.do");
			List<NameValuePair> params = new ArrayList<>();
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
			} catch (Exception e) {
				LOGGER.error("错误", e);
			} finally {
				response.close();
			}

		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				LOGGER.error("关闭httpclient发生错误", e);
			}
		}
		Map<String, Object> json = new HashMap<>();

		renderText(JSONObject.toJSONString(json));
	}

}
