package com.hiral.FolderManagementRest.web.controller;

import java.sql.Date;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hiral.FolderManagementRest.model.Files;
import com.hiral.FolderManagementRest.model.FilesDto;
import com.hiral.FolderManagementRest.model.Folder;
import com.hiral.FolderManagementRest.model.FolderDto;
import com.hiral.FolderManagementRest.model.dao.FolderFilesDao;

@RestController
@RequestMapping("/folders")
public class FolderController {

	@Autowired
	private FolderFilesDao folderfilesDao;

	long millis = System.currentTimeMillis();
	Date date = new Date(millis);

	@GetMapping
	public List<Object> topLevelList() {
		List<Object> ffList = new ArrayList<Object>();
		//------------- Folders -----------------------
		List<Folder> folders = folderfilesDao.getParentFolders();
		List<FolderDto> folderDtos = new ArrayList<FolderDto>();
		if (folders.isEmpty()) {
			ffList.add("Folders are not Found! Please create New Folder !!");
		}else{
			ffList.add("Folders:");
		}
		for (Folder f : folders) {
			folderDtos.add(new FolderDto(f));
		}
		System.out.println("hello");
		ffList.add(folderDtos);
		//------------- Files -----------------------
		List<Files> files = folderfilesDao.getParentFiles();
		List<FilesDto> filesDtos = new ArrayList<FilesDto>();
		if (files.isEmpty()) {
			ffList.add("Files are not Found ! Please upload New Files !!");
		}else {
			ffList.add("Files:");
		}
		for (Files f : files) {
			filesDtos.add(new FilesDto(f));
		}
		ffList.add(filesDtos);
		
		return ffList;
	}

	@GetMapping("/{folderid}")
	public List<Object> subLevelList(@PathVariable Integer folderid) {
		List<Object> ffSubList = new ArrayList<Object>();
		Folder folder = folderfilesDao.getFolder(folderid);
		if (folder == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Folder Id is not Available !");
		}
		//------------- Folders -----------------------
		List<Folder> folders = folderfilesDao.getChildFolders(folderid);
		List<FolderDto> folderDtos = new ArrayList<FolderDto>();
		if (folders.isEmpty()) {
			ffSubList.add("Sub Folders are not Found! Please create New Folder !!");
		}else {
			ffSubList.add("Folders:");
		}
		for (Folder f : folders) {
			folderDtos.add(new FolderDto(f));
		}
		ffSubList.add(folderDtos);
		//------------- Files -----------------------
		List<Files> files = folderfilesDao.getChildFiles(folderid);
		List<FilesDto> filesDtos = new ArrayList<FilesDto>();
		if (files.isEmpty()) {
			ffSubList.add("Sub Files are not Found! Please upload New Files !!");
		}else {
			ffSubList.add("Files:");
		}
		for (Files f : files) {
			filesDtos.add(new FilesDto(f));
		}
		ffSubList.add(filesDtos);
		
		return ffSubList;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FolderDto addTopFolder(@RequestBody FolderDto folderDto) {
	
		if (!StringUtils.hasText(folderDto.getFoldername()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Folder Name is Required !");

		Folder folder = new Folder();
		folder.setFoldername(folderDto.getFoldername());
		folder.setFoldercreatedon(date);
		if (folderDto.getParentid() != null) {
			Folder parentid = folderfilesDao.getFolder(folderDto.getParentid());
			folder.setParentid(parentid);
		}
		folder = folderfilesDao.saveFolder(folder);
		return new FolderDto(folder);
	}

	@PostMapping("/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public FolderDto addSubFolder(@RequestBody FolderDto folderDto, @PathVariable Integer id) {
		Folder folder1 = folderfilesDao.getFolder(id);
		if (folder1 == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Folder Id is not Available !");
		}
		if (!StringUtils.hasText(folderDto.getFoldername()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Folder Name is Required !");
		Folder folder = new Folder();
		folder.setFoldername(folderDto.getFoldername());
		folder.setFoldercreatedon(date);
		Folder parentid = folderfilesDao.getFolder(id);
		if (parentid != null) {
			folder.setParentid(parentid);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Folder Id is not Available !");
		}

		folder = folderfilesDao.saveFolder(folder);
		return new FolderDto(folder);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		Folder folder = folderfilesDao.getFolder(id);
		if (folder == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Folder Id is not Available !");
		}
		ArrayList<Integer> deleteFilesList = new ArrayList<Integer>();
		folderfilesDao.deleteFilesFolderList(id,deleteFilesList);		
		ArrayList<Integer> deleteFolderList = new ArrayList<Integer>();
		deleteFolderList.add(id);
		folderfilesDao.deleteFolderList(folder, deleteFolderList,deleteFilesList);
		folderfilesDao.deleteFolders(deleteFolderList);
		System.out.println(deleteFilesList);
		return new ResponseEntity<>(("Total " + deleteFolderList.size() + " folders Deleted Succesfully ! "
				+ "Total "+ deleteFilesList.size() +" files Deleted Succesfully ! "),HttpStatus.OK);
	}

	// -------------------------------------------------------

	@GetMapping("/{id}/{parentid}")
	public List<FolderDto> list2(@PathVariable Integer parentid) {
		List<Folder> folders = folderfilesDao.getChildFolders(parentid);
		List<FolderDto> folderDtos = new ArrayList<FolderDto>();

		for (Folder f : folders) {
			if (f != null)
				folderDtos.add(new FolderDto(f));
		}
		return folderDtos;
	}

}
