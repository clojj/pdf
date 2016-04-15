package de.jwin;

import com.itextpdf.text.pdf.parser.PdfImageObject;

import java.io.FileOutputStream;
import java.io.IOException;

public final class Util {

	public static void dropImageToFile(String name, PdfImageObject image) throws IOException {
/*
        String fileType = image.getFileType();
        System.out.println("fileType = " + fileType);
        long id = Thread.currentThread().getId();
        System.out.println("id = " + id);
*/

        String filename = String.format("%s.%s", name, "jpg");
        FileOutputStream os = new FileOutputStream(filename);
		os.write(image.getImageAsBytes());
		os.flush();
		os.close();
	}
}
