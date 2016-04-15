package de.jwin;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import de.jwin.listeners.ImageRenderListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class PdfITextExtractor {

	private final static Logger LOGGER = LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME);

	private ImageRenderListener listener;
	private PdfReaderContentParser parser;
	private Integer pageCount = 0;

	private void init(PdfReader reader) {
		parser = new PdfReaderContentParser(reader);
		listener = new ImageRenderListener();
		this.pageCount = reader.getNumberOfPages();
	}

	public PdfITextExtractor(InputStream pdfStream) {
		try {
			PdfReader reader = new PdfReader(pdfStream);
			init(reader);
		} catch (IOException e) {
			LOGGER.severe("Whoa whoa, palehche, exception");
		}
	}

	public Map<ImageRenderListener.PageAndRef, PdfImageObject> extractAllPages() throws IOException {
		for (int i = 1; i <= pageCount; i++) {
            listener.setPage(i);
            parser.processContent(i, listener);
        }
		return listener.getPdfImageObjects();
	}


}
