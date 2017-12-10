package com.saas.saasuser.application;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

import com.mob.MobSDK;
import com.saas.saasuser.view.cityselect.CityList;
import com.saas.saasuser.view.cityselect.Utils;
import com.uuch.adlibrary.LApplication;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends LApplication {
	public static Context applicationContext;
	public static Context mContext;
	public static MyApplication instance = null;
	private List<Activity> activityList = new ArrayList<Activity>();
	public static boolean ISAPPUPDATE = false;


	public static CityList cityList;


	// 悬浮控件
	private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();

	public WindowManager.LayoutParams getWindowParams() {
		return windowParams;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		MobSDK.init(this,"22b78a07eede0","7107382d4d8ecb239b9e44a46c704f6b");
		applicationContext = this;
		//Fresco.initialize(this);
		cityList = Utils.getCityList(this, "city.json");
		cityList.computePosition();//计算position

//		CrashHandler crashHandler  = CrashHandler.getInstance();
//		crashHandler.init(getApplicationContext());
		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
		JPushInterface.init(this);     		// 初始化 JPush
	}
	// 构造方法
	// 实例化一次
	public synchronized static MyApplication getInstance2() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}

	public Context getmyContext() {
		return mContext;
	}





}
