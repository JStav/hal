package com.stav.hal.action;

public abstract class Action {

  private static final int ACTION_BASE = 10;
  public static final int ACTION_ENABLE_BLUETOOTH = ACTION_BASE + 1;

  public abstract void executeAction();
}
