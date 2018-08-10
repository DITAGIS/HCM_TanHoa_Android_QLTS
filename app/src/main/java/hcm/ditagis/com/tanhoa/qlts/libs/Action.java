package hcm.ditagis.com.tanhoa.qlts.libs;

/**
 * Created by NGUYEN HONG on 6/15/2018.
 */

public class Action{
    private boolean isCreate, isDelete, isEdit, isView;


    public Action(boolean isView,boolean isCreate,boolean isEdit, boolean isDelete ) {
        this.isCreate = isCreate;
        this.isDelete = isDelete;
        this.isEdit = isEdit;
        this.isView = isView;
    }


    public boolean isCreate() {
        return isCreate;
    }

    public void setCreate(boolean create) {
        isCreate = create;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public boolean isView() {
        return isView;
    }

    public void setView(boolean view) {
        isView = view;
    }


}