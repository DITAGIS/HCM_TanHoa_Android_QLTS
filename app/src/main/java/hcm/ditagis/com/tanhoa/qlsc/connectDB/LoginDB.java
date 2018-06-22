package hcm.ditagis.com.tanhoa.qlsc.connectDB;

import android.content.Context;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import hcm.ditagis.com.tanhoa.qlsc.R;
import hcm.ditagis.com.tanhoa.qlsc.entities.EncodeMD5;
import hcm.ditagis.com.tanhoa.qlsc.entities.entitiesDB.KhachHang;

public class LoginDB implements IDB<KhachHang, Boolean, String> {
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private Context mContext;

    public LoginDB(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Boolean add(KhachHang khachHang) {
        return null;
    }

    @Override
    public Boolean delete(String k) {
        return false;
    }

    @Override
    public Boolean update(KhachHang khachHang) {
        return null;
    }


    @Override
    public KhachHang find(String userName, String passWord) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        KhachHang khachHang = null;
        ResultSet rs = null;
        try {
            if (cnn == null)
                return null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = mContext.getString(R.string.sql_login);
            PreparedStatement mStatement = cnn.prepareStatement(query);

            String passEncoded1 = (new EncodeMD5()).encode(passWord);
            String passEncoded2 = (new EncodeMD5()).encode(passEncoded1 + mContext.getString(R.string.encode_string));
            String passEncoded = (passEncoded1 + ":" + passEncoded2).replace("-", "");


            mStatement.setString(1, userName);
            mStatement.setString(2, passEncoded);
            rs = mStatement.executeQuery();

            while (rs.next()) {

                khachHang = new KhachHang();
                khachHang.setUserName(userName);
                khachHang.setDisplayName(rs.getString(mContext.getString(R.string.sql_coloumn_login_displayname)));
                khachHang.setTanBinh(rs.getBoolean(mContext.getString(R.string.sql_coloumn_login_tanbinh)));
                khachHang.setTanPhu(rs.getBoolean(mContext.getString(R.string.sql_coloumn_login_tanphu)));
                khachHang.setPhuNhuan(rs.getBoolean(mContext.getString(R.string.sql_coloumn_login_phunhuan)));
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (rs != null && !rs.isClosed())
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return khachHang;
    }

    @Override
    public KhachHang find(String s, String k1, String k2) {
        return null;
    }

    @Override
    public List<KhachHang> getAll() {
        return null;
    }


}
