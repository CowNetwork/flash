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
package network.cow.mc.minigame.flash.listener

import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.*
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.*
import org.bukkit.event.weather.WeatherChangeEvent

class CancelListener : Listener {
    @EventHandler
    fun on(e: WeatherChangeEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun on(e: PlayerInteractEvent) {
        if (e.action != Action.PHYSICAL) e.isCancelled = true
    }

    @EventHandler
    fun on(e: BlockPlaceEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun on(e: BlockBreakEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun on(e: EntityDamageEvent) {
        if (e.entityType != EntityType.PLAYER) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun on(e: FoodLevelChangeEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun on(e: BlockFormEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun on(e: BlockFromToEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun on(e: CreatureSpawnEvent) {
        if (e.spawnReason != CreatureSpawnEvent.SpawnReason.CUSTOM) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun on(e: InventoryClickEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun on(e: PlayerDropItemEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun on(e: PlayerPickupItemEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun on(e: PlayerInteractEntityEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun on(e: PlayerInteractAtEntityEvent) {
        e.isCancelled = true
    }
}