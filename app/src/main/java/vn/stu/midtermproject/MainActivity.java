package vn.stu.midtermproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import vn.stu.midtermproject.databinding.ActivityMainBinding;
import vn.stu.midtermproject.util.DBUtil;


public class MainActivity extends AppCompatActivity {
    public final String DB_NAME = "doan.db";
    public final String PATH_SUFFIX = "/databases/";
    ActivityMainBinding binding;
    long lastPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
//        checkLogin();
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        replaceFragment(new HomeFragment());
//
//        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.home:
//                    replaceFragment(new HomeFragment());
//                    break;
//                case R.id.profile:
//                    replaceFragment(new ProfileFragment());
//                    break;
//                case R.id.settings:
//                    replaceFragment(new SettingFragment());
//                    break;
//                default:
//                    break;
//            }
//            return true;
//        });
//
//
//        addControls();
//        addEvents();
//        copyDatabase();
    }

    private void checkLogin() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String username = preferences.getString("username", "");
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("username");
        editor.commit();
        if (username.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPress > 5000) {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_LONG).show();
            lastPress = currentTime;
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flMain, fragment);
        fragmentTransaction.commit();
    }

    private void copyDatabase() {
        DBUtil.copyDBFileFromAssets(MainActivity.this, DB_NAME, PATH_SUFFIX);
    }

    private void addEvents() {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void addControls() {
    }
}