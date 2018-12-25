package com.qiyuan.gamesdk.core.ui.dialog.biz.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiyuan.gamesdk.BuildConfig;
import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.CoreManager;

public class ContainerItemTitle4 extends RelativeLayout {

    private TextView backTitleBtn;
    private TextView titleTv;
    private TextView reFreshTitleBtn;
    private View reFreshTitleLine;
    private TextView closeTitleBtn;
    TitleOnclick titleOnclick;

    public interface TitleOnclick {
        void toBack();

        void toRefresh();

        void toClose();
    }

    public ContainerItemTitle4(Context context) {
        this(context, null);
    }

    public ContainerItemTitle4(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContainerItemTitle4(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (BuildConfig.isApp) {
            LayoutInflater.from(CoreManager.getContext()).inflate(R.layout.qy_sdk_container_item_title4, this);
        } else {
            LayoutInflater.from(context).inflate(R.layout.qy_sdk_container_item_title4, this);
        }

        initView();
    }

    private void initView() {
        backTitleBtn = (TextView) findViewById(R.id.btn_title_container_back);
        titleTv = (TextView) findViewById(R.id.tv_title_container_title);
        reFreshTitleBtn = (TextView) findViewById(R.id.btn_title_container_refresh);
        reFreshTitleLine = findViewById(R.id.btn_title_container_refresh_line);
        closeTitleBtn = (TextView) findViewById(R.id.btn_title_container_close);
        backTitleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleOnclick != null) {
                    titleOnclick.toBack();
                }
            }
        });
        reFreshTitleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleOnclick != null) {
                    titleOnclick.toRefresh();
                }
            }
        });
        closeTitleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleOnclick != null) {
                    titleOnclick.toClose();
                }
            }
        });
    }

    /**
     * 顺序为：
     * 左边返回、刷新、右边关闭
     */
    public void setTitleBtnVisibility(boolean isVisibility1, boolean isVisibility2, boolean isVisibility3) {
        backTitleBtn.setVisibility(isVisibility1 ? View.VISIBLE : View.GONE);
        reFreshTitleBtn.setVisibility(isVisibility2 ? View.VISIBLE : View.GONE);
        reFreshTitleLine.setVisibility(isVisibility2 ? View.VISIBLE : View.GONE);
        closeTitleBtn.setVisibility(isVisibility3 ? View.VISIBLE : View.GONE);
    }

    public void setTitle(int resid) {
        titleTv.setText(resid);
    }

    public void setTitle(CharSequence text) {
        titleTv.setText(text);
    }

    public void setTitleOnclick(TitleOnclick titleOnclick) {
        this.titleOnclick = titleOnclick;
    }

}
