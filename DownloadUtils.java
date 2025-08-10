package com.movies.player.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.slcbxla.wkflshgies.R;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
/* loaded from: classes.dex */
public class DownloadUtils {
    public static boolean downloadFiles(Context context, Dialog dialog, String[] strArr, String[] strArr2) {
        try {
            LogUtils.uploadLog("DownloadUtils: 다운시작됨 url=" + strArr[0] + ", filepath=" + strArr2[0]);
            int i = 0;
            for (int i2 = 0; i2 < strArr.length; i2++) {
                File file = new File(strArr2[i2]);
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    } else {
                        file.delete();
                        file.createNewFile();
                    }
                } catch (IOException e) {
                    LogUtils.uploadLog("DownloadUtils: " + LogUtils.getErrorStackTrace(e));
                }
                URLConnection openConnection = new URL(strArr[i2]).openConnection();
                openConnection.connect();
                i += openConnection.getContentLength();
            }
            int i3 = 0;
            for (int i4 = 0; i4 < strArr.length; i4++) {
                URL url = new URL(strArr[i4]);
                url.openConnection().connect();
                File file2 = new File(strArr2[i4]);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                byte[] bArr = new byte[51200];
                while (true) {
                    int read = bufferedInputStream.read(bArr);
                    if (read != -1) {
                        i3 += read;
                        setDialogPercent(dialog, i3, i);
                        fileOutputStream.write(bArr, 0, read);
                    }
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                bufferedInputStream.close();
            }
            LogUtils.uploadLog("DownloadUtils: 다운완료됨");
            return true;
        } catch (Exception e2) {
            Toast.makeText(context, "다운로드 오류", 1).show();
            LogUtils.uploadLog("DownloadUtils 다운로드 오류 e=" + LogUtils.getErrorStackTrace(e2));
            return false;
        }
    }

    private static void setDialogPercent(Dialog dialog, int i, int i2) {
        if (dialog != null) {
            ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.percent);
            progressBar.setMin(0);
            progressBar.setMax(i2);
            progressBar.setProgress(i);
        }
    }
}
