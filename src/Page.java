import java.util.ArrayList;

/**
 * Created by Karina on 19.03.2015.
 */
public class Page{

    private boolean redirecting = false;
    private String title;
    ArrayList<String> links = new ArrayList<String>();
    ArrayList<String> categories = new ArrayList<String>();

    public void setRedirecting(boolean b) {
        redirecting = b;
    }

    public boolean isRedirecting() {
        return redirecting;
    }

    public void setTitle(String s) {
        title = s;
    }

    public String getTitle(){
        return title;
    }

    public void addLink(String link){
        if(!links.contains(link)) {
            links.add(link);
        }
    }

    public ArrayList<String> getLinks(){
        return links;
    }

    public void addCategory(String category) {
        if (!categories.contains(category)) {
            categories.add(category);
        }
    }

    public ArrayList<String> getCategories(){
        return categories;
    }
}
