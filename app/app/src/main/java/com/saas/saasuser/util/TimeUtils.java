package com.saas.saasuser.util;

import com.saas.saasuser.bean.EnterpriseInviteCodeBean;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.saas.saasuser.util.DateUtil.getDate;
import static com.saas.saasuser.util.DateUtil.intToLong;


public class TimeUtils {
    public static String getNowTime() {//TODO 获取当前时间
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
    public static String getNowDate() {//TODO 获取当前日期
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }


    public static String getWeek() {
        Date date=new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("EEEE");
        return format1.format(date);//
    }



}
