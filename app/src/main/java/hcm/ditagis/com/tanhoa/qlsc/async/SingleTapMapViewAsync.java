package hcm.ditagis.com.tanhoa.qlsc.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.ArcGISFeatureTable;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.GeoElement;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import hcm.ditagis.com.tanhoa.qlsc.libs.FeatureLayerDTG;
import hcm.ditagis.com.tanhoa.qlsc.utities.Popup;

/**
 * Created by ThanLe on 4/16/2018.
 */

public class SingleTapMapViewAsync extends AsyncTask<Point, FeatureLayerDTG, Void> {
    private ProgressDialog mDialog;
    private Context mContext;
    private FeatureLayerDTG mFeatureLayerDTG;
    private Point mPoint;
    private List<FeatureLayerDTG> mFeatureLayerDTGs;
    private MapView mMapView;
    private ArcGISFeature mSelectedArcGISFeature;
    private Popup mPopUp;
    //    private Callout mCallOut;
    private static double DELTA_MOVE_Y = 0;//7000;
    private android.graphics.Point mClickPoint;
    private boolean isFound = false;

    public SingleTapMapViewAsync(Context context, List<FeatureLayerDTG> featureLayerDTGS, Popup popup, android.graphics.Point clickPoint, MapView mapview) {
        this.mMapView = mapview;
        this.mFeatureLayerDTGs = featureLayerDTGS;
        this.mPopUp = popup;
        this.mClickPoint = clickPoint;
        this.mContext = context;
        this.mDialog = new ProgressDialog(context, android.R.style.Theme_Material_Dialog_Alert);
    }

    public SingleTapMapViewAsync(Context context, List<FeatureLayerDTG> featureLayerDTGS,
                                 Popup popup, Callout callout, android.graphics.Point clickPoint,
                                 MapView mapview) {
        this.mMapView = mapview;
        this.mFeatureLayerDTGs = featureLayerDTGS;
        this.mPopUp = popup;
//        this.mCallOut = callout;
        this.mClickPoint = clickPoint;
        this.mContext = context;
        this.mDialog = new ProgressDialog(context, android.R.style.Theme_Material_Dialog_Alert);
    }

    @Override
    protected Void doInBackground(Point... points) {
        final ListenableFuture<List<IdentifyLayerResult>> listListenableFuture = mMapView.identifyLayersAsync(mClickPoint, 5, false, 1);
        listListenableFuture.addDoneListener(new Runnable() {
            @Override
            public void run() {
                List<IdentifyLayerResult> identifyLayerResults = null;
                try {
                    identifyLayerResults = listListenableFuture.get();
                    for (IdentifyLayerResult identifyLayerResult : identifyLayerResults) {
                        {
                            List<GeoElement> elements = identifyLayerResult.getElements();
                            if (elements.size() > 0 && elements.get(0) instanceof ArcGISFeature && !isFound) {
                                isFound = true;
                                mSelectedArcGISFeature = (ArcGISFeature) elements.get(0);
                                long serviceLayerId = mSelectedArcGISFeature.getFeatureTable().
                                        getServiceLayerId();
                                FeatureLayerDTG featureLayerDTG = getmFeatureLayerDTG(serviceLayerId);
                                publishProgress(featureLayerDTG);
                            }
                        }
                    }
                    publishProgress(null);
                } catch (
                        InterruptedException e)

                {
                    e.printStackTrace();
                } catch (
                        ExecutionException e)

                {
                    e.printStackTrace();
                }
            }
        });
        return null;
    }

    public FeatureLayerDTG getmFeatureLayerDTG(long serviceLayerId) {
        for (FeatureLayerDTG featureLayerDTG : mFeatureLayerDTGs) {
            long serviceLayerDTGId = ((ArcGISFeatureTable) featureLayerDTG.getFeatureLayer().getFeatureTable()).getServiceLayerId();
            if (serviceLayerDTGId == serviceLayerId) return featureLayerDTG;
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog.setMessage("Đang xử lý...");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    protected void onProgressUpdate(FeatureLayerDTG... values) {
        super.onProgressUpdate(values);
        if (values != null && mSelectedArcGISFeature != null) {
            FeatureLayerDTG featureLayerDTG = values[0];
            mPopUp.setFeatureLayerDTG(featureLayerDTG);
            mPopUp.showPopup(mSelectedArcGISFeature, false);
        }
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

    }

}