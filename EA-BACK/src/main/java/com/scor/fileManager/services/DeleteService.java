package com.scor.fileManager.services;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

@Service
public class DeleteService implements Serializable {

	public void delete(String filepath) throws IOException {
		Path path = Paths.get(filepath);
		// System.out.println("File exits before deleting: " + Files.exists(path));
		Files.delete(path);
		// System.out.println("File exits after deleting: " + Files.exists(path));
	}

}
