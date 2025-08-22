package com.zip.zipandroid.base;


import android.text.TextUtils;


public class UserInfo {
    private static UserInfo userInfo;
    private final PreferencesHelper mHelper;

    private UserInfo() {
        mHelper = new PreferencesHelper(PreferencesHelper.TB_USER);
    }

    public static UserInfo getInstance() {
        if (userInfo == null) {
            synchronized (UserInfo.class) {
                if (userInfo == null) {
                    userInfo = new UserInfo();
                }
            }
        }
        return userInfo;
    }



//    public static boolean isLogin() {
//        if (TextUtils.isEmpty(UserInfo.getInstance().getToken())) {
//            return false;
//        } else {
//            return true;
//        }
//    }

    public void logout() {
//        setToken("");
//        setUserId("");
    }
}
