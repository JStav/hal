package com.stav.hal.listener;

import com.stav.hal.PermissionResult;

public interface SinglePermissionResultListener {
  void onSinglePermissionResult(PermissionResult result);

  class EmptySinglePermissionResultListener implements SinglePermissionResultListener{
    @Override public void onSinglePermissionResult(PermissionResult result) {}
  }
}
