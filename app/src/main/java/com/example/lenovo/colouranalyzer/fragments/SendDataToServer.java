package com.example.lenovo.colouranalyzer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.lenovo.colouranalyzer.R;
import com.example.lenovo.colouranalyzer.common.Constans;
import com.example.lenovo.colouranalyzer.db.SqlQueryBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SendDataToServer extends Fragment {

    private RelativeLayout mCompositionlayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_data, container, false);

        mCompositionlayout = (RelativeLayout) view.findViewById(R.id.send_data_relative_layout);
        mCompositionlayout.setOnClickListener(onCompasitionLayout);

        new Thread(new Runnable() {
            @Override
            public void run() {
                insert("android", "1234", "192.168.1.4", "1433");
            }
        }).start();


        return view;
    }

    private void insert(String user, String pass, String url, String port) {

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String ConnectionURL = "jdbc:jtds:sqlserver://" + url + ":"+ port + ";" + "databaseName=" + ";user=" + user + ";password=" + pass + ";";

            Connection connection = DriverManager.getConnection(ConnectionURL);
            System.out.println("Connected.");
            Statement statement = connection.createStatement();
            statement.execute(SqlQueryBuilder.CREATE_DB_SQL);
            System.out.println("Create db.");
            statement.executeUpdate(SqlQueryBuilder.CREATE_TABLE_SQL);
            System.out.println("Create table.");
            statement.executeUpdate(SqlQueryBuilder.INSERT_DATA_SQL);
            System.out.println("Insert data.");



            statement.close();
            connection.close();

        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
            Log.e("ERRO number: ",String.valueOf(se.getErrorCode()));
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
    }

    View.OnClickListener onCompasitionLayout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };
}
