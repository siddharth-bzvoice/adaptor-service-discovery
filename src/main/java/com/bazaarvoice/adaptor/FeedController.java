package com.bazaarvoice.adaptor;

import com.bazaarvoice.ostrich.pool.ServicePoolBuilder;
import com.bazaarvoice.rolodex.client.RolodexService;

public class FeedController {

    public FeedController(ServicePoolBuilder<RolodexService> rolodexServicePoolBuilder, AdaptorServiceConfiguration adaptorConfiguration){
        System.out.println("This is Feed Controller");
    }
}