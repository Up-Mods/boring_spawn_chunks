package io.github.ennuil.boring_spawn_chunks.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class ModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return RedirectionScreen::new;
	}
}
