package com.loopswork.loops.handler;

import lombok.Data;


@Data
public class TargetInfo {
  private String host;
  private int port;
  private String remoteUri;
  private String upstreamId;
  private String targetId;
}
