package network.cow.minigame.flash.listener

import network.cow.minigame.noma.spigot.SpigotGame
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