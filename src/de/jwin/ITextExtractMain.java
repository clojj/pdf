package de.jwin;


import com.itextpdf.text.pdf.parser.PdfImageObject;
import de.jwin.listeners.ImageRenderListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ITextExtractMain {

    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime();

        FileInputStream stream = new FileInputStream("many-images.pdf");
		PdfITextExtractor extractor = new PdfITextExtractor(stream);

		Map<ImageRenderListener.PageAndRef, PdfImageObject> images = extractor.extractAllPages();
		for (ImageRenderListener.PageAndRef imageRef : images.keySet()) {
			Util.dropImageToFile("./images/image_" + imageRef.page + "_" + imageRef.ref, images.get(imageRef));
		}

        long endTime = System.nanoTime();
        long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
        System.out.println("Total elapsed time: " + elapsedTimeInMillis + " ms");
	}

}
