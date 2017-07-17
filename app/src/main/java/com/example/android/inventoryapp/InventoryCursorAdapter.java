package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.inventoryapp.data.InventoryContract;

import java.io.IOException;

public class InventoryCursorAdapter extends CursorAdapter {
    /**
     * Constructs a new InventoryCursorAdapter.
     *
     * @param context
     * @param cursor
     */
    public InventoryCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    /**
     * Makes a new blank list item view.     *
     *
     * @param context
     * @param cursor
     * @param parent
     * @return the new list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * Binds data to the given list item layout.     *
     *
     * @param view
     * @param context
     * @param cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final Context currentContext = context;
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView price = (TextView) view.findViewById(R.id.price);
        final TextView quantityView = (TextView) view.findViewById(R.id.quantity);
        ImageView mImageView = (ImageView) view.findViewById(R.id.image);

        int idIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry._ID);
        int nameIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_NAME);
        int priceIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE);
        int quantityIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
        int imageIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_IMAGE);

        final int id = cursor.getInt(idIndex);
        String nameString = cursor.getString(nameIndex);
        int priceInt = cursor.getInt(priceIndex);
        final int quantityInt = cursor.getInt(quantityIndex);
        String imageString = cursor.getString(imageIndex);
        Bitmap mBitmap = null;

        if (imageString == null || imageString.isEmpty()) {
            imageString = InventoryContract.InventoryEntry.DEFAULT_IMAGE;
            mImageView.setImageResource(Integer.parseInt(imageString));
        } else if (TextUtils.isDigitsOnly(imageString)) {
            mImageView.setImageResource(Integer.parseInt(imageString));
        } else {
            Uri mImageUri = Uri.parse(imageString);
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), mImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mBitmap = BitmapHandler.getResizedBitmap(mBitmap, 60);
            mImageView.setImageBitmap(mBitmap);
        }

        name.setText(nameString);
        price.setText(String.valueOf(priceInt));
        quantityView.setText(String.valueOf(quantityInt));

        final Button saleButton = (Button) view.findViewById(R.id.sale_button);
        saleButton.setTag(id);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = 0;
                if (quantityInt > 0) {
                    temp = quantityInt - 1;
                }
                ContentValues values = new ContentValues();
                values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, temp);
                Uri uri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, id);
                currentContext.getContentResolver().update(uri, values, null, null);
                quantityView.setText(String.valueOf(temp));
            }
        });
    }
}
