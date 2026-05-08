package com.ig.comments.web.scraping.component;

import com.ig.comments.web.scraping.dto.InstagramPostDataDTO;
import com.ig.comments.web.scraping.exception.ScrapingException;
import com.microsoft.playwright.*;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InstagramBrowserClient {

    private static final Logger log = LoggerFactory.getLogger(InstagramBrowserClient.class);

    private static final int INITIAL_PAGE_WAIT = 5000;
    private static final int MODAL_WAIT = 2000;
    private static final int SCROLL_WAIT = 1500;
    private static final int MAX_SCROLLS = 20;
    private static final int SCROLL_DISTANCE = 800;

    private static final String COMMENTS_SPAN_SELECTOR = "span[dir='auto']";

    private static final String COMMENTS_SCROLL_SELECTOR = "div.x5yr21d.xw2csxc.x1odjw0f.x1n2onr6";

    private static final String CLOSE_MODAL_SELECTOR = "div[role='button']:has(svg[aria-label='Fechar'])";

    private static final String COMMENT_VALIDATION_SCRIPT = """
            span => {
                const parentDiv = span.parentElement;
                if (!parentDiv || parentDiv.tagName !== 'DIV') return false;

                const prevSibling = parentDiv.previousElementSibling;

                return prevSibling && prevSibling.querySelector('time') !== null;
            }
            """;

    // script para extrair a descrição do post via page.evaluate()
    private static final String DESCRIPTION_EXTRACTION_SCRIPT = """
            () => {
                const spans = document.querySelectorAll('span[dir="auto"]');
                for (const s of spans) {
                    const parentDiv = s.parentElement;
                    if (!parentDiv || parentDiv.tagName !== 'DIV') continue;

                    // comentários têm um irmão anterior com <time> — a descrição não
                    const prevSibling = parentDiv.previousElementSibling;
                    if (prevSibling && prevSibling.querySelector('time')) continue;

                    // o avô da descrição tem um irmão anterior (div com avatar/username)
                    const grandParent = parentDiv.parentElement;
                    if (!grandParent) continue;
                    const gpPrevSib = grandParent.previousElementSibling;
                    if (!gpPrevSib || gpPrevSib.querySelector('time')) continue;

                    // dentro do container há: [div(username+time), span(descrição)]
                    const innerDiv = s.children[0];
                    if (!innerDiv) continue;
                    const descSpan = Array.from(innerDiv.children).find(
                        c => c.tagName === 'SPAN' && !c.querySelector('time')
                    );
                    if (descSpan) return descSpan.innerText.trim();
                }
                return null;
            }
            """;

    public InstagramPostDataDTO extractPostData(String url) {
        try (
            Playwright playwright = Playwright.create();
            Browser browser = createBrowser(playwright)
        ) {
            Page page = browser.newPage();
            openPost(page, url);
            closeGuestModal(page);
            loadAllComments(page);

            String description = extractDescription(page);
            List<String> comments = collectComments(page);

            if (comments.isEmpty()) {
                throw new ScrapingException(
                        "nenhum comentário encontrado para este post. o post pode ser privado, ter sido removido ou com comentários desativados.");
            }

            return new InstagramPostDataDTO(description, comments);

        } catch (ScrapingException e) {
            throw e;
        } catch (Exception e) {
            log.error("browser automation error for url '{}': {}", url, e.getMessage(), e);
            throw new ScrapingException(
                    "não foi possível acessar o post do instagram. a página pode estar indisponível ou a estrutura do site foi alterada.", e);
        }
    }

    private String extractDescription(Page page) {
        Object result = page.evaluate(DESCRIPTION_EXTRACTION_SCRIPT);
        return result != null ? result.toString() : "";
    }

    public List<String> extractComments(String url) {
        return extractPostData(url).comments();
    }

    private Browser createBrowser(Playwright playwright) {
        return playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    private void openPost(Page page, String url) {
        page.navigate(url);
        page.waitForTimeout(INITIAL_PAGE_WAIT);
    }

    private void closeGuestModal(Page page) {
        Locator closeButton = page.locator(CLOSE_MODAL_SELECTOR);

        if (closeButton.count() > 0) {
            closeButton.first().click();
            page.waitForTimeout(MODAL_WAIT);
        }
    }

    private void loadAllComments(Page page) {
        Locator scrollArea = page.locator(COMMENTS_SCROLL_SELECTOR);

        if (scrollArea.count() == 0) {
            return;
        }

        Locator area = scrollArea.first();

        int scrollCount = 0;
        int attemptsWithoutGrowth = 0;
        int maxAttemptsWithoutGrowth = 3;

        while (scrollCount < MAX_SCROLLS && attemptsWithoutGrowth < maxAttemptsWithoutGrowth) {
            int oldHeight = getScrollHeight(area);

            scrollDown(area);
            scrollCount++;

            page.waitForTimeout(SCROLL_WAIT);

            int newHeight = getScrollHeight(area);

            if (newHeight > oldHeight) {
                attemptsWithoutGrowth = 0;
            } else {
                attemptsWithoutGrowth++;
            }
        }
    }

    private void scrollDown(Locator area) {
        area.evaluate("el => el.scrollTop += " + SCROLL_DISTANCE);
    }

    private int getScrollHeight(Locator area) {
        Number value = (Number) area.evaluate("el => el.scrollHeight");
        return value.intValue();
    }

    private List<String> collectComments(Page page) {
        List<String> comments = new ArrayList<>();

        Locator spans = page.locator(COMMENTS_SPAN_SELECTOR);
        int total = spans.count();

        for (int i = 0; i < total; i++) {
            Locator current = spans.nth(i);

            if (isValidComment(current)) {
                extractText(current).ifPresent(comments::add);
            }
        }

        return comments;
    }

    private boolean isValidComment(Locator locator) {
        Boolean result = (Boolean) locator.evaluate(COMMENT_VALIDATION_SCRIPT);
        return Boolean.TRUE.equals(result);
    }

    private java.util.Optional<String> extractText(Locator locator) {
        String text = locator.innerText();

        if (text == null || text.isBlank()) {
            return java.util.Optional.empty();
        }

        return java.util.Optional.of(text.trim());
    }
}
