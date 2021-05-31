package io.ona.rdt.widget.validator;

import android.util.Patterns;

import androidx.annotation.NonNull;

import com.rengwuxian.materialedittext.validation.METValidator;

import org.apache.commons.lang3.StringUtils;

public class EmailValidator extends METValidator {

    public EmailValidator(@NonNull String errorMessage) {
        super(errorMessage);
    }

    @Override
    public boolean isValid(@NonNull CharSequence charSequence, boolean isEmpty) {
        if (!isEmpty) {
            return Patterns.EMAIL_ADDRESS.matcher(charSequence).matches();
        }

        return true;
    }
}
