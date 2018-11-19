package com.yiyou.gamesdk.core.ui.dialog.biz;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qygame.qysdk.outer.event.IDialogParam;
import com.qygame.qysdk.outer.util.StringUtils;
import com.yiyou.gamesdk.R;
import com.yiyou.gamesdk.core.api.ApiFacade;
import com.yiyou.gamesdk.core.base.http.volley.bean.QyDataBean;
import com.yiyou.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.yiyou.gamesdk.util.IMEUtil;
import com.yiyou.gamesdk.util.ToastUtils;
import com.yiyou.gamesdk.util.ViewUtils;

import java.util.Map;

public class RealNameAuthController extends BaseAuthViewController {

    private TextView titleTv;
    private EditText edit_real_name_container_name;
    private EditText edit_real_name_container_card_number;
    private Button btn_real_name_container_ensure;
    private Button btn_title_container_close;

    Context mContext;

    public RealNameAuthController(Context context, IDialogParam params) {
        super(context, params);
        mContext = context;
        initView();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.qy_sdk_container_real_name;
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

    private void initView() {
        titleTv = (TextView) findViewById(R.id.tv_title_container_title);
        titleTv.setText(R.string.str_real_name_title);
        edit_real_name_container_name = (EditText) findViewById(R.id.edit_real_name_container_name);
        edit_real_name_container_card_number = (EditText) findViewById(R.id.edit_real_name_container_card_number);
        btn_real_name_container_ensure = (Button) findViewById(R.id.btn_real_name_container_ensure);
        ViewUtils.setViewEnable(btn_real_name_container_ensure, false);
        btn_title_container_close = (Button) findViewById(R.id.btn_title_container_close);
        btn_title_container_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        btn_real_name_container_ensure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IMEUtil.hideIME(RealNameAuthController.this);
                submitRealName(edit_real_name_container_name.getText().toString(), edit_real_name_container_card_number.getText().toString());
            }
        });

        addTextWatcher(edit_real_name_container_name, edit_real_name_container_card_number);
    }

    private void addTextWatcher(EditText... editTexts) {
        for (final EditText editText : editTexts) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    updateLoginButtonState();
                }
            });
        }
    }

    private void updateLoginButtonState() {
        if (edit_real_name_container_name.length() == 0 || edit_real_name_container_card_number.length() == 0) {
            ViewUtils.setViewEnable(btn_real_name_container_ensure, false);
        } else {
            ViewUtils.setViewEnable(btn_real_name_container_ensure, true);
        }
    }

    private void submitRealName(String real_name, String card_no) {
        if (StringUtils.isBlank(real_name) || StringUtils.isBlank(card_no)) {
            ToastUtils.showMsg("请不要输入空字符");
            return;
        }
        ApiFacade.getInstance().realNameAuth(real_name, card_no, new QyRespListener<QyDataBean>() {
            @Override
            public void onNetSucc(String url, Map params, QyDataBean result) {
                super.onNetSucc(url, params, result);
                if (result.getCode() == 1) {
                    ToastUtils.showMsg(R.string.real_name_auth_succ + result.getMsg());
                    close();
                } else {
                    super.onFail(result.getCode(), R.string.real_name_auth_succ + result.getMsg());
                }
            }

            @Override
            public void onFail(int errorNo, String errmsg) {
                super.onFail(errorNo, errmsg);
            }

            @Override
            public void onNetError(String url, Map params, String errno, String errmsg) {
                super.onNetError(url, params, errno, errmsg);
            }

            @Override
            public void onNetworkComplete() {
                super.onNetworkComplete();
//                loadingDialog.dismiss();
            }
        });
    }
}
