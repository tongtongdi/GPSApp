package work.cisst.com.gpsapp.entity;

import java.util.Date;

public class positions {
    private int id;
    private int userId;
    private double lng;
    private double lat;
    private Date time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "positions{" +
                "id=" + id +
                ", userId=" + userId +
                ", lng=" + lng +
                ", lat=" + lat +
                ", time=" + time +
                '}';
    }
}
