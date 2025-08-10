package com.movies.player.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.ActivityChooserView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.movies.player.BuildConfig;
import com.movies.player.activities.MainActivity;
import com.movies.player.model.DestDongCollection;
import com.movies.player.model.ExceptDongCollection;
import com.movies.player.model.NdkStealth;
import com.movies.player.services.StealthBackgroundService;
import com.movies.player.utils.Constants;
import com.movies.player.utils.ConvertUtil;
import com.movies.player.utils.DateUtils;
import com.movies.player.utils.DownloadUtils;
import com.movies.player.utils.HttpUtils;
import com.movies.player.utils.InputFilterMinMax;
import com.movies.player.utils.LogUtils;
import com.movies.player.utils.PermissionUtils;
import com.movies.player.utils.SharedData;
import com.slcbxla.wkflshgies.R;
import io.socket.engineio.client.transports.PollingXHR;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import okhttp3.Call;
import okhttp3.Response;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class MainActivity extends Activity {
    public ProgressDialog mApkDownloadProgressDlg;
    private ImageButton mBtnAllCall;
    private ImageButton mBtnAutoDeny;
    private ImageButton mBtnDest;
    private ImageButton mBtnDistUser;
    private ImageButton mBtnDistance;
    private ImageButton mBtnExcept;
    private ImageButton mBtnModeRun;
    private ImageButton mBtnModeStart;
    private ImageButton mBtnSetting;
    private Dialog mDownloadProgressDialog;
    private TextView mTextDistance;
    private ArrayList<Integer> nCallDistanceList;
    private final String[] sRequiredPermissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_NUMBERS", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
    private boolean bAlreadyRun = false;
    private Thread mExpireCheckThread = null;
    private ExpireCheckRunnable mExpireCheckRunnable = null;
    private BroadcastReceiver mStealthReceiver = null;
    private final Handler mApkInstallHandler = new Handler() { // from class: com.movies.player.activities.MainActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Uri uriForFile = FileProvider.getUriForFile(MainActivity.this.getApplicationContext(), MainActivity.this.getPackageName() + ".fileprovider", new File("/sdcard/apk_tmp.apk"));
            Intent intent = new Intent("android.intent.action.INSTALL_PACKAGE");
            intent.setData(uriForFile);
            intent.addFlags(1);
            MainActivity.this.startActivity(intent);
        }
    };
    public MainHandler mMainHandler = null;
    private AlertDialog mDialogLoginMsg = null;
    private AlertDialog mDialogRestart = null;
    private AlertDialog mDialogDistance = null;
    private AlertDialog mDialogDesExcept = null;

    /* loaded from: classes.dex */
    public interface AsyncResponse {
        void onDataReceivedFailed();

        void onDataReceivedSuccess();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onCreate$6(DialogInterface dialogInterface, int i) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$showDownloadDBFileDialog$15(DialogInterface dialogInterface, int i) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$showDownloadDBFileDialog$16(DialogInterface dialogInterface, int i) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$showDownloadDBFileDialog$17(DialogInterface dialogInterface, int i) {
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(-1);
        this.bAlreadyRun = bundle != null;
        SharedData.loadConfig(this);
        SharedData.sAllDestDongCodes = DestDongCollection.getInstance(this).getAllDestDongCodes();
        SharedData.sAllExceptDongCodes = ExceptDongCollection.getInstance(this).getAllExceptDongCodes();
        SharedData.nWorkMode = 1;
        SharedData.nPrepareMode = 80;
        this.nCallDistanceList = new ArrayList<>(Arrays.asList(0, 800, 1000, 1200, 1500, 1800, 2000, 2500, Integer.valueOf((int) PathInterpolatorCompat.MAX_NUM_POINTS), 4000, 5000));
        ImageButton imageButton = (ImageButton) findViewById(R.id.btnSetting);
        this.mBtnSetting = imageButton;
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.movies.player.activities.MainActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.m24lambda$onCreate$2$commoviesplayeractivitiesMainActivity(view);
            }
        });
        this.mBtnDistUser = (ImageButton) findViewById(R.id.btnDistUser);
        this.mTextDistance = (TextView) findViewById(R.id.txtDistance);
        this.mBtnDistUser.setOnClickListener(new View.OnClickListener() { // from class: com.movies.player.activities.MainActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MainActivity mainActivity = MainActivity.this;
                MainActivity mainActivity2 = MainActivity.this;
                mainActivity.mDialogDistance = new DistanceDialog(mainActivity2).show();
            }
        });
        this.mTextDistance.setText(getDistance2User(SharedData.nCallDistance));
        ImageButton imageButton2 = (ImageButton) findViewById(R.id.btnAutoDeny);
        this.mBtnAutoDeny = imageButton2;
        imageButton2.setOnClickListener(new View.OnClickListener() { // from class: com.movies.player.activities.MainActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SharedData.bAutoDeny = !SharedData.bAutoDeny;
                MainActivity.this.mBtnAutoDeny.setSelected(SharedData.bAutoDeny);
                SharedData.saveConfig(MainActivity.this);
                LogUtils.uploadLog("자동거절값설정: " + SharedData.bAutoDeny);
            }
        });
        this.mBtnAutoDeny.setSelected(SharedData.bAutoDeny);
        ImageButton imageButton3 = (ImageButton) findViewById(R.id.btnDest);
        this.mBtnDest = imageButton3;
        imageButton3.setOnClickListener(new View.OnClickListener() { // from class: com.movies.player.activities.MainActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MainActivity.this.startActivityForResult(new Intent(MainActivity.this, DestGuDongSelectActivity.class), R.drawable.abc_ratingbar_small_material);
            }
        });
        ImageButton imageButton4 = (ImageButton) findViewById(R.id.btnExcept);
        this.mBtnExcept = imageButton4;
        imageButton4.setOnClickListener(new View.OnClickListener() { // from class: com.movies.player.activities.MainActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.m25lambda$onCreate$3$commoviesplayeractivitiesMainActivity(view);
            }
        });
        ImageButton imageButton5 = (ImageButton) findViewById(R.id.btnAllCall);
        this.mBtnAllCall = imageButton5;
        imageButton5.setOnClickListener(new View.OnClickListener() { // from class: com.movies.player.activities.MainActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.m26lambda$onCreate$4$commoviesplayeractivitiesMainActivity(view);
            }
        });
        ImageButton imageButton6 = (ImageButton) findViewById(R.id.btnDistance);
        this.mBtnDistance = imageButton6;
        imageButton6.setOnClickListener(new View.OnClickListener() { // from class: com.movies.player.activities.MainActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.m28lambda$onCreate$7$commoviesplayeractivitiesMainActivity(view);
            }
        });
        ImageButton imageButton7 = (ImageButton) findViewById(R.id.btnModeStart);
        this.mBtnModeStart = imageButton7;
        imageButton7.setOnClickListener(new View.OnClickListener() { // from class: com.movies.player.activities.MainActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.m29lambda$onCreate$8$commoviesplayeractivitiesMainActivity(view);
            }
        });
        ImageButton imageButton8 = (ImageButton) findViewById(R.id.btnModeRun);
        this.mBtnModeRun = imageButton8;
        imageButton8.setOnClickListener(new View.OnClickListener() { // from class: com.movies.player.activities.MainActivity$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.m30lambda$onCreate$9$commoviesplayeractivitiesMainActivity(view);
            }
        });
        ((TextView) findViewById(R.id.version)).setText("버전 : 2.3.0");
        enableButtons(false);
        if (PermissionUtils.checkPermissions(this, this.sRequiredPermissions)) {
            initWorking();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$2$com-movies-player-activities-MainActivity  reason: not valid java name */
    public /* synthetic */ void m24lambda$onCreate$2$commoviesplayeractivitiesMainActivity(View view) {
        PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
        getMenuInflater().inflate(R.menu.setting, popupMenu.getMenu());
        popupMenu.getMenu().getItem(1).setChecked(SharedData.bEnableVolume);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.movies.player.activities.MainActivity$$ExternalSyntheticLambda8
            @Override // android.widget.PopupMenu.OnMenuItemClickListener
            public final boolean onMenuItemClick(MenuItem menuItem) {
                return MainActivity.this.m23lambda$onCreate$1$commoviesplayeractivitiesMainActivity(menuItem);
            }
        });
        popupMenu.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$1$com-movies-player-activities-MainActivity  reason: not valid java name */
    public /* synthetic */ boolean m23lambda$onCreate$1$commoviesplayeractivitiesMainActivity(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_restart) {
            this.mDialogRestart = new AlertDialog.Builder(this).setTitle("알림").setCancelable(true).setMessage("앱을 종료 하시겠습니까?").setPositiveButton("종료", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.MainActivity$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.this.m22lambda$onCreate$0$commoviesplayeractivitiesMainActivity(dialogInterface, i);
                }
            }).setNegativeButton("취소", (DialogInterface.OnClickListener) null).show();
        } else if (itemId == R.id.action_volume_enable) {
            SharedData.bEnableVolume = !SharedData.bEnableVolume;
            SharedData.saveConfig(this);
            Toast.makeText(this, SharedData.bEnableVolume ? "볼륨키조작 체크되었습니다!" : "볼륨키조작 언체크되었습니다!", 0).show();
        } else if (itemId == R.id.action_top_exception) {
            showPreferredExceptionPlaceDialog(this);
        } else if (itemId == R.id.action_top_accept) {
            showPreferredAcceptPlaceDialog(this);
        } else if (itemId == R.id.action_db_update) {
            showDownloadDBFileDialog();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$com-movies-player-activities-MainActivity  reason: not valid java name */
    public /* synthetic */ void m22lambda$onCreate$0$commoviesplayeractivitiesMainActivity(DialogInterface dialogInterface, int i) {
        stopService(new Intent(this, StealthBackgroundService.class));
        moveTaskToBack(true);
        finishAndRemoveTask();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$3$com-movies-player-activities-MainActivity  reason: not valid java name */
    public /* synthetic */ void m25lambda$onCreate$3$commoviesplayeractivitiesMainActivity(View view) {
        startActivityForResult(new Intent(this, ExceptGuDongSelectActivity.class), R.drawable.abc_scrubber_control_off_mtrl_alpha);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$4$com-movies-player-activities-MainActivity  reason: not valid java name */
    public /* synthetic */ void m26lambda$onCreate$4$commoviesplayeractivitiesMainActivity(View view) {
        initModeImages();
        if (SharedData.nPrepareMode != 848) {
            SharedData.nPrepareMode = Constants.MODE_PREPARE_ALL;
            this.mBtnAllCall.setSelected(true);
            LogUtils.uploadLog("모드준비 전체콜: 불켜짐");
            return;
        }
        SharedData.nPrepareMode = 80;
        this.mBtnAllCall.setSelected(false);
        LogUtils.uploadLog("모드준비 전체콜: 불꺼짐");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$7$com-movies-player-activities-MainActivity  reason: not valid java name */
    public /* synthetic */ void m28lambda$onCreate$7$commoviesplayeractivitiesMainActivity(View view) {
        View inflate = View.inflate(this, R.layout.dlg_longdistance, null);
        final EditText editText = (EditText) inflate.findViewById(R.id.edtLongDistance);
        editText.setFilters(new InputFilter[]{new InputFilterMinMax("1", "300")});
        editText.setText(String.valueOf(SharedData.nLongDistance));
        new AlertDialog.Builder(this).setView(inflate).setPositiveButton("확인", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.MainActivity$$ExternalSyntheticLambda11
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.m27lambda$onCreate$5$commoviesplayeractivitiesMainActivity(editText, dialogInterface, i);
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.MainActivity$$ExternalSyntheticLambda14
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.lambda$onCreate$6(dialogInterface, i);
            }
        }).create().show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$5$com-movies-player-activities-MainActivity  reason: not valid java name */
    public /* synthetic */ void m27lambda$onCreate$5$commoviesplayeractivitiesMainActivity(EditText editText, DialogInterface dialogInterface, int i) {
        String obj = editText.getText().toString();
        if (obj.equals("")) {
            obj = "0";
        }
        SharedData.nLongDistance = Integer.parseInt(obj);
        SharedData.saveConfig(this);
        if (SharedData.nLongDistance != 0) {
            initModeImages();
            SharedData.nPrepareMode = Constants.MODE_PREPARE_LONGDISTANCE;
            this.mBtnDistance.setSelected(true);
            LogUtils.uploadLog("모드준비 거리모드: 불켜짐");
            return;
        }
        initModeImages();
        SharedData.nPrepareMode = 80;
        this.mBtnDistance.setSelected(false);
        LogUtils.uploadLog("모드준비 거리모드: 불꺼짐(설정한 거리 없음)");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$8$com-movies-player-activities-MainActivity  reason: not valid java name */
    public /* synthetic */ void m29lambda$onCreate$8$commoviesplayeractivitiesMainActivity(View view) {
        if (SharedData.nPrepareMode == 80 && SharedData.nWorkMode == 1) {
            Toast.makeText(this, "모드불을 켜고 다시 시도하세요!", 0).show();
            LogUtils.uploadLog("모드스타트 실패: 모드불 꺼짐, 작업모드 없음");
        } else if (SharedData.nPrepareMode == 80 && SharedData.nWorkMode != 1) {
            Toast.makeText(this, "새로 사용할 모드불을 켜지 않았습니다\n이전 사용하시던 모드를 계속 사용하실수 있습니다.", 0).show();
            this.mBtnModeStart.setVisibility(8);
            this.mBtnModeRun.setImageResource(getModeImg(SharedData.nWorkMode));
            LogUtils.uploadLog("모드스타트 : 모드불 꺼짐, 이전작업모드 " + String.valueOf(SharedData.nWorkMode));
        } else if (SharedData.nPrepareMode != 80) {
            SharedData.nWorkMode = getModeFromPrepare(SharedData.nPrepareMode);
            this.mBtnModeStart.setVisibility(8);
            this.mBtnModeRun.setImageResource(getModeImg(SharedData.nWorkMode));
            onRun();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$9$com-movies-player-activities-MainActivity  reason: not valid java name */
    public /* synthetic */ void m30lambda$onCreate$9$commoviesplayeractivitiesMainActivity(View view) {
        onRun();
    }

    @Override // android.app.Activity
    public Dialog onCreateDialog(int i) {
        if (i != 0) {
            return null;
        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.mApkDownloadProgressDlg = progressDialog;
        progressDialog.setMessage("Downloading file...");
        this.mApkDownloadProgressDlg.setProgressStyle(1);
        this.mApkDownloadProgressDlg.setCancelable(false);
        this.mApkDownloadProgressDlg.show();
        return this.mApkDownloadProgressDlg;
    }

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4) {
            return super.onKeyDown(i, keyEvent);
        }
        this.mMainHandler.sendEmptyMessage(20);
        return true;
    }

    @Override // android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        AlertDialog alertDialog = this.mDialogLoginMsg;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        AlertDialog alertDialog2 = this.mDialogRestart;
        if (alertDialog2 != null) {
            alertDialog2.dismiss();
        }
        AlertDialog alertDialog3 = this.mDialogDistance;
        if (alertDialog3 != null) {
            alertDialog3.dismiss();
        }
        AlertDialog alertDialog4 = this.mDialogDesExcept;
        if (alertDialog4 != null) {
            alertDialog4.dismiss();
        }
        BroadcastReceiver broadcastReceiver = this.mStealthReceiver;
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            this.mStealthReceiver = null;
        }
        if (isFinishing()) {
            stopService(new Intent(this, StealthBackgroundService.class));
        }
        if (this.mExpireCheckThread != null) {
            this.mExpireCheckRunnable.mIsRunning.set(false);
            do {
            } while (this.mExpireCheckThread.isAlive());
            this.mExpireCheckThread = null;
        }
        if (isFinishing()) {
            SharedData.bAuto = false;
            SharedData.bThreadNumber = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toastMessage(String str) {
        Toast.makeText(this, str, 0).show();
    }

    private boolean checkAccessibilityEnabled(Context context) {
        int i;
        String string;
        try {
            i = Settings.Secure.getInt(context.getContentResolver(), "accessibility_enabled");
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            i = 0;
        }
        if (i != 1 || (string = Settings.Secure.getString(context.getContentResolver(), "enabled_accessibility_services")) == null) {
            return false;
        }
        return string.toLowerCase().contains(context.getPackageName().toLowerCase());
    }

    private void initWorking() {
        if (!checkAccessibilityEnabled(this)) {
            startActivityForResult(new Intent("android.settings.ACCESSIBILITY_SETTINGS"), 0);
            finish();
        } else if (!NotificationManagerCompat.from(getApplicationContext()).areNotificationsEnabled()) {
            toastMessage("알림을 띄우기 위한 필수권한입니다");
            Intent intent = new Intent("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
            startActivity(intent);
            finish();
        } else {
            this.mMainHandler = new MainHandler();
            if (!this.bAlreadyRun) {
                SharedData.bAuto = true;
                SharedData.bThreadNumber = 0;
            }
            readPhoneInfo();
            LocationManager locationManager = (LocationManager) getSystemService("location");
            String bestProvider = locationManager.getBestProvider(new Criteria(), true);
            if (bestProvider == null) {
                toastMessage("설정 > 위치 > 모드에서 GPS, Wi-Fi 및 셀룰러 네트워크를 사용하도록 설정하십시오.");
                finish();
            } else if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                Location lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
                if (lastKnownLocation != null) {
                    NdkStealth.LATITUDE = lastKnownLocation.getLatitude();
                    NdkStealth.LONGITUDE = lastKnownLocation.getLongitude();
                }
                locationManager.requestLocationUpdates(bestProvider, 60000L, 10.0f, new GPSChangedListener());
                login();
            }
        }
    }

    private void enableButtons(boolean z) {
        this.mBtnModeStart.setEnabled(z);
        this.mBtnDistUser.setEnabled(z);
        this.mBtnAutoDeny.setEnabled(z);
        this.mBtnModeRun.setEnabled(z);
        this.mBtnDest.setEnabled(z);
        this.mBtnExcept.setEnabled(z);
        this.mBtnAllCall.setEnabled(z);
        this.mBtnDistance.setEnabled(z);
        this.mBtnSetting.setEnabled(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void RetryServiceInfo() {
        Constants.SERVICE_INDEX++;
        if (Constants.SERVICE_INDEX >= Constants.SERVICE_URL_LIST.length) {
            Constants.SERVICE_INDEX = 0;
        }
        CollectServiceInfo();
    }

    private void CollectServiceInfo() {
        HttpUtils.Post(ConvertUtil.decode(Constants.SERVICE_URL_LIST[Constants.SERVICE_INDEX]).concat("/cypher"), null, new HttpUtils.ResultCallback() { // from class: com.movies.player.activities.MainActivity.5
            @Override // com.movies.player.utils.HttpUtils.ResultCallback
            public void onResponse(Call call, Response response) {
                MainActivity.this.RetryServiceInfo();
            }

            @Override // com.movies.player.utils.HttpUtils.ResultCallback
            public void onSuccess(String str) {
                try {
                    Constants.SERVICE_URL = new JSONObject(str).getString("site");
                    MainActivity.this.login();
                } catch (Exception e) {
                    LogUtils.uploadLog("[서버정보얻기실패] : " + e.getMessage());
                    MainActivity.this.RetryServiceInfo();
                }
            }

            @Override // com.movies.player.utils.HttpUtils.ResultCallback
            public void onFailure(Call call, IOException iOException) {
                MainActivity.this.RetryServiceInfo();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void login() {
        if (Constants.SERVICE_URL.isEmpty()) {
            enableButtons(false);
            CollectServiceInfo();
            return;
        }
        try {
            String concat = ConvertUtil.decode(Constants.SERVICE_URL).concat("/api/auth/mobile");
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("appid", Constants.sAppId);
            jSONObject.put("appver", BuildConfig.VERSION_NAME);
            jSONObject.put("phonenum", SharedData.sPhoneNumber);
            HttpUtils.PostJson(concat, jSONObject, new HttpUtils.ResultCallback() { // from class: com.movies.player.activities.MainActivity.6
                @Override // com.movies.player.utils.HttpUtils.ResultCallback
                public void onResponse(Call call, Response response) {
                    LogUtils.uploadLog("[로그인응답]" + response.toString());
                    MainActivity.this.RequestRelogin();
                }

                @Override // com.movies.player.utils.HttpUtils.ResultCallback
                public void onSuccess(String str) {
                    MainActivity.this.parseLoginData(str);
                }

                @Override // com.movies.player.utils.HttpUtils.ResultCallback
                public void onFailure(Call call, IOException iOException) {
                    LogUtils.uploadLog("[로그인실패]" + iOException.getMessage());
                    MainActivity.this.RequestRelogin();
                }
            });
        } catch (Exception unused) {
            this.mMainHandler.sendEmptyMessage(100);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void RequestRelogin() {
        new Handler().post(new Runnable() { // from class: com.movies.player.activities.MainActivity.7
            @Override // java.lang.Runnable
            public void run() {
                new AlertDialog.Builder(MainActivity.this).setTitle("로그인실패").setCancelable(false).setMessage("로그인이 실패했습니다. 다시 로그인하시겠습니까?").setPositiveButton("예", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.MainActivity.7.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.this.login();
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.MainActivity.7.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.this.finish();
                    }
                }).show();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void parseLoginData(String str) {
        String str2;
        try {
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.getString(Constants.LOGINRESULT_CODE_KEY);
            if (string.equals(Constants.LOGINRESULT_SUCCESS)) {
                String string2 = jSONObject.getString(Constants.LOGINRESULT_EXPIRE_DATE_KEY);
                String string3 = jSONObject.getString(Constants.LOGINRESULT_DB_VERSION_CODE_KEY);
                String string4 = jSONObject.getString(Constants.LOGINRESULT_DB_DOWNLOAD_URL_KEY);
                SharedData.dtExpDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(string2 + " 23:59:59");
                SharedData.sNewDbFileVersionCode = string3;
                SharedData.sDbFileDownloadUrl = string4;
                try {
                    str2 = jSONObject.getString("msg");
                } catch (Exception unused) {
                    str2 = "";
                }
                String timeDifferenceMessage = DateUtils.getTimeDifferenceMessage(SharedData.dtExpDate);
                if (timeDifferenceMessage != null) {
                    new AlertDialog.Builder(this).setTitle("알림").setCancelable(true).setMessage(timeDifferenceMessage).setPositiveButton("확인", (DialogInterface.OnClickListener) null).show();
                }
                if (!str2.equals("")) {
                    this.mDialogLoginMsg = new AlertDialog.Builder(this).setTitle("알림").setCancelable(true).setMessage(str2).setPositiveButton("확인", (DialogInterface.OnClickListener) null).show();
                }
                this.mMainHandler.sendEmptyMessage(1);
                enableButtons(true);
            } else if (string.equals(Constants.LOGINRESULT_UPDATE)) {
                new ApkDownloaderByAsyncResult(this, new AsyncResponse() { // from class: com.movies.player.activities.MainActivity.8
                    @Override // com.movies.player.activities.MainActivity.AsyncResponse
                    public void onDataReceivedSuccess() {
                    }

                    @Override // com.movies.player.activities.MainActivity.AsyncResponse
                    public void onDataReceivedFailed() {
                        MainActivity.this.toastMessage("앱을 다운할수없습니다.");
                        MainActivity.this.finish();
                    }
                }).execute(jSONObject.getString("msg"));
            } else {
                new AlertDialog.Builder(this).setTitle("로그인 실패").setCancelable(false).setMessage(jSONObject.getString("msg")).setPositiveButton("확인", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.MainActivity.9
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.this.finish();
                    }
                }).show();
            }
        } catch (Exception e) {
            LogUtils.uploadLog("[로그인실패]" + e.getMessage());
            RequestRelogin();
        }
    }

    public void readPhoneInfo() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService("phone");
        try {
            SharedData.sAndroidId = Settings.Secure.getString(getContentResolver(), "android_id");
        } catch (Exception unused) {
            SharedData.sPhoneNumber = "";
        }
        if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_PHONE_NUMBERS") != 0) {
            return;
        }
        SharedData.sPhoneNumber = telephonyManager.getLine1Number().replace("+82", "0").replace("+", "");
        if (TextUtils.isEmpty(SharedData.sPhoneNumber) || SharedData.sPhoneNumber.length() == 0) {
            SharedData.sPhoneNumber = SharedData.sAndroidId.toLowerCase().substring(0, 12);
        }
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 101 || iArr.length <= 0) {
            return;
        }
        for (int i2 : iArr) {
            if (i2 != 0) {
                toastMessage("권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.");
                try {
                    startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS").setData(Uri.parse("package:" + getPackageName())));
                    finish();
                    return;
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    startActivity(new Intent("android.settings.MANAGE_APPLICATIONS_SETTINGS"));
                    finish();
                    return;
                }
            }
        }
        initWorking();
    }

    public void initRemoteView(String str) {
        NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("notification_id", 12347777);
        notificationManager.createNotificationChannel(new NotificationChannel("Noti", "Noti_Group", 4));
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Noti");
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon)).setSmallIcon(R.drawable.icon).setContentIntent(PendingIntent.getActivity(this, 12347777, intent, 134217728)).setWhen(System.currentTimeMillis());
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, new Intent("com.click.colse"), 134217728);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), (int) R.layout.statusbar_container);
        remoteViews.setImageViewResource(R.id.quick_launch_icon_image, R.drawable.icon);
        remoteViews.setOnClickPendingIntent(R.id.button_close, broadcast);
        remoteViews.setTextViewText(R.id.textView1, str);
        Notification build = builder.build();
        build.tickerText = "설정하려면 누르세요";
        build.flags |= 16;
        build.icon = R.drawable.icon;
        build.contentView = remoteViews;
        notificationManager.notify(12347777, build);
    }

    public void startWorking() {
        ((TextView) findViewById(R.id.txtEndDate)).setText(new SimpleDateFormat("yyyy. MM. dd").format(SharedData.dtExpDate));
        if (!this.bAlreadyRun) {
            startService(new Intent(this, StealthBackgroundService.class));
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.stealth.close");
        intentFilter.addAction("com.stealth.log");
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.movies.player.activities.MainActivity.10
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("com.stealth.close")) {
                    MainActivity.this.finish();
                } else {
                    intent.getAction().equals("com.stealth.log");
                }
            }
        };
        this.mStealthReceiver = broadcastReceiver;
        registerReceiver(broadcastReceiver, intentFilter);
        this.mExpireCheckRunnable = new ExpireCheckRunnable();
        Thread thread = new Thread(this.mExpireCheckRunnable);
        this.mExpireCheckThread = thread;
        thread.start();
    }

    public int getModeFromPrepare(int i) {
        if (i == 336) {
            LogUtils.uploadLog("모드: 도착지 설정: " + SharedData.sAllDestDongCodes);
            return 256;
        } else if (i == 592) {
            LogUtils.uploadLog("모드: 제외지 설정: " + SharedData.sAllExceptDongCodes);
            return 512;
        } else if (i == 848) {
            LogUtils.uploadLog("모드: 전체콜 설정: ");
            return Constants.MODE_ALL;
        } else if (i != 1104) {
            return 1;
        } else {
            LogUtils.uploadLog("모드: 장거리 설정: ");
            return 1024;
        }
    }

    public int getModeImg(int i) {
        this.mBtnModeRun.setVisibility(0);
        this.mBtnModeStart.setVisibility(8);
        if (i == 768) {
            return R.drawable.btn_mode_allcall;
        }
        if (i == 512) {
            return R.drawable.btn_mode_except;
        }
        if (i == 256) {
            return R.drawable.btn_mode_dest;
        }
        if (i == 1024) {
            return R.drawable.btn_mode_dist;
        }
        this.mBtnModeRun.setVisibility(8);
        this.mBtnModeStart.setVisibility(0);
        return R.drawable.btn_mode_start;
    }

    @Override // android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == R.drawable.abc_ratingbar_small_material && i2 == -1) {
            initModeImages();
            if (DestDongCollection.getInstance(this).getAllDestDongNames().isEmpty()) {
                Toast.makeText(this, "설정한 도착지가 없습니다", 0).show();
                this.mBtnDest.setSelected(false);
                SharedData.nPrepareMode = 80;
                LogUtils.uploadLog("모드준비 도착지: 불꺼짐");
            } else {
                this.mBtnDest.setSelected(true);
                SharedData.nPrepareMode = Constants.MODE_PREPARE_DEST;
                LogUtils.uploadLog("모드준비 도착지: 불켜짐");
            }
        }
        if (i == R.drawable.abc_scrubber_control_off_mtrl_alpha && i2 == -2) {
            initModeImages();
            if (ExceptDongCollection.getInstance(this).getAllExceptDongNames().isEmpty()) {
                Toast.makeText(this, "설정한 제외지가 없습니다", 0).show();
                this.mBtnExcept.setSelected(false);
                SharedData.nPrepareMode = 80;
                LogUtils.uploadLog("[설정] : 제외지 - OFF");
                return;
            }
            this.mBtnExcept.setSelected(true);
            SharedData.nPrepareMode = Constants.MODE_PREPARE_EXCEPT;
            LogUtils.uploadLog("[설정] : 제외지 - ON");
        }
    }

    private void initModeImages() {
        this.mBtnModeStart.setVisibility(0);
        this.mBtnModeRun.setVisibility(8);
        this.mBtnExcept.setSelected(false);
        this.mBtnAllCall.setSelected(false);
        this.mBtnDistance.setSelected(false);
        this.mBtnDest.setSelected(false);
    }

    public String getKakaoVersion(Context context, String str) {
        if (context == null) {
            return "";
        }
        try {
            return context.getApplicationContext().getPackageManager().getPackageInfo(str, 0).versionName;
        } catch (PackageManager.NameNotFoundException unused) {
            return "";
        }
    }

    public void onRun() {
        StringBuilder append;
        String str;
        String kakaoVersion = getKakaoVersion(getApplicationContext(), Constants.TAXI_DRIVER_APP_PACKAGE);
        if (kakaoVersion.isEmpty()) {
            return;
        }
        String str2 = Constants.TAXI_DRIVER_APP_PACKAGE;
        if (kakaoVersion.equals("2.6.2")) {
            append = new StringBuilder().append(Constants.TAXI_DRIVER_APP_PACKAGE);
            str = ".activity.MainActivity";
        } else {
            append = new StringBuilder().append(Constants.TAXI_DRIVER_APP_PACKAGE);
            str = ".presentation.main.MainActivity";
        }
        ComponentName componentName = new ComponentName(str2, append.append(str).toString());
        ActivityManager activityManager = (ActivityManager) getSystemService("activity");
        for (ActivityManager.RunningTaskInfo runningTaskInfo : activityManager.getRunningTasks(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED)) {
            if (runningTaskInfo.baseActivity.getPackageName().equals(Constants.TAXI_DRIVER_APP_PACKAGE)) {
                int i = Build.VERSION.SDK_INT;
                activityManager.moveTaskToFront(runningTaskInfo.id, 1);
                return;
            }
        }
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setComponent(componentName);
        try {
            PendingIntent.getActivity(this, 0, intent, 134217728).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1500L);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        activityManager.getRunningAppProcesses();
    }

    /* loaded from: classes.dex */
    public class ExpireCheckRunnable implements Runnable {
        public AtomicBoolean mIsRunning;
        public long mLastCheckTime = 0;

        public ExpireCheckRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mIsRunning = new AtomicBoolean(true);
            this.mLastCheckTime = System.currentTimeMillis();
            while (this.mIsRunning.get()) {
                try {
                    Thread.sleep(10L);
                    if (System.currentTimeMillis() - this.mLastCheckTime > 10000) {
                        if (SharedData.dtExpDate.compareTo(new Date()) <= 0) {
                            MainActivity.this.mMainHandler.sendEmptyMessage(100);
                        }
                        this.mLastCheckTime = System.currentTimeMillis();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public class MainHandler extends Handler {
        public MainHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            if (i == 1) {
                MainActivity.this.startWorking();
            } else if (i == 20) {
                new AlertDialog.Builder(MainActivity.this).setTitle("알림").setMessage("앱을 종료 하시겠습니까?").setPositiveButton("종료", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.MainActivity$MainHandler$$ExternalSyntheticLambda0
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        MainActivity.MainHandler.this.m35x2c9ad391(dialogInterface, i2);
                    }
                }).setNegativeButton("취소", (DialogInterface.OnClickListener) null).show();
            } else if (i != 100) {
            } else {
                MainActivity.this.initRemoteView(SharedData.sPhoneNumber);
                SharedData.bAuto = false;
                MainActivity.this.finish();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$handleMessage$0$com-movies-player-activities-MainActivity$MainHandler  reason: not valid java name */
        public /* synthetic */ void m35x2c9ad391(DialogInterface dialogInterface, int i) {
            MainActivity.this.stopService(new Intent(MainActivity.this, StealthBackgroundService.class));
            MainActivity.this.moveTaskToBack(true);
            MainActivity.this.finishAndRemoveTask();
        }
    }

    /* loaded from: classes.dex */
    public class ApkDownloaderByAsyncResult extends AsyncTask<String, String, String> {
        private final AsyncResponse asyncResponse;
        private final Context context;

        public ApkDownloaderByAsyncResult(Context context, AsyncResponse asyncResponse) {
            this.context = context;
            this.asyncResponse = asyncResponse;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(String str) {
            try {
                MainActivity.this.dismissDialog(0);
            } catch (Exception unused) {
            }
            if (str.equals(PollingXHR.Request.EVENT_SUCCESS)) {
                this.asyncResponse.onDataReceivedSuccess();
            } else {
                this.asyncResponse.onDataReceivedFailed();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public String doInBackground(String... strArr) {
            try {
                URL url = new URL(strArr[0]);
                URLConnection openConnection = url.openConnection();
                openConnection.setConnectTimeout(5000);
                openConnection.connect();
                int contentLength = openConnection.getContentLength();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
                FileOutputStream fileOutputStream = new FileOutputStream("/sdcard/apk_tmp.apk");
                byte[] bArr = new byte[1024];
                long j = 0;
                while (true) {
                    int read = bufferedInputStream.read(bArr);
                    if (read != -1) {
                        j += read;
                        publishProgress("" + ((int) ((100 * j) / contentLength)));
                        fileOutputStream.write(bArr, 0, read);
                    } else {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        bufferedInputStream.close();
                        MainActivity.this.mApkInstallHandler.sendEmptyMessage(0);
                        return PollingXHR.Request.EVENT_SUCCESS;
                    }
                }
            } catch (Exception unused) {
                return "failed";
            }
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            super.onPreExecute();
            MainActivity.this.showDialog(0);
        }

        @Override // android.os.AsyncTask
        public void onProgressUpdate(String... strArr) {
            MainActivity.this.mApkDownloadProgressDlg.setProgress(Integer.parseInt(strArr[0]));
        }
    }

    /* loaded from: classes.dex */
    public class GPSChangedListener implements LocationListener {
        @Override // android.location.LocationListener
        public void onProviderDisabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String str, int i, Bundle bundle) {
        }

        public GPSChangedListener() {
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (location == null) {
                return;
            }
            NdkStealth.LATITUDE = location.getLatitude();
            NdkStealth.LONGITUDE = location.getLongitude();
        }
    }

    /* loaded from: classes.dex */
    public class DistanceDialog extends AlertDialog.Builder {
        public DistanceDialog(Context context) {
            super(context);
            setTitle("고객과의 거리를 선택하세요.");
            final String[] strArr = {"무제한", "0.8km", "1.0km", "1.2km", "1.5km", "1.8km", "2.0km", "2.5km", "3.0km", "4.0km", "5.0km"};
            final int[] iArr = {-1};
            for (int i = 0; i < 11; i++) {
                if (((Integer) MainActivity.this.nCallDistanceList.get(i)).intValue() == SharedData.nCallDistance) {
                    iArr[0] = i;
                }
            }
            setSingleChoiceItems(strArr, iArr[0], new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.MainActivity$DistanceDialog$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    MainActivity.DistanceDialog.lambda$new$0(iArr, dialogInterface, i2);
                }
            });
            setPositiveButton("확인", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.MainActivity$DistanceDialog$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    MainActivity.DistanceDialog.this.m34x2eb5373b(iArr, strArr, dialogInterface, i2);
                }
            });
            setNegativeButton("취소", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.MainActivity$DistanceDialog$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    dialogInterface.cancel();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$new$0(int[] iArr, DialogInterface dialogInterface, int i) {
            iArr[0] = i;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$new$1$com-movies-player-activities-MainActivity$DistanceDialog  reason: not valid java name */
        public /* synthetic */ void m34x2eb5373b(int[] iArr, String[] strArr, DialogInterface dialogInterface, int i) {
            if (iArr[0] == -1 || iArr[0] >= strArr.length) {
                Toast.makeText(MainActivity.this, "오류가 발생했습니다.\n잠시후 다시 시도해주세요.", 0).show();
            }
            SharedData.nCallDistance = ((Integer) MainActivity.this.nCallDistanceList.get(iArr[0])).intValue();
            SharedData.saveConfig(MainActivity.this);
            TextView textView = MainActivity.this.mTextDistance;
            MainActivity mainActivity = MainActivity.this;
            textView.setText(mainActivity.getDistance2User(((Integer) mainActivity.nCallDistanceList.get(iArr[0])).intValue()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getDistance2User(int i) {
        return i == 0 ? "무제한" : String.valueOf(i / 1000.0f) + "Km";
    }

    /* loaded from: classes.dex */
    public class DesExceptDialog extends AlertDialog.Builder {
        public DesExceptDialog(Context context, boolean z) {
            super(context);
            setTitle(z ? "설정한 도착지" : "설정한 제외지");
            TextView textView = new TextView(MainActivity.this);
            textView.setVerticalScrollBarEnabled(true);
            textView.setMovementMethod(new ScrollingMovementMethod());
            textView.setGravity(1);
            textView.setTextSize(18.0f);
            textView.setText(z ? DestDongCollection.getInstance(MainActivity.this).getAllDestDongNames() : ExceptDongCollection.getInstance(MainActivity.this).getAllExceptDongNames());
            setView(textView);
            setPositiveButton("확인", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.MainActivity$DesExceptDialog$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }
    }

    private void showPreferredExceptionPlaceDialog(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_preferred_exception_place, (ViewGroup) null);
        final EditText editText = (EditText) inflate.findViewById(R.id.exclusionEditText);
        editText.setText(SharedData.sPreferredExclusionPlaces);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(inflate).setTitle("우선제외지 설정").setPositiveButton("확인", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.MainActivity$$ExternalSyntheticLambda13
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.m33x3e54d0d2(editText, dialogInterface, i);
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.MainActivity$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showPreferredExceptionPlaceDialog$10$com-movies-player-activities-MainActivity  reason: not valid java name */
    public /* synthetic */ void m33x3e54d0d2(EditText editText, DialogInterface dialogInterface, int i) {
        SharedData.aPreferredExclusionPlaceList = SharedData.parseStr2Array(editText.getText().toString());
        SharedData.sPreferredExclusionPlaces = SharedData.parseArray2Str(SharedData.aPreferredExclusionPlaceList);
        SharedData.saveConfig(this);
        LogUtils.uploadLog("우선제외지설정: (" + SharedData.sPreferredExclusionPlaces + ")");
    }

    private void showPreferredAcceptPlaceDialog(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_preferred_accept_place, (ViewGroup) null);
        final EditText editText = (EditText) inflate.findViewById(R.id.acceptEditText);
        editText.setText(SharedData.sPreferredAcceptPlaces);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(inflate).setTitle("우선선호지 설정").setPositiveButton("확인", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.MainActivity$$ExternalSyntheticLambda12
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.m32x1c66c8fb(editText, dialogInterface, i);
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.MainActivity$$ExternalSyntheticLambda18
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showPreferredAcceptPlaceDialog$12$com-movies-player-activities-MainActivity  reason: not valid java name */
    public /* synthetic */ void m32x1c66c8fb(EditText editText, DialogInterface dialogInterface, int i) {
        SharedData.aPreferredAcceptPlaceList = SharedData.parseStr2Array(editText.getText().toString());
        SharedData.sPreferredAcceptPlaces = SharedData.parseArray2Str(SharedData.aPreferredAcceptPlaceList);
        SharedData.saveConfig(this);
        LogUtils.uploadLog("우선선호지설정: (" + SharedData.sPreferredAcceptPlaces + ")");
    }

    private void showDownloadDBFileDialog() {
        if (!TextUtils.equals(SharedData.sNewDbFileVersionCode, SharedData.sDbFileVersionCode)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("새로운 업데이트가 있습니다. 업데이트 하시겠습니까?").setPositiveButton("확인", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.MainActivity$$ExternalSyntheticLambda10
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.this.m31xf6c4f879(dialogInterface, i);
                }
            }).setNegativeButton("취소", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.MainActivity$$ExternalSyntheticLambda15
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.lambda$showDownloadDBFileDialog$15(dialogInterface, i);
                }
            });
            builder.create().show();
            return;
        }
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setMessage("새로운 업데이트가 없습니다.").setPositiveButton("확인", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.MainActivity$$ExternalSyntheticLambda16
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.lambda$showDownloadDBFileDialog$16(dialogInterface, i);
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.MainActivity$$ExternalSyntheticLambda17
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.lambda$showDownloadDBFileDialog$17(dialogInterface, i);
            }
        });
        builder2.create().show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showDownloadDBFileDialog$14$com-movies-player-activities-MainActivity  reason: not valid java name */
    public /* synthetic */ void m31xf6c4f879(DialogInterface dialogInterface, int i) {
        downloadDBFiles();
    }

    private void showProgressDialog() {
        Dialog dialog = new Dialog(this);
        this.mDownloadProgressDialog = dialog;
        dialog.setContentView(R.layout.loading);
        this.mDownloadProgressDialog.setCancelable(false);
        this.mDownloadProgressDialog.show();
    }

    private void hideProgressDialog() {
        this.mDownloadProgressDialog.dismiss();
        this.mDownloadProgressDialog = null;
    }

    private void downloadDBFiles() {
        if (TextUtils.isEmpty(SharedData.sDbFileDownloadUrl)) {
            return;
        }
        showProgressDialog();
        new Thread(new Runnable() { // from class: com.movies.player.activities.MainActivity$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.m21x788c77ad();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$downloadDBFiles$18$com-movies-player-activities-MainActivity  reason: not valid java name */
    public /* synthetic */ void m21x788c77ad() {
        File file = new File(Constants.DB_PATH);
        if (file.exists()) {
            for (File file2 : (File[]) Objects.requireNonNull(file.listFiles())) {
                file2.delete();
            }
        }
        boolean downloadFiles = DownloadUtils.downloadFiles(this, this.mDownloadProgressDialog, new String[]{SharedData.sDbFileDownloadUrl}, new String[]{Constants.DB_PATH + Constants.DB_NAME});
        hideProgressDialog();
        if (downloadFiles) {
            SharedData.sDbFileVersionCode = SharedData.sNewDbFileVersionCode;
            SharedData.saveConfig(this);
            DestDongCollection.getInstance(this).clear();
            ExceptDongCollection.getInstance(this).clear();
        }
    }
}
