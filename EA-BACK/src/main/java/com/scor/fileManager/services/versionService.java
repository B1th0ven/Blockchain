package com.scor.fileManager.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import com.scor.fileManager.entities.change;
import com.scor.fileManager.entities.file;
import com.scor.fileManager.entities.log;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class versionService {

    private Stream<String> readFileContent(String path_file) throws IOException, URISyntaxException {
        Path path =  Paths.get(versionService.class.getClassLoader().getResource(path_file).toURI());
        Stream<String> lines = Files.lines(path, StandardCharsets.ISO_8859_1);
        return lines.skip(1);
    }

    public List<log> getLog() throws Exception {
        List<log> res = new ArrayList<>();
        File folder;
        try {
           folder  = new File(Paths.get( versionService.class.getClassLoader().getResource("history/").toURI()).toString());
        } catch (Exception e) {
            //in case there is no log file
            return new ArrayList<>();
        }
        if (!folder.isDirectory()) throw new Exception("Cannot Locate Logs Folder");
        else if (folder.isDirectory() && folder.listFiles().length<=0) return new ArrayList<>();
        else {
            File[] listOfFiles = folder.listFiles();
            res = scanFolder(listOfFiles);
        }

        return res;
    }

    public List<change> getChanges(Stream<String> lines ) throws IOException, URISyntaxException {
        List<change> res = new ArrayList<>();
        lines.forEach( s-> {
            res.add(mapTochange(s));
        });
        return res;
    }

    private change mapTochange(String line){
        String[] values = line.toLowerCase().trim().split(";");
        return new change(values[1],values[0],values[4],values[2],values[3]);
    }

    private List<log> scanFolder(File[] listOfFiles) throws IOException, URISyntaxException {
        List<log> logs = new ArrayList<>();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    log log;
                    String type = FilenameUtils.getExtension(listOfFiles[i].getName());
                    if (type.equals("csv")) {
                        Stream<String> lines = readFileContent("history/"+listOfFiles[i].getName());
                        List<change> changes = getChanges(lines);
                        int index = listOfFiles[i].getName().lastIndexOf(".");
                        String fileName =  listOfFiles[i].getName().substring(0, index);
                        String date = fileName.split("_",-1)[1];
                        String version =  fileName.split("_",-1)[2];
                        log = new log(version,date,changes);
                        logs.add(log);
                    }

                }
            }
        }
        return logs;
    }
}
