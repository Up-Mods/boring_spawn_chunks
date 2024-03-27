package io.github.ennuil.boring_spawn_chunks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.ennuil.boring_spawn_chunks.game_rules.ModGameRules;
import net.minecraft.network.packet.Packet;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Holder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
	@Unique
	private int spawnChunkRadius;

	private ServerWorldMixin(MutableWorldProperties worldProperties, RegistryKey<World> registryKey, DynamicRegistryManager registryManager, Holder<DimensionType> dimension, Supplier<Profiler> profiler, boolean client, boolean debug, long seed, int maxChainedNeighborUpdates) {
		super(worldProperties, registryKey, registryManager, dimension, profiler, client, debug, seed, maxChainedNeighborUpdates);
	}

	@WrapOperation(
		method = "setSpawnPos",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/MutableWorldProperties;setSpawnPos(Lnet/minecraft/util/math/BlockPos;F)V"
		)
	)
	private void wrapSetSpawnPos(MutableWorldProperties instance, BlockPos pos, float angle, Operation<Void> original) {
		if ((pos.getX() != this.properties.getSpawnX()
			&& pos.getZ() != this.properties.getSpawnZ())
			|| angle != this.properties.getSpawnAngle()
		) {
			original.call(instance, pos, angle);
		}
	}

	@WrapOperation(
		method = "setSpawnPos",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/world/ServerChunkManager;removeTicket(Lnet/minecraft/server/world/ChunkTicketType;Lnet/minecraft/util/math/ChunkPos;ILjava/lang/Object;)V"
		)
	)
	private <T> void wrapRemoveTicket(ServerChunkManager instance, ChunkTicketType<T> ticketType, ChunkPos pos, int radius, T argument, Operation<Void> original) {
		if (this.spawnChunkRadius > 1) {
			instance.removeTicket(ticketType, pos, this.spawnChunkRadius, argument);
		}
	}

	@WrapOperation(
		method = "setSpawnPos",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/world/ServerChunkManager;addTicket(Lnet/minecraft/server/world/ChunkTicketType;Lnet/minecraft/util/math/ChunkPos;ILjava/lang/Object;)V"
		)
	)
	private <T> void wrapAddTicket(ServerChunkManager instance, ChunkTicketType<T> ticketType, ChunkPos pos, int radius, T argument, Operation<Void> original) {
		int spawnChunkRadius = this.getGameRules().getInt(ModGameRules.SPAWN_CHUNK_RADIUS) + 1;
		if (spawnChunkRadius > 1) {
			instance.addTicket(ticketType, pos, spawnChunkRadius, argument);
		}
		this.spawnChunkRadius = spawnChunkRadius;
	}

	@WrapOperation(
		method = "setSpawnPos",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/PlayerManager;sendToAll(Lnet/minecraft/network/packet/Packet;)V"
		)
	)
	private void wrapSendToAll(PlayerManager instance, Packet<?> packet, Operation<Void> original, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) float angle) {
		if ((pos.getX() != this.properties.getSpawnX()
			&& pos.getZ() != this.properties.getSpawnZ())
			|| angle != this.properties.getSpawnAngle()
		) {
			original.call(instance, packet);
		}
	}
}
