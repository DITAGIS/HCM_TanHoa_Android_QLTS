
package hcm.ditagis.com.cholon.qlts.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import hcm.ditagis.com.cholon.qlts.R;

public class ObjectsAdapter extends ArrayAdapter<ObjectsAdapter.Item> {
    private Context context;
    private List<Item> items;


    public ObjectsAdapter(Context context, List<ObjectsAdapter.Item> items) {
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
            convertView = inflater.inflate(R.layout.item_tracuu, null);
        }
        Item item = items.get(position);
        TextView txt_tracuu_id = (TextView) convertView.findViewById(R.id.txt_tracuu_id);
        TextView txt_tracuu_ngaycapnhat = (TextView) convertView.findViewById(R.id.txt_tracuu_ngaycapnhat);
        TextView txt_tracuu_diachi = (TextView) convertView.findViewById(R.id.txt_tracuu_diachi);
        txt_tracuu_id.setText(item.getIdSuCo());
        txt_tracuu_ngaycapnhat.setText(item.getNgayXayRa());
        txt_tracuu_diachi.setText(item.getDiaChi());
        return convertView;
    }

    public static class Item{
        private String objectID;
        private String idSuCo;
        private String ngayXayRa;
        private String diaChi;
        double latitude;
        double longtitude;

        public Item() {
        }

        public Item(String objectID, String idSuCo, String ngayXayRa) {
            this.objectID = objectID;
            this.idSuCo = idSuCo;
            this.ngayXayRa = ngayXayRa;
        }

        public String getObjectID() {
            return objectID;
        }

        public void setObjectID(String objectID) {
            this.objectID = objectID;
        }

        public String getIdSuCo() {
            return idSuCo;
        }

        public void setIdSuCo(String idSuCo) {
            this.idSuCo = idSuCo;
        }

        public String getNgayXayRa() {
            return ngayXayRa;
        }

        public void setNgayXayRa(String ngayXayRa) {
            this.ngayXayRa = ngayXayRa;
        }

        public String getDiaChi() {
            return diaChi;
        }

        public void setDiaChi(String diaChi) {
            this.diaChi = diaChi;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongtitude() {
            return longtitude;
        }

        public void setLongtitude(double longtitude) {
            this.longtitude = longtitude;
        }
    }

}
