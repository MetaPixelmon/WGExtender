/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */

package WGExtender.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionsInAreaSearch {

	protected static List<String> getRegionsInPlayerSelection(WorldEditPlugin we, WorldGuardPlugin wg, Player player)
	{
		Selection psel = we.getSelection(player);
		if (psel == null)
		{
			player.sendMessage(ChatColor.BLUE+"Сначала выделите зону поиска");
			return null;
		} else
		{
			List<String> regions = new ArrayList<String>();
			ProtectedRegion fakerg = new ProtectedCuboidRegion("wgexfakerg",psel.getNativeMinimumPoint().toBlockVector(),psel.getNativeMaximumPoint().toBlockVector());
			ApplicableRegionSet ars = wg.getRegionManager(psel.getWorld()).getApplicableRegions(fakerg);
			Iterator<ProtectedRegion> it = ars.iterator();
			while (it.hasNext())
			{
				regions.add(it.next().getId());
			}
			return regions;
		}
	}
	
}
