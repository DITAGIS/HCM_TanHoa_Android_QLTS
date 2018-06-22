package hcm.ditagis.com.tanhoa.qlsc.async;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.Attachment;
import com.esri.arcgisruntime.data.EditResult;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureEditResult;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.GeoElement;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import hcm.ditagis.com.tanhoa.qlsc.R;
import hcm.ditagis.com.tanhoa.qlsc.entities.MyAddress;
import hcm.ditagis.com.tanhoa.qlsc.entities.entitiesDB.KhachHang;
import hcm.ditagis.com.tanhoa.qlsc.libs.FeatureLayerDTG;
import hcm.ditagis.com.tanhoa.qlsc.utities.Constant;
import hcm.ditagis.com.tanhoa.qlsc.utities.MySnackBar;

/**
 * Created by ThanLe on 4/16/2018.
 */

public class SingleTapAddFeatureAsync extends AsyncTask<Point, Feature, Void> {
    private ProgressDialog mDialog;
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private byte[] mImage;
    private ServiceFeatureTable mServiceFeatureTable;
    private ArcGISFeature mSelectedArcGISFeature;
    @SuppressLint("StaticFieldLeak")
    private MapView mMapView;
    private AsyncResponse mDelegate;
    private android.graphics.Point mClickPoint;
    private Geocoder mGeocoder;
    private List<FeatureLayerDTG> mFeatureLayerDTGS;

    public interface AsyncResponse {
        void processFinish(Feature output);
    }

    public SingleTapAddFeatureAsync(android.graphics.Point clickPoint, Context context, byte[] image,
                                    ServiceFeatureTable serviceFeatureTable, MapView mapView, Geocoder geocoder, List<FeatureLayerDTG> featureLayerDTGS, AsyncResponse delegate) {
        this.mServiceFeatureTable = serviceFeatureTable;
        this.mMapView = mapView;
        this.mImage = image;
        this.mContext = context;
        this.mClickPoint = clickPoint;
        this.mDialog = new ProgressDialog(context, android.R.style.Theme_Material_Dialog_Alert);
        this.mDelegate = delegate;
        this.mGeocoder = geocoder;
        this.mFeatureLayerDTGS = featureLayerDTGS;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog.setMessage("Đang xử lý...");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    protected Void doInBackground(Point... params) {
        final Point clickPoint = params[0];

        final Feature feature;
        try {
            feature = mServiceFeatureTable.createFeature();
            feature.setGeometry(clickPoint);
            FindLocationAsycn findLocationAsycn = new FindLocationAsycn(mContext, false,
                    mGeocoder, mFeatureLayerDTGS,false, new FindLocationAsycn.AsyncResponse() {
                @Override
                public void processFinish(List<MyAddress> output) {
                    if (output != null) {
                        feature.getAttributes().put(mContext.getString(R.string.SoNha), output.get(0).getLocation());
                        String subAdminArea = output.get(0).getSubAdminArea();
                        if (subAdminArea.equals(mContext.getString(R.string.QuanPhuNhuanName)))
                            feature.getAttributes().put(mContext.getString(R.string.Field_SuCo_MaQuan), mContext.getString(R.string.QuanPhuNhuanCode));
                        else if (subAdminArea.equals(mContext.getString(R.string.QuanTanPhuName)))
                            feature.getAttributes().put(mContext.getString(R.string.Field_SuCo_MaQuan), mContext.getString(R.string.QuanTanPhuCode));
                        else if (subAdminArea.equals(mContext.getString(R.string.QuanTanBinhName)))
                            feature.getAttributes().put(mContext.getString(R.string.Field_SuCo_MaQuan), mContext.getString(R.string.QuanTanBinhCode));
                        Short intObj = (short) 0;
                        feature.getAttributes().put(mContext.getString(R.string.Field_SuCo_TrangThai), intObj);

                        String searchStr = "";
                        String dateTime = "";
                        String timeID = "";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            dateTime = getDateString();
                            timeID = getTimeID();
                            searchStr = mContext.getString(R.string.Field_SuCo_IDSuCo) + " like '%" + timeID + "'";
                        }
                        QueryParameters queryParameters = new QueryParameters();
                        queryParameters.setWhereClause(searchStr);
                        final ListenableFuture<FeatureQueryResult> featureQuery =
                                mServiceFeatureTable.queryFeaturesAsync(queryParameters);
                        final String finalDateTime = dateTime;
                        final String finalTimeID = timeID;
                        featureQuery.addDoneListener(new Runnable() {
                            @Override
                            public void run() {
                                addFeatureAsync(featureQuery, feature, finalTimeID, finalDateTime);
                            }
                        });
                    }
                }
            });
            Geometry project = GeometryEngine.project(clickPoint, SpatialReferences.getWgs84());
            double[] location = {project.getExtent().getCenter().getX(), project.getExtent().getCenter().getY()};
            findLocationAsycn.setmLongtitude(location[0]);
            findLocationAsycn.setmLatitude(location[1]);

            findLocationAsycn.execute();

        } catch (Exception e) {
            MySnackBar.make(mMapView, mContext.getString(R.string.message_error_add_feature), true);
            publishProgress(null);
        }


        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getDateString() {
//        String timeStamp = Constant.DATE_FORMAT.format(Calendar.getInstance().getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat writeDate = new SimpleDateFormat("dd_MM_yyyy HH:mm:ss");
        writeDate.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        return writeDate.format(Calendar.getInstance().getTime());
    }

    private String getTimeID() {
        return Constant.DATE_FORMAT.format(Calendar.getInstance().getTime());
    }

    private void addFeatureAsync(ListenableFuture<FeatureQueryResult> featureQuery,
                                 final Feature feature, String finalTimeID, String finalDateTime) {
        try {
            // lấy id lớn nhất
            int id_tmp;
            int id = 0;
            FeatureQueryResult result = featureQuery.get();
            Iterator iterator = result.iterator();
            while (iterator.hasNext()) {
                Feature item = (Feature) iterator.next();
                id_tmp = Integer.parseInt(item.getAttributes().get(mContext.getString(R.string.Field_SuCo_IDSuCo)).toString().split("_")[0]);
                if (id_tmp > id) id = id_tmp;
            }
            id++;
            feature.getAttributes().put(mContext.getString(R.string.Field_SuCo_IDSuCo), id + "_" + finalTimeID);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Date date = Constant.DATE_FORMAT.parse(finalDateTime);
                Calendar c = Calendar.getInstance();
                feature.getAttributes().put(mContext.getString(R.string.Field_SuCo_NgayXayRa), c);
                feature.getAttributes().put(mContext.getString(R.string.Field_SuCo_NgayThongBao), c);
            }
            feature.getAttributes().put(mContext.getString(R.string.Field_SuCo_NguoiBaoSuCo), KhachHang.khachHangDangNhap.getUserName());
            feature.getAttributes().put(mContext.getString(R.string.Field_SuCo_LoaiSuCo), (short) 0);
            //---get DMA begin
            final ListenableFuture<List<IdentifyLayerResult>> listListenableFuture = mMapView.identifyLayersAsync(mClickPoint, 5, false, 1);
            listListenableFuture.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    List<IdentifyLayerResult> identifyLayerResults;
                    try {
                        identifyLayerResults = listListenableFuture.get();
                        for (IdentifyLayerResult identifyLayerResult : identifyLayerResults) {
                            {
                                List<GeoElement> elements = identifyLayerResult.getElements();
                                if (elements.size() > 0) {
                                    if (elements.get(0) instanceof ArcGISFeature) {
                                        mSelectedArcGISFeature = (ArcGISFeature) elements.get(0);
                                    }
                                }
                            }
                        }
//                        publishProgress(null);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });
            //---get DMA end
            ListenableFuture<Void> mapViewResult = mServiceFeatureTable.addFeatureAsync(feature);
            mapViewResult.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    final ListenableFuture<List<FeatureEditResult>> listListenableEditAsync = mServiceFeatureTable.applyEditsAsync();
                    listListenableEditAsync.addDoneListener(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                List<FeatureEditResult> featureEditResults = listListenableEditAsync.get();
                                if (featureEditResults.size() > 0) {
                                    long objectId = featureEditResults.get(0).getObjectId();
                                    final QueryParameters queryParameters = new QueryParameters();
                                    final String query = String.format(mContext.getString(R.string.arcgis_query_by_OBJECTID), objectId);
                                    queryParameters.setWhereClause(query);
                                    final ListenableFuture<FeatureQueryResult> featuresAsync = mServiceFeatureTable.queryFeaturesAsync(queryParameters,ServiceFeatureTable.QueryFeatureFields.LOAD_ALL);
                                    featuresAsync.addDoneListener(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                FeatureQueryResult result = featuresAsync.get();
                                                if (result.iterator().hasNext()) {
                                                    Feature item = result.iterator().next();
                                                    if (mImage != null)
                                                        addAttachment(featuresAsync, item);
                                                    else publishProgress(item);
                                                }
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            } catch (ExecutionException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                }
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            });
        } catch (InterruptedException | ExecutionException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void addAttachment(ListenableFuture<FeatureQueryResult> listenableFuture, final Feature feature) {
        FeatureQueryResult result;
        try {

            result = listenableFuture.get();
            if (result.iterator().hasNext()) {
                Feature item = result.iterator().next();
                mSelectedArcGISFeature = (ArcGISFeature) item;
                final String attachmentName = mContext.getString(R.string.attachment) + "_" + System.currentTimeMillis() + ".png";
                final ListenableFuture<Attachment> addResult = mSelectedArcGISFeature.addAttachmentAsync(mImage, Bitmap.CompressFormat.PNG.toString(), attachmentName);
                addResult.addDoneListener(new Runnable() {
                    @Override
                    public void run() {
                        if (mDialog != null && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        try {
                            Attachment attachment = addResult.get();
                            if (attachment.getSize() > 0) {
                                final ListenableFuture<Void> tableResult = mServiceFeatureTable.updateFeatureAsync(mSelectedArcGISFeature);
                                tableResult.addDoneListener(new Runnable() {
                                    @Override
                                    public void run() {
                                        final ListenableFuture<List<FeatureEditResult>> updatedServerResult = mServiceFeatureTable.applyEditsAsync();
                                        updatedServerResult.addDoneListener(new Runnable() {
                                            @Override
                                            public void run() {
                                                List<FeatureEditResult> edits;
                                                try {
                                                    edits = updatedServerResult.get();
                                                    if (edits.size() > 0) {
                                                        if (!edits.get(0).hasCompletedWithErrors()) {
                                                            publishProgress(feature);
                                                        }
                                                    }
                                                } catch (InterruptedException | ExecutionException e) {
                                                    e.printStackTrace();
                                                }
                                                if (mDialog != null && mDialog.isShowing()) {
                                                    mDialog.dismiss();
                                                }

                                            }
                                        });


                                    }
                                });
                            }

                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Envelope extent = item.getGeometry().getExtent();
                mMapView.setViewpointGeometryAsync(extent);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onProgressUpdate(Feature... values) {
        if (values == null)
            this.mDelegate.processFinish(null);
        else this.mDelegate.processFinish(values[0]);
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }


    @Override
    protected void onPostExecute(Void result) {


    }

}