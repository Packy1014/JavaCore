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

public class AsyncWriteDataToFile {
    private void writeViaFuture() throws IOException, ExecutionException, InterruptedException {
        Path path = Paths.get("AsyncWriteDataToFile.txt");
        AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        byteBuffer.put("writeViaFuture".getBytes());
        byteBuffer.flip();
        Future<Integer> result = asynchronousFileChannel.write(byteBuffer, 0);
        while (!result.isDone()) ;
        System.out.println("Write byte: " + result.get());
        System.out.println("Buffer limit: " + byteBuffer.limit());
        byteBuffer.clear();
        System.out.println("writeViaFuture finished");
        asynchronousFileChannel.close();
    }

    private void writeViaCompletionHandler() throws IOException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Path path = Paths.get("AsyncWriteDataToFile.txt");
        AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        byteBuffer.put("writeViaCompletionHandler".getBytes());
        byteBuffer.flip();
        asynchronousFileChannel.write(byteBuffer, 0, byteBuffer, new CompletionHandler<>() {
            @Override
            public void completed(Integer result, ByteBuffer byteBuffer) {
                System.out.println("Write byte: " + result);
                System.out.println("Buffer limit: " + byteBuffer.limit());
                byteBuffer.clear();
                System.out.println("writeViaCompletionHandler finished");
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
        AsyncWriteDataToFile asyncReadDataFromFile = new AsyncWriteDataToFile();
        asyncReadDataFromFile.writeViaFuture();
        asyncReadDataFromFile.writeViaCompletionHandler();
    }
}
