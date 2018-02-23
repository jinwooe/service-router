package com.skcc.servicerouter;

import com.skcc.servicerouter.curator.listener.ZnodeCameraCacheListener;
import com.skcc.servicerouter.curator.listener.ZnodeGatewayCacheListener;
import com.skcc.servicerouter.repository.redis.HttpSessionRepository;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

@SpringBootApplication
public class ServiceRouterApplication {
	private static Logger logger = LoggerFactory.getLogger(ServiceRouterApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ServiceRouterApplication.class, args);
	}

	@Bean
	public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(redisConnectionFactory);

		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();

		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
		redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);

		return redisTemplate;
	}

	@Bean
	public LettuceConnectionFactory lettuceConnectionFactory() {
		return new LettuceConnectionFactory();
	}

	@Bean(destroyMethod = "close")
	public CuratorFramework curatorFramework(Environment env) {
		CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(env.getProperty("zookeeper.connectString"),
				new RetryNTimes(5, 1000));
		curatorFramework.start();

		String znodeGateway = env.getProperty("zookeeper.znode.gateway");
		String znodeCamera = env.getProperty("zookeeper.znode.camera");

		try {
			if (curatorFramework.checkExists().forPath(znodeGateway) == null) {
				curatorFramework.create().creatingParentsIfNeeded().forPath(znodeGateway);
			}

			if (curatorFramework.checkExists().forPath(znodeCamera) == null) {
				curatorFramework.create().creatingParentsIfNeeded().forPath(znodeCamera);
			}

		}
		catch(Exception e) {
			logger.warn(e.getMessage(), e);
		}


		return curatorFramework;
	}

	@Bean
	@Qualifier("gateway")
	public PathChildrenCache gatewayPathChildrenCache(CuratorFramework curatorFramework, Environment env) {
		String znodeGateway = env.getProperty("zookeeper.znode.gateway");
		PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, znodeGateway, true);
		try {
			pathChildrenCache.start();
			pathChildrenCache.getListenable().addListener(new ZnodeGatewayCacheListener(pathChildrenCache));
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			return null;
		}
		return pathChildrenCache;
	}

	@Bean
	@Qualifier("camera")
	public PathChildrenCache cameraPathChildrenCache(CuratorFramework curatorFramework, Environment env) {
		String znodeCamera = env.getProperty("zookeeper.znode.camera");
		PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, znodeCamera, true);
		try {
			pathChildrenCache.start();
			pathChildrenCache.getListenable().addListener(new ZnodeCameraCacheListener(pathChildrenCache));
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			return null;
		}
		return pathChildrenCache;
	}
}
