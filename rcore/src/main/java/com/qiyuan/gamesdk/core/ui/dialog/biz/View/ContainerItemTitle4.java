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

    private TextView backTitleContainerBtn;
    private TextView titleTv;
    private TextView closeTitleContainerBtn;
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
        backTitleContainerBtn = (TextView) findViewById(R.id.btn_title_container_back);
        titleTv = (TextView) findViewById(R.id.tv_title_container_title);
        closeTitleContainerBtn = (TextView) findViewById(R.id.btn_title_container_close);
        backTitleContainerBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleOnclick != null) {
                    titleOnclick.toBack();
                }
//                ViewControllerNavigator.getInstance().close();
            }
        });
        closeTitleContainerBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleOnclick != null) {
                    titleOnclick.toClose();
                }
//                ViewControllerNavigator.getInstance().close();
            }
        });
    }

    public void setTitleOnclick(TitleOnclick titleOnclick) {
        this.titleOnclick = titleOnclick;
    }

}
