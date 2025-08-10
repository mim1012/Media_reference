package com.movies.player.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.movies.player.activities.DestGuDongSelectActivity;
import com.movies.player.model.DestDongCollection;
import com.movies.player.model.DongItem;
import com.slcbxla.wkflshgies.R;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public class DestGuDongListAdapter extends BaseAdapter {
    public ArrayList<Map.Entry<Integer, String>> mDestSigunguEntryList;
    public DestDongCollection mDongCollection;
    public LayoutInflater mLayoutInflater = null;
    public Map<Integer, ArrayList<Integer>> mSettingDestMap;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return 0L;
    }

    public DestGuDongListAdapter(DestGuDongSelectActivity destGuDongSelectActivity) {
        this.mDongCollection = DestDongCollection.getInstance(destGuDongSelectActivity);
        ArrayList<Map.Entry<Integer, String>> arrayList = new ArrayList<>();
        this.mDestSigunguEntryList = arrayList;
        arrayList.addAll(this.mDongCollection.mDestSigunguMap.entrySet());
        this.mSettingDestMap = destGuDongSelectActivity.mDestMap;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mDestSigunguEntryList.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.mDestSigunguEntryList.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        String str;
        if (view == null) {
            Context context = viewGroup.getContext();
            if (this.mLayoutInflater == null) {
                this.mLayoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
            }
            view = this.mLayoutInflater.inflate(R.layout.filter_list_item3, viewGroup, false);
        }
        final Map.Entry<Integer, String> entry = this.mDestSigunguEntryList.get(i);
        ((TextView) view.findViewById(R.id.textgu)).setText(entry.getValue());
        TextView textView = (TextView) view.findViewById(R.id.txtDong);
        if (this.mSettingDestMap.containsKey(entry.getKey())) {
            textView.setVisibility(0);
            ArrayList<Integer> arrayList = this.mSettingDestMap.get(entry.getKey());
            if (arrayList == null || arrayList.size() <= 0) {
                str = "전체";
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    sb.append(((DongItem) Objects.requireNonNull(this.mDongCollection.mAllDongMap.get(arrayList.get(i2)))).sDongName).append(",");
                }
                str = sb.toString();
            }
        } else {
            textView.setVisibility(8);
            str = "";
        }
        textView.setText(str);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.check);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.movies.player.adapter.DestGuDongListAdapter$$ExternalSyntheticLambda0
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                DestGuDongListAdapter.this.m36lambda$getView$0$commoviesplayeradapterDestGuDongListAdapter(entry, compoundButton, z);
            }
        });
        checkBox.setChecked(this.mSettingDestMap.containsKey(entry.getKey()));
        return view;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$getView$0$com-movies-player-adapter-DestGuDongListAdapter  reason: not valid java name */
    public /* synthetic */ void m36lambda$getView$0$commoviesplayeradapterDestGuDongListAdapter(Map.Entry entry, CompoundButton compoundButton, boolean z) {
        if (!z) {
            this.mSettingDestMap.remove(entry.getKey());
        } else if (!this.mSettingDestMap.containsKey(entry.getKey())) {
            this.mSettingDestMap.put(Integer.valueOf(((Integer) entry.getKey()).intValue()), null);
        }
        notifyDataSetChanged();
    }
}
