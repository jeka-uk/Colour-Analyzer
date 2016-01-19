package com.example.lenovo.colouranalyzer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.colouranalyzer.R;
import com.example.lenovo.colouranalyzer.common.CommonUtils;
import com.example.lenovo.colouranalyzer.db.ColorItem;

public class DuplicateItemSqlAdapter extends BaseAdapter {

    private Context mContext;
    private ColorItem [] mColorItems;

    public DuplicateItemSqlAdapter(Context context, ColorItem[] colorItems) {
        mContext = context;
        mColorItems = colorItems;
    }

    @Override
    public int getCount() {
        return mColorItems.length;
    }

    @Override
    public Object getItem(int position) {
        return mColorItems[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_layout, null);
            //brand new
            holder = new ViewHolder();

            holder.mNameItem = (TextView) convertView.findViewById(R.id.item_name_of_item);
            holder.mRgb = (TextView) convertView.findViewById(R.id.item_rgb);
            holder.mHex = (TextView) convertView.findViewById(R.id.item_hex);
            holder.mHsl = (TextView) convertView.findViewById(R.id.item_hsl);
            holder.mHsv = (TextView) convertView.findViewById(R.id.item_hsv);
            holder.mData = (TextView) convertView.findViewById(R.id.item_data);
            holder.mImageItem = (ImageView) convertView.findViewById(R.id.item_image);
            holder.mItemCheckBox = (CheckBox) convertView.findViewById(R.id.item_edit_checbox);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        ColorItem colorItem = mColorItems[position];

        holder.mHsv.setText(CommonUtils.getRgbToHsv(colorItem.getRgbItem()));
        holder.mImageItem.setImageBitmap(CommonUtils.convertByteToImage(colorItem.getImageItem()));
        holder.mNameItem.setText(colorItem.getNameItem());
        holder.mRgb.setText(CommonUtils.getRgbToString(colorItem.getRgbItem()));
        holder.mHex.setText(colorItem.getHexItem());
        holder.mData.setText(colorItem.getAddDateItem());
        holder.mHsl.setText(CommonUtils.getRgbToHsl(colorItem.getRgbItem()));
        holder.mItemCheckBox.setButtonDrawable(android.R.color.transparent);
        holder.mItemCheckBox.setChecked(true);

        return convertView;
    }

    private static class ViewHolder{
         TextView mNameItem;
         TextView mRgb;
         TextView mHex;
         TextView mHsl;
         TextView mHsv;
         TextView mData;
         ImageView mImageItem;
         CheckBox mItemCheckBox;
    }
}
