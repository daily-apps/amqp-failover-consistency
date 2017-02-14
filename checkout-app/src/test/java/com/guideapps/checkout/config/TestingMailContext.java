package com.guideapps.checkout.config;

import org.springframework.context.annotation.Import;

// If many beans need to be scanned to up context, use ComponentScan instead Import specific config
//@ComponentScan({"com.guideapps.checkout.mail", "com.guideapps.checkout.config"})
@Import(MailConfiguration.class)
public class TestingMailContext {

}
