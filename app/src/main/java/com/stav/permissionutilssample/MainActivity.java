package com.stav.permissionutilssample;

import android.Manifest;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.stav.hal.Hal;
import com.stav.hal.PermissionResult;
import com.stav.hal.action.Action;
import com.stav.hal.listener.PermissionsResultListener;
import com.stav.hal.listener.SinglePermissionResultListener;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.tv_location_permission) TextView tvLocationPermission;
  @BindView(R.id.tv_multiple_permissions) TextView tvMultiplePermissions;

  @OnClick(R.id.btn_request_location) public void onRequestLocationClick() {

    SinglePermissionResultListener listener = new SinglePermissionResultListener() {
      @Override public void onSinglePermissionResult(PermissionResult permissions) {
        String result = buildResultString(permissions);
        tvLocationPermission.setText(result);
      }
    };

    requestSingle(listener);
  }

  @OnClick(R.id.btn_request_multiple) public void onRequestMultipleClick() {

    PermissionsResultListener listener = new PermissionsResultListener() {
      @Override public void onPermissionsResult(List<PermissionResult> permissions) {
        String result = buildResultString(permissions);
        tvMultiplePermissions.setText(result);
      }
    };

    requestMultiple(listener);
  }

  @OnClick(R.id.btn_request_enable_bt) public void onEnableBtClick() {
    Hal.request(this, Action.ACTION_ENABLE_BLUETOOTH);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    initFragment();
  }

  private void initFragment() {
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.replace(R.id.container, new PermissionFragment());
    ft.commit();
  }

  private String buildResultString(List<PermissionResult> permissionResults) {

    String permissions = "";

    for(PermissionResult result : permissionResults) {
      permissions += result.getPermission() + " : " + result.isGranted() + "\n";
    }

    return permissions;
  }

  private String buildResultString(PermissionResult result) {
    return result.getPermission() + " : " + result.isGranted();
  }

  private void requestSingle(SinglePermissionResultListener listener) {
    Hal.init().addPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        .withListener(listener)
        .request(this);
  }

  private void requestMultiple(PermissionsResultListener listener) {
    Hal.init().addPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        .addPermission(Manifest.permission.READ_CONTACTS)
        .withListener(listener)
        .request(this);
  }
}
