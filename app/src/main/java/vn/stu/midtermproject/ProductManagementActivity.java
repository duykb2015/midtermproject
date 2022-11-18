package vn.stu.midtermproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import vn.stu.midtermproject.model.Category;
import vn.stu.midtermproject.util.DBUtil;

public class ProductManagementActivity extends AppCompatActivity {
    Integer productID, categoryID;
    EditText edtProductName, edtProductNetWeight, edtProductPrice;
    TextView tvCategoryName, tvProductCategoryOrigin, tvProductCurrency, tvProductUnit;
    ImageView ivProductImage;
    ImageButton btnSave;
    Spinner spnProductCategory;
    ArrayAdapter<Category> adapter;
    Bitmap selectedImage = null;
    Boolean flag = false;

    int SELECT_PICTURE = 200;
    String table = "product";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);

        addControls();
        Intent intent = getIntent();
        int oldProductID = intent.getIntExtra("product", 0);
        if (oldProductID != 0) {
            loadProductData(oldProductID);
        }
        addEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnAbout:
                Intent intent = new Intent(ProductManagementActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.mnExit:
                new AlertDialog.Builder(this).setTitle(R.string.app_dialog_title_exit)
                        .setMessage(R.string.app_dialog_message_exit)
                        .setPositiveButton(R.string.app_dialog_cancel, null)
                        .setNeutralButton(
                                R.string.app_dialog_confirm, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.remove("username");
                                        editor.commit();
                                        System.exit(0);
                                    }
                                })
                        .create()
                        .show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addControls() {
        edtProductName = findViewById(R.id.edtProductName);
        edtProductNetWeight = findViewById(R.id.edtProductNetWeight);
        edtProductPrice = findViewById(R.id.edtProductPrice);
        tvProductCategoryOrigin = findViewById(R.id.tvProductOriginName);
        tvCategoryName = findViewById(R.id.tvProductCategoryName);
        ivProductImage = findViewById(R.id.ivProductImage);
        tvProductCurrency = findViewById(R.id.tvProductCurrency);
        tvProductUnit = findViewById(R.id.tvProductUnit);
        spnProductCategory = findViewById(R.id.spnProductCategory);
        btnSave = findViewById(R.id.btnSaveProduct);
        btnSave.setEnabled(false);
    }

    @SuppressLint("SetTextI18n")
    private void loadProductData(Integer ID) {
        SQLiteDatabase database = DBUtil.openOrCreateDataBases(ProductManagementActivity.this);
        Cursor cursor = database.rawQuery("SELECT * FROM product WHERE id = ?", new String[]{ID + ""});
        cursor.moveToFirst();

        productID = cursor.getInt(0);
        edtProductName.setText(cursor.getString(1));
        Category category = this.getCategory(cursor.getInt(2));
        categoryID = category.getId();
        tvCategoryName.setText(category.getName());
        tvProductCategoryOrigin.setText(category.getOrigin());
        byte[] bytes = cursor.getBlob(3);
        Bitmap image = null;
        if (bytes != null) {
            image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        ivProductImage.setImageBitmap(image);
        edtProductPrice.setText(cursor.getInt(4) + "");
        edtProductNetWeight.setText(cursor.getString(5));
        cursor.close();
    }

    private Category getCategory(int id) {
        SQLiteDatabase database = DBUtil.openOrCreateDataBases(ProductManagementActivity.this);
        Cursor cursor = database.rawQuery("SELECT * FROM category WHERE id = ?", new String[]{id + ""});
        cursor.moveToFirst();
        Category category = new Category(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        cursor.close();
        return category;
    }

    private ArrayList<Category> getAllCategory() {
        SQLiteDatabase database = DBUtil.openOrCreateDataBases(ProductManagementActivity.this);
        Cursor cursor = database.rawQuery("SELECT * FROM category", null);
        ArrayList<Category> list = new ArrayList<>();
        list.add(new Category());
        while (cursor.moveToNext()) {
            list.add(new Category(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
        }
        cursor.close();
        return list;
    }

    private void addEvents() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getAllCategory());
        spnProductCategory.setAdapter(adapter);
        edtProductName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnSave.setEnabled(true);
            }
        });
        edtProductPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnSave.setEnabled(true);
            }
        });
        edtProductNetWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnSave.setEnabled(true);
            }
        });
        tvProductCurrency.setOnClickListener(view -> edtProductPrice.requestFocus(View.FOCUS_LEFT));
        tvProductUnit.setOnClickListener(view -> edtProductNetWeight.requestFocus(View.FOCUS_LEFT));
        ivProductImage.setOnClickListener(view -> {
            btnSave.setEnabled(true);
            imageChooser();
        });

        spnProductCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!flag) {
                    if (categoryID != null) {
                        spnProductCategory.setSelection(categoryID);
                    }
                    flag = true;
                } else {
                    if (i == 0) {
                        return;
                    }
                    btnSave.setEnabled(true);
                    categoryID = adapter.getItem(i).getId();
                    tvCategoryName.setText(adapter.getItem(i).getName());
                    tvProductCategoryOrigin.setText(adapter.getItem(i).getOrigin());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnSave.setOnClickListener(view -> {
            saveOrUpdate();
            btnSave.setEnabled(false);
        });
    }

    private void saveOrUpdate() {
        String productName = edtProductName.getText().toString();
        String productPrice = edtProductPrice.getText().toString();
        String productNetWeight = edtProductNetWeight.getText().toString();
        ContentValues values = new ContentValues();
        if (selectedImage != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] simple = stream.toByteArray();
            values.put("image", simple);
        }
        values.put("name", productName);
        values.put("price", productPrice);
        values.put("netweight", productNetWeight);

        if (values.get("name") == null || values.get("price") == null || values.get("netweight") == null) {
            Toast.makeText(this, getString(R.string.app_empty_info), Toast.LENGTH_SHORT).show();
            return;
        }

        if (categoryID != null) {
            values.put("category_id", categoryID);
        }
        if (productID == null) {
            if (values.get("category_id") == null) {
                Toast.makeText(this, getString(R.string.app_empty_info), Toast.LENGTH_SHORT).show();
                return;
            }
            insertData(values);
            return;
        }
        updateData(values);

    }

    private void updateData(ContentValues values) {
        SQLiteDatabase database = DBUtil.openOrCreateDataBases(ProductManagementActivity.this);
        database.update(table, values, "id = ?", new String[]{productID + ""});
        Toast.makeText(this, R.string.app_product_saved, Toast.LENGTH_SHORT).show();
    }

    private void insertData(ContentValues values) {
        SQLiteDatabase database = DBUtil.openOrCreateDataBases(ProductManagementActivity.this);
        database.insert(table, null, values);
        Toast.makeText(this, R.string.app_product_saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void imageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.app_picture_from_gallery)), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                assert data != null;
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    try {
                        selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ivProductImage.setImageURI(selectedImageUri);
                }
            }
        }
    }

}
