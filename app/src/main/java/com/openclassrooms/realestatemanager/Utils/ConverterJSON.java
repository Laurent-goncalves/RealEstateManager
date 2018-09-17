package com.openclassrooms.realestatemanager.Utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.SearchQuery;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConverterJSON {

    // ------------------------------------------ PROPERTY ----------------------------------------------------------

    public static String convertPropertyToJson(Property property){
        Gson gson = new Gson();
        return gson.toJson(property);
    }

    public static Property convertJsonToProperty(String propertyJson){
        Gson gson = new Gson();
        Type PropType = new TypeToken<Property>(){}.getType();

        if(propertyJson==null)
            return null;
        else
            return gson.fromJson(propertyJson,PropType);
    }

    // --------------------------------------- LIST PROPERTIES -----------------------------------------------------

    public static String convertListPropertyToJson(List<Property> listProperty){
        Gson gson = new Gson();
        return gson.toJson(listProperty);
    }

    public static List<Property> convertJsonToListProperty(String listPropertyJson){
        Gson gson = new Gson();
        Type listPropType = new TypeToken<ArrayList<Property>>(){}.getType();

        if(listPropertyJson==null)
            return null;
        else
            return gson.fromJson(listPropertyJson,listPropType);
    }

    // ------------------------------------- LIST IMAGES PROPERTY -------------------------------------------------

    public static String convertListImagesPropertyToJson(List<ImageProperty> listImages){
        Gson gson = new Gson();
        return gson.toJson(listImages);
    }

    public static List<ImageProperty> convertJsonToListImagesProperty(String listImagesJson){
        Gson gson = new Gson();
        Type listImagePropType = new TypeToken<ArrayList<ImageProperty>>(){}.getType();

        if(listImagesJson==null)
            return null;
        else
            return gson.fromJson(listImagesJson,listImagePropType);
    }

    // ------------------------------------- SearchQuery -------------------------------------------------

    public static String convertSearchQueryToJson(SearchQuery searchQuery){
        Gson gson = new Gson();
        return gson.toJson(searchQuery);
    }

    public static SearchQuery convertJsonToSearchQuery(String searchQueryJson){
        Gson gson = new Gson();
        Type searchQueryType = new TypeToken<SearchQuery>(){}.getType();

        if(searchQueryJson==null)
            return null;
        else
            return gson.fromJson(searchQueryJson,searchQueryType);
    }

}
