package vn.stu.midtermproject.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import vn.stu.midtermproject.R;
import vn.stu.midtermproject.model.Product;

public class ProductAdapter extends ArrayAdapter<Product> {
    Activity context;
    int resource;
    List<Product> objects;

    public ProductAdapter(@NonNull Activity context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        LayoutInflater inflater = this.context.getLayoutInflater();
        view = inflater.inflate(R.layout.product_item, null);
        TextView tvID = view.findViewById(R.id.tvID);
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvCategory = view.findViewById(R.id.tvCategory);
        ImageView ivImage = view.findViewById(R.id.ivProduct);
        Product product = this.objects.get(position);
        tvID.setText(context.getString(R.string.app_product_id) + ": " + product.getId());
        tvName.setText(context.getString(R.string.app_product_name) + ": " + product.getName());
        tvCategory.setText(context.getString(R.string.app_product_category_name) + ": " + product.getCategory().getName());
        ivImage.setImageBitmap(product.getImage());
        return view;
    }
}
