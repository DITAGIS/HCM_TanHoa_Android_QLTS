package hcm.ditagis.com.tanhoa.qlsc.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import hcm.ditagis.com.tanhoa.qlsc.R;
import hcm.ditagis.com.tanhoa.qlsc.connectDB.ConnectionDB;
import hcm.ditagis.com.tanhoa.qlsc.connectDB.LoginDB;
import hcm.ditagis.com.tanhoa.qlsc.entities.entitiesDB.KhachHang;

public class LoginAsycn extends AsyncTask<String, Void, KhachHang> {
    private ProgressDialog mDialog;
    private Context mContext;

    private AsyncResponse mDelegate;

    public interface AsyncResponse {
        void processFinish(KhachHang output);
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
    protected KhachHang doInBackground(String... params) {
        String danhBo = params[0];
        String pin = params[1];
        try {
            ConnectionDB.getInstance().getConnection();
            publishProgress();
            LoginDB loginDB = new LoginDB(mContext);
            KhachHang khachHang = loginDB.find(danhBo, pin);
            KhachHang.khachHangDangNhap = khachHang;
            return khachHang;
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
    protected void onPostExecute(KhachHang khachHang) {
//        if (khachHang != null) {
        mDialog.dismiss();
        this.mDelegate.processFinish(khachHang);
//        }
    }
}
