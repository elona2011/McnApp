package com.xlcmll.mcnapp.wxapi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xlcmll.mcnapp.AdWebviewActivity;
import com.xlcmll.mcnapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;
import static com.xlcmll.mcnapp.MainActivity.api;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

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
    public void onResp(BaseResp resp) {
        Log.i(TAG, "onResp: "+resp.errCode);

        Intent intent = new Intent(this, AdWebviewActivity.class);
        String text = "充值成功";
        intent.putExtra("code", "success");

        if(resp.errCode!= 0){
            text = "充值失败";
            intent.putExtra("code", "fail");
        }
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        startActivity(intent);
        finish();
    }
}