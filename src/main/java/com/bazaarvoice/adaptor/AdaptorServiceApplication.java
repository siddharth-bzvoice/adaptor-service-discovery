package com.bazaarvoice.adaptor;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.bazaarvoice.chameleon.Chameleon;
import com.bazaarvoice.ostrich.discovery.zookeeper.ZooKeeperHostDiscovery;
import com.bazaarvoice.ostrich.pool.ServicePoolBuilder;
import com.bazaarvoice.rolodex.client.RolodexService;
import com.bazaarvoice.rolodex.client.RolodexServiceFactory;
import com.bazaarvoice.rolodex.common.CuratorConnectionStateListener;
import com.google.common.annotations.VisibleForTesting;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.interact.sqsdw.sqs.MessageHandler;
import io.interact.sqsdw.sqs.SqsListener;
import io.interact.sqsdw.sqs.SqsListenerHealthCheck;
import io.interact.sqsdw.sqs.SqsListenerImpl;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.BoundedExponentialBackoffRetry;

import java.util.HashSet;
import java.util.Set;

public class AdaptorServiceApplication extends Application<AdaptorServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new AdaptorServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "AdaptorService";
    }

    @VisibleForTesting
    ServicePoolBuilder<RolodexService> _rolodexServicePoolBuilderOverride;

    @Override
    public void initialize(final Bootstrap<AdaptorServiceConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final AdaptorServiceConfiguration configuration, final Environment environment) {

        CuratorFramework curator = null;
        ServicePoolBuilder<RolodexService> rolodexServicePoolBuilder = _rolodexServicePoolBuilderOverride;
        if (_rolodexServicePoolBuilderOverride == null) {
            // Connect to Zookeeper
            curator = CuratorFrameworkFactory.builder().
                    connectString(Chameleon.RESOURCES.ZOOKEEPER_ENSEMBLE.getValue()).
                    retryPolicy(new BoundedExponentialBackoffRetry(
                            configuration.getBoundedExponentialBackoffRetryBaseSleepTimeMs(),
                            configuration.getBoundedExponentialBackoffRetryMaxSleepTimeMs(), configuration.getMaxRetries())).
                    build();
            curator.getConnectionStateListenable().addListener(new CuratorConnectionStateListener(environment.metrics()));
            curator.start();

            // Connect to Rolodex
            rolodexServicePoolBuilder = ServicePoolBuilder.create(RolodexService.class).
                    withHostDiscovery(new ZooKeeperHostDiscovery(curator, configuration.getRolodexService().getOstrichServiceName(), environment.metrics())).
                    withServiceFactory(new RolodexServiceFactory(configuration.getRolodexService().withReadTimeout(configuration.getRolodexReadTimeoutMs()))).
                    withMetricRegistry(environment.metrics());  // Increase Rolodex client timeout since large purge jobs take a while
        }

        final FeedController controller = new FeedController(rolodexServicePoolBuilder, configuration);

        String sqsRegion = "us-east-1";
        final MessageHandler handler = new MessageHandlerImpl("INTERACTION");

        final Set<MessageHandler> handlers = new HashSet<>();
        handlers.add(handler);

        BasicAWSCredentials awsCreds = new BasicAWSCredentials("****", "****");

        AmazonSQS sqs =  AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(sqsRegion)
                .build();

        String queueUrl = "https://sqs.us-east-1.amazonaws.com/984853399247/adaptor-sqs";
        final SqsListener sqsListener = new SqsListenerImpl(sqs, queueUrl, handlers);
        environment.lifecycle().manage(sqsListener);
        environment.healthChecks().register("SqsListener", new SqsListenerHealthCheck(sqsListener));


    }
}
