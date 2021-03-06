package chanceCubes.rewards.biodomeGen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chanceCubes.rewards.giantRewards.BioDomeReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BasicTreesBiome implements IBioDomeBiome
{
	private Random rand = new Random();

	@Override
	public Block getFloorBlock()
	{
		return Blocks.GRASS;
	}

	@Override
	public void getRandomGenBlock(float dist, Random rand, int x, int y, int z, List<OffsetBlock> blocks, int delay)
	{
		if(y != 0)
			return;
		if(dist < 0 && rand.nextInt(5) == 0)
		{
			OffsetBlock osb = new OffsetBlock(x, y + 1, z, Blocks.TALLGRASS, false, (delay / BioDomeReward.delayShorten));
			osb.setBlockState(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS));
			blocks.add(osb);
		}
		else if(dist < -5 && rand.nextInt(100) == 0)
		{
			List<OffsetBlock> treeblocks = this.addTree(x, y, z, (delay / BioDomeReward.delayShorten));
			blocks.addAll(treeblocks);
		}
	}

	public List<OffsetBlock> addTree(int x, int y, int z, int delay)
	{
		List<OffsetBlock> blocks = new ArrayList<OffsetBlock>();

		for(int yy = 1; yy < 6; yy++)
		{
			blocks.add(new OffsetBlock(x, y + yy, z, Blocks.LOG, false, delay));
			delay++;
		}

		for(int xx = -2; xx < 3; xx++)
		{
			for(int zz = -2; zz < 3; zz++)
			{
				for(int yy = 0; yy < 2; yy++)
				{
					if((xx != 0 || zz != 0))
					{
						blocks.add(new OffsetBlock(x + xx, y + 4 + yy, z + zz, Blocks.LEAVES, false, delay));
						delay++;
					}
				}
			}
		}

		blocks.add(new OffsetBlock(x + 1, y + 6, z, Blocks.LEAVES, false, delay));
		delay++;
		blocks.add(new OffsetBlock(x - 1, y + 6, z, Blocks.LEAVES, false, delay));
		delay++;
		blocks.add(new OffsetBlock(x, y + 6, z + 1, Blocks.LEAVES, false, delay));
		delay++;
		blocks.add(new OffsetBlock(x, y + 6, z - 1, Blocks.LEAVES, false, delay));
		delay++;
		blocks.add(new OffsetBlock(x, y + 6, z, Blocks.LEAVES, false, delay));
		delay++;

		return blocks;
	}

	@Override
	public void spawnEntities(BlockPos center, World world)
	{
		for(int i = 0; i < rand.nextInt(10) + 5; i++)
		{
			int ri = rand.nextInt(5);

			if(ri == 0)
			{
				EntityChicken chicken = new EntityChicken(world);
				chicken.setLocationAndAngles(center.getX() + (rand.nextInt(31) - 15), center.getY() + 1, center.getZ() + (rand.nextInt(31) - 15), 0, 0);
				world.spawnEntityInWorld(chicken);
			}
			else if(ri == 1)
			{
				EntityCow cow = new EntityCow(world);
				cow.setLocationAndAngles(center.getX() + (rand.nextInt(31) - 15), center.getY() + 1, center.getZ() + (rand.nextInt(31) - 15), 0, 0);
				world.spawnEntityInWorld(cow);
			}
			else if(ri == 2)
			{
				EntityHorse horse = new EntityHorse(world);
				horse.setLocationAndAngles(center.getX() + (rand.nextInt(31) - 15), center.getY() + 1, center.getZ() + (rand.nextInt(31) - 15), 0, 0);
				world.spawnEntityInWorld(horse);
			}
			else if(ri == 3)
			{
				EntityPig pig = new EntityPig(world);
				pig.setLocationAndAngles(center.getX() + (rand.nextInt(31) - 15), center.getY() + 1, center.getZ() + (rand.nextInt(31) - 15), 0, 0);
				world.spawnEntityInWorld(pig);
			}
			else if(ri == 4)
			{
				EntitySheep sheep = new EntitySheep(world);
				sheep.setLocationAndAngles(center.getX() + (rand.nextInt(31) - 15), center.getY() + 1, center.getZ() + (rand.nextInt(31) - 15), 0, 0);
				world.spawnEntityInWorld(sheep);
			}
		}
	}
}