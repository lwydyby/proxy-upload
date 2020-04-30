package com.loopswork.loops.http.entity;

import lombok.Data;

import java.util.Map;


@Data
public class HttpResponse {

  private Map<String, String> headers;
  private int statusCode;
  private String statusMessage;
  private BodyBuffer body;
  private String bodyString;
  private int bodyLength;

}
