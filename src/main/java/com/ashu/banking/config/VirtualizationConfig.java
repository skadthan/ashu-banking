package com.ashu.banking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.specto.hoverfly.junit.core.HoverflyConfig;
import io.specto.hoverfly.junit.rule.HoverflyRule;

@Configuration
public class VirtualizationConfig {
	@Bean
    public HoverflyRule hoverflyRule() {
		return HoverflyRule.inCaptureOrSimulationMode("externaldevaudit_virtual_data.json"
				,HoverflyConfig.configs().proxyLocalHost(true)
				);
    }
}
