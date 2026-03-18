package uk.gov.courtservice.ant.taskdefs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;

/**
 * <p>
 * Title: Merge Files
 * </p>
 * <p>
 * Description: This class provides an ant task to merge sets of properties
 * files
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.3 $
 * 
 */

public class Merge extends MatchingTask {

    private String destination;

    protected ArrayList filesets = new ArrayList();

    private HashMap filesMap = new HashMap();

    /**
     * Adds a set of files to be copied to the new direstory.
     */

    public void addFileset(FileSet set) {
        filesets.add(set);
    }

    // The setter for the "message" attribute
    public void setDestination(String destination) {
        this.destination = destination;
    }

    // The ant command to execute the task
    public void execute() throws BuildException {

        setUpFilesToMerge();
        checkDestinationDir();

        Iterator i = filesMap.keySet().iterator();

        while (i.hasNext()) {
            String file = (String) i.next();
            ArrayList filesToMerge = (ArrayList) filesMap.get(file);
            if (filesToMerge.size() > 1) {
                log("Merging file : " + file + " , number of files to merge : " + filesToMerge.size(),
                        Project.MSG_VERBOSE);
                mergeFiles(filesToMerge);
            } else {
                log("Copying file : " + file, Project.MSG_VERBOSE);
                File theFile = (File) filesToMerge.get(0);
                copyFile(theFile);
            }

        }

    }

    // create a lists in the map of files to merge

    public void setUpFilesToMerge() {
        Project project = getProject();
        for (int i = 0; i < filesets.size(); i++) {
            FileSet fs = (FileSet) filesets.get(i);
            log("File set dir : " + fs.getDir(project).getPath(), Project.MSG_VERBOSE);
            DirectoryScanner ds = fs.getDirectoryScanner(project);
            String[] files = ds.getIncludedFiles();
            for (int j = 0; j < files.length; j++) {
                log("File : " + files[j], Project.MSG_VERBOSE);
                File file = new File(fs.getDir(project).getPath() + "/" + files[j]);
                if (file.exists()) { // should do . . .
                    if (filesMap.get(files[j]) == null) {
                        ArrayList filesToMerge = new ArrayList();
                        filesToMerge.add(file);
                        filesMap.put(files[j], filesToMerge);
                    } else {
                        ArrayList filesToMerge = (ArrayList) filesMap.get(files[j]);
                        filesToMerge.add(file);
                    }
                }
            }
        }

    }

    // creates the destination directory if one does not exist
    public void checkDestinationDir() {
        log("Destination dir : " + destination, Project.MSG_VERBOSE);
        File destinationDir = new File(destination);
        if (!destinationDir.exists() || !destinationDir.isDirectory()) {
            if (!destinationDir.mkdir()) {
                throw new BuildException(
                        "Error creating destination directory,  destinationDir.mkdir() returned false!");
            }
        }
    }

    public void mergeFiles(ArrayList filesToMerge) {
        try {
            File theFile = (File) filesToMerge.get(0);
            FileOutputStream fos = new FileOutputStream(destination + "/" + theFile.getName());
            Iterator j = filesToMerge.iterator();
            while (j.hasNext()) {
                theFile = (File) j.next();
                FileInputStream fis = new FileInputStream(theFile);
                byte[] bytes = new byte[1024];
                int read = fis.read(bytes);
                while (read != -1) {
                    fos.write(bytes, 0, read);
                    read = fis.read(bytes);
                }
                fis.close();
            }
            fos.flush();
            fos.close();
        } catch (IOException e) {
            throw new BuildException("Error copying file,  " + e.getMessage());
        }
    }

    public void copyFile(File theFile) {
        try {
            FileInputStream fis = new FileInputStream(theFile);
            FileOutputStream fos = new FileOutputStream(destination + "/" + theFile.getName());
            byte[] bytes = new byte[1024];
            int read = fis.read(bytes);
            while (read != -1) {
                fos.write(bytes, 0, read);
                read = fis.read(bytes);
            }
            fis.close();
            fos.flush();
            fos.close();
        } catch (IOException e) {
            throw new BuildException("Error copying file,  " + e.getMessage());
        }
    }

}
