package cn.cyanbukkit.rejoin

import cn.cyanbukkit.rejoin.command.RejoinCommand
import net.md_5.bungee.api.event.ServerConnectedEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import net.md_5.bungee.event.EventHandler

class Rejoin : Plugin() , Listener {

    companion object {
        lateinit var instance: Rejoin
    }

    lateinit var config: Configuration
    lateinit var player: Configuration
    private val configFile = dataFolder.resolve("config.yml")
    private val playerData = dataFolder.resolve("player.yml")

    override fun onEnable() {
        instance = this
        config = ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(configFile)
        player = ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(playerData)
        proxy.pluginManager.registerListener(this, this)
        proxy.pluginManager.registerCommand(this, RejoinCommand())
        logger.info("BungeeRejoin Loaded")
    }

    override fun onDisable() {
        player.keys.forEach {
            player.set(it, null)
        }
        savePlayerData()
    }

    fun savePlayerData() {
        ConfigurationProvider.getProvider(YamlConfiguration::class.java).save(player, playerData)
    }



    @EventHandler
    fun on(e: ServerConnectedEvent) {
        if (config.getStringList("gameList").contains(e.player.server.info.name)) {
            player.set("${e.player.uniqueId}", e.player.server.info.name)
            savePlayerData()
            instance.proxy.scheduler.schedule(instance, {
                player.set("${e.player.uniqueId}", null)
                savePlayerData()
            }, config.getLong("timeRange"), java.util.concurrent.TimeUnit.SECONDS)
        }
    }

}