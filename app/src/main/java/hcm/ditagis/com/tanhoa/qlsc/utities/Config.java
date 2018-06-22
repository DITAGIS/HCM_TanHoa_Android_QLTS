package hcm.ditagis.com.tanhoa.qlsc.utities;

import android.content.Context;

/**
 * Created by NGUYEN HONG on 3/20/2018.
 */

public class Config {
    private String url;
    private String[] queryField;
    private String[] outField;
    private String[] updateField;
    private String alias;
    private String name;
    private int minScale;
    private Context mContext;
    private static Config instance = null;

    public static Config getInstance() {
        if (instance == null)
            instance = new Config();
        return instance;
    }

    private Config() {
    }

    public String[] getUpdateField() {
        return updateField;
    }

    public void setUpdateField(String[] updateField) {
        this.updateField = updateField;
    }

    public Config(String url, String[] outField, String alias) {
        this.url = url;
        this.outField = outField;
        this.alias = alias;
    }

    public Config(String url, String[] queryField, String[] outField, String alias) {
        this.url = url;
        this.queryField = queryField;
        this.outField = outField;
        this.alias = alias;
    }


    public Config(String url, String[] queryField, String[] outField, String alias, int minScale, String[] updateField) {
        this.url = url;
        this.queryField = queryField;
        this.outField = outField;
        this.updateField = updateField;
        this.alias = alias;
        this.minScale = minScale;
    }

    public Config(String url, String[] queryField, String[] outField, String name, String alias, int minScale, String[] updateField) {
        this.url = url;
        this.queryField = queryField;
        this.outField = outField;
        this.updateField = updateField;
        this.alias = alias;
        this.minScale = minScale;
        this.name = name;
    }

    public int getMinScale() {
        return minScale;
    }

    public void setMinScale(int minScale) {
        this.minScale = minScale;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String[] getQueryField() {
        return queryField;
    }

    public void setQueryField(String[] queryField) {
        this.queryField = queryField;
    }

    public String[] getOutField() {
        return outField;
    }

    public void setOutField(String[] outField) {
        this.outField = outField;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }





}
