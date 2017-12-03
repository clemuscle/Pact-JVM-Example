package ariman.pact.consumer;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JunitRuleMutipleInteractionsPactTest {

    @Rule
    public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2("PactJVMExampleProvider",this);

    @Pact(consumer="PactJVMExampleConsumerJunitRule")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json;charset=UTF-8");

        return builder
                .given("State Miku")
                .uponReceiving("Miku")
                .path("/information")
                .query("name=Miku")
                .method("GET")
                .willRespondWith()
                .headers(headers)
                .status(200)
                .body("{\n" +
                        "    \"salary\": 45000,\n" +
                        "    \"name\": \"Hatsune Miku\",\n" +
                        "    \"contact\": {\n" +
                        "        \"Email\": \"hatsune.miku@ariman.com\",\n" +
                        "        \"Phone Number\": \"9090950\"\n" +
                        "    }\n" +
                        "}")
                .given("State Nanoha")
                .uponReceiving("Nanoha")
                .path("/information")
                .query("name=Nanoha")
                .method("GET")
                .willRespondWith()
                .headers(headers)
                .status(200)
                .body("{\n" +
                        "    \"salary\": 0,\n" +
                        "    \"name\": \"Nanoha\",\n" +
                        "    \"contact\": null\n" +
                        "}")
                .toPact();
    }

    @Test
    @PactVerification()
    public void runTest() {
        ProviderHandler providerHandler = new ProviderHandler();
        providerHandler.setBackendURL(mockProvider.getUrl());
        Information information = providerHandler.getInformation();
        assertEquals(information.getName(), "Hatsune Miku");

        providerHandler.setBackendURL(mockProvider.getUrl(), "Nanoha");
        information = providerHandler.getInformation();
        assertEquals(information.getName(), "Nanoha");
    }
}
