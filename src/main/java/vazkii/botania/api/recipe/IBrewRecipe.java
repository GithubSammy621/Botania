/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;

import javax.annotation.Nonnull;

public interface IBrewRecipe extends Recipe<Inventory> {
	Identifier TYPE_ID = new Identifier(BotaniaAPI.MODID, "brew");

	Brew getBrew();

	int getManaUsage();

	ItemStack getOutput(ItemStack container);

	@Nonnull
	@Override
	default RecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getOrEmpty(TYPE_ID).get();
	}

	@Nonnull
	@Override
	default ItemStack getOutput() {
		return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	default ItemStack craft(@Nonnull Inventory inv) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean fits(int width, int height) {
		return false;
	}
}
