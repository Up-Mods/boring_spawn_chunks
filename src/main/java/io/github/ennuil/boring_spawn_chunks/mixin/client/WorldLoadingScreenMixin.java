package io.github.ennuil.boring_spawn_chunks.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.WorldGenerationProgressTracker;
import net.minecraft.client.gui.screen.WorldLoadingScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

// How to backport a neat thing without smashing Sodium's fingers
// Answer: just don't touch the messy thing! :p
@Mixin(WorldLoadingScreen.class)
public abstract class WorldLoadingScreenMixin {
	@Shadow
	@Final
	private WorldGenerationProgressTracker progressProvider;

	@ModifyArg(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/screen/WorldLoadingScreen;drawChunkMap(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/WorldGenerationProgressTracker;IIII)V"
		),
		index = 3
	)
	private int modifyMapYCoord(int original, @Local(ordinal = 3) int j) {
		return j;
	}

	@ModifyArg(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/GuiGraphics;drawCenteredShadowedText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V"
		),
		index = 3
	)
	private int modifyTextYCoord(int original, @Local(ordinal = 3) int j) {
		return j - (this.progressProvider.getSize() + 9 + 2);
	}
}
