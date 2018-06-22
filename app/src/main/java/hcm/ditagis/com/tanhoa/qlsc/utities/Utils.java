package hcm.ditagis.com.tanhoa.qlsc.utities;

import java.text.NumberFormat;
import java.util.Locale;

public class Utils {
    private NumberFormat numberFormat;
    public static final int CHANGE_PASSWORD_OLD_PASSWORD_WRONG = -1;
    public static final int CHANGE_PASSWORD_FAILURE = 0;
    public static final int CHANGE_PASSWORD_SUCCESS = 1;
    public NumberFormat getNumberFormat() {
        numberFormat = NumberFormat.getNumberInstance(Locale.GERMAN);
        numberFormat.setGroupingUsed(true);
        return numberFormat;
    }

    private static Utils instance = null;

    public static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();

        }

        return instance;
    }

    private Utils() {

    }
}
