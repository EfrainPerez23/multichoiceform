package com.hypernovalabs.multichoiceform.config;

import com.hypernovalabs.multichoiceform.BuildConfig;

/**
 * Created by ldemorais on 5/23/18.
 * ldemorais@hypernovalabs.com
 * <p>
 * Main configuration class.
 * </p>
 */
public class MCFConfig {

    private static final String EXTRA_PREFIX = BuildConfig.APPLICATION_ID;

    /**
     * @deprecated Use {@link #EXTRA_TAG_KEY}
     * Key to the view ID parameter.
     */
    @Deprecated
    public static final String EXTRA_ID_KEY = EXTRA_PREFIX + ".IdKey";

    /**
     * Key to the view ID parameter.
     */
    public static final String EXTRA_TAG_KEY = EXTRA_PREFIX + ".TagKey";
}
