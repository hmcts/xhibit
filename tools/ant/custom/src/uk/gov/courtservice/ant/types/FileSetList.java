package uk.gov.courtservice.ant.types;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.ZipFileSet;

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
 * @version $Id: FileSetList.java,v 1.1 2006/06/28 11:05:11 bzjrnl Exp $
 */

public class FileSetList extends DataType {

    ArrayList al = new ArrayList();

    ArrayList alFileSetLists = new ArrayList();

    public void addFileSet(ZipFileSet fs) {
        al.add(fs);
    }

    public void addFileSetList(FileSetList fs) {
        alFileSetLists.add(fs);
    }

    public FileSetList getCheckedRef() {
        return (FileSetList) super.getCheckedRef(FileSetList.class, "FileSetList");
    }

    public ZipFileSet[] getElements() {
        Iterator iter = alFileSetLists.iterator();
        while (iter.hasNext()) {
            FileSetList fsList = (FileSetList) iter.next();
            if (fsList.isReference())
                fsList = fsList.getCheckedRef();
            al.addAll(fsList.getFileSetArray());
        }
        return (ZipFileSet[]) al.toArray(new ZipFileSet[al.size()]);
    }

    protected ArrayList getFileSetArray() {
        return al;
    }
}