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
import com.movies.player.activities.DestGuDongSelectActivity;
import com.movies.player.adapter.DestGuDongListAdapter;
import com.movies.player.model.DestDongCollection;
import com.movies.player.utils.ConvertUtil;
import com.movies.player.utils.FileUtils;
import com.movies.player.utils.LogUtils;
import com.movies.player.utils.SharedData;
import com.slcbxla.wkflshgies.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
/* loaded from: classes.dex */
public class DestGuDongSelectActivity extends Activity implements View.OnClickListener {
    public Map<Integer, ArrayList<Integer>> mDestMap;
    public DestDongCollection mDongCollection;
    public DestGuDongListAdapter mGuDongListAdapter;
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
    /* renamed from: com.movies.player.activities.DestGuDongSelectActivity$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            DestGuDongSelectActivity destGuDongSelectActivity = DestGuDongSelectActivity.this;
            destGuDongSelectActivity.mDongCollection = DestDongCollection.getInstance(destGuDongSelectActivity);
            DestGuDongSelectActivity.this.mDestMap = new TreeMap(DestGuDongSelectActivity.this.mDongCollection.mDestMap);
            DestGuDongSelectActivity.this.mGuDongListAdapter = new DestGuDongListAdapter(DestGuDongSelectActivity.this);
            DestGuDongSelectActivity destGuDongSelectActivity2 = DestGuDongSelectActivity.this;
            destGuDongSelectActivity2.mGuDongListView = (ListView) destGuDongSelectActivity2.findViewById(R.id.list);
            DestGuDongSelectActivity.this.mGuDongListView.setAdapter((ListAdapter) DestGuDongSelectActivity.this.mGuDongListAdapter);
            DestGuDongSelectActivity.this.mGuDongListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.movies.player.activities.DestGuDongSelectActivity$1$$ExternalSyntheticLambda0
                @Override // android.widget.AdapterView.OnItemClickListener
                public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
                    DestGuDongSelectActivity.AnonymousClass1.this.m9x1ce95394(adapterView, view, i, j);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$run$0$com-movies-player-activities-DestGuDongSelectActivity$1  reason: not valid java name */
        public /* synthetic */ void m9x1ce95394(AdapterView adapterView, View view, int i, long j) {
            DestGuDongSelectActivity.this.onGudongItemSelected(i);
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
                this.mDongCollection.setDestMap(this.mDestMap);
                this.mDongCollection.saveDestMap();
                SharedData.sAllDestDongCodes = DestDongCollection.getInstance(this).getAllDestDongCodes();
                setResult(-1, getIntent());
                break;
            case R.id.btnLoad /* 2131230829 */:
                onLoad();
                return;
            case R.id.btnSave /* 2131230832 */:
                onSave();
                return;
            case R.id.btnSetAll /* 2131230833 */:
                Iterator<Map.Entry<Integer, String>> it = this.mGuDongListAdapter.mDestSigunguEntryList.iterator();
                while (it.hasNext()) {
                    Map.Entry<Integer, String> next = it.next();
                    if (!this.mGuDongListAdapter.mSettingDestMap.containsKey(Integer.valueOf(next.getKey().intValue()))) {
                        this.mGuDongListAdapter.mSettingDestMap.put(Integer.valueOf(next.getKey().intValue()), null);
                    }
                }
                this.mGuDongListAdapter.notifyDataSetChanged();
                return;
            case R.id.btnSido /* 2131230835 */:
                onSido();
                return;
            case R.id.btnUnSetAll /* 2131230836 */:
                this.mGuDongListAdapter.mSettingDestMap.clear();
                this.mGuDongListAdapter.notifyDataSetChanged();
                return;
            default:
                return;
        }
        finish();
    }

    public void onSave() {
        if (this.mGuDongListAdapter.mSettingDestMap.size() < 1) {
            Toast.makeText(this, "도착지를 선택한후 저장하세요.", 0).show();
        } else {
            this.mSaveLoadDlg = new SaveDialog(this).show();
        }
    }

    public void onLoad() {
        if (FileUtils.getDestinationFileList().size() == 0) {
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
            if (this.mDongCollection.mDestSidoList.contains(Integer.valueOf(intValue))) {
                zArr[i] = true;
            }
            i++;
        }
        new AlertDialog.Builder(this).setTitle("시도 선택").setMultiChoiceItems(strArr, zArr, new SidoChoiceListener(zArr)).setPositiveButton("확인", new SidoSelectionOKListener(this, this.mDongCollection.mDestSidoList, zArr, strArr, map)).setNegativeButton("취소", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.DestGuDongSelectActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                DestGuDongSelectActivity.lambda$onSido$0(dialogInterface, i2);
            }
        }).create().show();
    }

    /* loaded from: classes.dex */
    public static class SidoChoiceListener implements DialogInterface.OnMultiChoiceClickListener {
        boolean[] mIsChecks;

        public SidoChoiceListener(boolean[] zArr) {
            this.mIsChecks = zArr;
        }

        @Override // android.content.DialogInterface.OnMultiChoiceClickListener
        public void onClick(DialogInterface dialogInterface, int i, boolean z) {
            this.mIsChecks[i] = z;
        }
    }

    /* loaded from: classes.dex */
    public static class SidoSelectionOKListener implements DialogInterface.OnClickListener {
        public ArrayList<Integer> mDestArrayList;
        public DestGuDongSelectActivity mGuDongSelectActivity;
        public boolean[] mIsChecks;
        public String[] mNames;
        public Map<Integer, String> mSrcMap;

        public SidoSelectionOKListener(DestGuDongSelectActivity destGuDongSelectActivity, ArrayList<Integer> arrayList, boolean[] zArr, String[] strArr, Map<Integer, String> map) {
            this.mGuDongSelectActivity = destGuDongSelectActivity;
            this.mDestArrayList = arrayList;
            this.mIsChecks = zArr;
            this.mNames = strArr;
            this.mSrcMap = map;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            this.mDestArrayList.clear();
            int i2 = 0;
            while (true) {
                boolean[] zArr = this.mIsChecks;
                if (i2 >= zArr.length) {
                    break;
                }
                if (zArr[i2]) {
                    String str = this.mNames[i2];
                    Iterator<Integer> it = this.mSrcMap.keySet().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        int intValue = it.next().intValue();
                        if (Objects.equals(this.mSrcMap.get(Integer.valueOf(intValue)), str)) {
                            this.mDestArrayList.add(Integer.valueOf(intValue));
                            break;
                        }
                    }
                }
                i2++;
            }
            DestDongCollection destDongCollection = this.mGuDongSelectActivity.mDongCollection;
            try {
                SharedPreferences.Editor edit = destDongCollection.mContext.getSharedPreferences("dong_setting", 0).edit();
                edit.putString("dest_sido", ConvertUtil.integerArrayToString(destDongCollection.mDestSidoList));
                edit.apply();
            } catch (Exception unused) {
            }
            this.mGuDongSelectActivity.mDongCollection.refreshDestSiguMap();
            DestGuDongListAdapter destGuDongListAdapter = this.mGuDongSelectActivity.mGuDongListAdapter;
            destGuDongListAdapter.mDestSigunguEntryList.clear();
            destGuDongListAdapter.mDestSigunguEntryList.addAll(destGuDongListAdapter.mDongCollection.mDestSigunguMap.entrySet());
            this.mGuDongSelectActivity.mGuDongListAdapter.notifyDataSetChanged();
        }
    }

    public void onGudongItemSelected(int i) {
        Map.Entry<Integer, String> entry = this.mGuDongListAdapter.mDestSigunguEntryList.get(i);
        Map<Integer, String> allDongNamesFromSigunguCode = this.mDongCollection.getAllDongNamesFromSigunguCode(entry.getKey().intValue());
        boolean[] zArr = new boolean[allDongNamesFromSigunguCode.size()];
        String[] strArr = new String[allDongNamesFromSigunguCode.size()];
        ArrayList<Integer> arrayList = this.mDestMap.get(entry.getKey());
        int i2 = 0;
        for (Integer num : allDongNamesFromSigunguCode.keySet()) {
            int intValue = num.intValue();
            strArr[i2] = allDongNamesFromSigunguCode.get(Integer.valueOf(intValue));
            if (arrayList != null && arrayList.contains(Integer.valueOf(intValue))) {
                zArr[i2] = true;
            }
            i2++;
        }
        new AlertDialog.Builder(this).setTitle(entry.getValue()).setMultiChoiceItems(strArr, zArr, new DongChoiceListener(this, zArr)).setPositiveButton("확인", new ConfirmDongListener(this, allDongNamesFromSigunguCode, zArr, strArr, entry)).setNegativeButton("전체", new AllDongListener(this, entry)).create().show();
    }

    /* loaded from: classes.dex */
    public static class DongChoiceListener implements DialogInterface.OnMultiChoiceClickListener {
        public boolean[] mIsChecks;

        public DongChoiceListener(DestGuDongSelectActivity destGuDongSelectActivity, boolean[] zArr) {
            this.mIsChecks = zArr;
        }

        @Override // android.content.DialogInterface.OnMultiChoiceClickListener
        public void onClick(DialogInterface dialogInterface, int i, boolean z) {
            this.mIsChecks[i] = z;
        }
    }

    /* loaded from: classes.dex */
    public static class ConfirmDongListener implements DialogInterface.OnClickListener {
        public final Map<Integer, String> mAllDongInSigunguItemMap;
        public final Map.Entry<Integer, String> mEntry;
        public final DestGuDongSelectActivity mGuDongSelectActivity;
        public final boolean[] mIsCheck;
        public final String[] mOnlyDongNames;

        public ConfirmDongListener(DestGuDongSelectActivity destGuDongSelectActivity, Map<Integer, String> map, boolean[] zArr, String[] strArr, Map.Entry<Integer, String> entry) {
            this.mGuDongSelectActivity = destGuDongSelectActivity;
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
                        if (Objects.equals(this.mAllDongInSigunguItemMap.get(Integer.valueOf(intValue)), this.mOnlyDongNames[i2])) {
                            arrayList.add(Integer.valueOf(intValue));
                            break;
                        }
                    }
                }
            }
            if (arrayList.isEmpty()) {
                this.mGuDongSelectActivity.mDestMap.put(Integer.valueOf(this.mEntry.getKey().intValue()), null);
            } else {
                this.mGuDongSelectActivity.mDestMap.put(Integer.valueOf(this.mEntry.getKey().intValue()), arrayList);
            }
            this.mGuDongSelectActivity.mGuDongListAdapter.notifyDataSetChanged();
        }
    }

    /* loaded from: classes.dex */
    public static class AllDongListener implements DialogInterface.OnClickListener {
        public final Map.Entry<Integer, String> mEntry;
        public final DestGuDongSelectActivity mGuDongSelectActivity;

        public AllDongListener(DestGuDongSelectActivity destGuDongSelectActivity, Map.Entry<Integer, String> entry) {
            this.mGuDongSelectActivity = destGuDongSelectActivity;
            this.mEntry = entry;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            this.mGuDongSelectActivity.mDestMap.put(Integer.valueOf(this.mEntry.getKey().intValue()), null);
            this.mGuDongSelectActivity.mGuDongListAdapter.notifyDataSetChanged();
        }
    }

    /* loaded from: classes.dex */
    public class SaveDialog extends AlertDialog.Builder {
        public SaveDialog(Context context) {
            super(context);
            setTitle("파일이름을 입력하세요.");
            final EditText editText = new EditText(DestGuDongSelectActivity.this);
            editText.setInputType(1);
            setView(editText);
            setPositiveButton("확인", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.DestGuDongSelectActivity$SaveDialog$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    DestGuDongSelectActivity.SaveDialog.this.m14xc4eae60d(editText, dialogInterface, i);
                }
            });
            setNegativeButton("취소", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.DestGuDongSelectActivity$SaveDialog$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$new$0$com-movies-player-activities-DestGuDongSelectActivity$SaveDialog  reason: not valid java name */
        public /* synthetic */ void m14xc4eae60d(EditText editText, DialogInterface dialogInterface, int i) {
            String obj = editText.getText().toString();
            if (obj.equals("")) {
                Toast.makeText(DestGuDongSelectActivity.this, "파일이름을 입력하세요.", 0).show();
                DestGuDongSelectActivity destGuDongSelectActivity = DestGuDongSelectActivity.this;
                DestGuDongSelectActivity destGuDongSelectActivity2 = DestGuDongSelectActivity.this;
                destGuDongSelectActivity.mSaveLoadDlg = new SaveDialog(destGuDongSelectActivity2).show();
            } else if (FileUtils.isExistDestinationFile(obj)) {
                Toast.makeText(DestGuDongSelectActivity.this, "입력하신 이름의 파일이 이미 존재합니다.\n다시 입력하세요.", 0).show();
                DestGuDongSelectActivity destGuDongSelectActivity3 = DestGuDongSelectActivity.this;
                DestGuDongSelectActivity destGuDongSelectActivity4 = DestGuDongSelectActivity.this;
                destGuDongSelectActivity3.mSaveLoadDlg = new SaveDialog(destGuDongSelectActivity4).show();
            } else {
                FileUtils.saveDestinationTextToFile(obj, ConvertUtil.integerMapToString(DestGuDongSelectActivity.this.mGuDongListAdapter.mSettingDestMap));
                Toast.makeText(DestGuDongSelectActivity.this, "파일이 성공적으로 저장되었습니다.", 0).show();
            }
        }
    }

    /* loaded from: classes.dex */
    public class LoadDialog extends AlertDialog.Builder {
        public LoadDialog(Context context) {
            super(context);
            setTitle("로드할 파일을 선택하세요.");
            final int[] iArr = {-1};
            final String[] strArr = (String[]) FileUtils.getDestinationFileList().toArray(new String[0]);
            setSingleChoiceItems(strArr, iArr[0], new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.DestGuDongSelectActivity$LoadDialog$$ExternalSyntheticLambda4
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    DestGuDongSelectActivity.LoadDialog.lambda$new$0(iArr, dialogInterface, i);
                }
            });
            setPositiveButton("확인", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.DestGuDongSelectActivity$LoadDialog$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    DestGuDongSelectActivity.LoadDialog.this.m10xe2777ed5(iArr, strArr, dialogInterface, i);
                }
            });
            setNegativeButton("취소", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.DestGuDongSelectActivity$LoadDialog$$ExternalSyntheticLambda5
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            setNeutralButton("삭제", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.DestGuDongSelectActivity$LoadDialog$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    DestGuDongSelectActivity.LoadDialog.this.m13x8b359f51(iArr, strArr, dialogInterface, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$new$0(int[] iArr, DialogInterface dialogInterface, int i) {
            iArr[0] = i;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$new$1$com-movies-player-activities-DestGuDongSelectActivity$LoadDialog  reason: not valid java name */
        public /* synthetic */ void m10xe2777ed5(int[] iArr, String[] strArr, DialogInterface dialogInterface, int i) {
            try {
                if (iArr[0] == -1) {
                    Toast.makeText(DestGuDongSelectActivity.this, "파일을 정확히 선택하세요.", 0).show();
                    DestGuDongSelectActivity destGuDongSelectActivity = DestGuDongSelectActivity.this;
                    DestGuDongSelectActivity destGuDongSelectActivity2 = DestGuDongSelectActivity.this;
                    destGuDongSelectActivity.mSaveLoadDlg = new LoadDialog(destGuDongSelectActivity2).show();
                    return;
                }
                String loadDestinationFileToText = FileUtils.loadDestinationFileToText(strArr[iArr[0]]);
                if (loadDestinationFileToText.isEmpty()) {
                    Toast.makeText(DestGuDongSelectActivity.this, "로드가 실패되었습니다.\n해당 파일에 내용이 없습니다.", 0).show();
                    return;
                }
                Map<Integer, ArrayList<Integer>> stringToIntegerMap = ConvertUtil.stringToIntegerMap(loadDestinationFileToText);
                DestGuDongSelectActivity.this.mDestMap = stringToIntegerMap;
                DestGuDongSelectActivity.this.mGuDongListAdapter.mSettingDestMap = stringToIntegerMap;
                DestGuDongSelectActivity.this.mGuDongListAdapter.notifyDataSetChanged();
                Toast.makeText(DestGuDongSelectActivity.this, "도착지가 성공적으로 로드되었습니다.", 0).show();
            } catch (Exception unused) {
                Toast.makeText(DestGuDongSelectActivity.this, "로드가 실패되었습니다.\n해당 파일이 UTF-8형식으로 저장되지 않았습니다.", 0).show();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$new$5$com-movies-player-activities-DestGuDongSelectActivity$LoadDialog  reason: not valid java name */
        public /* synthetic */ void m13x8b359f51(final int[] iArr, final String[] strArr, DialogInterface dialogInterface, int i) {
            if (iArr[0] == -1) {
                Toast.makeText(DestGuDongSelectActivity.this, "삭제할 파일을 선택하세요.", 0).show();
                DestGuDongSelectActivity destGuDongSelectActivity = DestGuDongSelectActivity.this;
                DestGuDongSelectActivity destGuDongSelectActivity2 = DestGuDongSelectActivity.this;
                destGuDongSelectActivity.mSaveLoadDlg = new LoadDialog(destGuDongSelectActivity2).show();
                return;
            }
            DestGuDongSelectActivity.this.mDeleteDlg = new AlertDialog.Builder(DestGuDongSelectActivity.this.mSaveLoadDlg.getContext()).setTitle("파일을 삭제하시겠습니까?").setPositiveButton("Yes", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.DestGuDongSelectActivity$LoadDialog$$ExternalSyntheticLambda3
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface2, int i2) {
                    DestGuDongSelectActivity.LoadDialog.this.m11xb6d68f13(strArr, iArr, dialogInterface2, i2);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() { // from class: com.movies.player.activities.DestGuDongSelectActivity$LoadDialog$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface2, int i2) {
                    DestGuDongSelectActivity.LoadDialog.this.m12x21061732(dialogInterface2, i2);
                }
            }).show();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$new$3$com-movies-player-activities-DestGuDongSelectActivity$LoadDialog  reason: not valid java name */
        public /* synthetic */ void m11xb6d68f13(String[] strArr, int[] iArr, DialogInterface dialogInterface, int i) {
            try {
                FileUtils.deleteDestinationFile(strArr[iArr[0]]);
            } catch (Exception e) {
                LogUtils.uploadLog("도착지 설정파일삭제 오류: " + e.getMessage());
            }
            dialogInterface.cancel();
            DestGuDongSelectActivity.this.onLoad();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$new$4$com-movies-player-activities-DestGuDongSelectActivity$LoadDialog  reason: not valid java name */
        public /* synthetic */ void m12x21061732(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
            DestGuDongSelectActivity destGuDongSelectActivity = DestGuDongSelectActivity.this;
            DestGuDongSelectActivity destGuDongSelectActivity2 = DestGuDongSelectActivity.this;
            destGuDongSelectActivity.mSaveLoadDlg = new LoadDialog(destGuDongSelectActivity2).show();
        }
    }
}
