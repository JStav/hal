package com.stav.permissionutils.listener;

import com.stav.permissionutils.PermissionResult;
import java.util.List;

public interface PermissionsResultListener {

  void onPermissionsResult(List<PermissionResult> permissions);

  class EmptyPermissionsResultListener implements PermissionsResultListener {
    @Override public void onPermissionsResult(List<PermissionResult> permissions) {}
  }
}
