package hcm.ditagis.com.tanhoa.qlsc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import hcm.ditagis.com.tanhoa.qlsc.async.ChangePasswordAsycn;
import hcm.ditagis.com.tanhoa.qlsc.utities.Preference;
import hcm.ditagis.com.tanhoa.qlsc.utities.Utils;


public class DoiMatKhauActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEtxtOldPassord;
    private EditText mEtxtNewPassword;
    private EditText mEtxtNewPasswordConfirm;
    private TextView mTxtValidation;
    private TextView mTxtChangePassword;
    private LinearLayout mLayoutChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);
        mEtxtOldPassord = findViewById(R.id.etxt_change_password_old);
        mEtxtNewPassword = findViewById(R.id.etxt_change_password_new);
        mEtxtNewPasswordConfirm = findViewById(R.id.etxt_change_password_new_confirm);
        mTxtValidation = findViewById(R.id.txt_change_password_validation);
        mTxtChangePassword = findViewById(R.id.txt_change_password);
        mLayoutChangePassword = findViewById(R.id.layout_changepassword);

        findViewById(R.id.btn_change_password).setOnClickListener(this);
        View.OnKeyListener keyListener = new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    changPassword();
                }
                return false;
            }
        };
        mEtxtNewPasswordConfirm.setOnKeyListener(keyListener);

    }

    private boolean validate() {
        if (!mEtxtNewPassword.getText().toString().trim().equals(mEtxtNewPasswordConfirm.getText().toString().trim())) {
            mTxtValidation.setVisibility(View.VISIBLE);
            mTxtValidation.setText(getString(R.string.validate_change_password));
            return false;
        }
        if (mEtxtNewPassword.getText().toString().trim().length() < 6) {
            mTxtValidation.setVisibility(View.VISIBLE);
            mTxtValidation.setText(getString(R.string.validate_change_password_lack));
            return false;
        }
        mTxtValidation.setVisibility(View.GONE);
        return true;
    }

    private void changPassword() {
        if (validate()) {
            ChangePasswordAsycn asycn = new ChangePasswordAsycn(this, new ChangePasswordAsycn.AsyncResponse() {
                @Override
                public void processFinish(Integer output) {
                    if (output != null) {
                        int value = output.intValue();
                        switch (value) {
                            case Utils.CHANGE_PASSWORD_OLD_PASSWORD_WRONG:
                                mTxtValidation.setVisibility(View.VISIBLE);
                                mTxtValidation.setText(getString(R.string.validate_password_old));
                                break;
                            case Utils.CHANGE_PASSWORD_FAILURE:
                                mTxtValidation.setVisibility(View.VISIBLE);
                                mTxtValidation.setText(getString(R.string.validate_change_password_fail));
                                break;
                            case Utils.CHANGE_PASSWORD_SUCCESS:
                                mTxtChangePassword.setVisibility(View.VISIBLE);
                                mLayoutChangePassword.setVisibility(View.GONE);
                                break;
                        }

                    }
                }
            });
            asycn.execute(Preference.getInstance().loadPreference(getString(R.string.preference_username)), mEtxtOldPassord.getText().toString().trim(),
                    mEtxtNewPasswordConfirm.getText().toString().trim());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_password:
                changPassword();
                break;
        }
    }


}
