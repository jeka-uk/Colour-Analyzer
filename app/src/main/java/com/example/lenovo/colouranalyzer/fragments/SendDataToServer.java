package com.example.lenovo.colouranalyzer.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.lenovo.colouranalyzer.R;
import com.example.lenovo.colouranalyzer.common.Constans;
import com.example.lenovo.colouranalyzer.common.TransImageButton;
import com.example.lenovo.colouranalyzer.db.ColorItem;
import com.example.lenovo.colouranalyzer.db.ConnectToSQL;
import com.example.lenovo.colouranalyzer.db.DatabaseHelper;
import com.example.lenovo.colouranalyzer.db.SqlQueryBuilder;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SendDataToServer extends Fragment {

    private RelativeLayout mCompositionlayout;
    private EditText mInputIpEditText;
    private TransImageButton mSingIn;
    private CheckBox mSaveIpCheckBox;
    private SharedPreferences sPref;
    private String mInputIp;
    private ConnectToSQL mConnectToSQL = new ConnectToSQL();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_data, container, false);

        mCompositionlayout = (RelativeLayout) view.findViewById(R.id.send_data_relative_layout);
        mCompositionlayout.setOnClickListener(onCompasitionLayout);
        mInputIpEditText = (EditText) view.findViewById(R.id.input_ip);
        mSingIn = (TransImageButton) view.findViewById(R.id.button_sing_in);
        mSingIn.setOnClickListener(onSingIn);
        mSaveIpCheckBox = (CheckBox) view.findViewById(R.id.checkbox_save_ip);
        mSaveIpCheckBox.setButtonDrawable(android.R.color.transparent);

        return view;
    }


    View.OnClickListener onCompasitionLayout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };


    private List<ColorItem> loadDataFromLocalDb(){
        DatabaseHelper dbHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        final RuntimeExceptionDao<ColorItem, Integer> colorDao = dbHelper.getColorRuntimeExceptionDao();
        return colorDao.queryForAll();
    }


    @Override
    public void onStart() {
        super.onStart();
        mInputIpEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackgroundResource(R.drawable.focus_border_style);
                } else {
                    v.setBackgroundResource(R.drawable.lost_focus_style);
                }
            }
        });
        if(!isNetworkAvailable(getActivity())){
            Log.i(getClass().getName(), "Network off");
            informationConnection();
        }
    }


    public boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    private void informationConnection(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_information_network)
                .setMessage(R.string.message_information_network)
                .setCancelable(false)
                .setPositiveButton(R.string.button_ok_information_network, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).create().show();
    }


    View.OnClickListener onSingIn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mInputIpEditText.getText().length() != 0 && isNetworkAvailable(getActivity())){
                setmInputIp(mInputIpEditText.getText().toString());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mConnectToSQL.connectToSQL(mInputIp, loadDataFromLocalDb());
                    }
                }).start();
            }
        }
    };


    public void setmInputIp(String mInputIp) {
        this.mInputIp = mInputIp;
    }


    @Override
    public void onPause() {
        super.onPause();
        sPref = getActivity().getSharedPreferences(Constans.COLOR_ANALYZER, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        if (mSaveIpCheckBox.isChecked()) {
            ed.putString(Constans.SAVE_IP_VALUE, mInputIp);
            ed.putBoolean(Constans.SAVE_IP_CHECK_BOX, true);
        } else {
            ed.putString(Constans.SAVE_IP_VALUE, null);
            ed.putBoolean(Constans.SAVE_IP_CHECK_BOX, false);
        }
        ed.commit();
    }


    @Override
    public void onResume() {
        super.onResume();
        sPref = getActivity().getSharedPreferences(Constans.COLOR_ANALYZER, getActivity().MODE_PRIVATE);
        if(!sPref.equals(null))
            setmInputIp(sPref.getString(Constans.SAVE_IP_VALUE, null));
            mInputIpEditText.setText(sPref.getString(Constans.SAVE_IP_VALUE, null));
            mSaveIpCheckBox.setChecked(sPref.getBoolean(Constans.SAVE_IP_CHECK_BOX, false));
    }
}
