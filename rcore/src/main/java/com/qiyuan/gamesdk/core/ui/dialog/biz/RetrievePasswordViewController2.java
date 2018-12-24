package com.qiyuan.gamesdk.core.ui.dialog.biz;

import android.content.Context;
import android.widget.Button;

import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.ui.dialog.ViewControllerNavigator;
import com.qiyuan.gamesdk.core.ui.dialog.biz.View.ContainerItemTitle4;
import com.qygame.qysdk.outer.event.IDialogParam;

public class RetrievePasswordViewController2 extends BaseAuthViewController {

    public Context context;
    ContainerItemTitle4 containerItemTitle4;
    Button btn_enter_game;

//    ContainerItemBottom2 containerItemBottom2;

    public RetrievePasswordViewController2(Context context, IDialogParam params) {
        super(context, params);
        this.context = context;
        initView();
    }

    private void initView() {
        containerItemTitle4 = (ContainerItemTitle4) findViewById(R.id.containerItemTitle4);
        containerItemTitle4.setTitle(R.string.str_qiyuan_sdk_game_title);
        containerItemTitle4.setTitleOnclick(new ContainerItemTitle4.TitleOnclick() {
            @Override
            public void toBack() {
                ViewControllerNavigator.getInstance().toRegister2(getDialogParam());
            }

            @Override
            public void toRefresh() {

            }

            @Override
            public void toClose() {
                close();
            }
        });
        btn_enter_game = (Button) findViewById(R.id.btn_has_registered_container_enter_game);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.qy_sdk_container_retrieve_password2;
    }

}
