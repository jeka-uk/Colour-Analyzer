package com.example.lenovo.colouranalyzer.db;


import com.example.lenovo.colouranalyzer.common.Constans;

public class SqlQueryBuilder {
    public static final String CREATE_DB_SQL = "if not exists(select * from sys.databases where name = '"+ Constans.NAME_DB_SQL+"')" +
            "create database "+ Constans.NAME_DB_SQL+"";

    public static final String CREATE_TABLE_SQL = "IF OBJECT_ID(N'"+ Constans.NAME_DB_SQL+".dbo."+ Constans.NAME_TABLE_SQL+"', N'U')IS NULL CREATE TABLE "+ Constans.NAME_DB_SQL+".dbo."+ Constans.NAME_TABLE_SQL+" (id int NOT NULL, add_date varchar(20) NOT NULL, name_hex varchar(20) NOT NULL, name_rgb int NOT NULL, name_item varchar(20) UNIQUE NOT NULL, image_item image)";

    public static final String SELECT_NAME_ITEM_FROM_SQL = "SELECT name_item from "+ Constans.NAME_DB_SQL+".dbo."+ Constans.NAME_TABLE_SQL+" ";

   // public static final String INSERT_DATA_TO_SQL = "INSERT INTO "+ Constans.NAME_DB_SQL+".dbo."+ Constans.NAME_TABLE_SQL+" (id, add_date, name_hex, name_rgb, name_item, image_item) VALUES (?, ?, ?, ?, ?, ?)";

    public static final String INSERT_DATA_TO_SQL = "INSERT INTO "+ Constans.NAME_DB_SQL+".dbo."+ Constans.NAME_TABLE_SQL+" (id, add_date, name_hex, name_rgb, name_item, image_item) VALUES (?, ?, ?, ?, ?, ?)";

}


