package io.ona.rdt.activity;

import android.os.Bundle;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.interfaces.FormWidgetFactory;

import java.util.Map;

import io.ona.rdt.interactor.RDTJsonFormInteractor;
import io.ona.rdt.widget.CovidDatePickerFactory;
import io.ona.rdt.widget.CovidRDTLabelFactory;
import io.ona.rdt.widget.GoogleCovidRDTBarcodeFactory;
import io.ona.rdt.widget.OneScanCovidRDTBarcodeFactory;

import static com.vijay.jsonwizard.constants.JsonFormConstants.LABEL;
import static io.ona.rdt.util.CovidConstants.Table.COVID_PATIENTS;
import static io.ona.rdt.util.CovidConstants.Widget.GOOGLE_COVID_BARCODE_READER;
import static io.ona.rdt.util.CovidConstants.Widget.ONE_SCAN_COVID_BARCODE_READER;

/**
 * Created by Vincent Karuri on 19/06/2020
 */
public class CovidLoginActivity extends LoginActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerWidgets();
    }

    private void registerWidgets() {
        Map<String, FormWidgetFactory> widgetFactoryMap = RDTJsonFormInteractor.getInstance().map;
        widgetFactoryMap.put(GOOGLE_COVID_BARCODE_READER, new GoogleCovidRDTBarcodeFactory());
        widgetFactoryMap.put(ONE_SCAN_COVID_BARCODE_READER, new OneScanCovidRDTBarcodeFactory());
        widgetFactoryMap.put(LABEL, new CovidRDTLabelFactory());
        widgetFactoryMap.put(JsonFormConstants.DATE_PICKER, new CovidDatePickerFactory());
    }

    @Override
    protected Class getLoginActivityClass() {
        return CovidPatientRegisterActivity.class;
    }

    @Override
    protected String getRegisterTableName() {
        return COVID_PATIENTS;
    }
}
