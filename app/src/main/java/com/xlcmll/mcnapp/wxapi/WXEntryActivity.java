package com.xlcmll.mcnapp.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xlcmll.mcnapp.LoginActivity;
import com.xlcmll.mcnapp.WebviewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

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
            Log.i("wx", "wx callback");
            SendAuth.Resp resp = (SendAuth.Resp) baseResp;
            getAccessToken(resp.code);
        } else {
            Log.i("3333", "4444");
            this.finishAffinity();
        }
    }

    private void getAccessToken(String code) {
//        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx797a2d0621b870dc&secret=c4306d9079d1ea4de0e1d6d39caee859&code=" + code + "&grant_type=authorization_code";
//        String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
        String url = "http://xlcmll.top/wx/access_token?code=" + code;

        OkHttpClient mOkHttpClient = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(url)
                //.post(body)
                .build();
        Call call = mOkHttpClient.newCall(request);
        final Context context = this;

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("getAccessToken", "fail");
                finish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                Log.i("test", jsonStr);
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    String jwt = json.getString("jwt");
                    Intent intent = new Intent(context, WebviewActivity.class);
                    intent.putExtra("jwt", jwt);
                    startActivity(intent);
                } catch (JSONException err) {
                    Log.i("Error", err.toString());
                }
//                JSONObject accessToken = JSONObject.parseObject(json, new TypeReference<AccessToken>() {
//                });
//                finish();
            }
        });
        Log.i(TAG, "getAccessToken: start");
    }

    private void getUserInfo(String access_token, String openid) {
        //        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
        Log.i(TAG, "getUserInfo: start");

        String url = "https://api.weixin.qq.com/sns/userinfo";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("access_token", access_token)
                .add("openid", openid)
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = mOkHttpClient.newCall(request);
        final Context context = this;
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                finish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
//                WXUserInfo wxUserInfo = JSONObject.parseObject(json, new TypeReference<WXUserInfo>() {
//                });//至此昵称头像全部到手，传给你家后台吧
                Log.i("test", jsonStr);
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    String unionid = json.getString("unionid");
                    Intent intent = new Intent(context, WebviewActivity.class);
                    intent.putExtra("uid", unionid);
                    startActivity(intent);
                } catch (JSONException err) {
                    Log.i("Error", err.toString());
                }
                finish();
            }
        });
    }

}
