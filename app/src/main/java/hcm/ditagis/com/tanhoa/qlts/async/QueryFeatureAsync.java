package hcm.ditagis.com.tanhoa.qlts.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import hcm.ditagis.com.tanhoa.qlts.DataLoggerActivity;
import hcm.ditagis.com.tanhoa.qlts.QuanLyTaiSan;
import hcm.ditagis.com.tanhoa.qlts.adapter.FeatureAdapter;
import hcm.ditagis.com.tanhoa.qlts.R;
import hcm.ditagis.com.tanhoa.qlts.utities.Constant;

/**
 * Created by ThanLe on 4/16/2018.
 */

public class QueryFeatureAsync extends AsyncTask<String, List<FeatureAdapter.Item>, Void> {
    private ProgressDialog dialog;
    private Context mContext;
    private ServiceFeatureTable serviceFeatureTable;
    private FeatureAdapter featureAdapter;
    private TextView txtTongItem;

    public QueryFeatureAsync(DataLoggerActivity dataLoggerActivity, ServiceFeatureTable serviceFeatureTable, TextView txtTongItem, FeatureAdapter adapter, AsyncResponse asyncResponse) {
        this.delegate = asyncResponse;
        mContext = dataLoggerActivity;
        this.serviceFeatureTable = serviceFeatureTable;
        this.featureAdapter = adapter;
        this.txtTongItem = txtTongItem;
        dialog = new ProgressDialog(dataLoggerActivity, android.R.style.Theme_Material_Dialog_Alert);
    }


    public interface AsyncResponse {
        void processFinish(List<Feature> features);
    }

    private AsyncResponse delegate = null;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage(mContext.getString(R.string.async_dang_xu_ly));
        dialog.setCancelable(false);
        dialog.show();

    }

    @Override
    protected Void doInBackground(String... params) {
        final List<FeatureAdapter.Item> items = new ArrayList<>();
        final List<Feature> features = new ArrayList<>();
        QueryParameters queryParameters = new QueryParameters();
        String queryClause = params[0];
        queryParameters.setWhereClause(queryClause);
        final ListenableFuture<FeatureQueryResult> queryResultListenableFuture = serviceFeatureTable.queryFeaturesAsync(queryParameters, ServiceFeatureTable.QueryFeatureFields.LOAD_ALL);
        queryResultListenableFuture.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult result = queryResultListenableFuture.get();
                    Iterator iterator = result.iterator();

                    while (iterator.hasNext()) {
                        Feature feature = (Feature) iterator.next();
                        FeatureAdapter.Item item = new FeatureAdapter.Item();
                        Map<String, Object> attributes = feature.getAttributes();
                        if (attributes.get(mContext.getString(R.string.LayerID)) != null)
                            item.setLayerID(attributes.get(mContext.getString(R.string.LayerID)).toString());
                        if (attributes.get(mContext.getString(R.string.ID)) != null)
                            item.setID(attributes.get(mContext.getString(R.string.ID)).toString());
                        if (attributes.get(mContext.getString(R.string.TrangThai)) != null)
                            item.setTrangThai(attributes.get(mContext.getString(R.string.TrangThai)).toString());
                        if (attributes.get(mContext.getString(R.string.Ngay)) != null) {
                            String format_date = Constant.DATE_FORMAT.format(((Calendar) attributes.get(mContext.getString(R.string.Ngay))).getTime());
                            item.setNgay(format_date);
                        }
                        items.add(item);
                        features.add(feature);
                    }
                    delegate.processFinish(features);
                    publishProgress(items);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        return null;
    }

    @Override
    protected void onProgressUpdate(List<FeatureAdapter.Item>... values) {
        featureAdapter.clear();
        featureAdapter.setItems(values[0]);
        featureAdapter.notifyDataSetChanged();
        if (txtTongItem != null)
            txtTongItem.setText(mContext.getString(R.string.nav_thong_ke_tong_diem) + values[0].size());
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
        super.onProgressUpdate(values);

    }

    private String getValueAttributes(Feature feature, String fieldName) {
        if (feature.getAttributes().get(fieldName) != null)
            return feature.getAttributes().get(fieldName).toString();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (dialog != null || dialog.isShowing()) dialog.dismiss();
        super.onPostExecute(result);

    }

}

