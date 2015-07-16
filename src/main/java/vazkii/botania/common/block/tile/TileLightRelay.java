/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 15, 2015, 8:32:04 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;

public class TileLightRelay extends TileMod implements IWandBindable {

	private static final int MAX_DIST = 20;

	private static final String TAG_BIND_X = "bindX";
	private static final String TAG_BIND_Y = "bindY";
	private static final String TAG_BIND_Z = "bindZ";

	int bindX, bindY = -1, bindZ;
	int ticksElapsed = 0;

	public void playerInteract(EntityPlayer player) {
		if(player.ridingEntity != null || worldObj.isRemote || bindY == -1)
			return;
		
		EntityPlayerMover mover = new EntityPlayerMover(worldObj, xCoord, yCoord, zCoord, bindX, bindY, bindZ);
		worldObj.spawnEntityInWorld(mover);
		player.mountEntity(mover);
		worldObj.playSoundAtEntity(mover, "botania:lightRelay", 0.2F, (float) Math.random() * 0.3F + 0.7F); 
		
	}
	
	@Override
	public void updateEntity() {
		if(bindY > -1) {
			Block block = worldObj.getBlock(bindX, bindY, bindZ);
			if(block != ModBlocks.lightRelay) {
				bindY = -1;
				return;
			}

			ticksElapsed++;

			Vector3 vec = getMovementVector();

			double dist = 0.1;
			int size = (int) (vec.mag() / dist);
			int count = 10;
			int start = ticksElapsed % size;

			Vector3 vecMag = vec.copy().normalize().multiply(dist);
			Vector3 vecTip = vecMag.copy().multiply(start).add(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);

			double radPer = Math.PI / 16.0;
			float mul = 0.5F;
			float mulPer = 0.4F;
			float maxMul = 2;
			for(int i = start; i < start + count; i++) {
				mul = Math.min(maxMul, mul + mulPer);
				double rad = radPer * (i + ticksElapsed * 0.4);
				Vector3 vecRot = vecMag.copy().crossProduct(Vector3.one).multiply(mul).rotate(rad, vecMag).add(vecTip);
				Botania.proxy.wispFX(worldObj, vecRot.x, vecRot.y, vecRot.z, 0.4F, 0.4F, 1F, 0.1F, (float) -vecMag.x, (float) -vecMag.y, (float) -vecMag.z, 1F);
				vecTip.add(vecMag);
			}
		}
	}

	public Vector3 getMovementVector() {
		return new Vector3(bindX - xCoord, bindY - yCoord, bindZ - zCoord);
	}

	@Override
	public ChunkCoordinates getBinding() {
		return bindY == -1 ? null : new ChunkCoordinates(bindX, bindY, bindZ);
	}

	@Override
	public boolean canSelect(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		return true;
	}

	@Override
	public boolean bindTo(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		if(player.worldObj.getBlock(x, y, z) != ModBlocks.lightRelay || MathHelper.pointDistanceSpace(x, y, z, xCoord, yCoord, zCoord) > MAX_DIST)
			return false;

		bindX = x;
		bindY = y;
		bindZ = z;
		return true;
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		bindX = cmp.getInteger(TAG_BIND_X);
		bindY = cmp.getInteger(TAG_BIND_Y);
		bindZ = cmp.getInteger(TAG_BIND_Z);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_BIND_X, bindX);
		cmp.setInteger(TAG_BIND_Y, bindY);
		cmp.setInteger(TAG_BIND_Z, bindZ);
	}
	
	public static class EntityPlayerMover extends Entity {

		private static final String TAG_EXIT_X = "exitX";
		private static final String TAG_EXIT_Y = "exitY";
		private static final String TAG_EXIT_Z = "exitZ";
		
		public EntityPlayerMover(World world) {
			super(world);
		}
		
		public EntityPlayerMover(World world, int x, int y, int z, int exitX, int exitY, int exitZ) {
			this(world);
			setPosition(x + 0.5, y + 0.5, z + 0.5);
			setExit(exitX, exitY, exitZ);
		}

		@Override
		protected void entityInit() {
			setSize(0F, 0F);
			noClip = true;
			
			dataWatcher.addObject(20, 0);
			dataWatcher.addObject(21, 0);
			dataWatcher.addObject(22, 0);
			dataWatcher.setObjectWatched(20);
			dataWatcher.setObjectWatched(21);
			dataWatcher.setObjectWatched(22);
		}
		
		@Override
		public void onUpdate() {
			super.onUpdate();
			
			if(ticksExisted % 30 == 0)
				worldObj.playSoundAtEntity(this, "botania:lightRelay", 0.2F, (float) Math.random() * 0.3F + 0.7F); 

			if(riddenByEntity == null) {
				setDead();
				return;
			}
			
			int exitX = getExitX();
			int exitY = getExitY();
			int exitZ = getExitZ();
			
			int x = net.minecraft.util.MathHelper.floor_double(posX);
			int y = net.minecraft.util.MathHelper.floor_double(posY);
			int z = net.minecraft.util.MathHelper.floor_double(posZ);
			if(x == exitX && y == exitY && z == exitZ) {
				TileEntity tile = worldObj.getTileEntity(x, y, z);
				if(tile != null && tile instanceof TileLightRelay) {
					worldObj.playSoundAtEntity(this, "random.orb", 0.2F, (float) Math.random() * 0.3F + 0.7F);

					ChunkCoordinates bind = ((TileLightRelay) tile).getBinding();
					if(bind != null) {
						setExit(bind.posX, bind.posY, bind.posZ);
						return;
					}
				}
				
				posY += 1.5;
				setDead();
			} else {
				Vector3 thisVec = Vector3.fromEntity(this);
				Vector3 motVec = thisVec.negate().add(exitX + 0.5, exitY + 0.5, exitZ + 0.5).normalize().multiply(0.5);
				
				float partMod = 0F;
				Color color;
				
				int count = 4;
				for(int i = 0; i < count; i++) {
					color = Color.getHSBColor((float) (ticksExisted / 36F) + (1F / count * i), 1F, 1F);
					double rad = Math.PI * 2.0 / count * i + ((double) ticksExisted / Math.PI);
					double cos = Math.cos(rad);
					double sin = Math.sin(rad);
					double s = 0.4;
					
					Botania.proxy.sparkleFX(worldObj, posX + cos * s, posY - 0.5, posZ + sin * s, (float) color.getRed() / 255F, (float) color.getGreen() / 255F, (float) color.getBlue() / 255F, 1.2F, 10);
				}

 				posX += motVec.x;
				posY += motVec.y;
				posZ += motVec.z;
			}
		}
		
		@Override
		public boolean shouldRiderSit() {
			return false;
		}
		
		@Override
		public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
			return false;
		}

		@Override
		protected void readEntityFromNBT(NBTTagCompound cmp) {
			setExit(cmp.getInteger(TAG_EXIT_X), cmp.getInteger(TAG_EXIT_Y), cmp.getInteger(TAG_EXIT_Z));
		}

		@Override
		protected void writeEntityToNBT(NBTTagCompound cmp) {
			cmp.setInteger(TAG_EXIT_X, getExitX());
			cmp.setInteger(TAG_EXIT_Y, getExitY());
			cmp.setInteger(TAG_EXIT_Z, getExitZ());
		}
		
		public int getExitX() {
			return dataWatcher.getWatchableObjectInt(20);
		}
		
		public int getExitY() {
			return dataWatcher.getWatchableObjectInt(21);
		}
		
		public int getExitZ() {
			return dataWatcher.getWatchableObjectInt(22);
		}
		
		public void setExit(int x, int y, int z) {
			dataWatcher.updateObject(20, x);
			dataWatcher.updateObject(21, y);
			dataWatcher.updateObject(22, z);
		}
		
	}

}
