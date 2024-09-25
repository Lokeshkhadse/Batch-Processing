package com.batchexample.BatchProcessing.Repository;

import com.batchexample.BatchProcessing.Entity.Customer1;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface CustomerRepository  extends JpaRepository<Customer1, Integer> {
}
