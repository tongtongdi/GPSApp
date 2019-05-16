package work.cisst.com.gpsapp.mapper;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import work.cisst.com.gpsapp.DB.DBOpenHelper;
import work.cisst.com.gpsapp.entity.User;

public class UserMapper {

    public List<User> getAllUser(){
        List<User> list = new ArrayList<>();
        Connection conn = DBOpenHelper.getConn();
            String sql = "select * from user";
        try {
            PreparedStatement pst =  conn.prepareStatement(sql);
        ResultSet rs = null;
            rs = pst.executeQuery();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            list.add(user);
        }
        DBOpenHelper.closeAll(conn,pst,rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public User getUserByName(String name) {
        Connection connection = DBOpenHelper.getConn();
        User user = new User();
        String sql = "select * from user where name = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setObject(1, name);
            ResultSet rs = null;
            rs = statement.executeQuery();
            while (rs.next()){
                Integer id = rs.getInt("id");
                String userName = rs.getString("name");
                String passWord = rs.getString("password");
                user.setId(id);
                user.setPassword(passWord);
                user.setName(userName);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBOpenHelper.closeAll(connection,null);
        }
        return user;
    }

    public void addUser(String name, String password) {
        Connection connection = DBOpenHelper.getConn();
        User user = new User();
        String sql = "insert into user(name,password) value(?,?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setObject(1, name);
            statement.setObject(2,password);
            int id = statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBOpenHelper.closeAll(connection,null);
        }
    }
}
