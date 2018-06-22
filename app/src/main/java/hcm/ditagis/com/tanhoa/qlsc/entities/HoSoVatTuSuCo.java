package hcm.ditagis.com.tanhoa.qlsc.entities;

public class HoSoVatTuSuCo {
    private String idSuCo;
    private int soLuong;
    private String maVatTu;
    private String tenVatTu;
    private String donViTinh;

    public HoSoVatTuSuCo(String idSuCo, int soLuong, String maVatTu, String tenVatTu, String donViTinh) {
        this.idSuCo = idSuCo;
        this.soLuong = soLuong;
        this.maVatTu = maVatTu;
        this.tenVatTu = tenVatTu;
        this.donViTinh = donViTinh;
    }

    public String getIdSuCo() {
        return idSuCo;
    }

    public void setIdSuCo(String idSuCo) {
        this.idSuCo = idSuCo;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getMaVatTu() {
        return maVatTu;
    }

    public void setMaVatTu(String maVatTu) {
        this.maVatTu = maVatTu;
    }

    public String getTenVatTu() {
        return tenVatTu;
    }

    public void setTenVatTu(String tenVatTu) {
        this.tenVatTu = tenVatTu;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }
}
