package com.sain.jfinal.ext.render;

import java.io.IOException;
import java.io.PrintWriter;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

public class SpiderRender extends Render {
	private static final String CONTENT_TYPE = "text/html;charset=" + getEncoding();
	private static final String DEFAULT_STATUSCODE = "200";

	private String message = "";

	private String statusCode = DEFAULT_STATUSCODE;

	public SpiderRender() {
		this("");
	}

	public SpiderRender(String message) {
		this("", DEFAULT_STATUSCODE);
	}

	public SpiderRender(String message, String statusCode) {
		this.message = message;
		this.statusCode = statusCode;
	}

	public static SpiderRender error() {
		return error("操作失败");
	}

	public static SpiderRender error(String errorMsg) {
		return new SpiderRender(errorMsg, "300");
	}

	public static SpiderRender success() {
		return success("操作成功");
	}

	public static SpiderRender success(String successMsg) {
		return new SpiderRender(successMsg, DEFAULT_STATUSCODE);
	}

	public SpiderRender message(String message) {
		this.message = message;
		return this;
	}

	public SpiderRender statusCode(String statusCode) {
		this.statusCode = statusCode;
		return this;
	}

	@Override
	public void render() {
		JSONObject json = new JSONObject();
		json.put("statusCode", statusCode);
		json.put("message", message);
		PrintWriter writer = null;
		try {
			response.setHeader("Pragma", "no-cache"); // HTTP/1.0 caches might
														// not implement
														// Cache-Control and
														// might
														// only implement
														// Pragma:
														// no-cache
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType(CONTENT_TYPE);
			writer = response.getWriter();
			writer.write(json.toJSONString());
			writer.flush();
		} catch (IOException e) {
			throw new RenderException(e);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

}