package io.ona.rdt_app.util;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.smartregister.Context;
import org.smartregister.domain.UniqueId;
import org.smartregister.exception.JsonFormMissingStepCountException;
import org.smartregister.repository.UniqueIdRepository;
import org.smartregister.util.AssetHandler;

import io.ona.rdt_app.application.RDTApplication;
import io.ona.rdt_app.domain.Patient;

import static io.ona.rdt_app.interactor.PatientRegisterFragmentInteractorTest.PATIENT_REGISTRATION_JSON_FORM;
import static io.ona.rdt_app.util.Constants.Form.RDT_TEST_FORM;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Vincent Karuri on 07/08/2019
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AssetHandler.class, RDTApplication.class, Context.class})
public class FormLauncherTest {

    @Captor
    private ArgumentCaptor<FormLaunchArgs> formLaunchArgsArgumentCaptor;

    @Mock
    private RDTJsonFormUtils formUtils;

    @Mock
    private RDTApplication rdtApplication;

    @Mock
    private Context drishtiContext;

    private FormLauncher formLauncher;

    private final String RDT_ID = "rdt_id";

    private final String OPENMRS_ID = "openmrs_id";

    private final UniqueId uniqueId = new UniqueId(RDT_ID, OPENMRS_ID, null, null, null);
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockStaticMethods();
        formLauncher = new FormLauncher();
    }
    
    @Test
    public void testLaunchFormShouldLaunchForm() throws JSONException {
        Activity activity = mock(Activity.class);
        JSONObject jsonForm = new JSONObject();

        doReturn(jsonForm).when(formUtils).getFormJsonObject(anyString(), any(Activity.class));
        Whitebox.setInternalState(formLauncher, "formUtils", formUtils);
        formLauncher.launchForm(activity, RDT_TEST_FORM, null);

        verify(formUtils).launchForm(eq(activity), eq(RDT_TEST_FORM), isNull(Patient.class), isNull());
    }

    @Test
    public void testLaunchFormShouldFetchUniqueIdBeforeFormLaunch() throws JSONException {
        Activity activity = mock(Activity.class);
        final String FORM_NAME = "form";
        Patient patient = mock(Patient.class);

        Whitebox.setInternalState(formLauncher, "formUtils", formUtils);
        formLauncher.launchForm(activity, FORM_NAME, patient);
        verify(formUtils).getNextUniqueId(formLaunchArgsArgumentCaptor.capture(), eq(formLauncher));

        FormLaunchArgs args = formLaunchArgsArgumentCaptor.getValue();
        assertEquals(args.getActivity(), activity);
        assertEquals(args.getPatient(), patient);
    }

    @Test
    public void testOnUniqueIdFetchedShouldLaunchForm() throws JSONException, JsonFormMissingStepCountException {
        FormLaunchArgs args = new FormLaunchArgs();
        Activity activity = mock(Activity.class);
        Patient patient = mock(Patient.class);
        JSONObject jsonForm = new JSONObject();
        args.withActivity(activity)
                .withFormJsonObj(jsonForm)
                .withPatient(patient);


        Whitebox.setInternalState(formLauncher, "formUtils", formUtils);
        formLauncher.onUniqueIdFetched(args, uniqueId);

        verify(formUtils).launchForm(eq(activity), eq(RDT_TEST_FORM), eq(patient), eq(OPENMRS_ID));
    }

    private void mockStaticMethods() throws JsonFormMissingStepCountException, JSONException {
        // mock RDTApplication and Drishti context
        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getContext()).thenReturn(drishtiContext);

        // mock repositories
        UniqueIdRepository uniqueIdRepository = mock(UniqueIdRepository.class);
        PowerMockito.when(drishtiContext.getUniqueIdRepository()).thenReturn(uniqueIdRepository);
        when(uniqueIdRepository.getNextUniqueId()).thenReturn(uniqueId);

        // mock AssetHanler
        mockStatic(AssetHandler.class);
        PowerMockito.when( AssetHandler.readFileFromAssetsFolder(any(), any())).thenReturn(PATIENT_REGISTRATION_JSON_FORM);
    }
}
