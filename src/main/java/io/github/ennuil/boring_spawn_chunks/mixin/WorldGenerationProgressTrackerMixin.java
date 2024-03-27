package io.github.ennuil.boring_spawn_chunks.mixin;

import net.minecraft.client.gui.WorldGenerationProgressTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldGenerationProgressTracker.class)
public class WorldGenerationProgressTrackerMixin {
	@Mutable
	@Shadow
	@Final
	private int centerSize;

	@Mutable
	@Shadow
	@Final
	private int size;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void modifySizes(int radius, CallbackInfo ci) {
		if (radius == 0) {
			this.centerSize = 0;
			this.size = 0;
		}
	}
}
