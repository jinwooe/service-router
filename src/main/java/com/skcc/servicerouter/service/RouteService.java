package com.skcc.servicerouter.service;

import com.skcc.servicerouter.utils.BytesUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class RouteService {
    private static Logger logger = LoggerFactory.getLogger(RouteService.class);

    @Autowired
    private CuratorFramework curatorFramework;

    @Autowired
    @Qualifier("gateway")
    private PathChildrenCache znodeGatewayCache;

    @Autowired
    private TokenService tokenService;

    @Value("${zookeeper.znode.camera}")
    private String znodeCamera;

    @Value("${gateway.tcp.port}")
    private int tcpPort;

    @Value("${gateway.http.port}")
    private int httpPort;

    @Value("${gateway.http.path}")
    private String httpPath;


    public RouteInfo getRouteInfo(long cameraId, String scheme) {
        String path = scheme.toLowerCase().equals("http") ? httpPath : null;

        try {
            if (curatorFramework.checkExists().forPath(znodeCamera + "/" + cameraId) != null) {
                byte[] ipAddr = curatorFramework.getData().forPath(znodeCamera + "/" + cameraId);
                return new RouteInfo(cameraId, scheme, new String(ipAddr), tcpPort, path, tokenService.generate());
            }
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }

        List<ChildData> children = znodeGatewayCache.getCurrentData();
        if(children.size() == 0) {
            throw new RuntimeException("No RTSP Streaming Gateway server instance registered...");
        }

        ChildData bestNodeData = findBestNode(children.toArray(new ChildData[children.size()]));

        RouteInfo routeInfo = new RouteInfo(cameraId, scheme, getIpAddress(bestNodeData.getPath()), httpPort, path, tokenService.generate());
        return routeInfo;
    }

    /**
     * Sorts the passed array by the number of cameras being processed and returns the first element of the array
     * @param childData
     * @return
     */
    private ChildData findBestNode(ChildData[] childData) {
        Objects.requireNonNull(childData);
        Arrays.sort(childData, (c1, c2) -> {
            int n1 = BytesUtil.bytesToInt(c1.getData());
            int n2 = BytesUtil.bytesToInt(c2.getData());
            return n1 > n2 ? 1 : -1;
        });

        return childData[0];
    }

    /**
     * e.g) /gateway/127.0.0.1 -> 127.0.0.1
     * @param path
     * @return
     */
    private String getIpAddress(String path) {
        Objects.requireNonNull(path);
        String s = path.substring(path.lastIndexOf('/') + 1);
        return s;
    }
}
