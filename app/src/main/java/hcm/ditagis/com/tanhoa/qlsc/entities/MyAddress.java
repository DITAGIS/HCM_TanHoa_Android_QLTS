package hcm.ditagis.com.tanhoa.qlsc.entities;

public class MyAddress {
    private double longtitude;
    private double latitude;
    private String subAdminArea;
    private String location;
    private String maDuong;
    private String maPhuong;
    private String maDMA;

    public MyAddress(double longtitude, double latitude, String subAdminArea, String location, String maDuong, String maPhuong, String maDMA) {
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.subAdminArea = subAdminArea;
        this.location = location;
        this.maDuong = maDuong;
        this.maPhuong = maPhuong;
        this.maDMA = maDMA;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getSubAdminArea() {
        return subAdminArea;
    }

    public String getLocation() {
        return location;
    }

    public String getMaDuong() {
        return maDuong;
    }

    public String getMaPhuong() {
        return maPhuong;
    }

    public String getMaDMA() {
        return maDMA;
    }
}
