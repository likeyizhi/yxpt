package com.bbld.yxpt.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

/**
 * SharedPreferences保存、删除token，
 * 删除SharedPreferences文件
 * Created by likey on 2017/5/23.
 */

public class MyToken {
    private Context mContext;
    private static final String TOKEN=null;
    private final static String DATA_URL = "/data/data/";

    public MyToken(Context mContext){
        super();
        this.mContext=mContext;
    }
    /**
     * SharedPreferences保存token
     * */
    public String getToken(){
        SharedPreferences shared=mContext.getSharedPreferences("YXToken",mContext.MODE_PRIVATE);
        String token = shared.getString(TOKEN, null);
        return token;
    }
    /**
     * SharedPreferences保存HeadPortrait
     * */
    public String getSPHeadPortrait(){
        SharedPreferences shared=mContext.getSharedPreferences("YXToken",mContext.MODE_PRIVATE);
        String headPortrait = shared.getString("HeadPortrait", null);
        return headPortrait;
    }
    /**
     * 删除保存的token
     * */
    public void delToken(){
        SharedPreferences shared=mContext.getSharedPreferences("YXToken",mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.remove(TOKEN);
        editor.commit();
    }
    /**
     * 删除保存的HeadPortrait
     * */
    public void delSPHeadPortrait(){
        SharedPreferences shared=mContext.getSharedPreferences("YXToken",mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.remove("HeadPortrait");
        editor.commit();
    }
    /**
     * 删除保存的帐号密码
     * */
    public void delAP(){
        SharedPreferences shared=mContext.getSharedPreferences("YXAP",mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.remove("YXACC");
        editor.remove("YXPWD");
        editor.commit();
    }
    /**
     * 删除SharedPreferences文件
     * */
    public void delSPFile(){
        File file=new File(DATA_URL + mContext.getPackageName().toString()
                + "/shared_prefs", "YXToken.xml");
        if (file.exists()) {
            file.delete();
        }
    }
    /**
     * 删除SharedPreferences文件
     * */
    public void delAPFile(){
        File file=new File(DATA_URL + mContext.getPackageName().toString()
                + "/shared_prefs", "YXAP.xml");
        if (file.exists()) {
            file.delete();
        }
    }
}
