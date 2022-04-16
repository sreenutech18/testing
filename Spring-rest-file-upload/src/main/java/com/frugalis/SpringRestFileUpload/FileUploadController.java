package com.frugalis.SpringRestFileUpload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {

	String UPLOAD_DIR = "D://upload//";
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody String handleFileUpload(@RequestParam(value = "file") MultipartFile file) throws IOException {

		String fileExtension = getFileExtension(file);
		String filename = getRandomString();

		File targetFile = getTargetFile(fileExtension, filename);

		byte[] bytes = file.getBytes();
		file.transferTo(targetFile);
		String UploadedDirectory = targetFile.getAbsolutePath();

		return filename;
	}
	
	
	@RequestMapping(value = "/upload/{galleryId}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getFile(@PathVariable("galleryId")String galleryId) throws IOException {

		byte[] bFile = Files.readAllBytes(new File(UPLOAD_DIR+galleryId+".jpg").toPath());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		headers.setContentLength(bFile.length);

		ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(bFile, headers, HttpStatus.OK);
		return responseEntity;
		
	}
	

	private String getRandomString() {
		return new Random().nextInt(999999) + "_" + System.currentTimeMillis();
	}

	private File getTargetFile(String fileExtn, String fileName) {
		File targetFile = new File(UPLOAD_DIR + fileName + fileExtn);
		return targetFile;
	}

	private String getFileExtension(MultipartFile inFile) {
		String fileExtention = inFile.getOriginalFilename().substring(inFile.getOriginalFilename().lastIndexOf('.'));
		return fileExtention;
	}

}
