package com.example.lenovo.colouranalyzer.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


public class TransImageButton extends ImageView {

    OnClickListener onClickListener;
    private OnTouchListener onTouch = new OnTouchListener() {

        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                setAlpha(0.5f);

            } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                setAlpha(1f);
                if (onClickListener != null)
                    onClickListener.onClick(arg0);
            } else if (arg1.getAction() == MotionEvent.ACTION_CANCEL) {
                setAlpha(1f);
            }

            return true;
        }

    };

    public TransImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(onTouch);


    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.onClickListener = l;
    }

    public TransImageButton(Context context) {
        this(context, null);
    }
}
