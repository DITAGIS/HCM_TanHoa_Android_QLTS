package hcm.ditagis.com.cholon.qlts.libs;


import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.List;

/**
 * Created by NGUYEN HONG on 3/14/2018.
 */

public class FeatureLayerDTG {


    private FeatureLayer featureLayer;


    private MapView mMapView;


    private String[] outFields;
    private String[] queryFields;
    private String[] updateFields;
    private String groupLayer;
    private Action action;

    public String[] getUpdateFields() {
        return updateFields;
    }

    public void setUpdateFields(String[] updateFields) {
        this.updateFields = updateFields;
    }


    public FeatureLayerDTG(FeatureLayer featureLayer) {
        this.featureLayer = featureLayer;
    }

    public FeatureLayer getFeatureLayer() {
        return featureLayer;
    }


    public String[] getOutFields() {
        return outFields;
    }

    public void setOutFields(String[] outFields) {
        this.outFields = outFields;
    }

    public String[] getQueryFields() {
        return queryFields;
    }

    public void setQueryFields(String[] queryFields) {
        this.queryFields = queryFields;
    }

    public String getGroupLayer() {
        return groupLayer;
    }

    public void setGroupLayer(String groupLayer) {
        this.groupLayer = groupLayer;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }


}
