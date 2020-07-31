package com.xlcmll.mcnapp.jsbridge;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.JavascriptInterface;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JumpWx {
    Context mContext;

    /** Instantiate the interface and set the context */
    public JumpWx (Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void jumpwx(String url) {
        String newurl = "weixin://";

        try {
            Uri uri = Uri.parse(newurl);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUrl(String url){
        OkHttpClient mOkHttpClient = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(url)
                //.post(body)
                .build();
        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("getAccessToken", "fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                HttpUrl url = response.request().url();
                List<String> segments = url.pathSegments();

                if(segments.size()>2){
                    String newurl = "snssdk1128://aweme/detail/"+segments.get(2)+"?refer=web&gd_label=click_wap_download_finish&appParam=&needlaunchlog=1";

                    try {
                        Uri uri = Uri.parse(newurl);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        mContext.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
