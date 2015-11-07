package com.example.lenovo.colouranalyzer.fragments;


import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.lenovo.colouranalyzer.R;
import com.example.lenovo.colouranalyzer.common.SetNameItem;

public class NameOfComposition extends Fragment {

    private RelativeLayout mCompositionlayout;
    private ImageView mBntOk;
    private EditText mNameItem;
    private SetNameItem mSetNameItem;
    private SharedPreferences sPref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_name_composition, container, false);

        mCompositionlayout = (RelativeLayout) view.findViewById(R.id.name_composition_relative_layout);
        mCompositionlayout.setOnClickListener(onCompasitionLayout);
        mBntOk = (ImageView) view.findViewById(R.id.bnt_ok);
        mBntOk.setOnClickListener(onBntOk);
        mNameItem = (EditText) view.findViewById(R.id.name_item);

        return view;
    }

    View.OnClickListener onCompasitionLayout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    View.OnClickListener onBntOk = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mNameItem.getText().length() != 0){
                mSetNameItem.addName(mNameItem.getText().toString());
                sPref = getActivity().getSharedPreferences("COLOR_ANALYZER", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString("NAME_ITEM", mNameItem.getText().toString());
                ed.commit();

                if(getFragmentManager() != null)
                    getFragmentManager().popBackStack();
            }else{

            }
        }
    };

    public void setmSetNameItem(SetNameItem mSetNameItem) {
        this.mSetNameItem = mSetNameItem;
    }
}
