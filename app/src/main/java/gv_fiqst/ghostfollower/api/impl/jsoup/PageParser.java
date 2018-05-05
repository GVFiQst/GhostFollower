package gv_fiqst.ghostfollower.api.impl.jsoup;


import org.jsoup.nodes.Element;

public interface PageParser<PageModel> {
    PageModel parse(Element element, Parsers parsers);
}
