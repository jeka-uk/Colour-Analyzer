package com.example.lenovo.colouranalyzer.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.colouranalyzer.R;
import com.example.lenovo.colouranalyzer.common.CommonUtils;
import com.example.lenovo.colouranalyzer.common.Constans;

import java.io.File;


public class DataColorFragment extends Fragment {



    private TextView mRgbColor, mHexColor, mHsvColor, mHslColor, mNameItem;
    private ImageView mSampleColor;
    private RelativeLayout mdataLayout;
    private ImageView mSendResultToServer;
    private boolean mNeedCalculate = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_color, container, false);

     mdataLayout = (RelativeLayout) view.findViewById(R.id.data_layout);
     mRgbColor = (TextView) view.findViewById(R.id.rgb_data_text_view);
     mHexColor = (TextView) view.findViewById(R.id.hex_data_text_view);
     mHsvColor = (TextView) view.findViewById(R.id.hsv_data_text_view);
     mHslColor = (TextView) view.findViewById(R.id.hsl_data_text_view);
     mNameItem = (TextView) view.findViewById(R.id.name_item);
     mSampleColor = (ImageView) view.findViewById(R.id.image_color);
     mSendResultToServer = (ImageView) view.findViewById(R.id.send_result);
     mSendResultToServer.setOnClickListener(onSengResultToServer);

        if(mNeedCalculate){
            calculateColor();
        }else{
            mdataLayout.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public void setmNeedCalculate(boolean mNeedCalculate) {
        this.mNeedCalculate = mNeedCalculate;
    }

    private void calculateColor(){
        Handler mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if(msg != null)
                    setColor(msg.what);
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
        mHexColor.setText(CommonUtils.getRgbToHex(RGB));
        mHsvColor.setText(CommonUtils.getRgbToHsv(RGB));
        mSampleColor.setBackgroundColor(Color.parseColor(String.format(CommonUtils.getRgbToHex(RGB))));
        mHslColor.setText(CommonUtils.getRgbToHsl(RGB));
    }

    View.OnClickListener onSengResultToServer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private Bitmap setImage(File patch) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeFile(String.valueOf(patch), options);
        options.inSampleSize = 4;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(String.valueOf(patch), options);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
