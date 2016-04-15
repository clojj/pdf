package de.jwin;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import de.jwin.listeners.ImageRenderListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;

public class PDFParser {
    private PdfReader mReader;
    private int numPages;

    public PDFParser(String pdf)
            throws IOException {

        final FileChannel channel = new FileInputStream(pdf).getChannel();
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);
        channel.close();

        mReader = new PdfReader(byteArray);

        numPages = mReader.getNumberOfPages();
    }

    public void parse() {
        int numThreads = 8;
        int offset = (numPages / numThreads) - 1;
        int currentFirstPage = 1;
        ParserThread[] threads = new ParserThread[numThreads];
        int forEnd = numThreads - 1;
        for (int i = 0; i < forEnd; i++) {
            threads[i] = new ParserThread(mReader, currentFirstPage, currentFirstPage + offset);
            threads[i].start();
            currentFirstPage += offset + 1;
        }
        threads[forEnd] = new ParserThread(mReader, currentFirstPage, numPages);
        threads[forEnd].start();

        try {
            long before = System.currentTimeMillis();
            for (int i = 0; i < numThreads; i++)
                threads[i].join();
            System.out.println(System.currentTimeMillis() - before);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        mReader.close();
    }

    public static void main(final String args[]) throws IOException {
        new PDFParser("many-images.pdf").parse();
    }

}

// TODO BlockingQueue +
/*
    private void prepareConsumers() {

        ExecutorService producerPool = Executors.newFixedThreadPool(1);
        producerPool.submit(new Java8StreamRead(false));

        // create a pool of consumer threads to parse the lines read
        ExecutorService consumerPool = Executors.newFixedThreadPool(CONSUMER_COUNT);
        for (int i = 0; i < CONSUMER_COUNT; i++) {
            consumerPool.submit(new Java8StreamRead(true));
        }

        producerPool.shutdown();
        consumerPool.shutdown();

        while (!producerPool.isTerminated() && !consumerPool.isTerminated()) {
        }
    }
*/


class ParserThread extends Thread {
    private int mFirstPage;
    private int mLastPage;

    private PdfReaderContentParser parser;
    private final ImageRenderListener listener;

    ParserThread(PdfReader reader, int firstPage, int lastPage) {
        mFirstPage = firstPage;
        mLastPage = lastPage;
        parser = new PdfReaderContentParser(reader);
        listener = new ImageRenderListener();
    }

    @Override
    public void run() {
        try {
            for (int i = mFirstPage; i <= mLastPage; i++) {
                listener.setPage(i);
                parser.processContent(i, listener);
            }

            Map<ImageRenderListener.PageAndRef, PdfImageObject> images = listener.getPdfImageObjects();
            for (ImageRenderListener.PageAndRef imageRef : images.keySet()) {
                Util.dropImageToFile("./images/image_" + imageRef.page + "_" + imageRef.ref, images.get(imageRef));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
