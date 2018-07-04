package hcm.ditagis.com.tanhoa.qlsc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.layers.FeatureLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import hcm.ditagis.com.tanhoa.qlsc.adapter.ThongKeAdapter;
import hcm.ditagis.com.tanhoa.qlsc.utities.Config;
import hcm.ditagis.com.tanhoa.qlsc.utities.ListConfig;

public class ThongKeActivity extends AppCompatActivity {


    private List<FeatureLayer> mFeatureLayerS;
    private ThongKeAdapter thongKeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        List<Config> configs = ListConfig.getInstance(this).getConfigs();
        mFeatureLayerS = new ArrayList<>();
        thongKeAdapter = new ThongKeAdapter(this, new ArrayList<ThongKeAdapter.Item>());
        for (Config config : configs) {
            ServiceFeatureTable serviceFeatureTable = new ServiceFeatureTable(config.getUrl());

            FeatureLayer featureLayer = new FeatureLayer(serviceFeatureTable);
            featureLayer.setName(config.getAlias());
            mFeatureLayerS.add(featureLayer);

        }
        ImageView imageView = findViewById(R.id.img_refress);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
                Toast.makeText(ThongKeActivity.this, "Đã làm mới dữ liệu", Toast.LENGTH_LONG).show();
            }
        });
        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(thongKeAdapter);
        refresh();
    }

    public void refresh() {
        thongKeAdapter.clear();
        thongKeAdapter.notifyDataSetChanged();
        QueryParameters queryParameters = new QueryParameters();
        String queryClause = "1=1";
        queryParameters.setWhereClause(queryClause);
        for (final FeatureLayer featureLayer : mFeatureLayerS) {
            final ListenableFuture<Long> longListenableFuture = featureLayer.getFeatureTable().queryFeatureCountAsync(queryParameters);
            longListenableFuture.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        Long aLong = longListenableFuture.get();
                        ThongKeAdapter.Item item = new ThongKeAdapter.Item(featureLayer.getName(), aLong);
                        thongKeAdapter.add(item);
                        thongKeAdapter.notifyDataSetChanged();

                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

}
