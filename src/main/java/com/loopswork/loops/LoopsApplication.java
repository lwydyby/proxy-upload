package com.loopswork.loops;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.loopswork.loops.config.LoopsConfig;
import com.loopswork.loops.module.LoopsModule;
import com.loopswork.loops.module.VertxModule;
import com.loopswork.loops.util.ConfigUtil;
import com.loopswork.loops.verticle.RouterVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.logging.SLF4JLogDelegateFactory;

import java.io.BufferedReader;

import static io.vertx.core.logging.LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME;
import static java.nio.charset.StandardCharsets.UTF_8;


public class LoopsApplication {
  private final Logger log = LoggerFactory.getLogger(LoopsApplication.class);
  private Vertx vertx;
  private Injector injector;
  private LoopsConfig config;

  public static void main(String[] args) {
    //该配置保证vertx使用logback记录日志
    System.setProperty(LOGGER_DELEGATE_FACTORY_CLASS_NAME, SLF4JLogDelegateFactory.class.getName());
    Vertx vertx = Vertx.vertx();
    //读取系统配置
    Future.<LoopsConfig>future(h -> ConfigUtil.loadConfig(vertx, h)).compose(config -> {
      //启动
      return Future.<Long>future(h -> new LoopsApplication().run(vertx, config, h));
    }).onFailure(failure -> {
      System.exit(1);
    });
  }

  /**
   * 启动服务
   *
   * @param config 启动配置
   */
  public void run(Vertx vertx, LoopsConfig config, Handler<AsyncResult<Long>> startHandler) {
    long startTime = System.currentTimeMillis();
    //显示banner
    printBanner();
    //创建Vertx
    this.vertx = vertx;
    //读取配置
    this.config = config;
    log.info(config.toString());
    log.info("config load success");
    //创建注入器
    createInjector(config);
    //初始化Managers
    Future.future(this::deployRouterVerticle).onSuccess(success -> {
      long time = System.currentTimeMillis() - startTime;
      log.info("Loops start success in {}ms have fun ^_^", time);
      startHandler.handle(Future.succeededFuture(time));
    }).onFailure(failure -> {
      log.error("Loops start error", failure);
      startHandler.handle(Future.failedFuture(failure));
    });
  }

  /**
   * 关闭服务
   */
  public void close(Handler<AsyncResult<Void>> completeHandler) {
    log.info("Loops start to close");
    vertx.close(completeHandler);
  }

  private void printBanner() {
    //加载banner
    BufferedReader reader = ResourceUtil.getReader("banner.txt", UTF_8);
    String banner = IoUtil.read(reader);
    System.out.println(banner);
  }

  private void createInjector(LoopsConfig config) {
    injector = Guice.createInjector(new VertxModule(vertx, config), new LoopsModule());
    log.info("Loops injector created");
  }





  private void deployRouterVerticle(Handler<AsyncResult<String>> completeHandler) {
    RouterVerticle routerVerticle = injector.getInstance(RouterVerticle.class);
    vertx.deployVerticle(routerVerticle, completeHandler);
  }



  public LoopsConfig getConfig() {
    return config;
  }

  public Injector getInjector() {
    return injector;
  }
}
