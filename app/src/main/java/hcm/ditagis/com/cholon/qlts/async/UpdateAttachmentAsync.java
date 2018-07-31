package hcm.ditagis.com.cholon.qlts.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.Attachment;
import com.esri.arcgisruntime.data.FeatureEditResult;
import com.esri.arcgisruntime.data.ServiceFeatureTable;

import java.util.List;
import java.util.concurrent.ExecutionException;

import hcm.ditagis.com.cholon.qlts.R;

/**
 * Created by ThanLe on 4/16/2018.
 */

public class UpdateAttachmentAsync extends AsyncTask<Void, Void, Void> {
    private ProgressDialog mDialog;
    private Context mContext;
    private ServiceFeatureTable mServiceFeatureTable;
    private ArcGISFeature mSelectedArcGISFeature = null;
    private byte[] mImage;

    public UpdateAttachmentAsync(Context context, ArcGISFeature selectedArcGISFeature, byte[] image) {
        mContext = context;
        mServiceFeatureTable = (ServiceFeatureTable) selectedArcGISFeature.getFeatureTable();
        mSelectedArcGISFeature = selectedArcGISFeature;
        mDialog = new ProgressDialog(context, android.R.style.Theme_Material_Dialog_Alert);
        this.mImage = image;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog.setMessage(mContext.getString(R.string.async_dang_xu_ly));
        mDialog.setCancelable(false);

        mDialog.show();

    }

    @Override
    protected Void doInBackground(Void... params) {
        final String attachmentName = mContext.getString(R.string.attachment) + "_" + System.currentTimeMillis() + ".png";
        final ListenableFuture<Attachment> addResult = mSelectedArcGISFeature.addAttachmentAsync(mImage, Bitmap.CompressFormat.PNG.toString(), attachmentName);
        addResult.addDoneListener(new Runnable() {
            @Override
            public void run() {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                try {
                    Attachment attachment = addResult.get();
                    if (attachment.getSize() > 0) {
                        final ListenableFuture<Void> tableResult = mServiceFeatureTable.updateFeatureAsync(mSelectedArcGISFeature);
                        tableResult.addDoneListener(new Runnable() {
                            @Override
                            public void run() {
                                final ListenableFuture<List<FeatureEditResult>> updatedServerResult = mServiceFeatureTable.applyEditsAsync();
                                updatedServerResult.addDoneListener(new Runnable() {
                                    @Override
                                    public void run() {
                                        List<FeatureEditResult> edits = null;
                                        try {
                                            edits = updatedServerResult.get();
                                            if (edits.size() > 0) {
                                                if (!edits.get(0).hasCompletedWithErrors()) {
                                                    //attachmentList.add(fileName);
                                                    String s = mSelectedArcGISFeature.getAttributes().get("objectid").toString();
                                                    // update the attachment list view/ on the control panel
                                                } else {
                                                }
                                            } else {
                                            }
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        }
                                        if (mDialog != null && mDialog.isShowing()) {
                                            mDialog.dismiss();
                                        }

                                    }
                                });


                            }
                        });
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }


    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

    }

}

