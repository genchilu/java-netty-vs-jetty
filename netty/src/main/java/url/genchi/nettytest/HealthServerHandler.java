package url.genchi.nettytest;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import java.util.UUID;


/**
 * Created by genchi on 2017/6/28.
 */
public class HealthServerHandler extends ChannelInboundHandlerAdapter {
  private static final AsciiString CONTENT_TYPE = new AsciiString("Content-Type");
  private static final AsciiString CONTENT_LENGTH = new AsciiString("Content-Length");
  private static final AsciiString CONNECTION = new AsciiString("Connection");
  private static final AsciiString KEEP_ALIVE = new AsciiString("keep-alive");

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg)  {
    if (msg instanceof FullHttpRequest) {
      FullHttpRequest req = (FullHttpRequest) msg;//客户端的请求对象


      String uri = req.uri();//获取客户端的URL
      //根据不同的请求API做不同的处理(路由分发)，只处理POST方法
      if (req.method() == HttpMethod.GET && req.uri().equals("/hello/test")) {
        for(int i = 0;i<100;i++) {
          UUID uuid = UUID.randomUUID();
        }
        ResponseJson(ctx,req,"test");

      } else {
        //错误处理
        ResponseJson(ctx,req,"404 Not Find");
      }
    }
  }

  /**
   * 响应HTTP的请求
   * @param ctx
   * @param req
   * @param jsonStr
   */
  private void ResponseJson(ChannelHandlerContext ctx, FullHttpRequest req ,String jsonStr)
  {

    boolean keepAlive = HttpUtil.isKeepAlive(req);
    byte[] jsonByteByte = jsonStr.getBytes();
    FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(jsonByteByte));
    response.headers().set(CONTENT_TYPE, "text/plain");
    response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());

    if (!keepAlive) {
      ctx.write(response).addListener(ChannelFutureListener.CLOSE);
    } else {
      response.headers().set(CONNECTION, KEEP_ALIVE);
      ctx.write(response);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }


  /**
   * 获取请求的内容
   * @param request
   * @return
   */
  private String parseJosnRequest(FullHttpRequest request) {
    ByteBuf jsonBuf = request.content();
    String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);
    return jsonStr;
  }
}
