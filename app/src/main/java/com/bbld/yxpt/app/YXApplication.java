package com.bbld.yxpt.app;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.bbld.yxpt.jpush.Logger;
import com.wuxiaolong.androidutils.library.CrashHandlerUtil;
import com.wuxiaolong.androidutils.library.LogUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by likey on 2017/6/26.
 */

public class YXApplication extends Application{
    private static final String TAG = "JIGUANG-Example";
    private static YXApplication instance;
    /**
     * @return Application 实例
     */
    public static YXApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        Logger.d(TAG, "[YXApplication] onCreate");
        super.onCreate();

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        //程序崩溃处理
        LogUtil.setLog(false);
        CrashHandlerUtil crashHandlerUtil=CrashHandlerUtil.getInstance();
        crashHandlerUtil.init(this);
        crashHandlerUtil.setCrashTip("很抱歉，程序出现异常，即将退出！");

    }
}
