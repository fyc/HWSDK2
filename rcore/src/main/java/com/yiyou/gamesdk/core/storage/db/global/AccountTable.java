package com.yiyou.gamesdk.core.storage.db.global;

import com.qygame.qysdk.outer.util.Log;
import com.yiyou.gamesdk.core.storage.ITable;

/**
 * Created by levyyoung on 15/5/15.
 */
public class AccountTable implements ITable{

    public static final String TABLE_NAME = "account";
    public static final int TABLE_VERSION = 1;

    public static final String COL_USERID                     = "user_id";
    public static final String COL_QY_ACCOUNT                 = "qy_account";
    public static final String COL_USERNAME                   = "username";
    public static final String COL_PHONE                      = "phone";
    public static final String COL_PWD                        = "pwd";
    public static final String COL_ACCESS_TOKEN               = "access_token";
    public static final String COL_REFRESH_TOKEN              = "refresh_token";
    public static final String COL_EXPIRES_IN                 = "expires_in";
    public static final String COL_AVATAR_URL                 = "avatar_url";
    public static final String COL_CHANNEL                    = "channel";
    public static final String COL_LAST_LOGIN_TIME            = "last_login_time";
    public static final String COL_LAST_GAME_ID               = "last_login_game_id";
    public static final String COL_HAS_PAYPASSWORD            = "has_pay_password";

    public static final String COL_NEED_REAL               = "need_real";
    public static final String COL_ACCOUNT_ID               = "account_id";
    public static final String COL_GUEST               = "guest";
    public static final String COL_FIRST               = "first";

    public static final String COL_IS_LOGOUT               = "is_logout";

    public static final int INDEX_USERID                       = 0;
    public static final int INDEX_QY_ACCOUNT                   = 1;
    public static final int INDEX_USERNAME                     = 2;
    public static final int INDEX_PHONE                        = 3;
    public static final int INDEX_PWD                          = 4;
    public static final int INDEX_ACCESS_TOKEN                 = 5;
    public static final int INDEX_REFRESH_TOKEN                = 6;
    public static final int INDEX_EXPIRES_IN                   = 7;
    public static final int INDEX_AVATAR_URL                   = 8;
    public static final int INDEX_CHANNEL                      = 9;
    public static final int INDEX_LAST_LOGIN_TIME              = 10;
    public static final int INDEX_LAST_GAME_ID                 = 11;
    public static final int INDEX_HAS_PAYPASSWORD              = 12;


    public static final int INDEX_NEED_REAL               = 13;
    public static final int INDEX_ACCOUNT_ID               = 14;
    public static final int INDEX_GUEST               = 15;
    public static final int INDEX_FIRST               = 16;
    public static final int INDEX_IS_LOGOUT               = 17;

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public int tableVersion() {
        return TABLE_VERSION;
    }

    @Override
    public String createTableSQL() {
        String str = "CREATE TABLE IF NOT EXISTS '"+tableName()+"' ( "
                + COL_USERID + " number primary key, "
                + COL_QY_ACCOUNT + " text, "
                + COL_USERNAME + " text, "
                + COL_PHONE + " text, "
                + COL_PWD + " text, "
                    + COL_ACCESS_TOKEN     + " text, "
                + COL_REFRESH_TOKEN     + " text, "
                + COL_EXPIRES_IN     + " number, "
                + COL_AVATAR_URL + " text, "
                + COL_CHANNEL     + " text, "
                + COL_LAST_LOGIN_TIME + " number, "
                + COL_LAST_GAME_ID        + " number, "
                + COL_HAS_PAYPASSWORD + " number, "

                + COL_NEED_REAL     + " text, " //24
                + COL_ACCOUNT_ID     + " number, "
                + COL_GUEST     + " text, "
                + COL_FIRST     + " text, "
                + COL_IS_LOGOUT     + " number) ";
        Log.d("AccountTable-createTableSQL=", str );
        return str;
    }

    @Override
    public String[] alterTableSQL(int oldVersion, int newVersion) {
//        if (oldVersion == 1 && newVersion == 2){
//            Log.d("test", "alterTableSQL: ");
//            return new String[]{
//                    "ALTER TABLE " + TABLE_NAME + " RENAME TO " + TABLE_NAME + "_temp",
//                    createTableSQL(),
//                    new StringBuilder().append("insert into ").append(TABLE_NAME).append("(")
//                            .append(COL_USERID).append(", ")
//                            .append(COL_QY_ACCOUNT).append(", ")
//                            .append(COL_USERNAME).append(", ")
//                            .append(COL_PHONE).append(", ")
//                            .append(COL_PWD).append(", ")
//                            .append(COL_ACCESS_TOKEN).append(", ")
//                            .append(COL_REFRESH_TOKEN).append(", ")
//                            .append(COL_EXPIRES_IN).append(", ")
//                            .append(COL_AVATAR_URL).append(", ")
//                            .append(COL_CHANNEL).append(", ")
//                            .append(COL_LAST_LOGIN_TIME).append(", ")
//                            .append(COL_LAST_GAME_ID).append(", ")
//                            .append(COL_HAS_PAYPASSWORD).append(", ")
//                            .append(COL_REVERSE_TEXT_0).append(", ")
//                            .append(COL_REVERSE_TEXT_1).append(", ")
//                            .append(COL_REVERSE_TEXT_2).append(", ")
//                            .append(COL_REVERSE_TEXT_3).append(", ")
//                            .append(COL_REVERSE_TEXT_4).append(", ")
//                            .append(COL_REVERSE_TEXT_5).append(", ")
//                            .append(COL_REVERSE_NUMBER_0).append(", ")
//                            .append(COL_REVERSE_NUMBER_1).append(", ")
//                            .append(COL_REVERSE_NUMBER_2).append(", ")
//                            .append(COL_REVERSE_NUMBER_3).append(", ")
//                            .append(COL_REVERSE_NUMBER_4).append(", ")
//                            .append(COL_REVERSE_NUMBER_5).append(", ")
//                            .append(COL_REVERSE_BLOB_0).append(", ")
//                            .append(COL_REVERSE_BLOB_1).append(", ")
//                            .append(COL_REVERSE_BLOB_2).append(") ")
//                            .append("select ")
//                            .append(COL_USERID).append(", ")
//                            .append(COL_QY_ACCOUNT).append(", ")
//                            .append(COL_USERNAME).append(", ")
//                            .append(COL_PHONE).append(", ")
//                            .append(COL_PWD).append(", ")
//                            .append(COL_ACCESS_TOKEN).append(", ")
//                            .append(COL_REFRESH_TOKEN).append(", ")
//                            .append(COL_EXPIRES_IN).append(", ")
//                            .append(COL_AVATAR_URL).append(", ")
//                            .append(COL_CHANNEL).append(", ")
//                            .append(COL_LAST_LOGIN_TIME).append(", ")
//                            .append(COL_LAST_GAME_ID).append(", ")
//                            .append(" \"0\" ").append(", ")
//                            .append(COL_REVERSE_TEXT_0).append(", ")
//                            .append(COL_REVERSE_TEXT_1).append(", ")
//                            .append(COL_REVERSE_TEXT_2).append(", ")
//                            .append(COL_REVERSE_TEXT_3).append(", ")
//                            .append(COL_REVERSE_TEXT_4).append(", ")
//                            .append(COL_REVERSE_TEXT_5).append(", ")
//                            .append(COL_REVERSE_NUMBER_0).append(", ")
//                            .append(COL_REVERSE_NUMBER_1).append(", ")
//                            .append(COL_REVERSE_NUMBER_2).append(", ")
//                            .append(COL_REVERSE_NUMBER_3).append(", ")
//                            .append(COL_REVERSE_NUMBER_4).append(", ")
//                            .append(COL_REVERSE_NUMBER_5).append(", ")
//                            .append(COL_REVERSE_BLOB_0).append(", ")
//                            .append(COL_REVERSE_BLOB_1).append(", ")
//                            .append(COL_REVERSE_BLOB_2).append(" ")
//                            .append("from ").append(TABLE_NAME).append("_temp").toString(),
//                    "DROP TABLE " + TABLE_NAME + "_temp"
//            };
//        }
        Log.d("AccountTable", "oldVersion: " + oldVersion + ";newVersion: " + newVersion );

        return new String[0];
    }
}
