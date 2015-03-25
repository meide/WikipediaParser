import java.io.*;
import java.util.HashMap;

public class WikiParser {
    // Assume default encoding.
    private static FileWriter fileWriter;
    private static FileWriter fileWriter2;

    // Always wrap FileWriter in BufferedWriter.
    private static BufferedWriter bufferedWriter;
    private static BufferedWriter bufferedWriter2;

    private static HashMap<String,String> pagesThatHasCategory = new HashMap<String, String>();

    public static void main(final String[] args) throws IOException {
        //Oppretter en fil for alle artiklene og en for kantene
        fileWriter = new FileWriter("articles.txt");
        fileWriter2 = new FileWriter("edges.txt");
        bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter2 = new BufferedWriter(fileWriter2);

        //Setter opp headeren i hver fil
        fileNodeDefSetup(bufferedWriter);
        fileEdgeDefSetup(bufferedWriter2);

        final int[] nameKey = {1};
        XMLManager.load(new  PageProcessor(){
            @Override
            public void process(Page page){
                //Lagrer kun sider som har rett kategori
                if (page.getTitle() != null && page.getCategories().size() > 0) {
                    writeArticleToFile(nameKey[0], page);
                    for (String link : page.getLinks()) {
                            writeArticleEdgeToFile(page.getTitle(), link);
                    }
                    pagesThatHasCategory.put(page.getTitle(), page.getTitle());
                    nameKey[0]++;
                }
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
        int teller = 0;
        while(edgeLine != null){
            //Henter ut nodeinformasjonene
            String[] felt = edgeLine.split(",");
            if(felt.length > 2) {
                //Viss vi er på første linje, så skal vi skrive infoen (teller == 0)
                //Ellers må node2 være en side som har rett kategori
                if ((teller == 0 || pagesThatHasCategory.containsKey(felt[1]))){
                    bufferedWriter.write(edgeLine);
                    bufferedWriter.newLine();
                    teller++;
                }
            }
            edgeLine = bufferedReader2.readLine();
        }
    }

    private static void fileEdgeDefSetup(BufferedWriter bufferedWriter) {
        try {
            bufferedWriter.write("edgedef>node1 VARCHAR,node2 VARCHAR,directed BOOLEAN");
            bufferedWriter.newLine();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void fileNodeDefSetup(BufferedWriter bufferedWriter) {
        try {
            bufferedWriter.write("nodedef>name VARCHAR,label VARCHAR,category1 VARCHAR," +
                    "category2 VARCHAR,category3 VARCHAR");
            bufferedWriter.newLine();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Skriver en artikkel med gitt artikkelnummer til filen articles.txt
     * Skriver i rett format, der hver side kan ha alt fra 1 - 3 kategorier
     *
     */
    public static void writeArticleToFile(int articleNr, Page page){
        try {
            if(page.getCategories().size() == 1){
                bufferedWriter.write("article" + articleNr + "," + page.getTitle() + "," + page.getCategories().get(0)
                + ", , ");
            }else if(page.getCategories().size() == 2){
                bufferedWriter.write("article" + articleNr + "," + page.getTitle() + "," + page.getCategories().get(0)
                + "," + page.getCategories().get(1) + ", ");
            }else{
                bufferedWriter.write("article" + articleNr + "," + page.getTitle() + "," + page.getCategories().get(0)
                        + "," + page.getCategories().get(1) + "," + page.getCategories().get(2));
            }
            bufferedWriter.newLine();

        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Tar inn to noder og skriver det til filen edge.txt på rett format
     * Setter at dette er en directed kant
     *
     */
    public static void writeArticleEdgeToFile(String node1, String node2){
        try{
            bufferedWriter2.write(node1 + "," + node2 + "," + "true");
            bufferedWriter2.newLine();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}   