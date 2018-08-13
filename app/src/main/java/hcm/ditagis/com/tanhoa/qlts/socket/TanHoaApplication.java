package hcm.ditagis.com.tanhoa.qlts.socket;

import android.app.Application;

import java.net.URISyntaxException;

import hcm.ditagis.com.tanhoa.qlts.libs.Constants;
import io.socket.client.IO;
import io.socket.client.Socket;

public class TanHoaApplication extends Application {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String mUsername;

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public Socket getSocket() {
        return mSocket;
    }
}
