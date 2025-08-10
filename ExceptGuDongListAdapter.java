package com.movies.player.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.movies.player.activities.ExceptGuDongSelectActivity;
import com.movies.player.model.ExceptDongCollection;
import com.movies.player.utils.ConvertUtil;
import com.slcbxla.wkflshgies.R;
import java.util.ArrayList;
import java.util.Map;
/* loaded from: classes.dex */
public class ExceptGuDongListAdapter extends BaseAdapter {
    public ExceptDongCollection mDongCollection;
    public ArrayList mExceptSigunguEntryList;
    public LayoutInflater mLayoutInflater = null;
    public Map<Integer, ArrayList<Integer>> mSettingExceptMap;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return 0L;
    }

    public ExceptGuDongListAdapter(ExceptGuDongSelectActivity exceptGuDongSelectActivity) {
        this.mDongCollection = ExceptDongCollection.getInstance(exceptGuDongSelectActivity);
        ArrayList arrayList = new ArrayList();
        this.mExceptSigunguEntryList = arrayList;
        arrayList.addAll(this.mDongCollection.mExceptSigunguMap.entrySet());
        this.mSettingExceptMap = exceptGuDongSelectActivity.mExceptMap;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mExceptSigunguEntryList.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.mExceptSigunguEntryList.get(i);
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
        final Map.Entry entry = (Map.Entry) this.mExceptSigunguEntryList.get(i);
        ((TextView) view.findViewById(R.id.textgu)).setText((CharSequence) entry.getValue());
        TextView textView = (TextView) view.findViewById(R.id.txtDong);
        if (this.mSettingExceptMap.containsKey(entry.getKey())) {
            textView.setVisibility(0);
            ArrayList<Integer> arrayList = this.mSettingExceptMap.get(entry.getKey());
            if (arrayList != null) {
                StringBuilder sb = new StringBuilder();
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    sb.append(this.mDongCollection.mAllDongMap.get(ConvertUtil.parseInteger(arrayList.get(i2))).sDongName + ",");
                }
                str = sb.toString();
            } else {
                str = "전체";
            }
        } else {
            textView.setVisibility(8);
            str = "";
        }
        textView.setText(str);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.check);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.movies.player.adapter.ExceptGuDongListAdapter.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (!z) {
                    ExceptGuDongListAdapter.this.mSettingExceptMap.remove(entry.getKey());
                } else if (!ExceptGuDongListAdapter.this.mSettingExceptMap.containsKey(entry.getKey())) {
                    ExceptGuDongListAdapter.this.mSettingExceptMap.put(Integer.valueOf(((Integer) entry.getKey()).intValue()), null);
                }
                ExceptGuDongListAdapter.this.notifyDataSetChanged();
            }
        });
        checkBox.setChecked(this.mSettingExceptMap.containsKey(entry.getKey()));
        return view;
    }
}
