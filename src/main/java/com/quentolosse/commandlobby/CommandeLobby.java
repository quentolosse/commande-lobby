package com.quentolosse.commandlobby;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.quentolosse.commandlobby.commands.Lobby;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class CommandeLobby extends Plugin{

    private Configuration configuration;
    public Set<String> onlineLobbys = new HashSet<String>();

    @Override
    public void onEnable() {
        
        try {
            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Lobby commande = new Lobby(configuration, this);

        getProxy().getScheduler().schedule(this, new Runnable() {
            @Override
            public void run() {

                Map<String, ServerInfo> serveurs = getProxy().getServers();
                for(final String name: serveurs.keySet()){

                    if (name.contains("hub") || name.contains("lobby")){
                        serveurs.get(name).ping(new Callback<ServerPing>() {
                            
                            @Override
                            public void done(ServerPing result, Throwable error) {
                                
                                if (error == null && result != null) {
                                    onlineLobbys.add(name);
                                }
        
                            }
                        });
        
                    }
        
                }
            }
        }, 1, this.configuration.getInt("délai_update_lobby") , TimeUnit.SECONDS);

        getProxy().getPluginManager().registerCommand(this, commande);

    }


}
