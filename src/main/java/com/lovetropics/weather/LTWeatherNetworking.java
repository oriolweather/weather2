package com.lovetropics.weather;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class LTWeatherNetworking {

    private static final String PROTOCOL_VERSION = Integer.toString(4);
    private static short lastID = 0;
    public static final ResourceLocation NETWORK_CHANNEL_ID_MAIN = new ResourceLocation(LTWeather.MODID, "main");

    public static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder
            .named(NETWORK_CHANNEL_ID_MAIN)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void register() {
        registerMessage(UpdateWeatherPacket.class, UpdateWeatherPacket::encode, UpdateWeatherPacket::decode, UpdateWeatherPacket::handle, NetworkDirection.PLAY_TO_CLIENT);
    }

    private static <MSG> void registerMessage(Class<MSG> messageType, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer, NetworkDirection networkDirection) {
        HANDLER.registerMessage(lastID, messageType, encoder, decoder, messageConsumer, Optional.ofNullable(networkDirection));
        lastID++;
        if (lastID > 0xFF)
            throw new RuntimeException("Too many messages!");
    }

}

