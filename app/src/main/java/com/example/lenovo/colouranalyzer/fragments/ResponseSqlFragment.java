package com.example.lenovo.colouranalyzer.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.lenovo.colouranalyzer.R;
import com.example.lenovo.colouranalyzer.adapters.DuplicateItemSqlAdapter;
import com.example.lenovo.colouranalyzer.db.ColorItem;

import java.util.List;

public class ResponseSqlFragment extends Fragment {

    private ProgressBar mProgressBar;
    private ListView  mListView;
    private ImageButton mButton;
    private RelativeLayout mMainLayout;
    private View mView;
    private AlertDialog.Builder builder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_response_sql, container, false);

        mMainLayout = (RelativeLayout) view. findViewById(R.id.main_relative_layout);
        mMainLayout.setOnClickListener(onResponseLayout);
        mProgressBar = (ProgressBar) view.findViewById(R.id.response_progress_barr);
        mListView = (ListView) view.findViewById(R.id.respons_listview);
        mButton = (ImageButton) view.findViewById(R.id.test);
        builder = new AlertDialog.Builder(getActivity());

        return view;
    }


    View.OnClickListener onResponseLayout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };


    public void informationDuplicateItem(ColorItem[] duplicateItemSql) {
            mView = getActivity().getLayoutInflater().inflate(R.layout.header_layout,null);
            mListView.addHeaderView(mView);

            DuplicateItemSqlAdapter mDuplicateSqlLineAdapterNew = new DuplicateItemSqlAdapter(getActivity(), duplicateItemSql);
            mListView.setAdapter(mDuplicateSqlLineAdapterNew);
            mProgressBar.setVisibility(View.GONE);
        }


    public void informationSelectedUsers(String titleInformation) {
        builder.setMessage(titleInformation)
                .setCancelable(false)
                .setPositiveButton(R.string.button_information_available_sd, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if(getFragmentManager() != null)
                            getFragmentManager().popBackStack();
                    }
                }).create().show();
        mProgressBar.setVisibility(View.GONE);
    }
}
