package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract;

/**
 * Created by maela on 14.7.2017.
 */

public class ItemActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_INVENTORY_LOADER = 0;
    private Uri mCurrentItemUri;
    private Spinner mImageSpinner;
    private EditText mNameEdit;
    private EditText mPriceEdit;
    private EditText mDescriptionEdit;
    private Spinner mSupplierSpinner;
    private ImageView mImageView;
    private Integer mImage;
    private int mSupplier;
    private TextView mQuantity;
    private int quantityInt;

    private boolean mItemHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_editor);

        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();

        if (mCurrentItemUri == null) {
            setTitle(getString(R.string.add_new_item));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.edit_item));
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }

        mImageSpinner = (Spinner) findViewById(R.id.spinner_image);
        mSupplierSpinner = (Spinner) findViewById(R.id.spinner_suppliers);

        mNameEdit = (EditText) findViewById(R.id.edit_name);
        mPriceEdit = (EditText) findViewById(R.id.edit_price);
        mDescriptionEdit = (EditText) findViewById(R.id.edit_description);
        mImageView = (ImageView) findViewById(R.id.edit_image);
        mQuantity = (TextView) findViewById(R.id.edit_quantity);

        mImageSpinner.setOnTouchListener(mTouchListener);
        mSupplierSpinner.setOnTouchListener(mTouchListener);

        mPriceEdit.setOnTouchListener(mTouchListener);
        mDescriptionEdit.setOnTouchListener(mTouchListener);
        mQuantity.setOnTouchListener(mTouchListener);

        setupImageSpinner();
        setupSupplierSpinner();

        Button reOrder = (Button) findViewById(R.id.edit_order);
        Button minus = (Button) findViewById(R.id.minus);
        minus.setOnTouchListener(mTouchListener);
        Button plus = (Button) findViewById(R.id.plus);
        plus.setOnTouchListener(mTouchListener);
        final TextView quantityView = (TextView) findViewById(R.id.edit_quantity);
        quantityView.setText(String.valueOf(quantityInt));

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = quantityView.getText().toString();
                Log.i("ItemActivity", temp);
                quantityInt = Integer.parseInt(temp);
                if (quantityInt > 0) {
                    quantityInt--;
                }
                quantityView.setText(String.valueOf(quantityInt));
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = quantityView.getText().toString();
                Log.i("ItemActivity", temp);
                quantityInt = Integer.parseInt(temp);
                quantityInt++;
                quantityView.setText(String.valueOf(quantityInt));
            }
        });

        reOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNameEdit.getText().toString();
                if (name != null || name.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Re-order " + name);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            }
        });

    }

    private void setupImageSpinner() {
        ArrayAdapter imageSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_image_options, android.R.layout.simple_spinner_item);

        imageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        mImageSpinner.setAdapter(imageSpinnerAdapter);
        mImageSpinner.setPrompt(getString(R.string.select_image));

        mImageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.image_lamp))) {
                        mImage = (R.drawable.lamp);
                    } else if (selection.equals(getString(R.string.image_sofa))) {
                        mImage = (R.drawable.sofa);
                    } else if (selection.equals(getString(R.string.image_table))) {
                        mImage = (R.drawable.table);
                    }
                }
                mImageView.setImageResource(mImage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mImage = InventoryContract.InventoryEntry.DEFAULT_IMAGE;
            }
        });
    }

    private void setupSupplierSpinner() {
        ArrayAdapter supplierSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_supplier_options, android.R.layout.simple_spinner_item);

        supplierSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        mSupplierSpinner.setAdapter(supplierSpinnerAdapter);
        mSupplierSpinner.setPrompt(getString(R.string.select_supplier));

        mSupplierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.supplier_local))) {
                        mSupplier = InventoryContract.InventoryEntry.SUPPLIER_LOCAL;
                    } else if (selection.equals(getString(R.string.supplier_china))) {
                        mSupplier = InventoryContract.InventoryEntry.SUPPLIER_CHINA;
                    } else if (selection.equals(getString(R.string.supplier_usa))) {
                        mSupplier = InventoryContract.InventoryEntry.SUPPLIER_USA;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSupplier = InventoryContract.InventoryEntry.SUPPLIER_LOCAL;
            }
        });
    }

    private void saveItem() {
        String nameString = mNameEdit.getText().toString().trim();
        String descriptionString = mDescriptionEdit.getText().toString().trim();
        String priceString = mPriceEdit.getText().toString().trim();
        quantityInt = Integer.parseInt(mQuantity.getText().toString());


        if (mCurrentItemUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(descriptionString) &&
                TextUtils.isEmpty(priceString)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_NAME, nameString);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRICE, priceString);
        values.put(InventoryContract.InventoryEntry.COLUMN_DESCRIPTION, descriptionString);
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, quantityInt);
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER, mSupplier);
        values.put(InventoryContract.InventoryEntry.COLUMN_IMAGE, mImage);

        if (mCurrentItemUri == null) {
            Uri newUri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.saving_error),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.item_saved),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.error_updating),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.item_updated),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String nameString = mNameEdit.getText().toString().trim();
        if (nameString == null || nameString.isEmpty()) {
            mNameEdit.setError("An item must have a name.");
            return false;
        }
        switch (item.getItemId()) {
            case R.id.action_save:
                saveItem();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(ItemActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(ItemActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mItemHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRICE,
                InventoryContract.InventoryEntry.COLUMN_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER,
                InventoryContract.InventoryEntry.COLUMN_IMAGE,
                InventoryContract.InventoryEntry.COLUMN_DESCRIPTION};

        return new CursorLoader(this,
                mCurrentItemUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_NAME);
            int priceIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE);
            int quantityIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
            int supplierIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER);
            int imageIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_IMAGE);
            int descriptionIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_DESCRIPTION);

            String name = cursor.getString(nameIndex);
            String desc = cursor.getString(descriptionIndex);
            mImage = cursor.getInt(imageIndex);
            int price = cursor.getInt(priceIndex);
            int quantity = cursor.getInt(quantityIndex);
            int supplier = cursor.getInt(supplierIndex);

            mNameEdit.setText(name);
            mDescriptionEdit.setText(desc);
            mPriceEdit.setText(Integer.toString(price));
            mQuantity.setText(Integer.toString(quantity));

            if (mImage == null) {
                mImage = InventoryContract.InventoryEntry.DEFAULT_IMAGE;
            }
            mImageView.setImageResource(mImage);

            switch (mImage) {
                case R.drawable.lamp:
                    mImageSpinner.setSelection(0);
                    break;
                case R.drawable.table:
                    mImageSpinner.setSelection(2);
                    break;
                case R.drawable.sofa:
                    mImageSpinner.setSelection(1);
            }

            switch (supplier) {
                case InventoryContract.InventoryEntry.SUPPLIER_LOCAL:
                    mSupplierSpinner.setSelection(0);
                    break;
                case InventoryContract.InventoryEntry.SUPPLIER_CHINA:
                    mSupplierSpinner.setSelection(1);
                    break;
                case InventoryContract.InventoryEntry.SUPPLIER_USA:
                    mSupplierSpinner.setSelection(2);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEdit.setText("");
        mDescriptionEdit.setText("");
        mPriceEdit.setText("");
        mQuantity.setText("0");
        mSupplierSpinner.setSelection(0);

        mImageSpinner.setSelection(InventoryContract.InventoryEntry.DEFAULT_IMAGE);
        mImage = InventoryContract.InventoryEntry.DEFAULT_IMAGE;
        mImageView.setImageResource(mImage);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.discard_changes);
        builder.setPositiveButton(getString(R.string.discard), discardButtonClickListener);
        builder.setNegativeButton(getString(R.string.keep_editing), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_item);
        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteItem();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteItem() {
        if (mCurrentItemUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentItemUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.edit_error_delete),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.edit_item_deleted),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}