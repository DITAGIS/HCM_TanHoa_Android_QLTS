package hcm.ditagis.com.cholon.qlts.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import hcm.ditagis.com.cholon.qlts.R;
import hcm.ditagis.com.cholon.qlts.connectDB.ConnectionDB;
import hcm.ditagis.com.cholon.qlts.connectDB.LoginDB;
import hcm.ditagis.com.cholon.qlts.entities.entitiesDB.User;

public class LoginAsycn extends AsyncTask<String, Void, User> {
    private ProgressDialog mDialog;
    private Context mContext;

    private AsyncResponse mDelegate;

    public interface AsyncResponse {
        void processFinish(User output);
    }

    public LoginAsycn(Context context, AsyncResponse delegate) {
        this.mContext = context;
        this.mDelegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.mDialog = new ProgressDialog(this.mContext, android.R.style.Theme_Material_Dialog_Alert);
        this.mDialog.setMessage(mContext.getString(R.string.connect_message));
        this.mDialog.setCancelable(false);
        this.mDialog.show();
    }

    @Override
    protected User doInBackground(String... params) {
        String danhBo = params[0];
        String pin = params[1];
        try {
            ConnectionDB.getInstance().getConnection();
            publishProgress();
            LoginDB loginDB = new LoginDB(mContext);
            User user = loginDB.find(danhBo, pin);
            User.userDangNhap = user;
            return user;
        } catch (Exception e) {
            Log.e("Lỗi đăng nhập", e.toString());
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        this.mDialog.setMessage(mContext.getString(R.string.login_message));
    }

    @Override
    protected void onPostExecute(User user) {
//        if (user != null) {
        mDialog.dismiss();
        this.mDelegate.processFinish(user);
//        }
    }
}
