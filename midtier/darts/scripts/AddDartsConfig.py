from java.util import *

import os

print '*******************************************************************************'
print '*  Add DARTS config v0.1                                                      *'
print '*                                                                             *'
print '*******************************************************************************'

print 'Starting...'

# Debug - print the environment variables
"""
print 'Environment:'
for param in os.environ.keys():
	print "%20s %s" % (param,os.environ[param])
"""

# Example connection params - NB needed to use actual hostname, localhost didn't work...
username = 'weblogic'
password = 'password'
adminurl = 't3://xdevxpj:7001'

# Arguments
"""
for arg in sys.argv:
	print arg
"""
arglength = len(sys.argv)
if arglength<4 :
	print """
		Usage: 
		AddDartsConfig.py <username> <password> <adminurl>
		e.g. 'AddDartsConfig.py weblogic password t3://xdevxpj:7001'
	"""
	sys.exit()

arg = sys.argv
username = arg[1]
password = arg[2]
adminurl = arg[3]

# Connect to specified Weblogic server
connect(username,password,adminurl)

# Enter configuration edit mode
edit()
startEdit()

#Check if DARTS stuff is already set up, and exit with warning if so
dartsbean = getMBean("JMSSystemResources/Xhibit-DARTS-JMSModule")
if dartsbean != None :
	print """
	***
	*** ERROR: DARTS configuration appears to be set up already!
	***
	"""
	cancelEdit('y')
	disconnect()	
	exit()
else:
	print "Ready to add DARTS config..."

# Debug: show all the types that you could create at the moment
#print cmo #cmo = Current Management Object
#listChildTypes()

# Get hold of the cluster config
servermb = getMBean("Clusters/midcluster")

# Add the JMS stuff...
resource = create("Xhibit-DARTS-JMSModule","JMSSystemResource")
resource.addTarget(servermb)
jmsResource = resource.getJMSResource()

# Add DartsConnectionFactory
connfac = jmsResource.createConnectionFactory("DartsMessageConnectionFactory")
connfac.setJNDIName("DartsMessageConnectionFactory")
#connfac.setSubDeploymentName("midM1-JMSServer-Subdeployment")

subdeployment = resource.createSubDeployment("midM1-JMSServer-Subdeployment")
jmsserver = getMBean("JMSServers/midM1-JMSServer")
subdeployment.addTarget(jmsserver)

# Add Darts queue
queue = jmsResource.createUniformDistributedQueue("DartsMessageOutboundQueue")
queue.setJNDIName("jms/darts/DartsMessageOutboundQueue")
queue.setSubDeploymentName("midM1-JMSServer-Subdeployment")

# Add Darts topic
topic = jmsResource.createTopic("DartsMessageOutboundTopic")
topic.setJNDIName("jms/darts/DartsMessageOutboundTopic")
topic.setSubDeploymentName("midM1-JMSServer-Subdeployment")

# Add Darts database
# Actually I think Craig said he wanted to add this manually, so put it in the script...

# Go/NoGo decision
decision = raw_input("""
	*** Happy to commit all this stuff? (yes|no, default=no)
	""")
if decision=='yes':
	print 'OK, saving it'
	# Validate, save and activate config changes
	validate()
	save()
	activate()
	restart = isRestartRequired()
else:
	print 'OK, rolling back'
	# Roll back changes
	cancelEdit('y')

# Tidy up
disconnect()
exit()

print 'Finished.'