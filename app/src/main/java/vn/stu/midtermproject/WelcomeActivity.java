package vn.stu.midtermproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    ImageButton btnLogin;
    public final String DB_NAME = "doan.db";
    public final String PATH_SUFFIX = "/databases/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        addControls();
        addEvents();
    }


    private void addControls() {
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                intent.putExtra("DB_NAME", DB_NAME);
                intent.putExtra("PATH_SUFFIX", PATH_SUFFIX);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String username = preferences.getString("username", "");

        if (!username.isEmpty()) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {

    }
}
