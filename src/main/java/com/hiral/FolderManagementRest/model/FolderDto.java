package com.hiral.FolderManagementRest.model;

import java.util.Date;

public class FolderDto {

    private Integer id;

    private String foldername;
    
    private Date foldercreatedon;

    private Integer parentid;

	private String parentfoldername;

    public FolderDto()
    {
    }

    public FolderDto( Folder folder )
    {
        id = folder.getId();
        foldername = folder.getFoldername();
        foldercreatedon = folder.getFoldercreatedon();
        if( folder.getParentid() != null )
        {
            parentid = folder.getParentid().getId();
            parentfoldername = folder.getParentid().getFoldername();
        }
    }
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFoldername() {
		return foldername;
	}

	public void setFoldername(String foldername) {
		this.foldername = foldername;
	}

	public Integer getParentid() {
		return parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public Date getFoldercreatedon() {
		return foldercreatedon;
	}

	public void setFoldercreatedon(Date foldercreatedon) {
		this.foldercreatedon = foldercreatedon;
	}

	public String getParentfoldername() {
		return parentfoldername;
	}

	public void setParentfoldername(String parentfoldername) {
		this.parentfoldername = parentfoldername;
	}
    
}
