package com.weatherflow.permissionutils.listener;

import com.weatherflow.permissionutils.PermissionResult;
import java.util.List;

public interface PermissionsResultListener {

  void onPermissionsResult(List<PermissionResult> permissions);

  class EmptyPermissionsResultListener implements PermissionsResultListener {
    @Override public void onPermissionsResult(List<PermissionResult> permissions) {}
  }
}
