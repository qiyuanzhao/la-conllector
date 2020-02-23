package com.lavector.collector.crawler.es;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystem;

/**
 * Created on 16/07/2018.
 *
 * @author zeng.zhao
 */
public class ClientUtil {

    private static class ClientHolder {

        private static Settings settings = Settings.builder()
                .put("cluster.name", "lavector-es-cluster")
                .build();

        private static TransportClient client;

        private static TransportClient getIns() {
            try {
                client =  new PreBuiltTransportClient(settings)
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            return client;
        }

    }

    public static TransportClient getClient() {
        return ClientHolder.getIns();
    }







    private static class SingleFileUtill{
        private static Object fs = null;
        static{
            fs = SingleFileUtill.getInstance();
        }

        private static Object getInstance(){
            Object fs = null;
            try {
                Integer.parseInt("qw");
                fs = new Object();
                return fs;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return fs;
        }

    }
    public static Object getFileSystem(){
        return SingleFileUtill.fs;
    }

    public static void main(String[] args) {
        Object o = getFileSystem();
        System.out.println(o);
    }
}
