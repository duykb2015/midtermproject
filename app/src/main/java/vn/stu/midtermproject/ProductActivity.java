package vn.stu.midtermproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import vn.stu.midtermproject.model.Product;
import vn.stu.midtermproject.util.DBUtil;

public class ProductActivity extends AppCompatActivity {
    ListView lvProduct;
    ArrayAdapter<Product> adapter;
    String TABLE = "product";


    @NonNull
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
//        replaceFragment(new HomeFragment());
//
//        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                default:
//                    break;
//            }
//            return true;
//        });
        addControls();
        getProductData();

        //addEvents();
    }

    private void addControls() {
        lvProduct = findViewById(R.id.lvProduct);
        adapter = new ArrayAdapter<>(ProductActivity.this, android.R.layout.simple_list_item_1);
        lvProduct.setAdapter(adapter);
    }

    private void getProductData() {
        SQLiteDatabase database = DBUtil.openOrCreateDataBases(ProductActivity.this);

        Cursor cursor = database.rawQuery("SELECT * FROM product", null);

        while (cursor.moveToNext()) {
            Product product = new Product(cursor.getInt(0), cursor.getString(1), null, null, cursor.getInt(4), cursor.getString(5));
            adapter.add(product);
        }
        cursor.close();
        database.close();
    }


    private void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.flMain, fragment);
//        fragmentTransaction.commit();
    }
}
