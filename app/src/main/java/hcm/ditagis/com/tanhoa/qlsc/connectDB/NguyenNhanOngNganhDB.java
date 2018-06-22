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

public class NguyenNhanOngNganhDB implements IDB<HashMap<String, String>, Boolean, String> {
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private Context mContext;

    public NguyenNhanOngNganhDB(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public Boolean add(HashMap<String, String> stringStringHashMap) {
        return null;
    }

    @Override
    public Boolean delete(String s) {
        return null;
    }

    @Override
    public Boolean update(HashMap<String, String> stringStringHashMap) {
        return null;
    }

    @Override
    public HashMap<String, String> find(String s, String k1) {
        return null;
    }

    @Override
    public HashMap<String, String> find(String s, String k1, String k2) {
        return null;
    }

    public HashMap<String, String> find() {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        HashMap<String, String> hashMap = new HashMap<>();
        ResultSet rs = null;
        try {
            if (cnn == null)
                return null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = mContext.getString(R.string.sql_select_nguyennhan_ongnganh);
            PreparedStatement mStatement = cnn.prepareStatement(query);

            rs = mStatement.executeQuery();

            while (rs.next()) {
                hashMap.put(rs.getString(1), rs.getString(2));
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
    public List<HashMap<String, String>> getAll() {
        return null;
    }
}
