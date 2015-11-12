package com.example.lenovo.colouranalyzer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "color_item.db";
    private static final int DATABASE_VERSION = 2;


    private RuntimeExceptionDao colorItemRuntimeDAO = null;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ColorItem.class);
        } catch (SQLException e){
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {
            TableUtils.dropTable(connectionSource, ColorItem.class, true);
            onCreate(sqLiteDatabase,connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
        }

    }

    public RuntimeExceptionDao getStudRuntimeExceptionDao(){
        if(colorItemRuntimeDAO == null){
            colorItemRuntimeDAO = getRuntimeExceptionDao(ColorItem.class);
        }
        return colorItemRuntimeDAO;

    }

}