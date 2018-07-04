package hcm.ditagis.com.tanhoa.qlsc.utities;

import android.app.Activity;
import android.content.Context;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import hcm.ditagis.com.tanhoa.qlsc.QuanLySuCo;
import hcm.ditagis.com.tanhoa.qlsc.R;
import hcm.ditagis.com.tanhoa.qlsc.adapter.TraCuuAdapter;
import hcm.ditagis.com.tanhoa.qlsc.async.SingleTapAddFeatureAsync;
import hcm.ditagis.com.tanhoa.qlsc.async.SingleTapMapViewAsync;
import hcm.ditagis.com.tanhoa.qlsc.libs.FeatureLayerDTG;

/**
 * Created by ThanLe on 2/2/2018.
 */

public class MapViewHandler extends Activity {
    private static final int REQUEST_ID_IMAGE_CAPTURE = 1;
    private static double DELTA_MOVE_Y = 0;//7000;
    private final FeatureLayer suCoTanHoaLayer;
    private Callout mCallout;
    private android.graphics.Point mClickPoint;
    private ArcGISFeature mSelectedArcGISFeature;
    private MapView mMapView;
    private boolean isClickBtnAdd = false;
    private ServiceFeatureTable mServiceFeatureTable;
    private Popup mPopUp;
    private Context mContext;
    private Geocoder mGeocoder;

    public void setFeatureLayerDTGs(List<FeatureLayerDTG> mFeatureLayerDTGs) {
        this.mFeatureLayerDTGs = mFeatureLayerDTGs;
    }

    private List<FeatureLayerDTG> mFeatureLayerDTGs;

    public MapViewHandler(FeatureLayerDTG featureLayerDTG, Callout mCallout, MapView mapView,
                          Popup popupInfos, Context mContext, Geocoder geocoder) {
        this.mCallout = mCallout;
        this.mMapView = mapView;
        this.mServiceFeatureTable = (ServiceFeatureTable) featureLayerDTG.getFeatureLayer().getFeatureTable();
        this.mPopUp = popupInfos;
        this.mContext = mContext;
        this.suCoTanHoaLayer = featureLayerDTG.getFeatureLayer();
        this.mGeocoder = geocoder;
    }

    public void setClickBtnAdd(boolean clickBtnAdd) {
        isClickBtnAdd = clickBtnAdd;
    }

    public void addFeature(byte[] image, Point pointFindLocation) {
        mClickPoint = mMapView.locationToScreen(pointFindLocation);
        SingleTapAddFeatureAsync singleTapAdddFeatureAsync = new SingleTapAddFeatureAsync(mClickPoint, mContext,
                image, mServiceFeatureTable, mMapView, mGeocoder, mFeatureLayerDTGs, new SingleTapAddFeatureAsync.AsyncResponse() {
            @Override
            public void processFinish(Feature output) {
                if (output != null && QuanLySuCo.FeatureLayerDTGDiemSuCo != null) {
                    ArcGISFeature arcGISFeature = (ArcGISFeature) output;
                    if (arcGISFeature.canEditAttachments() && arcGISFeature.canUpdateGeometry()) {
                        mPopUp.setFeatureLayerDTG(QuanLySuCo.FeatureLayerDTGDiemSuCo);
                        mPopUp.showPopup(arcGISFeature, true);
                    } else {
                        MySnackBar.make(mMapView, "Điểm sự cố vừa thêm bị lỗi\nVui lòng liên hệ admin để xử lý", true);
                    }
                }
            }
        });
//        Point add_point = mMapView.getCurrentViewpoint(Viewpoint.Type.CENTER_AND_SCALE).getTargetGeometry().getExtent().getCenter();
        singleTapAdddFeatureAsync.execute(pointFindLocation);
    }


    public double[] onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Point center = mMapView.getCurrentViewpoint(Viewpoint.Type.CENTER_AND_SCALE).getTargetGeometry().getExtent().getCenter();
        Geometry project = GeometryEngine.project(center, SpatialReferences.getWgs84());
        double[] location = {project.getExtent().getCenter().getX(), project.getExtent().getCenter().getY()};
        mClickPoint = new android.graphics.Point((int) e2.getX(), (int) e2.getY());
//        Geometry geometry = GeometryEngine.project(project, SpatialReferences.getWebMercator());
        return location;
    }

    public void onSingleTapMapView(MotionEvent e) {
        final Point clickPoint = mMapView.screenToLocation(new android.graphics.Point(Math.round(e.getX()), Math.round(e.getY())));
        mClickPoint = new android.graphics.Point((int) e.getX(), (int) e.getY());
        if (isClickBtnAdd) {
            mMapView.setViewpointCenterAsync(clickPoint, 10);
        } else {

            SingleTapMapViewAsync singleTapMapViewAsync = new SingleTapMapViewAsync(mContext, mFeatureLayerDTGs, mPopUp, mClickPoint, mMapView);
            singleTapMapViewAsync.execute(clickPoint);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getDateString() {
        SimpleDateFormat writeDate = new SimpleDateFormat("dd_MM_yyyy HH:mm:ss");
        writeDate.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        return writeDate.format(Calendar.getInstance().getTime());
    }

    private String getTimeID() {
        return Constant.DATE_FORMAT.format(Calendar.getInstance().getTime());
    }

    public void queryByObjectID(int objectID) {
        final QueryParameters queryParameters = new QueryParameters();
        final String query = "OBJECTID = " + objectID;
        queryParameters.setWhereClause(query);

        final ListenableFuture<FeatureQueryResult> feature = mServiceFeatureTable.queryFeaturesAsync(queryParameters, ServiceFeatureTable.QueryFeatureFields.LOAD_ALL);
        feature.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult result = feature.get();
                    if (result.iterator().hasNext()) {
                        Feature item = result.iterator().next();
                        Envelope extent = item.getGeometry().getExtent();

                        mMapView.setViewpointGeometryAsync(extent);
                        suCoTanHoaLayer.selectFeature(item);
                        if (QuanLySuCo.FeatureLayerDTGDiemSuCo != null) {
                            mSelectedArcGISFeature = (ArcGISFeature) item;
                            mPopUp.setFeatureLayerDTG(QuanLySuCo.FeatureLayerDTGDiemSuCo);
                            if (mSelectedArcGISFeature != null)
                                mPopUp.showPopup(mSelectedArcGISFeature, false);
                        }
                    }

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void querySearch(String searchStr, final TraCuuAdapter adapter) {
        adapter.clear();
        adapter.notifyDataSetChanged();
        mCallout.dismiss();

        suCoTanHoaLayer.clearSelection();
        QueryParameters queryParameters = new QueryParameters();
        StringBuilder builder = new StringBuilder();
        for (Field field : mServiceFeatureTable.getFields()) {
            switch (field.getFieldType()) {
                case OID:
                case INTEGER:
                case SHORT:
                    try {
                        int search = Integer.parseInt(searchStr);
                        builder.append(String.format("%s = %s", field.getName(), search));
                        builder.append(" or ");
                    } catch (Exception ignored) {

                    }
                    break;
                case FLOAT:
                case DOUBLE:
                    try {
                        double search = Double.parseDouble(searchStr);
                        builder.append(String.format("%s = %s", field.getName(), search));
                        builder.append(" or ");
                    } catch (Exception ignored) {

                    }
                    break;
                case TEXT:
                    builder.append(field.getName()).append(" like N'%").append(searchStr).append("%'");
                    builder.append(" or ");
                    break;
            }
        }
        builder.append(" 1 = 2 ");
        queryParameters.setWhereClause(builder.toString());
        final ListenableFuture<FeatureQueryResult> feature = mServiceFeatureTable.queryFeaturesAsync(queryParameters);
        feature.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult result = feature.get();
                    Iterator iterator = result.iterator();
                    while (iterator.hasNext()) {
                        Feature item = (Feature) iterator.next();
                        Map<String, Object> attributes = item.getAttributes();
                        String format_date = "";
                        String[] split = attributes.get(mContext.getString(R.string.Field_SuCo_IDSuCo)).toString().split("_");
                        try {
                            format_date = Constant.DATE_FORMAT.format((new GregorianCalendar(Integer.parseInt(split[3]), Integer.parseInt(split[2]), Integer.parseInt(split[1])).getTime()));
                        } catch (Exception ignored) {

                        }
                        String viTri = "";
                        try {
                            viTri = attributes.get(mContext.getString(R.string.Field_SuCo_DiaChi)).toString();
                        } catch (Exception ignored) {

                        }
                        adapter.add(new TraCuuAdapter.Item(Integer.parseInt(attributes.get(mContext.getString(R.string.Field_OBJECTID)).toString()),
                                attributes.get(mContext.getString(R.string.Field_SuCo_IDSuCo)).toString(),
                                Integer.parseInt(attributes.get(mContext.getString(R.string.Field_SuCo_TrangThai)).toString()), format_date, viTri));
                        adapter.notifyDataSetChanged();

//                        queryByObjectID(Integer.parseInt(attributes.get(Constant.OBJECTID).toString()));
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}