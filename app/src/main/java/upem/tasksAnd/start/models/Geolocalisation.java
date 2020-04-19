package upem.tasksAnd.start.models;

public class Geolocalisation {
    private int geoid;
    private  long longtitude;
    private long latitude;
    public Geolocalisation(){

    }

    public int getGeoid() {
        return geoid;
    }

    public void setGeoid(int geoid) {
        this.geoid = geoid;
    }

    public long getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(long longtitude) {
        this.longtitude = longtitude;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }
}
