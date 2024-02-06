package com.hiral.FolderManagementRest.model.dao;

import java.util.ArrayList;

import java.util.List;

import com.hiral.FolderManagementRest.model.Files;
import com.hiral.FolderManagementRest.model.Folder;

public interface FolderFilesDao {
	
	// ------- Folders -----------
	List<Folder> getParentFolders();
	List<Folder> getChildFolders(Integer id);
	Folder saveFolder(Folder folder);
	void  deleteFolderList(Folder folder, ArrayList<Integer> deleteFolderList,ArrayList<Integer> deleteFilesList);
	void deleteFolders(ArrayList<Integer> deleteFolderList);
	Folder getFolder(Integer id);
	
	
	// ------- Files -----------
	List<Files> getParentFiles();
	List<Files> getChildFiles(Integer id);
	void saveFiles(Files files);
	Files CheckParentFiles(String  fileName);
	void  deleteFilesList(Files files, ArrayList<Integer> deleteFilesList);
	Files getFiles(Integer id);
	
	public void deleteFilesFolderList(Integer id, ArrayList<Integer> deleteFilesList);
	
	public void deleteFilesFolderVersionList(Files files, ArrayList<Integer> deleteFilesList);
	Files CheckSubFiles(String  fileName,Integer folderid);
}


