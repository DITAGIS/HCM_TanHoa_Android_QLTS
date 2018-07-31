package hcm.ditagis.com.tanhoa.qlts.utities;

import java.text.SimpleDateFormat;

import hcm.ditagis.com.tanhoa.qlts.adapter.SettingsAdapter;

/**
 * Created by ThanLe on 3/1/2018.
 */

public class Constant {
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd_MM_yyyy");
    private SettingsAdapter.Item[] mSettingsItems;
    public static final String OBJECTID = "OBJECTID";
    public static final String IDSU_CO = "IDSuCo";
    public static final String VI_TRI = "ViTri";
    public static final String TRANG_THAI = "TrangThai";
    public static final String NGAY_CAP_NHAT = "NgayCapNhat";
    public static final String NGAY_THONG_BAO = "NgayThongBao";
    public static final int REQUEST_CODE = 99;
    public static final String[] CODEID_DISTRICT = {null, "768", "766", "767"};
    public static final String[] CODE_PHANLOAI = {null, "1", "2"};

    public static final String NAME_DIEMSUCO = "DIEMSUCO";

    private static Constant mInstance = null;

    public static Constant getInstance() {
        if (mInstance == null)
            mInstance = new Constant();
        return mInstance;
    }

    private Constant() {
        mSettingsItems = new SettingsAdapter.Item[]{
                new SettingsAdapter.Item("Phương thức thêm điểm sự cố", ""),
                new SettingsAdapter.Item("Tùy chọn tìm kiếm", ""),
                new SettingsAdapter.Item("Bố cục giao diện", ""),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
        };
    }

    public SettingsAdapter.Item[] getSettingsItems() {
        return mSettingsItems;
    }


}
