package com.xlcmll.mcnapp.jsbridge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.xlcmll.mcnapp.WebviewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static com.xlcmll.mcnapp.MainActivity.api;

public class JumpDy {
    Context mContext;

    /** Instantiate the interface and set the context */
    public JumpDy (Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void jumpdy(String url) {
        getUrl(url);
    }

    @JavascriptInterface
    public void jumpwx() {
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

    @JavascriptInterface
    public void saveimage(String base64str) {
        Log.i("ttttt", "saveimage: "+base64str);
        File image = null;
        Bitmap bitmap = decodeBase64Profile(base64str);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".png";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException ex) {
            // Error occurred while creating the File
            Log.e(TAG, "jumpwx: " + ex.toString() );
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG,100, fOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally{
            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {

            MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                    image.getAbsolutePath(), imageFileName, null);
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(image);
        Log.i("tttt", contentUri.toString());
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
    }

    @JavascriptInterface
    public void pay(String base64str) {
        try {
            JSONObject json = new JSONObject(base64str);
            PayReq request = new PayReq();
            request.appId = json.getString("appId");
            request.partnerId = json.getString("partnerId");
            request.prepayId= json.getString("prepayId");
            request.packageValue = json.getString("packageValue");
            request.nonceStr=json.getString("nonceStr");
            request.timeStamp= json.getString("timeStamp");
            request.sign= json.getString("sign");
            Log.i(TAG, "pay: "+request.sign);
            api.sendReq(request);
        }catch (JSONException err){
            Log.i("Error", err.toString());
        }
    }

    public static Bitmap decodeBase64Profile(String input) {
        input = input.substring(input.indexOf(",")+1);
        Bitmap bitmap = null;
        if (input != null) {
            byte[] decodedByte = Base64.decode(input, 0);
            bitmap = BitmapFactory
                    .decodeByteArray(decodedByte, 0, decodedByte.length);
        }
        return bitmap;
    }

    public static Bitmap convertBLOB2Bitmap(byte[] blob) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap tmp = BitmapFactory.decodeByteArray(blob, 0, blob.length, options);
        return tmp;
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
