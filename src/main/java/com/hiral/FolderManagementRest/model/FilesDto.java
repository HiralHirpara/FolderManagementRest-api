package com.hiral.FolderManagementRest.model;

import java.util.Date;

public class FilesDto {

    private Integer id;
    
    private String filename;
	
    private Date filecreatedon;
    
    private long filesize;
    
    private String fileformat;
    
    private Integer fileversionid;
   
    private String fileversionname;

    private Integer folderid;

	private String foldername;
	
	

    public FilesDto()
    {
    }

    public FilesDto( Files files )
    {
        id = files.getId();
        filename = files.getFilename();
        filecreatedon = files.getFilecreatedon();
        filesize = files.getFilesize();
        fileformat = files.getFileformat();
        
       
        if( files.getFileversion() != null )
        {
        	fileversionid = files.getFileversion().getId();
        	fileversionname = files.getFileversion().getFilename();
        }
        if( files.getFolderid() != null )
        {
        	folderid = files.getFolderid().getId();
        	foldername = files.getFolderid().getFoldername();
        }
    }

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

	public Date getFilecreatedon() {
		return filecreatedon;
	}

	public void setFilecreatedon(Date filecreatedon) {
		this.filecreatedon = filecreatedon;
	}

	public long getFilesize() {
		return filesize;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	public String getFileformat() {
		return fileformat;
	}

	public void setFileformat(String fileformat) {
		this.fileformat = fileformat;
	}

	public Integer getFileversionid() {
		return fileversionid;
	}

	public void setFileversionid(Integer fileversionid) {
		this.fileversionid = fileversionid;
	}

	public String getFileversionname() {
		return fileversionname;
	}

	public void setFileversionname(String fileversionname) {
		this.fileversionname = fileversionname;
	}

	public Integer getFolderid() {
		return folderid;
	}

	public void setFolderid(Integer folderid) {
		this.folderid = folderid;
	}

	public String getFoldername() {
		return foldername;
	}

	public void setFoldername(String foldername) {
		this.foldername = foldername;
	}
    
}
