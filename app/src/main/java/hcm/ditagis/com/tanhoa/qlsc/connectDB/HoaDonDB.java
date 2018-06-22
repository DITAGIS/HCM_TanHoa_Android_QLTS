package hcm.ditagis.com.tanhoa.qlsc.connectDB;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import hcm.ditagis.com.tanhoa.qlsc.entities.entitiesDB.Code_CSC_SanLuong;
import hcm.ditagis.com.tanhoa.qlsc.entities.entitiesDB.Flag;
import hcm.ditagis.com.tanhoa.qlsc.entities.entitiesDB.HoaDon;

public class HoaDonDB implements IDB<HoaDon, Boolean, String> {
    private final String TABLE_NAME = "DocSo";
    private final String SQL_SELECT_GETALL_BY_USERNAME = "SELECT gb,dm,CSCU, MLT2, soThanCu, hieucu, cocu, vitricu, codemoi,tungay, denngay,codecu, tieuthucu FROM " + TABLE_NAME;
    private final String SQL_SELECT_THONGBAO_CHISOGANMOI = "select chiso from thongbao where danhba = ";

    private final String SQL_SELECT_DANHBO = "SELECT DANHBO FROM " + TABLE_NAME;
    private final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " VALUES(?,?,?,?,?)";
    private final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE ClassId=?";
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    PreparedStatement mStatement;
    private String mDot;
    private String mStaffname;

    public String getmDot() {
        return mDot;
    }


    public String getmStaffname() {
        return mStaffname;
    }

    //    private final String SQL_FIND = "SELECT * FROM " + TABLE_NAME + " WHERE ID=?";

    @Override
    public Boolean add(HoaDon e) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        String sql = this.SQL_INSERT;

        try {
            Statement st = cnn.createStatement();
            int result = st.executeUpdate(sql);
            st.close();
            cnn.close();
            return result > 0;

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean delete(String k) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        try {
            PreparedStatement pst = cnn.prepareStatement(this.SQL_DELETE);
            pst.setString(1, k);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean update(HoaDon e) {
//        Connection cnn = ConnectionDB.getInstance().getConnection();
//        try {
//            CallableStatement cbs = cnn.prepareCall(this.SQL_UPDATE);
//            cbs.setString(1, e.getChiSoCu());
//            return cbs.executeUpdate() > 0;
//        } catch (SQLException e1) {
//            e1.printStackTrace();
//        }
//        return false;
        return null;
    }

    @Override
    public HoaDon find(String s, String k1) {
        return null;
    }

    @Override
    public HoaDon find(String s, String k1, String k2) {
        return null;
    }


    @Override
    public List<HoaDon> getAll() {
        return null;
    }

    public List<String> getAllMaLoTrinh() throws SQLException {
        List<String> result = new ArrayList<String>();
        Connection cnn = ConnectionDB.getInstance().getConnection();
        try {
            Statement statement = cnn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT DISTINCT MLT FROM " + this.TABLE_NAME);
            while (rs.next()) {
                String maLoTrinh = rs.getString(1);
                result.add(maLoTrinh);
            }
            rs.close();
            statement.close();
            cnn.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }

    public List<String> getCountHoaDon(String userName, int dot, int nam, int ky) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        List<String> DBs = new ArrayList<>();
        try {
            if (cnn == null)
                return null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Statement statement = cnn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            String dotString = "", kyString = "";
            if (dot < 10)
                dotString = "0" + dot;
            else dotString = dot + "";
            if (ky < 10)
                kyString = "0" + ky;
            else kyString = ky + "";
            String like = dotString + userName + "%";

            //vẫn lấy danh sách code F, phòng trường hợp đọc vét nhưng localdatabase đã xóa
            final ResultSet rs = statement.executeQuery("select danhba from docso where docsoid like '" + nam + kyString + "%' and mlt2 like '" + like + "' and (codemoi ='' " +
//                    ")");
                    "or " +
                    " codemoi like 'F%')");

            while (rs.next()) {
                DBs.add(rs.getString(1));
            }
            rs.close();
            statement.close();

        } catch (SQLException e) {

        }
        return DBs;
    }

    public List<String> getCountHoaDon_Sync(String userName, int dot, int nam, int ky) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        List<String> DBs = new ArrayList<>();
        try {
            if (cnn == null)
                return null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Statement statement = cnn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            String dotString = "", kyString = "";
            if (dot < 10)
                dotString = "0" + dot;
            else dotString = dot + "";
            if (ky < 10)
                kyString = "0" + ky;
            else kyString = ky + "";
            String like = dotString + userName + "%";

            //vẫn lấy danh sách code F, phòng trường hợp đọc vét nhưng localdatabase đã xóa
            final ResultSet rs = statement.executeQuery("select danhba from docso where docsoid like '" + nam + kyString + "%' and mlt2 like '" + like + "' and codemoi <>'' ");

            while (rs.next()) {
                DBs.add(rs.getString(1));
            }
            rs.close();
            statement.close();

        } catch (SQLException e) {

        }
        return DBs;
    }

    public HoaDon getHoaDonByUserName(String userName, String danhBo, int dot, int nam, int ky) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        HoaDon hoaDon = null;
//        String sqlCode_CSC_SanLuong = "SELECT cscu, csmoi, codemoi, tieuthumoi  FROM Docso where docsoid like ? and danhba = ?";
//        String sqlCode_CSC_SanLuong3Ky = "SELECT cscu, csmoi, codemoi, tieuthumoi  FROM Docso where danhba = ? and (ky = ? or ky =? or ky = ?) and nam =? order by ky desc";
        try {
            if (cnn == null)
                return null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String dotString = "", kyString = "";
            if (dot < 10)
                dotString = "0" + dot;
            else dotString = dot + "";
            if (ky < 10)
                kyString = "0" + ky;
            else kyString = ky + "";
            Statement mStatement = cnn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            String id = nam + kyString + danhBo;

//            String query = "select danhba from docso where docsoid like '" + nam + kyString + "%' and mlt2 like '" + like + "' and (gioghi is null or (gioghi is not null and (CodeMoi like 'F%' )) or gioghi < DATEADD(day,1,'2017-01-01'))";
            String query = SQL_SELECT_GETALL_BY_USERNAME + "  where docsoid = '" +id + "'  and (codemoi ='' or " +
                    " codemoi like 'F%')";

            final ResultSet rs = mStatement.executeQuery(query);

            while (rs.next()) {
                String giaBieu = rs.getString(1);
                String dinhMuc = rs.getString(2);
                String chiSoCu = rs.getInt(3) + "";
                String maLoTrinh = rs.getString(4);
                String soThan = rs.getString(5);
                String hieu = rs.getString(6);
                String co = rs.getString(7);
                String viTri = rs.getString(8);
                String codeMoi = rs.getString(9);
                String soNha = "";
                String duong = "";
                String tenKhachHang = "";
                String sdt = "";
                String CSC1 = "";
                String CSC2 = "";
                String CSC3 = "";
                String code1 = "";
                String code2 = "";
                String code3 = "";
                String sanLuong_3 = "";
                String sanLuong_2 = "";
                String sanLuong_1 = "";
                int sh, sx, dv, hc;
                sh = sx = dv = hc = 0;
                String tuNgay = formatter.format(rs.getDate(10));
                String denNgay = formatter.format(rs.getDate(11));
                code1 = rs.getString(12);
                sanLuong_1 = rs.getString(13);
//                mStatement = cnn.prepareStatement("SELECT tenkh,so, duong, sdt, sh,sx,dv,hc FROM KhachHang where MLT2 = ?");
//                mStatement.setString(1, maLoTrinh);
                query = "SELECT tenkh,so, duong, sdt, sh,sx,dv,hc FROM KhachHang where danhba = '" + danhBo + "'";
                ResultSet rs1 = mStatement.executeQuery(query);
                if (rs1.next()) {
                    tenKhachHang = rs1.getString(1);
                    soNha = rs1.getString(2) == null ? "" : rs1.getString(2);
                    duong = rs1.getString(3) == null ? "" : rs1.getString(3);
                    sdt = rs1.getString(4) == null ? "" : rs1.getString(4);
                    sh = rs1.getInt(5);
                    sx = rs1.getInt(6);
                    dv = rs1.getInt(7);
                    hc = rs1.getInt(8);
                }
                List<Integer> kyNamList = get3Ky(ky, nam);
                //neu cung nam
                if (
//                        kyNamList.get(1) == kyNamList.get(3) && kyNamList.get(3) == kyNamList.get(5)
                        false
                        ) {
//                    mStatement = cnn.prepareStatement(sqlCode_CSC_SanLuong3Ky);
//                    mStatement.setString(1, danhBo);
//                    mStatement.setInt(2, kyNamList.get(0));
//                    mStatement.setInt(2, kyNamList.get(2));
//                    mStatement.setInt(2, kyNamList.get(4));
//                    mStatement.setInt(3, kyNamList.get(1));
//                    ResultSet rs2 = mStatement.executeQuery();
//                    if (rs2.next()) {
//                        CSC1 = rs2.getString(1);
//                        code1 = rs2.getString(3);
//                        sanLuong_1 = rs2.getString(4);
//                    }
//                    if (rs2.next()) {
//                        CSC2 = rs2.getString(1);
//                        code2 = rs2.getString(3);
//                        sanLuong_2 = rs2.getString(4);
//                    }
//                    if (rs2.next()) {
//                        CSC3 = rs2.getString(1);
//                        code3 = rs2.getString(3);
//                        sanLuong_3 = rs2.getString(4);
//                    }
                } else {
                    kyString = kyNamList.get(0) + "";
                    if (kyNamList.get(0) < 10)
                        kyString = "0" + kyNamList.get(0);
                    query = "SELECT cscu, codecu, tieuthucu FROM Docso where docsoid = '" + kyNamList.get(1) + kyString + danhBo + "'";


                    ResultSet rs2 = mStatement.executeQuery(query);
                    if (rs2.next()) {
                        CSC1 = rs2.getString(1);
                        code2 = rs2.getString(2);
                        sanLuong_2 = rs2.getString(3);
                    }
                    if (code1.equals("M0")) {
                        query = "select chiso from thongbao where danhba ='" + danhBo + "'";
                        ResultSet rsThongBao = mStatement.executeQuery(query);
                        if (rsThongBao.next()) {
                            chiSoCu = rsThongBao.getString(1);
                        }
                    }
//                mStatement = cnn.prepareStatement(sqlCode_CSC_SanLuong);
//                mStatement.setString(1, danhBo);
                    kyString = kyNamList.get(2) + "";
                    if (kyNamList.get(2) < 10)
                        kyString = "0" + kyNamList.get(2);
                    query = "SELECT cscu, codecu, tieuthucu  FROM Docso where docsoid = '" + kyNamList.get(3) + kyString + danhBo + "'";
//
//                    mStatement.setString(1, kyNamList.get(3) + kyNamList.get(2) + "%");
                    ResultSet rs3 = mStatement.executeQuery(query);
                    if (rs3.next()) {
                        CSC2 = rs3.getString(1);
                        code3 = rs3.getString(2);
                        sanLuong_3 = rs3.getString(3);
                    }

//                mStatement = cnn.prepareStatement(sqlCode_CSC_SanLuong);
//                mStatement.setString(1, danhBo);
                    kyString = kyNamList.get(4) + "";
                    if (kyNamList.get(4) < 10)
                        kyString = "0" + kyNamList.get(4);
                    query = "SELECT cscu  FROM Docso where docsoid = '" + kyNamList.get(5) + kyString + danhBo + "%'";

//                    mStatement.setString(1, kyNamList.get(5) + kyNamList.get(4) + "%");
                    ResultSet rs4 = mStatement.executeQuery(query);
                    if (rs4.next()) {
                        CSC3 = rs4.getString(1);

                    }
                    rs1.close();
//                    rs2.close();
                    rs3.close();
                    rs4.close();
                }
//                int chiSoGanMoi = 0;
                //Lay chi so gan moi
//                if (code1.equals("M0")) {
//                    ResultSet rsChiSoGanMoi = mStatement.executeQuery(SQL_SELECT_THONGBAO_CHISOGANMOI + "'" + danhBo + "'");
//                    if (rsChiSoGanMoi.next()) {
//                        chiSoGanMoi = rsChiSoGanMoi.getInt(1);
//                    }
//                }

                hoaDon = new HoaDon(dotString, danhBo, tenKhachHang, soNha, duong, giaBieu, dinhMuc, ky + "", chiSoCu, maLoTrinh,
                        sdt, Flag.UNREAD);
                Code_CSC_SanLuong code_csu_sanLuong = new Code_CSC_SanLuong(code1, code2, code3,
                        CSC1, CSC2, CSC3,
                        sanLuong_1, sanLuong_2, sanLuong_3);
                hoaDon.setCode_CSC_SanLuong(code_csu_sanLuong);
                hoaDon.setSoThan(soThan);
                hoaDon.setHieu(hieu);
                hoaDon.setCo(co);
                hoaDon.setViTri(viTri);
                hoaDon.setCodeMoi(codeMoi);
                hoaDon.setSh(sh);
                hoaDon.setSx(sx);
                hoaDon.setDv(dv);
                hoaDon.setHc(hc);
                hoaDon.setTuNgay(tuNgay);
                hoaDon.setDenNgay(denNgay);
                List<Integer> chiso = getCSGo(hoaDon.getDanhBo());
                hoaDon.setCsgo(chiso.get(0));
                hoaDon.setCsgan(chiso.get(1));
                hoaDon.setId(id);
            }
            rs.close();
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return hoaDon;
    }

    public List<Integer> getCSGo(String danhBo) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        int csgo = -1, csgan = 0;
        List<Integer> chiso = new ArrayList<>();
        chiso.add(csgo);
        chiso.add(csgan);
        try {
            if (cnn == null)
                return chiso;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            mStatement = cnn.prepareStatement("select csgo,csgan from baothay where danhba = ? ");
            mStatement.setString(1, danhBo);

            ResultSet rs = mStatement.executeQuery();

            while (rs.next()) {
                csgo = rs.getInt(1);
                csgan = rs.getInt(2);
            }

            rs.close();
        } catch (SQLException e) {

            e.printStackTrace();
        }
        chiso.clear();
        chiso.add(csgo);
        chiso.add(csgan);
        return chiso;
    }

    public void closeStatement() {
        if (mStatement != null)
            try {
                mStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }


    private List<Integer> get3Ky(int ky, int nam) {
        List<Integer> result = new ArrayList<>();
        int[] kyNam = new int[2];
        for (int i = 1; i <= 3; i++) {
            kyNam = getKyNam(ky, nam, i);
            result.add(kyNam[0]);
            result.add(kyNam[1]);
        }
        return result;
    }

    private int[] getKyNam(int ky, int nam, int prev) {
        int[] result = new int[2];
        int delta = ky - prev;
        if (delta > 0) {
            result[0] = ky - prev;
            result[1] = nam;
        } else if (delta == 0) {
            result[0] = 12;
            result[1] = nam - 1;
        } else if (delta == -1) {
            result[0] = 11;
            result[1] = nam - 1;
        } else if (delta == -2) {
            result[0] = 10;
            result[1] = nam - 1;
        }
        return result;
    }


}
