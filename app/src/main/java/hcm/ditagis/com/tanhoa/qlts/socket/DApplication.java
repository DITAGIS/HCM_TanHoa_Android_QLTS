package hcm.ditagis.com.tanhoa.qlts.socket;

import android.app.Application;
import android.location.Location;

import java.net.URISyntaxException;

import hcm.ditagis.com.tanhoa.qlts.entities.entitiesDB.User;
import hcm.ditagis.com.tanhoa.qlts.libs.Constants;
import io.socket.client.IO;
import io.socket.client.Socket;

public class DApplication extends Application {

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUserDangNhap;

    {
        getUserDangNhap = new User();
    }

    public void setUserDangNhap(User user) {
        getUserDangNhap = user;
    }

    public Socket getSocket() {
        return mSocket;
    }

    private Location mLocation;

    public Location getmLocation() {
        return mLocation;
    }

    public void setmLocation(Location mLocation) {
        this.mLocation = mLocation;
    }
}
