package com.stav.hal;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import com.stav.hal.listener.PermissionsResultListener;
import com.stav.hal.listener.SinglePermissionResultListener;
import java.util.ArrayList;
import java.util.List;

public class PermissionRequestFragment extends Fragment {

  private static final String KEY_PERMISSIONS = "key_permissions";

  private PermissionsResultListener listener =
      new PermissionsResultListener.EmptyPermissionsResultListener();

  private SinglePermissionResultListener singlePermissionResultListener =
      new SinglePermissionResultListener.EmptySinglePermissionResultListener();

  public static PermissionRequestFragment newInstance(ArrayList<String> permissions) {
    Bundle args = new Bundle();
    args.putStringArrayList(KEY_PERMISSIONS, permissions);

    PermissionRequestFragment fragment = new PermissionRequestFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    List<String> permissions = getArguments().getStringArrayList(KEY_PERMISSIONS);

    if (permissions != null && !permissions.isEmpty()) {
      requestPermissions(getPermissionsAsArray(permissions), Hal.RC_PERMISSIONS);
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {

    if (requestCode == Hal.RC_PERMISSIONS) {
      List<PermissionResult> results = getPermissionResults(permissions, grantResults);
      notifyListeners(results);
      getFragmentManager().popBackStack();
    }
  }

  private List<PermissionResult> getPermissionResults(String[] permissions, int[] grantResults) {

    List<PermissionResult> permissionResults = new ArrayList<>();

    for (int i = 0; i < permissions.length; i++) {
      boolean isGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
      permissionResults.add(new PermissionResult(isGranted, permissions[i]));
    }

    return permissionResults;
  }

  private void notifyListeners(List<PermissionResult> results) {

    listener.onPermissionsResult(results);

    if (results.size() == 1) {
      PermissionResult result = results.get(0);
      singlePermissionResultListener.onSinglePermissionResult(result);
    }
  }

  private String[] getPermissionsAsArray(List<String> permissions) {
    return permissions.toArray(new String[permissions.size()]);
  }

  public void setListener(PermissionsResultListener listener) {
    this.listener = listener;
  }

  public void setSinglePermissionResultListener(
      SinglePermissionResultListener singlePermissionResultListener) {
    this.singlePermissionResultListener = singlePermissionResultListener;
  }


}
