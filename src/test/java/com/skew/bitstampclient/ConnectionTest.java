package com.skew.bitstampclient;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import com.skew.bitstampclient.BitstampConnector;
import com.skew.bitstampclient.BitstampConnectorEndpoint;

import javax.websocket.DeploymentException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ConnectionTest {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ConnectionTest.class);
    
    @Test
    public void 
      givenPathAndContent_whenWritesToFileWithHandler_thenCorrect() throws IOException {
        
        String fileName = UUID.randomUUID().toString();
        Path path = Paths.get(fileName);
        System.out.println(path.toAbsolutePath().toString());
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
          path, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.DELETE_ON_CLOSE);
        
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("hello,world".getBytes());
        buffer.flip();

        fileChannel.write(
          buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                // result is number of bytes written
                // attachment is the buffer
            	
            }
            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
            	System.out.println("Test Failed");
            }
        });
    }
    
    @SuppressWarnings("resource")
	@Test
    public void connect() {

        BitstampConnectorEndpoint.onOpen(new ConnectionOpen());

        try {
        	
            BitstampConnector.connect();
            
        } catch (InterruptedException | DeploymentException | IOException e) {
            Assert.fail("Exception while connecting");
        }

//        new Scanner(System.in).nextLine();
    }
        

}
