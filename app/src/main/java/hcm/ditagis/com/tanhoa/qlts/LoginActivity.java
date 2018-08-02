package hcm.ditagis.com.tanhoa.qlts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import hcm.ditagis.com.tanhoa.qlts.async.LoginAsycn;
import hcm.ditagis.com.tanhoa.qlts.async.NewLoginAsycn;
import hcm.ditagis.com.tanhoa.qlts.entities.entitiesDB.User;
import hcm.ditagis.com.tanhoa.qlts.utities.CheckConnectInternet;
import hcm.ditagis.com.tanhoa.qlts.utities.Preference;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTxtUsername;
    private TextView mTxtPassword;
    private boolean isLastLogin;
    private TextView mTxtValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(hcm.ditagis.com.tanhoa.qlts.R.layout.activity_login);

        Button btnLogin = (findViewById(hcm.ditagis.com.tanhoa.qlts.R.id.btnLogin));
        btnLogin.setOnClickListener(this);
        findViewById(hcm.ditagis.com.tanhoa.qlts.R.id.txt_login_changeAccount).setOnClickListener(this);

        mTxtUsername = findViewById(hcm.ditagis.com.tanhoa.qlts.R.id.txtUsername);
        mTxtPassword = findViewById(hcm.ditagis.com.tanhoa.qlts.R.id.txtPassword);
        mTxtPassword.setText("ditagis@123");
        mTxtUsername.setText("ditagis");
        mTxtValidation = findViewById(hcm.ditagis.com.tanhoa.qlts.R.id.txt_login_validation);
        create();
    }

    private void create() {
        Preference.getInstance().setContext(this);
        String preference_userName = Preference.getInstance().loadPreference(getString(hcm.ditagis.com.tanhoa.qlts.R.string.preference_username));

        //nếu chưa từng đăng nhập thành công trước đó
        //nhập username và password bình thường
        if (preference_userName == null || preference_userName.isEmpty()) {
            findViewById(hcm.ditagis.com.tanhoa.qlts.R.id.layout_login_tool).setVisibility(View.GONE);
            findViewById(hcm.ditagis.com.tanhoa.qlts.R.id.layout_login_username).setVisibility(View.VISIBLE);
            isLastLogin = false;
        }
        //ngược lại
        //chỉ nhập pasword
        else {
            isLastLogin = true;
            findViewById(hcm.ditagis.com.tanhoa.qlts.R.id.layout_login_tool).setVisibility(View.VISIBLE);
            findViewById(hcm.ditagis.com.tanhoa.qlts.R.id.layout_login_username).setVisibility(View.GONE);
        }

    }

    private void login() {
        if (!CheckConnectInternet.isOnline(this)) {
            mTxtValidation.setText(hcm.ditagis.com.tanhoa.qlts.R.string.validate_no_connect);
            mTxtValidation.setVisibility(View.VISIBLE);
            return;
        }
        mTxtValidation.setVisibility(View.GONE);

        String userName;
        if (isLastLogin)
            userName = Preference.getInstance().loadPreference(getString(hcm.ditagis.com.tanhoa.qlts.R.string.preference_username));
        else
            userName = mTxtUsername.getText().toString().trim();
        final String passWord = mTxtPassword.getText().toString().trim();
        if (userName.length() == 0 || passWord.length() == 0) {
            handleInfoLoginEmpty();
            return;
        }
        NewLoginAsycn loginAsycn = new NewLoginAsycn(this, new LoginAsycn.AsyncResponse() {

            @Override
            public void processFinish(User output) {
                if (output != null)
                    handleLoginSuccess(output);
                else
                    handleLoginFail();
            }
        });
        loginAsycn.execute(userName, passWord);
    }

    private void handleInfoLoginEmpty() {
        mTxtValidation.setText(hcm.ditagis.com.tanhoa.qlts.R.string.info_login_empty);
        mTxtValidation.setVisibility(View.VISIBLE);
    }

    private void handleLoginFail() {
        mTxtValidation.setText(hcm.ditagis.com.tanhoa.qlts.R.string.validate_login_fail);
        mTxtValidation.setVisibility(View.VISIBLE);
    }

    private void handleLoginSuccess(User user) {


        Preference.getInstance().savePreferences(getString(hcm.ditagis.com.tanhoa.qlts.R.string.preference_username), mTxtUsername.getText().toString());
//        Preference.getInstance().savePreferences(getString(R.string.preference_password), khachHang.getPassWord());
        Preference.getInstance().savePreferences(getString(hcm.ditagis.com.tanhoa.qlts.R.string.preference_displayname), user.getDisplayName());
        mTxtUsername.setText("");
        mTxtPassword.setText("");
        Intent intent = new Intent(this, QuanLyTaiSan.class);

        startActivity(intent);
    }

    private void changeAccount() {
        mTxtUsername.setText("");
        mTxtPassword.setText("");

        Preference.getInstance().savePreferences(getString(hcm.ditagis.com.tanhoa.qlts.R.string.preference_username), "");
        create();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        create();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case hcm.ditagis.com.tanhoa.qlts.R.id.btnLogin:
                login();
                break;
            case hcm.ditagis.com.tanhoa.qlts.R.id.txt_login_changeAccount:
                changeAccount();
                break;
        }

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                if (mTxtPassword.getText().toString().trim().length() > 0) {
                    login();
                    return true;
                }
            default:
                return super.onKeyUp(keyCode, event);
        }
    }
}
