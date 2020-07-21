/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.gen.chunk.StructuresConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkGeneratorType.Preset.class)
public interface AccessorDimensionSettingsPreset {
	@Invoker("func_236135_a_")
	static ChunkGeneratorType createOverworldSettings(StructuresConfig structureSettings, boolean amplified, ChunkGeneratorType.Preset preset) {
		throw new IllegalStateException();
	}
}
