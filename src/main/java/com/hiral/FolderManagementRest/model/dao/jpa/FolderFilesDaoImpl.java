package com.hiral.FolderManagementRest.model.dao.jpa;

import java.util.ArrayList;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hiral.FolderManagementRest.model.Files;
import com.hiral.FolderManagementRest.model.Folder;
import com.hiral.FolderManagementRest.model.dao.FolderFilesDao;

@Repository
public class FolderFilesDaoImpl implements FolderFilesDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	//------------------------------- Folders ----------------------------------
	
	@Override
	public List<Folder> getParentFolders() {
		return entityManager.createQuery( "select f from Folder f where f.id.parentid IS NULL", Folder.class).getResultList();
	}
	
	@Override
	public List<Folder> getChildFolders(Integer id) {
		Folder e3 = entityManager.find(Folder.class, id);
		List<Folder> folder = entityManager.createQuery( "from Folder where parentid = :parentid ", Folder.class)
				.setParameter( "parentid", e3 )
				.getResultList();
		return folder;
	}
	
	@Override
	@Transactional
	public Folder saveFolder(Folder folder) {
		return entityManager.merge(folder);
	}
	
	@Override
	@Transactional
	public void deleteFolderList(Folder folder, ArrayList<Integer> deleteFolderList,ArrayList<Integer> deleteFilesList) {
			List<Folder> subFolders = entityManager.createQuery("from Folder where parentid = :parentid")
					.setParameter("parentid", folder)
					.getResultList();
			if(subFolders != null) {
				for(Folder f : subFolders) {
					deleteFolderList.add(f.getId());
					deleteFilesFolderList(f.getId(),deleteFilesList);
					deleteFolderList(f, deleteFolderList,deleteFilesList);
				}
			}	
	}
	
	@Override
	@Transactional
	public void deleteFolders(ArrayList<Integer> deleteFolderList) {
		for(int i = (deleteFolderList.size()-1);i >= 0 ; i--) {
			Folder folder = entityManager.find(Folder.class, deleteFolderList.get(i));
			entityManager.remove(folder);
		}
	}
	
	@Override
	public Folder getFolder(Integer id) {
		return entityManager.find( Folder.class, id );
	}
	
	//------------------------------- Files ----------------------------------

	@Override
	public List<Files> getParentFiles() {
		return entityManager.createQuery( "from Files where folderid_id IS NULL", Files.class).getResultList();
	}
	
	@Override
	public List<Files> getChildFiles(Integer folid) {
		List<Files>	files = entityManager.createQuery("from Files where folderid IS NOT NULL",Files.class).getResultList();
		List<Files> newList = new ArrayList<Files>();
		for(Files f: files) {
			if(f.getFolderid().getId().equals(folid)) {
				newList.add(f);
			}
		}
		return newList;
	}

	@Override
	@Transactional
	public void saveFiles(Files files) {
		entityManager.merge(files);
	}

	@Override
	public Files CheckParentFiles(String  fileName) {
		List<Files> subFiles =  entityManager.createQuery( "from Files", Files.class).getResultList();
		int latestid = 0;
		boolean isEnter = false;
			for(Files f : subFiles) {
				if(f.getFilename().equals(fileName) ) {
					latestid = f.getId();
				}
				if(latestid != 0) {
					if((latestid+"-"+fileName).equals(f.getFilename())) {
						latestid = f.getId();
						isEnter = true;
					}
				}
			}
			if(isEnter == false) {
				if(latestid == 0) {
					return null;
				}
				return entityManager.find(Files.class, latestid);
			}else {
				return entityManager.find(Files.class, latestid);
			}
	
	}
	@Override
	public Files CheckSubFiles(String fileName, Integer folderid) {
		List<Files> subFilesFolderid = entityManager.createQuery("from Files where id.folderid IS NOT NULL", Files.class).getResultList();
		for(Files f : subFilesFolderid) {
			if(f.getFolderid().getId().equals(folderid) && f.getFilename().equals(fileName)) {
				return f;
			}
		}
		return null;
	}
	
	@Override
	@Transactional
	public void deleteFilesList(Files files, ArrayList<Integer> deleteFilesList) {
		List<Files> subFiles = entityManager.createQuery("from Files where fileversion = :fileversion ")
				.setParameter("fileversion", files)
				.getResultList();
		for(Files f : subFiles) {
			deleteFilesList.add(f.getId());
			entityManager.remove(f);
		}
		entityManager.remove(files);
	}
	
	@Override
	@Transactional
	public void deleteFilesFolderVersionList(Files files, ArrayList<Integer> deleteFilesList) {
		List<Files> subFiles = entityManager.createQuery("from Files where fileversion = :fileversion ")
				.setParameter("fileversion", files)
				.getResultList();
		if(subFiles != null) {
		for(Files f : subFiles) {
			deleteFilesList.add(f.getId());
		}
		}
	}
	
	@Override
	@Transactional
	public void deleteFilesFolderList(Integer folderFileid, ArrayList<Integer> deleteFilesList) {
		List<Files> subFiles = entityManager.createQuery("from Files where id.folderid IS NOT NULL", Files.class).getResultList();
		
		for (Files f : subFiles) {
			if(f.getFolderid().getId() == (folderFileid)) {
				deleteFilesList.add(f.getId());
				deleteFilesFolderVersionList(f,deleteFilesList);
			}
		}
	
		for(int i = (deleteFilesList.size()-1);i >= 0 ; i--) {
			Files deleteFile = entityManager.find(Files.class, deleteFilesList.get(i));
			if(deleteFile !=  null) {
				entityManager.remove(deleteFile);
			}
		}
	}
	
	@Override
	public Files getFiles(Integer id) {
		return entityManager.find( Files.class, id );
	}
}