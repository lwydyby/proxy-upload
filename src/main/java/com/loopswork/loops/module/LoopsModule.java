package com.loopswork.loops.module;

import com.google.inject.AbstractModule;
import com.loopswork.loops.http.adaptor.IHttpRequestAdaptor;
import com.loopswork.loops.http.adaptor.VertHttpRequestAdaptor;
import com.loopswork.loops.http.wraper.IHttpResponseWrapper;
import com.loopswork.loops.http.wraper.VertHttpResponseWrapper;


public class LoopsModule extends AbstractModule {
  @Override
  protected void configure() {
    binder().bind(IHttpRequestAdaptor.class).to(VertHttpRequestAdaptor.class);
    binder().bind(IHttpResponseWrapper.class).to(VertHttpResponseWrapper.class);
    super.configure();
  }
}
