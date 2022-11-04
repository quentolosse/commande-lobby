package com.quentolosse.commandlobby.commands;

import java.util.HashMap;
import java.util.Map;


import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class Lobby extends Command{

    int minPlayers;

    public Lobby(Configuration config){

        super("Lobby", "", "hub");
        this.minPlayers = config.getInt("min_player");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length == 0) {
            if (sender instanceof ProxiedPlayer) {
            
                ProxiedPlayer player = (ProxiedPlayer)sender;
                if (player.getServer().getInfo().getName().contains("hub") || player.getServer().getInfo().getName().contains("lobby")) {
                    player.sendMessage(new ComponentBuilder("Vous êtes déjà dans un lobby").create());
                    return;
                }

                String ret = sendPlayerToLobby(player);
                if (ret == "-1") sender.sendMessage(new ComponentBuilder("Aucun lobby disponible").create());
                else sender.sendMessage(new ComponentBuilder("Vous avez été téléporté au lobby").create());

            }
        }
        else {

            if (sender.hasPermission("lobby.other")) {
                
                ProxiedPlayer targetPlayer;
                try {
                    targetPlayer = ProxyServer.getInstance().getPlayer(args[0]);
                } catch (Exception e) {
                    sender.sendMessage(new ComponentBuilder("Ce joueur n'existe pas ou n'est pas en ligne").create());
                    return;
                }

                if (targetPlayer.getServer().getInfo().getName().contains("hub") || targetPlayer.getServer().getInfo().getName().contains("lobby")) {
                    sender.sendMessage(new ComponentBuilder("Ce joueur est déjà dans un lobby").create());
                    return;
                }
                    
                String ret = sendPlayerToLobby(targetPlayer);
                if (ret == "-1") sender.sendMessage(new ComponentBuilder("Aucun lobby disponible").create());
                else {
                    sender.sendMessage(new ComponentBuilder("Vous avez téléporté " + args[0] + " au lobby").create());
                    targetPlayer.sendMessage(new ComponentBuilder("Vous avez été téléporté au lobby").create());
                }
            }
            else {

                sender.sendMessage(new ComponentBuilder("Vous n'avez pas la permission de téléporter un autre joueur au lobby, essayez /lobby pour vous téléporter au lobby").create());

            }

        }
        
    }
    

    private String sendPlayerToLobby(ProxiedPlayer player){

        Map<String, ServerInfo> serveurs = ProxyServer.getInstance().getServers();
        final Map<String, Integer>[] serveursEtJoueurs = (Map<String, Integer>[]) new Map[1];
        serveursEtJoueurs[0] = new HashMap<>();
        int nbLobbys = 0;
        final int[]lobbysFinis = {0};
        for(final String name: serveurs.keySet()){

            if (name.contains("hub") || name.contains("lobby")){
                nbLobbys += 1;
                serveurs.get(name).ping(new Callback<ServerPing>() {
         
                    @Override
                    public void done(ServerPing result, Throwable error) {
                        
                        if (result != null) {
                            serveursEtJoueurs[0].put(name, result.getPlayers().getOnline());
                        }
                        lobbysFinis[0] += 1;

                    }
                });

            }

        }
        
        while (nbLobbys != lobbysFinis[0]) {
            int a;
            a = 1;
            a -= 1;
        }

        if (serveursEtJoueurs[0].isEmpty()) return "-1";

        int minAuDessus = 1000 * 1000 * 1000, maxEnDessous = -1;
        String meilleurAuDessus = "", meilleurEnDessous = "";

        for(final String name: serveursEtJoueurs[0].keySet()){

            int nbJoueurs = serveursEtJoueurs[0].get(name);
            if (nbJoueurs >= this.minPlayers) {
                if (minAuDessus > nbJoueurs) {
                    minAuDessus = nbJoueurs;
                    meilleurAuDessus = name;
                }
            }
            else{
                if (maxEnDessous < nbJoueurs) {
                    maxEnDessous = nbJoueurs;
                    meilleurEnDessous = name;
                }
            }

        }

        String targetServer = "";

        if (meilleurAuDessus != "") targetServer = meilleurAuDessus; 
        else targetServer = meilleurEnDessous;


        player.connect(ProxyServer.getInstance().getServerInfo(targetServer));

        return targetServer;

    }

}
