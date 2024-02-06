package com.hiral.FolderManagementRest.model;

import java.io.Serializable;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "files")
public class Files implements Serializable{
	
    private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue
    private Integer id;
	@Column(name = "filename")
    private String filename;
	
	@Lob
    private byte[] fileData;
	
    @Column(name = "filecreated_on")
    private Date filecreatedon;
    
    @Column(name = "filesize")
    private long filesize;
    
    @Column(name = "fileformat")
    private String fileformat;
    
	@ManyToOne
    private Files fileversion;
    @ManyToOne
    private Folder folderid;
    
    
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public byte[] getFileData() {
		return fileData;
	}
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
	
	public Date getFilecreatedon() {
		return filecreatedon;
	}
	public void setFilecreatedon(Date filecreatedon) {
		this.filecreatedon = filecreatedon;
	}
	
	public long getFilesize() {
		return filesize;
	}
	public void setFilesize(long l) {
		this.filesize = l;
	}
	
	public String getFileformat() {
		return fileformat;
	}
	public void setFileformat(String fileformat) {
		this.fileformat = fileformat;
	}
	
	public Files getFileversion() {
		return fileversion;
	}
	public void setFileversion(Files fileversion) {
		this.fileversion = fileversion;
	}
	
	public Folder getFolderid() {
		return folderid;
	}
	public void setFolderid(Folder folderid) {
		this.folderid = folderid;
	}
	
    
    
	
}

