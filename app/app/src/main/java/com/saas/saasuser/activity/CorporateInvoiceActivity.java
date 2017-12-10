package com.saas.saasuser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.saas.saasuser.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tanlin on 2017/11/28.
 */

public class CorporateInvoiceActivity extends BaseActivity2 {

    @BindView(R.id.llzengzhishui)
    LinearLayout ll_zengzhishui;
    @BindView(R.id.llputong)
    LinearLayout ll_putong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporate_invoice);
        ButterKnife.bind(this);
        setTitle("企业发票");
        btn_right.setText("");
        ImmersionBar.with(this).statusBarColor(R.color.status_color)
                .fitsSystemWindows(true).init();

        btn_left.setImageResource(R.drawable.icon_back);

    }
    @OnClick({R.id.llzengzhishui, R.id.llputong})
    public void zengzhishui(View view) {
        switch (view.getId()) {
            case R.id.llzengzhishui:
                Intent intent = new Intent(this, VATSpecialVotesActivity.class);
                startActivity(intent);
                break;
            case R.id.llputong:
                Intent intent1 = new Intent(this, NormalInvoiceActivity.class);
                startActivity(intent1);
                break;


        }
    }


}
