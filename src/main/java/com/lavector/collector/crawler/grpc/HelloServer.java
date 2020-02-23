package com.lavector.collector.crawler.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

/**
 * Created on 24/07/2018.
 *
 * @author zeng.zhao
 */
public class HelloServer {

    private Server server;

    private int port = 50051;

//    private static class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
//        @Override
//        public void sayHello(Grpc.HelloRequest request, StreamObserver<Grpc.HelloReply> responseObserver) {
//            Grpc.HelloReply reply = Grpc.HelloReply.newBuilder()
//                    .setMessage("hello world")
//                    .build();
//            System.out.println("request : " + request.getName());
//            responseObserver.onNext(reply);
//            responseObserver.onCompleted();
//        }
//    }
//
//    private void start() throws IOException {
//        server = ServerBuilder.forPort(port)
//                .addService(new HelloServiceImpl())
//                .build()
//                .start();
//        Runtime.getRuntime()
//                .addShutdownHook(new Thread (HelloServer.this::stop));
//    }

    private void stop() {
        if (server != null) {
            try {
                server.awaitTermination();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void blockUntilShutdown() {
        if (server != null) {
            try {
                server.awaitTermination();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        HelloServer server = new HelloServer();
//        server.start();
        server.blockUntilShutdown();
    }
}
