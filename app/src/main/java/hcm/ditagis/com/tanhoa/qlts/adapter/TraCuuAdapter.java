package hcm.ditagis.com.tanhoa.qlts.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import hcm.ditagis.com.tanhoa.qlts.R;

/**
 * Created by ThanLe on 04/10/2017.
 */

public class TraCuuAdapter extends ArrayAdapter<TraCuuAdapter.Item> {
    private Context context;
    private List<Item> items;

    public TraCuuAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_tracuu, null);
        }
        Item item = items.get(position);

        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layout_tracuu);
        switch (item.getTrangThai()) {
            //chưa sửa chữa
            case 0:
                layout.setBackgroundColor(ContextCompat.getColor(context, R.color.color_chua_sua_chua));
                break;
            //đã sửa chữa
            case 1:
                layout.setBackgroundColor(ContextCompat.getColor(context, R.color.color_da_sua_chua));
                break;
            //đang sửa chữa
            case 2:
                layout.setBackgroundColor(ContextCompat.getColor(context, R.color.color_dang_sua_chua));
                break;
        }

        TextView txtID = (TextView) convertView.findViewById(R.id.txt_tracuu_id);
        //todo
        txtID.setText(item.getId());

        TextView txtDiaChi = (TextView) convertView.findViewById(R.id.txt_tracuu_diachi);
        //todo
        txtDiaChi.setText(item.getDiaChi());

        TextView txtNgayCapNhat = (TextView) convertView.findViewById(R.id.txt_tracuu_ngaycapnhat);
        //todo
        txtNgayCapNhat.setText(item.getNgayCapNhat());


        return convertView;
    }


    public static class Item {


        int objectID;
        String id;
        int trangThai;
        String ngayCapNhat;
        String diaChi;
        double latitude;
        double longtitude;

        public Item(int objectID, String id, int trangThai, String ngayCapNhat, String diaChi) {
            this.objectID = objectID;
            this.id = id;
            this.trangThai = trangThai;
            this.ngayCapNhat = ngayCapNhat;
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

        public int getObjectID() {
            return objectID;
        }

        public void setObjectID(int objectID) {
            this.objectID = objectID;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getTrangThai() {
            return trangThai;
        }

        public void setTrangThai(int trangThai) {
            this.trangThai = trangThai;
        }

        public String getNgayCapNhat() {
            return ngayCapNhat;
        }

        public void setNgayCapNhat(String ngayCapNhat) {
            this.ngayCapNhat = ngayCapNhat;
        }

        public String getDiaChi() {
            return diaChi;
        }

        public void setDiaChi(String diaChi) {
            this.diaChi = diaChi;
        }

        @Override
        public String toString() {
            return "Item{" + "objectID=" + objectID + ", typeSearch='" + id + '\'' + ", trangThai=" + trangThai + ", ngayCapNhat='" + ngayCapNhat + '\'' + ", diaChi='" + diaChi + '\'' + '}';
        }
    }
}