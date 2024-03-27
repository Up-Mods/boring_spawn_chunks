package io.github.ennuil.boring_spawn_chunks;

import io.github.ennuil.boring_spawn_chunks.game_rules.ModGameRules;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class ModInit implements ModInitializer {
	@Override
	public void onInitialize(ModContainer mod) {
		new ModGameRules();
	}
}
