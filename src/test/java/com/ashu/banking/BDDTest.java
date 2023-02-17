package com.ashu.banking;

import java.io.File;

import org.junit.AfterClass;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

import com.cucumber.listener.Reporter;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import io.specto.hoverfly.junit.core.HoverflyConfig;
import io.specto.hoverfly.junit.rule.HoverflyRule;

@ActiveProfiles("test")
@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features"
        , tags = {"@Regression"}
        , glue={"com.synechron.onlineacc.stepdef"}
		, plugin = { "pretty", "html:target/cucumber/cucumber-html-report", "json:target/cucumber/cucumber.json",
				"junit:target/cucumber/cucumber.xml",
				"com.cucumber.listener.ExtentCucumberFormatter:target/extent-report.html" })
public class BDDTest {
	@ClassRule
	public static HoverflyRule hoverflyRule = HoverflyRule.inCaptureOrSimulationMode("externalaudit_virtual_data.json",HoverflyConfig.configs().proxyLocalHost(true));

	@AfterClass
    public static void teardown() {
        Reporter.loadXMLConfig(new File("src/test/resources/extent-config.xml"));
        Reporter.setSystemInfo("user", System.getProperty("user.name"));
        Reporter.setSystemInfo("os", "Mac OSX");
        Reporter.setTestRunnerOutput("Sample test runner output message");
    }
}