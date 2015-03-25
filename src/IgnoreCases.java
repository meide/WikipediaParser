import java.util.ArrayList;

/**
 * Created by Karina on 22.03.2015.
 */
public class IgnoreCases {

    private ArrayList<String> cases;

    public IgnoreCases(){
        cases = new ArrayList<String>();
        cases.add("Wikipedia:");
        cases.add("User:");
        cases.add("File:");
        cases.add("MediaWiki:");
        cases.add("Template:");
        cases.add("Help:");
        cases.add("Category:");
        cases.add("Portal:");
        cases.add("Book:");
        cases.add("Draft:");
        cases.add("Education Program:");
        cases.add("TimedText:");
        cases.add("Module:");
        cases.add("Topic:");
        cases.add("Special:");
        cases.add("Media:");
        cases.add("Wikipedia talk:");
        cases.add("User talk:");
        cases.add("File talk:");
        cases.add("MediaWiki talk:");
        cases.add("Template talk:");
        cases.add("Help talk:");
        cases.add("Category talk:");
        cases.add("Portal talk:");
        cases.add("Book talk:");
        cases.add("Draft talk:");
        cases.add("Education Program talk:");
        cases.add("TimedText talk:");
        cases.add("Module talk:");
        cases.add("Topic talk:");
        cases.add("Special talk:");
        cases.add("Media talks:");

    }

    public ArrayList<String> getCases(){
        return cases;
    }

    public boolean caseExist(String ignoreCase){
        for(String c : cases){
            if(c.equals(ignoreCase)){
                return true;
            }
        }
        return false;
    }
}
