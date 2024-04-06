package io.github.ennuil.boring_spawn_chunks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.ennuil.boring_spawn_chunks.game_rules.ModGameRules;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.SaveProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
	@Shadow
	public abstract GameRules getGameRules();

	@Shadow
	@Final
	protected SaveProperties saveProperties;

	@ModifyExpressionValue(method = "loadWorld", at = @At(value = "CONSTANT", args = "intValue=11"))
	private int modify11(int original) {
		int radius = this.saveProperties.getGameRules().getInt(ModGameRules.SPAWN_CHUNK_RADIUS);
        return radius > 0 ? radius + 1 : 0;
	}

	@Redirect(
		method = "prepareStartRegion",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/world/ServerChunkManager;addTicket(Lnet/minecraft/server/world/ChunkTicketType;Lnet/minecraft/util/math/ChunkPos;ILjava/lang/Object;)V"
		)
	)
	private <T> void modifyAddTicket(ServerChunkManager instance, ChunkTicketType<T> ticketType, ChunkPos pos, int radius, T argument, @Local ServerWorld serverWorld, @Local BlockPos blockPos) {
		serverWorld.setSpawnPos(blockPos, serverWorld.getSpawnAngle());
	}

	@ModifyExpressionValue(method = "prepareStartRegion", at = @At(value = "CONSTANT", args = "intValue=441"))
	private int modify441(int original) {
		int spawnChunkRadius = this.getGameRules().getInt(ModGameRules.SPAWN_CHUNK_RADIUS);
		return spawnChunkRadius > 0 ? MathHelper.square(spawnChunkRadius * 2 + 1) : 0;
	}
}
