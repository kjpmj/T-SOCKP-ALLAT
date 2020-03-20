package com.tnc.app.netty.server;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tnc.app.business.DefineCode;
import com.tnc.app.netty.util.ConfigManager;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ServerBootstrapper {
	Logger logger = LoggerFactory.getLogger(ServerBootstrapper.class);
	
	public void init() {
		ConfigManager configManager = ConfigManager.getInstance();
		int port = Integer.parseInt(configManager.getValue(DefineCode.LISTEN_PORT));
		String listenType = configManager.getValue(DefineCode.LISTEN_TYPE);
		int bossGroupThreadNo = Integer.parseInt(configManager.getValue(DefineCode.BOSS_GROUP_THREAD_NO));
		int workerGroupThreadNo = Integer.parseInt(configManager.getValue(DefineCode.WORKER_GROUP_THREAD_NO));
		int connectTimeoutMills = Integer.parseInt(configManager.getValue(DefineCode.CONNECT_TIMEOUT_MILLIS));
		
		EventLoopGroup bossGroup = new NioEventLoopGroup(bossGroupThreadNo);
		EventLoopGroup workerGroup = new NioEventLoopGroup(workerGroupThreadNo);
		
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ServerInitializer(listenType));
			
			serverBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMills);
			
			ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(port)).sync();
			future.channel().closeFuture().sync();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				bossGroup.shutdownGracefully().sync();
				workerGroup.shutdownGracefully().sync();
				logger.info("EventLoopGroup shutdownGracefully");
			} catch (InterruptedException e) {
				logger.info("EventLoopGroup shutdown Error");
				e.printStackTrace();
			}
		}
	}
}