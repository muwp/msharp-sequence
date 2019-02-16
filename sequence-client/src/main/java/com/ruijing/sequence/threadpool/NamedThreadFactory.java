package com.ruijing.sequence.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * default thread factory
 *
 * @author mwup
 * @version 1.0
 * @created 2018/1/9 16:19
 **/
public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);

    private final AtomicInteger threadNumber;

    private final ThreadGroup group;

    private final String name;

    private boolean isDaemon = true;

    public NamedThreadFactory() {
        this("Default-Pool");
    }

    public NamedThreadFactory(final String name) {
        this(name, true);
    }

    public NamedThreadFactory(final String name, final boolean deamon) {
        this.threadNumber = new AtomicInteger(1);
        this.group = new ThreadGroup(name + "-" + POOL_NUMBER.getAndIncrement() + "-threadGroup");
        this.name = name;
        this.isDaemon = deamon;
    }

    @Override
    public Thread newThread(Runnable r) {
        final Thread t = new Thread(this.group, r, this.name, -3715992351445876736L);
        t.setDaemon(this.isDaemon);
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }

    /**
     * @return the group
     */
    public ThreadGroup getGroup() {
        return group;
    }
}
