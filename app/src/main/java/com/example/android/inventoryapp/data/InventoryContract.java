package com.example.android.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android.inventoryapp.R;

public class InventoryContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY = "inventory";

    // Empty contractor prevents anyone from instantiating this class
    private InventoryContract() {
    }

    public static abstract class InventoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);
        public static final int DEFAULT_IMAGE = R.drawable.lamp;
        /**
         * MIME type of CONTENT_URI for an inventory list.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /**
         * MIME type of CONTENT_URI for an inventory item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        public static final String TABLE_NAME = "inventory";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_SUPPLIER = "supplier";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE = "image";

        public static final int SUPPLIER_LOCAL = 0;
        public static final int SUPPLIER_CHINA = 1;
        public static final int SUPPLIER_USA = 2;

        /**
         * Returns true, if the supplier is in the list.
         */
        public static boolean isValidSupplier(int supplier) {
            if (supplier == SUPPLIER_LOCAL || supplier == SUPPLIER_CHINA || supplier == SUPPLIER_USA) {
                return true;
            }
            return false;
        }

    }
}
