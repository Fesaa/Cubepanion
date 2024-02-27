package art.ameliah.laby.addons.cubepanion.core.cubesocket;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;

public class CubeSocketHandler extends ChannelInitializer<SocketChannel> {

  private final CubeSocket cubeSocket;
  private final PacketHandler handler;

  private SocketChannel channel;

  public CubeSocketHandler(CubeSocket cubeSocket, PacketHandler handler) {
    this.cubeSocket = cubeSocket;
    this.handler = handler;
  }

  @Override
  protected void initChannel(SocketChannel channel) throws Exception {
    this.channel = channel;
    channel.pipeline()
        .addLast(new HttpClientCodec())
        .addLast(new HttpObjectAggregator(8192))
        .addLast(WebSocketClientCompressionHandler.INSTANCE)
        .addLast(this.handler);
  }

  public SocketChannel getChannel() {
    return this.channel;
  }
}
