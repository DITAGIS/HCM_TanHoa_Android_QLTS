package hcm.ditagis.com.tanhoa.qlsc.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import hcm.ditagis.com.tanhoa.qlsc.R;
import hcm.ditagis.com.tanhoa.qlsc.connectDB.ChangePasswordDB;
import hcm.ditagis.com.tanhoa.qlsc.entities.entitiesDB.KhachHang;
import hcm.ditagis.com.tanhoa.qlsc.utities.Utils;

public class ChangePasswordAsycn extends AsyncTask<String, Void, Integer> {
    private ProgressDialog mDialog;
    private Context mContext;
    private final int SAI_MAT_KHAU_CU = 1;
    private AsyncResponse mDelegate;

    public interface AsyncResponse {
        void processFinish(Integer output);
    }

    public ChangePasswordAsycn(Context context, AsyncResponse delegate) {
        this.mContext = context;
        this.mDelegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.mDialog = new ProgressDialog(this.mContext, android.R.style.Theme_Material_Dialog_Alert);
        this.mDialog.setMessage(mContext.getString(R.string.change_password_check_old_password_message));
        this.mDialog.setCancelable(false);
        this.mDialog.show();
    }

    @Override
    protected Integer doInBackground(String... params) {
        String danhBo = params[0];
        String pin = params[1];
        String newPin = params[2];
        try {
            ChangePasswordDB changePasswordDB = new ChangePasswordDB(mContext);
            KhachHang khachHang = changePasswordDB.find(danhBo, pin);
            if (khachHang != null) {
                publishProgress();
                KhachHang khachHang1 = changePasswordDB.change(danhBo, newPin);
                if (khachHang1 != null) {
                    return Utils.getInstance().CHANGE_PASSWORD_SUCCESS;
                } else return Utils.getInstance().CHANGE_PASSWORD_FAILURE;
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e("Lỗi đổi mật khẩu", e.toString());
        }
        return Utils.getInstance().CHANGE_PASSWORD_FAILURE;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        this.mDialog.setMessage(mContext.getString(R.string.change_password_message));


    }

    @Override
    protected void onPostExecute(Integer value) {
//        if (khachHang != null) {
        mDialog.dismiss();
        this.mDelegate.processFinish(value);
//        }
    }
}
