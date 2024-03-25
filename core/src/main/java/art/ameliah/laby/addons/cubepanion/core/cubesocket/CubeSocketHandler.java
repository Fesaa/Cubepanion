package art.ameliah.laby.addons.cubepanion.core.cubesocket;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.pipeline.PacketDecoder;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.pipeline.PacketEncoder;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.pipeline.PacketPrepender;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.pipeline.PacketSplitter;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.util.concurrent.TimeUnit;

public class CubeSocketHandler extends ChannelInitializer<NioSocketChannel> {

  private final CubeSocket cubeSocket;
  private final PacketHandler handler;

  private NioSocketChannel channel;

  public CubeSocketHandler(CubeSocket cubeSocket, PacketHandler handler) {
    this.cubeSocket = cubeSocket;
    this.handler = handler;
  }

  @Override
  protected void initChannel(NioSocketChannel channel) {
    this.channel = channel;
    channel.pipeline()
        .addLast("timeout", new ReadTimeoutHandler(30L, TimeUnit.SECONDS))
        .addLast("splitter", new PacketSplitter())
        .addLast("decoder", new PacketDecoder(this.cubeSocket))
        .addLast("prepender", new PacketPrepender())
        .addLast("encoder", new PacketEncoder(this.cubeSocket))
        .addLast(this.handler);
  }

  public NioSocketChannel getChannel() {
    return this.channel;
  }
}
