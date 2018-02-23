package com.skcc.servicerouter.repository;

public class HttpSession {
    private final long creationTime;
    private final int maxInactiveInterval;
    private final long lastAccessedTime;

    public HttpSession(long creationTime, int maxInactiveInterval, long lastAccessedTime) {
        this.creationTime = creationTime;
        this.maxInactiveInterval = maxInactiveInterval;
        this.lastAccessedTime = lastAccessedTime;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }
}
