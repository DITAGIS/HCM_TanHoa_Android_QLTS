package hcm.ditagis.com.tanhoa.qlsc.utities;

import android.content.Context;

import com.esri.arcgisruntime.data.ServiceFeatureTable;

import java.util.List;

import hcm.ditagis.com.tanhoa.qlsc.R;
import hcm.ditagis.com.tanhoa.qlsc.libs.FeatureLayerDTG;

public class MyServiceFeatureTable {
    private ServiceFeatureTable layerThuaDat;
    private ServiceFeatureTable layerDMA;
    private ServiceFeatureTable layerHanhChinh;

    private MyServiceFeatureTable(Context context, List<FeatureLayerDTG> mFeatureLayerDTGS) {
        for (FeatureLayerDTG feature : mFeatureLayerDTGS) {
            if (feature.getTitleLayer().equals(context.getString(R.string.ALIAS_THUA_DAT))) {
                layerThuaDat = (ServiceFeatureTable) feature.getFeatureLayer().getFeatureTable();
            } else if (feature.getTitleLayer().equals(context.getString(R.string.ALIAS_DMA))) {
                layerDMA = (ServiceFeatureTable) feature.getFeatureLayer().getFeatureTable();
            } else if (feature.getTitleLayer().equals(context.getString(R.string.ALIAS_HANH_CHINH))) {
                layerHanhChinh = (ServiceFeatureTable) feature.getFeatureLayer().getFeatureTable();
            }
        }
    }

    private static MyServiceFeatureTable instance = null;

    public static MyServiceFeatureTable getInstance(Context context, List<FeatureLayerDTG> mFeatureLayerDTGS) {
        if (instance == null) {
            instance = new MyServiceFeatureTable(context, mFeatureLayerDTGS);
        }
        return instance;
    }

    public ServiceFeatureTable getLayerDMA() {
        return layerDMA;
    }

    public ServiceFeatureTable getLayerThuaDat() {
        return layerThuaDat;
    }

    public ServiceFeatureTable getLayerHanhChinh() {
        return layerHanhChinh;
    }
}
