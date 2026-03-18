<?xml version="1.0" encoding="UTF-8"?>
<structure version="2" schemafile="C:\Documents and Settings\EDS\jbproject\Prototype\xml\schemas\Bail Order.xsd" workingxmlfile="C:\Documents and Settings\EDS\jbproject\Prototype\xml\exampleXML\Bail Order.xml" templatexmlfile="">
	<nspair prefix="n1" uri="http://www.courtservice.gov.uk/schemas/courtservice"/>
	<nspair prefix="bs7666" uri="http://www.govtalk.gov.uk/people/bs7666"/>
	<nspair prefix="xs" uri="http://www.w3.org/2001/XMLSchema"/>
	<template>
		<match overwrittenxslmatch="/"/>
		<children>
			<template>
				<match match="n1:BailOrder"/>
				<children>
					<paragraph paragraphtag="p">
						<children>
							<newline/>
							<text fixtext="This is my bail order, I am "/>
							<template>
								<match match="n1:BailOrderStructure"/>
								<children>
									<template>
										<match match="n1:CourtOfficer"/>
										<children>
											<template>
												<match match="n1:CitizenNameForename"/>
												<children>
													<xpath allchildren="1"/>
												</children>
											</template>
											<text fixtext=" "/>
											<template>
												<match match="n1:CitizenNameSurname"/>
												<children>
													<xpath allchildren="1"/>
												</children>
											</template>
										</children>
									</template>
								</children>
							</template>
							<newline/>
						</children>
					</paragraph>
				</children>
			</template>
		</children>
	</template>
	<template>
		<match match="n1:BailOrder"/>
		<children>
			<newline/>
			<newline/>
			<text fixtext="This is my Bail Order"/>
			<newline/>
			<newline/>
			<template>
				<match match="n1:BailOrderStructure"/>
				<children>
					<template>
						<match match="n1:CourtHouse"/>
						<children>
							<template>
								<match match="n1:CourtHouseAddress"/>
								<children>
									<template>
										<match match="n1:BS7666Address"/>
										<children>
											<template>
												<match match="bs7666:SAON"/>
												<children>
													<paragraph paragraphtag="p">
														<children>
															<xpath allchildren="1"/>
														</children>
													</paragraph>
												</children>
											</template>
										</children>
									</template>
								</children>
							</template>
						</children>
					</template>
				</children>
			</template>
			<newline/>
			<newline/>
			<newline/>
			<newline/>
		</children>
	</template>
</structure>
