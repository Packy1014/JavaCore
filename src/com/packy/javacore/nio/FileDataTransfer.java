package com.packy.javacore.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class FileDataTransfer {
    private void transferFrom() throws IOException {
        RandomAccessFile fromFile = new RandomAccessFile("ReadDataFromFile.txt", "rw");
        FileChannel fromChannel = fromFile.getChannel();
        RandomAccessFile toFile = new RandomAccessFile("FileDataTransferFile.txt", "rw");
        FileChannel toChannel = toFile.getChannel();
        toChannel.transferFrom(fromChannel, 0, fromChannel.size());
        fromChannel.close();
        toChannel.close();
    }
    public static void main(String[] args) throws IOException {
        FileDataTransfer fileDataTransfer = new FileDataTransfer();
        fileDataTransfer.transferFrom();
    }
}
