package hcm.ditagis.com.tanhoa.qlts.entities.entitiesDB;

/**
 * Created by ThanLe on 09/11/2017.
 */

public class Code_CSC_SanLuong {
    private String code1;
    private String code2;
    private String code3;
    private String CSC1;
    private String CSC2;
    private String CSC3;
    private String sanLuong1;
    private String sanLuong2;
    private String sanLuong3;

    public Code_CSC_SanLuong(String code1, String code2, String code3, String CSC1, String CSC2, String CSC3, String sanLuong1, String sanLuong2, String sanLuong3) {
        this.code1 = code1;
        this.code2 = code2;
        this.code3 = code3;
        this.CSC1 = CSC1;
        this.CSC2 = CSC2;
        this.CSC3 = CSC3;
        this.sanLuong1 = sanLuong1;
        this.sanLuong2 = sanLuong2;
        this.sanLuong3 = sanLuong3;
    }

    public String getCode1() {
        return code1;
    }

    public String getCode2() {
        return code2;
    }

    public String getCode3() {
        return code3;
    }

    public String getCSC1() {
        return CSC1;
    }

    public String getCSC2() {
        return CSC2;
    }

    public String getCSC3() {
        return CSC3;
    }

    public String getSanLuong1() {
        return sanLuong1;
    }

    public String getSanLuong2() {
        return sanLuong2;
    }

    public String getSanLuong3() {
        return sanLuong3;
    }
}
