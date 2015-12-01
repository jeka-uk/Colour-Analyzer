package com.example.lenovo.colouranalyzer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lenovo.colouranalyzer.common.Constans;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "color_item.db";
   // private static final int DATABASE_VERSION = 2;


    private Dao<ColorItem, Integer> colorItemDao = null;
    private RuntimeExceptionDao<ColorItem, Integer> colorItemRuntimeDAO = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, Constans.DATABASE_VERSION);
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

    public Dao<ColorItem, Integer> getDao() throws SQLException {
        if (colorItemDao == null) {
            colorItemDao = getDao(ColorItem.class);
        }
        return colorItemDao;
    }


    public RuntimeExceptionDao<ColorItem, Integer> getColorRuntimeExceptionDao(){
        if(colorItemRuntimeDAO == null){
            colorItemRuntimeDAO = getRuntimeExceptionDao(ColorItem.class);
        }
        return colorItemRuntimeDAO;

    }

    @Override
    public void close() {
        super.close();
        colorItemRuntimeDAO = null;
      //  colorItemDao = null;

    }
}