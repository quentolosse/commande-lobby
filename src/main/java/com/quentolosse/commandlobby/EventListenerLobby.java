package com.quentolosse.commandlobby;

import com.quentolosse.commandlobby.commands.Lobby;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventListenerLobby implements Listener{

    Lobby commande;
    public EventListenerLobby (Lobby commande){
        this.commande = commande;
    }

    @EventHandler
    public void onLogin(final PostLoginEvent event){

        ProxiedPlayer player = event.getPlayer();
        commande.sendPlayerToLobby(player);

    }

}
