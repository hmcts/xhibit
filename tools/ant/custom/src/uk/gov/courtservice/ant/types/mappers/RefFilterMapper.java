/*
 * Copyright 2004-2005 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package uk.gov.courtservice.ant.types.mappers;

import java.io.Reader;
import java.io.StringReader;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.filters.util.ChainReaderHelper;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;

/**
 * This is a FileNameMapper based on FilterMapper available in later versions of
 * ANT. Note later versions of ant have a FilterMapper based on a FilterChain.
 * This is not possible with this version of ant as FilterChain is final.
 */
public class RefFilterMapper extends ProjectComponent implements FileNameMapper {

	private String filterChainRefId;

	/**
	 * From the reference to the filterchain to use
	 * 
	 * @param from
	 *            a string
	 * @throws BuildException
	 *             always
	 */
	public void setFrom(String from) {
		if (filterChainRefId != null) {
			throw new BuildException("Filter chain has allready been set.");
		}
		filterChainRefId = from;
	}

	private FilterChain getFilterChain() {
		// Get the referenced filter chain
		Object object = getProject().getReference(filterChainRefId);
		if (object instanceof FilterChain) {
			return (FilterChain) object;
		}
		throw new BuildException("The reference \"" + filterChainRefId
				+ "\" doesn\'t refer to a FilterChain");
	}

	/**
	 * From attribute not supported.
	 * 
	 * @param to
	 *            a string
	 * @throws BuildException
	 *             always
	 */
	public void setTo(String to) {
		if (to != null) {
			throw new BuildException(
					"Unsupported Attribute \"to\" has been set to \"" + to
							+ "\".");
		}
	}

	/**
	 * Return the result of the filters on the sourcefilename.
	 * 
	 * @param sourceFileName
	 *            the filename to map
	 * @return a one-element array of converted filenames, or null if the
	 *         filterchain returns an empty string.
	 */
	public String[] mapFileName(String sourceFileName) {
		try {
			Reader stringReader = new StringReader(sourceFileName);
			ChainReaderHelper helper = new ChainReaderHelper();
			helper.setBufferSize(8192);
			helper.setPrimaryReader(stringReader);
			helper.setProject(getProject());
			Vector filterChains = new Vector();
			filterChains.add(getFilterChain());
			helper.setFilterChains(filterChains);
			String result = FileUtils.readFully(helper.getAssembledReader());
			if (result == null || result.length() == 0) {
				log("Mapped from \"" + sourceFileName+ "\" + to null.", Project.MSG_VERBOSE);
				return null;
			} else {
				log("Mapped from \"" + sourceFileName+ "\" + to \"" + result + "\".", Project.MSG_VERBOSE);
				return new String[] { result };
			}
		} catch (BuildException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new BuildException(ex);
		}
	}
}
