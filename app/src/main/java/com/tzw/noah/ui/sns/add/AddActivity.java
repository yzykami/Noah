package com.tzw.noah.ui.sns.add;

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
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.permission.BaseMPermission;
import com.netease.nim.uikit.permission.MPermission;
import com.netease.nim.uikit.permission.annotation.OnMPermissionDenied;
import com.netease.nim.uikit.permission.annotation.OnMPermissionGranted;
import com.netease.nim.uikit.permission.annotation.OnMPermissionNeverAskAgain;
import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.circle.FragmentViewPagerAdapter;
import com.tzw.noah.ui.sns.friendlist.FansFragment;
import com.tzw.noah.ui.sns.friendlist.FollowFragment;
import com.tzw.noah.ui.sns.friendlist.FriendFragment;
import com.tzw.noah.ui.sns.friendlist.GroupFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import me.xiaopan.sketchsample.adapter.itemfactory.LoadMoreItemFactory;

/**
 * Created by yzy on 2017/6/30.
 */

public class AddActivity extends MyBaseActivity implements ViewPager.OnPageChangeListener{
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    Context mContext = AddActivity.this;
//    private AssemblyRecyclerAdapter adapter;

    String Tag = "AddActivity";
    int selectPage;
    List<Fragment> fragmentList = null;

    /* Position */
    private static final int MINIMUM_TIME = 1000;  // 1s
    private static final int MINIMUM_DISTANCE = 1; // 50m

    /* GPS */
    private String mProviderName;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    public double lat = 0.0;
    public double lng = 0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_add);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
        initLocalManager();
        doWorking();
    }

    private void initdata() {
        selectPage = 0;
        fragmentList = new ArrayList<>();
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            selectPage = bu.getInt("DATA");
        }
    }

    private void findview() {

    }

    private void initview() {
        fragmentList.add(new AddFriendFragment());
        fragmentList.add(new AddGroupFragment());
        FragmentViewPagerAdapter adapter =new FragmentViewPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);

        showFragment(selectPage);
        setTag(selectPage);
    }

    private void showFragment(int selected) {
        viewPager.setCurrentItem(selected,true);
    }

    protected void setupViews(final PtrClassicFrameLayout ptrFrame) {

    }

    LoadMoreItemFactory loadMoreItem;


    private void doWorking() {
    }


    public void handle_select(View v) {
        int clickindex = 0;
        for (int i = 0; i < ll.getChildCount(); i++) {
            TextView child = (TextView) ll.getChildAt(i);
            if (child.equals(v)) {
                clickindex = i;
            } else {
            }
        }
        if (clickindex == selectPage) {
            return;
        } else {
            selectPage = clickindex;
            showFragment(selectPage);
            setTag(selectPage);
        }
    }

    public void setTag(int index) {
        for (int i = 0; i < ll.getChildCount(); i++) {
            TextView child = (TextView) ll.getChildAt(i);
            if (i == index) {
                child.setTextColor(getResources().getColor(R.color.white));
                child.setBackgroundColor(getResources().getColor(R.color.transParent));
            } else {
                if (i == 0) {
                    child.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_red_border_left_round));
                } else if (i == ll.getChildCount() - 1) {
                    child.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_red_boder_right_round));
                } else {
                    child.setBackgroundColor(getResources().getColor(R.color.white));
                }
                child.setTextColor(getResources().getColor(R.color.myRed));
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {
            finish();
        }
    }


    /**
     * ************************************ 权限检查 ***************************************
     */

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPermission() {
        List<String> lackPermissions = new ArrayList<>();//AVChatManager.getInstance().checkPermission(BaseMessageActivity.this);
        lackPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        lackPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        lackPermissions = BaseMPermission.getDeniedPermissions(this, lackPermissions.toArray(new String[lackPermissions.size()]));
        if (lackPermissions.isEmpty()) {
            onBasicPermissionSuccess();
        } else {
            String[] permissions = new String[lackPermissions.size()];
            for (int i = 0; i < lackPermissions.size(); i++) {
                permissions[i] = lackPermissions.get(i);
            }
            MPermission.with(this)
                    .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                    .permissions(permissions)
                    .request();
        }
    }

    public boolean isPermission() {
        List<String> lackPermissions = new ArrayList<>();//AVChatManager.getInstance().checkPermission(BaseMessageActivity.this);
        lackPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        lackPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        lackPermissions = BaseMPermission.getDeniedPermissions(this, lackPermissions.toArray(new String[lackPermissions.size()]));
        if (lackPermissions.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private static final int BASIC_PERMISSION_REQUEST_CODE = 0x100;

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        onAudioPermissionChecked();
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        Toast.makeText(this, "定位所需权限未全部授权，无法获取！", Toast.LENGTH_SHORT).show();
        onAudioPermissionChecked();
    }

    private void onAudioPermissionChecked() {
    }

    public void initLocalManager() {
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

            mProviderName = "network";

            // At least one provider activated. Get the coordinates
            Location location = null;
            switch (mProviderName) {
                case "passive":
                    mLocationManager.requestLocationUpdates(mProviderName, MINIMUM_TIME, MINIMUM_DISTANCE, locationListener);
                    location = mLocationManager.getLastKnownLocation(mProviderName);

                    break;

                case "network":
                    mLocationManager.requestLocationUpdates(mProviderName, MINIMUM_TIME, MINIMUM_DISTANCE, locationListener);
                    location = mLocationManager.getLastKnownLocation(mProviderName);

                    break;

                case "gps":
                    mLocationManager.requestLocationUpdates(mProviderName, MINIMUM_TIME, MINIMUM_DISTANCE, locationListener);
                    location = mLocationManager.getLastKnownLocation(mProviderName);

                    break;

            }
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
            }
            // One or both permissions are denied.
        } else {
        }
    }

    private Location getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        mLocationManager.requestLocationUpdates(mProviderName, MINIMUM_TIME, MINIMUM_DISTANCE, locationListener);
        Location location = mLocationManager.getLastKnownLocation(mProviderName);
        return location;
//        tv_area.setText(location.getLatitude() + " ," + location.getLongitude());
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
//            tv_area.setText(location.getLatitude() + " ," + location.getLongitude());
            Log.log(Tag, "local = " +location.getLatitude() + " ," + location.getLongitude());
            lat = location.getLatitude();
            lng = location.getLongitude();
            Geocoder ge = new Geocoder(mContext);

            try {
                List<Address> listarea = ge.getFromLocation(lat, lng, 30);
                for (Address add : listarea) {
                    Log.log(Tag, add.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTag(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
