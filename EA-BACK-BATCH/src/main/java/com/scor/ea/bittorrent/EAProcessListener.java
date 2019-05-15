package com.scor.ea.bittorrent;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.scor.omega2.core.service.utils.logger.AbstractLoggerFactory;
import com.scor.omega2.core.service.utils.logger.Logger;
import com.scor.torrent.client.process.ProcessEvent;
import com.scor.torrent.client.process.ProcessListener;
import com.scor.torrent.client.process.ProcessListeners;
import com.scor.torrent.client.process.ProcessReturnStatus;

@Component
public class EAProcessListener implements InitializingBean, ProcessListener {
	// logger initialisation :
	private static final Logger LOG = AbstractLoggerFactory.getLogger(EAProcessListener.class);

	// ProcessListeners injection :
	@Inject
	private ProcessListeners processListeners;

	// where to put file after download :
	private static final String FILE_DIRECTORY = "${file.directory}";
	@Value(FILE_DIRECTORY)
	private String fileDirectory;

	// path after download :
	private Path fileDirectoryPath;

	// Invoked by a BeanFactory after it has set all bean properties supplied :
	@Override
	public void afterPropertiesSet() throws Exception {
		this.processListeners.register(this);

		// now this whill wait for notifications from tracker

		// if the file directory is not changed throw exception :
		if (StringUtils.equalsAnyIgnoreCase(FILE_DIRECTORY, this.fileDirectory)) {
			throw new IllegalArgumentException("File directory property is not correctly set.");
		}

		// create fileDirectory if not exists :
		this.fileDirectoryPath = Paths.get(this.fileDirectory);
		if (!Files.exists(this.fileDirectoryPath)) {
			Files.createDirectories(this.fileDirectoryPath);
		}
	}

	// Invoked by the tracker after the download is done :
	@Override
	public ProcessReturnStatus onEvent(ProcessEvent event) {
		LOG.info("File directory {}.", event.getFileDirectory().toString());
		try {
			// copy file to destination :
			Files.walkFileTree(event.getFileDirectory(), new CopyDir(event.getFileDirectory(),
					this.fileDirectoryPath.resolve(event.getMetaData().getUserId().toUpperCase().trim())));

			return ProcessReturnStatus.OK;
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
			return ProcessReturnStatus.ERROR;
		}
	}

	private class CopyDir extends SimpleFileVisitor<Path> {
		private Path sourceDir;
		private Path targetDir;

		public CopyDir(Path sourceDir, Path targetDir) {
			this.sourceDir = sourceDir;
			this.targetDir = targetDir;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {

			try {
				Path targetFile = targetDir.resolve(sourceDir.relativize(file));
				targetFile = addPrefixToFileNameIfExist(file, targetFile, 0);
				Files.copy(file, targetFile);
				LOG.info("File downloaded : " + targetFile);
			} catch (IOException ex) {
				LOG.error(ex.getMessage(), ex);
			}

			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes) throws IOException {
			Path newDir = targetDir.resolve(sourceDir.relativize(dir));
			File destinationDir = new File(newDir.toString());
			if (destinationDir.exists()) {
				return FileVisitResult.CONTINUE;
			}
			Files.createDirectory(newDir);
			return FileVisitResult.CONTINUE;
		}

		private Path addPrefixToFileNameIfExist(Path file, Path targetFile, int index) {
			if (!Files.exists(targetFile)) {
				return targetFile;
			}
			String fileName = file.getFileName().normalize().toString();
			String[] fileList = fileName.split("\\.", 2);
			index++;
			String changedFileName = fileList[0] + "(" + index + (fileList.length > 1 ?  ")." + fileList[1] : "");
			targetFile = targetFile.getParent().resolve(changedFileName);
			return addPrefixToFileNameIfExist(file, targetFile, index);
		}
	}

}
