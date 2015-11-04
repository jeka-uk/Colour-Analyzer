package com.example.lenovo.colouranalyzer.activities;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.lenovo.colouranalyzer.R;
import com.example.lenovo.colouranalyzer.common.CommonUtils;
import com.example.lenovo.colouranalyzer.common.Constans;
import com.example.lenovo.colouranalyzer.fragments.CardViewFragment;
import com.example.lenovo.colouranalyzer.fragments.DataColorFragment;
import com.example.lenovo.colouranalyzer.fragments.TrasperedColorFragment;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cc.trity.floatingactionbutton.FloatingActionButton;
import cc.trity.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private FloatingActionButton mCamera, mGallery;
    private final  static int REQUEST_CAMERA_FOR_NEW_PHOTO = 0;
    private final static int REQUEST_GALLERY_FOR_NEW_PHOTO = 2;
    private FloatingActionsMenu mMainButtonMenu;
    private CardViewFragment mCdFragment = new CardViewFragment();
    private TrasperedColorFragment mTrFragment = new TrasperedColorFragment();
    private DataColorFragment mDtFragment = new DataColorFragment();
    private Context mContext = this;
    private Uri mUri = CommonUtils.generateFileUri();

    Bitmap bmp1, bmp2;
    Canvas canvas;
    Paint paint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCamera = (FloatingActionButton) findViewById(R.id.camera);
        mCamera.setOnClickListener(onCamera);
        mGallery = (FloatingActionButton) findViewById(R.id.gallery);
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

            CommonUtils.startFragmentSlideHorizont(new CardViewFragment(), R.id.card_view_layout, getSupportFragmentManager());

            DataColorFragment newFragment = new DataColorFragment();
            newFragment.setmNeedCalculate(true);
            CommonUtils.startFragmentSlideHorizont(newFragment, R.id.data_color_layout, getSupportFragmentManager());
        }

        if(requestCode == REQUEST_GALLERY_FOR_NEW_PHOTO && resultCode == RESULT_OK){

            CommonUtils.saveToInternalStorage(uriToBitmap(data.getData()));

            CommonUtils.startFragmentSlideHorizont(new CardViewFragment(), R.id.card_view_layout, getSupportFragmentManager());
            DataColorFragment newFragment = new DataColorFragment();
            newFragment.setmNeedCalculate(true);
            CommonUtils.startFragmentSlideHorizont(newFragment, R.id.data_color_layout, getSupportFragmentManager());
        }
    }

    FloatingActionsMenu.OnFloatingActionsMenuUpdateListener onTransparent = new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
        @Override
        public void onMenuExpanded() {
            CommonUtils.startFragmentTraspered(mTrFragment, R.id.transparent_color_layout, getSupportFragmentManager());
        }

        @Override
        public void onMenuCollapsed() {
            CommonUtils.closeFragmentTraspered(mDtFragment, mTrFragment, R.id.data_color_layout, getSupportFragmentManager());
        }
    };

    private void closeMainButtonMenu(){
        CommonUtils.closeFragmentTraspered(mDtFragment, mTrFragment, R.id.data_color_layout, getSupportFragmentManager());
        mMainButtonMenu.collapse();
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
        Bitmap image = null;
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
            options.inSampleSize = 2;
            options.inJustDecodeBounds = false;
            image = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

}
