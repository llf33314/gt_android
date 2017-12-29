package com.gt.gtapp.update;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gt.gtapp.R;
import com.gt.gtapp.base.BaseConstant;
import com.gt.gtapp.base.MyApplication;
import com.gt.gtapp.update.bean.AppUpdateBean;
import com.gt.gtapp.update.ui.UpdateDialog;
import com.gt.gtapp.utils.commonutil.AppUtils;
import com.gt.gtapp.utils.commonutil.ConvertUtils;
import com.gt.gtapp.utils.commonutil.LogUtils;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description:
 * Created by jack-lin on 2017/8/23 0023.
 */

public class UpdateManager {
    private final static String TAG = "update";
    private Context context;
    private int type;
    public static final int UPDATE_DIALOG=0;
    public static final int UPDATE_BADGE=1;
    public static final int UPDATE_BADGE_AND_DIALOG=2;
    private String appId = "";
    //String VERSION_URL = "https://deeptel.com.cn/app/79B4DE7C/getInfoByAppId.do?appId=";
    String VERSION_URL = "https://duofriend.com/app/79B4DE7C/getInfoByAppId.do?appId=";

    String APK_FILE_NAME = BaseConstant.UPDATE_NAME+".apk";
    private static final int NEED_UPDATE = 0;
    private static final int DOWNLOADING = 1;
    private static final int DOWNLOAD_FINISH = 2;
    private static final int FAILURE_DOWNLOAD = 3;
    private static final int CANCEL_DOWNLOAD = 4;
    private UpdateDialog askUpdateDialog;
    private Dialog mDownloadDialog;
    private TextView dialogContentTextView;
    private ProgressBar mProgress;
    private boolean cancelUpdate;
    private long breakPoints = -1L;
    private ProgressDownloader downloader;
    private File file;
    private long mTotalBytes;
    private long mContentLength;
    private AppUpdateBean appUpdateBean;
    private Dialog dialog;
    private boolean isNeedUpdate = false;
    private boolean isShowUpdateUI=true;

    private OnTaskFinishListener onTaskFinishListener;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NEED_UPDATE:
                    if (type==UPDATE_DIALOG) {
                        showAskUpdateDialog();
                    } else if (type==UPDATE_BADGE) {
                    }else if(type==UPDATE_BADGE_AND_DIALOG){
                        showAskUpdateDialog();
                    }
                    break;
                case DOWNLOADING:
                    int progress = msg.arg1;
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    if (mDownloadDialog != null) mDownloadDialog.dismiss();
                    installAPK();
                    break;
                case FAILURE_DOWNLOAD:
                    if (mDownloadDialog != null && mDownloadDialog.isShowing()) {
                        dialogContentTextView.setText("下载安装包出现问题");
                        mProgress.setVisibility(View.GONE);
                        break;
                    }
                case CANCEL_DOWNLOAD:
                    if (mDownloadDialog != null) {
                        mDownloadDialog.dismiss();
                    }
                    if (downloader != null) {
                        downloader.pause();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public UpdateManager(Context context, String appId,int type) {
        this.type=type;
        this.appId = appId;
        this.context = context;
    }

    public void setOnTaskFinishListener(OnTaskFinishListener onTaskFinishListener) {
        this.onTaskFinishListener = onTaskFinishListener;
    }

    public void requestUpdate() {
        RequestVersionTask versionTask = new RequestVersionTask();
        versionTask.execute();

    }

    OkHttpClient client = new OkHttpClient();

    public String requestByGet() {


        Request request = new Request.Builder()
                .url(VERSION_URL+appId)
                .get()
                .build();
        LogUtils.d("requestUpdate", "request==" + request.toString());
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                Log.e(TAG, "IOException:" + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean compareVersion(int versionCode) {

        int currentVersionCode = AppUtils.getAppVersionCode();
        if (versionCode > 0 && versionCode != currentVersionCode) {
            if (versionCode > currentVersionCode) {
                mHandler.sendEmptyMessage(NEED_UPDATE);
                return true;
            }
        }
        return false;
    }

    private class RequestVersionTask extends AsyncTask {
        @Override
        protected void onPostExecute(Object o) {
            if (onTaskFinishListener != null) {
                onTaskFinishListener.onTaskResult(isNeedUpdate);
            }
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            String response = requestByGet();
            LogUtils.d(TAG, "response==" + response);
            String str = ConvertUtils.unicode2String(response);
            AppUpdateBean updateBean = new Gson().fromJson(str, AppUpdateBean.class);
            if (updateBean != null&&!TextUtils.isEmpty(updateBean.appVersionCode)) {
                appUpdateBean = updateBean;
                try {
                    isNeedUpdate = compareVersion(Integer.parseInt(updateBean.appVersionCode));
                    Hawk.put("isNeedReLogin",Integer.parseInt(updateBean.remarks));
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
                Hawk.put("newestVersion",updateBean.appVersionCode);
            }
            return null;
        }
    }
    private void showAskUpdateDialog(){
        if (askUpdateDialog==null){
            askUpdateDialog=new UpdateDialog(context,context.getResources().getText(R.string.app_name).toString()
                    +appUpdateBean.appVersionName+"版本已经上线"
                    ,"是否立即更新版本",R.style.HttpRequestDialogStyle);
            askUpdateDialog.getConfirmButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mProgress=askUpdateDialog.getProgressBar();
                    askUpdateDialog.getProgressBar().setVisibility(View.VISIBLE);
                    askUpdateDialog.getContent().setText("正在更新");
                    askUpdateDialog.getConfirmButton().setVisibility(View.GONE);
                    askUpdateDialog.getCancelButton().setVisibility(View.GONE);
                    downloadAPK(appUpdateBean.apkUrl, APK_FILE_NAME);
                    askUpdateDialog.setCanceledOnTouchOutside(false);
                    askUpdateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            mHandler.sendEmptyMessage(CANCEL_DOWNLOAD);

                            Hawk.put("UPDATE_DIALOG-"+ Hawk.get("newestVersion",""),false);
                        }
                    });
                }
            });
        }
        askUpdateDialog.getCancelButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             askUpdateDialog.dismiss();
            }
        });
        askUpdateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                MyApplication.setNeedUpdate(false);
                LogUtils.d("setNeedUpdate =false");
            }
        });
        if (!askUpdateDialog.isShowing()){
            askUpdateDialog.show();
        }
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.start:
                    LogUtils.d(TAG, "onClickListener start");
                    LogUtils.d(TAG, "downloadAPK appUpdateBean.apkUrl=" + appUpdateBean.apkUrl);
                    downloadAPK(appUpdateBean.apkUrl, APK_FILE_NAME);
                    break;
                case R.id.cancel:
                    mHandler.sendEmptyMessage(CANCEL_DOWNLOAD);
                    // 设置取消状态
                    break;
            }
        }
    };

    private void downloadAPK(String url, final String fileName) {
        if (breakPoints == -1L) {
            breakPoints = 0L;
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), APK_FILE_NAME);
            downloader = new ProgressDownloader(url, file, listener);
            downloader.download(0L);
        } else if (breakPoints > 0) {
            downloader.download(breakPoints);

        }
    }

    private void installAPK() {
        if (mDownloadDialog != null) {
            mDownloadDialog.dismiss();
        }
        File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                , APK_FILE_NAME);
        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(context, "com.gt.gtapp.fileprovider", apkFile);//在AndroidManifest中的android:authorities值
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            context.startActivity(install);
        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        }
//        if (mDownloadDialog != null) mDownloadDialog.dismiss();
//        //安装应用
//        LogUtils.i(TAG, "installAPK APK_FILE_NAME=" + APK_FILE_NAME);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                        , APK_FILE_NAME)),
//                "application/vnd.android.package-archive");
//        context.startActivity(intent);

    }
    private ProgressResponseBody.ProgressListener listener = new ProgressResponseBody.ProgressListener() {
        @Override
        public void onPreExecute(long contentLength) {
            // 文件总长只需记录一次，要注意断点续传后的contentLength只是剩余部分的长度
            if (mContentLength == 0L) {
                mContentLength = contentLength;
                mProgress.setMax((int) (mContentLength / 1024));
            }
        }

        @Override
        public void update(long totalBytes, boolean done) {
            mTotalBytes = totalBytes + breakPoints;
            mProgress.setProgress((int) (totalBytes + breakPoints) / 1024);
            if (done) {
                askUpdateDialog.dismiss();
                installAPK();
            }
        }
    };
}
