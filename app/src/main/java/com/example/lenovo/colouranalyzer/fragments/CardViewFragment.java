package com.example.lenovo.colouranalyzer.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.lenovo.colouranalyzer.R;
import com.example.lenovo.colouranalyzer.common.CommonUtils;
import com.example.lenovo.colouranalyzer.common.Constans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class CardViewFragment extends Fragment {

    private ImageView mImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_view, container, false);

        mImageView = (ImageView) view.findViewById(R.id.imageView);

        if (Constans.FILE_PATCH.exists()) {
            mImageView.setImageBitmap(setImage(Constans.FILE_PATCH));
        }
        return view;
    }

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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}