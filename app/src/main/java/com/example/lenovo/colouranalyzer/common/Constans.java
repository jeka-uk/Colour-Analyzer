package com.example.lenovo.colouranalyzer.common;


import android.os.Environment;

import java.io.File;

public class Constans {
    public static final String FOLDER_NAME = "Colour analyzer";
    public static final String NAME_FILE = "file";
    public static final File FILE_PATCH = new File(String.valueOf(Environment.getExternalStorageDirectory() + "/" + Constans.FOLDER_NAME + "/" + Constans.NAME_FILE + ".jpg"));

    public static final String NAME_ITEM = "name_item";
    public static final String PREFS_FILE = "color_analyzer";
    public static final String RGB_VALUE = "rgb_value";
    public static final String NAME_DB_SQL = "Color";
    public static final String NAME_TABLE_SQL = "color_table";
    public static final String PORT_SQL = "1433";
    public static final String NAME_LOCAL_DB= "color_item.db";

    public static final int TIME_OUT = 7000;
    public static final String USER_SQL = "android";
    public static final String PASSWORD_SQL = "1234";
    public static final int DATABASE_VERSION = 2;
    public static final String SAVE_IP_VALUE = "save_ip_value";
    public static final String SAVE_IP_CHECK_BOX = "save_ip_check_box";

    public static final int STATUS_CONNECTING_TO_SQL = 0;
    public static final int STATUS_RESULT_FROM_SQL = 1;
    public static final int STATUS_ALL_DATA_SENT_TO_SQL = 2;
    public static final int STATUS_SQL_SERVER_NOT_FOUND = 3;
}
