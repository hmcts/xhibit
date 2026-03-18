Created by Kelvin Davies - 29/04/2009

When creating a New Mercatopr MVO file, the following files will require updating:
- MercatorBody.xsd
	This xsd file contains the elements/fields for the new MVO file.

- MercatorRequest.xsd
	This xsd ensures the MVO file can be used by a request to Mercator.

-../../config/castor-binding.xml
	This file defines the MVO's as common types.

-/support/config/src/config/componentss/integration.mercatorwrap.properties
	This file needs to be updated to include the reference to the coresponding mercator map.