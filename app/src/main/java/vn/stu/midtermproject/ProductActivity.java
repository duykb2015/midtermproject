package vn.stu.midtermproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import vn.stu.midtermproject.databinding.ActivityProductBinding;
import vn.stu.midtermproject.model.Product;
import vn.stu.midtermproject.util.DBUtil;

public class ProductActivity extends AppCompatActivity {
    ListView lvProduct;
    ArrayAdapter<Product> adapter;
    String TABLE = "product";

    ActivityProductBinding binding;

    public ProductActivity(){
        super(R.layout.activity_product);
    }

    @NonNull
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());//R.layout.activity_product);
        replaceFragment(new ProductFragment());

        binding.bnvBNV.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_product:
                    replaceFragment(new ProductFragment());
                    break;
                case R.id.menu_category:
                    replaceFragment(new CategoryFragment());
                    break;
                default:
                    break;
            }
            return true;
        });
        //addControls();
        //getProductData();

        //addEvents();
    }

    private void addControls() {
        //lvProduct = findViewById(R.id.lvProduct);
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
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.flContent, fragment)
                .commit();
    }
}
