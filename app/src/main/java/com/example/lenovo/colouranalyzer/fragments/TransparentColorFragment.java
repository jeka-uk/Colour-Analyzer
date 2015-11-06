package com.example.lenovo.colouranalyzer.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.lenovo.colouranalyzer.R;
import com.example.lenovo.colouranalyzer.common.CommonUtils;
import com.example.lenovo.colouranalyzer.common.FragmentCloseClickInterface;


public class TransparentColorFragment extends Fragment {

    private RelativeLayout mTransparentLayout;
    private FragmentCloseClickInterface mCloseTranspare;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_color_transparent, container, false);

        mTransparentLayout = (RelativeLayout) view.findViewById(R.id.transparent_layout);
        mTransparentLayout.setOnClickListener(onCloseFragment);

        return view;
    }

    public void setmCloseTranspare(FragmentCloseClickInterface mCloseTranspare) {
        this.mCloseTranspare = mCloseTranspare;
    }

    View.OnClickListener onCloseFragment = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCloseTranspare.onClick();
            getFragmentManager().popBackStack();

        }
    };
}
