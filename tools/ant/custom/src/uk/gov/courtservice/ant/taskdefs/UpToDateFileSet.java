package uk.gov.courtservice.ant.taskdefs;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

/**
 * Task that works like uptodate but works on the contents of 2 file sets src
 * and dest. The property is set to true if all the files in the dest fileset
 * are newer than all the files in the src fileset.
 * 
 * Author: William Fardell, Xdevelopment (2004)
 */
public class UpToDateFileSet extends Task implements Condition {
    private Path srcPath;

    private Path destPath;

    private String property;

    private String value;

    // Src FileSet Element
    public void addSrcfiles(FileSet fs) {
        if (srcPath == null) {
            srcPath = new Path(getProject());
        }
        srcPath.addFileset(fs);
    }

    // Dest FileSet Element
    public void addDestfiles(FileSet fs) {
        if (destPath == null) {
            destPath = new Path(getProject());
        }
        destPath.addFileset(fs);
    }

    // Src Path Attribute
    public void setSrcpath(Path path) {
        addSrcpath(path);
    }

    // Dest Path Attribute
    public void setDestpath(Path path) {
        addDestpath(path);
    }

    // Src Path Element
    public void addSrcpath(Path path) {
        if (srcPath == null) {
            srcPath = path;
        } else {
            srcPath.append(path);
        }
    }

    // Dest Path Element
    public void addDestpath(Path path) {
        if (destPath == null) {
            destPath = path;
        } else {
            destPath.append(path);
        }
    }

    // Src Path Ref Attribute
    public void setSrcpathref(Reference srcpathref) {
        if (srcPath == null) {
            srcPath = new Path(getProject());
            srcPath.setRefid(srcpathref);
        } else {
            srcPath.createPath().setRefid(srcpathref);
        }
    }

    // Dest Path Ref Attribute
    public void setDestpathref(Reference destpathref) {
        if (destPath == null) {
            destPath = new Path(getProject());
            destPath.setRefid(destpathref);
        } else {
            destPath.createPath().setRefid(destpathref);
        }

    }

    // Property Attribute
    public void setProperty(String property) {
        this.property = property;
    }

    // Value Attribute
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Sets property to true if target file(s) have a more recent timestamp than
     * (each of) the corresponding source file(s).
     */
    public void execute() throws BuildException {
        if (property == null) {
            throw new BuildException("property attribute is required.", getLocation());
        }
        if (eval()) {
            if (value == null) {
                log("Setting property \"" + property + " to \"true\".", Project.MSG_VERBOSE);
                getProject().setNewProperty(property, "true");
            } else {
                log("Setting property \"" + property + " to \"" + value + "\".", Project.MSG_VERBOSE);
                getProject().setNewProperty(property, value);
            }
        }
    }

    /**
     * Condition Implementation
     * 
     * @return true if all the files in the dest fileset are newer than the
     *         files in the src fileset.
     */
    public boolean eval() {
        // check state
        if (srcPath == null) {
            throw new BuildException("The src files have not been specified.", getLocation());
        }
        if (destPath == null) {
            throw new BuildException("The dest files have not been specified.", getLocation());
        }

        // determine result
        boolean result;

        String[] srcFiles = srcPath.list();
        if (srcFiles.length == 0) {
            log("No src files found.", Project.MSG_VERBOSE);
            result = true;
        } else {
            String[] destFiles = destPath.list();
            if (destFiles.length == 0) {
                log("No dest files found.", Project.MSG_VERBOSE);
                result = false;
            } else {
                result = getMinTimestamp(destFiles) >= getMaxTimestamp(srcFiles);
            }
        }

        // log result and return
        if (result) {
            log("Dest files are up to date.", Project.MSG_VERBOSE);
        } else {
            log("Dest files are not up to date.", Project.MSG_VERBOSE);
        }

        return result;
    }

    private long getMinTimestamp(String[] names) {
        long timestamp = Long.MAX_VALUE;
        for (int i = 0; i < names.length; i++) {
            log("Checking file \"" + names[i] + "\".", Project.MSG_VERBOSE);
            File file = new File(names[i]);
            if (file.exists() && file.lastModified() < timestamp) {
                timestamp = file.lastModified();
            }
        }
        log("Min timestamp " + timestamp + ".", Project.MSG_VERBOSE);
        return timestamp;

    }

    private long getMaxTimestamp(String[] names) {
        long timestamp = 0;
        for (int i = 0; i < names.length; i++) {
            log("Checking file \"" + names[i] + "\".", Project.MSG_VERBOSE);
            File file = new File(names[i]);
            if (file.exists() && file.lastModified() > timestamp) {
                timestamp = file.lastModified();
            }
        }
        log("Max timestamp " + timestamp + ".", Project.MSG_VERBOSE);
        return timestamp;
    }
}
