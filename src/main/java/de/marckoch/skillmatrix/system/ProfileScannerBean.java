package de.marckoch.skillmatrix.system;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@AllArgsConstructor
@Component
class ProfileScannerBean {

    private final Environment environment;

    private final Logger logger = LoggerFactory.getLogger(ProfileScannerBean.class);

    @PostConstruct
    void postConstruct(){
        String[] activeProfiles = environment.getActiveProfiles();
        logger.info("active profiles: " + Arrays.toString(activeProfiles));

        String[] defaultProfiles = environment.getDefaultProfiles();
        logger.info("default profiles: " + Arrays.toString(defaultProfiles));
    }
}

