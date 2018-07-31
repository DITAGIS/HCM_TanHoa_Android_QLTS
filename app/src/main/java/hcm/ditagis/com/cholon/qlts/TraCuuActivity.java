package hcm.ditagis.com.cholon.qlts;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.esri.arcgisruntime.data.ServiceFeatureTable;

import java.util.ArrayList;
import java.util.List;

import hcm.ditagis.com.cholon.qlts.adapter.FeatureViewMoreInfoAdapter;

public class TraCuuActivity extends AppCompatActivity {
    private ServiceFeatureTable mServiceFeatureTable;
    private DatePicker datePicker;
private List<String> mLstFeatureType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tra_cuu);

        mServiceFeatureTable = new ServiceFeatureTable(getResources().getString(R.string.service_feature_table));

        setContentView(R.layout.activity_tra_cuu);
        mLstFeatureType = new ArrayList<>();
        for (int i = 0; i < mServiceFeatureTable.getFeatureTypes().size(); i++) {
            mLstFeatureType.add(mServiceFeatureTable.getFeatureTypes().get(i).getName());
        }
        View layout = findViewById(R.id.layout_tracuu_include);
        ListView lstViewInfo = layout.findViewById(R.id.lstView_alertdialog_info);
         lstViewInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edit(parent, view, position, id);
            }
        });
    }    private void edit(final AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position) instanceof hcm.ditagis.com.cholon.qlts.adapter.FeatureViewMoreInfoAdapter.Item) {
            final FeatureViewMoreInfoAdapter.Item item = (FeatureViewMoreInfoAdapter.Item) parent.getItemAtPosition(position);
            if (item.isEdit()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this,
                        android.R.style.Theme_Material_Light_Dialog_Alert);
                builder.setTitle("Cập nhật thuộc tính");
                builder.setMessage(item.getAlias());
                builder.setCancelable(false).setNegativeButton("Hủy", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final android.widget.LinearLayout layout = (android.widget.LinearLayout) this.getLayoutInflater().
                        inflate(R.layout.layout_dialog_update_feature_listview, null);
                builder.setView(layout);
                final android.widget.FrameLayout layoutTextView = layout.findViewById(R.id.layout_edit_viewmoreinfo_TextView);
                final android.widget.TextView textView = layout.findViewById(R.id.txt_edit_viewmoreinfo);
//                final Button button = layout.findViewById(R.id.);
                final android.widget.LinearLayout layoutEditText = layout.findViewById(R.id.layout_edit_viewmoreinfo_Editext);
                final EditText editText = layout.findViewById(R.id.etxt_edit_viewmoreinfo);
                final android.widget.LinearLayout layoutSpin = layout.findViewById(R.id.layout_edit_viewmoreinfo_Spinner);
                final Spinner spin = layout.findViewById(R.id.spin_edit_viewmoreinfo);

                final com.esri.arcgisruntime.data.Domain domain = mServiceFeatureTable.getField(item.getFieldName()).getDomain();
                if (item.getFieldName().equals(mServiceFeatureTable.getTypeIdField())) {
                    layoutSpin.setVisibility(View.VISIBLE);
                    android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(layout.getContext(),
                            android.R.layout.simple_list_item_1, mLstFeatureType);
                    spin.setAdapter(adapter);
                    if (item.getValue() != null)
                        spin.setSelection(mLstFeatureType.indexOf(item.getValue()));
                } else if (domain != null) {
                    layoutSpin.setVisibility(View.VISIBLE);
                    List<com.esri.arcgisruntime.data.CodedValue> codedValues = ((com.esri.arcgisruntime.data.CodedValueDomain) domain).getCodedValues();
                    if (codedValues != null) {
                        List<String> codes = new ArrayList<>();
                        for (com.esri.arcgisruntime.data.CodedValue codedValue : codedValues)
                            codes.add(codedValue.getName());
                        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(layout.getContext(), android.R.layout.simple_list_item_1, codes);
                        spin.setAdapter(adapter);
                        if (item.getValue() != null)
                            spin.setSelection(codes.indexOf(item.getValue()));

                    }
                } else switch (item.getFieldType()) {
                    case DATE:
                        layoutTextView.setVisibility(View.VISIBLE);
                        textView.setText(item.getValue());
//                        button.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                final View dialogView = View.inflate(TraCuuActivity.this, R.layout.date_time_picker, null);
//                                final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(TraCuuActivity.this).create();
//                                dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        DatePicker datePicker =  dialogView.findViewById(R.id.date_picker);
//
//                                        String s = String.format("%02d_%02d_%d",
//                                                datePicker.getDayOfMonth(), datePicker.getMonth(), datePicker.getYear());
//
//                                        textView.setText(s);
//                                        alertDialog.dismiss();
//                                    }
//                                });
//                                alertDialog.setView(dialogView);
//                                alertDialog.show();
//                            }
//                        });
                        break;
                    case TEXT:
                        layoutEditText.setVisibility(View.VISIBLE);
                        editText.setText(item.getValue());
                        break;
                    case SHORT:
                        layoutEditText.setVisibility(View.VISIBLE);
                        editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
                        editText.setText(item.getValue());


                        break;
                    case DOUBLE:
                        layoutEditText.setVisibility(View.VISIBLE);
                        editText.setInputType(android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        editText.setText(item.getValue());
                        break;
                }
                builder.setPositiveButton("Cập nhật", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                                   if (item.getFieldName().equals(mServiceFeatureTable.getTypeIdField()) || (domain != null)) {
                            item.setValue(spin.getSelectedItem().toString());
                       } else {
                            switch (item.getFieldType()) {
                                case DATE:
                                    item.setValue(textView.getText().toString());
                                    break;
                                case DOUBLE:
                                    try {
                                        double x = Double.parseDouble(editText.getText().toString());
                                        item.setValue(editText.getText().toString());
                                    } catch (Exception e) {
                                        android.widget.Toast.makeText(TraCuuActivity.this, "Số liệu nhập vào không đúng định dạng!!!", android.widget.Toast.LENGTH_LONG).show();
                                    }
                                    break;
                                case TEXT:
                                    item.setValue(editText.getText().toString());
                                    break;
                                case SHORT:
                                    try {
                                        short x = Short.parseShort(editText.getText().toString());
                                        item.setValue(editText.getText().toString());
                                    } catch (Exception e) {
                                        android.widget.Toast.makeText(TraCuuActivity.this, "Số liệu nhập vào không đúng định dạng!!!", android.widget.Toast.LENGTH_LONG).show();
                                    }
                                    break;
                            }
                        }


                        dialog.dismiss();
                        FeatureViewMoreInfoAdapter adapter = (FeatureViewMoreInfoAdapter) parent.getAdapter();
                        new hcm.ditagis.com.cholon.qlts.async.NotifyDataSetChangeAsync(TraCuuActivity.this).execute(adapter);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
                dialog.show();

            }
        }

    }

}