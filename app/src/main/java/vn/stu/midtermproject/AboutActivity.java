package vn.stu.midtermproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class AboutActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 555;
    private static final String LOG_TAG = "AndroidExample";

    TextView tvPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        addControls();

        addEvents();
    }

    private void addControls() {
        tvPhone = findViewById(R.id.tvAboutPhone);
    }

    private void addEvents() {
        tvPhone.setOnClickListener(view -> {
            handlePhoneCall();
        });
    }

    private void handlePhoneCall() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int sendSmsPermission = ActivityCompat.checkSelfPermission(AboutActivity.this, Manifest.permission.CALL_PHONE);
            if (sendSmsPermission != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);
            }
        }
        this.callNow();
    }

    private void callNow() {
        String phoneNumber = tvPhone.getText().toString();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        try {
            startActivity(intent);
        } catch (Exception ex) {
            Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(LOG_TAG, "Permision Grandted");
                    Toast.makeText(this, "Permision Grandted", Toast.LENGTH_SHORT).show();
                    this.callNow();
                } else {
                    Log.i(LOG_TAG, "Permission denied!");
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Do something with data (Result returned).
                Toast.makeText(this, "Action OK", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
