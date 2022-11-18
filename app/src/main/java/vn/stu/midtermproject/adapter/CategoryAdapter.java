package vn.stu.midtermproject.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import vn.stu.midtermproject.R;
import vn.stu.midtermproject.model.Category;

public class CategoryAdapter extends ArrayAdapter<Category> {
    Activity context;
    int resource;
    List<Category> objects;

    public CategoryAdapter(@NonNull Activity context, int resource, @NonNull List<Category> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        LayoutInflater inflater = this.context.getLayoutInflater();
        view = inflater.inflate(R.layout.category_item, null);
        TextView tvCategory = view.findViewById(R.id.tvListCategoryName);
        TextView tvCategoryOrigin = view.findViewById(R.id.tvListCategoryOrigin);
        Category category = this.objects.get(position);
        tvCategory.setText(context.getString(R.string.app_product_category_name) + ": " + category.getName());
        tvCategoryOrigin.setText(context.getString(R.string.app_product_category_origin) + ": " + category.getOrigin());
        return view;
    }
}
