package hcm.ditagis.com.cholon.qlts.tools;

import java.util.ArrayList;
import java.util.List;

import hcm.ditagis.com.cholon.qlts.QuanLyTaiSan;
import hcm.ditagis.com.cholon.qlts.R;
import hcm.ditagis.com.cholon.qlts.adapter.SearchAdapter;
import hcm.ditagis.com.cholon.qlts.libs.FeatureLayerDTG;

/**
 * Created by NGUYEN HONG on 4/26/2018.
 */

public class SearchItem {
    private List<SearchAdapter.Item> items;
    private List<FeatureLayerDTG> mFeatureLayerDTGS;

    public SearchItem(List<FeatureLayerDTG> mFeatureLayerDTGS, QuanLyTaiSan quanLyTaiSan) {
        this.mFeatureLayerDTGS = mFeatureLayerDTGS;
        items = new ArrayList<>();
        for (FeatureLayerDTG featureLayerDTG : mFeatureLayerDTGS) {
            if (featureLayerDTG.getAction() != null && featureLayerDTG.getAction().getSearch())
                items.add(new SearchAdapter.Item(quanLyTaiSan.getString(R.string.type_search_feature_layer), featureLayerDTG.getFeatureLayer().getName(),featureLayerDTG.getFeatureLayer().getId()));
        }

    }

    public List<SearchAdapter.Item> getItems() {
        return items;
    }

    public void setItems(List<SearchAdapter.Item> items) {
        this.items = items;
    }
}
