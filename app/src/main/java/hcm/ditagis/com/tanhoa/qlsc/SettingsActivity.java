package hcm.ditagis.com.tanhoa.qlsc;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;

import hcm.ditagis.com.tanhoa.qlsc.adapter.SettingsAdapter;
import hcm.ditagis.com.tanhoa.qlsc.utities.Constant;
import hcm.ditagis.com.tanhoa.qlsc.utities.Preference;

public class SettingsActivity extends AppCompatActivity {
    private SettingsAdapter mSettingsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Preference.getInstance().setContext(SettingsActivity.this);

        ListView mLstViewSettings = findViewById(R.id.lstView_Settings);
        mSettingsAdapter = new SettingsAdapter(this, Constant.getInstance().getSettingsItems());
        mLstViewSettings.setAdapter(mSettingsAdapter);
        mLstViewSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                }

            }
        });
    }



}
