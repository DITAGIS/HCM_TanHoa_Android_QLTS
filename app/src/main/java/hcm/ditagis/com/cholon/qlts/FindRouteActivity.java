package hcm.ditagis.com.cholon.qlts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import hcm.ditagis.com.cholon.qlts.adapter.RouteAdapter;
import hcm.ditagis.com.cholon.qlts.entities.CustomLinearLayout;
import hcm.ditagis.com.cholon.qlts.entities.UserLockBottomSheetBehavior;
import hcm.ditagis.com.cholon.qlts.tools.DirectionFinder;
import hcm.ditagis.com.cholon.qlts.tools.DirectionFinderListener;
import hcm.ditagis.com.cholon.qlts.tools.Route;
import hcm.ditagis.com.cholon.qlts.tools.Step;

public class FindRouteActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener, View.OnClickListener {

    private GoogleMap mMap;
    private EditText etOrigin;
    private EditText etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    LinearLayout layoutBottomSheet;

    BottomSheetBehavior sheetBehavior;

    TextView txtSheetBehavior;
    CustomLinearLayout layoutSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_route);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        layoutBottomSheet = findViewById(R.id.layout_find_route_bottom_sheet);
        txtSheetBehavior = findViewById(R.id.txt_find_route_show_hide);
        layoutSheetBehavior = findViewById(R.id.layout_find_route_show_hide);
        sheetBehavior = UserLockBottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        txtSheetBehavior.setText(getString(R.string.find_route_xem_ban_do));
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        txtSheetBehavior.setText(getString(R.string.find_route_cac_buoc));
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        layoutSheetBehavior.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        break;
                }
                return false;
            }

        });
        layoutSheetBehavior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    txtSheetBehavior.setText("Xem bản đồ");
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                    txtSheetBehavior.setText("Các bước");
                }
            }
        });

        etOrigin = findViewById(R.id.etOrigin);
        etDestination = findViewById(R.id.etDestination);


        findViewById(R.id.btnFindPath).setOnClickListener(this);
        findViewById(R.id.imgBtn_find_route_change_location).setOnClickListener(this);
    }

    private void sendRequest() {
        layoutSheetBehavior.setVisibility(View.GONE);
        layoutBottomSheet.setVisibility(View.GONE);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        String origin = etOrigin.getText().toString();
        String destination = etDestination.getText().toString();
        if (origin.isEmpty()) {
            Toast.makeText(this, getString(R.string.find_route_diem_bat_dau), Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this,getString(R.string.find_route_diem_ket_thuc), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(FindRouteActivity.this, this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng capNuocTH = new LatLng(10.753529, 106.656892);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(capNuocTH, 18));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .title("Cấp nước tân hòa")
                .position(capNuocTH)));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Vui lòng đợi",
                "Đang tìm đường đi..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }


    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        ListView lstViewRoute = layoutBottomSheet.findViewById(R.id.lstView_route);
        RouteAdapter routeAdapter = new RouteAdapter(this, new ArrayList<RouteAdapter.Item>());
        lstViewRoute.setAdapter(routeAdapter);

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            for (Step step : route.steps) {
                RouteAdapter.Item item = new RouteAdapter.Item();
                item.setHtml_instructions(step.html_instructions);
                item.setHtml_sub_instructions(step.html_sub_instructions);
                item.setDistance(step.distance);

                routeAdapter.add(item);
                routeAdapter.notifyDataSetChanged();
            }
            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
        layoutSheetBehavior.setVisibility(View.VISIBLE);
        layoutBottomSheet.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnFindPath:
                sendRequest();
                break;
            case R.id.imgBtn_find_route_change_location:
                String temp = etDestination.getText().toString().trim();
                etDestination.setText(etOrigin.getText().toString().trim());
                etOrigin.setText(temp);
                break;
        }
    }
}
