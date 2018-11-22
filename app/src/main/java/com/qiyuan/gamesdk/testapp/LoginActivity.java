package com.qiyuan.gamesdk.testapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gamesdk.shouyouba.tzsy.R;
import com.qygame.qysdk.container.QYGameSDK;
import com.qygame.qysdk.outer.IOperateCallback;
import com.qygame.qysdk.outer.consts.OrientationDef;
import com.qygame.qysdk.outer.consts.QYCodeDef;
import com.qygame.qysdk.outer.model.GameParamInfo;
import com.qygame.qysdk.outer.util.Log;


public class LoginActivity extends FragmentActivity implements MainFragment.MainFragmentBtnClickListener, View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private MainFragment mainFragment = new MainFragment();
    private PayFragment payFragment = new PayFragment();
    private WindowManager windowManager;
    private LinearLayout layoutRoot;
    private int screenWidth;
    private int screenHeigth;
    private boolean isLandscape = true;
    private int mCurrentView = 0; //0:LoginActivity,1:MainFragment ,2:PayFragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate" + android.os.Process.myPid());
        findViewById(R.id.choose).setOnClickListener(this);
        findViewById(R.id.file).setOnClickListener(this);
        initSdk();
    }


    private void initSdk() {

        GameParamInfo paramInfo = new GameParamInfo();
        paramInfo.setGameId("2018111415564890400010102c2");
        paramInfo.setSdkKey("7dc18ce3418bcfb6ffa6e72ba1943884");

        mCurrentView = 0;  //0:LoginActivity,1:MainFragment ,2:PayFragmen

        QYGameSDK.getInstance().init(this, paramInfo, false, this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ?
                Configuration.ORIENTATION_LANDSCAPE : Configuration.ORIENTATION_PORTRAIT, new IOperateCallback<String>() {
            @Override
            public void onResult(int i, String s) {
                if (i == 0) {
                    loginImpl();
                }
            }
        });
    }


    private void loginImpl() {
        GameParamInfo paramInfo = new GameParamInfo();
        paramInfo.setGameId("2018111415564890400010102c2");
//        正式环境
//        paramInfo.setSdkKey("c9f3532184ecd7e2ddb7ac9bcac35c7c");
        //测试环境
        paramInfo.setSdkKey("7dc18ce3418bcfb6ffa6e72ba1943884");
        int orientation = this.getResources().getConfiguration().orientation;
        Log.d(TAG, "当前orientation = " + orientation);
        QYGameSDK.getInstance().init(this, paramInfo, false, orientation == Configuration.ORIENTATION_LANDSCAPE ?
                Configuration.ORIENTATION_LANDSCAPE : Configuration.ORIENTATION_PORTRAIT, new IOperateCallback<String>() {
            @Override
            public void onResult(int i, String s) {
                if (i == 0) {
                    QYGameSDK.getInstance().setLogoutListener(new IOperateCallback<String>() {
                        @Override
                        public void onResult(int code, String msg) {
                            if (code == QYCodeDef.LOGOUT_NO_INIT || code == QYCodeDef.LOGOUT_FAIL) {
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG)
                                        .show();
                            } else if (code == QYCodeDef.LOGOUT_NO_LOGIN || code == QYCodeDef.SUCCESS) {
                                QYGameSDK.getInstance().hideFloatView(LoginActivity.this);
                                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                ft.remove(mainFragment);
                                ft.commitAllowingStateLoss();
                                findViewById(R.id.choose).setVisibility(View.VISIBLE);
                                findViewById(R.id.file).setVisibility(View.VISIBLE);
                                loginImpl();
                                mCurrentView = 0; //0:LoginActivity,1:MainFragment ,2:PayFragment
                            }
                        }
                    });
                }
            }
        });
        QYGameSDK.getInstance().login(this, new IOperateCallback<String>() {
            @Override
            public void onResult(int code, String s) {
                if (code == QYCodeDef.SUCCESS) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.container, mainFragment);
                    ft.commitAllowingStateLoss();
                    findViewById(R.id.choose).setVisibility(View.GONE);
                    findViewById(R.id.file).setVisibility(View.GONE);
                    mCurrentView = 1;  //0:LoginActivity,1:MainFragment ,2:PayFragment
                    QYGameSDK.getInstance().showFloatView(LoginActivity.this);
                    // TODO: cp可以在这里检验login
                    Log.d(TAG, "session: " + QYGameSDK.getInstance().getSession());
                    Toast.makeText(LoginActivity.this, "session: " + QYGameSDK.getInstance().getSession() + "\n" + "uid: " + QYGameSDK.getInstance().getUid(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loginVisitorsImpl() {
        QYGameSDK.getInstance().loginVisitors(this, new IOperateCallback<String>() {
            @Override
            public void onResult(int code, String s) {
                if (code == QYCodeDef.SUCCESS) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.container, mainFragment);
                    ft.commitAllowingStateLoss();
                    findViewById(R.id.choose).setVisibility(View.GONE);
                    findViewById(R.id.file).setVisibility(View.GONE);
                    mCurrentView = 1;  //0:LoginActivity,1:MainFragment ,2:PayFragment
                    QYGameSDK.getInstance().showFloatView(LoginActivity.this);
                    // TODO: cp可以在这里检验login
                    Log.d(TAG, "session: " + QYGameSDK.getInstance().getSession());
                    Toast.makeText(LoginActivity.this, "session: " + QYGameSDK.getInstance().getSession() + "\n" + "uid: " + QYGameSDK.getInstance().getUid(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loginAuto() {
        QYGameSDK.getInstance().loginAuto(this, new IOperateCallback<String>() {
            @Override
            public void onResult(int code, String s) {
                if (code == QYCodeDef.SUCCESS) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.container, mainFragment);
                    ft.commitAllowingStateLoss();
                    findViewById(R.id.choose).setVisibility(View.GONE);
                    findViewById(R.id.file).setVisibility(View.GONE);
                    mCurrentView = 1;  //0:LoginActivity,1:MainFragment ,2:PayFragment
                    QYGameSDK.getInstance().showFloatView(LoginActivity.this);
                    // TODO: cp可以在这里检验login
                    Log.d(TAG, "session: " + QYGameSDK.getInstance().getSession());
                    Toast.makeText(LoginActivity.this, "session: " + QYGameSDK.getInstance().getSession() + "\n" + "uid: " + QYGameSDK.getInstance().getUid(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void exitImpl() {
        QYGameSDK.getInstance().uninit(LoginActivity.this, new IOperateCallback<String>() {
            @Override
            public void onResult(int i, String s) {
                if (i == QYCodeDef.SUCCESS) {
                    System.exit(0);
                }
            }
        });
    }

    private void logoutImpl() {
        QYGameSDK.getInstance().logout();
        mCurrentView = 0;  //0:LoginActivity,1:MainFragment ,2:PayFragment
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        Log.d(TAG, "onConfigurationChanged");

        View layout = View.inflate(this, R.layout.activity_main, null);
        layoutRoot = (LinearLayout) layout.findViewById(R.id.layout_root);
        windowManager = (WindowManager) LoginActivity.this.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        screenWidth = windowManager.getDefaultDisplay().getWidth();
        screenHeigth = windowManager.getDefaultDisplay().getHeight();

        if (screenHeigth > screenWidth) {
            isLandscape = false;
            layoutRoot.setBackgroundResource(R.drawable.sdk_bg1);
        } else {
            isLandscape = true;
            layoutRoot.setBackgroundResource(R.drawable.sdk_bg);
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.attach(mainFragment);
        ft.commitAllowingStateLoss();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onMainFragmentLogoutClick() {
        logoutImpl();
    }

    @Override
    public void onMainFragmentExitClick() {
        exitImpl();
    }

    @Override
    public void onMainFragmentPayClick() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, payFragment);
        ft.commitAllowingStateLoss();
        mCurrentView = 2;  //0:LoginActivity,1:MainFragment ,2:PayFragment
    }

    @Override
    public void onMainFragmentChangeOrientationClick() {
        int orientation = OrientationDef.LANDSCAPE;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.detach(mainFragment);
        ft.commitAllowingStateLoss();
        if (isLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            orientation = OrientationDef.PORTRAIT;
        } else {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
//        GameSDK.defaultSDK().setOrientation(orientation);

    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");

        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        QYGameSDK.getInstance().hideFloatView(LoginActivity.this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        QYGameSDK.getInstance().showFloatView(LoginActivity.this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.choose) {

            loginImpl();

        } else if (v.getId() == R.id.file) {
            startActivity(new Intent(this, FileBrowserActivity.class));
        }


    }


    @Override
    public void onBackPressed() {
        //mCurrentView : 0:LoginActivity,1:MainFragment ,2:PayFragment
        if (mCurrentView == 2) {
            if (!payFragment.onBackPressed()) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, mainFragment);
                ft.commitAllowingStateLoss();
                mCurrentView = 1;
            }
        } else {
            exitImpl();
        }
    }
}
