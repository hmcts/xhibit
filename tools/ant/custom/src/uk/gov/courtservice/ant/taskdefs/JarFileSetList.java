package uk.gov.courtservice.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Jar;
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
 * @version $Id: JarFileSetList.java,v 1.5 2006/06/28 11:05:07 bzjrnl Exp $
 */

public class JarFileSetList extends Jar {
    private FileSetList _fileSets;

    public void addFileSetList(FileSetList p) {
        if (_fileSets != null)
            throw new BuildException("You can only specify one source fileset per jar");

        _fileSets = p;
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

        super.execute();
    }

}