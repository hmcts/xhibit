This code has been extracted from openjms-0.7.7-alpha-3 for use in Xhibit. It has been cut 
back to reduce the risk of version conflicts with Weblogic.

One change had to be made to allow it to be used with Antlr-2.7.5. The permission on the method 
org.exolab.jms.selector.parser.SelectorAST.create(java.lang.String) had to be changed from 
protected to public due to a change in the base class.

The dependency in org.exolab.jms.selector.LikeExpression and org.exolab.jms.selector.RegexpFactory 
on jakarta-oro has been removed, it now uses the java.util.regex package.