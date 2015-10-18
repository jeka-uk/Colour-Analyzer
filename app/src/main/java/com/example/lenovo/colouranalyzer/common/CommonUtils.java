package com.example.lenovo.colouranalyzer.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.lenovo.colouranalyzer.R;

/**
 * Created by Lenovo on 05.10.2015.
 */
public class CommonUtils {

    public static void startFragmentSlideHorizont(Fragment newFragment, int containerViewId, FragmentManager fragmentManager){
        FragmentTransaction frTra = fragmentManager.beginTransaction();
        frTra.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        frTra.replace(containerViewId, newFragment);
        //frTra.addToBackStack(null);
        frTra.commit();
    }

    public static void startFragmentSlideVertical(Fragment newFragment, int containerViewId, FragmentManager fragmentManager){
        FragmentTransaction frTra = fragmentManager.beginTransaction();
        frTra.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down);
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
}
