package com.loopswork.loops.handler;

import com.google.inject.Inject;
import com.loopswork.loops.http.entity.HttpRequest;
import com.loopswork.loops.http.wraper.VertHttpRequestWrapper;
import com.loopswork.loops.util.ClientManager;
import com.loopswork.loops.util.ContextKeys;
import com.loopswork.loops.util.TargetUtil;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DirectHandler implements Handler<RoutingContext> {

  @Inject
  private ClientManager clientManager;
  @Inject
  private TargetUtil targetUtil;


  @Override
  public void handle(RoutingContext context) {
    HttpRequest request = context.get(ContextKeys.HTTP_REQUEST);
    log.trace("Starting to handle request {}", request);
    upload(request, context);
  }

  private void upload(HttpRequest request, RoutingContext context) {
    HttpServerRequest req = context.request();
    // need Modify target
    TargetInfo targetInfo = targetUtil.getTargetInfo();
    HttpClient client =  clientManager.getCurrentThreadHttpClient();
    HttpClientRequest c_req = client.request(VertHttpRequestWrapper.transMethod(request.getMethod()), SocketAddress.inetSocketAddress(targetInfo.getPort(), targetInfo.getHost())
      , targetInfo.getPort(), targetInfo.getHost(), targetInfo.getRemoteUri(), res -> {
          context.response().setChunked(true);
          context.response().setStatusCode(res.statusCode());
          context.response().headers().setAll(res.headers());
          res.handler(data -> {
            req.response().write(data);
          });
          res.exceptionHandler(context::fail);
          res.endHandler((v) -> {
            req.response().end();
          });
      }).exceptionHandler(context::fail);
    c_req.headers().setAll(context.request().headers());
    c_req.setChunked(true);
    req.handler(c_req::write);
    req.endHandler((v) -> {
      c_req.end();
    });
  }




}
