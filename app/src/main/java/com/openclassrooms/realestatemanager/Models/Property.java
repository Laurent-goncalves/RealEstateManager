package com.openclassrooms.realestatemanager.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.database.Cursor;


/** Le type de bien (appartement, loft, manoir, etc) ;
 Le prix du bien (en dollar) ;
 La surface du bien (en m2) ;
 Le nombre de pièces ;
 La description complète du bien ;
 Au moins une photo, avec une description associée. Vous devez gérer le cas où plusieurs photos sont présentes pour un bien ! La photo peut être récupérée depuis la galerie photos du téléphone, ou prise directement avec l'équipement ;
 L’adresse du bien ;
 Les points d’intérêts à proximité (école, commerces, parc, etc) ;
 Le statut du bien (toujours disponible ou vendu) ;
 La date d’entrée du bien sur le marché ;
 La date de vente du bien, s’il a été vendu ;
 L'agent immobilier en charge de ce bien.
 **/

@Entity
public class Property {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type;
    private Double price;
    private Double surface;
    private int roomNumber;
    private String description;
    private String address;
    private String interestPoints;
    private Boolean sold;
    private String dateStart;
    private String dateSold;
    private String estateAgent;
    private Double Lat;
    private Double Lng;
    private byte[] map;
    private String mainImagePath;
    private transient Boolean selected;

    public Property(int id, String type, Double price, Double surface, int roomNumber, String description, String address,
                    String interestPoints, Boolean sold, String dateStart, String dateSold, Double Lat, Double Lng,
                    String estateAgent, byte[] map, String mainImagePath) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.roomNumber = roomNumber;
        this.description = description;
        this.address = address;
        this.interestPoints = interestPoints;
        this.sold=sold;
        this.dateStart = dateStart;
        this.dateSold=dateSold;
        this.estateAgent = estateAgent;
        this.Lat = Lat;
        this.Lng = Lng;
        this.map = map;
        this.mainImagePath=mainImagePath;
    }

    @Ignore
    public Property() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSurface() {
        return surface;
    }

    public void setSurface(Double surface) {
        this.surface = surface;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInterestPoints() {
        return interestPoints;
    }

    public void setInterestPoints(String interestPoints) {
        this.interestPoints = interestPoints;
    }

    public Boolean getSold() {
        return sold;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateSold() {
        return dateSold;
    }

    public void setDateSold(String dateSold) {
        this.dateSold = dateSold;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLng() {
        return Lng;
    }

    public void setLng(Double lng) {
        Lng = lng;
    }

    public String getEstateAgent() {
        return estateAgent;
    }

    public void setEstateAgent(String estateAgent) {
        this.estateAgent = estateAgent;
    }

    public byte[] getMap() {
        return map;
    }

    public void setMap(byte[] map) {
        this.map = map;
    }

    public String getMainImagePath() {
        return mainImagePath;
    }

    public void setMainImagePath(String mainImagePath) {
        this.mainImagePath = mainImagePath;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    // --- UTILS ---
    public static Property fromContentValues(ContentValues values) {

        final Property property = new Property();

        if (values.containsKey("id")) property.setId(values.getAsInteger("id"));
        if (values.containsKey("type")) property.setType(values.getAsString("type"));
        if (values.containsKey("price")) property.setPrice(values.getAsDouble("price"));
        if (values.containsKey("surface")) property.setSurface(values.getAsDouble("surface"));
        if (values.containsKey("roomNumber")) property.setRoomNumber(values.getAsInteger("roomNumber"));
        if (values.containsKey("description")) property.setDescription(values.getAsString("description"));
        if (values.containsKey("address")) property.setAddress(values.getAsString("address"));
        if (values.containsKey("interestPoints")) property.setInterestPoints(values.getAsString("interestPoints"));
        if (values.containsKey("sold")) property.setSold(values.getAsBoolean("sold"));
        if (values.containsKey("dateStart")) property.setDateStart(values.getAsString("dateStart"));
        if (values.containsKey("dateSold")) property.setDateSold(values.getAsString("dateSold"));
        if (values.containsKey("estateAgent")) property.setEstateAgent(values.getAsString("estateAgent"));
        if (values.containsKey("Lat")) property.setLat(values.getAsDouble("Lat"));
        if (values.containsKey("Lng")) property.setLng(values.getAsDouble("Lng"));
        if (values.containsKey("map")) property.setMap(values.getAsByteArray("map"));
        if (values.containsKey("mainImagePath")) property.setMainImagePath(values.getAsString("mainImagePath"));

        return property;
    }

    public static ContentValues createContentValuesFromPropertyInsert(Property property) {

        final ContentValues values = new ContentValues();

        values.put("type",property.getType());
        values.put("price",property.getPrice());
        values.put("surface",property.getSurface());
        values.put("roomNumber",property.getRoomNumber());
        values.put("description",property.getDescription());
        values.put("address",property.getAddress());
        values.put("interestPoints",property.getInterestPoints());
        values.put("sold",property.getSold());
        values.put("dateStart",property.getDateStart());
        values.put("dateSold",property.getDateSold());
        values.put("estateAgent",property.getEstateAgent());
        values.put("Lat",property.getLat());
        values.put("Lng",property.getLng());
        values.put("map",property.getMap());
        values.put("mainImagePath",property.getMainImagePath());

        return values;
    }

    public static ContentValues createContentValuesFromPropertyUpdate(Property property) {

        final ContentValues values = new ContentValues();

        values.put("id",property.getId());
        values.put("type",property.getType());
        values.put("price",property.getPrice());
        values.put("surface",property.getSurface());
        values.put("roomNumber",property.getRoomNumber());
        values.put("description",property.getDescription());
        values.put("address",property.getAddress());
        values.put("interestPoints",property.getInterestPoints());
        values.put("sold",property.getSold());
        values.put("dateStart",property.getDateStart());
        values.put("dateSold",property.getDateSold());
        values.put("estateAgent",property.getEstateAgent());
        values.put("Lat",property.getLat());
        values.put("Lng",property.getLng());
        values.put("map",property.getMap());
        values.put("mainImagePath",property.getMainImagePath());

        return values;
    }

    public static Property getPropertyFromCursor(Cursor cursor){

        final Property property = new Property();

        if(cursor!=null){
            property.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            property.setType(cursor.getString(cursor.getColumnIndexOrThrow("type")));
            property.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
            property.setSurface(cursor.getDouble(cursor.getColumnIndexOrThrow("surface")));
            property.setRoomNumber(cursor.getInt(cursor.getColumnIndexOrThrow("roomNumber")));
            property.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
            property.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("address")));
            property.setInterestPoints(cursor.getString(cursor.getColumnIndexOrThrow("interestPoints")));
            property.setSold(cursor.getInt(cursor.getColumnIndexOrThrow("sold")) > 0);
            property.setDateStart(cursor.getString(cursor.getColumnIndexOrThrow("dateStart")));
            property.setDateSold(cursor.getString(cursor.getColumnIndexOrThrow("dateSold")));
            property.setEstateAgent(cursor.getString(cursor.getColumnIndexOrThrow("estateAgent")));
            property.setLat(cursor.getDouble(cursor.getColumnIndexOrThrow("Lat")));
            property.setLng(cursor.getDouble(cursor.getColumnIndexOrThrow("Lng")));
            property.setMap(cursor.getBlob(cursor.getColumnIndexOrThrow("map")));
            property.setMainImagePath(cursor.getString(cursor.getColumnIndexOrThrow("mainImagePath")));
        }

        return property;
    }
}
