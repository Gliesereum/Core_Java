package com.gliesereum.share.common.model.dto.karma.enumerated;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
public enum CarType {
    SEDAN,
    WAGON,
    HATCHBACK,
    LIFTBACK,
    LIMOUSINE,
    MINIVAN,
    COUPE,
    CABRIOLET,
    CROSSOVER,
    SUV;

    public String toString() {
        return name().toLowerCase();
    }
}
