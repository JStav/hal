# hal

1. Generate a HAL instance and attach it to your activity

```java
  Hal hal;
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    hal = Hal.with(this);
  }
```

2. Reroute onRequestPermissionResults callback to HAL
```java 
  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    hal.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
```

3. Write your listener
```java
    PermissionsResultListener listener = new PermissionsResultListener() {
      @Override public void onPermissionsResult(List<PermissionResult> permissions) {
        // do something 
      }
    };
```

The PermissionResult object contains the permission that was requested, and the result of the request.

Getters are as follow:
```java
  getPermission();
  isGranted();
```

4. Add desired permissions, register listener, and request.
```java
    hal.addPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        .addPermission(Manifest.permission.READ_CONTACTS)
        .withListener(listener)
        .request();
```



You can add one or many permissions.
