package hcm.ditagis.com.tanhoa.qlts.utities;

import java.text.SimpleDateFormat;

/**
 * Created by ThanLe on 3/1/2018.
 */

public class Constant {
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd_MM_yyyy");
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

    public class URL_API {
        private static final String SERVER_API = "http://113.161.88.180:798/apiv1/api";
        public static final String LOGIN = SERVER_API + "/Login";
        public static final String PROFILE = SERVER_API + "/Account/Profile";
        public static final String LAYER_INFO = SERVER_API + "/Account/LayerInfo";
        public static final String IS_ACCESS = SERVER_API + "/Account/IsAccess/m_qlts";
    }

    private static Constant mInstance = null;

    public static Constant getInstance() {
        if (mInstance == null)
            mInstance = new Constant();
        return mInstance;
    }

    private Constant() {
    }


    public static class POPUP_QUERY_TYPE {
        public static final String CLICKMAP = "CLICKMAP";
        public static final String SEARCH = "SEARCH";
        public static final String DATALOGGER = "DATALOGGER";
    }

    public static class VALUE_TRANGTHAI {
        public static final short CHUA_KIEM_TRA = 1;
        public static final short DA_KIEM_TRA = 2;
    }
}
