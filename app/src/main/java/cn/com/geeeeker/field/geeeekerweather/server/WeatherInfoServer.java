package cn.com.geeeeker.field.geeeekerweather.server;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class WeatherInfoServer {
    public static String cityName; // 城市名
    private static Geocoder geocoder; // 此对象能通过经纬度来获取相应的城市等信息

    /**
     * 通过地理坐标获取城市名 其中CN分别是city和name的首字母缩写
     *
     * @param context
     */
    public static String getCNBylocation(Context context) {
        geocoder = new Geocoder(context);
        // 用于获取Location对象，以及其他
        LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        // 实例化一个LocationManager对象
        locationManager = (LocationManager) context
                .getSystemService(serviceName);
        // provider的类型
        String provider = LocationManager.NETWORK_PROVIDER;

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        criteria.setAltitudeRequired(false); // 不要求海拔
        criteria.setBearingRequired(false); // 不要求方位
        criteria.setCostAllowed(false); // 不允许有话费
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗

        // 通过最后一次的地理位置来获得Location对象
        Location location = locationManager.getLastKnownLocation(provider);

        String queryed_name = updateWithNewLocation(location);
        if ((queryed_name != null) && (0 != queryed_name.length())) {
            cityName = queryed_name;
        }

        /*
         * 第二个参数表示更新的周期，单位为毫秒；第三个参数的含义表示最小距离间隔，单位是米 设定每30秒进行一次自动定位
         */
        locationManager.requestLocationUpdates(provider, 30000, 50,
                locationListener);
        // 移除监听器，在只有一个widget的时候，这个还是适用的
        locationManager.removeUpdates(locationListener);
        return cityName;
    }

    /**
     * 方位改变时触发，进行调用
     */
    private final static LocationListener locationListener = new LocationListener() {
        String tempCityName;

        public void onLocationChanged(Location location) {

            tempCityName = updateWithNewLocation(location);
            if ((tempCityName != null) && (tempCityName.length() != 0)) {

                cityName = tempCityName;
            }
        }

        public void onProviderDisabled(String provider) {
            tempCityName = updateWithNewLocation(null);
            if ((tempCityName != null) && (tempCityName.length() != 0)) {

                cityName = tempCityName;
            }
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    /**
     * 更新location
     *
     * @param location
     * @return cityName
     */
    private static String updateWithNewLocation(Location location) {
        String mcityName = "";
        double lat = 0;
        double lng = 0;
        List<Address> addList = null;
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        } else {
            Log.e(WeatherInfoServer.class.getName(), "无法获取地理信息");
        }

        try {
            addList = geocoder.getFromLocation(lat, lng, 1); // 解析经纬度
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addList != null && addList.size() > 0) {
            for (int i = 0; i < addList.size(); i++) {
                Address add = addList.get(i);
                mcityName += add.getLocality();
            }
        }
        if (mcityName.length() != 0) {
            return mcityName.substring(0, (mcityName.length() - 1));
        } else {
            return mcityName;
        }
    }
}
