package com.tnc.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tnc.app.App;
import com.tnc.app.netty.server.ServerBootstrapper;
import com.tnc.app.netty.util.ConfigManager;

/**
 * T-SOCKP Starter
 *
 */
public class App {
	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(App.class);

		if (args.length < 1) {
			logger.error("Config File Name Not found");
			System.exit(0);
		}

		ConfigManager configManager = ConfigManager.getInstance();
		configManager.load(ConfigManager.CONFIG_X, args[0]);

		ServerBootstrapper serverBootstrapper = new ServerBootstrapper();
		serverBootstrapper.init();
	}
}
