/*
 * Copyright © 2015, Leon Mangler and the SuperVanish contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.myzelyam.supervanish.commands.subcommands;

import de.myzelyam.supervanish.SuperVanish;
import de.myzelyam.supervanish.commands.CommandAction;
import de.myzelyam.supervanish.commands.SubCommand;
import io.github.slazurin.slloginannouncer.SLLoginAnnouncer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BroadcastLogout extends SubCommand {

    public BroadcastLogout(SuperVanish plugin) {
        super(plugin);
    }

    @Override
    public void execute(Command cmd, CommandSender p, String[] args, String label) {
        if (canDo(p, CommandAction.BROADCAST_LOGOUT, true)) {
            SLLoginAnnouncer la = (SLLoginAnnouncer) this.plugin.getServer().getPluginManager().getPlugin("SLLoginAnnouncer");
            boolean broadcastNotes = false;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
//                plugin.sendMessage(onlinePlayer, "VanishMessage", p, onlinePlayer);
                if (la != null) {
                    onlinePlayer.sendMessage(la.getApi().getLogoutMessage(onlinePlayer));
                    broadcastNotes = true;
                } else {
                    plugin.sendMessage(onlinePlayer, "VanishMessage", p, onlinePlayer);
                }
            }
            if (broadcastNotes && la != null) {
                la.getApi().broadcastLogoutNotes();
            }
        }
    }
}
