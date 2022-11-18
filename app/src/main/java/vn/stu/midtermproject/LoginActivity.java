package vn.stu.midtermproject;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import vn.stu.midtermproject.util.DBUtil;

public class LoginActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    TextView tvUsername, tvPassword;
    ImageButton btnLogin, btnShowHidePass;

    private String TABLE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        addControls();
        addEvents();
    }

    private void addControls() {

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        tvUsername = findViewById(R.id.tvUsername);
        tvPassword = findViewById(R.id.tvPassword);
        btnLogin = findViewById(R.id.btnLoginActivity);
        btnShowHidePass = findViewById(R.id.btn_show_hide_pass);

        TABLE = "user";
    }

    private void addEvents() {

        edtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvUsername.setText("");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtUsername.getText().toString().isEmpty()) {
                    tvUsername.setText(R.string.app_username);
                }
            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvPassword.setText("");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtPassword.getText().toString().isEmpty()) {
                    tvPassword.setText(R.string.app_password);
                }
            }
        });

        btnShowHidePass.setOnClickListener(view -> {
            if (btnShowHidePass.getTag().toString().equals(getString(R.string.app_show_password))) {
                edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                btnShowHidePass.setTag(getString(R.string.app_hide_password));
            } else {
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                btnShowHidePass.setTag(getString(R.string.app_show_password));
            }
        });

        btnLogin.setOnClickListener(view -> handleLogin());
    }

    private void handleLogin() {

        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.app_empty_info, Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase database = DBUtil.openOrCreateDataBases(LoginActivity.this);
        Cursor cursor = database.query(
                TABLE,
                new String[]{"username", "password"},
                "username = ?",
                new String[]{username},
                null, null, null);
        cursor.moveToFirst();

        if (cursor.getCount() < 1) {
            Toast.makeText(this, R.string.app_login_fail, Toast.LENGTH_SHORT).show();
            return;
        }

        String dbUsername = cursor.getString(0);
        String dbPassword = cursor.getString(1);

        if (username.equals(dbUsername)) {
            if (password.equals(dbPassword)) {
                Toast.makeText(this, R.string.app_login_success, Toast.LENGTH_SHORT).show();
                saveUsernamePreferences(dbUsername);
                cursor.close();
                database.close();
                finish();
                return;
            }
        }
        Toast.makeText(this, R.string.app_login_fail, Toast.LENGTH_SHORT).show();
        cursor.close();
        database.close();
    }

    private void saveUsernamePreferences(String username) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

}
