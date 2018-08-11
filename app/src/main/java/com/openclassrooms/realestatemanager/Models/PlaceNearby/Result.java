
package com.openclassrooms.realestatemanager.Models.PlaceNearby;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("geometry")
    @Expose
    private transient Geometry geometry;
    @SerializedName("icon")
    @Expose
    private transient String icon;
    @SerializedName("id")
    @Expose
    private transient String id;
    @SerializedName("name")
    @Expose
    private transient String name;
    @SerializedName("photos")
    @Expose
    private transient List<Photo> photos = null;
    @SerializedName("place_id")
    @Expose
    private transient String placeId;
    @SerializedName("reference")
    @Expose
    private transient String reference;
    @SerializedName("scope")
    @Expose
    private transient String scope;
    @SerializedName("types")
    @Expose
    private List<String> types = null;
    @SerializedName("vicinity")
    @Expose
    private transient String vicinity;
    @SerializedName("opening_hours")
    @Expose
    private transient OpeningHours openingHours;
    @SerializedName("plus_code")
    @Expose
    private transient PlusCode plusCode;
    @SerializedName("rating")
    @Expose
    private transient Double rating;

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public PlusCode getPlusCode() {
        return plusCode;
    }

    public void setPlusCode(PlusCode plusCode) {
        this.plusCode = plusCode;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

}
