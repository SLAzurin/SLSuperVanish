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
import io.github.slazurin.slloginannouncer.utils.RainbowText;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BroadcastLogin extends SubCommand {

    public BroadcastLogin(SuperVanish plugin) {
        super(plugin);
    }

    @Override
    public void execute(Command cmd, CommandSender p, String[] args, String label) {
        if (canDo(p, CommandAction.BROADCAST_LOGIN, true)) {
            SLLoginAnnouncer la = (SLLoginAnnouncer) this.plugin.getServer().getPluginManager().getPlugin("SLLoginAnnouncer");
            String loginMessage = "";
            boolean broadcastNotes = false;
            if (la != null) {
                loginMessage = new RainbowText(la.getApi().getRandomLoginMessage().replaceAll("<NAME>", p.getName())).getRainbowText();
            }
            
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
//                plugin.sendMessage(onlinePlayer, "ReappearMessage", p, onlinePlayer);
                if (la != null) {
                    onlinePlayer.sendMessage(loginMessage);
                    broadcastNotes = true;
                } else {
                    plugin.sendMessage(onlinePlayer, "ReappearMessage", p, onlinePlayer);
                }
            }
            if (broadcastNotes && la != null) {
                la.getApi().broadcastLoginNotes();
            }
        }
    }
}
