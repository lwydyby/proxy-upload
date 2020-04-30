package com.loopswork.loops.module;

import com.google.inject.AbstractModule;
import com.loopswork.loops.config.LoopsConfig;
import io.vertx.core.Vertx;


public class VertxModule extends AbstractModule {

  private final Vertx vertx;
  private final LoopsConfig config;

  public VertxModule(Vertx vertx, LoopsConfig config) {
    this.vertx = vertx;
    this.config = config;
  }

  @Override
  protected void configure() {
    bind(Vertx.class).toInstance(vertx);
    bind(LoopsConfig.class).toInstance(config);
    super.configure();
  }



}
