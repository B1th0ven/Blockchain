package com.scor.fileManager.controllers;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.scor.fileManager.entities.change;
import com.scor.fileManager.entities.file;
import com.scor.fileManager.entities.log;
import com.scor.fileManager.services.FileService;
import com.scor.fileManager.services.versionService;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class FileController {
    @Value("${custom.upload.path}")
    private String UPLOADED_FOLDER_BASE;

    @Autowired
    private FileService fileService;

    @Autowired
    private versionService vs;

    @RequestMapping("/explorer/{id}/{login}")
    public List<file> FileService(@PathVariable("id") int st_id, @PathVariable("login") String login) {
        return fileService.getLoclaFiles(st_id, login);
    }

    @RequestMapping("/explorer")
    public List<file> test(@RequestParam("path") String path) {
        return fileService.getLoclaFiles(path);
    }
    
    @RequestMapping("/clean")
    public void clean() {
        File dir = new File(UPLOADED_FOLDER_BASE + "/files");

        for (File file : dir.listFiles())
            if (!file.isDirectory())
                file.delete();
    }

    @RequestMapping("/torrent/files/{login}")
    public List<file> FileService(@PathVariable("login") String login) {
        return fileService.getUserFiles(login);
    }

    @RequestMapping(value = "/torrent/copy/{stid}", method = RequestMethod.POST)
    public file copy(@RequestBody String path, @PathVariable("stid") int stid) throws IOException {
        return fileService.CopieFileToStudy(path, stid);

    }

    @RequestMapping("/history")
    public List<log> getLogs() throws Exception {
        return vs.getLog();
    }
}
