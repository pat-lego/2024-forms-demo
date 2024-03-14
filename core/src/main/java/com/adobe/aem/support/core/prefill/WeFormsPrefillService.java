package com.adobe.aem.support.core.prefill;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.osgi.service.component.annotations.Component;

import com.adobe.forms.common.service.ContentType;
import com.adobe.forms.common.service.DataOptions;
import com.adobe.forms.common.service.DataProvider;
import com.adobe.forms.common.service.FormsException;
import com.adobe.forms.common.service.PrefillData;

@Component(service = DataProvider.class)
public class WeFormsPrefillService implements DataProvider {

    private final String data = "{ \"afData\": { \"afUnboundData\": { \"data\": { } }, \"afBoundData\": { \"data\": { \"stateOverrides\": { \"element\": [ { \"options\": { \"option\": [ {} ] } } ] }, \"Lead\": { \"FirstName\": \"Patrique\", \"LastName\": \"Legault\", \"email\": \"plegault@adobe.com\", \"MobilePhone\": \"16139142477\" } } }, \"afSubmissionInfo\": { \"lastFocusItem\": \"guide[0].guide1[0].guideRootPanel[0].main[0]\", \"stateOverrides\": {}, \"signers\": {} } } }";

    @Override
    public String getServiceDescription() {
        return "We Forms Prefill Service";
    }

    @Override
    public String getServiceName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public PrefillData getPrefillData(DataOptions options) throws FormsException {
        InputStream in = new ByteArrayInputStream(data.getBytes());
        PrefillData data = new PrefillData(in, ContentType.JSON);
        return data;
    }

}