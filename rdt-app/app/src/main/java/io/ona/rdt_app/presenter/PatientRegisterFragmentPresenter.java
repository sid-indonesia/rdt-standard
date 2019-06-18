package io.ona.rdt_app.presenter;

import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;
import org.smartregister.view.contract.BaseRegisterFragmentContract;

import io.ona.rdt_app.contract.PatientRegisterFragmentContract;
import io.ona.rdt_app.fragment.PatientRegisterFragment;
import io.ona.rdt_app.interactor.PatientRegisterFragmentInteractor;

import static io.ona.rdt_app.util.Constants.PATIENTS;

/**
 * Created by Vincent Karuri on 11/06/2019
 */
public class PatientRegisterFragmentPresenter implements BaseRegisterFragmentContract.Presenter, PatientRegisterFragmentContract.Presenter {

    private PatientRegisterFragmentInteractor interactor = new PatientRegisterFragmentInteractor();
    private PatientRegisterFragment patientRegisterFragment;

    public PatientRegisterFragmentPresenter(PatientRegisterFragment patientRegisterFragment) {
        this.patientRegisterFragment = patientRegisterFragment;
    }

    @Override
    public void processViewConfigurations() {

    }

    @Override
    public void initializeQueries(String mainCondition) {
        String tableName = PATIENTS;
        String countSelect = countSelect(tableName, mainCondition);
        String mainSelect = mainSelect(tableName, mainCondition);
        patientRegisterFragment.initializeQueryParams(tableName, countSelect, mainSelect);
        patientRegisterFragment.initializeAdapter();
        patientRegisterFragment.countExecute();
        patientRegisterFragment.filterandSortInInitializeQueries();
    }

    @Override
    public void startSync() {

    }

    @Override
    public void searchGlobally(String uniqueId) {

    }

    @Override
    public void saveForm(String jsonForm) throws JSONException {
        interactor.saveRegistrationForm(new JSONObject(jsonForm));
    }

    public String countSelect(String tableName, String mainCondition) {
        SmartRegisterQueryBuilder countQueryBuilder = new SmartRegisterQueryBuilder();
        countQueryBuilder.SelectInitiateMainTableCounts(tableName);
        return countQueryBuilder.mainCondition(mainCondition);
    }

    public String mainSelect(String tableName, String mainCondition) {
        SmartRegisterQueryBuilder queryBUilder = new SmartRegisterQueryBuilder();
        queryBUilder.SelectInitiateMainTable(tableName, this.mainColumns(tableName));
        return queryBUilder.mainCondition(mainCondition);
    }

    private String[] mainColumns(String tableName) {
        String[] columns = new String[]{tableName + "." + "relationalid", tableName + ".name", tableName + "." + "age", tableName + "." + "sex"};
        return columns;
    }

    @Override
    public String getMainCondition() {
        return String.format(" %s != '%s'", "id", "");
    }
}
