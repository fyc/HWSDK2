package com.qiyuan.gamesdk.container;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.qiyuan.gamesdk.R;
import com.qygame.qysdk.outer.IOperateCallback;
import com.qygame.qysdk.outer.consts.QYCodeDef;
import com.qygame.qysdk.outer.model.GameParamInfo;
import com.qygame.qysdk.outer.util.StorageConfig;

public class RMainActivity2 extends FragmentActivity {
    public static final String TAG = "QYGAMESDK:MAINACTIVITY";
    //    IQYSDK iQYSDK;
    Button init, regist, login, payH5;

    public static void openActivity(Activity act) {
        Intent i = new Intent(act, RMainActivity2.class);
        act.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.r_activity_main2);
        init = (Button) findViewById(R.id.init);
        regist = (Button) findViewById(R.id.regist);
        login = (Button) findViewById(R.id.login);
        payH5 = (Button) findViewById(R.id.payH5);
        init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSdk();
//                RMainActivity2.openActivity(RMainActivity.this);
            }
        });
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registImpl();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginImpl();
            }
        });
        payH5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payUrl = "http://www.373yx.com/payment/preview?cliBuyerId=19000&cliSellerId=2018111415564890400010102c2&" +
                        "cpOrderNo=" + System.currentTimeMillis() +
                        "&cpPrice=0.01&cpOrderTitle=%E9%A6%96%E5%85%851";
                payH5(payUrl);
            }
        });

    }

    private void initSdk() {
        GameParamInfo paramInfo = new GameParamInfo();
        paramInfo.setGameId("2018111415564890400010102c2");
        paramInfo.setSdkKey("7dc18ce3418bcfb6ffa6e72ba1943884");

//        iQYSDK = new QYSDKImpl();
        StorageConfig.prepare(this, false);
        LoadPlugin.getInstance().init(this, paramInfo, false, this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ?
                Configuration.ORIENTATION_LANDSCAPE : Configuration.ORIENTATION_PORTRAIT, new IOperateCallback<String>() {
            @Override
            public void onResult(int i, String s) {
                if (i == 0) {
                    Log.d(TAG, "QYGameSDK初始化成功！");
                    //设置退出回调
                    setLogoutListener();
                } else {
                    Log.d(TAG, "QYGameSDK初始化失败！");
                }
            }
        });

//        iQYSDK.init(this, paramInfo, false, this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ?
//                Configuration.ORIENTATION_LANDSCAPE : Configuration.ORIENTATION_PORTRAIT, new IOperateCallback<String>() {
//            @Override
//            public void onResult(int i, String s) {
//                if (i == 0) {
//                    Log.d(TAG, "QYGameSDK初始化成功！");
//                    //设置退出回调
//                    setLogoutListener();
//                } else {
//                    Log.d(TAG, "QYGameSDK初始化失败！");
//                }
//            }
//        });
    }

    private void registImpl() {
        LoadPlugin.getInstance().regist(
                this, new IOperateCallback<String>() {
                    @Override
                    public void onResult(int code, String s) {
                        if (code == QYCodeDef.SUCCESS) {
                            Log.d(TAG, "QYGameSDK登录成功！");
                            login.setText("退出");
                            login.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    logoutImpl();
                                }
                            });
                        } else {
                            Log.d(TAG, "QYGameSDK登录失败！");
                        }
                    }
                });
    }

    private void loginImpl() {
        LoadPlugin.getInstance().login(this, new IOperateCallback<String>() {
            @Override
            public void onResult(int code, String s) {
                if (code == QYCodeDef.SUCCESS) {
                    Log.d(TAG, "QYGameSDK登录成功！");
                    login.setText("退出");
                    login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            logoutImpl();
                        }
                    });
                } else {
                    Log.d(TAG, "QYGameSDK登录失败！");
                }
            }
        });
    }

    private void setLogoutListener() {
        LoadPlugin.getInstance().setLogoutListener(new IOperateCallback<String>() {
            @Override
            public void onResult(int code, String s) {
                if (code == QYCodeDef.SUCCESS) {
                    Log.d(TAG, "QYGameSDK退出成功！");
                    login.setText("登录");
                    login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loginImpl();
                        }
                    });
                } else {
                    Log.d(TAG, "QYGameSDK退出失败！");
                }
            }
        });
    }

    private void logoutImpl() {
        LoadPlugin.getInstance().logout();
    }

    private void payH5(String payUrl) {
//        QYGameSDK.getInstance().payH5(MainActivity.this, "http://www.373yx.com", payUrl, null);
        Long cliBuyerId = 19000L;
        String cliSellerId = "2018111415564890400010102c2";
        String cpOrderNo = System.currentTimeMillis() + "";
        String cpOrderTitle = "首充一";
        float cpPrice = 0.01f;
        LoadPlugin.getInstance().payH5(RMainActivity2.this, cliBuyerId, cliSellerId, cpOrderNo, cpOrderTitle, cpPrice);
    }

    @Override
    public void onBackPressed() {
        exitImpl();
    }

    private void exitImpl() {
        LoadPlugin.getInstance().uninit(RMainActivity2.this, new IOperateCallback<String>() {
            @Override
            public void onResult(int i, String s) {
                if (i == QYCodeDef.SUCCESS) {
                    System.exit(0);
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged" + newConfig.orientation);
    }

}
