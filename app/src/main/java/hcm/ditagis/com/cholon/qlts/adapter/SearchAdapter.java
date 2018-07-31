package hcm.ditagis.com.cholon.qlts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hcm.ditagis.com.cholon.qlts.R;

/**
 * Created by ThanLe on 04/10/2017.
 */
public class SearchAdapter extends ArrayAdapter<SearchAdapter.Item> {
    private Context context;
    private List<Item> items;

    public SearchAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_search_type, null);
        }
        Item item = items.get(position);
        TextView txt_title_layer = (TextView) convertView.findViewById(R.id.txt_title_layer);
        //todo
        txt_title_layer.setText(item.getTitleLayer());
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
        String typeSearch;
        String titleLayer;
        String idLayer;

        public Item() {
        }

        public Item(String typeSearch, String titleLayer,String idLayer) {
            this.typeSearch = typeSearch;
            this.titleLayer = titleLayer;
            this.idLayer = idLayer;
        }

        public String getTypeSearch() {
            return typeSearch;
        }

        public void setTypeSearch(String typeSearch) {
            this.typeSearch = typeSearch;
        }

        public String getTitleLayer() {
            return titleLayer;
        }

        public void setTitleLayer(String titleLayer) {
            this.titleLayer = titleLayer;
        }

        public String getIdLayer() {
            return idLayer;
        }

        public void setIdLayer(String idLayer) {
            this.idLayer = idLayer;
        }
    }
}
