package com.hoteam.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @ Author     ：丁明威
 * @ Date       ：Created in 17:32 2018/7/18
 * @ Description：HttpServer
 * @ Modified By：
 * @Version: 1.0.0
 */
public class HttpServer {
    //服务器端口
    private final int port;

    public void start() throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        System.out.println("InitChannel :" + socketChannel);
                        socketChannel.pipeline().addLast("decoder", new HttpRequestDecoder()).addLast("encoder", new
                                HttpResponseEncoder()).addLast("aggregator", new HttpObjectAggregator(512 * 1024))
                                .addLast("handler", new HttpHandler());
                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
        bootstrap.bind(port).sync();
    }

    /**
     * 构造方法
     *
     * @param port
     */
    public HttpServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
//        if (args.length!=1){
//            System.out.println("Please input server port!");
//            return;
//        }
//        int port = Integer.parseInt(args[0]);
        int port = 80;
        if (port <= 0) {
            System.out.println("Server port must lager than 1 ");
            return;
        }
        new HttpServer(port).start();
    }
}