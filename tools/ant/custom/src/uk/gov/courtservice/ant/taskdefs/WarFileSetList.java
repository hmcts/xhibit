package uk.gov.courtservice.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.War;
import org.apache.tools.ant.types.ZipFileSet;

import uk.gov.courtservice.ant.types.FileSetList;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: WarFileSetList.java,v 1.6 2006/06/28 11:05:07 bzjrnl Exp $
 */

public class WarFileSetList extends War {

    private FileSetList _fileSets;

    private FileSetList _classesFileSets;

    private FileSetList _libFileSets;

    public void addFileSetList(FileSetList p) {
        if (_fileSets != null)
            throw new BuildException("You can only specify one source fileset per war");

        _fileSets = p;
    }

    public void addClassesFileSetList(FileSetList p) {
        if (_classesFileSets != null)
            throw new BuildException("You can only specify one fileset group for classes");

        _classesFileSets = p;
    }

    public void addLibFileSetList(FileSetList p) {
        if (_libFileSets != null)
            throw new BuildException("You can only specify one fileset group for lib");

        _libFileSets = p;
    }

    public void execute() throws org.apache.tools.ant.BuildException {
        if (_fileSets != null) {
            if (_fileSets.isReference())
                _fileSets = _fileSets.getCheckedRef();

            ZipFileSet[] elements = _fileSets.getElements();
            for (int i = 0; i < elements.length; i++) {
                addFileset(elements[i]);
            }
        }
        if (_classesFileSets != null) {
            if (_classesFileSets.isReference())
                _classesFileSets = _classesFileSets.getCheckedRef();

            ZipFileSet[] elements = _classesFileSets.getElements();
            for (int i = 0; i < elements.length; i++) {
                addClasses(elements[i]);
            }
        }
        if (_libFileSets != null) {
            if (_libFileSets.isReference())
                _libFileSets = _libFileSets.getCheckedRef();

            ZipFileSet[] elements = _libFileSets.getElements();
            for (int i = 0; i < elements.length; i++) {
                addLib(elements[i]);
            }
        }

        super.execute();
    }
}