package hcm.ditagis.com.tanhoa.qlsc;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import hcm.ditagis.com.tanhoa.qlsc.adapter.ThongKeAdapter;
import hcm.ditagis.com.tanhoa.qlsc.entities.entitiesDB.KhachHang;
import hcm.ditagis.com.tanhoa.qlsc.utities.TimePeriodReport;

public class ThongKeActivity extends AppCompatActivity {
    private TextView mTxtTongSuCo, mTxtChuaSua, mTxtDangSua, mTxtHoanThanh;
    private TextView mTxtPhanTramChuaSua, mTxtPhanTramDangSua, mTxtPhanTramHoanThanh;
    private ServiceFeatureTable mServiceFeatureTable;
    private ThongKeAdapter mThongKeAdapter;
    private int mChuaSuaChua, mDangSuaChua, mHoanThanh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        mServiceFeatureTable = new ServiceFeatureTable(getResources().getString(R.string.URL_DIEM_SU_CO));
        TimePeriodReport timePeriodReport = new TimePeriodReport(this);
        List<ThongKeAdapter.Item> items = timePeriodReport.getItems();
        mThongKeAdapter = new ThongKeAdapter(this, items);

        this.mTxtTongSuCo = this.findViewById(R.id.txtTongSuCo);
        this.mTxtChuaSua = this.findViewById(R.id.txtChuaSua);
        this.mTxtDangSua = this.findViewById(R.id.txtDangSua);
        this.mTxtHoanThanh = this.findViewById(R.id.txtHoanThanh);
        this.mTxtPhanTramChuaSua = this.findViewById(R.id.txtPhanTramChuaSua);
        this.mTxtPhanTramDangSua = this.findViewById(R.id.txtPhanTramDangSua);
        this.mTxtPhanTramHoanThanh = this.findViewById(R.id.txtPhanTramHoanThanh);
        ThongKeActivity.this.findViewById(R.id.layout_thongke_thoigian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSelectTime();
            }
        });
        query(items.get(0));
    }

    private void showDialogSelectTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
        @SuppressLint("InflateParams") View layout = getLayoutInflater().inflate(R.layout.layout_listview_thongketheothoigian, null);
        ListView listView = layout.findViewById(R.id.lstView_thongketheothoigian);
        listView.setAdapter(mThongKeAdapter);
        builder.setView(layout);
        final AlertDialog selectTimeDialog = builder.create();
        selectTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectTimeDialog.show();
        final List<ThongKeAdapter.Item> finalItems = mThongKeAdapter.getItems();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ThongKeAdapter.Item itemAtPosition = (ThongKeAdapter.Item) parent.getItemAtPosition(position);
                selectTimeDialog.dismiss();
                if (itemAtPosition.getId() == finalItems.size()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ThongKeActivity.this, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
                    @SuppressLint("InflateParams") View layout = getLayoutInflater().inflate(R.layout.layout_thongke_thoigiantuychinh, null);
                    builder.setView(layout);
                    final AlertDialog tuychinhDateDialog = builder.create();
                    tuychinhDateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    tuychinhDateDialog.show();
                    final EditText edit_thongke_tuychinh_ngaybatdau = layout.findViewById(R.id.edit_thongke_tuychinh_ngaybatdau);
                    final EditText edit_thongke_tuychinh_ngayketthuc = layout.findViewById(R.id.edit_thongke_tuychinh_ngayketthuc);
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
                                mThongKeAdapter.notifyDataSetChanged();
                                query(itemAtPosition);
                            }
                        }
                    });

                } else {
                    query(itemAtPosition);
                }
            }
        });
    }

    private boolean kiemTraThoiGianNhapVao(String startDate, String endDate) {
        if (startDate.equals("") || endDate.equals("")) return false;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date1 = dateFormat.parse(startDate);
            Date date2 = dateFormat.parse(endDate);
            if (date1.after(date2)) {
                return false;
            } else return true;
        } catch (ParseException e) {
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
                DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
                Calendar calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                String displaytime = (String) DateFormat.format(getString(R.string.format_time_day_month_year), calendar.getTime());
                String format;
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
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatGmt = new SimpleDateFormat(getString(R.string.format_day_yearfirst));
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

    private void query(ThongKeAdapter.Item item) {
        mChuaSuaChua = mDangSuaChua  = mHoanThanh = 0;
        ((TextView) ThongKeActivity.this.findViewById(R.id.txt_thongke_mota)).setText(item.getMota());
        TextView txtThoiGian = ThongKeActivity.this.findViewById(R.id.txt_thongke_thoigian);
        if (item.getThoigianhienthi() == null) txtThoiGian.setVisibility(View.GONE);
        else {
            txtThoiGian.setText(item.getThoigianhienthi());
            txtThoiGian.setVisibility(View.VISIBLE);
        }
        KhachHang khachHang = KhachHang.khachHangDangNhap;
        String whereClause = "";
        if (item.getThoigianbatdau() == null || item.getThoigianketthuc() == null) {
            if (khachHang.isPhuNhuan())
                whereClause += String.format("%s = '%s' or ", getString(R.string.Field_SuCo_MaQuan), getString(R.string.QuanPhuNhuanCode));
            if (khachHang.isTanBinh())
                whereClause += String.format("%s = '%s' or ", getString(R.string.Field_SuCo_MaQuan), getString(R.string.QuanTanBinhCode));
            if (khachHang.isTanPhu())
                whereClause += String.format("%s = '%s' or ", getString(R.string.Field_SuCo_MaQuan), getString(R.string.QuanTanPhuCode));
            whereClause += " 1 = 1";
        } else {

            whereClause = String.format("(%s >= date '%s' and %s <= date '%s') and (",
                    getString(R.string.Field_SuCo_NgayThongBao), item.getThoigianbatdau(),
                    getString(R.string.Field_SuCo_NgayThongBao), item.getThoigianketthuc());
            if (khachHang.isPhuNhuan())
                whereClause += String.format("%s = '%s' or ", getString(R.string.Field_SuCo_MaQuan), getString(R.string.QuanPhuNhuanCode));
            if (khachHang.isTanBinh())
                whereClause += String.format("%s = '%s' or ", getString(R.string.Field_SuCo_MaQuan), getString(R.string.QuanTanBinhCode));
            if (khachHang.isTanPhu())
                whereClause += String.format("%s = '%s' or ", getString(R.string.Field_SuCo_MaQuan), getString(R.string.QuanTanPhuCode));
            whereClause += " 1 = 1)";
        }
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setWhereClause(whereClause);

        List<String> outFields = new ArrayList<>();
        outFields.add(getString(R.string.trangthai));

//        final ListenableFuture<FeatureQueryResult> feature =
//                mServiceFeatureTable.populateFromServiceAsync(queryParameters, true, outFields);
        final ListenableFuture<FeatureQueryResult> feature = mServiceFeatureTable.queryFeaturesAsync(queryParameters, ServiceFeatureTable.QueryFeatureFields.LOAD_ALL);
        feature.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult result = feature.get();
                    Iterator<Feature> iterator = result.iterator();
                    Feature item;
                    while (iterator.hasNext()) {
                        item = iterator.next();
//                    for (Object i : result) {
//                        Feature item = (Feature) i;
                        Object value = item.getAttributes().get(getString(R.string.trangthai));
                        int trangThai = getResources().getInteger(R.integer.trang_thai_chua_sua_chua);
                        if (value != null) {
                            trangThai = Integer.parseInt(value.toString());
                        }
                        if (trangThai == getResources().getInteger(R.integer.trang_thai_chua_sua_chua))
                            mChuaSuaChua++;
                        else if (trangThai == getResources().getInteger(R.integer.trang_thai_dang_sua_chua))
                            mDangSuaChua++;
                        else if (trangThai == getResources().getInteger(R.integer.trang_thai_hoan_thanh))
                            mHoanThanh++;

                    }
                    displayReport();

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void displayReport() {
        int tongloaitrangthai = mChuaSuaChua + mDangSuaChua + mHoanThanh;
        mTxtTongSuCo.setText(getString(R.string.nav_thong_ke_tong_su_co) + tongloaitrangthai);
        mTxtChuaSua.setText(mChuaSuaChua + "");
        mTxtDangSua.setText(mDangSuaChua + "");
        mTxtHoanThanh.setText(mHoanThanh + "");
        double percentChuaSua, percentDangSua, percentDaSua, percentHoanThanh;
        percentChuaSua = percentDangSua = percentDaSua = percentHoanThanh = 0.0;
        if (tongloaitrangthai > 0) {
            percentChuaSua = mChuaSuaChua * 100 / tongloaitrangthai;
            percentDangSua = mDangSuaChua * 100 / tongloaitrangthai;
            percentHoanThanh = mHoanThanh * 100 / tongloaitrangthai;
        }
        mTxtPhanTramChuaSua.setText(percentChuaSua + "%");
        mTxtPhanTramDangSua.setText(percentDangSua + "%");
        mTxtPhanTramHoanThanh.setText(percentHoanThanh + "%");
        PieChart mChart = findViewById(R.id.piechart);
        mChart = configureChart(mChart);

        mChart = setData(mChart);
        mChart.animateXY(1500, 1500);
    }

    public PieChart configureChart(PieChart chart) {
        chart.setHoleColor(getResources().getColor(android.R.color.background_dark));
        chart.setHoleRadius(60f);
        chart.setDescription("");
        chart.setTransparentCircleRadius(5f);
        chart.setDrawCenterText(true);
        chart.setDrawHoleEnabled(false);
        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);

        chart.setUsePercentValues(false);

        Legend legend = chart.getLegend();
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        return chart;
    }

    private PieChart setData(PieChart chart) {
        ArrayList<Entry> yVals1 = new ArrayList<>();

        yVals1.add(new Entry(mChuaSuaChua, 0));
        yVals1.add(new Entry(mDangSuaChua, 1));
        yVals1.add(new Entry(mHoanThanh, 2));
        ArrayList<String> xVals = new ArrayList<>();
        xVals.add(getString(R.string.nav_thong_ke_chua_sua_chua));
        xVals.add(getString(R.string.nav_thong_ke_dang_sua_chua));
        xVals.add(getString(R.string.nav_thong_ke_hoan_thanh));


        PieDataSet set1 = new PieDataSet(yVals1, "");
        set1.setSliceSpace(0f);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(android.R.color.holo_red_light));
        colors.add(getResources().getColor(android.R.color.holo_orange_light));
        colors.add(getResources().getColor(android.R.color.holo_green_light));
        set1.setColors(colors);
        PieData data = new PieData(xVals, set1);
        data.setValueTextSize(15);
        set1.setValueTextSize(0);
        chart.setData(data);
        chart.highlightValues(null);
//        chart.invalidate();
        return chart;
    }
}
