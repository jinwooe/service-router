package com.skcc.servicerouter.curator.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import java.util.List;

public class ZnodeCameraCacheListener implements PathChildrenCacheListener {
    private PathChildrenCache znodeCameraCache;

    public ZnodeCameraCacheListener(PathChildrenCache znodeCameraCache) {
        this.znodeCameraCache = znodeCameraCache;
    }

    /**
     * Every time when an event occurs on the children of "/camera", this method will be called
     * @param client
     * @param event
     * @throws Exception
     */
    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        System.out.println(event);
        List<ChildData> currentData = znodeCameraCache.getCurrentData();

        for(ChildData child : currentData) {
            System.out.println(child.getPath());
        }

        switch(event.getType()) {
            case CHILD_ADDED:
                break;
            case CHILD_UPDATED:
                break;
            case CHILD_REMOVED:
                break;
            default:

        }
    }
}
