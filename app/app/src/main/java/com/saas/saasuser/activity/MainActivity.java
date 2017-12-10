package com.saas.saasuser.activity;


import android.app.AlertDialog;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.fragment.FirstFragment;
import com.saas.saasuser.fragment.ForthFragment;
import com.saas.saasuser.fragment.SecondFragment;
import com.saas.saasuser.fragment.ThridFragment;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.CommonUtils;
import com.saas.saasuser.util.JiGuangBroadcastManager;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.TagAliasOperatorHelper;
import com.saas.saasuser.view.FloatView;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

import static com.saas.saasuser.util.TagAliasOperatorHelper.ACTION_SET;
import static com.saas.saasuser.util.TagAliasOperatorHelper.sequence;


public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,NetIntentCallBackListener.INetIntentCallBack {

    WindowManager windowManager = null;
    WindowManager.LayoutParams windowManagerParams = null;
    FloatView floatView = null;        //创建NmapView
    String showFloatView;
    @BindView(R.id.main_fragment)
    FrameLayout mainFragment;
    @BindView(R.id.rb_menu_function)
    RadioButton rbMenuFunction;
    @BindView(R.id.rb_menu_rent)
    RadioButton rbMenuRent;
    @BindView(R.id.rb_menu_message)
    RadioButton rbMenuMessage;
    @BindView(R.id.rbr_menu_order)
    RadioButton rbMenuOrder;
    @BindView(R.id.rg_home)
    RadioGroup rgHome;
    private SharedPreferencesUtil util;
    private AlertDialog inputDialog;
    private View dialogView;
    private TextView input;
    private Button sureBtn;
    private Button cancleBtn;
    private List<JSONObject> orderDatas = new ArrayList<JSONObject>();
    private JSONObject json;
    private boolean isNotShowFloatView=true;//是否展示悬浮控件

    public static boolean isForeground = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        util = new SharedPreferencesUtil(this);
        util.setFistAdviceShow(true);//标志,首次启动展示广告弹窗
        initViews();
    }
    private void initViews() {
        showFloatView = this.getIntent().getStringExtra("showFloatView");
        if (StringUtils.equals(showFloatView, "1") && isNotShowFloatView) {
            createView();
        }
        rgHome.setOnCheckedChangeListener(this);
        rbMenuRent.setChecked(true);
        registerMessageReceiver();  // used for receive msg
        initJiGuang();//初始化极光推送


        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = getIntent().getExtras();
            String title = null;
            String content = null;
            if(bundle!=null){
                title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                content = bundle.getString(JPushInterface.EXTRA_ALERT);
                LogUtils.e("极光推送标题："+ title+"极光推送内容："+content);
                if(StringUtils.isNotEmpty(title,true) && StringUtils.isNotEmpty(content,true)){
                    rbMenuMessage.setChecked(true);
                }

            }

        }
    }

    private void initJiGuang() {
        init();
        setStyleCustom();//自定义通知
        // Set<String> tags = null;//群组关键字推送
        String alias = CommonUtils.getImei(getApplicationContext(),"")+util.getMobilePhone();//针对个人推送
        int action =ACTION_SET;//ACTION_ADD 增加；ACTION_SET 设置；ACTION_DELETE 删除；
        boolean isAliasAction = true;
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = action;
        sequence++;
        tagAliasBean.alias = alias;
        // tagAliasBean.tags = tags;//todo 群组推送，后期延伸使用
        tagAliasBean.isAliasAction = isAliasAction;
        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(),sequence,tagAliasBean);
    }

    private void setStyleCustom() {//设置极光推送通知栏样式 - 定义通知栏Layout
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(MainActivity.this, R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);
        builder.layoutIconDrawable = R.drawable.saas_logo;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        builder.developerArg0 = "developerArg2";
        JPushInterface.setPushNotificationBuilder(2, builder);
        //  Toast.makeText(MainActivity.this, "Custom Builder - 2", Toast.LENGTH_SHORT).show();
    }
    private void changeFragment(Fragment targetFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup arg0, int checkedId) {
        if (rbMenuFunction.getId() == checkedId) {
            changeFragment(new FirstFragment());

        } else if (rbMenuRent.getId() == checkedId) {
            changeFragment(new SecondFragment());

        } else if (rbMenuMessage.getId() == checkedId) {
            changeFragment(new ThridFragment());
        } else if (rbMenuOrder.getId() == checkedId) {
            new NetIntent().client_underOrder(util.getUserId(), util.getPlatformRole(), new NetIntentCallBackListener(this));//检查是否有正在执行的订单
            changeFragment(new ForthFragment());
        }
    }

    // 定义是否已退出应用
    private static boolean isExit = false;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isExit = false;
        }
    };

    /**
     * 返回事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 双击返回键退出应用
     */
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            // 返回键双击延迟
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
        }
    }
    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (windowManager != null) {
            windowManager.removeView(floatView);
        }
        JiGuangBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

    }

    @Override
    public void doClick(View v) {
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    private void createView() {
        isNotShowFloatView=false;
        floatView = new FloatView(getApplicationContext());
        floatView.setImageResource(R.drawable.icon_floatview_100); // 这里简单的用自带的icon来做演示
        // 获取WindowManager
        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        // 设置LayoutParams(全局变量）相关参数
        windowManagerParams = ((MyApplication) getApplication()).getWindowParams();

        windowManagerParams.type = WindowManager.LayoutParams.TYPE_PHONE; // 设置window type
        windowManagerParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
        // 设置Window flag
        windowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * 注意，flag的值可以为：
		 * 下面的flags属性的效果形同“锁定”。
		 * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * LayoutParams.FLAG_NOT_TOUCH_MODAL 不影响后面的事件
		 * LayoutParams.FLAG_NOT_FOCUSABLE  不可聚焦
		 * LayoutParams.FLAG_NOT_TOUCHABLE 不可触摸
		 */
        // 调整悬浮窗口至左上角，便于调整坐标
        windowManagerParams.gravity = Gravity.LEFT| Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值
        windowManagerParams.x = 60;
        windowManagerParams.y = 500;
        // 设置悬浮窗口长宽数据
        windowManagerParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        // 显示myFloatView图像
        windowManager.addView(floatView, windowManagerParams);
        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderId=util.getOrderId();
                String strStatue=util.getOrderStatus();
                String strRole=util.getOrderRole();
                if(StringUtils.equals("1",strRole)){
                    startActivity(new Intent(MainActivity.this, UserTaskExcuteStatusActivity.class).putExtra("orderId",orderId).putExtra("orderStatue","-1"));// todo 订单状态待处理
                    isNotShowFloatView=true;
                }else if(StringUtils.equals("2",strRole)){
                    startActivity(new Intent(MainActivity.this, DriverTaskExcuteStatusActivity.class).putExtra("orderId",orderId).putExtra("orderStatue","-1"));// todo 订单状态待处理
                    isNotShowFloatView=true;
                }

                windowManager.removeView(floatView);
                //finish();
            }
        });
    }

    @Override
    public void onError(Request request, Exception e) {

    }

    @Override
    public void onResponse(String response) {
        com.alibaba.fastjson.JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(response);
            if (StringUtils.equals(jsonObject.getString("ErrCode"), "1")) {

                if (jsonObject.containsKey("Data")) {
                    JSONArray users_temp = jsonObject.getJSONArray("Data");

                    if (users_temp != null) {//TODO  是否有数据
                        for (int i = 0; i < users_temp.size(); i++) {
                            json = users_temp.getJSONObject(i);
                            orderDatas.add(json);
                        }
                        if(isNotShowFloatView){
                            handler.sendEmptyMessage(1);
                        }

                    }
                }
            }
        } catch (com.alibaba.fastjson.JSONException e) {
            e.printStackTrace();
        }

    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
//                  if(mBitmap!=null){
//                      BitmapDrawable bd = new BitmapDrawable(mBitmap);
//                      LogUtils.e("图片情况3："+mBitmap.toString()+":"+mBitmap.getWidth());
//                      rlRoot.setBackground(bd);
//                      try {
//                          saveBitmap(mBitmap, imageName);
//                          util.setPicture( Constants.DIR_AVATAR+imageName);
//                      } catch (IOException e) {
//                          e.printStackTrace();
//                      }
//                  }

                    break;
                case 1:
                    dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_confirm, null);
                    input = (TextView) dialogView.findViewById(R.id.input);
                    sureBtn = (Button) dialogView.findViewById(R.id.sureBtn);
                    cancleBtn = (Button) dialogView.findViewById(R.id.cancleBtn);
                    String strType;
                    input.setText("     您有未完成的订单，是否前往处理？");
                    sureBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject json =  orderDatas.get(0);//.TODO
                           LogUtils.e("当前订单："+json.toString());
                            String orderId=  json.getString("BOID");
                            String orderType=  json.getString("OrderType");
                            String strOrderStatue=  json.getString("BOOrderStatue");
                            String strDate=  json.getString("BOUseDate");
                            String strTime=  json.getString("BOUStartTime");
                             String strStartAddress=  json.getString("BOUpCarPlace");
                             String strEndAddress=  json.getString("BODownCarPlace");
                             String strStatue=  json.getString("BOOrderStatue");
                             String strUserTyype=  json.getString("OrderType");
                            if(StringUtils.equals("1",strUserTyype)){
                                startActivity(new Intent(MainActivity.this, UserTaskExcuteStatusActivity.class).putExtra("orderId",orderId).putExtra("orderStatue",strStatue));
                            }else if(StringUtils.equals("2",strUserTyype)){
                                startActivity(new Intent(MainActivity.this, DriverTaskExcuteStatusActivity.class).putExtra("orderId",orderId).putExtra("orderStatue",strStatue));
                            }
                            inputDialog.dismiss();
                            dialogView = null;
                        }
                    });
                    cancleBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isNotShowFloatView){
                                String strUserTyype=  json.getString("OrderType");
                                util.setOrderRole(strUserTyype);
                                createView();
                            }

                            inputDialog.dismiss();
                            dialogView = null;
                        }
                    });

                    inputDialog = new AlertDialog.Builder(MainActivity.this)
                            .setView(dialogView)
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {

                                }
                            }).setCancelable(false).create();
                    inputDialog.show();
                    break;
            }
        }
    };




    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init(){
        JPushInterface.init(getApplicationContext());
    }
    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        JiGuangBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String title= intent.getStringExtra(KEY_TITLE);
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!CommonUtils.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    //setCostomMsg(showMsg.toString());
                    LogUtils.e("当前推送数据："+"标题："+title+"内容"+showMsg.toString());
                }
            } catch (Exception e){
            }
        }
    }

}
