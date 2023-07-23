package com.datalioness;

import dev.waterdog.waterdogpe.event.defaults.PlayerChatEvent;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.plugin.Plugin;
import dev.waterdog.waterdogpe.utils.config.Configuration;

public class StaffChat extends Plugin {

    private String staffChatFormat;
    private char staffChatPrefix;

    @Override
    public void onEnable() {
        // Saving and getting config
        saveResource("config.yml");
        loadConfig();
        Configuration cfg = getConfig();
        staffChatFormat = cfg.getString("staffchat_format");
        staffChatPrefix = cfg.getString("staffchat_prefix").charAt(0);

        // Register EventManager
        this.getProxy().getEventManager().subscribe(PlayerChatEvent.class, this::onChat);
    }

    public void onChat(PlayerChatEvent event){
        String message = event.getMessage();
        ProxiedPlayer player = event.getPlayer();
        if(message.charAt(0) == staffChatPrefix){
            if(player.hasPermission("staffchat.send")){
                event.setCancelled();
                String completeMessage = staffChatFormat.replace("{player}", player.getName()).replace("{msg}", message.substring(1)).replace("{server}", player.getDownstreamConnection().getServerInfo().getServerName());
                getLogger().info(completeMessage);
                for (ProxiedPlayer proxiedPlayer : getProxy().getPlayers().values()) {
                    if(proxiedPlayer.hasPermission("staffchat.receive")) proxiedPlayer.sendMessage(completeMessage);
                }
            }
        }
    }
}
