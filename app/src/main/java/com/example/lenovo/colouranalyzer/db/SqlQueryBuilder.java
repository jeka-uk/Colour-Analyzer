package com.example.lenovo.colouranalyzer.db;


import com.example.lenovo.colouranalyzer.common.Constans;

public class SqlQueryBuilder {

    public static final String CREATE_DB_SQL = "if not exists(select * from sys.databases where name = '"+ Constans.NAME_DB_SQL+"')" +
            "create database "+ Constans.NAME_DB_SQL+"";

    public static final String CREATE_TABLE_SQL = "IF OBJECT_ID(N'"+ Constans.NAME_DB_SQL+".dbo."+ Constans.NAME_TABLE_SQL+"', N'U')IS NULL CREATE TABLE "+ Constans.NAME_DB_SQL+".dbo."+ Constans.NAME_TABLE_SQL+" (id int NOT NULL, add_date date, name_hex char, name_rgb int, name_item varchar(20), image_item binary, PRIMARY KEY (id))";

    public static final String INSERT_DATA_SQL = "INSERT INTO "+ Constans.NAME_DB_SQL+".dbo."+ Constans.NAME_TABLE_SQL+" (id, add_date, name_hex, name_rgb, name_item, image_item) VALUES (3, null, null, null, N'yeuhdj', null)";
}
