package com.end.csvtodb.config;

import com.end.csvtodb.entity.RssService;
import org.springframework.batch.item.ItemProcessor;

import java.util.Date;

public class RssServiceProcessor implements ItemProcessor<RssService, RssService> {


    @Override
    public RssService process(RssService item) throws Exception {

        // return item;

        if (item.getServiceType().equals("DCB")) {

            item.setCreatedBy(System.getProperty("user.name"));
            item.setCreatedOn(new Date());
            item.setModifiedBy(System.getProperty("user.name"));
            item.setModifiedOn(new Date());

            return item;
        } else {
            return null;
        }
    }

}
