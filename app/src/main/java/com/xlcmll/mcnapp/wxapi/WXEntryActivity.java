package com.xlcmll.mcnapp.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI iwxapi;
    private static final String APP_ID = "wx797a2d0621b870dc";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iwxapi = WXAPIFactory.createWXAPI(this, APP_ID, true);
        //接收到分享以及登录的intent传递handleIntent方法，处理结果
        iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        String result = "";
        if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
            SendAuth.Resp resp = (SendAuth.Resp) baseResp;
            getAccessToken(resp.code);
        } else {
            Log.i("3333", "4444");
            this.finishAffinity();
        }
    }

    private void getAccessToken(String code) {
        //        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        ///< Post方式也可以...
        //        RequestBody body = new FormBody.Builder()
        //                .add("appid", "替换为你的appid")
        //                .add("secret", "替换为你的app密钥")
        //                .add("code", code)
        //                .add("grant_type", "authorization_code")
        //                .build();
        url += "?appid=" + "替换为你的appid" + "&secret=xxxxxxxx"
                + "&code=" + code + "&grant_type=authorization_code";
        final Request request = new Request.Builder()
                .url(url)
                //.post(body)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                finish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                AccessToken accessToken = JSONObject.parseObject(json, new TypeReference<AccessToken>() {
                });
                getUserInfo(accessToken.getAccess_token(), accessToken.getOpenid());
            }
        });
    }


}
