package com.xlcmll.mcnapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.KeyEvent;
import android.view.Window;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.GetMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String APP_ID = "wx797a2d0621b870dc";

    // IWXAPI 是第三方app和微信通信的openApi接口
    public static IWXAPI api;

    private void regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);

        // 将应用的appId注册到微信
        api.registerApp(APP_ID);

        //建议动态监听微信启动广播进行注册到微信
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // 将该app注册到微信
                api.registerApp(APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));
    }

//    class LoginPagerAdapter extends FragmentPagerAdapter {
//        private ArrayList<Fragment> fragmentList = new ArrayList<>();
//
//        public LoginPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int i) {
//            return fragmentList.get(i);
//        }
//
//        @Override
//        public int getCount() {
//            return fragmentList.size();
//        }
//
//        void addFragmet(Fragment fragment) {
//            fragmentList.add(fragment);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ViewPager viewPager = findViewById(R.id.viewPager);
//
//        LoginPagerAdapter pagerAdapter = new LoginPagerAdapter(getSupportFragmentManager());
//        pagerAdapter.addFragmet(new LoginFragment());
//        viewPager.setAdapter((pagerAdapter));

        this.regToWx();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}