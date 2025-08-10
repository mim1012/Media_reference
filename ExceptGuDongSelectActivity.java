package com.movies.player.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.movies.player.activities.ExceptGuDongSelectActivity;
import com.movies.player.adapter.ExceptGuDongListAdapter;
import com.movies.player.model.ExceptDongCollection;
import com.movies.player.utils.ConvertUtil;
import com.movies.player.utils.FileUtils;
import com.movies.player.utils.LogUtils;
import com.movies.player.utils.SharedData;
import com.slcbxla.wkflshgies.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
/* loaded from: classes.dex */
public class ExceptGuDongSelectActivity extends Activity implements View.OnClickListener {
    public ExceptDongCollection mDongCollection;
    public Map<Integer, ArrayList<Integer>> mExceptMap;
    public ExceptGuDongListAdapter mGuDongListAdapter;
    public ListView mGuDongListView;
    public AlertDialog mSaveLoadDlg = null;
    public AlertDialog mDeleteDlg = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onSido$0(DialogInterface dialogInterface, int i) {
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_gudongselect);
        findViewById(R.id.btnConfirm).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);
        findViewById(R.id.btnSetAll).setOnClickListener(this);
        findViewById(R.id.btnUnSetAll).setOnClickListener(this);
        findViewById(R.id.btnSido).setOnClickListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);
        findViewById(R.id.btnLoad).setOnClickListener(this);
        new Handler().postDelayed(new AnonymousClass1(), 500L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.movies.player.activities.ExceptGuDongSelectActivity$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ExceptGuDongSelectActivity exceptGuDongSelectActivity = ExceptGuDongSelectActivity.this;
            exceptGuDongSelectActivity.mDongCollection = ExceptDongCollection.getInstance(exceptGuDongSelectActivity);
            ExceptGuDongSelectActivity.this.mExceptMap = new TreeMap(ExceptGuDongSelectActivity.this.mDongCollection.mExceptMap);
            ExceptGuDongSelectActivity.this.mGuDongListAdapter = new ExceptGuDongListAdapter(ExceptGuDongSelectActivity.this);
            ExceptGuDongSelectActivity exceptGuDongSelectActivity2 = ExceptGuDongSelectActivity.this;
            exceptGuDongSelectActivity2.mGuDongListView = (ListView) exceptGuDongSelectActivity2.findViewById(R.id.list);
            ExceptGuDongSelectActivity.this.mGuDongListView.setAdapter((ListAdapter) ExceptGuDongSelectActivity.this.mGuDongListAdapter);
            ExceptGuDongSelectActivity.this.mGuDongListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.movies.player.activities.ExceptGuDongSelectActivity$1$$ExternalSyntheticLambda0
                @Override // android.widget.AdapterView.OnItemClickListener
                public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
                    ExceptGuDongSelectActivity.AnonymousClass1.this.m15x7f570ccb(adapterView, view, i, j);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$run$0$com-movies-player-activities-ExceptGuDongSelectActivity$1  reason: not valid java name */
        public /* synthetic */ void m15x7f570ccb(AdapterView adapterView, View view, int i, long j) {
            ExceptGuDongSelectActivity.this.onGudongItemSelected(i);
        }
    }

    @Override // android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        AlertDialog alertDialog = this.mSaveLoadDlg;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        AlertDialog alertDialog2 = this.mDeleteDlg;
        if (alertDialog2 != null) {
            alertDialog2.dismiss();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCancel /* 2131230823 */:
                setResult(0, getIntent());
                break;
            case R.id.btnConfirm /* 2131230824 */:
                this.mDongCollection.setExceptMap(this.mExceptMap);
                this.mDongCollection.saveExceptMap();
                SharedData.sAllExceptDongCodes = ExceptDongCollection.getInstance(this).getAllExceptDongCodes();
                setResult(-2, getIntent());
                break;
            case R.id.btnLoad /* 2131230829 */:
                onLoad();
                return;
            case R.id.btnSave /* 2131230832 */:
                onSave();
                return;
            case R.id.btnSetAll /* 2131230833 */:
                Iterator it = this.mGuDongListAdapter.mExceptSigunguEntryList.iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    if (!this.mGuDongListAdapter.mSettingExceptMap.containsKey(entry.getKey())) {
                        this.mGuDongListAdapter.mSettingExceptMap.put(Integer.valueOf(((Integer) entry.getKey()).intValue()), null);
                    }
                }
                this.mGuDongListAdapter.notifyDataSetChanged();
                return;
            case R.id.btnSido /* 2131230835 */:
                onSido();
                return;
            case R.id.btnUnSetAll /* 2131230836 */:
                this.mGuDongListAdapter.mSettingExceptMap.clear();
                this.mGuDongListAdapter.notifyDataSetChanged();
                return;
            default:
                return;
        }
        finish();
    }

    public void onSave() {
        if (this.mGuDongListAdapter.mSettingExceptMap.size() < 1) {
            Toast.makeText(this, "제외지를 선택한후 저장하세요.", 0).show();
        } else {
            this.mSaveLoadDlg = new SaveDialog(this).show();
        }
    }

    public void onLoad() {
        if (FileUtils.getExceptionFileList().size() == 0) {
            Toast.makeText(this, "로드할 파일이 없습니다.\n파일을 저장하고 시도하세요.", 0).show();
        } else {
            this.mSaveLoadDlg = new LoadDialog(this).show();
        }
    }

    public void onSido() {
        Map<Integer, String> map = this.mDongCollection.mAllSidoMap;
        boolean[] zArr = new boolean[map.size()];
        String[] strArr = new String[map.size()];
        int i = 0;
        for (Integer num : map.keySet()) {
            int intValue = num.intValue();
            strArr[i] = map.get(Integer.valueOf(intValue));
            if (this.mDongCollection.mExceptSidoList.contains(Integer.valueOf(intValue))) {
                zArr[i] = true;
            }
            i++;
        }
        new AlertDialog.Builder(this).setTitle("시도 선택").setMultiChoiceItems(strArr, zArr, new SidoChoiceListener(zArr)).setPositiveButton("확인", new SidoSelectionOKListener(this, this.mDongCollection.mExceptSidoList, zArr, strArr, map)).setNegativeButton("취소", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.ExceptGuDongSelectActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                ExceptGuDongSelectActivity.lambda$onSido$0(dialogInterface, i2);
            }
        }).create().show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class SidoChoiceListener implements DialogInterface.OnMultiChoiceClickListener {
        boolean[] mIsChecks;

        SidoChoiceListener(boolean[] zArr) {
            this.mIsChecks = zArr;
        }

        @Override // android.content.DialogInterface.OnMultiChoiceClickListener
        public void onClick(DialogInterface dialogInterface, int i, boolean z) {
            this.mIsChecks[i] = z;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class SidoSelectionOKListener implements DialogInterface.OnClickListener {
        public ArrayList<Integer> mExceptArrayList;
        public ExceptGuDongSelectActivity mGuDongSelectActivity;
        public boolean[] mIsChecks;
        public String[] mNames;
        public Map<Integer, String> mSrcMap;

        public SidoSelectionOKListener(ExceptGuDongSelectActivity exceptGuDongSelectActivity, ArrayList<Integer> arrayList, boolean[] zArr, String[] strArr, Map<Integer, String> map) {
            this.mGuDongSelectActivity = exceptGuDongSelectActivity;
            this.mExceptArrayList = arrayList;
            this.mIsChecks = zArr;
            this.mNames = strArr;
            this.mSrcMap = map;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            this.mExceptArrayList.clear();
            int i2 = 0;
            while (true) {
                boolean[] zArr = this.mIsChecks;
                if (i2 >= zArr.length) {
                    break;
                }
                if (zArr[i2]) {
                    String str = this.mNames[i2].toString();
                    Iterator<Integer> it = this.mSrcMap.keySet().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        int intValue = it.next().intValue();
                        if (this.mSrcMap.get(Integer.valueOf(intValue)).equals(str)) {
                            this.mExceptArrayList.add(Integer.valueOf(intValue));
                            break;
                        }
                    }
                }
                i2++;
            }
            ExceptDongCollection exceptDongCollection = this.mGuDongSelectActivity.mDongCollection;
            try {
                SharedPreferences.Editor edit = exceptDongCollection.mContext.getSharedPreferences("dong_setting", 0).edit();
                edit.putString("except_sido", ConvertUtil.integerArrayToString(exceptDongCollection.mExceptSidoList));
                edit.apply();
            } catch (Exception unused) {
            }
            this.mGuDongSelectActivity.mDongCollection.refreshExceptSigunguMap();
            ExceptGuDongListAdapter exceptGuDongListAdapter = this.mGuDongSelectActivity.mGuDongListAdapter;
            exceptGuDongListAdapter.mExceptSigunguEntryList.clear();
            exceptGuDongListAdapter.mExceptSigunguEntryList.addAll(exceptGuDongListAdapter.mDongCollection.mExceptSigunguMap.entrySet());
            this.mGuDongSelectActivity.mGuDongListAdapter.notifyDataSetChanged();
        }
    }

    public void onGudongItemSelected(int i) {
        Map.Entry entry = (Map.Entry) this.mGuDongListAdapter.mExceptSigunguEntryList.get(i);
        Map<Integer, String> allDongNamesFromSigunguCode = this.mDongCollection.getAllDongNamesFromSigunguCode(((Integer) entry.getKey()).intValue());
        boolean[] zArr = new boolean[allDongNamesFromSigunguCode.size()];
        String[] strArr = new String[allDongNamesFromSigunguCode.size()];
        ArrayList<Integer> arrayList = this.mExceptMap.get(Integer.valueOf(((Integer) entry.getKey()).intValue()));
        int i2 = 0;
        for (Integer num : allDongNamesFromSigunguCode.keySet()) {
            int intValue = num.intValue();
            strArr[i2] = allDongNamesFromSigunguCode.get(Integer.valueOf(intValue));
            if (arrayList != null && arrayList.contains(Integer.valueOf(intValue))) {
                zArr[i2] = true;
            }
            i2++;
        }
        new AlertDialog.Builder(this).setTitle((String) entry.getValue()).setMultiChoiceItems(strArr, zArr, new DongChoiceListener(this, zArr)).setPositiveButton("확인", new ConfirmDongListener(this, allDongNamesFromSigunguCode, zArr, strArr, entry)).setNegativeButton("전체", new AllDongListener(this, entry)).create().show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class DongChoiceListener implements DialogInterface.OnMultiChoiceClickListener {
        public boolean[] mIsChecks;

        public DongChoiceListener(ExceptGuDongSelectActivity exceptGuDongSelectActivity, boolean[] zArr) {
            this.mIsChecks = zArr;
        }

        @Override // android.content.DialogInterface.OnMultiChoiceClickListener
        public void onClick(DialogInterface dialogInterface, int i, boolean z) {
            this.mIsChecks[i] = z;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ConfirmDongListener implements DialogInterface.OnClickListener {
        public final Map<Integer, String> mAllDongInSigunguItemMap;
        public final Map.Entry mEntry;
        public final ExceptGuDongSelectActivity mGuDongSelectActivity;
        public final boolean[] mIsCheck;
        public final String[] mOnlyDongNames;

        public ConfirmDongListener(ExceptGuDongSelectActivity exceptGuDongSelectActivity, Map<Integer, String> map, boolean[] zArr, String[] strArr, Map.Entry entry) {
            this.mGuDongSelectActivity = exceptGuDongSelectActivity;
            this.mAllDongInSigunguItemMap = map;
            this.mIsCheck = zArr;
            this.mOnlyDongNames = strArr;
            this.mEntry = entry;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            ArrayList<Integer> arrayList = new ArrayList<>();
            for (int i2 = 0; i2 < this.mAllDongInSigunguItemMap.size(); i2++) {
                if (this.mIsCheck[i2]) {
                    Iterator<Integer> it = this.mAllDongInSigunguItemMap.keySet().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        int intValue = it.next().intValue();
                        if (this.mAllDongInSigunguItemMap.get(Integer.valueOf(intValue)).equals(this.mOnlyDongNames[i2])) {
                            arrayList.add(Integer.valueOf(intValue));
                            break;
                        }
                    }
                }
            }
            if (arrayList.isEmpty()) {
                this.mGuDongSelectActivity.mExceptMap.put(Integer.valueOf(((Integer) this.mEntry.getKey()).intValue()), null);
            } else {
                this.mGuDongSelectActivity.mExceptMap.put(Integer.valueOf(((Integer) this.mEntry.getKey()).intValue()), arrayList);
            }
            this.mGuDongSelectActivity.mGuDongListAdapter.notifyDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class AllDongListener implements DialogInterface.OnClickListener {
        public final Map.Entry mEntry;
        public final ExceptGuDongSelectActivity mGuDongSelectActivity;

        public AllDongListener(ExceptGuDongSelectActivity exceptGuDongSelectActivity, Map.Entry entry) {
            this.mGuDongSelectActivity = exceptGuDongSelectActivity;
            this.mEntry = entry;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            this.mGuDongSelectActivity.mExceptMap.put(Integer.valueOf(((Integer) this.mEntry.getKey()).intValue()), null);
            this.mGuDongSelectActivity.mGuDongListAdapter.notifyDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class SaveDialog extends AlertDialog.Builder {
        public SaveDialog(Context context) {
            super(context);
            setTitle("파일이름을 입력하세요.");
            final EditText editText = new EditText(ExceptGuDongSelectActivity.this);
            editText.setInputType(1);
            setView(editText);
            setPositiveButton("확인", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.ExceptGuDongSelectActivity$SaveDialog$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ExceptGuDongSelectActivity.SaveDialog.this.m20x985e93f6(editText, dialogInterface, i);
                }
            });
            setNegativeButton("취소", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.ExceptGuDongSelectActivity$SaveDialog$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$new$0$com-movies-player-activities-ExceptGuDongSelectActivity$SaveDialog  reason: not valid java name */
        public /* synthetic */ void m20x985e93f6(EditText editText, DialogInterface dialogInterface, int i) {
            String obj = editText.getText().toString();
            if (obj.equals("")) {
                Toast.makeText(ExceptGuDongSelectActivity.this, "파일이름을 입력하세요.", 0).show();
                ExceptGuDongSelectActivity exceptGuDongSelectActivity = ExceptGuDongSelectActivity.this;
                ExceptGuDongSelectActivity exceptGuDongSelectActivity2 = ExceptGuDongSelectActivity.this;
                exceptGuDongSelectActivity.mSaveLoadDlg = new SaveDialog(exceptGuDongSelectActivity2).show();
            } else if (FileUtils.isExistExceptionFile(obj)) {
                Toast.makeText(ExceptGuDongSelectActivity.this, "입력하신 이름의 파일이 이미 존재합니다.\n다시 입력하세요.", 0).show();
                ExceptGuDongSelectActivity exceptGuDongSelectActivity3 = ExceptGuDongSelectActivity.this;
                ExceptGuDongSelectActivity exceptGuDongSelectActivity4 = ExceptGuDongSelectActivity.this;
                exceptGuDongSelectActivity3.mSaveLoadDlg = new SaveDialog(exceptGuDongSelectActivity4).show();
            } else {
                FileUtils.saveExceptionTextToFile(obj, ConvertUtil.integerMapToString(ExceptGuDongSelectActivity.this.mGuDongListAdapter.mSettingExceptMap));
                Toast.makeText(ExceptGuDongSelectActivity.this, "파일이 성공적으로 저장되었습니다.", 0).show();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class LoadDialog extends AlertDialog.Builder {
        public LoadDialog(Context context) {
            super(context);
            setTitle("로드할 파일을 선택하세요.");
            final int[] iArr = {-1};
            final String[] strArr = (String[]) FileUtils.getExceptionFileList().toArray(new String[0]);
            setSingleChoiceItems(strArr, iArr[0], new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.ExceptGuDongSelectActivity$LoadDialog$$ExternalSyntheticLambda4
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ExceptGuDongSelectActivity.LoadDialog.lambda$new$0(iArr, dialogInterface, i);
                }
            });
            setPositiveButton("확인", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.ExceptGuDongSelectActivity$LoadDialog$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ExceptGuDongSelectActivity.LoadDialog.this.m16xe829a0fe(iArr, strArr, dialogInterface, i);
                }
            });
            setNegativeButton("취소", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.ExceptGuDongSelectActivity$LoadDialog$$ExternalSyntheticLambda5
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            setNeutralButton("삭제", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.ExceptGuDongSelectActivity$LoadDialog$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ExceptGuDongSelectActivity.LoadDialog.this.m19x59e1927a(iArr, strArr, dialogInterface, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$new$0(int[] iArr, DialogInterface dialogInterface, int i) {
            iArr[0] = i;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$new$1$com-movies-player-activities-ExceptGuDongSelectActivity$LoadDialog  reason: not valid java name */
        public /* synthetic */ void m16xe829a0fe(int[] iArr, String[] strArr, DialogInterface dialogInterface, int i) {
            try {
                if (iArr[0] == -1) {
                    Toast.makeText(ExceptGuDongSelectActivity.this, "파일을 정확히 선택하세요.", 0).show();
                    ExceptGuDongSelectActivity exceptGuDongSelectActivity = ExceptGuDongSelectActivity.this;
                    ExceptGuDongSelectActivity exceptGuDongSelectActivity2 = ExceptGuDongSelectActivity.this;
                    exceptGuDongSelectActivity.mSaveLoadDlg = new LoadDialog(exceptGuDongSelectActivity2).show();
                    return;
                }
                String loadExceptionFileToText = FileUtils.loadExceptionFileToText(strArr[iArr[0]]);
                if (loadExceptionFileToText.isEmpty()) {
                    Toast.makeText(ExceptGuDongSelectActivity.this, "로드가 실패되었습니다.\n해당 파일에 내용이 없습니다.", 0).show();
                    return;
                }
                Map<Integer, ArrayList<Integer>> stringToIntegerMap = ConvertUtil.stringToIntegerMap(loadExceptionFileToText);
                ExceptGuDongSelectActivity.this.mExceptMap = stringToIntegerMap;
                ExceptGuDongSelectActivity.this.mGuDongListAdapter.mSettingExceptMap = stringToIntegerMap;
                ExceptGuDongSelectActivity.this.mGuDongListAdapter.notifyDataSetChanged();
                Toast.makeText(ExceptGuDongSelectActivity.this, "제외지가 성공적으로 로드되었습니다.", 0).show();
            } catch (Exception unused) {
                Toast.makeText(ExceptGuDongSelectActivity.this, "로드가 실패되었습니다.\n해당 파일이 UTF-8형식으로 저장되지 않았습니다.", 0).show();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$new$5$com-movies-player-activities-ExceptGuDongSelectActivity$LoadDialog  reason: not valid java name */
        public /* synthetic */ void m19x59e1927a(final int[] iArr, final String[] strArr, DialogInterface dialogInterface, int i) {
            if (iArr[0] == -1) {
                Toast.makeText(ExceptGuDongSelectActivity.this, "삭제할 파일을 선택하세요.", 0).show();
                ExceptGuDongSelectActivity exceptGuDongSelectActivity = ExceptGuDongSelectActivity.this;
                ExceptGuDongSelectActivity exceptGuDongSelectActivity2 = ExceptGuDongSelectActivity.this;
                exceptGuDongSelectActivity.mSaveLoadDlg = new LoadDialog(exceptGuDongSelectActivity2).show();
                return;
            }
            ExceptGuDongSelectActivity.this.mDeleteDlg = new AlertDialog.Builder(ExceptGuDongSelectActivity.this.mSaveLoadDlg.getContext()).setTitle("파일을 삭제하시겠습니까?").setPositiveButton("Yes", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.ExceptGuDongSelectActivity$LoadDialog$$ExternalSyntheticLambda3
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface2, int i2) {
                    ExceptGuDongSelectActivity.LoadDialog.this.m17x210599bc(strArr, iArr, dialogInterface2, i2);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.ExceptGuDongSelectActivity$LoadDialog$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface2, int i2) {
                    ExceptGuDongSelectActivity.LoadDialog.this.m18xbd73961b(dialogInterface2, i2);
                }
            }).show();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$new$3$com-movies-player-activities-ExceptGuDongSelectActivity$LoadDialog  reason: not valid java name */
        public /* synthetic */ void m17x210599bc(String[] strArr, int[] iArr, DialogInterface dialogInterface, int i) {
            try {
                FileUtils.deleteExceptionFile(strArr[iArr[0]]);
            } catch (Exception e) {
                LogUtils.uploadLog("제외지 설정파일삭제 오류: " + e.getMessage());
            }
            dialogInterface.cancel();
            ExceptGuDongSelectActivity.this.onLoad();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$new$4$com-movies-player-activities-ExceptGuDongSelectActivity$LoadDialog  reason: not valid java name */
        public /* synthetic */ void m18xbd73961b(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
            ExceptGuDongSelectActivity exceptGuDongSelectActivity = ExceptGuDongSelectActivity.this;
            ExceptGuDongSelectActivity exceptGuDongSelectActivity2 = ExceptGuDongSelectActivity.this;
            exceptGuDongSelectActivity.mSaveLoadDlg = new LoadDialog(exceptGuDongSelectActivity2).show();
        }
    }
}
