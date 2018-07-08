package com.hunter.nettytest;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @version 1.0.0
 * @author: hunter
 * @date: 2018/7/8 20:30
 * @description:
 */
public class NettyServer {

    public static void main(String args) throws Exception {
        //处理连接
        EventLoopGroup pGroup = new NioEventLoopGroup();
        //处理通信
        EventLoopGroup cGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(pGroup,cGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)//TCP缓冲区
                .option(ChannelOption.SO_SNDBUF,32*1024)  //发送缓冲
                .option(ChannelOption.SO_RCVBUF, 32*1024) //接受缓冲
                .option(ChannelOption.SO_KEEPALIVE, true)   //保持连接
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ServerHander());
                    }
                });

        ChannelFuture channelFuture = bootstrap.bind(8765).sync();
        channelFuture.channel().closeFuture().sync();
        pGroup.shutdownGracefully();
        cGroup.shutdownGracefully();
    }
}
