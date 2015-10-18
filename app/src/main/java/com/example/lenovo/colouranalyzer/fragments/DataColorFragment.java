package com.example.lenovo.colouranalyzer.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.colouranalyzer.R;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

/**
 * Created by Lenovo on 06.10.2015.
 */
public class DataColorFragment extends Fragment {



    private TextView mRgbColor, mHexColor, mHsvColor;
    private ImageView mSampleColor;
    private RelativeLayout mdataLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_color, container, false);

     mdataLayout = (RelativeLayout) view.findViewById(R.id.data_layout);
     mRgbColor = (TextView) view.findViewById(R.id.rgb_data_text_view);
     mHexColor = (TextView) view.findViewById(R.id.hex_data_text_view);
     mHsvColor = (TextView) view.findViewById(R.id.hsv_data_text_view);
     mSampleColor = (ImageView) view.findViewById(R.id.image_color);

        Handler mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if(msg != null)
                setColor(msg.what);
                mdataLayout.setVisibility(View.VISIBLE);
            };
        };

       Drawable drawable = this.getResources().getDrawable(R.drawable.blogimage);
       Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

        new Thread(new Runnable() {
            public void run() {

               int r = getAverageColorRGB(bitmap)[0];
               int g = getAverageColorRGB(bitmap)[1];
               int b = getAverageColorRGB(bitmap)[2];
                mHandler.sendEmptyMessage(Color.rgb(r, g, b));

            }
        }).start();


        return view;
    }

    private void setColor(int RGB){

        mRgbColor.setText((Color.red(RGB)+","+Color.green(RGB)+","+Color.blue(RGB)).toString());
        mHexColor.setText(String.format( "#%02x%02x%02x", Color.red(RGB), Color.green(RGB), Color.blue(RGB)));
      //  mHsvColor.setText(String.valueOf(Color.RGBToHSV(Color.green(RGB), Color.green(RGB), Color.blue(RGB), )));
        mSampleColor.setBackgroundColor(Color.parseColor(String.format( "#%02x%02x%02x", Color.red(RGB), Color.green(RGB), Color.blue(RGB))));
    }


    public static int[] getAverageColorRGB(Bitmap bitmap) {
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

                /*Observable<Integer> rColor  = Observable.just(Color.red(pixelColor));
                rColor.subscribe(nameColor -> Log.d("Color", nameColor.toString()));*/


                r += Color.red(pixelColor);
                g += Color.green(pixelColor);
                b += Color.blue(pixelColor);
            }
        }
        r /= size;
        g /= size;
        b /= size;
        return new int[] { r, g, b  };
    }
}
