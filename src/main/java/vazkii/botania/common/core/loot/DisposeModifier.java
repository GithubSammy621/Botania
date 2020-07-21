/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.loot;

import com.google.gson.JsonObject;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.event.RegistryEvent;

import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumPick;

import javax.annotation.Nonnull;

import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class DisposeModifier extends LootModifier {
	protected DisposeModifier(LootCondition[] conditions) {
		super(conditions);
	}

	public static void register(RegistryEvent.Register<GlobalLootModifierSerializer<?>> evt) {
		evt.getRegistry().register(new Serializer().setRegistryName(prefix("dispose")));
		evt.getRegistry().register(new CallTableModifier.Serializer().setRegistryName(prefix("call_table")));
	}

	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		Entity e = context.get(LootContextParameters.THIS_ENTITY);
		ItemStack tool = context.get(LootContextParameters.TOOL);
		if (e != null && tool != null && !tool.isEmpty()) {
			ItemElementiumPick.filterDisposable(generatedLoot, e, tool);
		}
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<DisposeModifier> {
		@Override
		public DisposeModifier read(Identifier location, JsonObject object, LootCondition[] conditions) {
			return new DisposeModifier(conditions);
		}
	}
}
