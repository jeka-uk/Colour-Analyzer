package com.example.lenovo.colouranalyzer.common;


import android.os.Environment;

import java.io.File;

public class Constans {
    public static final String FOLDER_NAME = "Colour analyzer";
    public static final String NAME_FILE = "file";
    public static final File FILE_PATCH = new File(String.valueOf(Environment.getExternalStorageDirectory() + "/" + Constans.FOLDER_NAME + "/" + Constans.NAME_FILE + ".jpg"));
}
