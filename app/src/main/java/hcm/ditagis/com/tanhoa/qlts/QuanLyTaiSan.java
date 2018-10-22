package hcm.ditagis.com.tanhoa.qlts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.ArcGISRuntimeException;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.ArcGISMapImageSublayer;
import com.esri.arcgisruntime.layers.ArcGISSublayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.UniqueValueRenderer;
import com.esri.arcgisruntime.util.ListenableList;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import hcm.ditagis.com.tanhoa.qlts.adapter.FeatureViewMoreInfoAdapter;
import hcm.ditagis.com.tanhoa.qlts.adapter.ObjectsAdapter;
import hcm.ditagis.com.tanhoa.qlts.adapter.SearchAdapter;
import hcm.ditagis.com.tanhoa.qlts.async.PreparingAsycn;
import hcm.ditagis.com.tanhoa.qlts.async.UpdateAttachmentAsync;
import hcm.ditagis.com.tanhoa.qlts.entities.entitiesDB.LayerInfoDTG;
import hcm.ditagis.com.tanhoa.qlts.entities.entitiesDB.ListObjectDB;
import hcm.ditagis.com.tanhoa.qlts.libs.Action;
import hcm.ditagis.com.tanhoa.qlts.libs.Constants;
import hcm.ditagis.com.tanhoa.qlts.libs.FeatureLayerDTG;
import hcm.ditagis.com.tanhoa.qlts.socket.LocationHelper;
import hcm.ditagis.com.tanhoa.qlts.socket.DApplication;
import hcm.ditagis.com.tanhoa.qlts.tools.MySnackBar;
import hcm.ditagis.com.tanhoa.qlts.tools.SearchItem;
import hcm.ditagis.com.tanhoa.qlts.tools.ThongKe;
import hcm.ditagis.com.tanhoa.qlts.utities.CheckConnectInternet;
import hcm.ditagis.com.tanhoa.qlts.utities.ImageFile;
import hcm.ditagis.com.tanhoa.qlts.utities.MapViewHandler;
import hcm.ditagis.com.tanhoa.qlts.utities.Popup;
import hcm.ditagis.com.tanhoa.qlts.utities.Preference;

public class QuanLyTaiSan extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Uri mUri;
    private Popup popupInfos;
    private MapView mMapView;
    private ArcGISMap mMap;
    private Callout mCallout;
    private FeatureLayerDTG mFeatureLayerDTG;
    private MapViewHandler mMapViewHandler;
    private ListView mListViewSearch;
    private ObjectsAdapter mSearchAdapter;
    private LocationDisplay mLocationDisplay;
    private static final int REQUEST_SEARCH = 2;
    private Point mCurrentPoint;
    private Geocoder mGeocoder;
    private GraphicsOverlay mGraphicsOverlay;
    private boolean isSearchingFeature = false;
    private LinearLayout mLayoutTimKiem;
    private FloatingActionButton mFloatButtonLayer;
    private FloatingActionButton mFloatButtonLocation;
    private CheckBox cb_Layer_HanhChinh, cb_Layer_TaiSan;
    private LinearLayout mLinnearDisplayLayerTaiSan;
    private LinearLayout mLinnearDisplayLayerBaseMap;
    private List<FeatureLayerDTG> mFeatureLayerDTGS;
    private LinearLayout mLinearLayoutCover;
    private ThongKe thongKe;
    private ArcGISMapImageLayer hanhChinhImageLayers, taiSanImageLayers;
    private int states[][];
    private int colors[];

    public void setUri(Uri uri) {
        this.mUri = uri;
    }

    public void setFeatureViewMoreInfoAdapter(FeatureViewMoreInfoAdapter featureViewMoreInfoAdapter) {
        this.mFeatureViewMoreInfoAdapter = featureViewMoreInfoAdapter;
    }

    private FeatureViewMoreInfoAdapter mFeatureViewMoreInfoAdapter;

    public void setSelectedArcGISFeature(ArcGISFeature selectedArcGISFeature) {
        this.mSelectedArcGISFeature = selectedArcGISFeature;
    }

    private ArcGISFeature mSelectedArcGISFeature;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 55;
    private static final int REQUEST_ID_DATA_LOGGER = 22;
    private static final int REQUEST_ID_IMAGE_CAPTURE_POPUP = 44;

    String[] reqPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private LocationHelper mLocationHelper;
    private Location mLocation;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_tai_san);
        startGPS();
        startSignIn();


    }

    private void startGPS() {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationHelper = new LocationHelper(this, new LocationHelper.AsyncResponse() {
            @Override
            public void processFinish(double longtitude, double latitude) {

            }

        });
        mLocationHelper.checkpermission();
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLocation = location;
                ((DApplication) QuanLyTaiSan.this.getApplication()).setmLocation(mLocation);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
//                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(i);
                mLocationHelper.execute();

                mLocationHelper = new LocationHelper(QuanLyTaiSan.this, new LocationHelper.AsyncResponse() {
                    @Override
                    public void processFinish(double longtitude, double latitude) {

                    }

                });
                mLocationHelper.checkpermission();
            }
        };
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, listener);
    }

    private void startSignIn() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, Constants.REQUEST_LOGIN);
    }

    private void setUp() {

        states = new int[][]{{android.R.attr.state_checked}, {}};
        colors = new int[]{hcm.ditagis.com.tanhoa.qlts.R.color.colorTextColor_1, hcm.ditagis.com.tanhoa.qlts.R.color.colorTextColor_1};
        setLicense();
        //for camera
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        // for navigation
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // permisson
        requestPermisson();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, hcm.ditagis.com.tanhoa.qlts.R.string.navigation_drawer_open, hcm.ditagis.com.tanhoa.qlts.R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // location


        // khởi tạo chức năng tìm kiếm
        initListViewSearch();

        // ẩn hiện thị lớp dữ liệu
        initLayerListView();
        initMapView();

    }

    private void initMapView() {
        mMap = new ArcGISMap(Basemap.Type.OPEN_STREET_MAP, 10.7554041, 106.6546293, 12);
        mMapView = findViewById(R.id.mapView);
        mMapView.setMap(mMap);
        final PreparingAsycn preparingAsycn = new PreparingAsycn(this, new PreparingAsycn.AsyncResponse() {
            @Override
            public void processFinish(Void output) {
                setFeatureService();
            }
        });
        if (CheckConnectInternet.isOnline(this))
            preparingAsycn.execute();

        changeStatusOfLocationDataSource();
        mLocationDisplay.addLocationChangedListener(new LocationDisplay.LocationChangedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationChanged(LocationDisplay.LocationChangedEvent locationChangedEvent) {
            }
        });
        mGraphicsOverlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(mGraphicsOverlay);
    }

    private void setFeatureService() {

        // config feature layer service
        mFeatureLayerDTGS = new ArrayList<>();
        mCallout = mMapView.getCallout();
        mMapViewHandler = new MapViewHandler(mMapView, QuanLyTaiSan.this);
        popupInfos = new Popup(QuanLyTaiSan.this, mMapView, mCallout);
        for (final LayerInfoDTG layerInfoDTG : ListObjectDB.getInstance().getLstFeatureLayerDTG()) {
            String url = layerInfoDTG.getUrl();
            if (!layerInfoDTG.getUrl().startsWith("http"))
                url = "http:" + layerInfoDTG.getUrl();
            if(layerInfoDTG.getId() != null && layerInfoDTG.getId().equals("aplucbatthuongTBL")){
                ServiceFeatureTable apLucBatThuongTBL = new ServiceFeatureTable(url);
                popupInfos.setApLucBatThuongTBL(apLucBatThuongTBL);
            }
            if (layerInfoDTG.getId().substring(layerInfoDTG.getId().length() - 3).equals("TBL") || !layerInfoDTG.isView())
                continue;
            if (layerInfoDTG.getId().equals(getString(hcm.ditagis.com.tanhoa.qlts.R.string.IDLayer_Basemap))) {
                hanhChinhImageLayers = new ArcGISMapImageLayer(url);
                hanhChinhImageLayers.setId(layerInfoDTG.getId());
                mMapView.getMap().getOperationalLayers().add(hanhChinhImageLayers);
                hanhChinhImageLayers.addDoneLoadingListener(new Runnable() {
                    @Override
                    public void run() {
                        if (hanhChinhImageLayers.getLoadStatus() == LoadStatus.LOADED) {
                            ListenableList<ArcGISSublayer> sublayerList = hanhChinhImageLayers.getSublayers();
                            for (ArcGISSublayer sublayer : sublayerList) {
                                if (sublayer.getId() == 3) {
                                    String url_HanhChinh = layerInfoDTG.getUrl() + "/" + sublayer.getId();
                                    if (!layerInfoDTG.getUrl().startsWith("http")) {
                                        url_HanhChinh = "http:" + layerInfoDTG.getUrl() + "/" + sublayer.getId();
                                    }
                                    ServiceFeatureTable serviceFeatureTable = new ServiceFeatureTable(url_HanhChinh);
                                    popupInfos.setmSFTHanhChinh(serviceFeatureTable);
                                }
                                addCheckBox_SubLayer((ArcGISMapImageSublayer) sublayer);

                            }

                        }
                    }
                });
                hanhChinhImageLayers.loadAsync();
            } else {
                Action action = new Action(layerInfoDTG.isView(), layerInfoDTG.isCreate(), layerInfoDTG.isEdit(), layerInfoDTG.isDelete());

                ServiceFeatureTable serviceFeatureTable = new ServiceFeatureTable(url);
                final FeatureLayer featureLayer = new FeatureLayer(serviceFeatureTable);
                featureLayer.setName(layerInfoDTG.getTitleLayer());
                featureLayer.setId(layerInfoDTG.getId());
                mMap.getOperationalLayers().add(featureLayer);
                featureLayer.setMaxScale(0);
                featureLayer.setMinScale(1000000);
                FeatureLayerDTG featureLayerDTG = new FeatureLayerDTG(featureLayer);
                if (layerInfoDTG.getId().equals("diemsucoLYR")) {
                    action = new Action(layerInfoDTG.isView(), false, false, false);
                    setRendererSuCoFeatureLayer(featureLayer);
                }
                featureLayerDTG.setAction(action);
                featureLayerDTG.setOutFields(getFieldsDTG(layerInfoDTG.getOutField()));
                featureLayerDTG.setQueryFields(getFieldsDTG(layerInfoDTG.getOutField()));
                featureLayerDTG.setUpdateFields(getFieldsDTG(layerInfoDTG.getOutField()));
                mFeatureLayerDTGS.add(featureLayerDTG);
                featureLayer.addDoneLoadingListener(new Runnable() {
                    @Override
                    public void run() {
                        addCheckBox_TaiSanLayer(featureLayer);
                    }
                });

//            }
            }

        }

        mMapViewHandler.setmPopUp(popupInfos);
        mMapViewHandler.setFeatureLayerDTGs(mFeatureLayerDTGS);
        thongKe = new ThongKe(this, mFeatureLayerDTGS);
        final TextView txtToaDo = ((TextView) findViewById(R.id.txt_toado));
        mMap.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                mLinnearDisplayLayerTaiSan = findViewById(R.id.linnearDisplayLayerTaiSan);
                mLinnearDisplayLayerBaseMap = findViewById(R.id.linnearDisplayLayerBaseMap);
            }
        });
        mMapView.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mMapView) {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                try {
                    mMapViewHandler.onSingleTapMapView(e);
                } catch (ArcGISRuntimeException ex) {
                    Log.d("", ex.toString());
                }
                return super.onSingleTapConfirmed(e);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (mMapViewHandler != null) {
                    double[] location = mMapViewHandler.onScroll(e1, e2, distanceX, distanceY);
                    float log = (float) Math.round(location[0] * 100000) / 100000;
                    float lat = (float) Math.round(location[1] * 100000) / 100000;
                    txtToaDo.setText(lat + ", " + log);
//                    edit_latitude.setText(location[1] + "");
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                return super.onScale(detector);
            }
        });
        SeekBar skbr_hanhchinh_layer = findViewById(R.id.skbr_hanhchinh_layers);
        skbr_hanhchinh_layer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                hanhChinhImageLayers.setOpacity((float) i / 100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private String[] getFieldsDTG(String stringFields) {
        String[] returnFields = null;
        if (stringFields != null) {
            if (stringFields == "*") {
                returnFields = new String[]{"*"};
            } else {
                returnFields = stringFields.split(",");
            }

        }
        return returnFields;
    }

    private void initLayerListView() {

        findViewById(R.id.layout_layer_open_street_map).setOnClickListener(this);
        findViewById(R.id.layout_layer_street_map).setOnClickListener(this);
        findViewById(R.id.layout_layer_topo).setOnClickListener(this);
        mFloatButtonLayer = findViewById(R.id.floatBtnLayer);
        mFloatButtonLayer.setOnClickListener(this);
        mLinearLayoutCover = findViewById(R.id.layout_cover_quan_ly_su_co);
        mLinearLayoutCover.setOnClickListener(this);
        findViewById(R.id.btn_layer_close).setOnClickListener(this);
        mFloatButtonLocation = findViewById(R.id.floatBtnLocation);
        mFloatButtonLocation.setOnClickListener(this);
        mLayoutTimKiem = findViewById(R.id.layout_tim_kiem);

        cb_Layer_HanhChinh = findViewById(R.id.cb_Layer_HanhChinh);
        cb_Layer_TaiSan = findViewById(R.id.cb_Layer_TaiSan);
        cb_Layer_TaiSan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < mLinnearDisplayLayerTaiSan.getChildCount(); i++) {
                    View view = mLinnearDisplayLayerTaiSan.getChildAt(i);
                    if (view instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) view;
                        if (isChecked) checkBox.setChecked(true);
                        else checkBox.setChecked(false);
                    }
                }
            }


        });
        cb_Layer_HanhChinh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < mLinnearDisplayLayerBaseMap.getChildCount(); i++) {
                    View view = mLinnearDisplayLayerBaseMap.getChildAt(i);
                    if (view instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) view;
                        if (isChecked) checkBox.setChecked(true);
                        else checkBox.setChecked(false);
                    }
                }
            }


        });
    }

    private void initListViewSearch() {
        this.mListViewSearch = findViewById(R.id.lstview_search);
        //đưa listview search ra phía sau
        this.mListViewSearch.invalidate();
        List<ObjectsAdapter.Item> items = new ArrayList<>();
        this.mSearchAdapter = new ObjectsAdapter(QuanLyTaiSan.this, items);
        this.mListViewSearch.setAdapter(mSearchAdapter);
        this.mListViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ObjectsAdapter.Item item = ((ObjectsAdapter.Item) parent.getItemAtPosition(position));
                int objectID = Integer.parseInt(item.getObjectID());
                if (objectID != -1) {
                    mMapViewHandler.queryByObjectID(objectID);
                    mSearchAdapter.clear();
                    mSearchAdapter.notifyDataSetChanged();
                }
                //tìm kiếm địa chỉ
                else {
                    setViewPointCenterLongLat(new Point(item.getLongtitude(), item.getLatitude()));
                }
            }
        });
    }

    private void setLicense() {
        //way 1
        ArcGISRuntimeEnvironment.setLicense(getString(hcm.ditagis.com.tanhoa.qlts.R.string.license));
    }

    private void addCheckBox_SubLayer(final ArcGISMapImageSublayer layer) {
        final CheckBox checkBox = new CheckBox(mLinnearDisplayLayerBaseMap.getContext());
        checkBox.setText(layer.getName());
        checkBox.setChecked(false);
        layer.setVisible(false);
        CompoundButtonCompat.setButtonTintList(checkBox, new ColorStateList(states, colors));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (checkBox.isChecked()) {
                    if (buttonView.getText().equals(layer.getName()))
                        layer.setVisible(true);


                } else {
                    if (checkBox.getText().equals(layer.getName()))
                        layer.setVisible(false);
                }
            }
        });
        mLinnearDisplayLayerBaseMap.addView(checkBox);
    }

    private void addCheckBox_TaiSanLayer(final FeatureLayer featureLayer) {
        final CheckBox checkBox = new CheckBox(mLinnearDisplayLayerTaiSan.getContext());
        checkBox.setText(featureLayer.getName());
        checkBox.setChecked(false);
        featureLayer.setVisible(false);
        CompoundButtonCompat.setButtonTintList(checkBox, new ColorStateList(states, colors));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (checkBox.isChecked()) {
                    if (buttonView.getText().equals(featureLayer.getName()))
                        featureLayer.setVisible(true);
                } else {
                    if (checkBox.getText().equals(featureLayer.getName()))
                        featureLayer.setVisible(false);
                }
            }
        });
        mLinnearDisplayLayerTaiSan.addView(checkBox);
    }

    private void changeStatusOfLocationDataSource() {
        mLocationDisplay = mMapView.getLocationDisplay();
//        changeStatusOfLocationDataSource();
        mLocationDisplay.addDataSourceStatusChangedListener(new LocationDisplay.DataSourceStatusChangedListener() {
            @Override
            public void onStatusChanged(LocationDisplay.DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {

                // If LocationDisplay started OK, then continue.
                if (dataSourceStatusChangedEvent.isStarted()) return;

                // No error is reported, then continue.
                if (dataSourceStatusChangedEvent.getError() == null) return;

                // If an error is found, handle the failure to start.
                // Check permissions to see if failure may be due to lack of permissions.
                boolean permissionCheck1 = ContextCompat.checkSelfPermission(QuanLyTaiSan.this, reqPermissions[0]) == PackageManager.PERMISSION_GRANTED;
                boolean permissionCheck2 = ContextCompat.checkSelfPermission(QuanLyTaiSan.this, reqPermissions[1]) == PackageManager.PERMISSION_GRANTED;

                if (!(permissionCheck1 && permissionCheck2)) {
                    // If permissions are not already granted, request permission from the user.
                    ActivityCompat.requestPermissions(QuanLyTaiSan.this, reqPermissions, REQUEST_SEARCH);
                }  // Report other unknown failure types to the user - for example, location services may not // be enabled on the device. //                    String message = String.format("Error in DataSourceStatusChangedListener: %s", dataSourceStatusChangedEvent //                            .getSource().getLocationDataSource().getError().getMessage()); //                    Toast.makeText(QuanLyTaiSan.this, message, Toast.LENGTH_LONG).show();

            }
        });
    }

    private void setRendererSuCoFeatureLayer(FeatureLayer featureLayer) {
        UniqueValueRenderer uniqueValueRenderer = new UniqueValueRenderer();
        uniqueValueRenderer.getFieldNames().add("TrangThai");
        uniqueValueRenderer.getFieldNames().add("HinhThucPhatHien");


        PictureMarkerSymbol chuaXuLySymbol = new PictureMarkerSymbol(getString(R.string.url_image_symbol_chuasuachua));
        chuaXuLySymbol.setHeight(getResources().getInteger(R.integer.size_feature_renderer));
        chuaXuLySymbol.setWidth(getResources().getInteger(R.integer.size_feature_renderer));

        PictureMarkerSymbol dangXuLySymbol = new PictureMarkerSymbol(getString(R.string.url_image_symbol_dangsuachua));
        dangXuLySymbol.setHeight(getResources().getInteger(R.integer.size_feature_renderer));
        dangXuLySymbol.setWidth(getResources().getInteger(R.integer.size_feature_renderer));

        PictureMarkerSymbol hoanThanhSymBol = new PictureMarkerSymbol(getString(R.string.url_image_symbol_hoanthanh));
        hoanThanhSymBol.setHeight(getResources().getInteger(R.integer.size_feature_renderer));
        hoanThanhSymBol.setWidth(getResources().getInteger(R.integer.size_feature_renderer));

        PictureMarkerSymbol beNgamSymbol = new PictureMarkerSymbol(getString(R.string.url_image_symbol_beNgam));
        beNgamSymbol.setHeight(getResources().getInteger(R.integer.size_feature_renderer));
        beNgamSymbol.setWidth(getResources().getInteger(R.integer.size_feature_renderer));

        uniqueValueRenderer.setDefaultSymbol(chuaXuLySymbol);
        uniqueValueRenderer.setDefaultLabel("Chưa xác định");

        List<Object> chuaXuLyValue = new ArrayList<>();
        chuaXuLyValue.add(0);

        //đang xử lý: begin
        List<Object> dangXuLyValue = new ArrayList<>();
        dangXuLyValue.add(1);
        dangXuLyValue.add(1);
        List<Object> dangXuLyValue1 = new ArrayList<>();
        dangXuLyValue1.add(1);
        dangXuLyValue1.add(2);

        List<Object> dangXuLyValue2 = new ArrayList<>();
        dangXuLyValue2.add(1);
        dangXuLyValue2.add(3);

        List<Object> dangXuLyValue3 = new ArrayList<>();
        dangXuLyValue3.add(1);
        dangXuLyValue3.add(4);

        List<Object> dangXuLyValue4 = new ArrayList<>();
        dangXuLyValue4.add(1);
        dangXuLyValue4.add(5);

        List<Object> dangXuLyValue5 = new ArrayList<>();
        dangXuLyValue5.add(1);
        dangXuLyValue5.add(6);
        //đang xỷ lý: end

        List<Object> beNgamChuaXuLyValue = new ArrayList<>();
        beNgamChuaXuLyValue.add(0);
        beNgamChuaXuLyValue.add(1);

        //hoàn thành: begin
        List<Object> hoanThanhValue = new ArrayList<>();
        hoanThanhValue.add(3);
        hoanThanhValue.add(1);
        List<Object> hoanThanhValue1 = new ArrayList<>();
        hoanThanhValue1.add(3);
        hoanThanhValue1.add(2);

        List<Object> hoanThanhValue2 = new ArrayList<>();
        hoanThanhValue2.add(3);
        hoanThanhValue2.add(3);

        List<Object> hoanThanhValue3 = new ArrayList<>();
        hoanThanhValue3.add(3);
        hoanThanhValue3.add(4);

        List<Object> hoanThanhValue4 = new ArrayList<>();
        hoanThanhValue4.add(3);
        hoanThanhValue4.add(5);

        List<Object> hoanThanhValue5 = new ArrayList<>();
        hoanThanhValue5.add(3);
        hoanThanhValue5.add(6);
        //hoàn thành: end

        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Chưa xử lý", "Chưa xử lý", chuaXuLySymbol, chuaXuLyValue));

        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Đang xử lý", "Đang xử lý", dangXuLySymbol, dangXuLyValue));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Đang xử lý", "Đang xử lý", dangXuLySymbol, dangXuLyValue1));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Đang xử lý", "Đang xử lý", dangXuLySymbol, dangXuLyValue2));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Đang xử lý", "Đang xử lý", dangXuLySymbol, dangXuLyValue3));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Đang xử lý", "Đang xử lý", dangXuLySymbol, dangXuLyValue4));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Đang xử lý", "Đang xử lý", dangXuLySymbol, dangXuLyValue5));

        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Chưa xử lý bể ngầm", "Chưa xử lý bể ngầm", beNgamSymbol, beNgamChuaXuLyValue));

        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Hoàn thành", "Hoàn thành", hoanThanhSymBol, hoanThanhValue));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Hoàn thành", "Hoàn thành", hoanThanhSymBol, hoanThanhValue1));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Hoàn thành", "Hoàn thành", hoanThanhSymBol, hoanThanhValue2));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Hoàn thành", "Hoàn thành", hoanThanhSymBol, hoanThanhValue3));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Hoàn thành", "Hoàn thành", hoanThanhSymBol, hoanThanhValue4));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Hoàn thành", "Hoàn thành", hoanThanhSymBol, hoanThanhValue5));
        featureLayer.setRenderer(uniqueValueRenderer);
        featureLayer.loadAsync();


    }

    private void setViewPointCenter(Point position) {
        Geometry geometry = GeometryEngine.project(position, SpatialReferences.getWebMercator());
        mMapView.setViewpointCenterAsync(geometry.getExtent().getCenter());
    }

    private void setViewPointCenterLongLat(Point position) {
        Geometry geometry = GeometryEngine.project(position, SpatialReferences.getWgs84());
        Geometry geometry1 = GeometryEngine.project(geometry, SpatialReferences.getWebMercator());
        Point point = geometry1.getExtent().getCenter();

        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CROSS, Color.RED, 20);
        Graphic graphic = new Graphic(point, symbol);
        mGraphicsOverlay.getGraphics().add(graphic);

        mMapView.setViewpointCenterAsync(point);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(hcm.ditagis.com.tanhoa.qlts.R.menu.quan_ly_su_co, menu);
        final SearchView mTxtSearch = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mTxtSearch.setQueryHint(getString(hcm.ditagis.com.tanhoa.qlts.R.string.title_search));
        mTxtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                mTxtSearch.clearFocus();
                if (isSearchingFeature)
                    mMapViewHandler.querySearch(query, mListViewSearch, mSearchAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    mSearchAdapter.clear();
                    mSearchAdapter.notifyDataSetChanged();
                } else if (!isSearchingFeature) {

                    try {
                        mSearchAdapter.clear();
                        List<Address> addressList = mGeocoder.getFromLocationName(newText, 1);
                        for (Address address : addressList) {
                            ObjectsAdapter.Item item = new ObjectsAdapter.Item("-1", "", address.getAddressLine(0));
                            item.setLatitude(address.getLatitude());
                            item.setLongtitude(address.getLongitude());
                            mSearchAdapter.add(item);
                        }
                        mSearchAdapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        findViewById(R.id.img_clearSelectLayer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) findViewById(R.id.txt_title_search)).setText(getString(hcm.ditagis.com.tanhoa.qlts.R.string.nav_find_address));
                isSearchingFeature = false;
            }
        });
        findViewById(R.id.img_selectLayer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogSelectTypeSearch();
            }
        });
        menu.findItem(R.id.action_search).setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                hiddenFloatButton();
                mLayoutTimKiem.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                showFloatButton();
                mLayoutTimKiem.setVisibility(View.INVISIBLE);
                return true;
            }
        });
        return true;
    }

    private void showDialogSelectTypeSearch() {
        SearchItem searchItem = new SearchItem(mFeatureLayerDTGS, this);
        List<SearchAdapter.Item> items = searchItem.getItems();
        SearchAdapter searchAdapter = new SearchAdapter(this, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
        @SuppressLint("InflateParams") View layout = getLayoutInflater().inflate(hcm.ditagis.com.tanhoa.qlts.R.layout.layout_title_listview, null);
        ListView listView = layout.findViewById(R.id.listview);
        listView.setAdapter(searchAdapter);
        TextView txt_Title_Layout = layout.findViewById(R.id.txt_Title_Layout);
        txt_Title_Layout.setText("Tìm kiếm theo");
        builder.setView(layout);
        final AlertDialog selectTimeDialog = builder.create();
        selectTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectTimeDialog.show();
        final List<SearchAdapter.Item> finalItems = searchAdapter.getItems();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectTimeDialog.dismiss();
                final SearchAdapter.Item itemAtPosition = (SearchAdapter.Item) parent.getItemAtPosition(position);
                String idLayer = itemAtPosition.getIdLayer();
                ((TextView) findViewById(R.id.txt_title_search)).setText(itemAtPosition.getTitleLayer());
                ServiceFeatureTable serviceFeatureTable = getServiceFeatureTable(idLayer);
                serviceFeatureTable.getFeatureLayer().setVisible(true);
                mMapViewHandler.setSearchSFT(serviceFeatureTable);
                isSearchingFeature = true;
            }
        });
    }

    private ServiceFeatureTable getServiceFeatureTable(String idLayer) {
        for (FeatureLayerDTG featureLayerDTG : mFeatureLayerDTGS) {
            String id = featureLayerDTG.getFeatureLayer().getId();
            if (id.equals(idLayer))
                return (ServiceFeatureTable) featureLayerDTG.getFeatureLayer().getFeatureTable();
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_search:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_thongke:
                thongKe.start();
                break;
            case R.id.nav_datalogger:
                intent = new Intent(this, DataLoggerActivity.class);
                startActivityForResult(intent, REQUEST_ID_DATA_LOGGER);
                break;
            case R.id.nav_tracuu:
//                traCuu.start();
                break;
            case R.id.nav_find_route:
                intent = new Intent(this, FindRouteActivity.class);
                this.startActivity(intent);
                break;
            case R.id.nav_visible_float_button:
                toogleFloatButton();
                break;
            case R.id.nav_logOut:
               startSignIn();
                break;
            case R.id.nav_delete_searching:
                mGraphicsOverlay.getGraphics().clear();
                mSearchAdapter.clear();
                mSearchAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void requestPermisson() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE}, REQUEST_ID_IMAGE_CAPTURE);
        }
//        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this,
//                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
//                this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mLocationDisplay.startAsync();

        } else {
            Toast.makeText(QuanLyTaiSan.this, getResources().getString(hcm.ditagis.com.tanhoa.qlts.R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
        }
    }

    private void hiddenFloatButton() {
        findViewById(R.id.floatBtnLayer).setVisibility(View.INVISIBLE);
        findViewById(R.id.floatBtnLocation).setVisibility(View.INVISIBLE);
    }

    private void showFloatButton() {
        findViewById(R.id.floatBtnLayer).setVisibility(View.VISIBLE);
        findViewById(R.id.floatBtnLocation).setVisibility(View.VISIBLE);
    }

    private void toogleFloatButton() {
        if (findViewById(R.id.floatBtnLayer).getVisibility() == View.VISIBLE) {
            findViewById(R.id.floatBtnLayer).setVisibility(View.INVISIBLE);
        } else
            findViewById(R.id.floatBtnLayer).setVisibility(View.VISIBLE);
        if (findViewById(R.id.floatBtnLocation).getVisibility() == View.VISIBLE) {
            findViewById(R.id.floatBtnLocation).setVisibility(View.INVISIBLE);
        } else
            findViewById(R.id.floatBtnLocation).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatBtnLayer:
                toogleFloatButton();
                findViewById(R.id.layout_layer).setVisibility(View.VISIBLE);
                mCurrentPoint = mMapView.getCurrentViewpoint(Viewpoint.Type.CENTER_AND_SCALE).getTargetGeometry().getExtent().getCenter();
                break;
            case R.id.layout_layer_open_street_map:
                mMapView.getMap().setMaxScale(1128.497175);
                mMapView.getMap().setBasemap(Basemap.createOpenStreetMap());
                handlingColorBackgroundLayerSelected(R.id.layout_layer_open_street_map);
                mMapView.getCurrentViewpoint(Viewpoint.Type.CENTER_AND_SCALE);

                setViewPointCenter(mCurrentPoint);
                break;
            case R.id.layout_layer_street_map:
                mMapView.getMap().setMaxScale(1128.497176);
                mMapView.getMap().setBasemap(Basemap.createStreets());
                handlingColorBackgroundLayerSelected(R.id.layout_layer_street_map);

                setViewPointCenter(mCurrentPoint);
                break;
            case R.id.layout_layer_topo:
                mMapView.getMap().setMaxScale(5);
                mMapView.getMap().setBasemap(Basemap.createImageryWithLabels());
                handlingColorBackgroundLayerSelected(R.id.layout_layer_topo);

                setViewPointCenter(mCurrentPoint);
                break;
            case R.id.btn_layer_close:
                findViewById(R.id.layout_layer).setVisibility(View.INVISIBLE);
                toogleFloatButton();
                break;
            case R.id.layout_cover_quan_ly_su_co:
                break;
            case R.id.floatBtnLocation:
                if (!mLocationDisplay.isStarted()) {
                    mLocationDisplay.startAsync();
                    setViewPointCenter(mLocationDisplay.getMapLocation());
                } else mLocationDisplay.stop();
                break;
        }
    }

    public void capture() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());

        File photo = ImageFile.getFile(this);
//        this.mUri= FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".my.package.name.provider", photo);
        this.mUri = Uri.fromFile(photo);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, this.mUri);
//        this.mUri = Uri.fromFile(photo);
        startActivityForResult(cameraIntent, REQUEST_ID_IMAGE_CAPTURE);
    }

    @Nullable
    private Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            assert in != null;
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b;
            in = getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x, (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            assert in != null;
            in.close();

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " + b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }

    @SuppressLint("ResourceAsColor")
    private void handlingColorBackgroundLayerSelected(int id) {
        switch (id) {
            case R.id.layout_layer_open_street_map:
                findViewById(R.id.img_layer_open_street_map).setBackgroundResource(hcm.ditagis.com.tanhoa.qlts.R.drawable.layout_shape_basemap);
                ((TextView) findViewById(R.id.txt_layer_open_street_map)).setTextColor(ContextCompat.getColor(this, hcm.ditagis.com.tanhoa.qlts.R.color.colorPrimary));
                findViewById(R.id.img_layer_street_map).setBackgroundResource(hcm.ditagis.com.tanhoa.qlts.R.drawable.layout_shape_basemap_none);
                ((TextView) findViewById(R.id.txt_layer_street_map)).setTextColor(ContextCompat.getColor(this, hcm.ditagis.com.tanhoa.qlts.R.color.colorBlack));
                findViewById(R.id.img_layer_topo).setBackgroundResource(hcm.ditagis.com.tanhoa.qlts.R.drawable.layout_shape_basemap_none);
                ((TextView) findViewById(R.id.txt_layer_topo)).setTextColor(ContextCompat.getColor(this, hcm.ditagis.com.tanhoa.qlts.R.color.colorBlack));
                break;
            case R.id.layout_layer_street_map:
                findViewById(R.id.img_layer_street_map).setBackgroundResource(hcm.ditagis.com.tanhoa.qlts.R.drawable.layout_shape_basemap);
                ((TextView) findViewById(R.id.txt_layer_street_map)).setTextColor(ContextCompat.getColor(this, hcm.ditagis.com.tanhoa.qlts.R.color.colorPrimary));
                findViewById(R.id.img_layer_open_street_map).setBackgroundResource(hcm.ditagis.com.tanhoa.qlts.R.drawable.layout_shape_basemap_none);
                ((TextView) findViewById(R.id.txt_layer_open_street_map)).setTextColor(ContextCompat.getColor(this, hcm.ditagis.com.tanhoa.qlts.R.color.colorBlack));
                findViewById(R.id.img_layer_topo).setBackgroundResource(hcm.ditagis.com.tanhoa.qlts.R.drawable.layout_shape_basemap_none);
                ((TextView) findViewById(R.id.txt_layer_topo)).setTextColor(ContextCompat.getColor(this, hcm.ditagis.com.tanhoa.qlts.R.color.colorBlack));
                break;
            case R.id.layout_layer_topo:
                findViewById(R.id.img_layer_topo).setBackgroundResource(hcm.ditagis.com.tanhoa.qlts.R.drawable.layout_shape_basemap);
                ((TextView) findViewById(R.id.txt_layer_topo)).setTextColor(ContextCompat.getColor(this, hcm.ditagis.com.tanhoa.qlts.R.color.colorPrimary));
                findViewById(R.id.img_layer_open_street_map).setBackgroundResource(hcm.ditagis.com.tanhoa.qlts.R.drawable.layout_shape_basemap_none);
                ((TextView) findViewById(R.id.txt_layer_open_street_map)).setTextColor(ContextCompat.getColor(this, hcm.ditagis.com.tanhoa.qlts.R.color.colorBlack));
                findViewById(R.id.img_layer_street_map).setBackgroundResource(hcm.ditagis.com.tanhoa.qlts.R.drawable.layout_shape_basemap_none);
                ((TextView) findViewById(R.id.txt_layer_street_map)).setTextColor(ContextCompat.getColor(this, hcm.ditagis.com.tanhoa.qlts.R.color.colorBlack));
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {

            switch (requestCode) {
                case REQUEST_ID_DATA_LOGGER:
                    if (resultCode == Activity.RESULT_OK) {
                        mMapViewHandler.queryObjectByLayerID_ObjectID();
                    }
                    break;
                case Constants.REQUEST_LOGIN:
                    if (Activity.RESULT_OK != resultCode) {
                        finish();
                        return;
                    } else {
                        mGeocoder = new Geocoder(this);
                        // create an empty map instance
                        setUp();
                        ((TextView) QuanLyTaiSan.this.findViewById(R.id.txt_nav_username)).setText(Preference.getInstance().
                                loadPreference(getString(R.string.preference_username)));
                        ((TextView) QuanLyTaiSan.this.findViewById(R.id.txt_nav_displayname)).setText(Preference.getInstance().
                                loadPreference(getString(R.string.preference_displayname)));
                    }

            }
        } catch (Exception ignored) {
        }

        if (requestCode == REQUEST_ID_IMAGE_CAPTURE)

        {
            if (resultCode == RESULT_OK) {
                if (this.mUri != null) {
//                    Uri selectedImage = this.mUri;
//                    getContentResolver().notifyChange(selectedImage, null);
                    Bitmap bitmap = getBitmap(mUri.getPath());
                    try {
                        if (bitmap != null) {
                            Matrix matrix = new Matrix();
                            matrix.postRotate(90);
                            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                            byte[] image = outputStream.toByteArray();
                            Toast.makeText(this, "Đã lưu ảnh", Toast.LENGTH_SHORT).show();
                            mMapViewHandler.addFeature(image);
                        }
                    } catch (Exception ignored) {
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                MySnackBar.make(mMapView, "Hủy chụp ảnh", false);
            } else {
                MySnackBar.make(mMapView, "Lỗi khi chụp ảnh", false);
            }
        } else if (requestCode == getResources().getInteger(hcm.ditagis.com.tanhoa.qlts.R.integer.REQUEST_ID_UPDATE_ATTACHMENT)) {
            if (resultCode == RESULT_OK) {
                if (this.mUri != null) {
                    Bitmap bitmap = getBitmap(mUri.getPath());
                    try {
                        if (bitmap != null) {
                            Matrix matrix = new Matrix();
                            matrix.postRotate(90);
                            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                            byte[] image = outputStream.toByteArray();
                            Toast.makeText(this, "Đã lưu ảnh", Toast.LENGTH_SHORT).show();
                            UpdateAttachmentAsync updateAttachmentAsync = new UpdateAttachmentAsync(this, mSelectedArcGISFeature, image);
                            updateAttachmentAsync.execute();
                        }
                    } catch (Exception ignored) {
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                MySnackBar.make(mMapView, "Hủy chụp ảnh", false);
            } else {
                MySnackBar.make(mMapView, "Lỗi khi chụp ảnh", false);
            }
        }
    }
}