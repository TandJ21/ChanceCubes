package chanceCubes.blocks;

import java.util.Random;

import chanceCubes.tileentities.TileCubeDispenser;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCubeDispenser extends BaseChanceBlock implements ITileEntityProvider
{
	public static final PropertyEnum<BlockCubeDispenser.DispenseType> DISPENSING = PropertyEnum.<BlockCubeDispenser.DispenseType> create("dispensing", BlockCubeDispenser.DispenseType.class);

	public BlockCubeDispenser()
	{
		super("cube_Dispenser");
		this.setHardness(2f);
		this.setDefaultState(this.blockState.getBaseState().withProperty(DISPENSING, DispenseType.CHANCE_CUBE));
		this.setLightOpacity(0);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileCubeDispenser();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(world.isRemote)
			return true;
		if(!(world.getTileEntity(pos) instanceof TileCubeDispenser))
			return true;

		TileCubeDispenser te = (TileCubeDispenser) world.getTileEntity(pos);
		if(player.isSneaking())
		{
			state = state.cycleProperty(DISPENSING);
			world.setBlockState(pos, state, 3);
		}
		else
		{
			if(player.inventory.getCurrentItem() != null)
			{
				Block block = Block.getBlockFromItem(player.inventory.getCurrentItem().getItem());
				if(block != null && block.equals(te.getCurrentBlock(BlockCubeDispenser.getCurrentState(state))))
					player.inventory.decrStackSize(player.inventory.currentItem, 1);
			}
		}
		return true;
	}

	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player)
	{
		if(world.isRemote)
			return;
		if(!(world.getTileEntity(pos) instanceof TileCubeDispenser))
			return;
		TileCubeDispenser te = (TileCubeDispenser) world.getTileEntity(pos);

		double px = player.posX;
		double py = player.posY;
		double pz = player.posZ;

		EntityItem entitem = te.getNewEntityItem(BlockCubeDispenser.getCurrentState(world.getBlockState(pos)));
		entitem.setLocationAndAngles(px, py, pz, 0, 0);
		if(player.isSneaking())
		{
			entitem.getEntityItem().stackSize = 1;
			world.spawnEntityInWorld(entitem);
		}
		else
		{
			entitem.getEntityItem().stackSize = 64;
			world.spawnEntityInWorld(entitem);
		}
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
	{
		return null;
	}

	public int getMetaFromState(IBlockState state)
	{
		DispenseType type = getCurrentState(state);
		if(type == DispenseType.CHANCE_CUBE)
			return 0;
		else if(type == DispenseType.CHANCE_ICOSAHEDRON)
			return 1;
		else if(type == DispenseType.COMPACT_GAINTCUBE)
			return 2;
		else
			return 0;
	}

	public IBlockState getStateFromMeta(int meta)
	{
		if(meta == 0)
			return this.getDefaultState().withProperty(DISPENSING, DispenseType.CHANCE_CUBE);
		else if(meta == 1)
			return this.getDefaultState().withProperty(DISPENSING, DispenseType.CHANCE_ICOSAHEDRON);
		else if(meta == 2)
			return this.getDefaultState().withProperty(DISPENSING, DispenseType.COMPACT_GAINTCUBE);
		else
			return this.getDefaultState().withProperty(DISPENSING, DispenseType.CHANCE_CUBE);
	}

	public static enum DispenseType implements IStringSerializable
	{
		CHANCE_CUBE("chance_cube"), CHANCE_ICOSAHEDRON("chance_icosahedron"), COMPACT_GAINTCUBE("compact_gaint_cube");

		private String type;

		private DispenseType(String name)
		{
			this.type = name;
		}

		@Override
		public String getName()
		{
			return this.type;
		}

		public DispenseType getNextState()
		{
			switch(this)
			{
				case CHANCE_CUBE:
					return CHANCE_ICOSAHEDRON;
				case CHANCE_ICOSAHEDRON:
					return COMPACT_GAINTCUBE;
				case COMPACT_GAINTCUBE:
					return CHANCE_CUBE;
				default:
					return CHANCE_CUBE;

			}
		}
	}

	public static DispenseType getNextState(IBlockState state)
	{
		return state.getValue(DISPENSING).getNextState();
	}

	public static DispenseType getCurrentState(IBlockState state)
	{
		return state.getValue(DISPENSING);
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { DISPENSING });
	}
}