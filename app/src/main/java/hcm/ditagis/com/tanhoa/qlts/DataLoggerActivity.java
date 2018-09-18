package hcm.ditagis.com.tanhoa.qlts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.ServiceFeatureTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import hcm.ditagis.com.tanhoa.qlts.adapter.DataLoggerAdapter;
import hcm.ditagis.com.tanhoa.qlts.adapter.FeatureAdapter;
import hcm.ditagis.com.tanhoa.qlts.async.QueryFeatureAsync;
import hcm.ditagis.com.tanhoa.qlts.entities.entitiesDB.LayerInfoDTG;
import hcm.ditagis.com.tanhoa.qlts.entities.entitiesDB.ListObjectDB;
import hcm.ditagis.com.tanhoa.qlts.socket.DApplication;


public class DataLoggerActivity extends AppCompatActivity {
    private TextView txtTongItem;
    private ServiceFeatureTable serviceLayerSelected;
    private ServiceFeatureTable apLucBatThuongTBL;
    private DataLoggerAdapter dataLoggerAdapter;
    private DApplication mDApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);
        mDApplication = (DApplication) getApplication();
        List<DataLoggerAdapter.Item> items = new ArrayList<>();

        for (final LayerInfoDTG layerInfoDTG : ListObjectDB.getInstance().getLstFeatureLayerDTG()) {
            String url = layerInfoDTG.getUrl();
            if (!layerInfoDTG.getUrl().startsWith("http"))
                url = "http:" + layerInfoDTG.getUrl();
            if (layerInfoDTG.getId() != null && layerInfoDTG.getId().equals("aplucbatthuongTBL")) {
                apLucBatThuongTBL = new ServiceFeatureTable(url);
            }
            if (layerInfoDTG.getId().equals(getString(hcm.ditagis.com.tanhoa.qlts.R.string.IDLayer_Basemap)) ||
                    layerInfoDTG.getId().substring(layerInfoDTG.getId().length() - 3).equals("TBL") || !layerInfoDTG.isView())
                continue;
            DataLoggerAdapter.Item item = new DataLoggerAdapter.Item();
            item.setUrlLayer(url);

            if (layerInfoDTG.getId() != null && layerInfoDTG.getId().equals(getString(R.string.diemsucoLYR))) {
                serviceLayerSelected = new ServiceFeatureTable(url);
                item.setMota("Tất cả");
                items.add(item);
            }

            if (layerInfoDTG.isView()) {
                item.setMota(layerInfoDTG.getTitleLayer());
                items.add(item);
            }
        }

//        TimePeriodReport timePeriodReport = new TimePeriodReport(this);

//        items = timePeriodReport.getItems();
        dataLoggerAdapter = new DataLoggerAdapter(this, items);

        this.txtTongItem = this.findViewById(R.id.txtTongItem);

        getQueryDiemDanhGiaAsync(items.get(0));
    }


    private void getQueryApLucBatThuong() {
        String whereClause = "1=1";
    }

    private void getQueryDiemDanhGiaAsync(DataLoggerAdapter.Item item) {
        String whereClause = "1 = 1";
        ListView listView = (ListView) findViewById(R.id.listview);
        final List<FeatureAdapter.Item> items = new ArrayList<>();
        final FeatureAdapter adapter = new FeatureAdapter(this, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                FeatureAdapter.Item item = new FeatureAdapter.Item();
                item.setValue_TrangThai(adapter.getItems().get(position).getValue_TrangThai());
                item.setID(adapter.getItems().get(position).getID());
                item.setLayerID(adapter.getItems().get(position).getLayerID());
                mDApplication.setItemDataLogger(item);
                finish();
            }
        });
        if (apLucBatThuongTBL != null)
            new QueryFeatureAsync(this, apLucBatThuongTBL, txtTongItem, adapter, new QueryFeatureAsync.AsyncResponse() {
                public void processFinish(List<Feature> features) {
                }
            }).execute(whereClause);
    }


}
