package com.stav.hal.action;

import android.content.Context;

public class EnableBluetoothAction extends Action {

  private Context context;

  public EnableBluetoothAction(Context context) {
    this.context = context;
  }

  @Override public void executeAction() {
    ActionUtils.requestEnableBluetooth(context);
  }
}
