package com.qiyuan.gamesdk.core.ui.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qygame.qysdk.outer.event.EventDispatcherAgent;
import com.qygame.qysdk.outer.event.IEventListener;
import com.qygame.qysdk.outer.util.Log;
import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.storage.events.NativeTitleBarTitleContentEvent;
import com.qiyuan.gamesdk.core.storage.events.NativeTitleBarUpdateEvent;
import com.qiyuan.gamesdk.core.ui.fragment.PayCenterFragment;
import com.qiyuan.gamesdk.model.NativeTitleBarUpdateInfo;
import com.qiyuan.gamesdk.util.ExtraDef;
import com.qiyuan.gamesdk.util.PhoneUtils;

/**
 * Created by Nekomimi on 2017/11/14.
 */

public class PayCenterTitleFragment extends Fragment implements View.OnClickListener {



    public static final String TITLE_BAR_ARG = "title_bar_arg";

    private static final String TAG = "QYSDK:PayCenterTitleFragment";

    private View rootView;
    private TextView tvTitleContent;
    private ImageView imgClose;
    private String titleContent = "";
    private static final String TITLE_CONTENT = "title_content";
    private ImageView imgBack;
    private IEventListener onNativeBarUpdateListener, onNativeBarContentListener;


    public static PayCenterTitleFragment newInstance(String content) {
        return newInstance(content, ExtraDef.DisplayStyle.StyleFullScreen);
    }

    public static PayCenterTitleFragment newInstance(String content, int displayStyle) {
        PayCenterTitleFragment fragment = new PayCenterTitleFragment();
        Bundle args = new Bundle();
        args.putString(TITLE_CONTENT, content);
        args.putInt(ExtraDef.EXTRA_DISPLAY_STYLE, displayStyle);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NativeTitleBarUpdateInfo config = null;
        if (getArguments() != null) {
            titleContent = getArguments().getString(TITLE_CONTENT);
            config = getArguments().getParcelable(TITLE_BAR_ARG);
        }

        addEvent();
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.sdk_pay_fragment_title, container, false);
            tvTitleContent = (TextView) rootView.findViewById(R.id.tv_title_content);
            imgClose = (ImageView) rootView.findViewById(R.id.img_close);
            imgBack = (ImageView) rootView.findViewById(R.id.img_back);
            imgClose.setOnClickListener(this);
            imgBack.setOnClickListener(this);
            tvTitleContent.setText(titleContent);
            tvTitleContent.setOnClickListener(this);
        }
        updateViewTitleBar(config);
        if (rootView.getParent() != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
            Log.e("QYViews", "titleBar reuse. remove from parent.");
        }

        return rootView;
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.img_close:
                PhoneUtils.closeInput(getActivity());

                closePayCallback();

                getActivity().finish();

                break;


            case R.id.img_back:
                onclickBack();
                break;

//            case R.id.img_refresh:
//                EventDispatcherAgent.defaultAgent().broadcast(RefreshEvent.TYPE, new RefreshEvent.RefreshEventParam());
//                break;

            case R.id.tv_title_content:

                break;


        }

    }


    private void onclickBack() {
        PhoneUtils.closeInput(getActivity());

        if (getParentFragment() != null && getParentFragment() instanceof PayCenterFragment) {

            ((PayCenterFragment) getParentFragment()).onBackPressed();

        }
//          else {
//        EventDispatcherAgent.defaultAgent().broadcast(FinishFragmentEvent.TYPE_FINISH_FRAGMENT,
//                new FinishFragmentEvent.Param(0, getActivity(), getParentFragment()));
//        }
    }

    private void closePayCallback() {
        if (getParentFragment() instanceof PayCenterFragment) {
            ApiFacade.getInstance().closeNotify();
        }
    }

    private void addEvent() {
        onNativeBarUpdateListener = new IEventListener<NativeTitleBarUpdateEvent.NativeTitleBarUpdateEventParam>() {

            @Override
            public void onEvent(String eventType, NativeTitleBarUpdateEvent.NativeTitleBarUpdateEventParam params) {
                updateViewTitleBar(params.mNativeTitleBarUpdateInfo);
            }
        };


        EventDispatcherAgent.defaultAgent().addEventListener(PayCenterTitleFragment.this,
                NativeTitleBarUpdateEvent.TYPE_ON_NATIVE_TITLE_BAR_UPDATE,
                onNativeBarUpdateListener);


        onNativeBarContentListener = new IEventListener<NativeTitleBarTitleContentEvent.NativeTitleBarTitleContentEventParam>() {

            @Override
            public void onEvent(String eventType, NativeTitleBarTitleContentEvent.NativeTitleBarTitleContentEventParam params) {
                updateViewTitleContent(params.mTitleContent);
            }
        };

        EventDispatcherAgent.defaultAgent().addEventListener(PayCenterTitleFragment.this,
                NativeTitleBarTitleContentEvent.TYPE_ON_NATIVE_TITLE_BAR_TITLE_CONTENT,
                onNativeBarContentListener);

    }

    private void updateViewTitleBar(NativeTitleBarUpdateInfo nativeTitleBarUpdateInfo) {

        if (nativeTitleBarUpdateInfo != null) {
            imgBack.setVisibility(nativeTitleBarUpdateInfo.showBackButton ? View.VISIBLE : View.GONE);
//            imgRefresh.setVisibility(nativeTitleBarUpdateInfo.showRefreshButton ? View.VISIBLE : View.GONE);
            imgClose.setVisibility(nativeTitleBarUpdateInfo.showCloseButton?View.VISIBLE:View.GONE);
        } else {
            Log.w(TAG, "error updateViewTitleBar : invalid title bar config.");
        }

    }

    public void updateViewTitleContent(String mTitleContent) {
        if (tvTitleContent != null)
            tvTitleContent.setText(mTitleContent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventDispatcherAgent.defaultAgent()
                .removeEventListenersBySource(this);
    }
}
