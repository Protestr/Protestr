package es.dmoral.protestr.custom;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by grender on 4/07/17.
 */

public abstract class SimplifiedTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // ignored
    }

    @Override
    public abstract void onTextChanged(CharSequence charSequence, int i, int i1, int i2);

    @Override
    public void afterTextChanged(Editable editable) {
        // ignored
    }
}
