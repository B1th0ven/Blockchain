package com.scor.fileManager.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.scor.fileManager.entities.file;

@Service
public class UploadService implements Serializable {

	@Value("${custom.upload.path}")
	private String UPLOADED_FOLDER_BASE;

	public file upload(HttpServletRequest request, String type, int st_id) {

		String UPLOADED_FOLDER = null;

		if (type.equals("dataset"))
			UPLOADED_FOLDER = UPLOADED_FOLDER_BASE + "/files/" + st_id + "/";
		else if (type.equals("attached"))
			UPLOADED_FOLDER = UPLOADED_FOLDER_BASE + "/attachedfiles/";
		else if (type.equals("ibnr"))
			UPLOADED_FOLDER = UPLOADED_FOLDER_BASE + "/ibnr/";
		else if (type.equalsIgnoreCase("expected"))
			UPLOADED_FOLDER = UPLOADED_FOLDER_BASE + "/expectedfiles/";

		File folder = new File(UPLOADED_FOLDER);
		if (!folder.exists())
			folder.mkdirs();

		if (UPLOADED_FOLDER.isEmpty())
			return null;

		file res = new file("", "", 0, "");
		try {

			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (!isMultipart) {
				// Inform user about invalid request
				if (res.getName().equals(""))
					throw new IllegalArgumentException("Upload failed");
				return res;
			}

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload();

			// Parse the request
			FileItemIterator iter = upload.getItemIterator(request);
			while (iter.hasNext()) {
				FileItemStream item = iter.next();
				String name = item.getFieldName();
//             InputStreamReader stream = new InputStreamReader(item.openStream(), "ISO-8859-1");
//             InputStream stream = item.openStream();
				if (!item.isFormField()) {
					String filename = item.getName();

					String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HHmmss")
							.format(Calendar.getInstance().getTime());

					List<String> a = new ArrayList(Arrays.asList(filename.split("\\.", -1)));
					String ext = a.get(a.size() - 1);
					if (a.size() >= 2) {
						a.remove(a.size() - 1);
						filename = String.join("", a) + "_" + timeStamp + "." + ext;
					}

					res.setType(ext);
					res.setPath(UPLOADED_FOLDER.replaceAll("\\\\", "/") + filename);
					res.setName(filename);

					// Process the input stream
					OutputStream out = new FileOutputStream(UPLOADED_FOLDER + filename);
					if (type.equals("attached")) {
						InputStream stream = item.openStream();
						IOUtils.copy(stream, out);
						stream.close();
					} else {
						InputStreamReader stream = new InputStreamReader(item.openStream(), "ISO-8859-1");
						IOUtils.copy(stream, out);
						stream.close();
					}
					out.close();
				}
			}
		} catch (FileUploadException e) {
			return res;
		} catch (IOException e) {
			return res;
		}

		if (res.getName().equals(""))
			throw new IllegalArgumentException("Upload failed");

		return res;
	}
}
