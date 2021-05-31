package io.ona.rdt.widget;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.utils.FormUtils;
import com.vijay.jsonwizard.widgets.EditTextFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.widget.validator.EmailValidator;
import io.ona.rdt.widget.validator.PhoneNumberValidator;
import timber.log.Timber;

public class CovidEditTextFactory extends EditTextFactory {

    private static final String RIGHT_MARGIN = "right_margin";

    @Override
    protected List<View> attachJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener, boolean popup) throws Exception {
        List<View> views = super.attachJson(stepName, context, formFragment, jsonObject, listener, popup);
        views.get(0).setMinimumHeight(0);
        return views;
    }

    @Override
    protected void attachLayout(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, MaterialEditText editText, ImageView editButton) throws Exception {
        formFragment.getJsonApi().getAppExecutors().mainThread().execute(() -> {
            try {
                CovidEditTextFactory.super.attachLayout(stepName, context, formFragment, jsonObject, editText, editButton);
            } catch (Exception e) {
                Timber.e(e);
            }
        });

        addEmailValidator(jsonObject, editText);
        addPhoneNumberValidator(jsonObject, editText);

        if (!jsonObject.has(JsonFormConstants.HINT)) {
            editText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NONE);
        }

        updateMargin(editText, jsonObject, context);
        updateTopPadding(editText, jsonObject, context);
    }

    protected void updateTopPadding(MaterialEditText editText, JSONObject jsonObject, Context context) {
        final int topPadding =  jsonObject.has(JsonFormConstants.TOP_PADDING) ? getValueFromSpOrDpOrPx(context, jsonObject.optString(JsonFormConstants.TOP_PADDING, "0dp")) : editText.getFloatingLabelPadding();
        editText.setFloatingLabelPadding(topPadding);
    }

    protected void updateMargin(MaterialEditText editText, JSONObject jsonObject, Context context) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) editText.getLayoutParams();

        int leftMargin = normalizeValue(context, jsonObject, JsonFormConstants.LEFT_MARGIN, layoutParams.leftMargin);
        int topMargin = normalizeValue(context, jsonObject, JsonFormConstants.TOP_MARGIN, layoutParams.topMargin);
        int rightMargin = normalizeValue(context, jsonObject, RIGHT_MARGIN, layoutParams.rightMargin);
        int bottomMargin = normalizeValue(context, jsonObject, JsonFormConstants.BOTTOM_MARGIN, layoutParams.bottomMargin);

        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
    }

    protected int getValueFromSpOrDpOrPx(Context context, String value) {
        return FormUtils.getValueFromSpOrDpOrPx(value, context);
    }

    private int normalizeValue(Context context, JSONObject jsonObject, String key, int defaultValue) {
        return jsonObject.has(key) ? getValueFromSpOrDpOrPx(context, jsonObject.optString(key, "0dp")) : defaultValue;
    }

    private void addEmailValidator(JSONObject jsonObject, MaterialEditText editText) throws JSONException {
        JSONObject object = jsonObject.optJSONObject(CovidConstants.Validation.V_EMAIL);
        if (object != null) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            editText.addValidator(new EmailValidator(object.getString(JsonFormConstants.ERR)));
        }
    }

    private void addPhoneNumberValidator(JSONObject jsonObject, MaterialEditText editText) throws JSONException {
        JSONObject object = jsonObject.optJSONObject(CovidConstants.Validation.V_PHONE_NUMBER);
        if (object != null) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.addValidator(new PhoneNumberValidator(object.getString(JsonFormConstants.ERR)));
        }
    }
}
