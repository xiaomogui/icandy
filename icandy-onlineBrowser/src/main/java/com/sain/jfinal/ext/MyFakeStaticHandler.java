package com.sain.jfinal.ext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

public class MyFakeStaticHandler extends Handler {

  public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
    if ("/".equals(target) || target.indexOf('.') == -1) {
      next.handle(target, request, response, isHandled);
      return;
    }
    if (target.indexOf("/sys/") > -1) {
      int index = target.lastIndexOf(".html");
      if (index != -1)
        target = target.substring(0, index);
      next.handle(target, request, response, isHandled);
    } else {
      next.handle(target, request, response, isHandled);
    }
  }
}
