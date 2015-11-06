package com.example.lenovo.colouranalyzer.common;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.WindowManager;

import com.example.lenovo.colouranalyzer.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;


public class CommonUtils {

    public static void startFragmentSlideHorizont(Fragment newFragment, int containerViewId, FragmentManager fragmentManager){
        FragmentTransaction frTra = fragmentManager.beginTransaction();
        frTra.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        frTra.replace(containerViewId, newFragment);
        //frTra.addToBackStack(null);
        frTra.commit();
    }

    public static void startFragmentSlideVerticalDownUp(Fragment newFragment, int containerViewId, FragmentManager fragmentManager){
        FragmentTransaction frTra = fragmentManager.beginTransaction();
        frTra.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down);
        frTra.replace(containerViewId, newFragment);
        //frTra.addToBackStack(null);
        frTra.commit();
    }

    public static void startFragmentTraspered(Fragment newFragment, int containerViewId, FragmentManager fragmentManager){
        FragmentTransaction frTra = fragmentManager.beginTransaction();
        //frTra.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_in_up);
        frTra.replace(containerViewId, newFragment);
        frTra.addToBackStack("transpare");
        frTra.commit();
    }

    public static void closeFragmentTraspered(Fragment newFragment, Fragment oldFragment, int containerViewId, FragmentManager fragmentManager){
        FragmentTransaction frTra = fragmentManager.beginTransaction();
       // frTra.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down);
        frTra.remove(oldFragment);
        frTra.replace(containerViewId, newFragment);
        //frTra.addToBackStack(null);
        frTra.commit();
    }

    public static void startFragmentRefresh(Fragment newFragment, int containerViewId, FragmentManager fragmentManager, boolean transparent){
        FragmentTransaction frTra = fragmentManager.beginTransaction();
        frTra.replace(containerViewId, newFragment);
        //frTra.addToBackStack(null);
        frTra.commit();
    }

    public static int getAverageColorRGB(Bitmap bitmap) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        int size = width * height;
        int pixelColor;
        int r, g, b;
        r = g = b = 0;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                pixelColor = bitmap.getPixel(x, y);
                if (pixelColor == 0) {
                    size--;
                    continue;
                }

                r += Color.red(pixelColor);
                g += Color.green(pixelColor);
                b += Color.blue(pixelColor);
            }
        }
        r /= size;
        g /= size;
        b /= size;
        return Color.rgb(r,g,b);
    }

    public static String getRgbToHex(int color){
        return String.format( "#%02x%02x%02x", Color.red(color), Color.green(color), Color.blue(color));
    }

    public static String getRgbToString(int color){
        return (Color.red(color)+"."+Color.green(color)+"."+Color.blue(color)).toString();
    }

    public static String getRgbToHsv(int color){
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return (Math.round(hsv[0]) + "." + Math.round(hsv[1]*100) + "." + Math.round(hsv[2]*100)).toString();
    }


    public static String getRgbToHsl(int rgb) {
        DecimalFormat numberFormat = new DecimalFormat("#0.00");
        float[] hsl = new float[3];

        float r = ((0x00ff0000 & rgb) >> 16) / 255.f;
        float g = ((0x0000ff00 & rgb) >> 8) / 255.f;
        float b = ((0x000000ff & rgb)) / 255.f;
        float max = Math.max(Math.max(r, g), b);
        float min = Math.min(Math.min(r, g), b);
        float c = max - min;

        float h_ = 0.f;
        if (c == 0) {
            h_ = 0;
        } else if (max == r) {
            h_ = (float)(g-b) / c;
            if (h_ < 0) h_ += 6.f;
        } else if (max == g) {
            h_ = (float)(b-r) / c + 2.f;
        } else if (max == b) {
            h_ = (float)(r-g) / c + 4.f;
        }
        float h = 60.f * h_;

        float l = (max + min) * 0.5f;

        float s;
        if (c == 0) {
            s = 0.f;
        } else {
            s = c / (1 - Math.abs(2.f * l - 1.f));
        }
        hsl[0] = h;
        hsl[1] = s;
        hsl[2] = l;

        return String.format(numberFormat.format(h/360) + ". " + numberFormat.format(s) + ". " + numberFormat.format(l)).toString();
    }

    public static Uri generateFileUri() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return null;

        File path = new File (Environment.getExternalStorageDirectory(), Constans.FOLDER_NAME);
        if (! path.exists()){
            if (! path.mkdirs()){
                return null;
            }
        }

        File newFile = new File(path.getPath() + File.separator + Constans.NAME_FILE + ".jpg");
        return Uri.fromFile(newFile);
    }

    public static Uri saveToInternalStorage(Bitmap bitmapImage){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return null;

        File path = new File (Environment.getExternalStorageDirectory(), Constans.FOLDER_NAME);
        if (! path.exists()){
            if (! path.mkdirs()){
                return null;
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(Constans.FILE_PATCH);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 75, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
