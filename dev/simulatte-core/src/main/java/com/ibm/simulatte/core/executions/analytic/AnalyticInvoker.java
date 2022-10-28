package com.ibm.simulatte.core.executions.analytic;

import com.ibm.simulatte.core.datamodels.run.Run;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ibm.simulatte.core.executions.online.OnlineServiceInvoker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

@Service
@Slf4j
public class AnalyticInvoker {

    @Autowired
    private OnlineServiceInvoker onlineServiceInvoker;
    public static int executeNotebookFromShellCommand(String absolutePath, String [] shellCommand){
        ProcessBuilder processBuilder = new ProcessBuilder(shellCommand);
        processBuilder.directory(new File(absolutePath));

        try {
            Process process = processBuilder.start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.warn(line);
            }

            int exitCode = process.waitFor();
            return exitCode;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void executeNotebook(Run run, Map<String, String> headerOptions) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(run);
        onlineServiceInvoker.sendRequest("http://127.0.0.1:8000/simulation/compute-notebook", json, headerOptions);
    }

    //export to html format  "-Xms1024M", "-Xmx4096M",
        /*File notebook = new File(notebookUri);
        List<String> shellCommand = new ArrayList<String>();
        Collections.addAll(shellCommand, new String[]{"jupyter", "nbconvert", "--to", "html", "--no-input"});
        shellCommand.add(String.format("comparison_output_%s_vs_%s.ipynb", firstRun.getUid(), secondRun.getUid()));

        System.out.println("DR: "+notebook.getParent()+" CMD: "+shellCommand.toString());
        String filename = String.format("comparison_output_%s_vs_%s.ipynb", firstRun.getUid(), secondRun.getUid());
        log.warn("\nExited with error code (Jupyter notebook execution): " +
                    analyticInvoker.executeNotebookFromShellCommand(notebook.getParent(),
                            new String[]{"jupyter", "nbconvert", "--to", "html", "--no-input", filename})
                +"\n");*/
}
