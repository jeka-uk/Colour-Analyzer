package com.example.lenovo.colouranalyzer.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.lenovo.colouranalyzer.R;
import com.example.lenovo.colouranalyzer.common.CommonUtils;
import com.example.lenovo.colouranalyzer.common.Constans;
import com.example.lenovo.colouranalyzer.common.TransImageButton;
import com.example.lenovo.colouranalyzer.db.ColorItem;
import com.example.lenovo.colouranalyzer.db.ConnectToSQL;
import com.example.lenovo.colouranalyzer.db.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;


import java.util.List;
public class SendDataToServer extends Fragment  {

    private RelativeLayout mCompositionlayout;
    private EditText mInputIpEditText;
    private TransImageButton mSingIn;
    private CheckBox mSaveIpCheckBox;
    private SharedPreferences sPref;
    private String mInputIp;
    private ConnectToSQL mConnectToSQL = new ConnectToSQL();
    private Handler h;
    private ResponseSqlFragment mResponseSqlFragment = new ResponseSqlFragment();
    private List<ColorItem> duplicateItemSql;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_data, container, false);

        mCompositionlayout = (RelativeLayout) view.findViewById(R.id.send_data_relative_layout);
        mCompositionlayout.setOnClickListener(onCompasitionLayout);
        mInputIpEditText = (EditText) view.findViewById(R.id.input_ip);
        mInputIpEditText.setOnKeyListener(onPressButton);
        mSingIn = (TransImageButton) view.findViewById(R.id.button_sing_in);
        mSingIn.setOnClickListener(onSingIn);
        mSaveIpCheckBox = (CheckBox) view.findViewById(R.id.checkbox_save_ip);
        mSaveIpCheckBox.setButtonDrawable(android.R.color.transparent);


        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case Constans.STATUS_CONNECTING_TO_SQL:
                        StartResponseSqlFragment(false, false, null);
                        break;
                    case Constans.STATUS_RESULT_FROM_SQL:
                        StartResponseSqlFragment(true, false, null);
                        break;
                    case Constans.STATUS_ALL_DATA_SENT_TO_SQL:
                        StartResponseSqlFragment(false, true, "task");
                        break;
                    case Constans.STATUS_SQL_SERVER_NOT_FOUND:
                        StartResponseSqlFragment(false, true, null);
                        break;
                }
            };
        };
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
                connectToSql(mInputIpEditText);
            }
        }
    };


    public void setmInputIp(String mInputIp) {
        this.mInputIp = mInputIp;
    }


    @Override
    public void onPause() {
        super.onPause();
        sPref = getActivity().getSharedPreferences(Constans.PREFS_FILE, getActivity().MODE_PRIVATE);
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
        sPref = getActivity().getSharedPreferences(Constans.PREFS_FILE, getActivity().MODE_PRIVATE);
        if(!sPref.equals(null))
            setmInputIp(sPref.getString(Constans.SAVE_IP_VALUE, null));
        mInputIpEditText.setText(sPref.getString(Constans.SAVE_IP_VALUE, null));
        mSaveIpCheckBox.setChecked(sPref.getBoolean(Constans.SAVE_IP_CHECK_BOX, false));
    }



    private void StartResponseSqlFragment(boolean showListView, boolean showInformation, String task){
        if(showListView ==false && showInformation ==false){
            CommonUtils.startFragmentSlideVerticalDownUpWithBackStack(mResponseSqlFragment, R.id.response_sql_fragment, getFragmentManager());
        }

        if(showListView) {
            ColorItem[] colorItems = new ColorItem[duplicateItemSql.size()];
            for (int i = 0; i < duplicateItemSql.size(); i++) {
                colorItems[i] = duplicateItemSql.get(i);
            }
            mResponseSqlFragment.informationDuplicateItem(colorItems);
        }

        if(showInformation){
            if("task".equals(task)){
                mResponseSqlFragment.informationSelectedUsers(getString(R.string.send_data_to_server_fragment_information_about_task));
            }else{
                mResponseSqlFragment.informationSelectedUsers(getString(R.string.send_data_to_server_fragment_information_about_found_sql));
            }
        }
        hideKeyboard();
    }


    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getActivity().getCurrentFocus();
        if (v != null)
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    private void connectToSql(EditText editTextIp){
        if(editTextIp.getText().length() != 0 && isNetworkAvailable(getActivity())){
            setmInputIp(editTextIp.getText().toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    h.sendEmptyMessage(Constans.STATUS_CONNECTING_TO_SQL);
                    if(mConnectToSQL.testIpSQL(mInputIp)){
                        duplicateItemSql = mConnectToSQL.connectToSQL(mInputIp, loadDataFromLocalDb());
                        if(duplicateItemSql != null && duplicateItemSql.size() > 0){
                            h.sendEmptyMessage(Constans.STATUS_RESULT_FROM_SQL);
                        }else{
                            h.sendEmptyMessage(Constans.STATUS_ALL_DATA_SENT_TO_SQL);
                        }
                    }else{
                        h.sendEmptyMessage(Constans.STATUS_SQL_SERVER_NOT_FOUND);
                    }
                }
            }).start();
        }
    }


    View.OnKeyListener onPressButton = (View.OnKeyListener) (dialog, keyCode, event) -> {
        if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
            connectToSql(mInputIpEditText);
            return true;
        }
        return false;
    };
}