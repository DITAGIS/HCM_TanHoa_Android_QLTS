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
import hcm.ditagis.com.tanhoa.qlsc.entities.HoSoVatTuSuCo;

public class HoSoVatTuSuCoDB implements IDB<HoSoVatTuSuCo, Boolean, String> {
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private Context mContext;

    public HoSoVatTuSuCoDB(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public Boolean add(HoSoVatTuSuCo hoSoVatTuSuCo) {
        return null;
    }

    @Override
    public Boolean delete(String s) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        boolean result = false;
        try {
            if (cnn == null)
                return false;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            PreparedStatement deleteStatement = cnn.prepareStatement(mContext.getString(R.string.sql_delete_hosovattu_suco));
            deleteStatement.setString(1, s);
            result = deleteStatement.execute();


        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {

        }
        return result;
    }

    @Override
    public Boolean update(HoSoVatTuSuCo hoSoVatTuSuCo) {
        return null;
    }

    @Override
    public HoSoVatTuSuCo find(String s, String k1) {
        return null;
    }

    @Override
    public HoSoVatTuSuCo find(String s, String k1, String k2) {
        return null;
    }


    @Override
    public List<HoSoVatTuSuCo> getAll() {

        return null;
    }

    public List<HoSoVatTuSuCo> find(String idSuCo) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        List<HoSoVatTuSuCo> hoSoVatTuSuCos = new ArrayList<>();
        ResultSet rs = null;
        try {
            if (cnn == null)
                return null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = mContext.getString(R.string.sql_select_hosovattu_suco);
            PreparedStatement mStatement = cnn.prepareStatement(query);
            mStatement.setString(1, idSuCo);
            rs = mStatement.executeQuery();

            while (rs.next()) {
                hoSoVatTuSuCos.add(new HoSoVatTuSuCo(idSuCo, rs.getDouble(1), rs.getString(2), rs.getString(3), rs.getString(4)));
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
        return hoSoVatTuSuCos;
    }


    public boolean insert(HoSoVatTuSuCo hoSoVatTuSuCo) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        int result = 0;
        try {
            if (cnn == null)
                return false;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String query = mContext.getString(R.string.sql_insert_hosovattu_suco);
            PreparedStatement insertStatement = cnn.prepareStatement(query);
            insertStatement.setString(1, hoSoVatTuSuCo.getIdSuCo());
            insertStatement.setDouble(2, hoSoVatTuSuCo.getSoLuong());
            insertStatement.setString(3, hoSoVatTuSuCo.getMaVatTu());

            result = insertStatement.executeUpdate();

        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {

        }
        return result > 0;
    }
}
