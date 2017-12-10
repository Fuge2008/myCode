package com.saas.saasuser.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.saas.saasuser.R;
import com.saas.saasuser.async.HttpException;
import com.saas.saasuser.async.HttpOkUtils;
import com.saas.saasuser.bean.SpecialIvotesInfo;
import com.saas.saasuser.global.Constants;
import com.saas.saasuser.util.SharedPreferencesUtil;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by tanlin on 2017/11/28.
 */

public class VATSpecialVotesActivity extends BaseActivity2 {

    private TextView tv_name_company;
    private TextView tv_enterprise_id_number;
    private TextView tv_bank;
    private TextView tv_bank_account;
    private TextView tv_contact_phone_number;
    private TextView tv_adress;

    private final int ZENGZHIFAPIAO = 1005;
    private SharedPreferencesUtil util;

     SpecialIvotesInfo.DataBean gsjcList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_votes);

        setTitle("增值税专用票");
        btn_right.setText("");
        ImmersionBar.with(this).statusBarColor(R.color.status_color)
                .fitsSystemWindows(true).init();

        btn_left.setImageResource(R.drawable.icon_back);

        gsjcList=new SpecialIvotesInfo.DataBean();

        tv_name_company= (TextView) findViewById(R.id.name_company);
        tv_enterprise_id_number= (TextView) findViewById(R.id.enterprise_id_number);
        tv_bank= (TextView) findViewById(R.id.bank);
        tv_bank_account= (TextView) findViewById(R.id.bank_account);
        tv_contact_phone_number= (TextView) findViewById(R.id.contact_phone_number);
        tv_adress= (TextView) findViewById(R.id.adress);


        util = new SharedPreferencesUtil(this);
        request(ZENGZHIFAPIAO);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        super.onSuccess(requestCode, result);

        switch (requestCode){

            case ZENGZHIFAPIAO:

                String ss= (String) result;

                gsjcList = new Gson().fromJson(ss,SpecialIvotesInfo.class).getData();


                tv_name_company.setText(gsjcList.getInvoiceTitle()==null ? null:gsjcList.getInvoiceTitle()+"");

                tv_enterprise_id_number.setText(gsjcList.getComDuty()==null ? null:gsjcList.getComDuty()+"");

                tv_bank.setText(gsjcList.getBank()==null ? null:gsjcList.getBank()+"");
                tv_bank_account.setText(gsjcList.getBankAccount()==null ? null:gsjcList.getBankAccount()+"");
                tv_contact_phone_number.setText(gsjcList.getRegLoginPhone()==null ? null:gsjcList.getRegLoginPhone()+"");
                tv_adress.setText(gsjcList.getRegAddress()==null ? null:gsjcList.getRegAddress()+"");
        }



    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        super.onFailure(requestCode, state, result);
    }

    @Override
    public Object doInBackground(int requestCode) throws HttpException, IOException {

        switch (requestCode) {
            case ZENGZHIFAPIAO:
                HashMap<String, String> map = new HashMap<>();
                map.put("companyID", util.getCompanyId());
                String result = HttpOkUtils.post(Constants.HttpRoot + Constants.QIYEFAPIAO, map);

                LogUtils.tag("companyID").d(result);
                return result;
        }
        return super.doInBackground(requestCode);
    }




}
