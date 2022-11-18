package vn.stu.midtermproject;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import vn.stu.midtermproject.databinding.ActivityProductBinding;

public class ProductActivity extends AppCompatActivity {
    ActivityProductBinding binding;

    public ProductActivity(){
        super(R.layout.activity_product);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.flContent, fragment)
                .commit();
    }
}
