package com.example.lenovo.colouranalyzer.db;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.SimpleDateFormat;
import java.util.Date;


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

    public String getDate(){
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

    public void setId(int id) {
        this.id = id;
    }

    public void setRgbItem(int rgbItem) {
        this.rgbItem = rgbItem;
    }

    public void setAddDateItem(String addDateItem) {
        this.addDateItem = addDateItem;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public void setHexItem(String hexItem) {
        this.hexItem = hexItem;
    }

    public void setImageItem(byte[] imageItem) {
        this.imageItem = imageItem;
    }
}