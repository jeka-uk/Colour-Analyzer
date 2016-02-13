package com.example.lenovo.colouranalyzer.fragments;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.colouranalyzer.R;
import com.example.lenovo.colouranalyzer.common.CommonUtils;
import com.example.lenovo.colouranalyzer.common.Constans;
import com.example.lenovo.colouranalyzer.db.ColorItem;
import com.example.lenovo.colouranalyzer.db.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DataColorFragment extends Fragment {

    @Bind(R.id.rgb_data_text_view) TextView mRgbColor;
    @Bind(R.id.hex_data_text_view) TextView mHexColor;
    @Bind(R.id.hsv_data_text_view) TextView mHsvColor;
    @Bind(R.id.hsl_data_text_view) TextView mHslColor;
    @Bind(R.id.name_item) TextView mNameItem;
    @Bind(R.id.image_color) ImageView mSampleColor;
    @Bind(R.id.data_layout) RelativeLayout mdataLayout;

    private boolean mNeedCalculate = false;
    private boolean mSaveData = false;
    private int mColorRGB;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private DatabaseHelper dbHelper;
    private boolean mUpdateData = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_color, container, false);
        ButterKnife.bind(this, view);

        mSharedPreferences = getActivity().getSharedPreferences(Constans.PREFS_FILE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();


        if(!mSharedPreferences.equals(null)){
            int retrieveColorRGB = mSharedPreferences.getInt(Constans.RGB_VALUE, 0);
            setmColorRGB(retrieveColorRGB);
            String retrieveName = mSharedPreferences.getString(Constans.NAME_ITEM, "Name");
            mNameItem.setText(retrieveName);
            setColor(mColorRGB);
        }



        if(mNeedCalculate){
            calculateColor();
        }else{
            mdataLayout.setVisibility(View.VISIBLE);
        }
        return view;
    }


    public void setNeedCalculate(boolean mNeedCalculate) {
        this.mNeedCalculate = mNeedCalculate;
    }

   public void setSaveData(boolean mSaveData){
        this.mSaveData = mSaveData;
    }


    private void calculateColor(){
        Handler mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if(msg != null)
                    setColor(msg.what);
                    setmColorRGB(msg.what);

                mEditor.putInt(Constans.RGB_VALUE, mColorRGB);
                mEditor.commit();
                saveDataToDb(mSharedPreferences.getString(Constans.NAME_ITEM, "Name"), mColorRGB, CommonUtils.getRgbToHex(mColorRGB), CommonUtils.convertImageToByte(setImage(Constans.FILE_PATCH)));
                mdataLayout.setVisibility(View.VISIBLE);
            };
        };
        if (Constans.FILE_PATCH.exists()) {
            new Thread(new Runnable() {
                public void run() {
                    mHandler.sendEmptyMessage(CommonUtils.getAverageColorRGB(setImage(Constans.FILE_PATCH)));
                }
            }).start();
        }
    }


    private void setColor(int RGB){
            mRgbColor.setText(CommonUtils.getRgbToString(RGB));
            mHexColor.setText(CommonUtils.getRgbToHex(RGB).toUpperCase());
            mHsvColor.setText(CommonUtils.getRgbToHsv(RGB));
            mSampleColor.setBackgroundColor(Color.parseColor(String.format(CommonUtils.getRgbToHex(RGB))));
        mHslColor.setText(CommonUtils.getRgbToHsl(RGB));


    }

    @OnClick(R.id.send_result)
    public void onSengResultToServer(View view){
        CommonUtils.startFragmentSlideVerticalDownUpWithBackStack(new SendDataToServer(), R.id.send_data_fragment, getFragmentManager());
    }


    private Bitmap setImage(File patch) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeFile(String.valueOf(patch), options);

        int imageWidth = options.outWidth;
        int displayWidth = CommonUtils.getDisplayWidth(getActivity());
        int coefficient = 0;

        if(imageWidth > displayWidth){
            coefficient = imageWidth/displayWidth + 1;
        }else{
            coefficient = 0;
        }

        options.inSampleSize = coefficient;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(String.valueOf(patch), options);
    }


    @Override
    public void onPause() {
        super.onPause();

    }


    public void setmColorRGB(int mColorRGB) {
        this.mColorRGB = mColorRGB;
    }


    @Override
    public void onResume() {
        super.onResume();

    }


    private void saveDataToDb(String nameValue, int rgbValue, String hexValue, byte[] imageValue){
        if(mUpdateData && existsInputName() != 0){
            update(existsInputName());
        }else{
            dbHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
            final RuntimeExceptionDao<ColorItem, Integer> colorDao = dbHelper.getColorRuntimeExceptionDao();
            colorDao.create(new ColorItem(nameValue, rgbValue, hexValue, imageValue));
        }

    }

    private void update(int id){
        int rgbValue;
        String hexValue;
        byte[] imageValue;

        rgbValue = mColorRGB;
        hexValue = CommonUtils.getRgbToHex(mColorRGB);
        imageValue = CommonUtils.convertImageToByte(setImage(Constans.FILE_PATCH));



        dbHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        ColorItem colorItem = null;
        colorItem = dbHelper.getColorRuntimeExceptionDao().queryForId(id);
        colorItem.setRgbItem(rgbValue);
        colorItem.setHexItem(hexValue);
        colorItem.setImageItem(imageValue);
        colorItem.setAddDateItem(colorItem.getDate());
        dbHelper.getColorRuntimeExceptionDao().update(colorItem);

    }

    public void setUpdateData(boolean mUpdateData) {
        this.mUpdateData = mUpdateData;
    }

    private int existsInputName(){
        String name;
        name = mSharedPreferences.getString(Constans.NAME_ITEM, "Name");
        dbHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        final RuntimeExceptionDao<ColorItem, Integer> colorDao = dbHelper.getColorRuntimeExceptionDao();
        List<ColorItem> result = colorDao.queryForAll();
        for (int i = 0; i <result.size() ; i++) {
            if(name.equals(result.get(i).getNameItem())){
                int idItem;
                idItem = result.get(i).getId();
                return idItem;
            }
        }
        return 0;
    }


}
