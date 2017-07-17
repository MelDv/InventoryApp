package com.example.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class InventoryDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = InventoryDbHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "store.db";
    public static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_INVENTORY_TABLE =
            "CREATE TABLE IF NOT EXISTS " + InventoryContract.InventoryEntry.TABLE_NAME + " (" +
                    InventoryContract.InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    InventoryContract.InventoryEntry.COLUMN_NAME + " TEXT NOT NULL," +
                    InventoryContract.InventoryEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0," +
                    InventoryContract.InventoryEntry.COLUMN_PRICE + " INTEGER," +
                    InventoryContract.InventoryEntry.COLUMN_SUPPLIER + " INTEGER NOT NULL," +
                    InventoryContract.InventoryEntry.COLUMN_DESCRIPTION + " TEXT," +
                    InventoryContract.InventoryEntry.COLUMN_IMAGE + " TEXT);";

    private static final String SQL_DELETE_INVENTORY_TABLE =
            "DROP TABLE IF EXISTS " + InventoryContract.InventoryEntry.TABLE_NAME;

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
        Log.i(LOG_TAG, SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_INVENTORY_TABLE);
        onCreate(db);
    }
}
