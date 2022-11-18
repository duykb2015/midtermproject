package vn.stu.midtermproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import vn.stu.midtermproject.util.DBUtil;

public class CategoryManagementActivity extends AppCompatActivity {
    Integer categoryID;
    EditText edtCategoryName, edtCategoryOrigin;
    ImageButton btnSave;
    String table = "category";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_management);

        addControls();
        Intent intent = getIntent();
        int oldCategoryID = intent.getIntExtra("category", 0);
        if (oldCategoryID != 0) {
            loadCategoryData(oldCategoryID);
        }
        addEvents();
    }


    private void addControls() {
        edtCategoryName = findViewById(R.id.edtMCategoryName);
        edtCategoryOrigin = findViewById(R.id.edtMCategoryOrigin);
        btnSave = findViewById(R.id.btnSaveCategory);
        btnSave.setEnabled(false);
    }

    private void loadCategoryData(int ID) {
        SQLiteDatabase database = DBUtil.openOrCreateDataBases(CategoryManagementActivity.this);
        Cursor cursor = database.rawQuery("SELECT * FROM category WHERE id = ?", new String[]{ID + ""});
        cursor.moveToFirst();

        categoryID = cursor.getInt(0);
        edtCategoryName.setText(cursor.getString(1));
        edtCategoryOrigin.setText(cursor.getString(2));
        cursor.close();

    }

    private void addEvents() {
        edtCategoryName.addTextChangedListener(new TextWatcher() {
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
        edtCategoryOrigin.addTextChangedListener(new TextWatcher() {
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

        btnSave.setOnClickListener(view -> {
            saveOrUpdate();
            btnSave.setEnabled(false);
        });
    }

    private void saveOrUpdate() {
        String categoryName = edtCategoryName.getText().toString();
        String categoryOrigin = edtCategoryOrigin.getText().toString();

        ContentValues values = new ContentValues();
        values.put("name", categoryName);
        values.put("origin", categoryOrigin);

        if (values.get("name") == null || values.get("origin") == null) {
            Toast.makeText(this, getString(R.string.app_empty_info), Toast.LENGTH_SHORT).show();
            return;
        }

        if (categoryID == null) {
            insertData(values);
            return;
        }
        updateData(values);

    }

    private void updateData(ContentValues values) {
        SQLiteDatabase database = DBUtil.openOrCreateDataBases(CategoryManagementActivity.this);
        database.update(table, values, "id = ?", new String[]{categoryID + ""});
        Toast.makeText(this, R.string.app_product_saved, Toast.LENGTH_SHORT).show();
    }

    private void insertData(ContentValues values) {
        SQLiteDatabase database = DBUtil.openOrCreateDataBases(CategoryManagementActivity.this);
        long isSave = database.insert(table, null, values);
        if (isSave <= 0) {
            Toast.makeText(this, "Err", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, R.string.app_product_saved, Toast.LENGTH_SHORT).show();
        finish();
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
                Intent intent = new Intent(CategoryManagementActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.mnExit:
                new AlertDialog.Builder(this).setTitle(R.string.app_dialog_title_exit)
                        .setMessage(R.string.app_dialog_message_exit)
                        .setPositiveButton(R.string.app_dialog_cancel, null)
                        .setNeutralButton(
                                R.string.app_dialog_confirm, (dialogInterface, i) -> {
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.remove("username");
                                    editor.commit();
                                    System.exit(0);
                                })
                        .create()
                        .show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
