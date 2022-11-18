package vn.stu.midtermproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
        int productID = intent.getIntExtra("product", 0);
        if (productID != 0) {
            loadProductData(productID);
        }
        addEvents();


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
        btnSave = findViewById(R.id.btnSave);
        btnSave.setEnabled(false);
    }

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

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getAllCategory());
        spnProductCategory.setAdapter(adapter);
    }

    private Category getCategory(int id) {
        SQLiteDatabase database = DBUtil.openOrCreateDataBases(ProductManagementActivity.this);
        Cursor cursor = database.rawQuery("SELECT * FROM category WHERE id = ?", new String[]{id + ""});
        cursor.moveToFirst();
        Category category = new Category(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        return category;
    }

    private ArrayList<Category> getAllCategory() {
        SQLiteDatabase database = DBUtil.openOrCreateDataBases(ProductManagementActivity.this);
        Cursor cursor = database.rawQuery("SELECT * FROM category", null);
        ArrayList<Category> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(new Category(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
        }
        return list;
    }

    private void addEvents() {
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
        tvProductCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtProductPrice.requestFocus(View.FOCUS_LEFT);
            }
        });
        tvProductUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtProductNetWeight.requestFocus(View.FOCUS_LEFT);
            }
        });
        ivProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSave.setEnabled(true);
                imageChooser();
                Toast.makeText(ProductManagementActivity.this, "Wait", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        spnProductCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (flag == false) {
                    flag = true;
                } else {
                    btnSave.setEnabled(true);
                    categoryID = adapter.getItem(i).getId();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOrUpdate();
                btnSave.setEnabled(false);
            }
        });
    }

    private void saveOrUpdate() {
        if (productID == null) {
            insertData();
            return;
        }
        updateData();
    }

    private void updateData() {
        String productName = edtProductName.getText().toString();
        String productPrice = edtProductPrice.getText().toString();
        String productNetWeight = edtProductNetWeight.getText().toString();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] simple = stream.toByteArray();

        ContentValues values = new ContentValues();
        values.put("name", productName);
        values.put("image", simple);

        SQLiteDatabase database = DBUtil.openOrCreateDataBases(ProductManagementActivity.this);
        database.update(table, values, null, null);
    }

    private void insertData() {
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
