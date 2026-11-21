package com.blackmoss.thaumaturgesdelight.efficacy;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.*;

@EventBusSubscriber(modid = ThaumaturgesDelight.MODID)
public final class AttributeEfficacyHandler {
    private static final Map<UUID, List<Pending>> PENDING = new HashMap<>();

    public static void track(Player player, Holder<Attribute> attribute, Identifier modifier, int duration) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        PENDING.computeIfAbsent(serverPlayer.getUUID(), _ -> new ArrayList<>())
                .add(new Pending(attribute, modifier, serverPlayer.level().getGameTime() + duration));
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        List<Pending> entries = PENDING.get(player.getUUID());
        if (entries == null) {
            return;
        }
        AttributeMap attributes = player.getAttributes();
        long now = player.level().getGameTime();
        for (Iterator<Pending> iterator = entries.iterator(); iterator.hasNext(); ) {
            Pending pending = iterator.next();
            if (pending.expireTick() <= now) {
                AttributeInstance instance = attributes.getInstance(pending.attribute());
                if (instance != null) {
                    instance.removeModifier(pending.modifier());
                }
                iterator.remove();
            }
        }
        if (entries.isEmpty()) {
            PENDING.remove(player.getUUID());
        }
    }

    @SubscribeEvent
    public static void onLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        PENDING.remove(event.getEntity().getUUID());
    }

    private record Pending(Holder<Attribute> attribute, Identifier modifier, long expireTick) {
    }
}
