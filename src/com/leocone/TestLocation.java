package com.leocone;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class TestLocation {
    static final String TAG = "TestLocation";
    private Context mContext;

    private LocationManager mLocationManager;
    private String mProvider;
    private TextView mTextView;
    private String mString = "";

    public TestLocation(Context context, TextView textview) {
        this.mContext = context;
        mTextView = textview;
        setLatitudeAndLongitude();
    }

    public void setLatitudeAndLongitude() {
        // TODO Auto-generated method stub
        // 获取 LocationManager 服务
        mLocationManager = (LocationManager) (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);
        // locationManager.setTestProviderEnabled("gps", true);
        // 获取 Location Provider
        getProvider();
        // 如果未设置位置源，打开 GPS 设置界面
        openGPS();
        // 获取位置

        // 显示位置信息到文字标签
        updateWithNewLocation(mLocationManager.getLastKnownLocation(mProvider));
        // 注册监听器 locationListener ，第 2 、 3 个参数可以控制接收 gps 消息的频度以节省电力。第 2 个参数为毫秒，
        // 表示调用 listener 的周期，第 3 个参数为米 , 表示位置移动指定距离后就调用 listener

    }

    // Gps 消息监听器
    private final LocationListener mlocationListener = new LocationListener() {

        // 位置发生改变后调用
        public void onLocationChanged(Location location) {
            mString += "\n\n onLocationChanged";
            mTextView.setText(mString);
            updateWithNewLocation(location);
        }

        // provider 被用户关闭后调用
        public void onProviderDisabled(String provider) {
            mString += "\n onProviderDisabled";
            mTextView.setText(mString);
            updateWithNewLocation(null);
        }

        // provider 被用户开启后调用
        public void onProviderEnabled(String provider) {
            mString += "\n onProviderEnabled";
            mTextView.setText(mString);
        }

        // provider 状态变化时调用
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private void updateWithNewLocation(Location location) {
        mString += "\n time:"+ System.currentTimeMillis();
        if (location != null) {
            mString += "\n getLatitude :"+location.getLatitude();
            mString += "\n getLongitude :"+location.getLongitude();

            Log.v(TAG, "updateWithNewLocation() getLatitude :"+location.getLatitude());
            Log.v(TAG, "updateWithNewLocation() getLongitude :"+location.getLongitude());
        } else {
            mString += "\n mLocation is null";
            Log.v(TAG, "mLocation is null");
        }
        mTextView.setText(mString);
        
        // TODO Auto-generated method stub
//        while (location == null) {
            Log.v(TAG, "updateWithNewLocation() requestLocationUpdates");
            mLocationManager.requestLocationUpdates(mProvider, 100, 0,
                    mlocationListener);
//        }
            if(mString.length() > 200) {
                mString = "";
            }
    }

    private void openGPS() {
        // TODO Auto-generated method stub
        Log.v(TAG, "gps provider:"+mLocationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER));
        Log.v(TAG, "network provider:"+mLocationManager
                        .isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER));
        if (mLocationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
                || mLocationManager
                        .isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
            mString += "\n  位置源已设置！ ";
            Toast.makeText(mContext, " 位置源已设置！ ", Toast.LENGTH_SHORT).show();
            mTextView.setText(mString);
            return;
        }
        Toast.makeText(mContext, " 位置源未设置！", Toast.LENGTH_SHORT).show();
        mString += "\n  位置源未设置！ ";
        mTextView.setText(mString);
    }

    private void getProvider() {
        // TODO Auto-generated method stub
        // 构建位置查询条件
        Criteria criteria = new Criteria();
        // 查询精度
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 是否查询海拨
        criteria.setAltitudeRequired(true);
        // 是否查询方位角
        criteria.setBearingRequired(true);
        // 是否允许付费
        criteria.setCostAllowed(false);
        // 电量要求
        criteria.setPowerRequirement(Criteria.ACCURACY_COARSE);
        // 返回最合适的符合条件的 provider ，第 2 个参数为 true 说明 , 如果只有一个 provider 是有效的 , 则返回当前
        // provider
        mProvider = mLocationManager.getBestProvider(criteria, true);
        mString += "\n getProvider():"+ mProvider;
        mTextView.setText(mString);
        Log.v(TAG, mString );
    }
}
