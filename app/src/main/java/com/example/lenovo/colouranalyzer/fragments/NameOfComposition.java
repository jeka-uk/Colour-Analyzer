package com.example.lenovo.colouranalyzer.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.lenovo.colouranalyzer.R;
import com.example.lenovo.colouranalyzer.common.Constans;
import com.example.lenovo.colouranalyzer.common.SetNameItem;
import com.example.lenovo.colouranalyzer.common.TransImageButton;
import com.example.lenovo.colouranalyzer.db.ColorItem;
import com.example.lenovo.colouranalyzer.db.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;

public class NameOfComposition extends Fragment {

    private RelativeLayout mCompositionlayout;
    private TransImageButton mBntOk;
    private EditText mNameItem;
    private SetNameItem mSetNameItem;
    private SharedPreferences sPref;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_name_composition, container, false);

        mCompositionlayout = (RelativeLayout) view.findViewById(R.id.name_composition_relative_layout);
        mCompositionlayout.setOnClickListener(onCompasitionLayout);
        mBntOk = (TransImageButton) view.findViewById(R.id.bnt_ok);
        mBntOk.setOnClickListener(onBntOk);
        mNameItem = (EditText) view.findViewById(R.id.name_item);
        mNameItem.setOnKeyListener(onPressButton);

        return view;
    }


    View.OnKeyListener onPressButton = (View.OnKeyListener) (dialog, keyCode, event) -> {
        if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
            saveResult(mNameItem);
            return true;
        }
        return false;
    };


    View.OnClickListener onCompasitionLayout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };


    View.OnClickListener onBntOk = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveResult(mNameItem);
        }
    };


    private void saveResult(EditText name){
        if(name.getText().length() != 0 && existsInputName(name.getText().toString()) == true){
            sPref = getActivity().getSharedPreferences(Constans.COLOR_ANALYZER, getActivity().MODE_PRIVATE);
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString(Constans.NAME_ITEM, name.getText().toString());
            ed.commit();
            mSetNameItem.addName(name.getText().toString());

            if(getFragmentManager() != null)
                getFragmentManager().popBackStack();
        }else{
            informationSelectedUsers();
            mNameItem.setText("");
        }
    }


    public void setmSetNameItem(SetNameItem mSetNameItem) {
        this.mSetNameItem = mSetNameItem;
    }

    @Override
    public void onResume() {
        super.onResume();
        mNameItem.requestFocus();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }


    @Override
    public void onPause() {
        super.onPause();
        mNameItem.clearFocus();
        hideKeyboard();
    }

    private boolean existsInputName(String name){
        boolean existsName = true;
        dbHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        final RuntimeExceptionDao<ColorItem, Integer> colorDao = dbHelper.getColorRuntimeExceptionDao();
        List<ColorItem> result = colorDao.queryForAll();
        for (int i = 0; i <result.size() ; i++) {
                if(name.equals(result.get(i).getNameItem())){
                    existsName = false;
                }
            }
        return existsName;
    }


    private void informationSelectedUsers() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.name_of_composition_activity_information)
                .setCancelable(false)
                .setPositiveButton(R.string.button_information_available_sd, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).create().show();
    }


    @Override
    public void onStart() {
        super.onStart();
        mNameItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    v.setBackgroundResource(R.drawable.focus_border_style);
                }else{
                    v.setBackgroundResource(R.drawable.lost_focus_style);
                }
            }
        });
    }
}

