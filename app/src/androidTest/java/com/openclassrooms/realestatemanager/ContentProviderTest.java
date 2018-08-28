package com.openclassrooms.realestatemanager;

import android.arch.persistence.room.Room;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.Models.Provider.ImageContentProvider;
import com.openclassrooms.realestatemanager.Models.Provider.PropertyContentProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class ContentProviderTest {

    // FOR DATA
    private PropertyContentProvider propertyContentProvider;
    private ImageContentProvider imageContentProvider;

    // Property for demo
    private Property PROPERTY_DEMO = new Property(0, "Appartment", 125000d,30.25d,1,
            "description","address","School, Subway",false,"01/06/2018","02/06/2018",0d,0d,"Eric",null,null);

    private ImageProperty IMAGE_DEMO = new ImageProperty(0,"pathMainImage","description",1);

    @Before
    public void setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(),
                PropertyDatabase.class)
                .allowMainThreadQueries()
                .build();

        propertyContentProvider = new PropertyContentProvider();
        propertyContentProvider.setUtils(InstrumentationRegistry.getTargetContext(),false);

        imageContentProvider = new ImageContentProvider();
        imageContentProvider.setUtils(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void addUpdateDeletePropertyInDatabase() {

        // Insert new property in database
        Uri uriInsert = propertyContentProvider.insert(PropertyContentProvider.URI_ITEM, Property.createContentValuesFromPropertyInsert(PROPERTY_DEMO));

        // Recover the property just inserted
        Uri uriQuery = ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, ContentUris.parseId(uriInsert));
        final Cursor cursor = propertyContentProvider.query(uriQuery, null, null, null, null);

        // check that address is the same as initially
        if (cursor != null) {
            while (cursor.moveToNext()) {
                assertThat(cursor.getString(cursor.getColumnIndexOrThrow("address")), is("address"));
            }
            cursor.close();
        }

        // Update the address for this property
        Uri uriUpdate = ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, ContentUris.parseId(uriInsert));

        PROPERTY_DEMO.setId((int) ContentUris.parseId(uriInsert));
        PROPERTY_DEMO.setAddress("new address");
        propertyContentProvider.update(uriUpdate,Property.createContentValuesFromPropertyUpdate(PROPERTY_DEMO),null,null);

        // Check that the address is well updated
        uriQuery = ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, ContentUris.parseId(uriInsert));
        final Cursor Newcursor = propertyContentProvider.query(uriQuery, null, null, null, null);

        if (Newcursor != null) {
            while (Newcursor.moveToNext()) {
                assertThat(Newcursor.getString(Newcursor.getColumnIndexOrThrow("address")), is("new address"));
            }
            Newcursor.close();
        }

        // Delete property
        Uri uriDelete = ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, ContentUris.parseId(uriInsert));
        propertyContentProvider.delete(uriDelete,null,null);
    }

    @Test
    public void addUpdateDeleteImagePropertyInDatabase(){

        // Insert new property in database
        Uri uriPropInsert = propertyContentProvider.insert(PropertyContentProvider.URI_ITEM, Property.createContentValuesFromPropertyInsert(PROPERTY_DEMO));

        // change id property
        IMAGE_DEMO.setIdProperty((int) ContentUris.parseId(uriPropInsert));

        // Insert new image in database
        Uri uriImgInsert = imageContentProvider.insert(ImageContentProvider.URI_ITEM, ImageProperty.createContentValuesFromImagePropertyInsert(IMAGE_DEMO));

        // check the image is well inserted in database
        Uri uriQuery = ContentUris.withAppendedId(ImageContentProvider.URI_ITEM, ContentUris.parseId(uriImgInsert));
        final Cursor cursor = imageContentProvider.query(uriQuery, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                assertThat(cursor.getString(cursor.getColumnIndexOrThrow("description")), is("description"));
            }
            cursor.close();
        }

        // Update the image description
        IMAGE_DEMO.setDescription("new description");
        IMAGE_DEMO.setId((int) ContentUris.parseId(uriImgInsert));

        Uri uriUpdate = ContentUris.withAppendedId(ImageContentProvider.URI_ITEM, ContentUris.parseId(uriImgInsert));
        propertyContentProvider.update(uriUpdate,Property.createContentValuesFromPropertyUpdate(PROPERTY_DEMO),null,null);

        // Check that the image has been well updated
        final Cursor NewCursor = imageContentProvider.query(uriQuery, null, null, null, null);

        if (NewCursor != null) {
            while (NewCursor.moveToNext()) {
                assertThat(NewCursor.getString(NewCursor.getColumnIndexOrThrow("description")), is("new description"));
            }
            NewCursor.close();
        }

        // Delete Image Property
        Uri uriImgDelete = ContentUris.withAppendedId(ImageContentProvider.URI_ITEM, ContentUris.parseId(uriImgInsert));
        imageContentProvider.delete(uriImgDelete,null,null);

        // Delete property
        Uri uriPropDelete = ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, ContentUris.parseId(uriPropInsert));
        propertyContentProvider.delete(uriPropDelete,null,null);
    }
}
