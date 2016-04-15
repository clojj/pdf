package de.jwin.listeners;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageRenderListener implements RenderListener {

    private int page;

	private Map<PageAndRef, PdfImageObject> bufferedImages = new HashMap<PageAndRef, PdfImageObject>();

	public Map<PageAndRef, PdfImageObject> getPdfImageObjects() {
		return bufferedImages;
	}

	@Override
	public void beginTextBlock() {
	}

	@Override
	public void renderText(TextRenderInfo renderInfo) {
	}

	@Override
	public void endTextBlock() {
	}

	public void renderImage(ImageRenderInfo renderInfo) {
		try {
			PdfImageObject image = renderInfo.getImage();
			PdfName filter = (PdfName) image.get(PdfName.FILTER);

			if (PdfName.DCTDECODE.equals(filter)) {
			} else if (PdfName.JPXDECODE.equals(filter)) {
			} else if (PdfName.JBIG2DECODE.equals(filter)) {
			} else {
            }
            bufferedImages.put(new PageAndRef(page, renderInfo.getRef().getNumber()), image);
        } catch (IOException e) {
			e.printStackTrace();
		}
	}

    public void setPage(int page) {
        this.page = page;
    }

    public class PageAndRef {
        public int page;
        public int ref;

        public PageAndRef(int page, int ref) {
            this.page = page;
            this.ref = ref;
        }
    }
}
