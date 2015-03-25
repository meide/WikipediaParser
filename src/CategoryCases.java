import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Karina on 25.03.2015.
 */
public class CategoryCases {
    private ArrayList<String> categoryCases = new ArrayList<String>();

    public CategoryCases(){
        categoryCases.add("Category:Network theory");
        categoryCases.add("Category:Computer science");
        categoryCases.add("Category:Latin language");
    }

    public ArrayList<String> getCategoryCases() {
        return categoryCases;
    }
}
