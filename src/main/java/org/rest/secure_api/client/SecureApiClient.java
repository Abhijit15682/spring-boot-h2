package org.rest.secure_api.client;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class SecureApiClient {

    private final RestClient restClient;

    public SecureApiClient() {
        String baseUrl = "http://localhost:8080";
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Unified method: Authenticates credentials and fetches protected data in a single call
     *
     * @param username     The H2 stored user identification
     * @param password     The raw password string
     * @param endpointPath The protected target resource path (e.g., "/api/dashboard")
     * @return The response string from the protected API
     */
    public String executeAuthenticatedCall(String username, String password, String endpointPath) {
        // Step 1: Automatically log in to retrieve the JWT token
        String jwtToken = loginAndGetToken(username, password);
        System.out.printf("Token : -> %s%n", jwtToken);
        // Step 2: Make the final protected resource invocation using the retrieved token
        return fetchProtectedData(jwtToken, endpointPath);
    }

    /**
     * Internal Helper: Authenticates with username and password to retrieve a JWT
     */
    private String loginAndGetToken(String username, String password) {
        Map<String, String> credentials = Map.of(
                "username", username,
                "password", password
        );

        try {
            @SuppressWarnings("unchecked")
            Map<String, String> response = restClient.post()
                    .uri("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(credentials)
                    .retrieve()
                    .body(Map.class);

            if (response != null && response.containsKey("token")) {
                return response.get("token");
            }
            throw new RuntimeException("Token missing from authentication response");

        } catch (Exception e) {
            throw new RuntimeException("Authentication phase failed: " + e.getMessage(), e);
        }
    }

    /**
     * Internal Helper: Makes a GET request to a protected endpoint using the Bearer JWT token
     */
    private String fetchProtectedData(String jwtToken, String endpointPath) {
        try {
            return restClient.get()
                    .uri(endpointPath)
                    .header("Authorization", "Bearer " + jwtToken)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch protected data from path [" + endpointPath + "]: " + e.getMessage(), e);
        }
    }
}
