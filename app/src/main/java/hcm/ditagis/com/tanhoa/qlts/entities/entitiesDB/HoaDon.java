package hcm.ditagis.com.tanhoa.qlts.entities.entitiesDB;

/**
 * @author fuhi
 */
public class HoaDon {
    private String id;
    private String dot;
    private String danhBo;
    private String tenKhachHang;
    private String soNha;
    private String duong;
    private String giaBieu;
    private String dinhMuc;
    private String ky;
    private String nam;
    private String codeCu;
    private String chiSoCu;
    private String codeMoi;
    private String chiSoMoi;
    private String tieuThuMoi;
    private String maLoTrinh;
    private String sdt;
    private String ghiChu;
    private String image;
    private byte[] image_byteArray;
    private String thoiGian;
    private Code_CSC_SanLuong code_CSC_SanLuong;
    private int flag;
    private int sh, sx, dv, hc;
    private int csgo, csganmoi;
    private String soThan;
    private String viTri;
    private String hieu;
    private String co;
    private String tieuThuTBMoi;
    private String tuNgay, denNgay;
    private String staffPhone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HoaDon() {
    }

    public HoaDon(String dot, String danhBo, String tenKhachHang, String soNha, String duong, String giaBieu, String dinhMuc, String ky, String chiSoCu, String maLoTrinh, String sdt, int flag) {
        this.dot = dot;
        this.danhBo = danhBo;
        this.tenKhachHang = tenKhachHang;
        this.soNha = soNha;
        this.duong = duong;
        this.giaBieu = giaBieu;
        this.dinhMuc = dinhMuc;
        this.ky = ky;
        this.chiSoCu = chiSoCu;
        this.maLoTrinh = maLoTrinh;
        this.sdt = sdt;
        this.flag = flag;
    }

    public int getCsganmoi() {
        return csganmoi;
    }

    public void setCsgan(int csganmoi) {
        this.csganmoi = csganmoi;
    }

    public byte[] getImage_byteArray() {
        return image_byteArray;
    }

    public void setImage_byteArray(byte[] image_byteArray) {
        this.image_byteArray = image_byteArray;
    }

    public int getCsgo() {
        return csgo;
    }

    public void setCsgo(int csgo) {
        this.csgo = csgo;
    }

    public String getStaffPhone() {
        return staffPhone;
    }

    public void setStaffPhone(String staffPhone) {
        this.staffPhone = staffPhone;
    }

    public String getTuNgay() {
        return tuNgay;
    }

    public void setTuNgay(String tuNgay) {
        this.tuNgay = tuNgay;
    }

    public String getDenNgay() {
        return denNgay;
    }

    public void setDenNgay(String denNgay) {
        this.denNgay = denNgay;
    }

    public String getTieuThuTBMoi() {
        return tieuThuTBMoi;
    }

    public void setTieuThuTBMoi(String tieuThuTBMoi) {
        this.tieuThuTBMoi = tieuThuTBMoi;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getHieu() {
        return hieu;
    }

    public void setHieu(String hieu) {
        this.hieu = hieu;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getSoThan() {
        return soThan;
    }

    public void setSoThan(String soThan) {
        this.soThan = soThan;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getCodeMoi() {
        return codeMoi;
    }

    public void setCodeMoi(String codeMoi) {
        this.codeMoi = codeMoi;
    }

    public String getChiSoMoi() {
        return chiSoMoi;
    }

    public void setChiSoMoi(String chiSoMoi) {
        this.chiSoMoi = chiSoMoi;
    }

    public String getTieuThuMoi() {
        return tieuThuMoi;
    }

    public void setTieuThuMoi(String tieuThuMoi) {
        this.tieuThuMoi = tieuThuMoi;
    }

    public int getSh() {
        return sh;
    }

    public void setSh(int sh) {
        this.sh = sh;
    }

    public int getSx() {
        return sx;
    }

    public void setSx(int sx) {
        this.sx = sx;
    }

    public int getDv() {
        return dv;
    }

    public void setDv(int dv) {
        this.dv = dv;
    }

    public int getHc() {
        return hc;
    }

    public void setHc(int hc) {
        this.hc = hc;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Code_CSC_SanLuong getCode_CSC_SanLuong() {
        return code_CSC_SanLuong;
    }

    public void setCode_CSC_SanLuong(Code_CSC_SanLuong code_CSC_SanLuong) {
        this.code_CSC_SanLuong = code_CSC_SanLuong;
    }

    public String getDot() {
        return dot;
    }

    public void setDot(String dot) {
        this.dot = dot;
    }

    public String getDanhBo() {
        return danhBo;
    }

    public void setDanhBo(String danhBo) {
        this.danhBo = danhBo;
    }


    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getSoNha() {
        return soNha;
    }

    public void setSoNha(String soNha) {
        this.soNha = soNha;
    }

    public String getDuong() {
        return duong;
    }

    public void setDuong(String duong) {
        this.duong = duong;
    }

    public String getGiaBieu() {
        return giaBieu;
    }

    public void setGiaBieu(String giaBieu) {
        this.giaBieu = giaBieu;
    }

    public String getDinhMuc() {
        return dinhMuc;
    }

    public void setDinhMuc(String dinhMuc) {
        this.dinhMuc = dinhMuc;
    }

    public String getKy() {
        return ky;
    }

    public void setKy(String ky) {
        this.ky = ky;
    }

    public String getNam() {
        return nam;
    }

    public void setNam(String nam) {
        this.nam = nam;
    }

    public String getCodeCu() {
        return codeCu;
    }

    public void setCodeCu(String codeCu) {
        this.codeCu = codeCu;
    }


    public String getChiSoCu() {
        return chiSoCu;
    }

    public void setChiSoCu(String chiSoCu) {
        this.chiSoCu = chiSoCu;
    }


    public String getMaLoTrinh() {
        return maLoTrinh;
    }

    public void setMaLoTrinh(String maLoTrinh) {
        this.maLoTrinh = maLoTrinh;
    }

    public String getDiaChi() {
        return getSoNha() + " " + getDuong();
    }


}
