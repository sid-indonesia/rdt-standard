package io.ona.rdt.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.contract.TestsProfileFragmentContract;
import io.ona.rdt.domain.FormattedRDTTestDetails;
import io.ona.rdt.domain.ParasiteProfileResult;
import io.ona.rdt.presenter.TestsProfileFragmentPresenter;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.Table.MICROSCOPY_RESULTS;
import static io.ona.rdt.util.Constants.Table.PCR_RESULTS;
import static io.ona.rdt.util.Constants.Test.BLOODSPOT_Q_PCR;
import static io.ona.rdt.util.Constants.Test.INVALID;
import static io.ona.rdt.util.Constants.Test.MICROSCOPY;
import static io.ona.rdt.util.Constants.Test.NEGATIVE;
import static io.ona.rdt.util.Constants.Test.POSITIVE;
import static io.ona.rdt.util.Constants.Test.RDT_Q_PCR;
import static io.ona.rdt.util.Constants.Test.RDT_TEST_DETAILS;
import static io.ona.rdt.util.Utils.convertDate;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.chop;

/**
 * Created by Vincent Karuri on 29/01/2020
 */
public class TestsProfileFragment extends Fragment implements View.OnClickListener, TestsProfileFragmentContract.View {

    private FormattedRDTTestDetails formattedRDTTestDetails;
    private TestsProfileFragmentPresenter presenter;
    private View rootLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        formattedRDTTestDetails = getArguments().getParcelable(RDT_TEST_DETAILS);
        presenter = new TestsProfileFragmentPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootLayout = inflater.inflate(R.layout.fragment_tests_profile, container, false);
        rootLayout.findViewById(R.id.btn_back_to_patient_profile).setOnClickListener(this);
        rootLayout.findViewById(R.id.tv_back_to_profile_text).setOnClickListener(this);
        populateRDTTestDetails(rootLayout);

        return rootLayout;
    }

    private void populateRDTTestDetails(View rootLayout) {
        ((TextView) rootLayout.findViewById(R.id.tests_profile_rdt_id))
                .setText(formattedRDTTestDetails.getFormattedRDTId());

        View testNameAndDate = rootLayout.findViewById(R.id.rdt_test_name_and_date);
        ((TextView) testNameAndDate.findViewById(R.id.tv_results_label))
                .setText(formattedRDTTestDetails.getFormattedRDTType());
        ((TextView) testNameAndDate.findViewById(R.id.tv_results_date))
                .setText(formattedRDTTestDetails.getFormattedRDTTestDate());

        populateRDTTestResults();

        String[] fragmentedRdtId = formattedRDTTestDetails.getFormattedRDTId().split(" ");
        String rdtId = fragmentedRdtId[fragmentedRdtId.length - 1];
        getParasiteProfiles(rdtId, PCR_RESULTS, RDT_Q_PCR);
        getParasiteProfiles(rdtId, PCR_RESULTS, BLOODSPOT_Q_PCR);
        getParasiteProfiles(rdtId, MICROSCOPY_RESULTS, MICROSCOPY);
    }

    private void populateRDTTestResults() {
        TextView pvResult = rootLayout.findViewById(R.id.tv_rdt_pv_result);
        TextView pfResult = rootLayout.findViewById(R.id.tv_rdt_pf_result);
        switch (formattedRDTTestDetails.getTestResult()) {
            case (NEGATIVE):
                pvResult.setText(R.string.pv_negative_result);
                pfResult.setText(R.string.pf_negative_result);
                break;
            case (INVALID):
                pfResult.setText(R.string.invalid_result);
                pvResult.setVisibility(View.GONE);
                break;
            case (POSITIVE):
                setTestPositiveStatus(pfResult, pvResult);
                break;
            default:
                // do nothing
        }
    }

    private void setTestPositiveStatus(TextView pfResult, TextView pvResult) {
        String[] parasites = formattedRDTTestDetails.getFormattedTestResults().split(",");
        if (parasites.length < 2) {
            pfResult.setText(parasites[0]);
            String pfPositive = getResources().getString(R.string.pf_positive_result);
            if (pfPositive.equals(parasites[0])) {
                pvResult.setText(R.string.pv_negative_result);
            } else {
                pvResult.setText(R.string.pf_negative_result);
            }
        } else {
            pfResult.setText(parasites[0]);
            pvResult.setText(parasites[1]);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back_to_profile_text:
            case R.id.btn_back_to_patient_profile:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            default:
                // do nothing
        }
    }

    public synchronized void getParasiteProfiles(String rdtId, String tableName, String experimentType) {
        class GetParasiteProfilesTask extends AsyncTask<Void, Void, List<ParasiteProfileResult>> {
            @Override
            protected List<ParasiteProfileResult> doInBackground(Void... voids) {
                return presenter.getParasiteProfiles(rdtId, tableName, experimentType);
            }

            @Override
            protected void onPostExecute(List<ParasiteProfileResult> parasiteProfileResults) {
                onParasiteProfileFetched(parasiteProfileResults);
            }
        }
        new GetParasiteProfilesTask().execute();
    }

    @Override
    public synchronized void onParasiteProfileFetched(List<ParasiteProfileResult> parasiteProfileResults) {
        try {
            for (ParasiteProfileResult parasiteProfileResult : parasiteProfileResults) {
                switch (parasiteProfileResult.getExperimentType()) {
                    case MICROSCOPY:
                        populateParasiteProfileFields(rootLayout.findViewById(R.id.rdt_microscopy_results), parasiteProfileResult);
                        break;
                    case BLOODSPOT_Q_PCR:
                        populateParasiteProfileFields(rootLayout.findViewById(R.id.blood_spot_qpcr_results), parasiteProfileResult);
                        break;
                    case RDT_Q_PCR:
                        populateParasiteProfileFields(rootLayout.findViewById(R.id.rdt_qpcr_results), parasiteProfileResult);
                        break;
                    default:
                        // do nothing
                }
            }
        } catch (ParseException e) {
            Timber.e(e);
        }
    }

    private void populateParasiteProfileFields(View parasiteProfile, ParasiteProfileResult parasiteProfileResult) throws ParseException {
        populateFormattedParasiteProfile(parasiteProfile, parasiteProfileResult);

        String formattedExperimentType = formattedExperimentType(parasiteProfileResult.getExperimentType());
        String humanReadableDate = convertDate(parasiteProfileResult.getExperimentDate(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "dd MMM yyyy");
        View labelAndDate = parasiteProfile.findViewById(R.id.label_and_date);
        ((TextView) labelAndDate.findViewById(R.id.tv_results_date)).setText(humanReadableDate);
        ((TextView) labelAndDate.findViewById(R.id.tv_results_label)).setText(formattedExperimentType);
    }

    private String formattedExperimentType(String experimentType) {
        String formattedExperimentType = "";
        switch (experimentType) {
            case RDT_Q_PCR:
                formattedExperimentType =  formattedRDTTestDetails.getFormattedRDTType() + " qPCR";
                break;
            case BLOODSPOT_Q_PCR:
                formattedExperimentType = "Blood Spot qPCR";
                break;
            case MICROSCOPY:
                formattedExperimentType = "Microscopy";
                break;
            default:
                // do nothing
        }
        return formattedExperimentType;
    }

    private void populateFormattedParasiteProfile(View parasiteProfile, ParasiteProfileResult parasiteProfileResult) {
        ((TextView) parasiteProfile.findViewById(R.id.tv_qpcr_falciparum)).setText("P. falciparum - " + capitalize(parasiteProfileResult.getpFalciparum()));
        ((TextView) parasiteProfile.findViewById(R.id.tv_qpcr_vivax)).setText("P. vivax - " + capitalize(parasiteProfileResult.getpVivax()));
        ((TextView) parasiteProfile.findViewById(R.id.tv_qpcr_malariae)).setText("P. malariae - " + capitalize(parasiteProfileResult.getpMalariae()));
        ((TextView) parasiteProfile.findViewById(R.id.tv_qpcr_ovale)).setText("P. ovale - " + capitalize(parasiteProfileResult.getpOvale()));
        ((TextView) parasiteProfile.findViewById(R.id.tv_qpcr_gameto)).setText("Pf. gameto - " + capitalize(parasiteProfileResult.getPfGameto()));
    }
}
