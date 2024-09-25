package com.batchexample.BatchProcessing.Config;

import com.batchexample.BatchProcessing.Entity.Customer1;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer1, Customer1> {

    @Override
    public Customer1 process(Customer1 item) throws Exception {

        // logic

        return item;
    }
}
