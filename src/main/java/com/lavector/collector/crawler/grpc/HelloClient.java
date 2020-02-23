package com.lavector.collector.crawler.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * Created on 24/07/2018.
 *
 * @author zeng.zhao
 */
public class HelloClient {

//    private final ManagedChannel channel;
//
//    private final HelloServiceGrpc.HelloServiceBlockingStub blockingStub;
//
//    private HelloClient(String host, int port) {
//        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
//        blockingStub = HelloServiceGrpc.newBlockingStub(channel);
//    }
//
//    private void sayHello() {
//        Grpc.HelloRequest helloRequest = Grpc.HelloRequest.newBuilder().setName("1111").build();
//        Grpc.HelloReply helloReply = blockingStub.sayHello(helloRequest);
//        System.out.println(helloReply.getMessage());
//    }

//    public static void main(String[] args) {
//        new HelloClient("127.0.0.1", 50051).sayHello();
//    }
}
