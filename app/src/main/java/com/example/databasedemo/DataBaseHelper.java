package com.example.databasedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String COLUMN_CUSTOMER_AGE = "CUSTOMER_AGE";
    public static final String COLUMN_ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER";
    public static final String COLUMN_ID = "ID";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "customer.db", null, 1);
    }

    // this is called the first timea database is accessed. There should be code n here to create a new database.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create the table
        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CUSTOMER_NAME + " TEXT, " + COLUMN_CUSTOMER_AGE + " INT, " + COLUMN_ACTIVE_CUSTOMER + " BOOL)";
        db.execSQL(createTableStatement);
    }

    // this is called if the database version number changes. It prevents previous users apps from braking when you change the database design.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public boolean addOne(CustomerModel customerModel) {
        // to insert new data we have to get the writable database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();

        value.put(COLUMN_CUSTOMER_NAME, customerModel.getName());
        value.put(COLUMN_CUSTOMER_AGE, customerModel.getAge());
        value.put(COLUMN_ACTIVE_CUSTOMER, customerModel.isActive());

        long insert = db.insert(CUSTOMER_TABLE, null, value);

        // if insert = -1 -> inseration was not successful, otherwise inseration was successfull
        return insert != -1;
    }

    public List<CustomerModel> selectAll() {
        List<CustomerModel> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + CUSTOMER_TABLE;

        // we don't need a writable database (when we have a writable copy of the database
        // this will be locked
        SQLiteDatabase db = this.getReadableDatabase();

        // we will use rawQuery to be able to use the cursor
        // we need the cursor to process the data

        // parcurg
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            // if this returns true that means that there are results
            // loop through the cursor

            do {
                int customerID = cursor.getInt(0);
                String customerName  = cursor.getString(1);
                int customerAge = cursor.getInt(2);
                boolean customerActive = (cursor.getInt(3) == 1) ? true : false;

                CustomerModel newCustomer = new CustomerModel(customerID, customerName, customerAge, customerActive);
                returnList.add(newCustomer);
            }while (cursor.moveToNext());
        }

        // close the cursor
        // close the database
        cursor.close();
        db.close();
        return returnList;
    }

    public boolean deleteOne(CustomerModel customerModel) {
        // return true if the given customer is in the database
        // else return false
        String queryString = "DELETE FROM " + CUSTOMER_TABLE + " WHERE "  + COLUMN_ID + " = " + customerModel.getId();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            return true;
        }
        return false;

    }
}
