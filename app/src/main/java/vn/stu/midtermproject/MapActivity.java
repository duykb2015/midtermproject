package vn.stu.midtermproject;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.EdgeInsets;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        addControls();
    }

    private void addControls() {
        mapView = findViewById(R.id.mapViews);
        mapView.getMapboxMap().loadStyleUri(Style.SATELLITE, style -> xuLyNapBanDo());
    }

    private void xuLyNapBanDo() {
        Point pointSTU = Point.fromLngLat(106.67794694095484, 10.738242625107251);
        Point pointRMIT = Point.fromLngLat(106.69303983011709, 10.729919508366823);
        Point pointUNKNOW = Point.fromLngLat(106.6931844327292, 10.746457680880287);

        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
        PointAnnotationManager manager = PointAnnotationManagerKt.createPointAnnotationManager(
                annotationPlugin,
                new AnnotationConfig()
        );
        PointAnnotationOptions optionsSTU = new PointAnnotationOptions()
                .withPoint(pointSTU)
                .withTextField("STU")
                .withIconImage(BitmapFactory.decodeResource(this.getResources(), R.drawable.red_marker));
        manager.create(optionsSTU);
        PointAnnotationOptions optionsRMIT = new PointAnnotationOptions()
                .withPoint(pointRMIT)
                .withTextField("RMIT")
                .withIconImage(BitmapFactory.decodeResource(this.getResources(), R.drawable.red_marker));
        manager.create(optionsRMIT);
        PointAnnotationOptions optionsUNKNOW = new PointAnnotationOptions()
                .withPoint(pointUNKNOW)
                .withTextField("Equilateral Triangle")
                .withIconImage(BitmapFactory.decodeResource(this.getResources(), R.drawable.red_marker));
        manager.create(optionsUNKNOW);

        List<Point> points = new ArrayList<>();
        points.add(pointSTU);
        points.add(pointRMIT);
        points.add(pointUNKNOW);
        List<List<Point>> coordinates = new ArrayList<>();
        coordinates.add(points);
        Polygon polygon = Polygon.fromLngLats(coordinates);
        EdgeInsets edgeInsets = new EdgeInsets(100.0, 100.0, 100.0, 100.0);
        CameraOptions cameraOptions = mapView.getMapboxMap().cameraForGeometry(
                polygon,
                edgeInsets,
                0.0,
                0.0
        );
        mapView.getMapboxMap().setCamera(cameraOptions);
    }
}
