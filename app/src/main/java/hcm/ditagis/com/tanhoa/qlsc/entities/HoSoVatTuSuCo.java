package hcm.ditagis.com.tanhoa.qlsc.entities;

public class HoSoVatTuSuCo {
    private String idSuCo;
    private double soLuong;
    private String maVatTu;
    private String tenVatTu;
    private String donViTinh;

    public HoSoVatTuSuCo(String idSuCo, double soLuong, String maVatTu, String tenVatTu, String donViTinh) {
        this.idSuCo = idSuCo;
        this.soLuong = soLuong;
        this.maVatTu = maVatTu;
        this.tenVatTu = tenVatTu;
        this.donViTinh = donViTinh;
    }

    public String getIdSuCo() {
        return idSuCo;
    }

    public double getSoLuong() {
        return soLuong;
    }

    public String getMaVatTu() {
        return maVatTu;
    }

    public String getTenVatTu() {
        return tenVatTu;
    }

    public String getDonViTinh() {
        return donViTinh;
    }
}
