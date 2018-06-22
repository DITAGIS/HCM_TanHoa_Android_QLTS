package hcm.ditagis.com.tanhoa.qlsc.entities.entitiesDB;

public class KhachHang {
    private String userName;
    private String passWord;
    private String displayName;
    private boolean isTanBinh;
    private boolean isTanPhu;
    private boolean isPhuNhuan;
    public static KhachHang khachHangDangNhap;

    public KhachHang() {

    }

    public KhachHang(String userName, String passWord, String nameDisplay, boolean isTanBinh, boolean isTanPhu, boolean isPhuNhuan) {
        this.userName = userName;
        this.passWord = passWord;
        this.displayName = nameDisplay;
        this.isTanBinh = isTanBinh;
        this.isTanPhu = isTanPhu;
        this.isPhuNhuan = isPhuNhuan;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isTanBinh() {
        return isTanBinh;
    }

    public void setTanBinh(boolean tanBinh) {
        isTanBinh = tanBinh;
    }

    public boolean isTanPhu() {
        return isTanPhu;
    }

    public void setTanPhu(boolean tanPhu) {
        isTanPhu = tanPhu;
    }

    public boolean isPhuNhuan() {
        return isPhuNhuan;
    }

    public void setPhuNhuan(boolean phuNhuan) {
        isPhuNhuan = phuNhuan;
    }
}
