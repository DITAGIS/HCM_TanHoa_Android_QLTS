package hcm.ditagis.com.cholon.qlts.connectDB;

import android.content.Context;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import hcm.ditagis.com.cholon.qlts.R;
import hcm.ditagis.com.cholon.qlts.entities.EncodeMD5;
import hcm.ditagis.com.cholon.qlts.entities.entitiesDB.User;


public class LoginDB implements IDB<User, Boolean, String> {
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private Context mContext;

    public LoginDB(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Boolean add(User user) {
        return null;
    }

    @Override
    public Boolean delete(String k) {
        return false;
    }

    @Override
    public Boolean update(User user) {
        return null;
    }

    @Override
    public User find(String userName, String passWord) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        User user = null;
        ResultSet rs = null;
        try {
            if (cnn == null)
                return null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = mContext.getString(R.string.sql_login);
            PreparedStatement mStatement = cnn.prepareStatement(query);

            String passEncoded = (new EncodeMD5()).encode(passWord + "_DITAGIS");


            mStatement.setString(1, userName);
            mStatement.setString(2, passEncoded);
            rs = mStatement.executeQuery();
            while (rs.next()) {
                user = new User();
                user.setUserName(userName);
                user.setDisplayName(rs.getString(mContext.getString(R.string.sql_coloumn_login_displayname)));
                user.setQuan5(true);
                user.setQuan6(true);
                user.setQuan8(true);
                user.setQuanBinhTan(true);
                user.setValid(rs.getBoolean(mContext.getString(R.string.sql_coloumn_login_status)));
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
        return user;
    }

    @Override
    public User find(String s, String k1, String k2) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }


}
