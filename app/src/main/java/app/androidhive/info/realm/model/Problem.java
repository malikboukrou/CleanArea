package app.androidhive.info.realm.model;


import android.graphics.Bitmap;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Problem extends RealmObject {

    @PrimaryKey
    private long id;

    private String type;
    private double longitude;
    private double latitude;
    private String description;
    private byte[] photo;

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}

