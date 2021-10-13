package de.marckoch.skillmatrix.system;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@AllArgsConstructor
@Component
class ProfileScannerBean {

    private Environment environment;

    @PostConstruct
    void postConstruct(){
        String[] activeProfiles = environment.getActiveProfiles();
        System.out.println("active profiles: " + Arrays.toString(activeProfiles));

        String[] defaultProfiles = environment.getDefaultProfiles();
        System.out.println("default profiles: " + Arrays.toString(defaultProfiles));
    }
}

