package io.github.ititus.recstat.network.message;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;

public abstract class BaseMessage<T extends IMessage> implements IMessage, IMessageHandler<T, IMessage> {
}
