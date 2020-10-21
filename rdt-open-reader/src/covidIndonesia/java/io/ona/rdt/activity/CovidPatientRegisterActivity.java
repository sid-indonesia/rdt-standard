package io.ona.rdt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;

import com.vijay.jsonwizard.utils.AllSharedPreferences;

import org.smartregister.view.fragment.BaseRegisterFragment;

import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.CovidPatientRegisterFragment;
import io.ona.rdt.presenter.CovidPatientRegisterActivityPresenter;
import io.ona.rdt.presenter.PatientRegisterActivityPresenter;
import io.ona.rdt.util.CovidRDTJsonFormUtils;
import io.ona.rdt.util.RDTJsonFormUtils;
import io.ona.rdt.util.Utils;

import static io.ona.rdt.util.CovidConstants.Form.SAMPLE_DELIVERY_DETAILS_FORM;

/**
 * Created by Vincent Karuri on 16/06/2020
 */
public class CovidPatientRegisterActivity extends PatientRegisterActivity {

    private int selectedLanguageIndex;
    private AllSharedPreferences allSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerLanguageSwitcher();
    }

    @Override
    public BaseRegisterFragment getRegisterFragment() {
        return new CovidPatientRegisterFragment();
    }

    @Override
    protected RDTJsonFormUtils initializeFormUtils() {
        return new CovidRDTJsonFormUtils();
    }

    @Override
    public boolean selectDrawerItem(MenuItem menuItem) {
        if (super.selectDrawerItem(menuItem)) {
            return true;
        }
        switch(menuItem.getItemId()) {
            case R.id.menu_item_create_shipment:
                getPresenter().launchForm(this, SAMPLE_DELIVERY_DETAILS_FORM, null);
                return true;

            case R.id.menu_item_switch_language:
                languageSwitcherDialog();
                return true;
        }
        return false;
    }

    @Override
    protected Class getLoginPage() {
        return CovidLoginActivity.class;
    }

    protected PatientRegisterActivityPresenter createPatientRegisterActivityPresenter() {
        return new CovidPatientRegisterActivityPresenter(this);
    }

    private void languageSwitcherDialog() {
        String[] localesVal = getResources().getStringArray(R.array.locales_value);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.drawer_menu_item_change_language));
        builder.setSingleChoiceItems(R.array.locales_key, selectedLanguageIndex, (dialog, position) -> {
            allSharedPreferences.saveLanguagePreference(localesVal[position]);
            Utils.updateLocale(CovidPatientRegisterActivity.this);
            reloadClass();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void registerLanguageSwitcher() {
        allSharedPreferences = RDTApplication.getInstance().getSharedPreferences();
        String activeLanguage = getResources().getConfiguration().locale.getLanguage();

        String[] localesVal = getResources().getStringArray(R.array.locales_value);

        for (int i = 0; i < localesVal.length; i++) {
            if (activeLanguage.equals(localesVal[i])) {
                selectedLanguageIndex = i;
            }
        }
    }

    private void reloadClass() {
        Intent intent = getIntent();
        startActivity(intent);
        finish();
    }
}
