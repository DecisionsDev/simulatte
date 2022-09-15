package com.ibm.simulatte.core.utils;

import com.ibm.simulatte.core.datamodels.data.FileType;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

@Slf4j
public class DataManager {

    @Autowired
    static SparkSession sparkSession;
    public static JSONArray serializeToJSONArray(String filepath) throws IOException {

        // Format checking here

        // Create json array from document
        JSONArray loanRequestList = new JSONArray();

        //convert json file as jsonArray
        URL url = new File(filepath).toURI().toURL();
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))){
            List<String> lines = bufferedReader.lines().collect(Collectors.toList());
            for(String line : lines){ loanRequestList.put(new JSONObject(line)); }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Simulation data file not found at "+filepath+". Please, check your data source uri.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Simulation data file not found at "+filepath+". Please, check your data source uri.");
        }
        return loanRequestList;
    }


    public static void setSimulationDataSink(String filename, JSONArray jsonArray) throws IOException {
        if (filename!="" || filename!=null){
            Files.write(Paths.get(filename), jsonArray.toString().getBytes());
        }else{
            System.out.println("Data sink uri not valid.");
        }
    }

    public static void writeArgumentForAnalytics(String filepath, String key, String value) throws IOException {
        //Add file path validator here
        if (filepath=="" || filepath==null){ log.error("Arguments file uri not valid (for analytics)."); }
        OutputStream out = Files.newOutputStream(Paths.get(filepath), CREATE, APPEND);
        out.write((String.format("%s = %s", key, value)+"\n").getBytes(UTF_8));
        out.flush();
    }

    private static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    public static void writeInDataSink(boolean optimization, String filepath, JSONArray data, JavaRDD<String> dataRDD, FileType format) throws IOException {
        //Add file path validator here
        if (filepath=="" || filepath==null){ System.out.println("Data sink uri not valid."); } // Quick check

        if(format==FileType.JSON){
            if(Files.exists(Paths.get(filepath)) && optimization) new File(filepath).delete();
            System.out.println("JUST BEFORE FILE CREATION");
            System.out.println("OUTPUT FILE : "+filepath);
            OutputStream out = Files.newOutputStream(Paths.get(filepath), CREATE, APPEND);
            for(Object line: data){
                out.write((line.toString()+"\n").getBytes(UTF_8));
            }
            out.flush();
        }
        if(format==FileType.PARQUET){
            Path path = Paths.get(filepath.replaceFirst("[.][^.]+$", ""));
            if (Files.exists(path)) deleteDirectory(path.toFile());
            dataRDD.saveAsTextFile(filepath.replaceFirst("[.][^.]+$", ""));
        }
    }
}
