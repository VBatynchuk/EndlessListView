package com.batynchuk.endlesslistview.utils;

/**
 * Created by Батинчук on 10.12.2016.
 */

public class Constants {

    private static final int DEFAULT_MAX_ELEMENTS_IN_MEMORY = 50;
    public static final int PAGE_LIMIT = DEFAULT_MAX_ELEMENTS_IN_MEMORY / 2;
    public static final int LOAD_OFFSET = 5;

    /**
     * Prevention from constructing objects of this class.
     */
    private Constants() {
        //this prevents even the native class from
        //calling this constructor as well:
        throw new AssertionError();
    }
}
