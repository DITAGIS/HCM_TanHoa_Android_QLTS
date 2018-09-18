package hcm.ditagis.com.tanhoa.qlts.tools;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.FeatureEditResult;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.ServiceFeatureTable;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import hcm.ditagis.com.tanhoa.qlts.QuanLyTaiSan;
import hcm.ditagis.com.tanhoa.qlts.R;

public class FeatureLayerUpdate {
    private ServiceFeatureTable serviceFeatureTable;
    private QuanLyTaiSan mMainActivity;
    public FeatureLayerUpdate(QuanLyTaiSan mMainActivity){
        this.mMainActivity = mMainActivity;
    }

    public ServiceFeatureTable getServiceFeatureTable() {
        return serviceFeatureTable;
    }

    public void setServiceFeatureTable(ServiceFeatureTable serviceFeatureTable) {
        this.serviceFeatureTable = serviceFeatureTable;
    }
    public void updateFeatureAsync(final ServiceFeatureTable mServiceFeatureTable, ArcGISFeature feature){
        Field field_NgayCapNhat = mServiceFeatureTable.getField(mMainActivity.getString(R.string.NGAYCAPNHAT));
        if(field_NgayCapNhat != null){
            Calendar currentTime = Calendar.getInstance();
            feature.getAttributes().put(mMainActivity.getString(R.string.NGAYCAPNHAT), currentTime);
        }
        final ListenableFuture<Void> mapViewResult = mServiceFeatureTable.updateFeatureAsync(feature);
        mapViewResult.addDoneListener(new Runnable() {
            @Override
            public void run() {
                final ListenableFuture<List<FeatureEditResult>> listListenableEditAsync = mServiceFeatureTable.applyEditsAsync();
                listListenableEditAsync.addDoneListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<FeatureEditResult> featureEditResults = listListenableEditAsync.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }
}
