package com.example.lenovo.colouranalyzer.db;

import android.provider.ContactsContract;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;


@DatabaseTable(tableName = "colorItem")
public class ColorItem {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String addDateItem;
    @DatabaseField(uniqueCombo = true)
    private String nameItem;
    @DatabaseField
    private int rgbItem;
    @DatabaseField
    private String hexItem;
    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] imageItem;

    public ColorItem(){ }

    public ColorItem(String nameItem, int rgbItem, String hexItem, byte[] imageItem) {
        this.nameItem = nameItem;
        this.rgbItem = rgbItem;
        this.hexItem = hexItem;
        this.imageItem = imageItem;
        this.addDateItem = getDate();
    }

    private String getDate(){
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return dataFormat.format(new Date());
    }

    public int getId() {
        return id;
    }

    public String getAddDateItem() {
        return addDateItem;
    }

    public String getNameItem() {
        return nameItem;
    }

    public int getRgbItem() {
        return rgbItem;
    }

    public String getHexItem() {
        return hexItem;
    }

    public byte[] getImageItem() {
        return imageItem;
    }
}