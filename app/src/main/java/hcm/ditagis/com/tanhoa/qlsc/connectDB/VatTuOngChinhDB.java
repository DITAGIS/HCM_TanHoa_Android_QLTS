package hcm.ditagis.com.tanhoa.qlsc.connectDB;

import android.content.Context;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hcm.ditagis.com.tanhoa.qlsc.R;
import hcm.ditagis.com.tanhoa.qlsc.entities.VatTu;

public class VatTuOngChinhDB implements IDB<List<VatTu>, Boolean, String> {
    private Context mContext;

    public VatTuOngChinhDB(Context mContext) {
        this.mContext = mContext;
    }


    public List<VatTu> find() {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        List<VatTu> vatTus = new ArrayList<>();
        ResultSet rs = null;
        try {
            if (cnn == null)
                return null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = mContext.getString(R.string.sql_select_vattu_ongchinh);
            PreparedStatement mStatement = cnn.prepareStatement(query);

            rs = mStatement.executeQuery();

            while (rs.next()) {
                vatTus.add(new VatTu(rs.getString(1), rs.getString(2), rs.getString(3)));
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
        return vatTus;
    }


    @Override
    public Boolean add(List<VatTu> vatTus) {
        return null;
    }

    @Override
    public Boolean delete(String s) {
        return null;
    }

    @Override
    public Boolean update(List<VatTu> vatTus) {
        return null;
    }

    @Override
    public List<VatTu> find(String s, String k1) {
        return null;
    }

    @Override
    public List<VatTu> find(String s, String k1, String k2) {
        return null;
    }

    @Override
    public List<List<VatTu>> getAll() {
        return null;
    }
}
