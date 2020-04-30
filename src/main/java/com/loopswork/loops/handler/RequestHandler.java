package com.loopswork.loops.handler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.loopswork.loops.http.adaptor.IHttpRequestAdaptor;
import com.loopswork.loops.http.entity.HttpRequest;
import com.loopswork.loops.util.ContextKeys;
import com.loopswork.loops.util.StringUtils;
import com.loopswork.loops.util.UUIDUtil;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;


@Singleton
public class RequestHandler implements Handler<RoutingContext> {

  @SuppressWarnings("rawtypes")
  @Inject
  private IHttpRequestAdaptor httpRequestAdaptor;

  @SuppressWarnings("unchecked")
  @Override
  public void handle(RoutingContext context) {
    //设置唯一标示
    context.put(ContextKeys.ID, UUIDUtil.getUUID());
    context.put(ContextKeys.TIME_START, System.currentTimeMillis());
    //处理request数据
    HttpRequest request = httpRequestAdaptor.request(context);
    //设置Host
    String host = request.getHeaders().get("Host");
    if (!StringUtils.isEmpty(host)) {
      request.setHost(host);
    }

    context.put(ContextKeys.HTTP_REQUEST, request);
    context.next();
  }
}
