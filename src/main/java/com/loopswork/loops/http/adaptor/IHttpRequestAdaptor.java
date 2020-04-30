package com.loopswork.loops.http.adaptor;


import com.loopswork.loops.http.entity.HttpRequest;


public interface IHttpRequestAdaptor<T> {

  HttpRequest request(T t);
}
