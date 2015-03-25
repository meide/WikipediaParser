import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Karina on 24.03.2015.
 */
public class PageHandler extends DefaultHandler {
    private StringBuilder stringBuilder;
    private final PageProcessor processor;
    private Page page;
    private IgnoreCases ignoreCases;
    private CategoryCases categoryCases;

    public PageHandler(PageProcessor processor){
        this.processor = processor;
        ignoreCases = new IgnoreCases();
        categoryCases = new CategoryCases();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        stringBuilder = new StringBuilder();

        if (qName.equals("page")){
            page = new Page();

        } else if (qName.equals("redirect")){
            if (page != null){
                page.setRedirecting(true);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (page != null && !page.isRedirecting()){
            if (qName.equals("title")){
                titleAnalyze();
            } else if (qName.equals("text")){
                textAnalyze();
            } else if (qName.equals("page")){
                processor.process(page);
                page = null;

            }
        } else {
            page = null;
        }
    }

    private void textAnalyze() {
        String articleText = stringBuilder.toString();

        Pattern pattern = Pattern.compile("\\[\\[(.+?)\\]\\]");
        Matcher matcher = pattern.matcher(articleText);
        while(matcher.find()) {
            String tmpLink = matcher.group(0);
            tmpLink = tmpLink.substring(2, tmpLink.length() - 2);
            String[] felt = tmpLink.split("\\|");

            if (felt.length > 0) {
                String link = felt[0];

                //Lagre kategorien om den fyller kravet
                for(String catCase : categoryCases.getCategoryCases()) {
                    if (link.equals(catCase)) {
                        String[] categorySplit = felt[0].split(":");
                        page.addCategory(categorySplit[1]);
                        break;
                    }
                }

                boolean saveLink = true;
                //Lagrer ikke gitt link om den er av typen som vi ignorerer
                for (String iCase : ignoreCases.getCases()) {
                    if (link.contains(iCase)) {
                        saveLink = false;
                        break;
                    }
                }
                //alt gikk fint, vi lagrer!
                if (saveLink) {
                    link = link.replaceAll(",", " ");
                    page.addLink(link);
                }
            }
        }
    }

    private void titleAnalyze() {
        boolean saveTitle = true;
        for(String iCase : ignoreCases.getCases()) {
            if(stringBuilder.toString().contains(iCase)){
                saveTitle = false;
            }
        }
        if(saveTitle){
            String title = stringBuilder.toString().replaceAll(","," ");
            page.setTitle(title);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        stringBuilder.append(ch,start, length);
    }
}
