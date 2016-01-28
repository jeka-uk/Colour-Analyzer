package com.example.lenovo.colouranalyzer.activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.lenovo.colouranalyzer.R;
import com.example.lenovo.colouranalyzer.common.OnClickTransparent;
import com.example.lenovo.colouranalyzer.common.CommonUtils;
import com.example.lenovo.colouranalyzer.common.SetNameItem;
import com.example.lenovo.colouranalyzer.db.ColorItem;
import com.example.lenovo.colouranalyzer.db.DatabaseHelper;
import com.example.lenovo.colouranalyzer.fragments.CardViewFragment;
import com.example.lenovo.colouranalyzer.fragments.DataColorFragment;
import com.example.lenovo.colouranalyzer.fragments.NameOfComposition;
import com.example.lenovo.colouranalyzer.fragments.TransparentColorFragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.io.IOException;
import java.util.List;

import cc.trity.floatingactionbutton.FloatingActionButton;
import cc.trity.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends AppCompatActivity implements OnClickTransparent, SetNameItem{

    private Toolbar mToolbar;
    private FloatingActionButton mCamera, mGallery;
    private final  static int REQUEST_CAMERA_FOR_NEW_PHOTO = 3;
    private final static int REQUEST_GALLERY_FOR_NEW_PHOTO = 4;
    private FloatingActionsMenu mMainButtonMenu;
    private CardViewFragment mCdFragment = new CardViewFragment();
    private TransparentColorFragment mTrFragment = new TransparentColorFragment();
    private DataColorFragment mDtFragment = new DataColorFragment();
    private ImageView mSplashScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCamera = (FloatingActionButton) findViewById(R.id.camera);
        mCamera.setColorNormal(Color.WHITE);
        mCamera.setColorPressed(R.color.white_pressed_trasparent);
        mCamera.setOnClickListener(onCamera);
        mGallery = (FloatingActionButton) findViewById(R.id.gallery);
        mGallery.setColorNormal(Color.WHITE);
        mGallery.setColorPressed(R.color.white_pressed_trasparent);
        mGallery.setOnClickListener(onGallery);
        mMainButtonMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions_down);
        mMainButtonMenu.setOnFloatingActionsMenuUpdateListener(onTransparent);
        mSplashScreen = (ImageView) findViewById(R.id.splash_screen);
        mSplashScreen.setVisibility(View.VISIBLE);

        Runnable mRunableSplash = new Runnable() {
            @Override
            public void run() {
                mSplashScreen.setVisibility(View.GONE);
            }
        };

        new Handler().postDelayed(mRunableSplash, 2000);
        deleteAllDataFromDatabase();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CommonUtils.startFragmentSlideHorizont(mCdFragment, R.id.card_view_fragment, getSupportFragmentManager());
        CommonUtils.startFragmentSlideVerticalDownUp(mDtFragment, R.id.data_color_fragment, getSupportFragmentManager());
    }


    View.OnClickListener onCamera = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri mUri = CommonUtils.generateFileUri();
            if (mUri == null) {
                informationSelectedUsers();
                return;
            }
            Intent takePhotoFromCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePhotoFromCamera.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            startActivityForResult(takePhotoFromCamera, REQUEST_CAMERA_FOR_NEW_PHOTO);
        }
    };


    View.OnClickListener onGallery = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri mUri = CommonUtils.generateFileUri();
            if (mUri == null) {
                informationSelectedUsers();
                return;
            }
            Intent takePhotoFromGallery = new Intent(Intent.ACTION_PICK);
            takePhotoFromGallery.setType("image/*");
            startActivityForResult(takePhotoFromGallery, REQUEST_GALLERY_FOR_NEW_PHOTO);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CAMERA_FOR_NEW_PHOTO && resultCode == RESULT_OK){
            startNameOfCompasition();
            CommonUtils.startFragmentSlideHorizont(new CardViewFragment(), R.id.card_view_fragment, getSupportFragmentManager());
            DataColorFragment newFragment = new DataColorFragment();
            newFragment.setmNeedCalculate(true);
            CommonUtils.startFragmentSlideHorizont(newFragment, R.id.data_color_fragment, getSupportFragmentManager());
        }
        if(requestCode == REQUEST_GALLERY_FOR_NEW_PHOTO && resultCode == RESULT_OK){
            Handler mHandler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    if(msg != null)
                    CommonUtils.startFragmentSlideHorizont(new CardViewFragment(), R.id.card_view_fragment, getSupportFragmentManager());
                    DataColorFragment newFragment = new DataColorFragment();
                    newFragment.setmNeedCalculate(true);
                    CommonUtils.startFragmentSlideHorizont(newFragment, R.id.data_color_fragment, getSupportFragmentManager());
                };
            };
                new Thread(new Runnable() {
                    public void run() {
                        startNameOfCompasition();
                        CommonUtils.saveToInternalStorage(uriToBitmap(data.getData()));
                        mHandler.sendEmptyMessage(5);
                    }
                }).start();
        }
    }


    FloatingActionsMenu.OnFloatingActionsMenuUpdateListener onTransparent = new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
        @Override
        public void onMenuExpanded() {
            startTranspa();
        }

        @Override
        public void onMenuCollapsed() {
            getSupportFragmentManager().popBackStack();
        }
    };


    private void closeMainButtonMenu(){
        CommonUtils.closeFragmentTraspered(mDtFragment, mTrFragment, R.id.data_color_fragment, getSupportFragmentManager());
        mMainButtonMenu.collapse();
    }


    private void startTranspa(){
        TransparentColorFragment newFragment = new TransparentColorFragment();
        newFragment.setmCloseTranspare(this);
        CommonUtils.startFragmentTraspered(newFragment, R.id.transparent_color_fragment, getSupportFragmentManager());
    }


    private void startNameOfCompasition(){
        NameOfComposition newFragment = new NameOfComposition();
        newFragment.setmSetNameItem(this);
        CommonUtils.startFragmentSlideVerticalDownUpWithBackStack(newFragment, R.id.name_composition_layout, getSupportFragmentManager());
    }



    @Override
    protected void onPause() {
        super.onPause();
        closeMainButtonMenu();
    }


    private Bitmap uriToBitmap(Uri selectedFileUri) {
        Bitmap newBitmap = null;
        try {
            newBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedFileUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newBitmap;
    }


    @Override
    public void onClick() {
        mMainButtonMenu.collapse();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mMainButtonMenu.collapse();
    }


    @Override
    public void addName(String name) {
        DataColorFragment newFragment = new DataColorFragment();
        newFragment.setmNeedCalculate(true);
        newFragment.setmSaveData(true);
        CommonUtils.startFragmentSlideHorizont(newFragment, R.id.data_color_fragment, getSupportFragmentManager());
    }

    private void informationSelectedUsers() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.main_activity_information)
                .setCancelable(false)
                .setPositiveButton(R.string.button_information_available_sd, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).create().show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OpenHelperManager.releaseHelper();
    }


    private void deleteAllDataFromDatabase(){
        DatabaseHelper dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        final RuntimeExceptionDao<ColorItem, Integer> colorDao = dbHelper.getColorRuntimeExceptionDao();
        List<ColorItem> list = colorDao.queryForAll();
        colorDao.delete(list);
    }
}
