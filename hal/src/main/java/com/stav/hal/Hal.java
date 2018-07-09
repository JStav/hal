package com.stav.hal;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import android.support.v7.app.AppCompatActivity;
import com.stav.hal.action.Action;
import com.stav.hal.action.ActionFactory;
import com.stav.hal.listener.PermissionsResultListener;
import com.stav.hal.listener.SinglePermissionResultListener;
import com.stav.hal.logging.HalTree;

import java.util.ArrayList;
import java.util.HashSet;
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
   * Request the added permissions.
   */
  public void request(Activity activity) {
    PermissionRequestFragment fragment = createRequestFragment();

    if (activity instanceof AppCompatActivity) {
      ((AppCompatActivity) activity).getSupportFragmentManager()
          .beginTransaction()
          .add(fragment, null)
          .commit();
    } else {
      Timber.e(new ClassCastException("Activity must be an AppCompatActivity"));
    }
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

  /**
   * Must be called in Application onCreate
   * Log debug statements, only works if in debug build.
   */
  public static void enableDebugLog() {
    if(BuildConfig.DEBUG) {
      Timber.plant(new HalTree());
    }
  }

  private PermissionRequestFragment createRequestFragment() {
    PermissionRequestFragment fragment = PermissionRequestFragment.newInstance(new ArrayList<>(permissions));
    fragment.setListener(listener);
    fragment.setSinglePermissionResultListener(singlePermissionResultListener);

    return fragment;
  }
}
