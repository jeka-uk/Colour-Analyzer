package com.example.lenovo.colouranalyzer.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.lenovo.colouranalyzer.R;
import com.example.lenovo.colouranalyzer.activities.CaptureActivityAnyOrientation;
import com.example.lenovo.colouranalyzer.common.Constans;
import com.example.lenovo.colouranalyzer.common.SetNameItem;
import com.example.lenovo.colouranalyzer.common.TransImageButton;
import com.example.lenovo.colouranalyzer.db.ColorItem;
import com.example.lenovo.colouranalyzer.db.DatabaseHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NameOfComposition extends Fragment {

    @Bind(R.id.name_item) EditText mNameItem;
    private SetNameItem mSetNameItem;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_name_composition, container, false);
        ButterKnife.bind(this, view);

        mSharedPreferences = getActivity().getSharedPreferences(Constans.PREFS_FILE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

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



    private void saveResult(EditText name){
        if(name.getText().length() != 0 && existsInputName(name.getText().toString()) == true && name.getText().length() < 20){
            mEditor.putString(Constans.NAME_ITEM, name.getText().toString());
            mEditor.commit();
            mSetNameItem.addName(name.getText().toString());
            if(getFragmentManager() != null)
                getFragmentManager().popBackStack();
        }else if(name.getText().length() > 20){
            informationSelectedUsers(getString(R.string.name_of_composition_activity_information_lench_name));
        }else{
            informationSelectedUsers(getString(R.string.name_of_composition_activity_information));
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
      //  getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

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


    private void informationSelectedUsers(String information) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(information)
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


    public void scanFromFragment() {
          IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
          integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
          integrator.setOrientationLocked(true);
          integrator.initiateScan();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {

            } else {
                mNameItem.setText(result.getContents());
            }
        }
    }

    @OnClick(R.id.name_composition_relative_layout)
    public void onCompasitionLayout(View view){
    }

    @OnClick(R.id.bnt_ok)
    public void onBntOk(View view){
        saveResult(mNameItem);
    }

    @OnClick(R.id.barcode_scanner)
    public void onBarcodeScanner(View view){
        scanFromFragment();
    }
}

