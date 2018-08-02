package hcm.ditagis.com.tanhoa.qlts.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.Attachment;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureEditResult;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import hcm.ditagis.com.tanhoa.qlts.R;
import hcm.ditagis.com.tanhoa.qlts.tools.MySnackBar;
import hcm.ditagis.com.tanhoa.qlts.utities.Constant;

/**
 * Created by ThanLe on 4/16/2018.
 */

public class SingleTapAddFeatureAsync extends AsyncTask<Point, Void, Void> {
    private ProgressDialog mDialog;
    private Context mContext;
    private byte[] mImage;
    private ServiceFeatureTable mServiceFeatureTable;
    private LocatorTask mLocatorTask;
    private ArcGISFeature mSelectedArcGISFeature;
    private MapView mMapView;

    public SingleTapAddFeatureAsync(Context context, byte[] image, ServiceFeatureTable serviceFeatureTable,
                                    LocatorTask locatorTask, MapView mapView) {
        this.mServiceFeatureTable = serviceFeatureTable;
        this.mLocatorTask = locatorTask;
        this.mMapView = mapView;
        this.mImage = image;
        this.mContext = context;
        this.mDialog = new ProgressDialog(context, android.R.style.Theme_Material_Dialog_Alert);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog.setMessage("Đang xử lý...");
        mDialog.setCancelable(false);
        mDialog.setButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                publishProgress(null);
            }
        });
        mDialog.show();
    }

    @Override
    protected Void doInBackground(Point... params) {
        final Point clickPoint = params[0];
        final Feature feature;
        try {
            feature = mServiceFeatureTable.createFeature();
            feature.setGeometry(clickPoint);
            final ListenableFuture<List<GeocodeResult>> listListenableFuture =
                    mLocatorTask.reverseGeocodeAsync(clickPoint);
            listListenableFuture.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<GeocodeResult> geocodeResults = listListenableFuture.get();
                        if (geocodeResults.size() > 0) {
                            GeocodeResult geocodeResult = geocodeResults.get(0);
                            Map<String, Object> attrs = new HashMap<>();
                            for (String key : geocodeResult.getAttributes().keySet()) {
                                attrs.put(key, geocodeResult.getAttributes().get(key));
                            }
                            String address = geocodeResult.getAttributes().get("LongLabel").toString();
                            feature.getAttributes().put(Constant.VI_TRI, address);
                        }
                        Short intObj = new Short((short) 0);
                        feature.getAttributes().put(Constant.TRANG_THAI, intObj);

                        String searchStr = "";
                        String dateTime = "";
                        String timeID = "";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            dateTime = getDateString();
                            timeID = getTimeID();
                            searchStr = Constant.IDSU_CO + " like '%" + timeID + "'";
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
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } catch (ExecutionException e1) {
                        e1.printStackTrace();
                    }


                }
            });

        } catch (Exception e) {
            MySnackBar.make(mMapView, "Không tạo được thuộc tính. Vui lòng thử lại sau", true);
        }


        return null;
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

    private void addFeatureAsync(ListenableFuture<FeatureQueryResult> featureQuery,
                                 Feature feature, String finalTimeID, String finalDateTime) {
        try {
            // lấy id lớn nhất
            int id_tmp;
            int id = 0;
            FeatureQueryResult result = featureQuery.get();
            Iterator iterator = result.iterator();
            while (iterator.hasNext()) {
                Feature item = (Feature) iterator.next();
                id_tmp = Integer.parseInt(item.getAttributes().get(Constant.IDSU_CO).toString().split("_")[0]);
                if (id_tmp > id) id = id_tmp;
            }
            id++;
            feature.getAttributes().put(Constant.IDSU_CO, id + "_" + finalTimeID);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Date date = Constant.DATE_FORMAT.parse(finalDateTime);
                Calendar c = Calendar.getInstance();
                feature.getAttributes().put(Constant.NGAY_CAP_NHAT, c);
                feature.getAttributes().put(Constant.NGAY_THONG_BAO, c);
            }
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
                                    final String query = "OBJECTID = " + objectId;
                                    queryParameters.setWhereClause(query);
                                    final ListenableFuture<FeatureQueryResult> feature = mServiceFeatureTable.queryFeaturesAsync(queryParameters);
                                    feature.addDoneListener(new Runnable() {
                                        @Override
                                        public void run() {
                                            addAttachment(feature);
                                        }
                                    });
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
    }

    private void addAttachment(ListenableFuture<FeatureQueryResult> feature) {
        FeatureQueryResult result = null;
        try {
            result = feature.get();
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
                                                List<FeatureEditResult> edits = null;
                                                try {
                                                    edits = updatedServerResult.get();
                                                    if (edits.size() > 0) {
                                                        if (!edits.get(0).hasCompletedWithErrors()) {
                                                            //attachmentList.add(fileName);
                                                            String s = mSelectedArcGISFeature.getAttributes().get("objectid").toString();
                                                            // update the attachment list view/ on the control panel
                                                        } else {
                                                        }
                                                    } else {
                                                    }
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                } catch (ExecutionException e) {
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

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Envelope extent = item.getGeometry().getExtent();
                mMapView.setViewpointGeometryAsync(extent);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }


    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

    }

}