package hcm.ditagis.com.cholon.qlts.libs;

/**
 * Created by NGUYEN HONG on 6/15/2018.
 */

public class Action{
    private Boolean isUpdate, isSearch, isStatistics = false;

    public Action(Boolean isUpdate, Boolean isSearch, Boolean isStatistics) {
        this.isUpdate = isUpdate;
        this.isSearch = isSearch;
        this.isStatistics = isStatistics;
    }

    public Action() {
    }

    public Boolean getUpdate() {
        return isUpdate;
    }

    public void setUpdate(Boolean update) {
        isUpdate = update;
    }

    public Boolean getSearch() {
        return isSearch;
    }

    public void setSearch(Boolean search) {
        isSearch = search;
    }

    public Boolean getStatistics() {
        return isStatistics;
    }

    public void setStatistics(Boolean statistics) {
        isStatistics = statistics;
    }
}