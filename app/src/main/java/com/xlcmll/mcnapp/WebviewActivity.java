package com.xlcmll.mcnapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.xlcmll.mcnapp.jsbridge.JumpDy;
import com.xlcmll.mcnapp.jsbridge.JumpWx;

import static android.content.ContentValues.TAG;

public class WebviewActivity extends Activity {
    private WebView mWebView;
//    ProgressBar progressBar;
    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE=1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if(requestCode==FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage) return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String jwt = intent.getStringExtra("jwt");

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mWebView = new WebView(this);
        mWebView.loadUrl("http://www.xlcmll.top/home/#/user/new/" + jwt);
//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                // TODO Auto-generated method stub
//                super.onPageStarted(view, url, favicon);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                // TODO Auto-generated method stub
//                super.onPageFinished(view, url);
//
////                progressBar.setVisibility(View.GONE);
//            }
//        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @SuppressWarnings({"UnusedDeclaration"})
            public void openFileChooser(ValueCallback<Uri> uploadMessage) {
                mUploadMessage = uploadMessage;

                final Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }
//            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//                Log.i(TAG, "openFileChooser: aaaaa");
//
//                mUploadMessage = uploadMsg;
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("image/*");
//                WebviewActivity.this.startActivityForResult(Intent.createChooser(i,"File Chooser"), FILECHOOSER_RESULTCODE);
//
//            }
//
//            // For Android 3.0+
//            public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
//                Log.i(TAG, "openFileChooser: aaaaa");
//
//                mUploadMessage = uploadMsg;
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("*/*");
//                WebviewActivity.this.startActivityForResult(
//                        Intent.createChooser(i, "File Browser"),
//                        FILECHOOSER_RESULTCODE);
//            }
//
//            //For Android 4.1
//            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
//                Log.i(TAG, "openFileChooser: aaaaa");
//                mUploadMessage = uploadMsg;
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("image/*");
//                WebviewActivity.this.startActivityForResult( Intent.createChooser( i, "File Chooser" ), WebviewActivity.FILECHOOSER_RESULTCODE );
//
//            }
        });
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        mWebView.addJavascriptInterface(new JumpDy(this), "Android");
        mWebView.addJavascriptInterface(new JumpWx(this), "Android");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        setContentView(mWebView);
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}