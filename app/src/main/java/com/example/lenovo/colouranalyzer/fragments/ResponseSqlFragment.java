package com.example.lenovo.colouranalyzer.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.lenovo.colouranalyzer.R;
import com.example.lenovo.colouranalyzer.adapters.DuplicateSqlLineAdapter;
import com.example.lenovo.colouranalyzer.common.CustomTextView;
import com.example.lenovo.colouranalyzer.common.OnSwipeTouchListener;
import com.example.lenovo.colouranalyzer.db.ColorItem;

import java.util.List;

public class ResponseSqlFragment extends Fragment {

    private List<ColorItem> mDataItem;
    private ProgressBar mProgressBar;
    private ListView  mListView;
    private DuplicateSqlLineAdapter mDuplicateSqlAdapter;
    private ImageButton mButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_response_sql, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.response_progress_barr);
        mListView = (ListView) view.findViewById(R.id.respons_listview);
        mButton = (ImageButton) view.findViewById(R.id.test);

        return view;
    }


    public void informationDuplicateItem(List<ColorItem> duplicateItemSql) {
        this.mDataItem = duplicateItemSql;
        View view = getActivity().getLayoutInflater().inflate(R.layout.header_layout,null);
        mListView.addHeaderView(view);

        mDuplicateSqlAdapter = new DuplicateSqlLineAdapter(getActivity(), mDataItem);
        mListView.setAdapter(mDuplicateSqlAdapter);
        mProgressBar.setVisibility(View.GONE);
    }


    public void informationSelectedUsers() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.response_sql_fragment_information_about_task)
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
