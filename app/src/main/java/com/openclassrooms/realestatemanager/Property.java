package com.openclassrooms.realestatemanager;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.media.Image;

import java.util.Date;
import java.util.List;

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

    public Property(int id, String type, Double price, Double surface, int roomNumber, String description, String address, String interestPoints, String dateStart, String estateAgent) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.roomNumber = roomNumber;
        this.description = description;
        this.address = address;
        this.interestPoints = interestPoints;
        this.dateStart = dateStart;
        this.estateAgent = estateAgent;
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

    public String getEstateAgent() {
        return estateAgent;
    }

    public void setEstateAgent(String estateAgent) {
        this.estateAgent = estateAgent;
    }
}
