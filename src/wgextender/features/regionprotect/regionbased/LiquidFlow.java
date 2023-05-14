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

package wgextender.features.regionprotect.regionbased;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFromToEvent;
import wgextender.Config;
import wgextender.utils.WGRegionUtils;

public class LiquidFlow implements Listener {

	protected final Config config;
	public LiquidFlow(Config config) {
		this.config = config;
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onLiquidFlow(BlockFromToEvent event) {
		Block b = event.getBlock();
		switch (b.getType()) {
			case LAVA -> {
				if (config.checkLavaFlow) {
					check(b.getLocation(), event.getToBlock().getLocation(), event);
				}
			}
			case WATER -> {
				if (config.checkWaterFlow) {
					check(b.getLocation(), event.getToBlock().getLocation(), event);
				}
			}
			default -> {
				if (config.checkOtherLiquidFlow) {
					check(b.getLocation(), event.getToBlock().getLocation(), event);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onDispenserDispense(BlockDispenseEvent event) {
		Block block = event.getBlock();
		BlockData blockData = block.getState().getBlockData();
		if (blockData instanceof Directional) {
			Block nextBlock = block.getRelative(((Directional) blockData).getFacing());
			switch (event.getItem().getType()) {
				case LAVA_BUCKET -> {
					if (config.checkLavaFlow) {
						check(block.getLocation(), nextBlock.getLocation(), event);
					}
				}
				case WATER_BUCKET -> {
					if (config.checkWaterFlow) {
						check(block.getLocation(), nextBlock.getLocation(), event);
					}
				}
				default -> {
					if (config.checkOtherLiquidFlow) {
						check(block.getLocation(), nextBlock.getLocation(), event);
					}
				}
			}
		}
	}

	protected void check(Location source, Location to, Cancellable event) {
		if (!WGRegionUtils.isInTheSameRegionOrWild(source, to)) {
			event.setCancelled(true);
		}
	}

}
