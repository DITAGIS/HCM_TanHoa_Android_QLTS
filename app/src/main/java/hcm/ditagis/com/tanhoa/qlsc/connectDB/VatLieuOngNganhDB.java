package hcm.ditagis.com.tanhoa.qlsc.connectDB;

import android.content.Context;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import hcm.ditagis.com.tanhoa.qlsc.R;

public class VatLieuOngNganhDB implements IDB<HashMap<Integer, String>, Boolean, String> {
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private Context mContext;

    public VatLieuOngNganhDB(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Boolean add(HashMap<Integer, String> integerStringHashMap) {
        return null;
    }

    @Override
    public Boolean delete(String s) {
        return null;
    }

    @Override
    public Boolean update(HashMap<Integer, String> integerStringHashMap) {
        return null;
    }

    @Override
    public HashMap<Integer, String> find(String s, String k1) {
        return null;
    }

    @Override
    public HashMap<Integer, String> find(String s, String k1, String k2) {
        return null;
    }

    public HashMap<Integer, String> find() {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        HashMap<Integer, String> hashMap = new HashMap<>();
        ResultSet rs = null;
        try {
            if (cnn == null)
                return null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = mContext.getString(R.string.sql_select_vatlieu_ongnganh);
            PreparedStatement mStatement = cnn.prepareStatement(query);

            rs = mStatement.executeQuery();

            while (rs.next()) {
                hashMap.put(rs.getInt(1), rs.getString(2));
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
        return hashMap;
    }

    @Override
    public List<HashMap<Integer, String>> getAll() {
        return null;
    }
}
