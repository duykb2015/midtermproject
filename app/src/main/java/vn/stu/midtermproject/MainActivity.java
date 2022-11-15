package vn.stu.midtermproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import vn.stu.midtermproject.util.DBUtil;


public class MainActivity extends AppCompatActivity {
    long lastPress;
    ImageButton btnProductManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLogin();
        addControls();
        addEvents();
        copyDatabase();
    }

    private void checkLogin() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String username = preferences.getString("username", "");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnAbout:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
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
                                        Toast.makeText(MainActivity.this, "Xoá dữ liệu thành công!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //R.id.lvProduct
        if (v.getId() == 0) {
            getMenuInflater().inflate(R.menu.menu_product, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Integer index = info.position;
        switch (item.getItemId()) {
            case R.id.mnEdit:
                break;
            case R.id.mnDelete:
                new AlertDialog.Builder(this).setTitle("Xác nhận xoá")
                        .setMessage("Bạn có chắc là muốn xoá đơn đặt phòng này?")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                DuLieu.xoaDatPhong(index);
//                                adapter.remove(DuLieu.layDatPhong(index));
//                                adapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Xoá dữ liệu thành công!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNeutralButton("Huỷ", null)
                        .create()
                        .show();
                break;
            default:
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void copyDatabase() {
        DBUtil.copyDBFileFromAssets(MainActivity.this);
    }

    private void addEvents() {
//        btnProductManagement.setOnClickListener(view -> {
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void addControls() {
        btnProductManagement = findViewById(R.id.btnProductManagement);
    }
}