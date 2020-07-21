/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.internal;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.Set;

public class DummyManaNetwork implements IManaNetwork {

	public static final DummyManaNetwork instance = new DummyManaNetwork();

	@Override
	public void clear() {}

	@Override
	public BlockEntity getClosestPool(BlockPos pos, World world, int limit) {
		return null;
	}

	@Override
	public BlockEntity getClosestCollector(BlockPos pos, World world, int limit) {
		return null;
	}

	@Override
	public Set<BlockEntity> getAllCollectorsInWorld(World world) {
		return Collections.emptySet();
	}

	@Override
	public Set<BlockEntity> getAllPoolsInWorld(World world) {
		return Collections.emptySet();
	}

}
