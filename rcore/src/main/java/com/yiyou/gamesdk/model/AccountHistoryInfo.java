package com.yiyou.gamesdk.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.yiyou.gamesdk.core.storage.db.global.AccountTable;

import static android.R.attr.accountType;

/**
 * Created by levyyoung on 15/6/24.
 */
public class AccountHistoryInfo {

    public static final int ACCOUNT_TYPE_USER = 0; //普通用户

    public static final int ACCOUNT_STATUS_ACTIVATING = 0; //激活

    public long userID              = 0;
//    public String QYAccount           = "";
//    public String username          = "";
    public String phone             = "";
//    public String password          = "";
    public String accessToken       = "";
//    public String refreshToken      = "";
//    public String avatarUrl         = "";
    public boolean hasPayPassword   = false;
//    public int expiresIn            = 0;
//    public String channel           = "";
    public long     lastLoginTime = 0;
//    public int      lastLoginGameId = 0;

    public String need_real = "";
    public int account_id = 0;
    public String guest = "";
    public String first = "";
    public int is_logout = 0;

    @Override
    public String toString() {
        return "AccountHistoryInfo{" +
                "userID=" + userID +
                ", phone='" + phone + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", hasPayPassword=" + hasPayPassword +
                ", lastLoginTime=" + lastLoginTime +
                ", need_real='" + need_real + '\'' +
                ", account_id=" + account_id +
                ", guest='" + guest + '\'' +
                ", first='" + first + '\'' +
                ", is_logout=" + is_logout +
                '}';
    }

    public static ContentValues transformToCV(AccountHistoryInfo info) {
        ContentValues cv = new ContentValues();
        cv.put(AccountTable.COL_USERID, info.userID);
        cv.put(AccountTable.COL_PHONE, info.phone);
        cv.put(AccountTable.COL_ACCESS_TOKEN, info.accessToken);
        cv.put(AccountTable.COL_LAST_LOGIN_TIME, info.lastLoginTime);
//        cv.put(AccountTable.COL_HAS_PAYPASSWORD, info.hasPayPassword);
        cv.put(AccountTable.COL_NEED_REAL, info.need_real);
        cv.put(AccountTable.COL_ACCOUNT_ID, info.account_id);
        cv.put(AccountTable.COL_GUEST, info.guest);
        cv.put(AccountTable.COL_FIRST, info.first);
        cv.put(AccountTable.COL_IS_LOGOUT, info.is_logout);
        return cv;
    }

    public static AccountHistoryInfo fromCV(ContentValues contentValues) {
        AccountHistoryInfo info = new AccountHistoryInfo();
        info.userID = contentValues.getAsLong(AccountTable.COL_USERID);
//        if (contentValues.containsKey(AccountTable.COL_QY_ACCOUNT)) {
//            info.QYAccount = contentValues.getAsString(AccountTable.COL_QY_ACCOUNT);
//        }
//        if (contentValues.containsKey(AccountTable.COL_USERNAME)) {
//            info.username = contentValues.getAsString(AccountTable.COL_USERNAME);
//        }
        if (contentValues.containsKey(AccountTable.COL_PHONE)) {
            info.phone = contentValues.getAsString(AccountTable.COL_PHONE);
        }
//        if (contentValues.containsKey(AccountTable.COL_PWD)) {
//            info.password = contentValues.getAsString(AccountTable.COL_PWD);
//        }
//        if (contentValues.containsKey(AccountTable.COL_AVATAR_URL)) {
//            info.avatarUrl = contentValues.getAsString(AccountTable.COL_AVATAR_URL);
//        }
        if (contentValues.containsKey(AccountTable.COL_LAST_LOGIN_TIME)) {
            info.lastLoginTime = contentValues.getAsLong(AccountTable.COL_LAST_LOGIN_TIME);
        }
        if (contentValues.containsKey(AccountTable.COL_ACCESS_TOKEN)){
            info.accessToken = contentValues.getAsString(AccountTable.COL_ACCESS_TOKEN);
        }

        if (contentValues.containsKey(AccountTable.COL_NEED_REAL)) {
            info.need_real = contentValues.getAsString(AccountTable.COL_NEED_REAL);
        }
        if (contentValues.containsKey(AccountTable.COL_ACCOUNT_ID)){
            info.account_id = contentValues.getAsInteger(AccountTable.COL_ACCOUNT_ID);
        }
        if (contentValues.containsKey(AccountTable.COL_GUEST)) {
            info.guest = contentValues.getAsString(AccountTable.COL_GUEST);
        }
        if (contentValues.containsKey(AccountTable.COL_FIRST)){
            info.first = contentValues.getAsString(AccountTable.COL_FIRST);
        }
        if (contentValues.containsKey(AccountTable.COL_IS_LOGOUT)){
            info.is_logout = contentValues.getAsInteger(AccountTable.COL_IS_LOGOUT);
        }
        return info;
    }

    public static AccountHistoryInfo transformFromCursor(Cursor cursor) {
        AccountHistoryInfo info = new AccountHistoryInfo();
        info.userID = cursor.getLong(AccountTable.INDEX_USERID);
//        info.QYAccount = cursor.getString(AccountTable.INDEX_QY_ACCOUNT);
//        info.username = cursor.getString(AccountTable.INDEX_USERNAME);
        info.phone = cursor.getString(AccountTable.INDEX_PHONE);
        info.accessToken = cursor.getString(AccountTable.INDEX_ACCESS_TOKEN);
//        info.password = cursor.getString(AccountTable.INDEX_PWD);
//        info.avatarUrl = cursor.getString(AccountTable.INDEX_AVATAR_URL);
        info.lastLoginTime = cursor.getLong(AccountTable.INDEX_LAST_LOGIN_TIME);
        info.hasPayPassword = cursor.getInt(AccountTable.INDEX_HAS_PAYPASSWORD) == 1;

        info.need_real = cursor.getString(AccountTable.INDEX_NEED_REAL);
        info.account_id = cursor.getInt(AccountTable.INDEX_ACCOUNT_ID);
        info.guest = cursor.getString(AccountTable.INDEX_GUEST);
        info.first = cursor.getString(AccountTable.INDEX_FIRST);
        info.is_logout = cursor.getInt(AccountTable.INDEX_IS_LOGOUT);
        return info;
    }
}
