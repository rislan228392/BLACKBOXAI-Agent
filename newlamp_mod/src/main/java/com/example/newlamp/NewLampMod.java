package com.example.newlamp;

import com.example.newlamp.blocks.NewLampBlock;
import com.example.newlamp.commands.LampCommands;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod("newlamp")
public class NewLampMod {
    public static final String MODID = "newlamp";
    
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    
    public static final RegistryObject<Block> NEW_LAMP = BLOCKS.register("new_lamp", 
        () -> new NewLampBlock(BlockBehaviour.Properties.of(Material.GLASS)
            .strength(-1.0F, 3600000.0F)
            .lightLevel((state) -> state.getValue(NewLampBlock.LIT) ? 15 : 0)
            .noDrops()));
    
    public static final RegistryObject<Item> NEW_LAMP_ITEM = ITEMS.register("new_lamp",
        () -> new BlockItem(NEW_LAMP.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    
    public NewLampMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        LampCommands.register(event.getDispatcher());
    }
}
