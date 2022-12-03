package com.quentolosse.commandlobby.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.quentolosse.commandlobby.CommandeLobby;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.config.Configuration;

public class Lobby extends Command implements TabExecutor{

    int minPlayers;
    CommandeLobby main;

    public Lobby(Configuration config, CommandeLobby main){

        super("Lobby", "", "hub");
        this.minPlayers = config.getInt("min_player");
        this.main = main;

    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length == 0) {
            if (sender instanceof ProxiedPlayer) {
            
                ProxiedPlayer player = (ProxiedPlayer)sender;
                if (player.getServer().getInfo().getName().contains("hub") || player.getServer().getInfo().getName().contains("lobby")) {
                    player.sendMessage(new ComponentBuilder("Vous êtes déjà dans un lobby").color(ChatColor.RED).create());
                    return;
                }

                String ret = sendPlayerToLobby(player);
                if (ret == "-1") sender.sendMessage(new ComponentBuilder("Aucun lobby disponible").color(ChatColor.RED).create());
                else {
                    String[] names;
                    if(ret.contains("hub")){
                        names = ret.split("hub");
                    } 
                    else{
                        names = ret.split("lobby");
                    }
                    String num = names[1];
                    sender.sendMessage(new ComponentBuilder("Vous avez été envoyé au lobby n°" + num + ".").color(ChatColor.GREEN).create());
                }

            }
            else sender.sendMessage(new ComponentBuilder("Seul un joueur peut se téléporter au lobby").color(ChatColor.RED).create());
        }
        else {

            if (sender.hasPermission("lobby.other")) {
                
                ProxiedPlayer targetPlayer;
                try {
                    targetPlayer = ProxyServer.getInstance().getPlayer(args[0]);
                } catch (Exception e) {
                    sender.sendMessage(new ComponentBuilder("Ce joueur n'existe pas ou n'est pas en ligne").color(ChatColor.RED).create());
                    return;
                }
                if (targetPlayer == null) {
                    sender.sendMessage(new ComponentBuilder("Ce joueur n'existe pas ou n'est pas en ligne").color(ChatColor.RED).create());
                    return;
                }

                if (targetPlayer.getServer().getInfo().getName().contains("hub") || targetPlayer.getServer().getInfo().getName().contains("lobby")) {
                    sender.sendMessage(new ComponentBuilder("Ce joueur est déjà dans un lobby").color(ChatColor.RED).create());
                    return;
                }
                    
                String ret = sendPlayerToLobby(targetPlayer);
                
                if (ret == "-1") sender.sendMessage(new ComponentBuilder("Aucun lobby disponible").color(ChatColor.RED).create());
                else {
                    String[] names;
                    if(ret.contains("hub")){
                        names = ret.split("hub");
                    } 
                    else{
                        names = ret.split("lobby");
                    }
                    String num = names[1];
                    sender.sendMessage(new ComponentBuilder("Vous avez envoyé " + args[0] + " au lobby n°" + num + ".").color(ChatColor.GREEN).create());
                    targetPlayer.sendMessage(new ComponentBuilder("Vous avez été envoyé au lobby n°" + num + ".").color(ChatColor.GREEN).create());
                }
            }
            else {

                sender.sendMessage(new ComponentBuilder("Vous n'avez pas la permission de téléporter un autre joueur au lobby, essayez /lobby pour vous téléporter au lobby").color(ChatColor.RED).create());

            }

        }
        
    }
    

    public String sendPlayerToLobby(ProxiedPlayer player){

        if (main.onlineLobbys.isEmpty()) return "-1";

        int minAuDessus = 1000 * 1000 * 1000, maxEnDessous = -1;
        String meilleurAuDessus = "", meilleurEnDessous = "";
        Map<String, Integer> joueursParServeur = (Map<String, Integer>)new HashMap<String, Integer>();

        for(final String name: main.onlineLobbys){

            joueursParServeur.put(name, 0);

        }

        for (final ProxiedPlayer player1 : ProxyServer.getInstance().getPlayers()) {

            Server serveur = player1.getServer();
            if (serveur != null){
                if (main.onlineLobbys.contains(serveur.getInfo().getName())) {
                    joueursParServeur.put(serveur.getInfo().getName(), joueursParServeur.get(serveur.getInfo().getName() + 1));
                }
            }

        }


        for(final String name: joueursParServeur.keySet()){

            int nbJoueurs = joueursParServeur.get(name);
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

        if (meilleurEnDessous != "") targetServer = meilleurEnDessous; 
        else targetServer = meilleurAuDessus;



        player.connect(ProxyServer.getInstance().getServerInfo(targetServer));

        return targetServer;

    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            
            Set<String> joueurs =  new HashSet<String>();;
            for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){

                if (player.getName().startsWith(args[0])) {

                    joueurs.add(player.getName());
                    
                }

            }

            String[] ret = joueurs.toArray(new String[joueurs.size()]);

            return Arrays.asList(ret);
        }
        else{

            return null;

        }
    }


}
