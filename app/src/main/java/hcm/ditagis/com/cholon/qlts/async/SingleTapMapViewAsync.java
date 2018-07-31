package hcm.ditagis.com.cholon.qlts.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.ArcGISFeatureTable;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.GeoElement;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import hcm.ditagis.com.cholon.qlts.R;
import hcm.ditagis.com.cholon.qlts.libs.FeatureLayerDTG;
import hcm.ditagis.com.cholon.qlts.utities.Popup;

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
    private android.graphics.Point mClickPoint;


    public SingleTapMapViewAsync(Context context, List<FeatureLayerDTG> featureLayerDTGS,
                                 Popup popup, android.graphics.Point clickPoint,
                                 MapView mapview) {
        this.mMapView = mapview;
        this.mFeatureLayerDTGs = featureLayerDTGS;
        this.mPopUp = popup;
        this.mClickPoint = clickPoint;
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
                            if (elements.size() > 0) {
                                if (elements.get(0) instanceof ArcGISFeature) {
                                    mSelectedArcGISFeature = (ArcGISFeature) elements.get(0);
                                    String tableName = ((ArcGISFeatureTable) mSelectedArcGISFeature.getFeatureTable()).getTableName();
                                    FeatureLayerDTG featureLayerDTG = getmFeatureLayerDTG(tableName);
                                    publishProgress(featureLayerDTG);
                                }
                            }
                        }

                    }
                    publishProgress(null);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        return null;
    }
    public FeatureLayerDTG getmFeatureLayerDTG(String tableName) {
        for (FeatureLayerDTG featureLayerDTG : mFeatureLayerDTGs) {
            String tableNameDTG = ((ArcGISFeatureTable) featureLayerDTG.getFeatureLayer().getFeatureTable()).getTableName();
            if (tableNameDTG.equals(tableName)) return featureLayerDTG;
        }
        return null;
    }
    @Override
    protected void onProgressUpdate(FeatureLayerDTG... values) {
        super.onProgressUpdate(values);
        mPopUp.clearSelection();
        mPopUp.dimissCallout();
        if (values != null) {
            FeatureLayerDTG featureLayerDTG = values[0];
            mPopUp.setFeatureLayerDTG(featureLayerDTG);
            if (mSelectedArcGISFeature != null) mPopUp.showPopup(mSelectedArcGISFeature,true);
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