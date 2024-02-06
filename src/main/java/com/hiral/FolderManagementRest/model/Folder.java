package com.hiral.FolderManagementRest.model;

import java.io.Serializable;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "folder")
public class Folder implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue
    private Integer id;
    
    @Column(name = "foldername")
    private String foldername;
    
    @Column(name = "foldercreated_on")
    private Date foldercreatedon;
    
    @ManyToOne()
    private Folder parentid;

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

	public Date getFoldercreatedon() {
		return foldercreatedon;
	}

	public void setFoldercreatedon(Date foldercreatedon) {
		this.foldercreatedon = foldercreatedon;
	}

	public Folder getParentid() {
		return parentid;
	}

	public void setParentid(Folder parentid) {
		this.parentid = parentid;
	}
    

	 
}
