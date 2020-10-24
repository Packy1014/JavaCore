package com.packy.javacore.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ReadDataFromFile {
    private void readFile() throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("ReadDataFromFile.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(48);
        int bytesRead = fileChannel.read(byteBuffer);
        while (bytesRead != -1) {
            System.out.println("Bytes read: " + bytesRead);
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                System.out.println((char) byteBuffer.get());
            }
            byteBuffer.clear();
            bytesRead = fileChannel.read(byteBuffer);
        }
        randomAccessFile.close();
    }

    public static void main(String[] args) throws IOException {
        ReadDataFromFile readDataFromFile = new ReadDataFromFile();
        readDataFromFile.readFile();
    }
}
