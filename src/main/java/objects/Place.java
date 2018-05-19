package objects;

/**
 * Объект, инкаспулирующий заданную точку на карте.
 */
public class Place {
    private String name;
    private float latitude;
    private float longitude;


    public Place(String name, float latitude, float longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void dumpInfo() {
        System.out.println("-- Place in map: name: " + this.name + ", latitude: " + this.latitude + ", longitude: " + this.longitude);
    }
}
