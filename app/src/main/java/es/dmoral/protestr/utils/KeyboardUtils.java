package es.dmoral.protestr.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by grender on 5/07/17.
 */

public class KeyboardUtils {
    public static void closeKeyboard(View currentFocus) {
        if (currentFocus != null) {
            final InputMethodManager inputMethodManager =
                    (InputMethodManager) currentFocus.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
    }
}
