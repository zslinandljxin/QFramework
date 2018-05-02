package com.zsl.zhaoqing.framework.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * 修改状态栏字体颜色
 * Created by Administrator on 2016/12/22.
 */

public class StatusBarUtils {

    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MEIZU_VERSION_NAME = "ro.build.display.id";

    private StatusBarUtils(){

    }

    /**设置状态栏字体颜色*/
    public static void setStatusBarTextColor(Activity ctx, int type){
        transparentStatusBar(ctx);
        if (isMIUIV6()) { //MIUI系统
            setMIUIStatusBarTextColor(ctx, type);
        } else if (isFlyme()){ //Flyme系统
            setFlymeStatusBarTextColor(ctx, type);
        }
//        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //Android 6.0
//            ctx.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
    }

    /**
     * 小米MIUIV6系统
     *
     * @param context
     * @param type
     * 0--只需要状态栏透明 1-状态栏透明且黑色字体 2-清除黑色字体
     */
    public static void setMIUIStatusBarTextColor(Activity context, int type) {
        Window window = context.getWindow();
        Class clazz = window.getClass();
        try {
            int tranceFlag = 0;
            int darkModeFlag = 0;
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);
            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (type == 0) {
                extraFlagField.invoke(window, tranceFlag, tranceFlag);// 只需要状态栏透明
            } else if (type == 1) {
                extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);// 状态栏透明且黑色字体
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag);// 清除黑色字体
            }
        } catch (Exception e) {

        }
    }

    /**
     * 判断是否为MIUIV6系统
     * @return
     */
    public static boolean isMIUIV6() {
        InputStream is = null;
        try {
            Properties prop = new Properties();
            is = new FileInputStream(new File(Environment.getRootDirectory(),"build.prop"));
            prop.load(is);
            is.close();
            is = null;
            String name = prop.getProperty(KEY_MIUI_VERSION_NAME, "");
            if (null != name && name.length() > 0) {
                name = name.toLowerCase();
            } else {
                return false;
            }
            int vercode = 0;
            if (-1 != name.indexOf("v")) {
                String vernum = name.substring(name.indexOf("v") + 1);
                vercode = Integer.valueOf(vernum);
                if (vercode >= 6) {
                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            return false;
        }finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 魅族Flyme系统
     * @param activity 需要设置的activity的窗口
     * @param type   1-状态栏透明且黑色字体 2-清除黑色字体
     */
    public static boolean setFlymeStatusBarTextColor(Activity activity, int type) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (type == 1) { //状态栏透明且黑色字体
                    value |= bit;
                } else if (type == 2) { //清除黑色字体
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 判断是否为flayme系统
     * @return
     */
    public static boolean isFlyme(){
        //获取魅族系统操作版本标识
        InputStream is = null;
        try {
            Properties prop = new Properties();
            is = new FileInputStream(new File(Environment.getRootDirectory(),"build.prop"));
            prop.load(is);
            is.close();
            is = null;
            String meizuFlyme = prop.getProperty(KEY_MEIZU_VERSION_NAME,"");
            if (!TextUtils.isEmpty(meizuFlyme)) {
                meizuFlyme = meizuFlyme.toLowerCase();
            } else {
                return false;
            }
            if (-1 != meizuFlyme.indexOf("e")) {
                String vernum = meizuFlyme.substring(meizuFlyme.indexOf("e") + 2, meizuFlyme.indexOf("e") + 3);
                int vercode = Integer.valueOf(vernum);
                if (vercode >= 4) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 修改状态栏为全透明
     *
     * @param activity
     */
    @TargetApi(19)
    public static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
            Window window = activity.getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}