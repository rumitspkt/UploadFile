package com.rum.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.rum.GGDrive.Quickstart;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ListFile", urlPatterns = {"listFile"}) 
public class ListFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Drive service = Quickstart.getDriveService();

        // Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
             .setMaxResults(20)
             .execute();
        List<File> files = result.getItems();
        /*if (files == null || files.size() == 0) {
            out.println("No files found.");
        } else {
            out.println("Files:");
            for (File file : files) {
                out.println(file.getTitle() + " " + file.getId());
            }
        }*/
        request.setAttribute("files", files);
        request.getRequestDispatcher("views/ListFile.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response);
    }
}