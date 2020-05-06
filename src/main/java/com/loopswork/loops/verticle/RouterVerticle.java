package com.loopswork.loops.verticle;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.loopswork.loops.handler.DirectHandler;
import com.loopswork.loops.handler.RequestHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@Singleton
public class RouterVerticle extends AbstractVerticle {

  @Inject
  private DirectHandler directHandler;
  @Inject
  private RequestHandler requestHandler;

  @Override
  public void start(Promise<Void> startFuture) {
    //初始化处理器
    Router router = Router.router(vertx);
    initHandlers(router);
    //增加配置初始长度，规避可能存在的uri过长被服务器拒绝的问题
    HttpServerOptions options = new HttpServerOptions();

    Router router1=Router.router(vertx);
    router1.post().handler(routingContext -> {
      routingContext.request().handler(buffer -> {
        FileOutputStream fos = null;
        // 开一个流写文件
        try {
          fos = new FileOutputStream(new File( "test.txt"),true);
          fos.write(buffer.getBytes(),0,buffer.length());
          fos.flush();
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          if (fos != null) {
            try {
              fos.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      }).endHandler(event -> {
        routingContext.response().end();
      });

    });


    //创建Http服务器
    vertx.createHttpServer(options).requestHandler(router).listen(9000, http -> {
      if (http.succeeded()) {
        startFuture.handle(Future.succeededFuture());
      } else {
        startFuture.handle(Future.failedFuture(http.cause()));
      }
    });
    vertx.createHttpServer().requestHandler(router1).listen(9001);


  }

  private void initHandlers(Router router) {
    router.route().handler(requestHandler);
    router.route().handler(directHandler);
  }


}
