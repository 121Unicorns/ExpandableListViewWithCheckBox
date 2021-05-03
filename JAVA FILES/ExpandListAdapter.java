package com.expandablelistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandListAdapter extends BaseExpandableListAdapter {

    private ArrayList<ArrayList<String>> mGroupList = new ArrayList<>();
    private Context mContext;
    private List<String> mListDataHeader;
    private HashMap<String, List<String>> mListDataChild;
    final ArrayList<String> currentChildren = new ArrayList<String>();
    ArrayList<ArrayList<Boolean>> selectedChildCheckBoxStates = new ArrayList<>();

    ListedListener mListener = new ListedListener() {
        @Override
        public void onListChanged(ArrayList<String> chosenChildren) {

        }
    };

    public void setmListener(ListedListener mListener) {
        this.mListener = mListener;
    }

    public void setmGroupList(ArrayList<ArrayList<String>> mGroupList) {
        this.mGroupList = mGroupList;
    }

    class ViewHolder {
        public TextView groupName;
        public CheckBox childCheckBox;
    }

    public ExpandListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;

        initCheckStates(false);
    }

    private void initCheckStates(boolean defaultState) {
        for (int i = 0; i < this.mListDataHeader.size(); i++) {
            ArrayList<Boolean> childStates = new ArrayList<>();
            for (int j = 0; j < this.mListDataChild.get(this.mListDataHeader.get(i))
                    .size(); j++) {
                childStates.add(defaultState);
            }
            selectedChildCheckBoxStates.add(i, childStates);
        }
    }

    @Override
    public int getGroupCount() {
        return this.mListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);
        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.childCheckBox = (CheckBox) convertView.findViewById(R.id.listItem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.childCheckBox.setText(childText);

        if (selectedChildCheckBoxStates.size() <= groupPosition) {
            ArrayList<Boolean> childState = new ArrayList<>();
            for (int i = 0; i < this.mListDataHeader.get(groupPosition).length(); i++) {
                if (childState.size() > childPosition) {
                    if (childState.size() > childPosition)
                        childState.add(childPosition, false);
                    else
                        childState.add(false);
                }
            }
        } else {
            holder.childCheckBox.setChecked(selectedChildCheckBoxStates.get(groupPosition).get(childPosition));
        }

        holder.childCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean state = selectedChildCheckBoxStates.get(groupPosition).get(childPosition);
                selectedChildCheckBoxStates.get(groupPosition).remove(childPosition);
                selectedChildCheckBoxStates.get(groupPosition).add(childPosition, state ? false : true);

                System.out.println(groupPosition);

                if (holder.childCheckBox.isChecked()) {
                    currentChildren.add(holder.childCheckBox.getText().toString());
                } else if (currentChildren.contains(holder.childCheckBox.getText().toString()) && !holder.childCheckBox.isChecked()) {
                    currentChildren.remove(holder.childCheckBox.getText().toString());
                } else {
                    holder.childCheckBox.setChecked(false);
                }
            }
        });
        mListener.onListChanged(currentChildren);
        return convertView;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
            holder = new ViewHolder();
            holder.groupName = (TextView) convertView.findViewById(R.id.listGroup);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.groupName.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
