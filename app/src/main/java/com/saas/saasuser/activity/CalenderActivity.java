package com.saas.saasuser.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.saas.saasuser.R;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.util.Util;
import com.saas.saasuser.view.datechoice.CollapseCalendarView;
import com.saas.saasuser.view.datechoice.manager.CalendarManager;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.saas.saasuser.R.id.tv_show_selected;

public class CalenderActivity extends Activity {

    @BindView(R.id.btn_submit)
    TextView btnSubmit;
    @BindView(tv_show_selected)
    TextView tvShowSelected;
    @BindView(R.id.fab_today)
    FloatingActionButton fabToday;
    private CollapseCalendarView calendarView;
    private CalendarManager mManager;
    private JSONObject json;
    private SimpleDateFormat sdf;
    private boolean show = false;
    private String strDate, strWeek;
    public static final String DATA_STR_WEEK = "DATA_WEEK_CODE";
    public static final String DATA_STR_DATE = "DATA_DATECODE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        ButterKnife.bind(this);
        Util.setHeadTitleMore(this, "日期选择", true);
        btnSubmit.setText("确定");

        sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendarView = (CollapseCalendarView) findViewById(R.id.calendar);
        mManager = new CalendarManager(LocalDate.now(),
                CalendarManager.State.MONTH, LocalDate.now().withYear(100),
                LocalDate.now().plusYears(100));
        //月份切换监听器
        mManager.setMonthChangeListener(new CalendarManager.OnMonthChangeListener() {

            @Override
            public void monthChange(String month, LocalDate mSelected) {
                // TODO Auto-generated method stub
                Toast.makeText(CalenderActivity.this, month, Toast.LENGTH_SHORT).show();
            }

        });
        /**
         * 日期选中监听器
         */
        calendarView.setDateSelectListener(new CollapseCalendarView.OnDateSelect() {
            @Override
            public void onDateSelected(LocalDate date) {
                //Toast.makeText(CalenderActivity.this, date.toString(), Toast.LENGTH_SHORT).show();
                strDate = date.toString();
                strWeek = StringUtils.getChanceWeek(date.getDayOfWeek() + "");
                tvShowSelected.setText(strDate + "   " + strWeek);
            }


        });
        calendarView.setTitleClickListener(new CollapseCalendarView.OnTitleClickListener() {

            @Override
            public void onTitleClick() {
                Toast.makeText(CalenderActivity.this, "点击标题", Toast.LENGTH_SHORT).show();
            }
        });

//				mManager.toggleView();
//				calendarView.populateLayout();

//				calendarView.showChinaDay(show);
//				show = !show;
        Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.MONTH, 9);
//		cal.set(Calendar.DAY_OF_MONTH, 0);
        json = new JSONObject();
        try {
            for (int i = 0; i < 30; i++) {
                JSONObject jsonObject2 = new JSONObject();
                if (i <= 6) {
                    jsonObject2.put("type", "休");
                } else if (i > 6 && i < 11) {
                    jsonObject2.put("type", "班");
                }
                if (i % 3 == 0) {
                    jsonObject2.put("list", new JSONArray());
                }

                json.put(sdf.format(cal.getTime()), jsonObject2);

                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        //设置数据显示
        calendarView.setArrayData(json);
        //初始化日历管理器
        calendarView.init(mManager);
    }


    @OnClick({R.id.btn_submit, R.id.fab_today})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if (StringUtils.isEmpty(strDate) && StringUtils.isEmpty(strWeek)) {
                    ToastUtils.showShortToastSafe(CalenderActivity.this, "日期不不能为空");
                } else {
                    Intent intent = new Intent(CalenderActivity.this, AddTripApplyActivity.class);
                    intent.putExtra(DATA_STR_WEEK, strWeek);
                    intent.putExtra(DATA_STR_DATE, strDate);
                    setResult(RESULT_OK, intent);
                    finish();// 结束之后会将结果传回

                }
                break;
            case R.id.fab_today:
                calendarView.changeDate(LocalDate.now().toString());
                strDate = LocalDate.now().toString();
                strWeek = StringUtils.getChanceWeek(LocalDate.now().dayOfWeek() + "");
                tvShowSelected.setText(strDate + "   " + strWeek);
                break;
        }
    }
}
