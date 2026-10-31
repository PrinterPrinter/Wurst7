/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.mixin.nopitchlimit;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.world.entity.Entity;
import net.wurstclient.WurstClient;
import net.wurstclient.hack.HackList;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	/**
	 * Removes the pitch clamp from turn() and absSnapRotationTo() for the
	 * local player while NoPitchLimit is enabled.
	 */
	@WrapOperation(method = {"turn", "absSnapRotationTo"},
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/util/Mth;clamp(FFF)F"))
	private float wrapClampPitch(float value, float min, float max,
		Operation<Float> original)
	{
		if(!isPitchUnlocked())
			return original.call(value, min, max);
		
		return value;
	}
	
	/**
	 * Removes the pitch clamp from setXRot() for the local player while
	 * NoPitchLimit is enabled. Without this, turn() would still get its pitch
	 * clamped when it calls setXRot().
	 */
	@WrapOperation(method = "setXRot",
		at = @At(value = "FIELD",
			target = "Lnet/minecraft/world/entity/Entity;xRot:F",
			opcode = Opcodes.PUTFIELD))
	private void wrapSetXRot(Entity instance, float clamped,
		Operation<Void> original, @Local(argsOnly = true) float xRot)
	{
		original.call(instance, isPitchUnlocked() ? xRot : clamped);
	}
	
	private boolean isPitchUnlocked()
	{
		if((Object)this != WurstClient.MC.player)
			return false;
		
		HackList hax = WurstClient.INSTANCE.getHax();
		return hax != null && hax.noPitchLimitHack.isEnabled();
	}
}
