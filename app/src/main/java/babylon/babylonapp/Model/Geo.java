package babylon.babylonapp.Model;

/**
 * Created by manuel on 8/3/16.
 */
public class Geo {

    Double lat;
    Double lng;

    public Geo(){}

    public Geo(Double lat, Double lng ){
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
