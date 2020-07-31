package com.xlcmll.mcnapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bytedance.sdk.open.aweme.authorize.model.Authorization;
import com.bytedance.sdk.open.douyin.DouYinOpenApiFactory;
import com.bytedance.sdk.open.douyin.api.DouYinOpenApi;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button button = this.findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wechat_sdk_demo_test";
                MainActivity.api.sendReq(req);
                finish();
            }
        });

        final DouYinOpenApi douyinOpenApi = DouYinOpenApiFactory.create(this);
        final Button dyloginButton = this.findViewById(R.id.dylogin);

        dyloginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here

                Authorization.Request request = new Authorization.Request();
                request.scope = "user_info";                          // 用户授权时必选权限
                request.state = "ww";                                 // 用于保持请求和回调的状态，授权请求后原样带回给第三方。
                //request.callerLocalEntry = "com.xxx.xxx...activity";
                douyinOpenApi.authorize(request);
                finish();
            }
        });
    }
}