package hcm.ditagis.com.tanhoa.qlsc.async;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;

import java.util.concurrent.ExecutionException;

import hcm.ditagis.com.tanhoa.qlsc.QuanLySuCo;
import hcm.ditagis.com.tanhoa.qlsc.R;

public class QueryFeatureAsycn extends AsyncTask<String, ArcGISFeature, Void> {
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private AsyncResponse mDelegate;
    private ServiceFeatureTable mServiceFeatureTable;

    public interface AsyncResponse {
        void processFinish(ArcGISFeature output);
    }


    public QueryFeatureAsycn(Context context, ServiceFeatureTable serviceFeatureTable, AsyncResponse delegate) {
        this.mDelegate = delegate;
        this.mContext = context;
        this.mServiceFeatureTable = serviceFeatureTable;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {
        String idSuCo = params[0];
        final QueryParameters queryParameters = new QueryParameters();
        final String query = mContext.getString(R.string.Field_SuCo_IDSuCo) + " = '" + idSuCo +"'";
        queryParameters.setWhereClause(query);
        final ListenableFuture<FeatureQueryResult> feature = mServiceFeatureTable.queryFeaturesAsync(queryParameters);
        feature.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult result = feature.get();
                    if (result.iterator().hasNext()) {
                        Feature item = result.iterator().next();
                        if (QuanLySuCo.FeatureLayerDTGDiemSuCo != null) {
                            publishProgress((ArcGISFeature) item);
                        }
                    }

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        return null;
    }

    @Override
    protected void onProgressUpdate(ArcGISFeature... arcGISFeatures) {
        super.onProgressUpdate(arcGISFeatures);
        if (arcGISFeatures == null)
            Toast.makeText(mContext, R.string.message_no_geocoder_available, Toast.LENGTH_LONG).show();
        assert arcGISFeatures != null;
        this.mDelegate.processFinish(arcGISFeatures[0]);
    }

}
