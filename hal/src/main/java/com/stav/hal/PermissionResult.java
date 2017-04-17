package com.stav.hal;


public class PermissionResult {
  private boolean granted;
  private String permission;

  public PermissionResult(boolean granted, String permission) {
    this.granted = granted;
    this.permission = permission;
  }

  public boolean isGranted() {
    return granted;
  }

  public String getPermission() {
    return permission;
  }
}
