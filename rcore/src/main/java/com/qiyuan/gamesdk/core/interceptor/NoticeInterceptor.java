package com.qiyuan.gamesdk.core.interceptor;

import android.content.DialogInterface;

import com.qiyuan.gamesdk.model.NoticeInfo;
import com.qiyuan.gamesdk.util.VersionUtil;
import com.qiyuan.gamesdk.core.api.ApiFacade;
import com.qiyuan.gamesdk.core.base.http.RequestManager;
import com.qiyuan.gamesdk.core.base.http.utils.Urlpath;
import com.qiyuan.gamesdk.core.base.http.volley.HwRequest;
import com.qiyuan.gamesdk.core.base.http.volley.listener.QyRespListener;
import com.qiyuan.gamesdk.core.ui.widget.dialog.CommDialog;
import com.qiyuan.gamesdk.core.ui.widget.dialog.YunWeiDialogView;
import com.qiyuan.gamesdk.model.NoticeInfo;
import com.qygame.qysdk.outer.util.Log;
import com.qiyuan.gamesdk.util.VersionUtil;

import java.util.Map;
import java.util.Random;

/**
 * Created by Win on 2016-4-12.
 */
public class NoticeInterceptor implements InitInterceptor {

    private CommDialog NoticeDialog;
    private static final String TAG = "QYSDK: NoticeInterceptor";

    @Override
    public void intercept(Chain<InitParams> chain) {
        String url;
        Random r = new Random();
        if (r.nextInt(3) == 1){
            url = Urlpath.YUNWEI_1;
        }else{
            url = Urlpath.YUNWEI_2;
        }
        popupNotice(url, chain);
    }

    private void popupNotice(String url, final Chain<InitParams> chain){

        final String YunWeiUrl = url+String.format("/%s/%s", VersionUtil.getSdkVersion(), VersionUtil.getCoreVersion());

        HwRequest request = new HwRequest<>(YunWeiUrl,NoticeInfo.class,new QyRespListener<NoticeInfo>(){

            @Override
            public void onNetSucc(String url, Map<String, String> params, NoticeInfo notice) {
                if(isPopup(notice.getGameid())){
                    if(NoticeDialog == null){
                        CommDialog.Builder builder = new CommDialog.Builder(chain.getData().getActivity());
                        YunWeiDialogView dialogView = new YunWeiDialogView(chain.getData().getContext());
                        dialogView.setTitle(notice.getTitle());
                        dialogView.setHtmlText(notice.getHtmlText());
                        builder.setView(dialogView);
                        NoticeDialog = builder.create();
                        NoticeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                NoticeDialog = null;
                                chain.proceed(chain.getData());
                            }
                        });
                        NoticeDialog.show();
                    }
                }else{
                    chain.proceed(chain.getData());
                }
            }

            @Override
            public void onNetError(String url, Map<String, String> params, String errno, String errmsg) {
                Log.e(TAG, "url: " + url + "params: " + params + "errno: " + errno + "errmsg: " + errmsg);
                chain.proceed(chain.getData());
            }

            @Override
            public void onFail(int errorNo, String errmsg) {
                Log.e(TAG, "errorNo: " + errorNo + "errmsg: " + errmsg);
                chain.proceed(chain.getData());
            }
        });
        RequestManager.getInstance(chain.getData().getContext()).addRequest(request,null);
    }

    private boolean isPopup(String[] gid){
        String ClientGameId = ApiFacade.getInstance().getCurrentGameID();
        if(gid.length ==0){
            return true;
        }
        if(gid.length >0){

            for(int i = 0; i< gid.length;i++){
                if(ClientGameId.equals(gid[i])){
                    return true;
                }
            }
        }
        return false;
    }
}
