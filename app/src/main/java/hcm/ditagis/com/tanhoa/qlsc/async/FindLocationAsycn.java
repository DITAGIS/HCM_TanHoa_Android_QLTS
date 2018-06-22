package hcm.ditagis.com.tanhoa.qlsc.async;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import hcm.ditagis.com.tanhoa.qlsc.R;
import hcm.ditagis.com.tanhoa.qlsc.entities.MyAddress;
import hcm.ditagis.com.tanhoa.qlsc.libs.FeatureLayerDTG;
import hcm.ditagis.com.tanhoa.qlsc.utities.MyServiceFeatureTable;

public class FindLocationAsycn extends AsyncTask<String, List<MyAddress>, Void> {
    private Geocoder mGeocoder;
    private boolean mIsFromLocationName;
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private AsyncResponse mDelegate;
    private double mLongtitude, mLatitude;
    private List<FeatureLayerDTG> mFeatureLayerDTGS;
    private boolean mIsAddFeature;

    public interface AsyncResponse {
        void processFinish(List<MyAddress> output);
    }

    public void setmLongtitude(double mLongtitude) {
        this.mLongtitude = mLongtitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public FindLocationAsycn(Context context, boolean isFromLocationName, Geocoder geocoder,
                             List<FeatureLayerDTG> featureLayerDTGS, boolean isAddFeature, AsyncResponse delegate) {
        this.mDelegate = delegate;
        this.mContext = context;
        this.mIsFromLocationName = isFromLocationName;
        this.mGeocoder = geocoder;
        this.mFeatureLayerDTGS = featureLayerDTGS;
        mIsAddFeature = isAddFeature;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {
        if (!Geocoder.isPresent())
            return null;
        final List<MyAddress> lstLocation = new ArrayList<>();
        if (mIsFromLocationName) {
            final String text = params[0];
            try {
                List<Address> addressList = mGeocoder.getFromLocationName(text, 5);
                for (Address address : addressList)
                    lstLocation.add(new MyAddress(address.getLongitude(), address.getLatitude(),
                            address.getSubAdminArea(), address.getAddressLine(0), "", "", ""));
                publishProgress(lstLocation);
            } catch (IOException ignored) {
                //todo grpc failed
                Log.e("error", ignored.toString());
            }
        } else {
            try {
                if (!mIsAddFeature) {
                    List<Address> addressList = mGeocoder.getFromLocation(mLatitude, mLongtitude, 1);
                    for (Address address : addressList)
                        lstLocation.add(new MyAddress(address.getLongitude(), address.getLatitude(),
                                address.getSubAdminArea(), address.getAddressLine(0), "", "", ""));
                    publishProgress(lstLocation);
                } else {
                    if (MyServiceFeatureTable.getInstance(mContext, mFeatureLayerDTGS).getLayerThuaDat() != null) {
                        Point project = new Point(mLongtitude, mLatitude);
                        Geometry center = GeometryEngine.project(project, SpatialReferences.getWgs84());
                        Geometry geometry = GeometryEngine.project(center, SpatialReferences.getWebMercator());
                        //kiểm tra có thuộc địa bàn quản lý của tài khoản hay không
                        QueryParameters queryParam = new QueryParameters();
                        //lấy hành chính của điểm báo sự cố
                        queryParam.setGeometry(geometry);
                        queryParam.setWhereClause("1=1");
                        final ListenableFuture<FeatureQueryResult> featureQueryResultListenableFuture = MyServiceFeatureTable.getInstance(mContext, mFeatureLayerDTGS).getLayerThuaDat().queryFeaturesAsync(queryParam);
                        featureQueryResultListenableFuture.addDoneListener(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    FeatureQueryResult features = featureQueryResultListenableFuture.get();
                                    Iterator iterator = features.iterator();
                                    while (iterator.hasNext()) {
                                        Feature feature = (Feature) iterator.next();
                                    }
                                    for (Object item : features) {
                                        Feature feature = (Feature) item;
                                        Object soNha = feature.getAttributes().get("SoNha");
                                        Object tenConDuong = feature.getAttributes().get("TenConDuong");
                                        Object maDuong = feature.getAttributes().get("MaConDuong");
                                        Object maPhuong = feature.getAttributes().get("MaPhuong");
                                        String location = "";
                                        //không có địa chỉ trên thửa đất
                                        if (soNha == null || tenConDuong == null) {
                                            List<Address> addressList = mGeocoder.getFromLocation(mLatitude, mLongtitude, 1);
                                            for (Address address : addressList) {
                                                location = address.getAddressLine(0);
                                                lstLocation.add(new MyAddress(mLongtitude, mLatitude, address.getSubAdminArea(), location, "", "", ""));
                                            }
                                            publishProgress(lstLocation);
                                        }
                                        //ngược lại, địa chỉ khác null
                                        else {
                                            location = soNha.toString() + " " + tenConDuong.toString();
                                            String maDuongStr = "";
                                            String maPhuongStr = "";
                                            if (maDuong != null)
                                                maDuongStr = maDuong.toString();
                                            if (maPhuong != null)
                                                maPhuongStr = maPhuong.toString();
                                            else if (MyServiceFeatureTable.getInstance(mContext, mFeatureLayerDTGS).getLayerDMA() != null) {
                                                Point project = new Point(mLongtitude, mLatitude);
                                                Geometry center = GeometryEngine.project(project, SpatialReferences.getWgs84());
                                                Geometry geometry = GeometryEngine.project(center, SpatialReferences.getWebMercator());

                                                //kiểm tra có thuộc địa bàn quản lý của tài khoản hay không
                                                QueryParameters queryParam = new QueryParameters();
                                                //lấy hành chính của điểm báo sự cố
                                                queryParam.setGeometry(geometry);
                                                queryParam.setWhereClause("1=1");
                                                final ListenableFuture<FeatureQueryResult> featureQueryResultListenableFuture = MyServiceFeatureTable.getInstance(mContext, mFeatureLayerDTGS).getLayerThuaDat().queryFeaturesAsync(queryParam);
                                                final String finalLocation = location;
                                                final String finalMaDuongStr = maDuongStr;
                                                final String finalMaPhuongStr = maPhuongStr;
                                                featureQueryResultListenableFuture.addDoneListener(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            FeatureQueryResult features = featureQueryResultListenableFuture.get();
                                                            for (Object item : features) {
                                                                Feature feature = (Feature) item;
                                                                Object maDMA = feature.getAttributes().get("MADMA");

                                                                String maDMAStr = "";

                                                                if (maDMA != null)
                                                                    maDMAStr = maDMA.toString();
                                                                lstLocation.add(new MyAddress(mLongtitude, mLatitude, "", finalLocation, finalMaDuongStr, finalMaPhuongStr, maDMAStr));
                                                                publishProgress(lstLocation);


                                                            }
                                                        } catch (InterruptedException | ExecutionException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                } catch (InterruptedException | ExecutionException | IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            } catch (IOException ignored) {
                Log.e("error", ignored.toString());
            }
        }


        return null;
    }

    @Override
    protected void onProgressUpdate(List<MyAddress>... addressList) {
        super.onProgressUpdate(addressList);
        if (addressList == null)
            Toast.makeText(mContext, R.string.message_no_geocoder_available, Toast.LENGTH_LONG).show();
        this.mDelegate.processFinish(addressList[0]);
    }

}
