package com.diddlebits.gpslib4j;

import java.util.Date;

/**
 * Common interface of every time/date packets.
 */
public interface ITimeDate {
    /**
     * @return The time.
     */
    public Date getTime();
}
