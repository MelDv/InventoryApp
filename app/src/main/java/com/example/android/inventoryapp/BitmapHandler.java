package com.example.android.inventoryapp;

import android.graphics.Bitmap;

public class BitmapHandler {

    public static Bitmap getResizedBitmap(Bitmap mBitmap, int maxSize) {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(mBitmap, width, height, true);
    }
}

