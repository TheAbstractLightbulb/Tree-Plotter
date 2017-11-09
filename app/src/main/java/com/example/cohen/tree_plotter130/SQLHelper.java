package com.example.cohen.tree_plotter130;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SQLHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Database.db";
    private static final String TABLE_NAME = "Data_table";
    private static final String NAME = "Name";
    private static final String MAP_NAME = "Map_name";
    private static final String DATE = "Date";
    private static final String PIN_LAT = "Pin_lat";
    private static final String PIN_LNG = "Pin_Lng";
    private static final String LAT = "Latitude";
    private static final String LNG = "Longitude";
    private static final String _id = "_id";
    private static final String TAG = "SQLHelper";
    private static final String ADDRESS = "Address";
    private static final String MAP_TYPE = "Map_type";




    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = "CREATE TABLE " + TABLE_NAME + " (_id INTAGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + MAP_NAME + " TEXT, "
                + ADDRESS + " TEXT, " + DATE + " TEXT, " + MAP_TYPE + " TEXT, " + PIN_LAT + " TEXT, " + PIN_LNG + " TEXT, " + LAT + " TEXT, " + LNG + " TEXT, )";
        sqLiteDatabase.execSQL(create);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public boolean insertData(String name, String map_name, String address, String date, String map_Type, String pinLat, String pinLng, String lat, String lng){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(MAP_NAME, map_name);
        contentValues.put(ADDRESS, address);
        contentValues.put(DATE, date);
        contentValues.put(MAP_TYPE, map_Type);
        contentValues.put(PIN_LAT, pinLat);
        contentValues.put(PIN_LNG, pinLng);
        contentValues.put(LAT, lat);
        contentValues.put(LNG, lng);

        long result = database.insert(TABLE_NAME, null, contentValues);
        if (result == -1){
            Log.e(TAG, "Error: " + TAG + "did not add data to database ");
            return false;

        }else {
            Log.e(TAG, TAG + " added data to database under the name " + name + ". Insertion successful!");
            return true;

        }
    }

    public Cursor getData(){
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = database.rawQuery(query, null);
        close();
        return data;

    }

    public int deleteData(int id){
        String ID = String.valueOf(id);
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_NAME, _id + " = ?", new String[]{ID});
    }

    public boolean updateData(String id, String newName, String newMapName, String newAddress, String newDate, String newMapType, String newPinLat, String newPinLng, String newLat, String newLng){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, newName);
        contentValues.put(MAP_NAME, newMapName);
        contentValues.put(ADDRESS, newAddress);
        contentValues.put(DATE, newDate);
        contentValues.put(MAP_TYPE, newMapType);
        contentValues.put(PIN_LAT, newPinLat);
        contentValues.put(PIN_LNG, newPinLng);
        contentValues.put(LAT, newLat);
        contentValues.put(LNG, newLng);
        long result = database.update(TABLE_NAME, contentValues, "_id = ?", new String[]{id});
        if (result == -1){
            Log.e(TAG, "Error: Database " + DATABASE_NAME + " was not updated. Id = " +id);
            return false;
        }else {
            Log.i(TAG, "Database " + DATABASE_NAME + " was updated. Id = " + id);
            return true;
        }


    }
}
