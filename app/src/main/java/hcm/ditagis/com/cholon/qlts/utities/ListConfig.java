package hcm.ditagis.com.cholon.qlts.utities;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import hcm.ditagis.com.cholon.qlts.R;

/**
 * Created by ThanLe on 5/8/2018.
 */

public class ListConfig {
    private static Context mContext;
    private static ListConfig instance = null;

    public static ListConfig getInstance(Context context) {
        if (instance == null)
            instance = new ListConfig();
        mContext = context;
        return instance;
    }

    private ListConfig() {
    }

    public List<Config> getConfigs() {
        List<Config> configs = new ArrayList<>();
        //        CONFIG BASEMAP
        configs.add(new Config(mContext.getResources().getString(R.string.URL_TIM_DUONG),mContext.getResources().getString(R.string.ID_TIM_DUONG), mContext.getResources().getString(R.string.TITLE_TIM_DUONG), mContext.getResources().getInteger(R.integer.MIN_SCALE_TIM_DUONG), mContext.getString(R.string.group_base_map)));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_TIM_SONG),mContext.getResources().getString(R.string.ID_TIM_SONG), mContext.getResources().getString(R.string.TITLE_TIM_SONG), mContext.getResources().getInteger(R.integer.MIN_SCALE_TIM_SONG), mContext.getString(R.string.group_base_map)));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_GIAO_THONG),mContext.getResources().getString(R.string.ID_GIAO_THONG), mContext.getResources().getString(R.string.TITLE_GIAO_THONG), mContext.getResources().getInteger(R.integer.MIN_SCALE_GIAO_THONG), mContext.getString(R.string.group_base_map)));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_SONG_HO),mContext.getResources().getString(R.string.ID_SONG_HO), mContext.getResources().getString(R.string.TITLE_SONG_HO), mContext.getResources().getInteger(R.integer.MIN_SCALE_SONG_HO), mContext.getString(R.string.group_base_map)));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_THUA_DAT),mContext.getResources().getString(R.string.ID_THUA_DAT), mContext.getResources().getString(R.string.TITLE_THUA_DAT), mContext.getResources().getInteger(R.integer.MIN_SCALE_THUA_DAT), mContext.getString(R.string.group_base_map)));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_HANH_CHINH),mContext.getResources().getString(R.string.ID_HANH_CHINH), mContext.getResources().getString(R.string.TITLE_HANH_CHINH), mContext.getResources().getInteger(R.integer.MIN_SCALE_HANH_CHINH), mContext.getString(R.string.group_base_map)));


        configs.add(new Config(mContext.getResources().getString(R.string.URL_DMA),mContext.getResources().getString(R.string.ID_DMA), mContext.getResources().getString(R.string.TITLE_DMA), mContext.getResources().getInteger(R.integer.minScale_dma), mContext.getString(R.string.group_tai_san), mContext.getResources().getStringArray(R.array.update_fields_dma)));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_ONG_NGANH),mContext.getResources().getString(R.string.ID_ONG_NGANH), mContext.getResources().getString(R.string.TITLE_ONG_NGANH), mContext.getResources().getInteger(R.integer.minScale_ongnganh), mContext.getString(R.string.group_tai_san), mContext.getResources().getStringArray(R.array.update_fields_ongnganh)));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_ONG_PHAN_PHOI),mContext.getResources().getString(R.string.ID_ONG_PHAN_PHOI), mContext.getResources().getString(R.string.TITLE_ONG_PHAN_PHOI), mContext.getResources().getInteger(R.integer.minScale_ongphanphoi), mContext.getString(R.string.group_tai_san), mContext.getResources().getStringArray(R.array.update_fields_ongphanphoi)));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_ONG_TRUYEN_DAN),mContext.getResources().getString(R.string.ID_ONG_TRUYEN_DAN), mContext.getResources().getString(R.string.TITLE_ONG_TRUYEN_DAN), mContext.getResources().getInteger(R.integer.minScale_ongtruyendan), mContext.getString(R.string.group_tai_san), mContext.getResources().getStringArray(R.array.update_fields_ongtruyendan)));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_THUY_DAI),mContext.getResources().getString(R.string.ID_THUY_DAI), mContext.getResources().getString(R.string.TITLE_THUY_DAI), mContext.getResources().getInteger(R.integer.minScale_thuydai), mContext.getString(R.string.group_tai_san), mContext.getResources().getStringArray(R.array.update_fields_thuydai)));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_THAP_CAT_AP),mContext.getResources().getString(R.string.ID_THAP_CAT_AP), mContext.getResources().getString(R.string.TITLE_THAP_CAT_AP), mContext.getResources().getInteger(R.integer.minScale_thapcatap), mContext.getString(R.string.group_tai_san), mContext.getResources().getStringArray(R.array.update_fields_thapcatap)));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_BE_CHUA),mContext.getResources().getString(R.string.ID_BE_CHUA), mContext.getResources().getString(R.string.TITLE_BE_CHUA), mContext.getResources().getInteger(R.integer.minScale_bechua), mContext.getString(R.string.group_tai_san), mContext.getResources().getStringArray(R.array.update_fields_bechua)));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_DONG_HO_TONG),mContext.getResources().getString(R.string.ID_DONG_HO_TONG), mContext.getResources().getString(R.string.TITLE_DONG_HO_TONG), mContext.getResources().getInteger(R.integer.minScale_donghotong), mContext.getString(R.string.group_tai_san), mContext.getResources().getStringArray(R.array.update_fields_donghotong)));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_DONG_HO_KHACH_HANG),mContext.getResources().getString(R.string.ID_DONG_HO_KHACH_HANG), mContext.getResources().getString(R.string.TITLE_DONG_HO_KHACH_HANG), mContext.getResources().getInteger(R.integer.minScale_donghokhachhang), mContext.getString(R.string.group_tai_san), mContext.getResources().getStringArray(R.array.update_fields_donghokhachhang)));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_MOI_NOI),mContext.getResources().getString(R.string.ID_MOI_NOI), mContext.getResources().getString(R.string.TITLE_MOI_NOI), mContext.getResources().getInteger(R.integer.minScale_moinoi), mContext.getString(R.string.group_tai_san), mContext.getResources().getStringArray(R.array.update_fields_moinoi)));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_TRU_HONG),mContext.getResources().getString(R.string.ID_TRU_HONG), mContext.getResources().getString(R.string.TITLE_TRU_HONG), mContext.getResources().getInteger(R.integer.minScale_truhong), mContext.getString(R.string.group_tai_san), mContext.getResources().getStringArray(R.array.update_fields_truhong)));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_VAN),mContext.getResources().getString(R.string.ID_VAN), mContext.getResources().getString(R.string.TITLE_VAN), mContext.getResources().getInteger(R.integer.minScale_van), mContext.getString(R.string.group_tai_san), mContext.getResources().getStringArray(R.array.update_fields_van)));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_DIEM_CUOI_ONG),mContext.getResources().getString(R.string.ID_DIEM_CUOI_ONG), mContext.getResources().getString(R.string.TITLE_DIEM_CUOI_ONG), mContext.getResources().getInteger(R.integer.minScale_diemcuoiong), mContext.getString(R.string.group_tai_san), mContext.getResources().getStringArray(R.array.update_fields_diemcuoiong)));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_DIEM_XA_CAN),mContext.getResources().getString(R.string.ID_DIEM_XA_CAN), mContext.getResources().getString(R.string.TITLE_DIEM_XA_CAN), mContext.getResources().getInteger(R.integer.minScale_diemxacan), mContext.getString(R.string.group_tai_san), mContext.getResources().getStringArray(R.array.update_fields_diemxacan)));


        return configs;
    }
}
