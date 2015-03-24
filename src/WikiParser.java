import java.io.*;

public class WikiParser {
    // Assume default encoding.
    private static FileWriter fileWriter;
    private static FileWriter fileWriter2;

    // Always wrap FileWriter in BufferedWriter.
    private static BufferedWriter bufferedWriter;
    private static BufferedWriter bufferedWriter2;

    public static void main(final String[] args) throws IOException {
        //Oppretter en fil for alle artiklene og en for kantene
        fileWriter = new FileWriter("articles.txt");
        fileWriter2 = new FileWriter("edges.txt");
        bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter2 = new BufferedWriter(fileWriter2);

        fileNodeDefSetup(bufferedWriter);
        fileEdgeDefSetup(bufferedWriter2);

        final int[] nameKey = {1};

        XMLManager.load(new  PageProcessor(){
            @Override
            public void process(Page page){
                writeArticleToFile(nameKey[0], page.getTitle());
                for(String link : page.getLinks()){
                    writeArticleEdgeToFile(page.getTitle(), link);
                }
                nameKey[0]++;
            }
        });

        bufferedWriter.close();

        //Setter samme de to forrige filene til en GDF fil
        createGDFFile();
    }

    private static void createGDFFile() throws IOException {
        FileReader fileReader = new FileReader("articles.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        FileReader fileReader2 = new FileReader("edges.txt");
        BufferedReader bufferedReader2 = new BufferedReader(fileReader2);

        FileWriter fileWriter = new FileWriter("wikipediaGraph.gdf");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        String articleLine = bufferedReader.readLine();
        while(articleLine != null){
            bufferedWriter.write(articleLine);
            bufferedWriter.newLine();
            articleLine = bufferedReader.readLine();
        }

        String edgeLine = bufferedReader2.readLine();
        while(edgeLine != null){
            bufferedWriter.write(edgeLine);
            bufferedWriter.newLine();
            edgeLine = bufferedReader2.readLine();
        }
    }

    private static void fileEdgeDefSetup(BufferedWriter bufferedWriter) {
        try {
            bufferedWriter.write("edgedef>node1 VARCHAR,node2 VARCHAR");
            bufferedWriter.newLine();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void fileNodeDefSetup(BufferedWriter bufferedWriter) {
        try {
            bufferedWriter.write("nodedef>name VARCHAR,label VARCHAR");
            bufferedWriter.newLine();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void writeArticleToFile(int articleNr, String text){
        try {
            bufferedWriter.write("article" + articleNr + "," + text);
            bufferedWriter.newLine();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void writeArticleEdgeToFile(String node1, String node2){
        try{
            bufferedWriter2.write(node1 + "," + node2);
            bufferedWriter2.newLine();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}   