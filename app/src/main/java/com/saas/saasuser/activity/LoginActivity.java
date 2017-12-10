package com.saas.saasuser.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.tlbanner.FlyBanner;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.saas.saasuser.R;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.ClickUtils;
import com.saas.saasuser.util.CommonUtils;
import com.saas.saasuser.util.DESUitl;
import com.saas.saasuser.util.NetUtil;
import com.saas.saasuser.util.SPUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.view.ClearEditText;
import com.saas.saasuser.view.CustomProgress;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static com.saas.saasuser.R.id.enroll_vv;
import static com.saas.saasuser.R.id.forget_pwd;


public class LoginActivity extends BaseNoStatuBarActivity {
    private static String TAG = LoginActivity.class.getSimpleName();
    private ClearEditText edit_password, edit_userid;
    private SharedPreferencesUtil util;
    private JSONObject json;

    //private ImageView userimg;
    private CheckBox cb_hide_password;
    private String strUsername, strPassword;
    private Button mForgetpwd;
    private Button mEnrollvv;
//    private CheckBox mLoginchkselector;
    FlyBanner mBannerLocal;
    private Context mLoginActivity;
    //    private TextView tv_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginActivity = this;
        initLocalBanner();
        setStatus();
        ButterKnife.bind(this);
        util = new SharedPreferencesUtil(this);
        //AMapUtil.setHeadTitleMore(this, "登    录", true);

        //findViewById(R.id.head_more).setVisibility(View.GONE);
        SPUtils.put(getApplicationContext(), "is_login_self", true);
        SPUtils.put(getApplicationContext(), "is_save_psw", true);

        findViewById(R.id.btn_login).setOnClickListener(this);

        edit_userid = (ClearEditText) findViewById(R.id.ce_input_username);
        edit_password = (ClearEditText) findViewById(R.id.ce_input_password);
        cb_hide_password = (CheckBox) findViewById(R.id.cb_hide_password);
     /*   tv_more= (TextView) findViewById(tv_more);
        tv_more.setOnClickListener(this);*/
        mForgetpwd = (Button) findViewById(forget_pwd);
        mEnrollvv = (Button) findViewById(enroll_vv);
//        mLoginchkselector = (CheckBox) findViewById(R.id.login_chk_selector);

        mForgetpwd.setOnClickListener(this);
        mEnrollvv.setOnClickListener(this);

        edit_userid.setText(util.getMobilePhone());
        edit_password.setText(util.getPassWord());

        //AMapUtil.ImageLoaderToPicAuto(this, util.getPicture(), userimg);
        if(StringUtils.isNotEmpty(util.getMobilePhone(),true)&& StringUtils.isNotEmpty(util.getPassWord(),true)){
//            mLoginchkselector.setChecked(true);
            cb_hide_password.setVisibility(View.VISIBLE);
            login();
        }
        edit_userid.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                edit_password.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
               /* if ("".equals(edit_userid.getText()
                        .toString())) {
                    mLoginchkselector.setChecked(false);
                }*/
            }
        });

        edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if ("".equals(edit_password.getText()
                        .toString())) {
                    cb_hide_password.setVisibility(View.INVISIBLE);
                }else {
                    cb_hide_password.setVisibility(View.VISIBLE);
                }

            }
        });

        cb_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    edit_password.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    edit_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
            }
        });


    }



    private void initLocalBanner() {
        mBannerLocal = (FlyBanner) findViewById(R.id.banner_1);

        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.a);
        images.add(R.drawable.b);
        images.add(R.drawable.c);

        mBannerLocal.setImages(images);

        mBannerLocal.setOnItemClickListener(new FlyBanner.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //  toast("点击了第"+position+"张图片");
            }
        });

    }

    /**
     * 透明状态栏
     */
    private void setStatus() {
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();

    }


    private void saveUser() {
        try {

            util.setUserId(json.getString("LgAccID"));
            util.setUserName(json.getString("LoginName"));
            util.setPassWord(edit_password.getText().toString());
            util.setCompanyId(json.getString("CompanyId"));
            util.setPlatformRole(json.getString("PlatformRole"));
            util.setMobilePhone(edit_userid.getText().toString());
            util.setIsLogin(true);

            startActivity(new Intent(LoginActivity.this,MainActivity.class).putExtra("showFloatView","0"));
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
//                if (mLoginchkselector.isChecked()) {
                if(ClickUtils.isFastClick()) {
                    login();
                }
               /* }else {
                    ToastUtils.MyToast(mContext, "请点击确定VV服务条款", Toast.LENGTH_SHORT);
                }*/
                break;
           /* mForgetpwd = (Button) findViewById(R.id.forget_pwd);
            mEnrollvv = (Button) findViewById(R.id.enroll_vv);*/
            case forget_pwd:
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
                break;
            case enroll_vv:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }

    }


    private NetIntentCallBackListener.INetIntentCallBack intentCallBack = new NetIntentCallBackListener.INetIntentCallBack() {

        @Override
        public void onError(Request request, Exception e) {
            if(dialog!=null){
                dialog.dismiss();
                dialog = null;
            }
            ToastUtils.MyToast(mLoginActivity, "网络出错或服务器繁忙,请重新操作~", Toast.LENGTH_SHORT);
        }

        @Override
        public void onResponse(String response) {
            if(dialog!=null){
                dialog.dismiss();
                dialog = null;
            }

            try {
                JSONObject jsonObject = new JSONObject(response);
               // ToastUtils.showShortToastSafe(LoginActivity.this, "返回数据："+jsonObject.toString());
                if (StringUtils.equals(jsonObject.getString("ErrCode"),"1")) {
                    if (jsonObject.has("Data")) {
                        json = jsonObject.getJSONObject("Data");
                        //Log.e("info===>", jsonObject.toString());
                    }
                    saveUser();

                }else {
                    ToastUtils.showShortToast(LoginActivity.this,jsonObject.getString("ErrMsg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private void login() {
        if (!NetUtil.isNetworkConnected(this)) {
            ToastUtils.showShortToastSafe(LoginActivity.this, "您的设备尚未连接到网络！");
            return;
        } else {
            strUsername = edit_userid.getText().toString();
            strPassword = edit_password.getText().toString();
            if (StringUtils.isPhone(strUsername)
                    && (StringUtils.getLength(strPassword, true) >= 6
                    && 16 >= StringUtils.getLength(strPassword, true))) {
                if(dialog == null){
                    dialog = CustomProgress.show(this, "登录中..", true, null);
                }

                NetIntent intent = new NetIntent();
                intent.client_login(strUsername, DESUitl.tran2cypher(strPassword), CommonUtils.getImei(getApplicationContext(),"")+strUsername,new NetIntentCallBackListener(intentCallBack));

            } else if (!StringUtils.isPhone(strUsername)) {
                ToastUtils.showShortToastSafe(LoginActivity.this,
                        "请输入11位的手机号码！");
                return;

            } else if (6 > StringUtils.getLength(strPassword, true)
                    || 17 < StringUtils.getLength(strPassword, true)) {
                ToastUtils.showShortToastSafe(LoginActivity.this,
                        "请输入6~16位的密码！");
                return;
            }

        }

    }
}
