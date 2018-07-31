package hcm.ditagis.com.cholon.qlts.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import hcm.ditagis.com.cholon.qlts.R;
import hcm.ditagis.com.cholon.qlts.adapter.TraCuuAdapter;

/**
 * Created by ThanLe on 4/16/2018.
 */

public class NotifyDataSetChangeTraCuuAsync extends AsyncTask<TraCuuAdapter, Void, Void> {
    private ProgressDialog dialog;
    private Context mContext;
    private Activity mActivity;

    public NotifyDataSetChangeTraCuuAsync(Activity activity) {
        mActivity =activity;
        dialog = new ProgressDialog(mActivity, android.R.style.Theme_Material_Dialog_Alert);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage(mActivity.getString(R.string.async_dang_cap_nhat_giao_dien));
        dialog.setButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                publishProgress(null);
            }
        });
        dialog.setCancelable(false);

        dialog.show();

    }

    @Override
    protected Void doInBackground(TraCuuAdapter... params) {
        final TraCuuAdapter adapter = params[0];
        try {
            Thread.sleep(500);
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();

                }
            });
        } catch (InterruptedException e) {

        }


        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }


    @Override
    protected void onPostExecute(Void result) {
        if (dialog != null || dialog.isShowing())
            dialog.dismiss();
        super.onPostExecute(result);

    }

}
