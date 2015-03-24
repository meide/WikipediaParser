import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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

    public PageHandler(PageProcessor processor){
        this.processor = processor;
        ignoreCases = new IgnoreCases();
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
                        if (link.contains("Category:Network theory") || link.contains("Category:Computer science") || link.contains("Category:World War II")) {
                            String[] categorySplit = felt[0].split(":");
                            page.addCategory(categorySplit[1]);
                            break;
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
                            page.addLink(felt[0]);
                        }
                    }
                }
            } else if (qName.equals("page")){
                processor.process(page);
                page = null;

            }
        } else {
            page = null;
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
            page.setTitle(stringBuilder.toString());
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        stringBuilder.append(ch,start, length);
    }
}
