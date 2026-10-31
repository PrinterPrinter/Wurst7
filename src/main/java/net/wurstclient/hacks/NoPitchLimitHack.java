/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks;

import net.minecraft.util.Mth;
import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.hack.Hack;

@SearchTags({"no pitch limit", "pitch limit", "NoPitchClamp", "no pitch clamp",
	"unlimited pitch", "UnlockPitch", "unlock pitch"})
public final class NoPitchLimitHack extends Hack
{
	public NoPitchLimitHack()
	{
		super("NoPitchLimit");
		setCategory(Category.RENDER);
	}
	
	@Override
	protected void onDisable()
	{
		// put the camera back into the vanilla range
		if(MC.player == null)
			return;
		
		MC.player.setXRot(Mth.clamp(MC.player.getXRot(), -90, 90));
		MC.player.xRotLast = Mth.clamp(MC.player.xRotLast, -90, 90);
	}
}
