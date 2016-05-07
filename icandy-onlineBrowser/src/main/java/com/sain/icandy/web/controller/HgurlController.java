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
import com.sain.common.PingUtil;
import com.sain.icandy.exception.SpiderException;
import com.sain.icandy.model.Hgurl;
import com.sain.icandy.service.HgurlService;
import com.sain.icandy.web.interceptor.AuthInterceptor;
import com.sain.jfinal.ext.render.SpiderRender;

@Before(AuthInterceptor.class)
public class HgurlController extends BaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(HgurlController.class);

	public void index() {
		String hgurl = getPara("hgurl");
		String status = getPara("status");
		setAttr("recordPage", HgurlService.service.paginate(hgurl, status, getParaToInt("page", 1), 10));
		keepPara();
		render("list.ftl");
	}

	public void add() {
	}

	// @Before(HostValidator.class)
	public void addAction() {
		String hgurl = getPara("hgurl");
		String status = getPara("status");
		status = StringUtils.isEmpty(status) ? "1" : status;
		Hgurl h = new Hgurl();
		h.setHgUrl(hgurl);
		h.setStatus(status);
		HgurlService.service.save(h);
		render(SpiderRender.success("添加成功"));
	}

	public void resetStatus() {
		Integer id = getParaToInt(0);
		String status = getPara(1);
		if (id == null || id == 0 || StringUtils.isEmpty(status) || ("1".equals(status) && "2".equals(status))) {
			throw new SpiderException("参数不对");
		}
		Hgurl h = HgurlService.service.findById(id);
		if (h == null) {
			throw new SpiderException("皇冠地址不存在");
		}
		if (!Objects.equals(h.getStatus(), status)) {
			h.setStatus(status);
			HgurlService.service.update(h);
		}
		render(SpiderRender.success("状态修改成功"));
	}

	public void delete() {
		HgurlService.service.deleteById(getParaToInt(0));
		render(SpiderRender.success("皇冠地址删除成功"));
	}

	public void edit() {
		setAttr("item", HgurlService.service.findById(getParaToInt()));
	}

	// @Before(HostValidator.class)
	public void update() {
		Hgurl h = getModel(Hgurl.class, "");
		Hgurl old = HgurlService.service.findById(h.getId());
		if (old == null) {
			throw new SpiderException("皇冠地址不存在");
		}
		old.setHgUrl(h.getHgUrl());
		old.setStatus(h.getStatus());
		HgurlService.service.update(old);
		render(SpiderRender.success("修改成功"));
	}

	public void ping() {
		Integer id = getParaToInt("id");
		if (id == null || id == 0) {
			throw new SpiderException("参数不对");
		}
		Hgurl h = HgurlService.service.findById(id);
		if (h == null) {
			throw new SpiderException("皇冠地址不存在");
		}

		String hgurl = h.getHgUrl();

		String delay = PingUtil.pingDelay(hgurl);

		Map<String, Object> json = new HashMap<>();
		json.put("statusCode", "0");
		json.put("delay", delay);

		renderText(JSONObject.toJSONString(json));
	}

	public void testHgurl() {
		Integer id = getParaToInt("id");
		if (id == null || id == 0) {
			throw new SpiderException("参数不对");
		}
		Hgurl h = HgurlService.service.findById(id);
		if (h == null) {
			throw new SpiderException("皇冠地址不存在");
		}

		String hgurl = h.getHgUrl();
		if(!hgurl.startsWith("http://")){
			hgurl = "http://" + hgurl;
		}
		if(hgurl.endsWith("/") && (hgurl.length() - 1) > 0){
			hgurl = hgurl.substring(0, hgurl.length() - 1);
		}

		String testHgurlStatus = "测试皇冠地址";

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			String host = "http://127.0.0.1:8080/soprtscenter";
			HttpPost httpPost = new HttpPost(host + "/sdcmanagero/testHgurl.do");
			List<NameValuePair> params = new ArrayList<>();

			params.add(new BasicNameValuePair("hgurl", hgurl));

			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				String e = EntityUtils.toString(response.getEntity());
				if (StringUtils.isNotEmpty(e)) {
					@SuppressWarnings("unchecked")
					Map<String, Object> json = JSONObject.parseObject(e, new HashMap<String, Object>().getClass());
					Integer code = (Integer) json.get("code");
					if(code == 200){
						testHgurlStatus = (String) json.get("data");
					}
				}
			} catch (Exception e) {
				LOGGER.error("错误", e);
				testHgurlStatus = "测试异常";
			} finally {
				response.close();
			}

		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			testHgurlStatus = "测试异常";
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				LOGGER.error("关闭httpclient发生错误", e);
			}
		}
		Map<String, Object> json = new HashMap<>();
		json.put("testHgurlStatus", testHgurlStatus);

		renderText(JSONObject.toJSONString(json));
	}

	public void refreshHgurl() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			String host = "http://127.0.0.1:8080/soprtscenter";
			HttpPost httpPost = new HttpPost(host + "/sdcmanagero/refreshHgurl.do");
			List<NameValuePair> params = new ArrayList<>();
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				/*
				String e = EntityUtils.toString(response.getEntity());
				if (StringUtils.isNotEmpty(e)) {
					@SuppressWarnings("unchecked")
					Map<String, Object> json = JSONObject.parseObject(e, new HashMap<String, Object>().getClass());
					Integer code = (Integer) json.get("code");
					if(code != 200){
					}
				}
				*/
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
