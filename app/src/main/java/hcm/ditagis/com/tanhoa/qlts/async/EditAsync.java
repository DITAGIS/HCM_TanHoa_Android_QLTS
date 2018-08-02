package hcm.ditagis.com.tanhoa.qlts.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.Attachment;
import com.esri.arcgisruntime.data.CodedValue;
import com.esri.arcgisruntime.data.CodedValueDomain;
import com.esri.arcgisruntime.data.Domain;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureEditResult;
import com.esri.arcgisruntime.data.FeatureType;
import com.esri.arcgisruntime.data.ServiceFeatureTable;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import hcm.ditagis.com.tanhoa.qlts.R;
import hcm.ditagis.com.tanhoa.qlts.adapter.FeatureViewMoreInfoAdapter;
import hcm.ditagis.com.tanhoa.qlts.utities.Constant;

/**
 * Created by ThanLe on 4/16/2018.
 */

public class EditAsync extends AsyncTask<FeatureViewMoreInfoAdapter, Void, Void> {
    public interface AsyncResponse {
        void processFinish();
    }
    public AsyncResponse delegate = null;
    private ProgressDialog mDialog;
    private Context mContext;
    private ServiceFeatureTable mServiceFeatureTable;
    private ArcGISFeature mSelectedArcGISFeature = null;
    private boolean isUpdateAttachment;
    private byte[] mImage;

    public EditAsync(Context context, ServiceFeatureTable serviceFeatureTable, ArcGISFeature selectedArcGISFeature,AsyncResponse delegate) {
        mContext = context;
        mServiceFeatureTable = serviceFeatureTable;
        mSelectedArcGISFeature = selectedArcGISFeature;
        mDialog = new ProgressDialog(context, android.R.style.Theme_Material_Dialog_Alert);
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog.setMessage(mContext.getString(R.string.async_dang_xu_ly));
        mDialog.setCancelable(false);
        mDialog.setButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                publishProgress();
            }
        });
        mDialog.show();

    }

    @Override
    protected Void doInBackground(FeatureViewMoreInfoAdapter... params) {
        FeatureViewMoreInfoAdapter adapter = params[0];
        List<FeatureType> featureTypes = mSelectedArcGISFeature.getFeatureTable().getFeatureTypes();
        for (FeatureViewMoreInfoAdapter.Item item : adapter.getItems()) {
            if (item.getValue() == null || !item.isEdit()) continue;
            Domain domain = mSelectedArcGISFeature.getFeatureTable().getField(item.getFieldName()).getDomain();
            Object codeDomain = null;
            if (domain != null) {
                List<CodedValue> codedValues = ((CodedValueDomain) this.mSelectedArcGISFeature.getFeatureTable().getField(item.getFieldName()).getDomain()).getCodedValues();
                codeDomain = getCodeDomain(codedValues, item.getValue());
            }
            if (featureTypes.size() > 0 && item.getFieldName().equals(mSelectedArcGISFeature.getFeatureTable().getTypeIdField())) {
                Object idFeatureTypes = getIdFeatureTypes(featureTypes, item.getValue());
                mSelectedArcGISFeature.getAttributes().put(item.getFieldName(), Short.parseShort(idFeatureTypes.toString()));

            } else switch (item.getFieldType()) {
                case DATE:
                    Date date = null;
                    try {
                        date = Constant.DATE_FORMAT.parse(item.getValue());
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        mSelectedArcGISFeature.getAttributes().put(item.getFieldName(), c);
                    } catch (ParseException e) {
                    }
                    break;

                case TEXT:
                    if (codeDomain != null) {
                        mSelectedArcGISFeature.getAttributes().put(item.getFieldName(), codeDomain.toString());
                    } else
                        mSelectedArcGISFeature.getAttributes().put(item.getFieldName(), item.getValue());
                    break;
                case INTEGER:
                    if (codeDomain != null) {
                        mSelectedArcGISFeature.getAttributes().put(item.getFieldName(), Integer.parseInt(codeDomain.toString()));
                    } else
                        mSelectedArcGISFeature.getAttributes().put(item.getFieldName(), Integer.parseInt(item.getValue()));
                    break;
                case SHORT:
                    if (codeDomain != null) {
                        mSelectedArcGISFeature.getAttributes().put(item.getFieldName(), Short.parseShort(codeDomain.toString()));
                    } else
                        mSelectedArcGISFeature.getAttributes().put(item.getFieldName(), Short.parseShort(item.getValue()));
                    break;
            }
        }
        mServiceFeatureTable.loadAsync();
        mServiceFeatureTable.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                try {
                    updateFeature(mSelectedArcGISFeature);


                } catch (Exception e) {
                }
            }
        });
        return null;
    }
    private void updateFeature(final Feature table_maudanhgiaFeature) {
        final ListenableFuture<Void> mapViewResult = mServiceFeatureTable.updateFeatureAsync(table_maudanhgiaFeature);
        mapViewResult.addDoneListener(new Runnable() {
            @Override
            public void run() {
                final ListenableFuture<List<FeatureEditResult>> listListenableEditAsync = mServiceFeatureTable.applyEditsAsync();
                listListenableEditAsync.addDoneListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mDialog.dismiss();
                            List<FeatureEditResult> featureEditResults = listListenableEditAsync.get();
                            publishProgress();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }
    private void addAttachment() {

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
//                Envelope extent = item.getGeometry().getExtent();
//                mMapView.setViewpointGeometryAsync(extent);
    }
    private Object getIdFeatureTypes(List<FeatureType> featureTypes, String value) {
        Object code = null;
        for (FeatureType featureType : featureTypes) {
            if (featureType.getName().equals(value)) {
                code = featureType.getId();
                break;
            }
        }
        return code;
    }

    private Object getCodeDomain(List<CodedValue> codedValues, String value) {
        Object code = null;
        for (CodedValue codedValue : codedValues) {
            if (codedValue.getName().equals(value)) {
                code = codedValue.getCode();
                break;
            }

        }
        return code;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        delegate.processFinish();

    }


    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

    }

}

