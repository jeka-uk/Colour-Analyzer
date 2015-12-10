package com.example.lenovo.colouranalyzer.adapters;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.colouranalyzer.R;
import com.example.lenovo.colouranalyzer.common.CommonUtils;
import com.example.lenovo.colouranalyzer.common.OnSwipeTouchListener;
import com.example.lenovo.colouranalyzer.db.ColorItem;

import org.w3c.dom.Text;

import java.util.List;


public class DuplicateSqlLineAdapter extends ArrayAdapter<ColorItem> {

    private List<ColorItem> mDataItem;
    private TextView mNameItem, mRgb, mHex, mHsl, mHsv, mData;
    private ImageView mItemItem;
    private CheckBox mEditCheckBox;

    public DuplicateSqlLineAdapter(Context context, List<ColorItem> colorItem) {
        super(context, 0, colorItem);
        this.mDataItem = colorItem;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout, parent, false);
        }

        mEditCheckBox = (CheckBox) convertView.findViewById(R.id.item_edit_checbox);
        mEditCheckBox.setButtonDrawable(android.R.color.transparent);
        mItemItem = (ImageView) convertView.findViewById(R.id.item_image);
        mNameItem = (TextView) convertView.findViewById(R.id.item_name_of_item);
        mRgb = (TextView) convertView.findViewById(R.id.item_rgb);
        mHex = (TextView) convertView.findViewById(R.id.item_hex);
        mData = (TextView) convertView.findViewById(R.id.item_data);
        mHsl = (TextView) convertView.findViewById(R.id.item_hsl);
        mHsv = (TextView) convertView.findViewById(R.id.item_hsv);


        mHsv.setText(CommonUtils.getRgbToHsv(mDataItem.get(position).getRgbItem()));
        mItemItem.setImageBitmap(CommonUtils.convertByteToImage(mDataItem.get(position).getImageItem()));
        mNameItem.setText(mDataItem.get(position).getNameItem());
        mRgb.setText(CommonUtils.getRgbToString(mDataItem.get(position).getRgbItem()));
        mHex.setText(mDataItem.get(position).getHexItem());
        mData.setText(mDataItem.get(position).getAddDateItem());
        mHsl.setText(CommonUtils.getRgbToHsl(mDataItem.get(position).getRgbItem()));

        return convertView;
    }
}
