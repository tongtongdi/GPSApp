package work.cisst.com.gpsapp.DB;



import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBOpenHelper {

    private static String driver = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://192.168.66.2:3306/gps?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
    private static String user = "root";
    private static String password = "admin";
    //连接数据库
    public static Connection getConn() {
        Connection conn = null;
        try {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("连接成功！！！！！");
        } catch (ClassNotFoundException  e) {
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return conn;
    }

    //关闭数据
    public static void closeAll(Connection conn, Statement ps) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //关闭数据库
    public static void closeAll(Connection conn, Statement ps, ResultSet resultSet) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(ps != null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
