package de.marckoch.skillmatrix.skills;

import java.util.List;

public interface HasExperiences {
    List<Experience> getExperiences();

    default Integer getWeight() {
        return getExperiences()
                .stream()
                .map(Experience::getWeight)
                .reduce(Integer::sum)
                .orElse(0);
    }
}
