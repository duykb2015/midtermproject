package vn.stu.midtermproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import vn.stu.midtermproject.adapter.CategoryAdapter;
import vn.stu.midtermproject.model.Category;
import vn.stu.midtermproject.util.DBUtil;

public class CategoryFragment extends Fragment {
    ListView lvCategory;
    ArrayList<Category> listCategory;
    CategoryAdapter adapter;
    String table = "category";

    public CategoryFragment() {
        super(R.layout.fragment_category);
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
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listCategory = new ArrayList<>();
        Fragment context = CategoryFragment.this;
        adapter = new CategoryAdapter(context.requireActivity(), R.layout.product_item, listCategory);
        lvCategory = requireView().findViewById(R.id.lvCategory);
        lvCategory.setAdapter(adapter);
        registerForContextMenu(lvCategory);

        loadCategory();
        addEvents();
    }

    private void loadCategory() {
        adapter.clear();
        SQLiteDatabase database = DBUtil.openOrCreateDataBases(CategoryFragment.this);
        Cursor cursor = database.rawQuery("SELECT * FROM category", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String origin = cursor.getString(2);
            Category category = new Category(id, name, origin);

            listCategory.add(category);
        }
        adapter.notifyDataSetChanged();
        cursor.close();
        database.close();
    }

    private void addEvents() {
        lvCategory.setOnItemClickListener((adapterView, view, i, l) -> {
            if (i < 0 || i >= listCategory.size()) {
                return;
            }
            int categoryID = listCategory.get(i).getId();
            Intent intent = new Intent(getActivity(), CategoryManagementActivity.class);
            intent.putExtra("category", categoryID);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCategory();
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
                Intent intent2 = new Intent(getActivity(), CategoryManagementActivity.class);
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

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lvCategory) {
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
                        int categoryID = listCategory.get(index).getId();
                        SQLiteDatabase database = DBUtil.openOrCreateDataBases(CategoryFragment.this);
                        Cursor cursor = database.rawQuery("SELECT * FROM product WHERE category_id = ?", new String[]{categoryID + ""});
                        cursor.moveToFirst();
                        if (cursor.getCount() > 0) {
                            Toast.makeText(getContext(), getString(R.string.app_have_product_use), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        deleteCategory(categoryID);
                        adapter.remove(listCategory.get(index));
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), getString(R.string.app_dialog_success), Toast.LENGTH_SHORT).show();
                    })
                    .create()
                    .show();
        }

        return super.onContextItemSelected(item);
    }

    private void deleteCategory(int ID) {
        SQLiteDatabase database = DBUtil.openOrCreateDataBases(CategoryFragment.this);
        database.delete(table, "id = ?", new String[]{ID + ""});
    }
}