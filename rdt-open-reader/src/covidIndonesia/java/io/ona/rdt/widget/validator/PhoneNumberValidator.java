package io.ona.rdt.widget.validator;

import android.util.Patterns;

import androidx.annotation.NonNull;

import com.rengwuxian.materialedittext.validation.METValidator;

import java.util.regex.Pattern;

public class PhoneNumberValidator extends METValidator {

    public PhoneNumberValidator(@NonNull String errorMessage) {
        super(errorMessage);
    }

    @Override
    public boolean isValid(@NonNull CharSequence charSequence, boolean isEmpty) {
        if (!isEmpty) {
            return Pattern.compile("[0-9]{8,13}").matcher(charSequence).matches();
        }

        return true;
    }
}
