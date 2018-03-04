package com.rum.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.rum.GGDrive.Quickstart;


import java.io.*;
import java.util.*;



@WebServlet(name = "UploadFile", urlPatterns = {"uploadFile"}) 
public class UploadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.getWriter().println("Access denied!!!");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    	Drive service = Quickstart.getDriveService();
    	PrintWriter out = response.getWriter();
    	boolean isMultipart;
    	String filePath;
    	int maxFileSize = 5000 * 1024;
    	int maxMemSize = 5 * 1024;
    	filePath = "E:\\LocalFile";
    	isMultipart = ServletFileUpload.isMultipartContent(request);
    	if(!isMultipart) {
    		//response file not uploaded
    		out.println("failed!!");
    		return;
    	}
    	DiskFileItemFactory factory = new DiskFileItemFactory();
    	factory.setSizeThreshold(maxFileSize);
    	factory.setRepository(new java.io.File("C:\\temp"));
    	ServletFileUpload upload = new ServletFileUpload(factory);
    	upload.setSizeMax(maxFileSize);
    	java.io.File file = null;
    	try {
    		List fileItems = upload.parseRequest(request);
    		Iterator i = fileItems.iterator();
    		while(i.hasNext()) {
    			FileItem fi = (FileItem) i.next();
    			if(!fi.isFormField()) {
    				String fieldName = fi.getFieldName();
    				String fileName = fi.getName();
    				String contentType = fi.getContentType();
    				boolean isInMemory = fi.isInMemory();
    				long sizeInBytes = fi.getSize();
    				if(fileName.lastIndexOf("\\") >= 0) {
    					file = new java.io.File(filePath + "\\" + fileName.substring(fileName.lastIndexOf("\\")));
    				}else {
    					file = new java.io.File(filePath + "\\" + fileName.substring(fileName.lastIndexOf("\\") + 1));
    				}
    				fi.write(file);
    				
    				File fileMetadata = new File();
    		    	fileMetadata.setTitle(fileName);
    		    	//java.io.File filePath = new java.io.File("C:\\Users\\RumIT\\eclipse-workspace\\UploadWeb\\src\\main\\webapp\\files\\photo.jpg");
    		    	FileContent mediaContent = new FileContent(contentType, file);
    		    	System.out.println(fileName);
    		    	File fileGoogle = service.files().insert(fileMetadata, mediaContent).setFields("id").execute();
    		    	//out.println("File ID: " + fileGoogle.getId());
    				//out.println(file.getAbsolutePath());
    		    	String linkDownload = "https://drive.google.com/open?id=" + fileGoogle.getId();
    		    	request.setAttribute("linkDownload", linkDownload);
    		    	request.getRequestDispatcher("views/UploadFile.jsp").forward(request, response);
    			}
    		}
    	} catch(Exception e) {
    		out.println("failed");
    	}
    	
    	   
    	
    	/*Drive service = Quickstart.getDriveService();
    	File fileMetadata = new File();
    	fileMetadata.setTitle("photo.jpg");
    	java.io.File filePath = new java.io.File("C:\\Users\\RumIT\\eclipse-workspace\\UploadWeb\\src\\main\\webapp\\files\\photo.jpg");
    	FileContent mediaContent = new FileContent("image/jpg", filePath);
    	System.out.println(filePath.getName());
    	
    	File file = service.files().insert(fileMetadata, mediaContent).setFields("id").execute();
    	out.println("File ID: " + file.getId()); */
    	
    	
    	
    	
        
    }
}