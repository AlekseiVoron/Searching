import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class WorkWithFiles {
    public static Gson gson = new Gson();

    public static void writeToFile(Object object, String fileName){
        String json = gson.toJson(object);
        try(FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(json);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void writeToFileByLine(Collection<String> lines, String wordsPath) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(wordsPath))))) {
            for (String line: lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void writeSynonyms(Collection<Collection<String>> lines, String file) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(file))))) {
            for (Collection<String> line: lines) {
                String newLine = String.join(",", line);
                writer.write(newLine);
                writer.newLine();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static ArrayList<Article> readSeries(String fileName) throws IOException {
        Type listType = new TypeToken<ArrayList<Article>>() {}.getType();
        FileReader fileReader = new FileReader(new File(fileName));
        JsonReader reader = new JsonReader(fileReader);
        ArrayList<Article> result = gson.fromJson(reader, listType);
        fileReader.close();
        return result;
    }

    public static List<List<String>> readSynonyms(String filePath) {
        String line;
        List<List<String>> resList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))))) {
            while ((line = reader.readLine()) != null) {
                List<String> words = Arrays.asList(line.split(","));
                resList.add(words);
            }
        } catch (IOException e){
            System.out.println("Error file");
        }
        return resList;
    }
}