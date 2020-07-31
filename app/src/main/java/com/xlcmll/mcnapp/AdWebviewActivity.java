package com.xlcmll.mcnapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;

import com.xlcmll.mcnapp.jsbridge.JumpDy;
import com.xlcmll.mcnapp.jsbridge.JumpWx;

import im.delight.android.webview.AdvancedWebView;

public class AdWebviewActivity extends Activity implements AdvancedWebView.Listener {

    private AdvancedWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_webview);

        mWebView = (AdvancedWebView) findViewById(R.id.webview);
        mWebView.setListener(this, this);

        Intent intent = getIntent();
        String code = intent.getStringExtra("code");

        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        String jwt = prefs.getString("jwt", "");
        if(jwt == ""){
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }else {
            if(code!=null){
                mWebView.loadUrl("http://www.xlcmll.top/home/#/publish/pay/" + jwt);
            }else {
                mWebView.loadUrl("http://www.xlcmll.top/home/#/user/new/" + jwt);
            }

            mWebView.addJavascriptInterface(new JumpDy(this), "Android");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                AdvancedWebView.setWebContentsDebuggingEnabled(true);
            }
        }
    }

    public static void payCallback(){

    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (!mWebView.onBackPressed()) {
            return;
        }
        // ...
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
    }

    @Override
    public void onPageFinished(String url) {
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
    }

    @Override
    public void onExternalPageRequest(String url) {
    }

}