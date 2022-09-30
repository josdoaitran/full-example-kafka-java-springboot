package com.testing4everyone.kafka.user.service.config;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Runner for Cucumber acceptance tests.
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = {"classpath:features/"},
    glue = {
        "com.testing4everyone.kafka.user.service.config",
        "com.testing4everyone.kafka.user.service.stepdefs"},
    tags = "not @Pending")
public class AcceptanceTestsCucumberConfig {
}

