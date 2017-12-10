//package com.saas.saasuser.activity;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.LinearLayout;
//
//import com.saas.saasuser.R;
//import com.saas.saasuser.global.NetIntent;
//import com.saas.saasuser.global.NetIntentCallBackListener;
//import com.saas.saasuser.util.SharedPreferencesUtil;
//import com.saas.saasuser.util.StringUtils;
//import com.saas.saasuser.util.ToastUtils;
//import com.saas.saasuser.util.Util;
//import com.saas.saasuser.view.CustomProgress;
//import com.squareup.okhttp.Request;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
///**
// * 内部安排
// */
//public class IndoorContactActivity extends BaseActivity implements NetIntentCallBackListener.INetIntentCallBack {
//    @BindView(R.id.head_more)
//    LinearLayout headMore;
//    private JSONObject json;
//    String orderId;
//    private SharedPreferencesUtil util;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_indoor_contact);
//        ButterKnife.bind(this);
//        util=new SharedPreferencesUtil(this);
//        Util.setHeadTitleMore(this, "内部安排", true);
//        headMore.setVisibility(View.GONE);
//
//        orderId = this.getIntent().getStringExtra("orderId");
//        new NetIntent().client_getOrderDetailById(orderId,util.getUserId(),util.getPlatformRole(), new NetIntentCallBackListener(this));//TODO  根据id去拿订单数据显示
//        if(dialog==null){
//            dialog = CustomProgress.show(this, "加载中..", true, null);
//        }
//        //ToastUtils.showShortToastSafe(IndoorContactActivity.this, "界面还没有设计好");
//
//    }
//
//    @Override
//    public void doClick(View v) {
//
//    }
//    @Override
//    public void onError(Request request, Exception e) {
//        if(dialog!=null){
//            dialog.dismiss();
//        }
//
//    }
//
//    @Override
//    public void onResponse(String response) {
//        if(dialog!=null){
//            dialog.dismiss();
//        }
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            if (StringUtils.equals(jsonObject.getString("ErrCode"), "1")) {
//                if (jsonObject.has("Data")) {
//                    json = jsonObject.getJSONObject("Data");
//                    Log.e("info===>", jsonObject.toString());
//                } else {
//                    ToastUtils.showShortToastSafe(IndoorContactActivity.this, "操作成功");
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//
//
//}
