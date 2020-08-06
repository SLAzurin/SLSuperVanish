/*
 * Copyright © 2015, Leon Mangler and the SuperVanish contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.myzelyam.supervanish.features;

import de.myzelyam.api.vanish.PlayerShowEvent;
import de.myzelyam.api.vanish.PostPlayerHideEvent;
import de.myzelyam.supervanish.SuperVanish;
import io.github.slazurin.slloginannouncer.SLLoginAnnouncer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class Broadcast extends Feature {

    public Broadcast(SuperVanish plugin) {
        super(plugin);
    }

    public static void announceSilentJoin(Player vanished, SuperVanish plugin) {
        if (plugin.getSettings().getBoolean("MessageOptions.AnnounceRealJoinQuitToAdmins", true)) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (vanished == onlinePlayer)
                    continue;
                if (plugin.canSee(onlinePlayer, vanished)) {
                    plugin.sendMessage(onlinePlayer, "SilentJoinMessageForAdmins", vanished, onlinePlayer);
                }
            }
        }
    }

    public static void announceSilentDeath(Player p, SuperVanish plugin, String deathMessage) {
        if (plugin.getSettings().getBoolean("MessageOptions.AnnounceDeathToAdmins", true)) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (p == onlinePlayer)
                    continue;
                if (plugin.canSee(onlinePlayer, p)) {
                    String message = plugin.getMessage("SilentDeathMessage")
                            .replace("%deathmsg%", deathMessage);
                    plugin.sendMessage(onlinePlayer, message, p, onlinePlayer);
                }
            }
        }
    }

    public static void announceSilentQuit(Player p, SuperVanish plugin) {
        if (plugin.getSettings().getBoolean("MessageOptions.AnnounceRealJoinQuitToAdmins", true)) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (p == onlinePlayer)
                    continue;
                if (plugin.canSee(onlinePlayer, p)) {
                    plugin.sendMessage(onlinePlayer, "SilentQuitMessageForAdmins", p, onlinePlayer);
                }
            }
        }
    }

    @Override
    public boolean isActive() {
        return plugin.getSettings().getBoolean("MessageOptions.FakeJoinQuitMessages.BroadcastFakeQuitOnVanish")
                || plugin.getSettings().getBoolean("MessageOptions.FakeJoinQuitMessages" +
                ".BroadcastFakeQuitOnReappear");
    }

    @EventHandler
    public void onVanish(PostPlayerHideEvent e) {
        final Player p = e.getPlayer();
        if (plugin.getSettings().getBoolean("MessageOptions.FakeJoinQuitMessages.BroadcastFakeQuitOnVanish")
                && !e.isSilent()) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!plugin.canSee(onlinePlayer, p)) {
                    if (!plugin.getSettings().getBoolean(
                            "MessageOptions.FakeJoinQuitMessages.SendMessageOnlyToAdmins"))
                        plugin.sendMessage(onlinePlayer, "VanishMessage", p, onlinePlayer);
                } else if (!plugin.getSettings().getBoolean(
                        "MessageOptions.FakeJoinQuitMessages.SendMessageOnlyToUsers"))
                    if (!plugin.getSettings().getBoolean(
                            "MessageOptions.FakeJoinQuitMessages.AnnounceVanishReappearToAdmins"))
                        plugin.sendMessage(onlinePlayer, "VanishMessage", p, onlinePlayer);
                    else if (onlinePlayer == p && !plugin.getSettings().getBoolean(
                            "MessageOptions.FakeJoinQuitMessages.SendMessageOnlyToAdmins"))
                        plugin.sendMessage(onlinePlayer, "VanishMessage", p, onlinePlayer);
                    else if (onlinePlayer != p)
                        plugin.sendMessage(onlinePlayer, "VanishMessageWithPermission", p, onlinePlayer);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onReappear(PlayerShowEvent e) {
        Player p = e.getPlayer();
        if (plugin.getSettings().getBoolean(
                "MessageOptions.FakeJoinQuitMessages.BroadcastFakeJoinOnReappear") && !e.isSilent()) {
            SLLoginAnnouncer la = (SLLoginAnnouncer) this.plugin.getServer().getPluginManager().getPlugin("SLLoginAnnouncer");
            String loginMessage = "";
            boolean broadcastNotes = false;
            if (la != null) {
                loginMessage = la.getApi().getRandomLoginMessage().replaceAll("<NAME>", p.getName());
            }
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!plugin.canSee(onlinePlayer, p)) {
                    if (!plugin.getSettings().getBoolean(
                            "MessageOptions.FakeJoinQuitMessages.SendMessageOnlyToAdmins")) {
//                        plugin.sendMessage(onlinePlayer, "ReappearMessage", p, onlinePlayer);
                        if (la != null) {
                            onlinePlayer.sendMessage(loginMessage);
                            broadcastNotes = true;
                        } else {
                            plugin.sendMessage(onlinePlayer, "ReappearMessage", p, onlinePlayer);
                        }
                    }
                } else if (!plugin.getSettings().getBoolean(
                        "MessageOptions.FakeJoinQuitMessages.SendMessageOnlyToUsers"))
                    if (!plugin.getSettings().getBoolean(
                            "MessageOptions.FakeJoinQuitMessages.AnnounceVanishReappearToAdmins"))
                        plugin.sendMessage(onlinePlayer, "ReappearMessageWithPermission", p, onlinePlayer);
                    else if (onlinePlayer == p && !plugin.getSettings().getBoolean(
                            "MessageOptions.FakeJoinQuitMessages.SendMessageOnlyToAdmins")) {
//                        plugin.sendMessage(onlinePlayer, "ReappearMessage", p, onlinePlayer);
                        if (la != null) {
                            onlinePlayer.sendMessage(loginMessage);
                            broadcastNotes = true;
                        } else {
                            plugin.sendMessage(onlinePlayer, "ReappearMessage", p, onlinePlayer);
                        }
                    
                    } else if (onlinePlayer != p)
                        plugin.sendMessage(onlinePlayer, "ReappearMessageWithPermission", p, onlinePlayer);
            }
            if (broadcastNotes && la != null) {
                la.getApi().broadcastLoginNotes();
            }
        }
    }
}
