package hcm.ditagis.com.cholon.qlts.connectDB;

import android.content.Context;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hcm.ditagis.com.cholon.qlts.R;
import hcm.ditagis.com.cholon.qlts.entities.entitiesDB.LayerInfoDTG;


public class ListFeatureLayerDTGDB implements IDB<HashMap<String, String>, Boolean, String> {
    private Context mContext;

    public ListFeatureLayerDTGDB(Context mContext) {
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

    public List<LayerInfoDTG> find(String account) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        List<LayerInfoDTG> layerDTGS = new ArrayList<>();
        ResultSet rs = null;
        try {
            if (cnn == null)
                return null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = mContext.getString(R.string.sql_select_sys_layer);
            PreparedStatement mStatement = cnn.prepareStatement(query);
            mStatement.setString(1, account);
            rs = mStatement.executeQuery();

            boolean isAddLayerThematic = false;
            while (rs.next()) {
                if (rs.getString(mContext.getString(R.string.sql_coloumn_sys_id)).equals(mContext.getString(R.string.IDLayer_Basemap)))
                    layerDTGS.add(new LayerInfoDTG(rs.getString(mContext.getString(R.string.sql_coloumn_sys_id)),
                            rs.getString(mContext.getString(R.string.sql_coloumn_sys_title)), rs.getString(mContext.getString(R.string.sql_coloumn_sys_url)),
                            rs.getBoolean(mContext.getString(R.string.sql_coloumn_sys_iscreate)), rs.getBoolean(mContext.getString(R.string.sql_coloumn_sys_isdelete)),
                            rs.getBoolean(mContext.getString(R.string.sql_coloumn_sys_isedit)), rs.getBoolean(mContext.getString(R.string.sql_coloumn_sys_isview)),
                            rs.getString(mContext.getString(R.string.sql_coloumn_sys_outfield)), rs.getString(mContext.getString(R.string.sql_coloumn_sys_definition))));
                else if (!isAddLayerThematic) {
                    layerDTGS.add(new LayerInfoDTG(rs.getString(mContext.getString(R.string.sql_coloumn_sys_id)),
                            rs.getString(mContext.getString(R.string.sql_coloumn_sys_title)), rs.getString(mContext.getString(R.string.sql_coloumn_sys_url)),
                            rs.getBoolean(mContext.getString(R.string.sql_coloumn_sys_iscreate)), rs.getBoolean(mContext.getString(R.string.sql_coloumn_sys_isdelete)),
                            rs.getBoolean(mContext.getString(R.string.sql_coloumn_sys_isedit)), rs.getBoolean(mContext.getString(R.string.sql_coloumn_sys_isview)),
                            rs.getString(mContext.getString(R.string.sql_coloumn_sys_outfield)), rs.getString(mContext.getString(R.string.sql_coloumn_sys_definition))));
                    isAddLayerThematic = true;
                }
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
        return layerDTGS;
    }

    @Override
    public List<HashMap<String, String>> getAll() {
        return null;
    }
}
