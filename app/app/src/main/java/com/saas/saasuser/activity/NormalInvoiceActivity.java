package com.saas.saasuser.activity;

import android.os.Bundle;
import android.view.View;
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

public class NormalInvoiceActivity extends BaseActivity2 {

    private TextView tv_name_company;
    private TextView tv_enterprise_id_number;
    private TextView tv_bank;
    private TextView tv_bank_account;
    private TextView tv_contact_phone_number;
    private TextView tv_adress;

    private TextView tv_name_company0;
    private TextView tv_enterprise_id_number0;
    private TextView tv_bank0;
    private TextView tv_bank_account0;
    private TextView tv_contact_phone_number0;
    private TextView tv_adress0;

    private final int FAPIAO = 1005;
    private SharedPreferencesUtil util;
    SpecialIvotesInfo.DataBean gsjcList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_invoice);


        setTitle("普通发票");
        btn_right.setText("");
        ImmersionBar.with(this).statusBarColor(R.color.status_color)
                .fitsSystemWindows(true).init();

        btn_left.setImageResource(R.drawable.icon_back);


        gsjcList=new SpecialIvotesInfo.DataBean();
        util = new SharedPreferencesUtil(this);
        request(FAPIAO);

        tv_name_company= (TextView) findViewById(R.id.name_company);
        tv_enterprise_id_number= (TextView) findViewById(R.id.enterprise_id_number);
        tv_bank= (TextView) findViewById(R.id.bank);
        tv_bank_account= (TextView) findViewById(R.id.bank_account);
        tv_contact_phone_number= (TextView) findViewById(R.id.contact_phone_number);
        tv_adress= (TextView) findViewById(R.id.adress);



        tv_bank0= (TextView) findViewById(R.id.bank0);
        tv_bank_account0= (TextView) findViewById(R.id.bank_account0);
        tv_contact_phone_number0= (TextView) findViewById(R.id.contact_phone_number0);
        tv_adress0= (TextView) findViewById(R.id.adress0);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        super.onSuccess(requestCode, result);
        switch (requestCode){

            case FAPIAO:

                String ss= (String) result;

                gsjcList = new Gson().fromJson(ss,SpecialIvotesInfo.class).getData();


                tv_name_company.setText(gsjcList.getInvoiceTitle()==null ? null:gsjcList.getInvoiceTitle()+"");

                tv_enterprise_id_number.setText(gsjcList.getComDuty()==null ? null:gsjcList.getComDuty()+"");


                if(gsjcList.getBank()!=null||gsjcList.getBankAccount()!=null||gsjcList.getRegLoginPhone()!=null||gsjcList.getRegAddress()!=null){

                    tv_bank0.setVisibility(View.VISIBLE);
                    tv_bank.setVisibility(View.VISIBLE);
                    tv_bank.setText(gsjcList.getBank()+"");


                    tv_bank_account.setVisibility(View.VISIBLE);
                    tv_bank_account0.setVisibility(View.VISIBLE);
                    tv_bank_account.setText(gsjcList.getBankAccount()+"");

                    tv_contact_phone_number.setVisibility(View.VISIBLE);
                    tv_contact_phone_number0.setVisibility(View.VISIBLE);
                    tv_contact_phone_number.setText(gsjcList.getRegLoginPhone()+"");

                    tv_adress.setVisibility(View.VISIBLE);
                    tv_adress0.setVisibility(View.VISIBLE);
                    tv_adress.setText(gsjcList.getRegAddress()+"");




                }

        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        super.onFailure(requestCode, state, result);
    }

    @Override
    public Object doInBackground(int requestCode) throws HttpException, IOException {


        switch (requestCode) {
            case FAPIAO:
                HashMap<String, String> map = new HashMap<>();
                map.put("companyID", util.getCompanyId());
                String result = HttpOkUtils.post(Constants.HttpRoot + Constants.QIYEFAPIAO, map);

                LogUtils.tag("companyID").d(map);
                LogUtils.tag("companyID").d(result);


                return result;





        }
        return super.doInBackground(requestCode);
    }
}
