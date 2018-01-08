package com.weather.bean;
import java.util.List;
import java.util.Date;

/**
 * Auto-generated: 2017-06-21 16:38:41
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Results {

    private Location location;
    private List<Daily> daily;
    private String last_update;
    public void setLocation(Location location) {
         this.location = location;
     }
     public Location getLocation() {
         return location;
     }

    public void setDaily(List<Daily> daily) {
         this.daily = daily;
     }
     public List<Daily> getDaily() {
         return daily;
     }

    public void setLast_update(String last_update) {
         this.last_update = last_update;
     }
     public String getLast_update() {
         return last_update;
     }

}