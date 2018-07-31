package hcm.ditagis.com.cholon.qlts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import hcm.ditagis.com.cholon.qlts.R;

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
            convertView = inflater.inflate(R.layout.item_viewinfo, null);
        }
        Item item = items.get(position);
        TextView txt_viewinfo_alias = (TextView) convertView.findViewById(R.id.txt_viewinfo_alias);
        TextView txt_viewinfo_value = (TextView) convertView.findViewById(R.id.txt_viewinfo_value);
        txt_viewinfo_alias.setText(item.getTitleLayer());
        txt_viewinfo_value.setText(item.getSumFeatures().toString());
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
    }
}
