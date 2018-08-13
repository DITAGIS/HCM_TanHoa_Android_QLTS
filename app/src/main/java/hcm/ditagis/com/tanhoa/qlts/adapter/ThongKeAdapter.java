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
public class ThongKeAdapter extends ArrayAdapter<ThongKeAdapter.Item> {
    private Context context;
    private List<Item> items;

    public ThongKeAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.item_viewinfo, null);
            convertView = inflater.inflate(R.layout.item_viewmoreinfo, null);
        }

        Item item = items.get(position);
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layout_viewmoreinfo);
        TextView txtAlias = (TextView) convertView.findViewById(R.id.txt_viewmoreinfo_alias);
        TextView txtValue = (TextView) convertView.findViewById(R.id.txt_viewmoreinfo_value);
        txtAlias.setText(item.getTitleLayer());
        txtValue.setText(item.getSumFeatures().toString());
        if (item.isView()) {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent_1));
            convertView.findViewById(R.id.img_viewmoreinfo_edit).setVisibility(View.VISIBLE);
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBackground_1));
            convertView.findViewById(R.id.img_viewmoreinfo_edit).setVisibility(View.INVISIBLE);

        }
        return convertView;
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


    public static class Item {
        private String titleLayer;
        private Long sumFeatures;
        private boolean isView = false;

        public Item(String titleLayer, Long sumFeatures) {
            this.titleLayer = titleLayer;
            this.sumFeatures = sumFeatures;
        }

        public String getTitleLayer() {
            return titleLayer;
        }

        public void setTitleLayer(String titleLayer) {
            this.titleLayer = titleLayer;
        }

        public Long getSumFeatures() {
            return sumFeatures;
        }

        public void setSumFeatures(Long sumFeatures) {
            this.sumFeatures = sumFeatures;
        }

        public boolean isView() {
            return isView;
        }

        public void setView(boolean view) {
            isView = view;
        }
    }
}
