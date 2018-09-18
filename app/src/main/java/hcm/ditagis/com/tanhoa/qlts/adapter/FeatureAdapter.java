
package hcm.ditagis.com.tanhoa.qlts.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import hcm.ditagis.com.tanhoa.qlts.R;

public class FeatureAdapter extends ArrayAdapter<FeatureAdapter.Item> {
    private Context context;
    private List<Item> items;


    public FeatureAdapter(Context context, List<FeatureAdapter.Item> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void clear() {
        items.clear();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_data_logger, null);
        }
        Item item = items.get(position);
        TextView txt_LayerID = (TextView) convertView.findViewById(R.id.txt_LayerID);
        TextView txt_ObjectID = (TextView) convertView.findViewById(R.id.txt_ObjectID);
        TextView txt_TrangThai = (TextView) convertView.findViewById(R.id.txt_TrangThai);
        TextView txt_Ngay = (TextView) convertView.findViewById(R.id.txt_Ngay);
        txt_LayerID.setText(item.getLayerID());
        txt_ObjectID.setText(item.getID());
        txt_TrangThai.setText(item.getTrangThai());
        txt_Ngay.setText(item.getNgay());
        return convertView;
    }

    public static class Item{
        private String layerID;
        private String iD;
        private String trangThai;
        private short value_TrangThai = -1;
        private String ngay;

        public Item() {
        }

        public Item(String layerID, String iD, String trangThai, String ngay) {
            this.layerID = layerID;
            this.iD = iD;
            this.trangThai = trangThai;
            this.ngay = ngay;
        }

        public String getLayerID() {
            return layerID;
        }

        public void setLayerID(String layerID) {
            this.layerID = layerID;
        }

        public String getID() {
            return iD;
        }

        public void setID(String iD) {
            this.iD = iD;
        }

        public String getTrangThai() {
            return trangThai;
        }

        public void setTrangThai(String trangThai) {
            this.trangThai = trangThai;
        }

        public short getValue_TrangThai() {
            return value_TrangThai;
        }

        public void setValue_TrangThai(short value_TrangThai) {
            this.value_TrangThai = value_TrangThai;
        }

        public String getNgay() {
            return ngay;
        }

        public void setNgay(String ngay) {
            this.ngay = ngay;
        }
    }

}
