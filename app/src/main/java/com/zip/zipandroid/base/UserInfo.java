package com.zip.zipandroid.base;


import android.text.TextUtils;


public class UserInfo {
    private static UserInfo userInfo;
    private final PreferencesHelper mHelper;
    private String token = "token";
    private String newUser = "newUser";
    private String userId = "userId";
    private String test = "test";
    private String panId = "panId";
    private String phoneNumber = "phoneNumber";


    public String getUserId() {
        String s = mHelper.getValue(userId) == null ? "" : mHelper.getValue(userId);
        return s;
    }

    public String getPhoneNumber() {
        String s = mHelper.getValue(phoneNumber) == null ? "" : mHelper.getValue(phoneNumber);
        return (s);
    }

    public String getPanId() {
        String s = mHelper.getValue(panId) == null ? "1289" : mHelper.getValue(panId);
        return s;
    }

    public void setPanId(String panId) {
        mHelper.setValue(this.panId, panId + "");
    }


    public void setUserId(String userId) {
        mHelper.setValue(this.userId, userId + "");
    }

    public String getTestUser() {
        String s = mHelper.getValue(test) == null ? "0" : mHelper.getValue(test);
        return s;
    }


    public void setTestUser(String test) {
        mHelper.setValue(this.test, test + "");
    }

    public void setPhone(String phone) {
        mHelper.setValue(this.phoneNumber, phone + "");
    }

    private String tokenCache;

    public String getToken() {
        if (TextUtils.isEmpty(tokenCache)) {
            tokenCache = mHelper.getValue(token) == null ? "" : mHelper.getValue(token);
        }
        return tokenCache;
    }

    public void setToken(String token) {
        tokenCache = token;
        mHelper.setValue(this.token, token);
    }

    public void setNewUser(String newUser) {
        mHelper.setValue(this.newUser, newUser);
    }

    public String getNewUser() {
        return mHelper.getValue(this.newUser, "");
    }


//    public void setUserId(String userId) {
//        this.userId = userId;
//    }

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

//
//    public void setLogUser(LoginResponse dto) {
//        setPanId("1289");
//        setUserId(dto.getProfitroutinetree() + "");
//        setToken(dto.getDisappointmentspaceshipglue());
//        setTestUser(dto.getDeclarechant());
////        dto.getDeclarechant();//是不是测试用户 1的话就是
//    }


    public static boolean isLogin() {
        if (TextUtils.isEmpty(UserInfo.getInstance().getToken())) {
            return false;
        } else {
            return true;
        }
    }

    public void logout() {
        setToken("");
        setUserId("");
    }
}
