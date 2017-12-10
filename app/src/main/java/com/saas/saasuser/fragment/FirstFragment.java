package com.saas.saasuser.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.saas.saasuser.R;
import com.saas.saasuser.activity.CompanyCardActivity;
import com.saas.saasuser.activity.CorporateInvoiceActivity;
import com.saas.saasuser.activity.NewOrdersActivity2;
import com.saas.saasuser.activity.SetActivity;
import com.saas.saasuser.activity.VVThreeJiuActivity;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.view.CircleMenuLayout;
import com.uuch.adlibrary.AdConstant;
import com.uuch.adlibrary.AdManager;
import com.uuch.adlibrary.bean.AdInfo;
import com.uuch.adlibrary.transformer.RotateDownPageTransformer;

import java.util.ArrayList;
import java.util.List;


public class FirstFragment extends BaseFragment implements OnClickListener {
	private CircleMenuLayout mCircleMenuLayout;
	private TextView tvtIitle, tvContent;


	private int[] mItemImgs = new int[]{
			R.drawable.icon_vehicle_handover,
			R.drawable.icon_personal_settings,
			R.drawable.icon_more_functions,
			R.drawable.icon_corporate_invoice,
			R.drawable.icon_enterprisebusinesscard,
			R.drawable.icon_news,
			R.drawable.icon_neworders,
			R.drawable.icon_vvdynamic,

	};
	private List<AdInfo> advList = null;

	private SharedPreferencesUtil util;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View initView() {

		View view = View.inflate(mActivity, R.layout.fragment_first, null);
		tvContent = (TextView) view.findViewById(R.id.tv_content);
		tvtIitle = (TextView) view.findViewById(R.id.tv_title);
		TextView tvTitle=(TextView)view.findViewById(R.id.head_title);
		tvTitle.setText("功能");
		util=new SharedPreferencesUtil(mActivity);
		view.findViewById(R.id.head_more).setVisibility(View.GONE);
		final String[] mItemContent = getResources().getStringArray(R.array.rent_enter_content);
		final String[] mItemTitle = getResources().getStringArray(R.array.rent_enter_title);
		mCircleMenuLayout = (CircleMenuLayout) view.findViewById(R.id.id_menulayout);
		mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTitle);
		mCircleMenuLayout.Rotating();

		mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
			@Override
			public void itemClick(View view, int pos) {
				Toast.makeText(mActivity, mItemTitle[pos], Toast.LENGTH_SHORT).show();
				tvtIitle.setText(mItemTitle[pos]);
				tvContent.setText(mItemContent[pos]);
//				if(mItemTitle[pos].equals("最新功能")){
//					startActivity(new Intent(mActivity,DriverTaskExcuteStatusActivity.class));
//				}
				if (mItemTitle[pos].equals("个人设置")) {
					startActivity(new Intent(mActivity, SetActivity.class));
				}
				if(mItemTitle[pos].equals("企业名片")){
					startActivity(new Intent(mActivity, CompanyCardActivity.class));

				}
				if (mItemTitle[pos].equals("租行新闻")) {
					startActivity(new Intent(mActivity,CompanyCardActivity.class));

				}
				if (mItemTitle[pos].equals("新增订单")) {
					startActivity(new Intent(mActivity, NewOrdersActivity2.class));

				}
				if (mItemTitle[pos].equals("企业发票")) {
					startActivity(new Intent(mActivity,CorporateInvoiceActivity.class));

				}

			}


			@Override
			public void itemCenterClick(View view) {
				//Toast.makeText(mActivity, "抱歉！当前SAAS服务器正忙！请稍后...", Toast.LENGTH_SHORT).show();
				//startActivity(new Intent(mActivity, LoginActivity.class));
                Intent intent = new Intent(mActivity, VVThreeJiuActivity.class);
                startActivity(intent);
            }
		});


		return view;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		}
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (util.getFistAdviceShow()) {
			initData();
			util.setFistAdviceShow(false);
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			//mCircleMenuLayout.Rotating();//相当于onReusme()方法
		}
	}

	private void initData() {

		advList = new ArrayList<>();

		AdInfo adInfo = new AdInfo();
		adInfo.setActivityImg("https://raw.githubusercontent.com/yipianfengye/android-adDialog/master/images/testImage1.png");
		adInfo.setAdId("1");


		advList.add(adInfo);

		adInfo = new AdInfo();
		adInfo.setActivityImg("https://raw.githubusercontent.com/yipianfengye/android-adDialog/master/images/testImage2.png");
		adInfo.setAdId("2");

		advList.add(adInfo);
		AdManager adManager = new AdManager(getActivity(), advList);


		adManager.setOnImageClickListener(new AdManager.OnImageClickListener() {
			@Override
			public void onImageClick(View view, AdInfo advInfo) {

				if(advInfo.getAdId().equals("1")){

					Uri uri = Uri.parse("http://www.baidu.com");
					Intent it = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(it);

				}else if(advInfo.getAdId().equals("2")){

					Uri uri = Uri.parse("http://www.sina.com");
					Intent it = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(it);

				}

				//  Toast.makeText(getActivity(), "您点击了ViewPagerItem..." + advInfo.getAdId(), Toast.LENGTH_SHORT).show();
			}
		});
		adManager.setOverScreen(true)
				.setPageTransformer(new RotateDownPageTransformer());

		adManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP);


	}
}
