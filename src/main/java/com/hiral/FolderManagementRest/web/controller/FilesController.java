package com.hiral.FolderManagementRest.web.controller;

import java.io.IOException;


import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.hiral.FolderManagementRest.model.Files;
import com.hiral.FolderManagementRest.model.Folder;
import com.hiral.FolderManagementRest.model.dao.FolderFilesDao;

@RestController
@RequestMapping("/files")
public class FilesController {

	@Autowired
	private FolderFilesDao folderfilesDao;

	//private static String UPLOAD_DIR = "uploads";

	long millis = System.currentTimeMillis();
	Date date = new Date(millis);

	@PostMapping()
	public String uploadTopFile(@RequestParam("file") MultipartFile file,HttpServletRequest request)throws IOException {
		return UploadFile(null, file, request);
	}

	@PostMapping("/{folderid}")
	public String uploadSameFile(@RequestParam("file") MultipartFile file, HttpServletRequest request, @PathVariable Integer folderid) throws IOException {
		return UploadFile(folderid, file, request);
	}
	
	@PostMapping("/notsame/{folderid}")
	public String uploadNotSameFile(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			@PathVariable Integer folderid) throws IOException {
		Folder folder = folderfilesDao.getFolder(folderid);
		if (folder == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Folder Id is not Available !");
		}
		String fileName = file.getOriginalFilename();
		Files checkFile = folderfilesDao.CheckSubFiles(fileName,folderid);
		Files ownfile = new Files();

		try {
			ownfile.setFilecreatedon(date);
			ownfile.setFileformat(file.getContentType());
			ownfile.setFilesize(file.getSize());
			ownfile.setFolderid(folderfilesDao.getFolder(folderid));
			if (checkFile != null) {
				return "File already available in this folder !! Please Upload new file  !";

			} else {
				ownfile.setFilename(fileName);
				ownfile.setFileData(file.getBytes());
				ownfile.setFileversion(checkFile);
				folderfilesDao.saveFiles(ownfile);
				return "File Uploaded Sucessfully !!";
			}

		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<Object> downloadMainFile(@PathVariable Integer id) throws IOException {
		Files files1 = folderfilesDao.getFiles(id);
		if (files1 == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Files Id is not Available !");
		}
		HttpHeaders headers = new HttpHeaders();
		Files filepath = folderfilesDao.getFiles(id);
		if(filepath.getFileversion() != null) {
			int latestfileId = filepath.getFileversion().getId();
			Files newLatestFile = folderfilesDao.getFiles(latestfileId);
			headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", newLatestFile.getFilename()));
			ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers)
					.contentLength(newLatestFile.getFilesize()).contentType(MediaType.parseMediaType("application/txt"))
					.body(newLatestFile.getFileData());

			return responseEntity;
		}
	
		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", filepath.getFilename()));
		ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers)
				.contentLength(filepath.getFilesize()).contentType(MediaType.parseMediaType("application/txt"))
				.body(filepath.getFileData());

		return responseEntity;
	}
	@GetMapping("/version/{id}")
	public ResponseEntity<Object> downloadVersionFile(@PathVariable Integer id) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		Files files1 = folderfilesDao.getFiles(id);
		if (files1 == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Files Id is not Available !");
		}
		Files filepath = folderfilesDao.getFiles(id);
		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", filepath.getFilename()));
		ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers)
				.contentLength(filepath.getFilesize()).contentType(MediaType.parseMediaType("application/txt"))
				.body(filepath.getFileData());

		return responseEntity;
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Object> deleteVersionFile(@PathVariable Integer id) {
		Files files = folderfilesDao.getFiles(id);
		if (files == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Files Id is not Available !");
		}
		ArrayList<Integer> deleteFilesList = new ArrayList<Integer>();
		deleteFilesList.add(id);
		folderfilesDao.deleteFilesList(files, deleteFilesList);
		if (deleteFilesList.size() <= 1) {
			return new ResponseEntity<>(("Total " + deleteFilesList.size()
					+ " Main file Deleted Succesfully ! The Version file not available !"), HttpStatus.OK);
		}
		return new ResponseEntity<>(
				("Total " + deleteFilesList.size() + " Main file and all vesion files  Deleted Succesfully ! "),
				HttpStatus.OK);
	}
	
	public String UploadFile(Integer folderid,MultipartFile file, HttpServletRequest request) {
		Folder folder = new Folder();
		if(folderid != null) {
			folder = folderfilesDao.getFolder(folderid);
			if (folder == null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Folder Id is not Available !");
			}
		}
		String fileName = file.getOriginalFilename();
		Files checkFile = folderfilesDao.CheckParentFiles(fileName);
		Files ownfile = new Files();

		try {
			ownfile.setFilecreatedon(date);
			ownfile.setFileformat(file.getContentType());
			ownfile.setFilesize(file.getSize());
			if(folderid != null) {
				ownfile.setFolderid(folder);
			}else {
				ownfile.setFolderid(null);
			}
			

			if (checkFile != null) {
				ownfile.setFileData(file.getBytes());
				ownfile.setFilename(checkFile.getId() +"-"+ fileName);
				if(checkFile.getFileversion() != null) {
					ownfile.setFileversion(checkFile.getFileversion());
				}else {
					ownfile.setFileversion(checkFile);
				}
				folderfilesDao.saveFiles(ownfile);
				return "File already available in database !!  New Version created !";

			} else {
				ownfile.setFilename(fileName);
				ownfile.setFileData(file.getBytes());
				ownfile.setFileversion(checkFile);
				folderfilesDao.saveFiles(ownfile);
				return "File Uploaded Sucessfully !!";
			}
		} catch (Exception e) {
			return e.getMessage();
		}
	}

}
