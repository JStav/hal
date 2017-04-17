package com.stav.permissionutilssample;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.stav.hal.Hal;
import com.stav.hal.PermissionResult;
import com.stav.hal.listener.SinglePermissionResultListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class PermissionFragment extends Fragment {

  private Hal hal;

  @BindView(R.id.tv_request_fragment) TextView tvFragment;

  @OnClick(R.id.btn_request_fragment)
  public void onFragmentPermissionRequestClick() {

    SinglePermissionResultListener listener = new SinglePermissionResultListener() {
      @Override public void onSinglePermissionResult(PermissionResult result) {
        tvFragment.setText(result.getPermission() + " : " + result.isGranted());
      }
    };

    hal.withListener(listener)
        .addPermission(Manifest.permission.READ_SMS)
        .request(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_permission, container, false);
    ButterKnife.bind(this, v);

    hal = Hal.init();

    return v;
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    hal.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}
