package com.bazaarvoice.adaptor;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class RolodexConfiguration {

    @NotNull
    @JsonProperty
    private String serviceUrl;

    @NotNull
    @JsonProperty
    private String apiKey;

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

}