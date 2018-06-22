package hcm.ditagis.com.tanhoa.qlsc.libs;


import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.view.MapView;

/**
 * Created by NGUYEN HONG on 3/14/2018.
 */

public class FeatureLayerDTG {


    private FeatureLayer featureLayer;


    private MapView mMapView;


    private String[] outFields;
    private String[] queryFields;
    private String[] updateFields;
    private String titleLayer;

    public String[] getUpdateFields() {
        return updateFields;
    }

    public void setUpdateFields(String[] updateFields) {
        this.updateFields = updateFields;
    }

    public FeatureLayerDTG(MapView mMapView, FeatureLayer featureLayer, String[] outFields) {
        this.mMapView = mMapView;
        this.featureLayer = featureLayer;
        this.outFields = outFields;
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

    public String getTitleLayer() {
        return titleLayer;
    }

    public void setTitleLayer(String titleLayer) {
        this.titleLayer = titleLayer;
    }
}
