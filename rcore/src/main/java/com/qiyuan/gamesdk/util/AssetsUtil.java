package com.qiyuan.gamesdk.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AssetsUtil {
    private AssetsUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    /**
     * 加载assets目录下的网页,返回一个路径
     * webView.loadUrl("file:///android_asset/html/index.htmll");
     *
     * @param htmlFileName 带后缀 如 index.html
     * @return
     */
    public static String getHtml(String htmlFileName) {
        return "file:///android_asset/html/" + htmlFileName;
    }

    /**
     * 加载assets目录下的图片资源
     *
     * @param ctx
     * @param fileName 带后缀
     * @return
     */
    public static Bitmap getBitmap(Context ctx, String fileName) {
        InputStream is = null;
        try {
            is = ctx.getResources().getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

    /**
     * 加载assets目录下文本文件
     *
     * @param ctx
     * @param fileName 带后缀
     * @return
     */
    public static String getFile(Context ctx, String fileName) {
        String result = "";
        try {
            InputStream in = ctx.getResources().getAssets().open(fileName);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 加载assets目录下音乐
     *
     * @param ctx
     * @param musicFileName 带后缀
     */
    public static void openMusic(Context ctx, String musicFileName) {
        AssetFileDescriptor afd = null;
        MediaPlayer mPlayer = new MediaPlayer();
        try {
            // 打开指定音乐文件,获取assets目录下指定文件的AssetFileDescriptor对象
            afd = ctx.getResources().getAssets().openFd(musicFileName);

            mPlayer.reset();
            // 使用MediaPlayer加载指定的声音文件。
            mPlayer.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            // 准备声音
            mPlayer.prepare();
            // 播放
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从assets目录中复制整个文件夹内容
     *
     * @param context Context 使用CopyFiles类的Activity
     * @param oldPath String  原文件路径  如：/aa
     * @param newPath String  复制后路径  如：xx:/bb/cc
     */
    public static void copyFilesFassets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //如果捕捉到错误则通知UI线程
//            MainActivity.handler.sendEmptyMessage(COPY_FALSE);
        }
    }
}
