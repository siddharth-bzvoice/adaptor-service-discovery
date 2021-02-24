package com.bazaarvoice.adaptor;

import com.bazaarvoice.rolodex.client.RolodexServiceConfiguration;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class AdaptorServiceConfiguration extends Configuration {
    // TODO: implement service configuration

    @JsonProperty
    @NotNull
    private RolodexServiceConfiguration rolodexService;

    @Valid
    private RolodexConfiguration rolodexConfiguration;

    @JsonProperty
    private int rolodexReadTimeoutMs;

    @JsonProperty
    private int maxRetries;

    @JsonProperty
    private int exponentialBackoffRetryBaseSleepTimeMs;

    @JsonProperty
    private int exponentialBackoffRetryMaxSleepTimeMs;

    @JsonProperty
    private int boundedExponentialBackoffRetryBaseSleepTimeMs;

    @JsonProperty
    private int boundedExponentialBackoffRetryMaxSleepTimeMs;

    @JsonProperty
    public RolodexConfiguration getRolodexConfiguration() { return rolodexConfiguration; }

    public int getRolodexReadTimeoutMs() {
        return rolodexReadTimeoutMs;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public int getExponentialBackoffRetryBaseSleepTimeMs() {
        return exponentialBackoffRetryBaseSleepTimeMs;
    }

    public int getExponentialBackoffRetryMaxSleepTimeMs() {
        return exponentialBackoffRetryMaxSleepTimeMs;
    }

    public int getBoundedExponentialBackoffRetryBaseSleepTimeMs() {
        return boundedExponentialBackoffRetryBaseSleepTimeMs;
    }

    public int getBoundedExponentialBackoffRetryMaxSleepTimeMs() {
        return boundedExponentialBackoffRetryMaxSleepTimeMs;
    }

    public RolodexServiceConfiguration getRolodexService() {
        return rolodexService;
    }
}
