package com.skcc.servicerouter.curator.listener;

import com.skcc.servicerouter.utils.BytesUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import java.util.List;

public class ZnodeGatewayCacheListener implements PathChildrenCacheListener {
    private PathChildrenCache znodeGatewayCache;

    public ZnodeGatewayCacheListener(PathChildrenCache znodeGatewayCache) {
        this.znodeGatewayCache = znodeGatewayCache;
    }

    /**
     * Every time when an event occurs on the children of "/gateway", this method will be called
     * @param client
     * @param event
     * @throws Exception
     */
    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        System.out.println(event);
        List<ChildData> currentData = znodeGatewayCache.getCurrentData();

        for(ChildData child : currentData) {
            System.out.println("number of cameras processing: " + BytesUtil.bytesToInt(child.getData()));
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
