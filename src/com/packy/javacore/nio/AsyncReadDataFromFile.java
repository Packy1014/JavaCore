package com.packy.javacore.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsyncReadDataFromFile {
    private void readViaFuture() throws IOException, ExecutionException, InterruptedException {
        Path path = Paths.get("AsyncReadDataFromFile.txt");
        AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        // If the size byteBuffer is insufficient to store all file contents, we have to read again
        Future<Integer> result = asynchronousFileChannel.read(byteBuffer, 0);
        while (!result.isDone()) ;
        System.out.println("Read byte: " + result.get());
        byteBuffer.flip();
        System.out.println("Buffer limit: " + byteBuffer.limit());
        byte[] data = new byte[byteBuffer.limit()];
        byteBuffer.get(data);
        System.out.println("Data: " + new String(data));
        byteBuffer.clear();
        asynchronousFileChannel.close();
    }

    private void readViaCompletionHandler() throws IOException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Path path = Paths.get("AsyncReadDataFromFile.txt");
        AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        asynchronousFileChannel.read(byteBuffer, 0, byteBuffer, new CompletionHandler<>() {
            @Override
            public void completed(Integer result, ByteBuffer byteBuffer) {
                System.out.println("Read byte: " + result);
                byteBuffer.flip();
                System.out.println("Buffer limit: " + byteBuffer.limit());
                byte[] data = new byte[byteBuffer.limit()];
                byteBuffer.get(data);
                System.out.println("Data: " + new String(data));
                byteBuffer.clear();
                countDownLatch.countDown();
            }

            // This method can only handle IOException
            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                countDownLatch.countDown();
            }
        });
        // Wait until read operation finished. If any exception thrown before invoking countDown(), deadlock happens
        countDownLatch.await();
        asynchronousFileChannel.close();
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        AsyncReadDataFromFile asyncReadDataFromFile = new AsyncReadDataFromFile();
        asyncReadDataFromFile.readViaFuture();
        asyncReadDataFromFile.readViaCompletionHandler();
    }
}
