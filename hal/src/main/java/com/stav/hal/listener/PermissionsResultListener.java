package com.stav.hal.listener;

import com.stav.hal.PermissionResult;
import java.util.List;

public interface PermissionsResultListener {

  void onPermissionsResult(List<PermissionResult> permissions);

  class EmptyPermissionsResultListener implements PermissionsResultListener {
    @Override public void onPermissionsResult(List<PermissionResult> permissions) {}
  }
}
