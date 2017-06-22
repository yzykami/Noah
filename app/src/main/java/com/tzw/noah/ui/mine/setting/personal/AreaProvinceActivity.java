package com.tzw.noah.ui.mine.setting.personal;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.db.DBManager;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Area;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/9.
 */

public class AreaProvinceActivity extends MyBaseActivity {

    String TAG = "AreaProvinceActivity";
    AreaProvinceActivity mycontext = AreaProvinceActivity.this;
    private TextView tv_area;
    private ListView list;

    List<Boolean> selected;
    List<Area> items;
    private AreaAdapter adapter;
    private Area province;
    private Area city;
    private Area town;

    /* GPS Constant Permission */
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    /* Position */
    private static final int MINIMUM_TIME = 1000;  // 10s
    private static final int MINIMUM_DISTANCE = 1; // 50m

    /* GPS */
    private String mProviderName;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private double lat = 0.0;
    private double lng = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_settting_personal_areaprovince);

        initdata();
        findview();
        initview();
//        initLocalManager();
        doWorking();
    }


    private void initdata() {
        int townId = UserCache.getUser().areaId;
        DBManager db = new DBManager(mycontext);

        province = db.queryProvinceByTownId(townId);
        city = db.queryCityByTownId(townId);
        town = db.queryTownByTownId(townId);

        items = new ArrayList<>();
        selected = new ArrayList<>();

        List<Area> arealist = db.queryProvince();
        items = sortList(arealist, province);


        for (int i = 0; i < items.size(); i++) {
            if (province.areaId.equals(items.get(i).areaId))
                selected.add(true);
            else
                selected.add(Boolean.FALSE);
        }
        adapter = new AreaAdapter(mycontext, items, selected, AreaAdapter.mode_province);
    }

    private List<Area> sortList(List<Area> arealist, Area province) {
        List<Area> list = new ArrayList<>();
        for (Area area : arealist
                ) {
            if (area.areaId.equals(province.areaId)) {
                list.add(area);
                arealist.remove(area);
                break;
            }
        }
        for (Area area : arealist
                ) {
            list.add(area);
        }
        return list;
    }

    private void findview() {
        tv_area = (TextView) findViewById(R.id.tv_area);
        list = (ListView) findViewById(R.id.list);

        list.setAdapter(adapter);
    }

    private void initview() {

//        tv_area.setText(lng + " " + lat);
        tv_area.setText(getArea(UserCache.getUser().areaId));

        selected = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (province.areaId.equals(items.get(i).areaId))
                selected.add(true);
            else
                selected.add(Boolean.FALSE);
        }
        adapter = new AreaAdapter(mycontext, items, selected, AreaAdapter.mode_province);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                for (int i = 0; i < items.size(); i++) {
//                    selected.set(i, Boolean.FALSE);
//                }
//                selected.set(position, true);
                adapter.notifyDataSetChanged();
                Bundle bu = new Bundle();
                bu.putString("ID", items.get(position).areaId);
                startActivityForResult(100, AreaCityActivity.class, bu);
            }
        });
    }

    private String getArea(int areaId) {
        String area = "定位未开启";
//        DBManager db = new DBManager(mycontext);
//
//        Area province = db.queryProvinceByTownId(areaId);
//        Area city = db.queryCityByTownId(areaId);
//        Area town = db.queryTownByTownId(areaId);
//        area = province.areaName + " " + city.areaName + " " + town.areaName;
        return area;
    }


    private void doWorking() {

    }


    public void handle_login(View view) {
    }

    public void handle_save(View view) {
        final User user = UserCache.getUser();
        List<Param> body = new ArrayList<>();
        body.add(new Param("memberNickName", user.memberNickName));
        body.add(new Param("memberSex", user.memberSex));
        body.add(new Param("memberInterest", user.memberInterest));
        body.add(new Param("memberCharacter", user.memberCharacter));
        body.add(new Param("memberWork", user.memberWork));
        body.add(new Param("areaId", user.areaId));
        body.add(new Param("memberIntroduce", user.memberIntroduce));
        body.add(new Param("memberBirthday", Utils.String2Timestamp(user.memberBirthday)));
        NetHelper.getInstance().setUserInfo(body, new StringDialogCallback(this) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(e.getMessage());
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    UserCache.setUser(user);
                    toast("昵称修改成功");
                    finish();
                } else {
                    toast(iMsg.getMsg());
                }
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {
            finish();
        }
    }

    private void initLocalManager() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Get the best provider between gps, network and passive
        Criteria criteria = new Criteria();
        mProviderName = mLocationManager.getBestProvider(criteria, true);

        // API 23: we have to check if ACCESS_FINE_LOCATION and/or ACCESS_COARSE_LOCATION permission are granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // No one provider activated: prompt GPS
            if (mProviderName == null || mProviderName.equals("")) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

            // At least one provider activated. Get the coordinates
            switch (mProviderName) {
                case "passive":
                    mLocationManager.requestLocationUpdates(mProviderName, MINIMUM_TIME, MINIMUM_DISTANCE, locationListener);
                    Location location = mLocationManager.getLastKnownLocation(mProviderName);
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    break;

                case "network":
                    mLocationManager.requestLocationUpdates(mProviderName, MINIMUM_TIME, MINIMUM_DISTANCE, locationListener);
                    Location location1 = mLocationManager.getLastKnownLocation(mProviderName);
                    lat = location1.getLatitude();
                    lng = location1.getLongitude();
                    break;

                case "gps":
                    mLocationManager.requestLocationUpdates(mProviderName, MINIMUM_TIME, MINIMUM_DISTANCE, locationListener);
                    Location location2 = mLocationManager.getLastKnownLocation(mProviderName);

                    lat = location2.getLatitude();
                    lng = location2.getLongitude();
                    break;

            }

            // One or both permissions are denied.
        } else {

            // The ACCESS_COARSE_LOCATION is denied, then I request it and manage the result in
            // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSION_ACCESS_COARSE_LOCATION);
            }
            // The ACCESS_FINE_LOCATION is denied, then I request it and manage the result in
            // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_ACCESS_FINE_LOCATION);
            }

        }
    }

    private void aa() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(mProviderName, MINIMUM_TIME, MINIMUM_DISTANCE, locationListener);
        Location location = mLocationManager.getLastKnownLocation(mProviderName);
        tv_area.setText(location.getLatitude() + " ," + location.getLongitude());
    }

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderDisabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            // 更新当前经纬度
            tv_area.setText(location.getLatitude() + " ," + location.getLongitude());
            Log.log(TAG, location.getLatitude() + " ," + location.getLongitude());
            lat = location.getLatitude();
            lng = location.getLongitude();
            Geocoder ge = new Geocoder(mycontext);
            try {
                List<Address> listarea = ge.getFromLocation(lat, lng, 10);
                for(Address add:listarea)
                {
                    Log.log(TAG,add.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


}
