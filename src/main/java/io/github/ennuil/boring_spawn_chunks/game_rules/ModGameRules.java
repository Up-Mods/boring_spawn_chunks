package io.github.ennuil.boring_spawn_chunks.game_rules;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class ModGameRules {
	public static final GameRules.Key<GameRules.IntGameRule> SPAWN_CHUNK_RADIUS = GameRuleRegistry.register("spawnChunkRadius", GameRules.Category.MISC, GameRuleFactory.createIntRule(2, 0, 32, ((server, intRule) -> {
		var serverWorld = server.getOverworld();
		serverWorld.setSpawnPos(serverWorld.getSpawnPos(), serverWorld.getSpawnAngle());
	})));
}
