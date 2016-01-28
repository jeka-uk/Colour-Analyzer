package com.example.lenovo.colouranalyzer.db;




import android.os.Handler;

import com.example.lenovo.colouranalyzer.common.Constans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConnectToSQL {

    private Connection mConnection = null;
    private Handler handler = new Handler();

   public ArrayList <ColorItem> connectToSQL(String url, List<ColorItem> dataFromLocalDB){
        ArrayList resultSelectFromSql = new ArrayList();
        ArrayList <ColorItem> duplicateItemFromSql = new ArrayList<>();
            try {
                if(mConnection != null) {
                    mConnection.close();
                }
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionURL = "jdbc:jtds:sqlserver://" + url + ":"+ Constans.PORT_SQL + ";" + "databaseName=" + ";user=" + Constans.USER_SQL + ";password=" + Constans.PASSWORD_SQL + ";";

            mConnection = DriverManager.getConnection(connectionURL);
            System.out.println("Connect to SQL Server successfully");

            Statement statement = mConnection.createStatement();
            statement.executeUpdate(SqlQueryBuilder.CREATE_DB_SQL);
            System.out.println("Create db SQL Server successfully");

            statement.executeUpdate(SqlQueryBuilder.CREATE_TABLE_SQL);
            System.out.println("Create table SQL Server successfully");



            ResultSet rs = statement.executeQuery(SqlQueryBuilder.createRequestDuplicationInSql(dataFromLocalDB));
            while (rs.next()) {
                resultSelectFromSql.add(rs.getString(1));
            }

            PreparedStatement ps = mConnection.prepareStatement(SqlQueryBuilder.INSERT_DATA_TO_SQL);
            PreparedStatement psUpdate = mConnection.prepareStatement(SqlQueryBuilder.UPDATE_DATA_TO_SQL);
            for (int i = 0; i < dataFromLocalDB.size(); i++) {
                if(!resultSelectFromSql.contains(dataFromLocalDB.get(i).getNameItem())){
                    ps.setInt(1, i);
                    ps.setString(2, dataFromLocalDB.get(i).getAddDateItem());
                    ps.setString(3, dataFromLocalDB.get(i).getHexItem());
                    ps.setInt(4, dataFromLocalDB.get(i).getRgbItem());
                    ps.setString(5, dataFromLocalDB.get(i).getNameItem());
                    ps.setBytes(6, dataFromLocalDB.get(i).getImageItem());
                    ps.addBatch();
                    System.out.println("Added Item to SQL "+dataFromLocalDB.get(i).getNameItem());
                }else{
                    System.out.println("Duplicate Item " + dataFromLocalDB.get(i).getNameItem());
                    duplicateItemFromSql.add(dataFromLocalDB.get(i));
                    psUpdate.setInt(1, i);
                    psUpdate.setString(2, dataFromLocalDB.get(i).getAddDateItem());
                    psUpdate.setString(3, dataFromLocalDB.get(i).getHexItem());
                    psUpdate.setInt(4, dataFromLocalDB.get(i).getRgbItem());
                    psUpdate.setString(5, dataFromLocalDB.get(i).getNameItem());
                    psUpdate.setBytes(6, dataFromLocalDB.get(i).getImageItem());
                    psUpdate.setString(7, dataFromLocalDB.get(i).getNameItem());
                    psUpdate.addBatch();

                }
            }
            mConnection.setAutoCommit(true);
            ps.executeBatch();
            psUpdate.executeBatch();

            System.out.println("Insert data to SQL");
            ps.close();
            psUpdate.close();
            statement.close();
            mConnection.close();

                } catch (SQLException e) {
                           e.printStackTrace();

                } catch (ClassNotFoundException e) {
                          e.printStackTrace();
        }
        return duplicateItemFromSql;
    }


    public boolean testIpSQL(String url){
        Socket s = new Socket();
        try {
            s.connect(new InetSocketAddress(url , Integer.parseInt(Constans.PORT_SQL)), Constans.TIME_OUT);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("SQL Server not found ");
            return false;
        }
        System.out.println("SQL Server found ");
        return true;
    }
}