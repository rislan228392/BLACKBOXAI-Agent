package com.example.newlamp.commands;

import com.example.newlamp.NewLampMod;
import com.example.newlamp.blocks.NewLampBlock;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

public class LampCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("nl")
            .then(Commands.argument("state", StringArgumentType.word())
                .executes(context -> {
                    String state = StringArgumentType.getString(context, "state");
                    CommandSourceStack source = context.getSource();
                    
                    if (!(source.getEntity() instanceof ServerPlayer)) {
                        source.sendFailure(Component.literal("This command can only be used by players!"));
                        return 0;
                    }
                    
                    ServerPlayer player = (ServerPlayer) source.getEntity();
                    ServerLevel world = player.getLevel();
                    BlockPos playerPos = player.blockPosition();
                    
                    boolean shouldLight;
                    if ("on".equalsIgnoreCase(state)) {
                        shouldLight = true;
                    } else if ("off".equalsIgnoreCase(state)) {
                        shouldLight = false;
                    } else {
                        source.sendFailure(Component.literal("Use 'on' or 'off'!"));
                        return 0;
                    }
                    
                    int radius = 25;
                    int lampsChanged = 0;
                    
                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            for (int z = -radius; z <= radius; z++) {
                                BlockPos checkPos = playerPos.offset(x, y, z);
                                BlockState blockState = world.getBlockState(checkPos);
                                
                                if (blockState.getBlock() == NewLampMod.NEW_LAMP.get()) {
                                    if (blockState.getValue(NewLampBlock.LIT) != shouldLight) {
                                        ((NewLampBlock) blockState.getBlock()).setLit(blockState, world, checkPos, shouldLight);
                                        lampsChanged++;
                                    }
                                }
                            }
                        }
                    }
                    
                    String action = shouldLight ? "включены" : "выключены";
                    source.sendSuccess(Component.literal("Лампы " + action + "! Изменено ламп: " + lampsChanged), true);
                    
                    return lampsChanged;
                })));
    }
}
