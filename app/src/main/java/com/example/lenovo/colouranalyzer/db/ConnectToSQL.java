package com.example.lenovo.colouranalyzer.db;




import com.example.lenovo.colouranalyzer.common.Constans;
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

    public void connectToSQL(String url, List<ColorItem> dataFromLocalDB){
        ArrayList resultSelectFromSql = new ArrayList();
        try {
            if(mConnection != null){
                mConnection.close();
            }
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionURL = "jdbc:jtds:sqlserver://" + url + ":"+ Constans.PORT_SQL + ";" + "databaseName=" + ";user=" + Constans.USER_SQL + ";password=" + Constans.PASSWORD_SQL + ";";
            System.out.println(DriverManager.getLoginTimeout());
            mConnection = DriverManager.getConnection(connectionURL);
            System.out.println("Connect to SQL Server successfully");

            Statement statement = mConnection.createStatement();
            statement.executeUpdate(SqlQueryBuilder.CREATE_DB_SQL);
            System.out.println("Create db SQL Server successfully");

            statement.executeUpdate(SqlQueryBuilder.CREATE_TABLE_SQL);
            System.out.println("Create table SQL Server successfully");

            ResultSet rs = statement.executeQuery(SqlQueryBuilder.SELECT_NAME_ITEM_FROM_SQL);
            while (rs.next()) {
                resultSelectFromSql.add(rs.getString(1));
            }

            PreparedStatement ps = mConnection.prepareStatement(SqlQueryBuilder.INSERT_DATA_TO_SQL);
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
                    System.out.println("Duplicate Item "+dataFromLocalDB.get(i).getNameItem());
                }
            }
            mConnection.setAutoCommit(true);
            ps.executeBatch();

            System.out.println("Insert data to SQL");
            ps.close();
            statement.close();
            mConnection.close();

                } catch (SQLException e) {
                           e.printStackTrace();
                } catch (ClassNotFoundException e) {
                          e.printStackTrace();
        }
    }
}