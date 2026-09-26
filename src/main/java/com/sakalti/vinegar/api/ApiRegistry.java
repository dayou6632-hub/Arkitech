package com.sakalti.vinegar.api;

import com.sakalti.vinegar.api.item.FoodApi;

/**
 * Central registry for the Vinegar API.
 *
 * External mods should use the API classes instead of
 * depending on Vinegar's internal implementation.
 */
public final class ApiRegistry {

    private ApiRegistry() {
    }

    /**
     * Initializes the Vinegar API.
     */
    public static void register() {
        FoodApi.init();
    }
}
