package com.stav.permissionutilssample;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.stav.hal.Hal;
import com.stav.hal.PermissionResult;
import com.stav.hal.listener.PermissionsResultListener;
import com.stav.hal.listener.SinglePermissionResultListener;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private Hal hal;

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

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    initFragment();

    hal = Hal.init();
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
    hal.addPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        .removeListeners()
        .withListener(listener)
        .openPodBayDoors(this);
  }

  private void requestMultiple(PermissionsResultListener listener) {
    hal.addPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        .addPermission(Manifest.permission.READ_CONTACTS)
        .removeListeners()
        .withListener(listener)
        .openPodBayDoors(this);
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    hal.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}
