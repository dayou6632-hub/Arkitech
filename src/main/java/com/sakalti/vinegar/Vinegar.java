package com.sakalti.vinegar;

import com.sakalti.vinegar.api.ApiRegistry;
import net.neoforged.fml.common.Mod;

@Mod(Vinegar.MOD_ID)
public class Vinegar {

    public static final String MOD_ID = "vinegar";

    public Vinegar() {
        // Register Vinegar APIs
        ApiRegistry.register();
    }
}
