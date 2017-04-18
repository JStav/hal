package com.stav.hal.action;

import android.content.Context;

public class ActionFactory {
  private Context context;

  public ActionFactory(Context context) {
    this.context = context;
  }

  public Action generate(int actionCode) {
    switch (actionCode) {
      case Action.ACTION_ENABLE_BLUETOOTH:
        return new EnableBluetoothAction(context);
    }

    return null;
  }

}
