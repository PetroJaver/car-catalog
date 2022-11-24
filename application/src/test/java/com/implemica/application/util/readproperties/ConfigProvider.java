package com.implemica.application.util.readproperties;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public interface ConfigProvider {
    Config config = readConfig();

    static Config readConfig(){
        return ConfigFactory.systemProperties()
                .hasPath("testProfile")
                ?ConfigFactory.load(ConfigFactory.systemProperties().getString("testProfile"))
                :ConfigFactory.load("application.conf");
    }

    String URL = readConfig().getString("url");
    String ADMIN_USERNAME = readConfig().getString("usersParams.admin.username");
    String ADMIN_PASSWORD = readConfig().getString("usersParams.admin.password");
}
