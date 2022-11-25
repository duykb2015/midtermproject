package vn.stu.midtermproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import vn.stu.midtermproject.adapter.ProductAdapter;
import vn.stu.midtermproject.model.Category;
import vn.stu.midtermproject.model.Product;
import vn.stu.midtermproject.util.DBUtil;

public class ProductFragment extends Fragment {

    ListView lvProduct;
    ArrayList<Product> listProduct;
    ProductAdapter adapter;
    String table = "product";

    public ProductFragment() {
        super(R.layout.fragment_product);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listProduct = new ArrayList<>();
        Fragment context = ProductFragment.this;
        adapter = new ProductAdapter(context.requireActivity(), R.layout.product_item, listProduct);
        lvProduct = requireView().findViewById(R.id.lvProduct);
        lvProduct.setAdapter(adapter);
        registerForContextMenu(lvProduct);

        loadProduct();
        addEvents();
    }

    private void loadProduct() {
        adapter.clear();
        SQLiteDatabase database = DBUtil.openOrCreateDataBases(ProductFragment.this);
        Cursor cursor = database.rawQuery("SELECT * FROM product", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Category category = this.getCategory(cursor.getInt(2));
            byte[] bytes = cursor.getBlob(3);
            Bitmap image = null;
            if (bytes != null) {
                image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
            int price = cursor.getInt(4);
            String netWeight = cursor.getString(5);
            Product product = new Product(id, name, category, image, price, netWeight);

            listProduct.add(product);
        }
        adapter.notifyDataSetChanged();
        cursor.close();
        database.close();
    }

    private Category getCategory(int id) {
        SQLiteDatabase database = DBUtil.openOrCreateDataBases(ProductFragment.this);
        Cursor cursor = database.rawQuery("SELECT * FROM category WHERE id = ?", new String[]{id + ""});
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            return null;
        }
        Category category = new Category(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        cursor.close();
        return category;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(0, 1, 0, getString(R.string.app_menu_add)).setShortcut('3', 'c').setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnAbout:
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent2 = new Intent(getActivity(), ProductManagementActivity.class);
                startActivity(intent2);
                break;
            case R.id.mnExit:
                new AlertDialog.Builder(getContext()).setTitle(R.string.app_dialog_title_exit)
                        .setMessage(R.string.app_dialog_message_exit)
                        .setPositiveButton(R.string.app_dialog_cancel, null)
                        .setNeutralButton(
                                R.string.app_dialog_confirm, (dialogInterface, i) -> {
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
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

    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lvProduct) {
            requireActivity().getMenuInflater().inflate(R.menu.menu_product, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        if (item.getItemId() == R.id.mnDelete) {
            new AlertDialog.Builder(getContext()).setTitle(R.string.app_dialog_title_delete)
                    .setMessage(R.string.app_dialog_message_delete)
                    .setPositiveButton(R.string.app_dialog_cancel, null)
                    .setNeutralButton(R.string.app_dialog_confirm, (dialogInterface, i) -> {
                        int productID = listProduct.get(index).getId();
                        deleteProduct(productID);
                        adapter.remove(listProduct.get(index));
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), getString(R.string.app_dialog_success), Toast.LENGTH_SHORT).show();
                    })
                    .create()
                    .show();
        }

        return super.onContextItemSelected(item);
    }

    private void deleteProduct(Integer ID) {
        SQLiteDatabase database = DBUtil.openOrCreateDataBases(ProductFragment.this);
        database.delete(table, "id = ?", new String[]{ID + ""});
    }

    private void addEvents() {
        lvProduct.setOnItemClickListener((adapterView, view, i, l) -> {
            if (i < 0 || i >= listProduct.size()) {
                return;
            }
            int productID = listProduct.get(i).getId();
            Intent intent = new Intent(getActivity(), ProductManagementActivity.class);
            intent.putExtra("product", productID);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProduct();
    }
}