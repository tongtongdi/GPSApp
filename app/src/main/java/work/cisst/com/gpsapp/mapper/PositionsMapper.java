package work.cisst.com.gpsapp.mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import work.cisst.com.gpsapp.DB.DBOpenHelper;
import work.cisst.com.gpsapp.entity.Positions;
import work.cisst.com.gpsapp.entity.User;

public class PositionsMapper {

    public List<Positions> getListByTime(String startTime,String endTime,int userId) {
        List<Positions> list = new ArrayList<>();
        Connection conn = DBOpenHelper.getConn();
        String sql = "select * from positions where userid = ? and time between ? and ? order by time";
        try {
            PreparedStatement pst =  conn.prepareStatement(sql);
            pst.setObject(1, userId);
            pst.setObject(2, startTime);
            pst.setObject(3, endTime);
            ResultSet rs = null;
            rs = pst.executeQuery();
            while (rs.next()) {
                Positions positions = new Positions();
                positions.setId(rs.getInt("id"));
                positions.setUserId(rs.getInt("userId"));
                positions.setLat(rs.getDouble("Lat"));
                positions.setLng(rs.getDouble("Lng"));
                positions.setTime(rs.getTimestamp("time"));
                list.add(positions);
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
}
