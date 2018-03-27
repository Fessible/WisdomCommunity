package com.example.com.wisdomcommunity.ui.person.set;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;


import com.example.com.wisdomcommunity.R;

import java.io.File;

/**
 * app更新下载后台
 * Created by rhm on 2017/12/28.
 */

public class UpdateService extends Service {
    private String apkURL;
    private String filePath;
    private NotificationManager notificationManager;
    private Notification notification;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        filePath = Environment.getExternalStorageDirectory() + "/MyProject/test.apk";
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            notifyUser("下载失败", "网络错误", 0);
            stopSelf();//结束
        }
        apkURL = intent.getStringExtra("apkUrl");
        notifyUser("开始下载", "开始下载", 0);
        startDownload();


        return super.onStartCommand(intent, flags, startId);
    }

    private void startDownload() {
        UpdateManager.getInstance().startDownloads(apkURL, filePath, new UpdateDownloadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgressChanged(int process, String downloadUrl) {
                notifyUser("正在下载中", "下载中", process);
            }

            @Override
            public void onFinished(float completeSize, String downloadUrl) {
                notifyUser("下载完成", "下载完成", 100);
                stopSelf();
            }

            @Override
            public void onFailure() {
                notifyUser("下载失败", "下载失败", 0);
                stopSelf();
            }
        });
    }

    //更新我们的notification来告知我们的用户当前的下载进度
    public void notifyUser(String result, String reason, int progress) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.icon_login)
                .setContentTitle("下载通知")
                .setContentText(result);

        if (progress > 0 && progress < 100) {
            builder.setProgress(100, progress, false);
        } else {
            builder.setProgress(0, 0, false);
        }
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        builder.setTicker(result);
        builder.setContentIntent(progress >= 100 ? (PendingIntent) getContentIntent() :
                PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
        notification = builder.build();
        notificationManager.notify(0, notification);
    }

    public PendingIntent getContentIntent() {
        File apkFile = new File(filePath);
        if (!apkFile.exists()) {
            return null;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //Android 7.0
//        Uri uri = Uri.parse("file://" + apkFile.toString());
        Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", apkFile);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        //在服务中开启activity必须设置flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        startActivity(intent);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}

