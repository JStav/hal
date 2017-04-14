package com.stav.permissionutilssample;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.stav.hal.Hal;
import com.stav.hal.PermissionResult;
import com.stav.hal.listener.PermissionsResultListener;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private Hal hal;

  @BindView(R.id.tv_location_permission) TextView tvLocationPermission;
  @BindView(R.id.tv_multiple_permissions) TextView tvMultiplePermissions;

  @OnClick(R.id.btn_request_location) public void onRequestLocationClick() {

    PermissionsResultListener listener = new PermissionsResultListener() {
      @Override public void onPermissionsResult(List<PermissionResult> permissions) {
        String result = buildResultString(permissions);
        tvLocationPermission.setText(result);
      }
    };

    requestLocationPermission(listener);
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
    hal = Hal.with(this);
  }

  private String buildResultString(List<PermissionResult> permissionResults) {

    String permissions = "";

    for(PermissionResult result : permissionResults) {
      permissions += result.getPermission() + " : " + result.isGranted() + "\n";
    }

    return permissions;
  }


  private void requestLocationPermission(PermissionsResultListener listener) {
    hal.addPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        .withListener(listener)
        .openPodBayDoors();
  }

  private void requestMultiple(PermissionsResultListener listener) {
    hal.addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .addPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        .addPermission(Manifest.permission.READ_CONTACTS)
        .withListener(listener)
        .openPodBayDoors();
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    hal.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}
