package com.stav.hal;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.Fragment;
import com.stav.hal.action.Action;
import com.stav.hal.action.ActionFactory;
import com.stav.hal.listener.PermissionsResultListener;
import com.stav.hal.listener.SinglePermissionResultListener;
import com.stav.hal.logging.HalTree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import timber.log.Timber;

public class Hal {

  private static final int BASE = 10;
  public static final int RC_PERMISSIONS = BASE + 1;

  private PermissionsResultListener listener;
  private SinglePermissionResultListener singlePermissionResultListener;

  private Set<String> permissions;

  private Hal() {
    permissions = new HashSet<>();
    listener = new PermissionsResultListener.EmptyPermissionsResultListener();
    singlePermissionResultListener = new SinglePermissionResultListener.EmptySinglePermissionResultListener();
  }

  public static Hal init() {
    return new Hal();
  }

  public Hal withListener(PermissionsResultListener listener) {

    if(listener == null) {
      this.listener = new PermissionsResultListener.EmptyPermissionsResultListener();
    } else {
      this.listener = listener;
    }

    return this;
  }

  public Hal withListener(SinglePermissionResultListener singlePermissionResultListener) {

    if(singlePermissionResultListener == null) {
      this.singlePermissionResultListener = new SinglePermissionResultListener.EmptySinglePermissionResultListener();
    } else {
      this.singlePermissionResultListener = singlePermissionResultListener;
    }

    return this;
  }

  public Hal removeListeners() {
    listener = new PermissionsResultListener.EmptyPermissionsResultListener();
    singlePermissionResultListener = new SinglePermissionResultListener.EmptySinglePermissionResultListener();
    return this;
  }

  /** Add a permission to request. Must be a dangerous permission to be
   * included in the pop up dialog.
   * @param permission One of {@link Manifest.permission}
   * @return Hal instance
   */
  public Hal addPermission(String permission) {
    permissions.add(permission);
    return this;
  }

  /**
   * Request the added permissions. Make sure to override {@link Activity#onRequestPermissionsResult(int, String[], int[])}
   * and reroute its calls to Hal's {@link Hal#onRequestPermissionsResult(int, String[], int[])}.
   * @return Hal instance
   */
  public Hal request(Activity activity) {
    ActivityCompat.requestPermissions(activity, getPermissionsAsArray(), RC_PERMISSIONS);
    return this;
  }

  /**
   * Requests permissions in a fragment. Make sure to call super {@link Activity#onRequestPermissionsResult(int, String[], int[])}
   * if overridden in Activity.
   * @param fragment fragment
   * @return Hal instance
   */
  public Hal request(Fragment fragment) {
    fragment.requestPermissions(getPermissionsAsArray(), RC_PERMISSIONS);
    return this;
  }

  /**
   * Execute a HAL {@link Action} based on the action code passed in.
   * @param context Context
   * @param actionCode Action desired
   */
  public static void request(Context context, int actionCode) {
    new ActionFactory(context)
        .generate(actionCode)
        .executeAction();
  }

  /** Same thing as {@link Hal#request(Activity)}, but more thematic given the library's name
   * @return Hal instance
   */
  public Hal openPodBayDoors(Activity activity) {
    return request(activity);
  }

  /** Same thing as {@link Hal#request(Fragment)}, but more thematic given the library's name
   * @return Hal instance
   */
  public Hal openPodBayDoors(Fragment fragment) {
    return request(fragment);
  }

  /** Intercept {@link Activity#onRequestPermissionsResult(int, String[], int[])}
   */
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    if(requestCode == RC_PERMISSIONS) {
      List<PermissionResult> permissionResults = getPermissionResults(permissions, grantResults);
      notifyListeners(permissionResults);
    }

    this.permissions.clear();
  }

  /**
   * Must be called in Application onCreate
   * Log debug statements, only works if in debug build.
   */
  public static void enableDebugLog() {
    if(BuildConfig.DEBUG) {
      Timber.plant(new HalTree());
    }
  }

  private String[] getPermissionsAsArray() {
    return permissions.toArray(new String[permissions.size()]);
  }

  private List<PermissionResult> getPermissionResults(String[] permissions, int[] grantResults) {

    List<PermissionResult> permissionResults = new ArrayList<>();

    for(int i = 0; i < permissions.length; i++) {
      boolean isGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
      permissionResults.add(new PermissionResult(isGranted, permissions[i]));
    }

    return permissionResults;
  }

  private void notifyListeners(List<PermissionResult> results) {

    listener.onPermissionsResult(results);

    if(results.size() == 1) {
      PermissionResult result = results.get(0);
      singlePermissionResultListener.onSinglePermissionResult(result);
    }
  }

}
