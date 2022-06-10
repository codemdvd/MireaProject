package ru.mirea.apasov.mireaproject;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.GeoObjectTapEvent;
import com.yandex.mapkit.layers.GeoObjectTapListener;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.GeoObjectSelectionMetadata;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.SizeChangedListener;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

import ru.mirea.apasov.mireaproject.databinding.FragmentYandexMapBinding;

public class YandexMap extends Fragment implements UserLocationObjectListener, GeoObjectTapListener, InputListener {
    private FragmentYandexMapBinding binding;
    private UserLocationLayer userLocationLayer;
    private MapView mapView;
    private MapObjectCollection mapObjects;
    private final String MAPKIT_API_KEY = "0940fe6d-32ac-4d94-99c2-56516922fbb8";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){


        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1);
        }
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(requireContext());
        DirectionsFactory.initialize(requireContext());
        binding = FragmentYandexMapBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        mapView = binding.mapview;
        mapView.getMap().move(
                new CameraPosition(new Point(55.751574,
                        37.573856),
                        11.0f,
                        0.0f,
                        0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
        loadUserLocationLayer();
        mapObjects = mapView.getMap().getMapObjects().addCollection();

        mapView.getMap().addTapListener(this);
        mapView.getMap().addInputListener(this);
        setMarks();
        return root;
    }



    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    public void onObjectAdded(@NonNull UserLocationView userLocationView) {
        userLocationLayer.setAnchor(
                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() *
                        0.5)),
                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() *
                        0.83)));
        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                requireContext(),android.R.drawable.star_big_on ));
        userLocationView.getPin().setIcon(ImageProvider.fromResource(
                requireContext(), android.R.drawable.ic_menu_mylocation));
        userLocationView.getAccuracyCircle().setFillColor(Color.BLUE);
    }

    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {

    }

    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView,
                                @NonNull ObjectEvent objectEvent) {

    }

    private void loadUserLocationLayer(){
        MapKit mapKit = MapKitFactory.getInstance();
        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);
    }

    @Override
    public boolean onObjectTap(@NonNull GeoObjectTapEvent geoObjectTapEvent) {
        final GeoObjectSelectionMetadata selectionMetadata = geoObjectTapEvent
                .getGeoObject()
                .getMetadataContainer()
                .getItem(GeoObjectSelectionMetadata.class);

        String text = geoObjectTapEvent.getGeoObject().getName();
        Toast.makeText(requireContext(),
                text,
                Toast.LENGTH_SHORT).show();

        if (selectionMetadata != null) {
            mapView.getMap().selectGeoObject(selectionMetadata.getId(),
                    selectionMetadata.getLayerId());
        }

        return selectionMetadata != null;
    }

        @Override
    public void onMapTap(@NonNull Map map, @NonNull Point point) {
        mapView.getMap().deselectGeoObject();
    }

    @Override
    public void onMapLongTap(@NonNull Map map, @NonNull Point point) {

    }
    private void setMarks(){
        mapObjects = mapView.getMap().getMapObjects().addCollection();
        placeNewMark("Филиал на Вернадского 78",
                "Главное здание",
                "г. Москва, Проспект Вернадского, д. 78",
                new Point(55.669945,
                        37.479492));
        placeNewMark("Филиал на Вернадского 86",
                "Ни разу там не был",
                "г. Москва, Проспект Вернадского, д. 86",
                new Point(55.661445,
                        37.477049));
        placeNewMark("Филиал на Стромынке",
                "Мой филиал",
                "г. Москва, ул. Стромынка, д.20",
                new Point(55.794259,
                        37.701448));
    }
    public void placeNewMark(String name, String description, String address, Point place){
        PlacemarkMapObject mark = mapObjects.addPlacemark(place,
                ImageProvider.fromResource(requireContext(),
                        R.drawable.metka));
        mark.setUserData(new Information(name, description, address));
        mark.addTapListener((mapObject, point) -> {
            Toast.makeText(requireContext(),
                    (mapObject.getUserData().toString()),
                    Toast.LENGTH_SHORT).show();
            return true;
        });
    }
}