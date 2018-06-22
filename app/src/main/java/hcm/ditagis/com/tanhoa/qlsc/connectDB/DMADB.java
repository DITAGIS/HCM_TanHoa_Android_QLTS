package hcm.ditagis.com.tanhoa.qlsc.connectDB;

import android.content.Context;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import hcm.ditagis.com.tanhoa.qlsc.R;

public class DMADB implements IDB<List<String>, Boolean, String> {
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private Context mContext;

    public DMADB(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public Boolean add(List<String> strings) {
        return null;
    }

    @Override
    public Boolean delete(String s) {
        return null;
    }

    @Override
    public Boolean update(List<String> strings) {
        return null;
    }

    @Override
    public List<String> find(String userName, String passWord) {
        return null;
    }

    public List<String> find() {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        List<String> lstDMA = new ArrayList<>();
        ResultSet rs = null;
        try {
            if (cnn == null)
                return null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = mContext.getString(R.string.sql_select_ma_dma);
            PreparedStatement mStatement = cnn.prepareStatement(query);

            rs = mStatement.executeQuery();

            while (rs.next()) {

                lstDMA.add(rs.getString(1));
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
        return lstDMA;
    }

    @Override
    public List<String> find(String s, String k1, String k2) {
        return null;
    }

    @Override
    public List<List<String>> getAll() {
        return null;
    }


}
