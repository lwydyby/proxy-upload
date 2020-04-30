package com.loopswork.loops.http.wraper;


import com.loopswork.loops.http.entity.HttpResponse;

public interface IHttpResponseWrapper<T> {

  void response(HttpResponse response, T wrapped);

}
