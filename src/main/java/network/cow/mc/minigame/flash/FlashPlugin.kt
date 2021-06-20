/*
    Flash mini game spigot plugin
    Copyright (C) 2021  Yannic Rieger

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package network.cow.mc.minigame.flash

import network.cow.mc.minigame.flash.listener.CancelListener
import network.cow.mc.minigame.flash.listener.PlayerListener
import network.cow.messages.adventure.gradient
import network.cow.messages.core.Gradients
import network.cow.messages.spigot.MessagesPlugin
import network.cow.minigame.noma.spigot.NomaGamePlugin
import network.cow.minigame.noma.spigot.SpigotGame
import org.bukkit.Bukkit

class FlashPlugin : NomaGamePlugin() {
    override fun onEnable() {
        super.onEnable()
        MessagesPlugin.PREFIX = "FLASH".gradient(Gradients.MINIGAME)
        Bukkit.getPluginManager().registerEvents(PlayerListener(this.game as SpigotGame, this), this)
        Bukkit.getPluginManager().registerEvents(CancelListener(), this)
    }
}