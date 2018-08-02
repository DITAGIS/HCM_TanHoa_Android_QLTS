package hcm.ditagis.com.tanhoa.qlts.utities;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.widget.ListView;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.ArcGISFeatureTable;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import hcm.ditagis.com.tanhoa.qlts.QuanLyTaiSan;
import hcm.ditagis.com.tanhoa.qlts.adapter.ObjectsAdapter;
import hcm.ditagis.com.tanhoa.qlts.async.SingleTapAddFeatureAsync;
import hcm.ditagis.com.tanhoa.qlts.async.SingleTapMapViewAsync;
import hcm.ditagis.com.tanhoa.qlts.libs.FeatureLayerDTG;


/**
 * Created by ThanLe on 2/2/2018.
 */

public class MapViewHandler extends Activity {

    private static final int REQUEST_ID_IMAGE_CAPTURE = 1;
    private static double DELTA_MOVE_Y = 0;//7000;
    private final ArcGISMap mMap;
    LocatorTask loc = new LocatorTask("http://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer");
    private FeatureLayerDTG mFeatureLayerDTG;
    private android.graphics.Point mClickPoint;
    private ArcGISFeature mSelectedArcGISFeature;
    private MapView mMapView;
    private boolean isClickBtnAdd = false;
    private ServiceFeatureTable searchSFT;
    private Popup mPopUp;
    private Context mContext;


    public void setFeatureLayerDTGs(List<FeatureLayerDTG> mFeatureLayerDTGs) {
        this.mFeatureLayerDTGs = mFeatureLayerDTGs;
    }

    public void setmPopUp(Popup mPopUp) {
        this.mPopUp = mPopUp;
    }

    private List<FeatureLayerDTG> mFeatureLayerDTGs;

    public MapViewHandler(MapView mMapView, QuanLyTaiSan quanLyTaiSan) {
        this.mMapView = mMapView;
        this.mContext = quanLyTaiSan;
        this.mMap = mMapView.getMap();
    }

    public ServiceFeatureTable getSearchSFT() {
        return searchSFT;
    }

    public void setSearchSFT(ServiceFeatureTable searchSFT) {
        this.searchSFT = searchSFT;
    }

    public void setClickBtnAdd(boolean clickBtnAdd) {
        isClickBtnAdd = clickBtnAdd;
    }

    public void addFeature(byte[] image) {
        SingleTapAddFeatureAsync singleTapAdddFeatureAsync = new SingleTapAddFeatureAsync(mContext, image, searchSFT, loc, mMapView);
        Point add_point = mMapView.getCurrentViewpoint(Viewpoint.Type.CENTER_AND_SCALE).getTargetGeometry().getExtent().getCenter();
        singleTapAdddFeatureAsync.execute(add_point);
    }

    public void onSingleTapMapView(MotionEvent e) {
        final Point clickPoint = mMapView.screenToLocation(new android.graphics.Point(Math.round(e.getX()), Math.round(e.getY())));
        mClickPoint = new android.graphics.Point((int) e.getX(), (int) e.getY());
        SingleTapMapViewAsync singleTapMapViewAsync = new SingleTapMapViewAsync(mContext, mFeatureLayerDTGs, mPopUp, mClickPoint, mMapView);
        singleTapMapViewAsync.execute(clickPoint);
    }

    public double[] onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Point center = ((MapView) mMapView).getCurrentViewpoint(Viewpoint.Type.CENTER_AND_SCALE).getTargetGeometry().getExtent().getCenter();
        Geometry project = GeometryEngine.project(center, SpatialReferences.getWgs84());
        double[] location = {project.getExtent().getCenter().getX(), project.getExtent().getCenter().getY()};
//        Geometry geometry = GeometryEngine.project(project, SpatialReferences.getWebMercator());
        return location;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getDateString() {
        String timeStamp = Constant.DATE_FORMAT.format(Calendar.getInstance().getTime());

        SimpleDateFormat writeDate = new SimpleDateFormat("dd_MM_yyyy HH:mm:ss");
        writeDate.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        String timeStamp1 = writeDate.format(Calendar.getInstance().getTime());
        return timeStamp1;
    }

    private String getTimeID() {
        String timeStamp = Constant.DATE_FORMAT.format(Calendar.getInstance().getTime());
        return timeStamp;
    }

    public void queryByObjectID(int objectID) {
        final QueryParameters queryParameters = new QueryParameters();
        final String query = "OBJECTID = " + objectID;
        queryParameters.setWhereClause(query);
        final ListenableFuture<FeatureQueryResult> feature = searchSFT.queryFeaturesAsync(queryParameters,ServiceFeatureTable.QueryFeatureFields.LOAD_ALL);
        feature.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult result = feature.get();
                    if (result.iterator().hasNext()) {
                        mSelectedArcGISFeature = (ArcGISFeature) result.iterator().next();
                        if (mSelectedArcGISFeature != null) {
                            String tableName = ((ArcGISFeatureTable) mSelectedArcGISFeature.getFeatureTable()).getTableName();
                            FeatureLayerDTG featureLayerDTG = getmFeatureLayerDTG(tableName);
                            mPopUp.setFeatureLayerDTG(featureLayerDTG);
                            mPopUp.showPopup(mSelectedArcGISFeature,false);
                        } else mPopUp.dimissCallout();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public FeatureLayerDTG getmFeatureLayerDTG(String tableName) {
        for (FeatureLayerDTG featureLayerDTG : mFeatureLayerDTGs) {
            String tableNameDTG = ((ArcGISFeatureTable) featureLayerDTG.getFeatureLayer().getFeatureTable()).getTableName();
            if (tableNameDTG.equals(tableName)) return featureLayerDTG;
        }
        return null;
    }

    public void querySearch(String searchStr, ListView listView, final ObjectsAdapter adapter) {
        adapter.clear();
        adapter.notifyDataSetChanged();
        QueryParameters queryParameters = new QueryParameters();
        StringBuilder builder = new StringBuilder();
        for (Field field : searchSFT.getFields()) {
            switch (field.getFieldType()) {
                case OID:
                case INTEGER:
                case SHORT:
                    try {
                        int search = Integer.parseInt(searchStr);
                        builder.append(String.format("%s = %s", field.getName(), search));
                        builder.append(" or ");
                    } catch (Exception e) {

                    }
                    break;
                case FLOAT:
                case DOUBLE:
                    try {
                        double search = Double.parseDouble(searchStr);
                        builder.append(String.format("%s = %s", field.getName(), search));
                        builder.append(" or ");
                    } catch (Exception e) {

                    }
                    break;
                case TEXT:
                    builder.append(field.getName() + " like N'%" + searchStr + "%'");
                    builder.append(" or ");
                    break;
            }
        }
        builder.append(" 1 = 2 ");
        queryParameters.setWhereClause(builder.toString());
        final ListenableFuture<FeatureQueryResult> feature = searchSFT.queryFeaturesAsync(queryParameters);
        feature.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult result = feature.get();
                    Iterator iterator = result.iterator();
                    while (iterator.hasNext()) {
                        Feature item = (Feature) iterator.next();
                        Map<String, Object> attributes = item.getAttributes();
                        String objectid = attributes.get(Constant.OBJECTID).toString();
                        adapter.add(new ObjectsAdapter.Item(objectid, objectid, ""));
                        adapter.notifyDataSetChanged();

//                        queryByObjectID(Integer.parseInt(attributes.get(Constant.OBJECTID).toString()));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}