package art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol;

import art.ameliah.laby.addons.cubepanion.core.proto.S2CHelloPacket;
import art.ameliah.laby.addons.cubepanion.core.proto.S2CPacket;
import art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakeException;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.concurrent.Promise;
import net.labymod.api.util.logging.Logging;

public abstract class PacketHandler extends SimpleChannelInboundHandler<Object> {

  private static final Logging LOGGER = Logging.create(PacketHandler.class);
  private final WebSocketClientHandshaker handshaker;
  private Promise<Void> handshakeFuture;

  public PacketHandler(WebSocketClientHandshaker handshaker) {
    this.handshaker = handshaker;
  }

  public Promise<Void> getHandshakeFuture() {
    return handshakeFuture;
  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) {
    handshakeFuture = ctx.newPromise();
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    handshaker.handshake(ctx.channel());
  }

  @Override
  public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (!handshaker.isHandshakeComplete()) {
      try {
        handshaker.finishHandshake(ctx.channel(), (FullHttpResponse) msg);
        handshakeFuture.setSuccess(null);
      } catch (WebSocketClientHandshakeException e) {
        handshakeFuture.setFailure(e);
        Logging.getLogger().error("WebSocket Client failed to connect!", e);
      }
      return;
    }

    if (msg instanceof FullHttpResponse response) {
      throw new IllegalStateException(
          "Unexpected FullHttpResponse (getStatus=" + response.status() + ", content="
              + response.content().toString(io.netty.util.CharsetUtil.UTF_8) + ')');
    }

    WebSocketFrame frame = (WebSocketFrame) msg;
    if (frame instanceof BinaryWebSocketFrame binaryFrame) {
      handle(ctx, binaryFrame);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    if (!handshakeFuture.isDone()) {
      handshakeFuture.setFailure(cause);
    }
    ctx.close();
  }

  private void handle(ChannelHandlerContext ctx, BinaryWebSocketFrame frame) {
    try {
      S2CPacket packet = S2CPacket.parseFrom(frame.content().nioBuffer());
      handle(packet);
    } catch (InvalidProtocolBufferException e) {
      LOGGER.warn("[CUBESOCKET] [IN] Failed to read packet", e);
    }
  }

  private void handle(S2CPacket packet) {
    LOGGER.debug("[CUBESOCKET] [IN] "
        + packet.getPacketCase().getNumber()
        + " "
        + packet.getPacketCase().name());

    switch (packet.getPacketCase()) {
      case UPDATEPERK -> handle(packet.getUpdatePerk());
      case HELLO -> handle(packet.getHello());

      default -> LOGGER.warn("[CUBESOCKET] [IN] Unknown packet type: "
          + packet.getPacketCase().getNumber()
          + " "
          + packet.getPacketCase().name());
    }
  }

  protected abstract void handle(S2CPerkUpdatePacket packet);

  protected abstract void handle(S2CHelloPacket packet);
}
