package com.loopswork.loops.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.loopswork.loops.config.LoopsConfig;
import com.loopswork.loops.handler.TargetInfo;


@Singleton
public class TargetUtil {

  @Inject
  private LoopsConfig loopsConfig;


  public TargetInfo getTargetInfo() {
    TargetInfo targetInfo = new TargetInfo();
    targetInfo.setHost(loopsConfig.getHost());
    targetInfo.setPort(loopsConfig.getPort());
    targetInfo.setRemoteUri(loopsConfig.getRemoteUri());

    return targetInfo;
  }


}
