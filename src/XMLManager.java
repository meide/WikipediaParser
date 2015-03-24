import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;

/**
 * Created by Karina on 19.03.2015.
 */
public class XMLManager {
    public static void load(PageProcessor processor) {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            SAXParser parser = factory.newSAXParser();
            File file = new File("file.xml");
            PageHandler pageHandler = new PageHandler(processor);
            parser.parse(file, pageHandler);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
