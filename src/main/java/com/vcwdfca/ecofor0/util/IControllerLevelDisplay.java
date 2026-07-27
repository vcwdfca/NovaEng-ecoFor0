package com.vcwdfca.ecofor0.util;

/**
 * Supplies the user-facing tier for controllers whose tier is not represented
 * by Core's finite {@code Levels} enum.
 */
public interface IControllerLevelDisplay {

    String getDisplayLevel();
}
