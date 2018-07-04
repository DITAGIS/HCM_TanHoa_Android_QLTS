package hcm.ditagis.com.tanhoa.qlsc.utities;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import hcm.ditagis.com.tanhoa.qlsc.R;

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


        configs.add(new Config(mContext.getResources().getString(R.string.URL_HANH_CHINH), null, null,
                mContext.getResources().getString(R.string.ALIAS_HANH_CHINH),
                mContext.getResources().getInteger(R.integer.MIN_SCALE_HANH_CHINH), new String[]{}));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_DMA),
                mContext.getResources().getStringArray(R.array.queryFields_dma),
                mContext.getResources().getStringArray(R.array.outFields_dma),
                mContext.getResources().getString(R.string.ALIAS_DMA),
                mContext.getResources().getInteger(R.integer.minScale_dma), new String[]{}));
        configs.add(new Config(mContext.getResources().getString(R.string.URL_THUA_DAT), null, null,
                mContext.getResources().getString(R.string.ALIAS_THUA_DAT),
                mContext.getResources().getInteger(R.integer.MIN_SCALE_THUA_DAT), new String[]{}));

        configs.add(new Config(mContext.getResources().getString(R.string.URL_SONG_HO), null, null,
                mContext.getResources().getString(R.string.ALIAS_SONG_HO),
                mContext.getResources().getInteger(R.integer.MIN_SCALE_SONG_HO), new String[]{}));

        configs.add(new Config(mContext.getResources().getString(R.string.URL_GIAO_THONG), null, null,
                mContext.getResources().getString(R.string.ALIAS_GIAO_THONG),
                mContext.getResources().getInteger(R.integer.MIN_SCALE_GIAO_THONG), new String[]{}));






        configs.add(new Config(mContext.getResources().getString(R.string.URL_VAN),
                mContext.getResources().getStringArray(R.array.queryFields_van),
                mContext.getResources().getStringArray(R.array.outFields_van),
                mContext.getResources().getString(R.string.ALIAS_VAN),
                mContext.getResources().getInteger(R.integer.minScale_van), new String[]{}));



        configs.add(new Config(mContext.getResources().getString(R.string.URL_DONG_HO_TONG),
                mContext.getResources().getStringArray(R.array.queryFields_donghotong),
                mContext.getResources().getStringArray(R.array.outFields_donghotong)
                , mContext.getResources().getString(R.string.ALIAS_DONG_HO_TONG),
                mContext.getResources().getInteger(R.integer.minScale_donghotong), new String[]{}));

        configs.add(new Config(mContext.getResources().getString(R.string.URL_ONG_NGANH),
                mContext.getResources().getStringArray(R.array.queryFields_ongnganh),
                mContext.getResources().getStringArray(R.array.outFields_ongnganh),
                mContext.getResources().getString(R.string.ALIAS_ONG_NGANH),
                mContext.getResources().getInteger(R.integer.minScale_ongnganh), new String[]{}));

        configs.add(new Config(mContext.getResources().getString(R.string.URL_ONG_PHAN_PHOI),
                mContext.getResources().getStringArray(R.array.queryFields_ongphanphoi),
                mContext.getResources().getStringArray(R.array.outFields_ongphanphoi),
                mContext.getResources().getString(R.string.ALIAS_ONG_PHAN_PHOI),
                mContext.getResources().getInteger(R.integer.minScale_ongphanphoi), new String[]{}));

        configs.add(new Config(mContext.getResources().getString(R.string.URL_ONG_TRUYEN_DAN),
                mContext.getResources().getStringArray(R.array.queryFields_ongtruyendan),
                mContext.getResources().getStringArray(R.array.outFields_ongtruyendan),
                mContext.getResources().getString(R.string.ALIAS_ONG_TRUYEN_DAN),
                mContext.getResources().getInteger(R.integer.minScale_ongtruyendan), new String[]{}));

        configs.add(new Config(mContext.getResources().getString(R.string.URL_MOI_NOI),
                mContext.getResources().getStringArray(R.array.queryFields_moinoi),
                mContext.getResources().getStringArray(R.array.outFields_moinoi),
                mContext.getResources().getString(R.string.ALIAS_MOI_NOI),
                mContext.getResources().getInteger(R.integer.minScale_moinoi), new String[]{}));

        configs.add(new Config(mContext.getResources().getString(R.string.URL_TRU_HONG),
                mContext.getResources().getStringArray(R.array.queryFields_truhong),
                mContext.getResources().getStringArray(R.array.outFields_truhong),
                mContext.getResources().getString(R.string.ALIAS_TRU_HONG),
                mContext.getResources().getInteger(R.integer.minScale_truhong), new String[]{}));

        configs.add(new Config(mContext.getResources().getString(R.string.URL_DIEM_AP_LUC),
                mContext.getResources().getStringArray(R.array.queryFields_diemapluc),
                mContext.getResources().getStringArray(R.array.outFields_diemapluc),
                mContext.getResources().getString(R.string.ALIAS_DIEM_AP_LUC),
                mContext.getResources().getInteger(R.integer.minScale_diemapluc), new String[]{}));

        configs.add(new Config(mContext.getResources().getString(R.string.URL_DIEM_CUOI_ONG),
                mContext.getResources().getStringArray(R.array.queryFields_diemcuoiong),
                mContext.getResources().getStringArray(R.array.outFields_diemcuoiong),
                mContext.getResources().getString(R.string.ALIAS_DIEM_CUOI_ONG),
                mContext.getResources().getInteger(R.integer.minScale_diemcuoiong), new String[]{}));

        configs.add(new Config(mContext.getResources().getString(R.string.URL_DONG_HO_KHACH_HANG),
                mContext.getResources().getStringArray(R.array.queryFields_donghokhachhang),
                mContext.getResources().getStringArray(R.array.outFields_donghokhachhang),
                mContext.getResources().getString(R.string.ALIAS_DONG_HO_KHACH_HANG),
                mContext.getResources().getInteger(R.integer.minScale_donghokhachhang), new String[]{}));

        return configs;
    }

    public static class Name {
        public static String name_diemsuco = "DIEMSUCO";
    }
//    }
}
