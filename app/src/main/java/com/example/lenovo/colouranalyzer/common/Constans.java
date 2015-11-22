package com.example.lenovo.colouranalyzer.common;


import android.os.Environment;

import java.io.File;

public class Constans {
    public static final String FOLDER_NAME = "Colour analyzer";
    public static final String NAME_FILE = "file";
    public static final File FILE_PATCH = new File(String.valueOf(Environment.getExternalStorageDirectory() + "/" + Constans.FOLDER_NAME + "/" + Constans.NAME_FILE + ".jpg"));

    public static final String NAME_ITEM = "name_item";
    public static final String COLOR_ANALYZER = "color_analyzer";
    public static final String RGB_VALUE = "rgb_value";
    public static final String NAME_DB_SQL = "Color";
    public static final String NAME_TABLE_SQL = "color_table";
}
