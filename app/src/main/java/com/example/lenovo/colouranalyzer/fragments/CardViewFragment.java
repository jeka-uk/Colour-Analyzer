package com.example.lenovo.colouranalyzer.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.lenovo.colouranalyzer.R;
import com.example.lenovo.colouranalyzer.common.CommonUtils;
import com.example.lenovo.colouranalyzer.common.Constans;
import com.example.lenovo.colouranalyzer.common.SetNameItem;
import com.soundcloud.android.crop.Crop;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardViewFragment extends Fragment implements SetNameItem {

    @Bind(R.id.imageView) ImageView mImageView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_view, container, false);
        ButterKnife.bind(this, view);


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

        int imageWidth = options.outWidth;
        int cooficient = imageWidth/getWindowWidth() + 1;

        options.inSampleSize = cooficient;
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


    @OnClick(R.id.imageView)
    public void startCropFra(View view){
        Crop.of(Uri.fromFile(Constans.FILE_PATCH), Uri.fromFile(Constans.FILE_PATCH)).asSquare().start(getActivity(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Crop.REQUEST_CROP && data != null) {
            startNameOfCompasition();
            mImageView.setImageBitmap(setImage(Constans.FILE_PATCH));
        }
    }

    private void startNameOfCompasition() {
        NameOfComposition newFragment = new NameOfComposition();
        newFragment.setmSetNameItem(this);
        CommonUtils.startFragmentSlideVerticalDownUpWithBackStack(newFragment, R.id.name_composition_layout, getFragmentManager());
    }

    @Override
    public void addName(String name) {
        DataColorFragment newFragment = new DataColorFragment();
        newFragment.setmNeedCalculate(true);
        newFragment.setmSaveData(true);
        CommonUtils.startFragmentSlideHorizont(newFragment, R.id.data_color_fragment, getFragmentManager());
    }


    private int getWindowWidth(){
        int mWindowWidth;
        Point size = new Point();
        WindowManager w = getActivity().getWindowManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            w.getDefaultDisplay().getSize(size);
            mWindowWidth = size.x;
        } else {
            Display d = w.getDefaultDisplay();
            mWindowWidth = d.getWidth();
        }
        return mWindowWidth;
    }
}