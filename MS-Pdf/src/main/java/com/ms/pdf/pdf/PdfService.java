package com.ms.pdf.pdf;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.Media;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class PdfService {
    private Playwright playwright;
    private Browser browser;

    @PostConstruct
    public void init(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    public byte[] generatePdfFromHtml(String htmlContent) {
        try (Page page = browser.newPage()) {
            page.setContent(htmlContent);
            System.out.println("Conteúdo definido, gerando bytes...");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            page.emulateMedia(new Page.EmulateMediaOptions().setMedia(Media.PRINT));


            return page.pdf(new Page.PdfOptions()
                    .setFormat("A4")
                    .setPrintBackground(true)
                    .setMargin(new com.microsoft.playwright.options.Margin()
                            .setTop("1.5cm").setBottom("1.5cm")
                            .setLeft("1cm").setRight("1cm")));
        }
    }

    @PreDestroy
    public void cleanup() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
