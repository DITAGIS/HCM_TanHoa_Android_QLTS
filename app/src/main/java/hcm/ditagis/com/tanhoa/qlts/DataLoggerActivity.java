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


public class DataLoggerActivity extends AppCompatActivity {
    private TextView txtTongItem;
    private ServiceFeatureTable serviceLayerSelected;
    private ServiceFeatureTable apLucBatThuongTBL;
    private DataLoggerAdapter dataLoggerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);
        List<DataLoggerAdapter.Item> items = new ArrayList<>();

        for (final LayerInfoDTG layerInfoDTG : ListObjectDB.getInstance().getLstFeatureLayerDTG()) {
            String url = layerInfoDTG.getUrl();
            if (!layerInfoDTG.getUrl().startsWith("http"))
                url = "http:" + layerInfoDTG.getUrl();
            if(layerInfoDTG.getId() != null && layerInfoDTG.getId().equals("aplucbatthuongTBL")){
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

            if(layerInfoDTG.isView()){
                item.setMota(layerInfoDTG.getTitleLayer());
                items.add(item);
            }
        }

//        TimePeriodReport timePeriodReport = new TimePeriodReport(this);

//        items = timePeriodReport.getItems();
        dataLoggerAdapter = new DataLoggerAdapter(this, items);

        this.txtTongItem = this.findViewById(R.id.txtTongItem);
        ((LinearLayout) DataLoggerActivity.this.findViewById(R.id.layout_thongke_thoigian)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLayerSelection();
            }
        });
        getQueryDiemDanhGiaAsync(items.get(0));
    }

    private void showLayerSelection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
        View layout = getLayoutInflater().inflate(R.layout.layout_listview_thongketheothoigian, null);
        ListView listView = (ListView) layout.findViewById(R.id.lstView_thongketheothoigian);
        listView.setAdapter(dataLoggerAdapter);
        builder.setView(layout);
        final AlertDialog selectLayerDialog = builder.create();
        selectLayerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectLayerDialog.show();
        final List<DataLoggerAdapter.Item> finalItems = dataLoggerAdapter.getItems();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final DataLoggerAdapter.Item itemAtPosition = (DataLoggerAdapter.Item) parent.getItemAtPosition(position);
                selectLayerDialog.dismiss();
                serviceLayerSelected = new ServiceFeatureTable(itemAtPosition.getUrlLayer());
                if (itemAtPosition.getId() == finalItems.size()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(DataLoggerActivity.this, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
                    View layout = getLayoutInflater().inflate(R.layout.layout_thongke_thoigiantuychinh, null);
                    builder.setView(layout);
                    final AlertDialog tuychinhDateDialog = builder.create();
                    tuychinhDateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    tuychinhDateDialog.show();
                    final EditText edit_thongke_tuychinh_ngaybatdau = (EditText) layout.findViewById(R.id.edit_thongke_tuychinh_ngaybatdau);
                    final EditText edit_thongke_tuychinh_ngayketthuc = (EditText) layout.findViewById(R.id.edit_thongke_tuychinh_ngayketthuc);
                    if (itemAtPosition.getThoigianbatdau() != null)
                        edit_thongke_tuychinh_ngaybatdau.setText(itemAtPosition.getThoigianbatdau());
                    if (itemAtPosition.getThoigianketthuc() != null)
                        edit_thongke_tuychinh_ngayketthuc.setText(itemAtPosition.getThoigianketthuc());

                    final StringBuilder finalThoigianbatdau = new StringBuilder();
                    finalThoigianbatdau.append(itemAtPosition.getThoigianbatdau());
                    edit_thongke_tuychinh_ngaybatdau.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDateTimePicker(edit_thongke_tuychinh_ngaybatdau, finalThoigianbatdau, "START");
                        }
                    });
                    final StringBuilder finalThoigianketthuc = new StringBuilder();
                    finalThoigianketthuc.append(itemAtPosition.getThoigianketthuc());
                    edit_thongke_tuychinh_ngayketthuc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDateTimePicker(edit_thongke_tuychinh_ngayketthuc, finalThoigianketthuc, "FINISH");
                        }
                    });

                    layout.findViewById(R.id.btn_layngaythongke).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (kiemTraThoiGianNhapVao(finalThoigianbatdau.toString(), finalThoigianketthuc.toString())) {
                                tuychinhDateDialog.dismiss();
                                itemAtPosition.setThoigianbatdau(finalThoigianbatdau.toString());
                                itemAtPosition.setThoigianketthuc(finalThoigianketthuc.toString());
                                itemAtPosition.setThoigianhienthi(edit_thongke_tuychinh_ngaybatdau.getText() + " - " + edit_thongke_tuychinh_ngayketthuc.getText());
                                dataLoggerAdapter.notifyDataSetChanged();
                                getQueryDiemDanhGiaAsync(itemAtPosition);
                            }
                        }
                    });

                } else {
                    getQueryDiemDanhGiaAsync(itemAtPosition);
                }
            }
        });
    }

    private boolean kiemTraThoiGianNhapVao(String startDate, String endDate) {
        if (startDate == "" || endDate == "") return false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date1 = dateFormat.parse(startDate);
            Date date2 = dateFormat.parse(endDate);
            if (date1.after(date2)) {
                return false;
            } else return true;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public void showDateTimePicker(final EditText editText, final StringBuilder output, final String typeInput) {
        output.delete(0, output.length());
        final View dialogView = View.inflate(this, R.layout.date_time_picker, null);
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();
        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                Calendar calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                String displaytime = (String) DateFormat.format(getString(R.string.format_time_day_month_year), calendar.getTime());
                String format = null;
                if (typeInput.equals("START")) {
                    calendar.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
                    calendar.clear(Calendar.MINUTE);
                    calendar.clear(Calendar.SECOND);
                    calendar.clear(Calendar.MILLISECOND);
                } else if (typeInput.equals("FINISH")) {
                    calendar.set(Calendar.HOUR_OF_DAY, 23);
                    calendar.set(Calendar.MINUTE, 59);
                    calendar.set(Calendar.SECOND, 59);
                    calendar.set(Calendar.MILLISECOND, 999);
                }
                SimpleDateFormat dateFormatGmt = new SimpleDateFormat(getString(R.string.format_day_yearfirst));
                dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
                format = dateFormatGmt.format(calendar.getTime());
                editText.setText(displaytime);
                output.append(format);
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(dialogView);
        alertDialog.show();

    }
    private void getQueryApLucBatThuong(){
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
                returnIntent.putExtra(getString(R.string.ID), adapter.getItems().get(position).getID());
                returnIntent.putExtra(getString(R.string.LayerID), adapter.getItems().get(position).getLayerID());
                setResult(Activity.RESULT_OK, returnIntent);
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
