package com.xlx.utility;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;


public class NettyTest {

	public static Channel ch;
	private String host;
	private int port;

	public NettyTest(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	public void start() {
		try {
			new Thread(new NettyRunnable()).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class NettyRunnable implements Runnable {

		public void run() {

			while (true) {
				System.out.println("启动线程，连接" + host + ":" + port);
				try {
					NettyTest.connect(host, port);
				} catch (Exception e) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	public static synchronized void connect(String host, int port) throws Exception {
		if (host == null || port < 0) {
			throw new NullPointerException("socket unconnected");
		}

		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000).handler(new ClientChildChannelHandler());

			ch = b.connect(host, port).sync().channel();
			ch.closeFuture().sync();
			System.out.println("................断开连接................");

		} finally {
			group.shutdownGracefully();
		}
	}

	private static class ClientChildChannelHandler extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ch.pipeline().addLast(new StringDecoder());
			ch.pipeline().addLast(new Pipe1ClientHandler());
		}
		
		private class Pipe1ClientHandler extends ChannelHandlerAdapter {

		    @Override
		    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		        try {
		            String message = (String) msg;
		            System.out.println("Pipe1ClientHandler: " + message);
		            ctx.writeAndFlush(Unpooled.copiedBuffer(message.getBytes()));

		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }

		    @Override
		    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		        ctx.flush();
		    }

		    @Override
		    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		        cause.printStackTrace();
		        ctx.close();
		    }

		}
	}

	
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		new NettyTest("127.0.0.1", 60000).start();
	}

}
