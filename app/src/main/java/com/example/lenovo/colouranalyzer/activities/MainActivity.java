package com.example.lenovo.colouranalyzer.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.lenovo.colouranalyzer.R;
import com.example.lenovo.colouranalyzer.common.CloseTransparentClickInterface;
import com.example.lenovo.colouranalyzer.common.CommonUtils;
import com.example.lenovo.colouranalyzer.common.Constans;
import com.example.lenovo.colouranalyzer.common.SetNameItem;
import com.example.lenovo.colouranalyzer.fragments.CardViewFragment;
import com.example.lenovo.colouranalyzer.fragments.DataColorFragment;
import com.example.lenovo.colouranalyzer.fragments.NameOfComposition;
import com.example.lenovo.colouranalyzer.fragments.TransparentColorFragment;

import java.io.IOException;

import cc.trity.floatingactionbutton.FloatingActionButton;
import cc.trity.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends AppCompatActivity implements CloseTransparentClickInterface, SetNameItem {

    private Toolbar mToolbar;
    private FloatingActionButton mCamera, mGallery;
    private final  static int REQUEST_CAMERA_FOR_NEW_PHOTO = 0;
    private final static int REQUEST_GALLERY_FOR_NEW_PHOTO = 2;
    private FloatingActionsMenu mMainButtonMenu;
    private CardViewFragment mCdFragment = new CardViewFragment();
    private TransparentColorFragment mTrFragment = new TransparentColorFragment();
    private DataColorFragment mDtFragment = new DataColorFragment();
    private Context mContext = this;

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

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CommonUtils.startFragmentSlideHorizont(mCdFragment, R.id.card_view_layout, getSupportFragmentManager());
        CommonUtils.startFragmentSlideVerticalDownUp(mDtFragment, R.id.data_color_layout, getSupportFragmentManager());
    }

    View.OnClickListener onCamera = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Uri mUri = CommonUtils.generateFileUri();
            if (mUri == null) {
                Toast.makeText(mContext, R.string.main_activity_information, Toast.LENGTH_LONG).show();
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
                Toast.makeText(mContext, R.string.main_activity_information, Toast.LENGTH_LONG).show();
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

            //CommonUtils.startFragmentSlideVerticalDownUpWithBakStak(new NameOfComposition(), R.id.name_composition_layout, getSupportFragmentManager());
            startNameOfCompasition();
            CommonUtils.startFragmentSlideHorizont(new CardViewFragment(), R.id.card_view_layout, getSupportFragmentManager());
            DataColorFragment newFragment = new DataColorFragment();
            newFragment.setmNeedCalculate(true);
            CommonUtils.startFragmentSlideHorizont(newFragment, R.id.data_color_layout, getSupportFragmentManager());
        }

        if(requestCode == REQUEST_GALLERY_FOR_NEW_PHOTO && resultCode == RESULT_OK){

            Handler mHandler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    if(msg != null)
                    CommonUtils.startFragmentSlideHorizont(new CardViewFragment(), R.id.card_view_layout, getSupportFragmentManager());
                    DataColorFragment newFragment = new DataColorFragment();
                    newFragment.setmNeedCalculate(true);
                    CommonUtils.startFragmentSlideHorizont(newFragment, R.id.data_color_layout, getSupportFragmentManager());
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
        CommonUtils.closeFragmentTraspered(mDtFragment, mTrFragment, R.id.data_color_layout, getSupportFragmentManager());
        mMainButtonMenu.collapse();
    }

    private void startTranspa(){
        TransparentColorFragment newFragment = new TransparentColorFragment();
        newFragment.setmCloseTranspare(this);
        CommonUtils.startFragmentTraspered(newFragment, R.id.transparent_color_layout, getSupportFragmentManager());
    }

    private void startNameOfCompasition(){
        NameOfComposition newFragment = new NameOfComposition();
        newFragment.setmSetNameItem(this);
        CommonUtils.startFragmentSlideVerticalDownUpWithBakStak(newFragment, R.id.name_composition_layout, getSupportFragmentManager());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        CommonUtils.startFragmentSlideHorizont(newFragment, R.id.data_color_layout, getSupportFragmentManager());
    }
}
