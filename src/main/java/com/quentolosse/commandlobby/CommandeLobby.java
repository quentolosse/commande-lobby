package com.quentolosse.commandlobby;

import java.io.File;
import java.io.IOException;

import com.quentolosse.commandlobby.commands.Lobby;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class CommandeLobby extends Plugin{
    


    private Configuration configuration;

    @Override
    public void onEnable() {
        
        try {
            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Lobby(configuration));

    }

}
