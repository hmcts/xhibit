/**
 * Created by IntelliJ IDEA.
 * User: qzd3k3
 * Date: May 14, 2003
 * Time: 12:12:15 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.skeletonschedule.print;

import uk.gov.courtservice.xhibit.client.print.helper.AbstractPrintHelper;

/**
 * <p>
 * Title: Order Display Helper
 * </p>
 * <p>
 * Description: Generates the Orders Right Hand Panel as a JPanel. This performs
 * a FOP transform on a DOM, using a streamed XSLT ffile
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003</p
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class WitnessPrintHelper extends AbstractPrintHelper// implements
// PropertyChangeListener
{
    //
    // // OrderPreviewPanel frame;
    // Document dom;
    // // private OrderData data;
    //
    // public static final String DISPLAY_MODE = "display";
    //
    // private static final String BASE_IMAGE_URL = "/resource.txt";
    //
    //
    // /**
    // * Constructor using an OrderData object and streamed XSL
    // * @param xmlIs XML
    // * @param xslIs InputStream - streamed XSL file
    // * @throws FOPException
    // */
    // public WitnessPrintHelper(InputStream xmlIs, InputStream xslIs)
    // throws FOPException
    // {
    // // XMLOrderData xmlOrderData = (XMLOrderData) order;
    // // xmlOrderData.addPropertyChangeListener(this);
    // // dom = new XMLDocument();
    // try
    // {
    // transformer = TransformerFactory.newInstance().newTransformer(new
    // StreamSource(xslIs));
    // }
    // catch (TransformerException te)
    // {
    // throw new FOPException(te);
    // }
    // setupResources();
    //
    // renderer = new AWTRenderer(resource);
    // // frame = createPreviewDialog(renderer, resource);
    // // renderer.setComponent(frame);
    //
    // driver = getDriver(this.renderer);
    // try
    // {
    // loadTransform(dom);
    // }
    // catch (FOPException fe)
    // {
    // throw new FOPException(fe);
    // }
    // //Only start refresh timer when everything else is ready.
    // timer.start();
    // }
    //
    //
    // /**
    // * Constructor using a Document object and streamed XSL
    // * @param order Document
    // * @param is InputStream - streamed XSL file
    // * @throws OrderTransformException
    // */
    // public OrderDisplayHelper(Document order, InputStream is) throws
    // OrderTransformException
    // {
    // dom = order;
    // try
    // {
    // transformer = TransformerFactory.newInstance().newTransformer(new
    // StreamSource(is));
    // }
    // catch (TransformerException te)
    // {
    // throw new OrderTransformException(te);
    // }
    // setupResources();
    //
    // renderer = new AWTRenderer(resource);
    // frame = createPreviewDialog(renderer, resource);
    //
    // try
    // {
    // loadTransform(dom);
    // }
    // catch (FOPException fe)
    // {
    // throw new OrderTransformException(fe);
    // }
    // //Only start refresh timer when everything else is ready.
    // timer.start();
    // }
    //
    // /**
    // * Creates an OrderPreviewPanel containing the AWT rendering of the
    // FOP document
    // * @param renderer AWTRenderer to display the transform
    // * @param res Translator - populated from FOP resource bundles
    // * @return OrderPreviewPanel
    // */
    // protected OrderPreviewPanel createPreviewDialog(AWTRenderer renderer,
    // Translator res)
    // {
    // OrderPreviewPanel frame = new OrderPreviewPanel(renderer, res);
    // frame.validate();
    //
    // // center window
    // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    // Dimension frameSize = frame.getSize();
    // if (frameSize.height > screenSize.height)
    // frameSize.height = screenSize.height;
    // if (frameSize.width > screenSize.width)
    // frameSize.width = screenSize.width;
    // frame.setLocation((screenSize.width - frameSize.width) / 2,
    // (screenSize.height - frameSize.height) / 2);
    // frame.setVisible(true);
    // return frame;
    // }
    //
    // /**
    // * Transforms the DOM using FOP
    // */
    // public void loadTransform(Document dom) throws FOPException
    // {
    // setUpRenderer();
    // try
    // {
    // setFOPBaseDir();
    // transformer.setParameter("mode", DISPLAY_MODE);
    // transformer.transform(new DOMSource(dom), new
    // SAXResult(driver.getContentHandler()));
    // }
    // catch (TransformerException te)
    // {
    // throw new FOPException("Transformer Exception", te);
    // }
    //
    // frame.showPage(this.renderer);
    // driver.reset();
    // transformer.clearParameters();
    // }
    //
    //
    // /**
    // * Property change listener added to the DOM. Updates the display
    // whenever
    // * DOM is updated
    // * @param pce PropertyChangeEvent
    // */
    // public void propertyChange(PropertyChangeEvent pce)
    // {
    // this.data = (OrderData) pce.getSource();
    // this.timer.update();
    // }
    //
    //
    // /**
    // * Produces an error if the TimerThread invoked loadTransform
    // * generates an exception
    // * @param e Exception
    // */
    // public void displayCriticicalFailure(Exception e)
    // {
    // log.error("Critical Transform Error" + e.getMessage());
    // e.printStackTrace();
    // }
    //
    //
    // /**
    // * Returns the scale ComboBox from the panel
    // * @return
    // */
    // public JComboBox getScale()
    // {
    // return frame.getScale();
    // }
    //
    //
    // /**
    // * Resets the renderer betweeen transforms. If we don't do this we
    // have to
    // * create a new AWTRenderer for each transform - with obvious
    // performance
    // * implications
    // */
    // private void setUpRenderer()
    // {
    // // Clean up the renderer - ensures that the panel redisplays
    // while (renderer.getPageCount() != 0)
    // renderer.removePage(0);
    //
    // //reset the scale factor
    // this.renderer.setScaleFactor(new Double((String)
    // getScale().getSelectedItem()).doubleValue());
    //
    // this.timer.update();
    // }
    //
    //
    // /**
    // * Returns the Document object to refresh the display
    // * @return Document
    // */
    // public Document getDOM()
    // {
    // return dom;
    // }
    //
    //
    // /**
    // * Returns the OrderPreviewPanel as a JPanel
    // * @return JPanel
    // */
    // public JPanel getDisplayPanel()
    // {
    // return (JPanel) frame;
    // }
    //
    // /**
    // * Stop refresh of preview pane, normally used while
    // saving/printing/signing.
    // * While saving we need to remove the narrative node, therefore we
    // cannot redisplay.
    // */
    // public void suspendRedisplay()
    // {
    // this.timer.suspendThread();
    // }
    //
    // /**
    // * Resume refresh of preview pane, normally used while
    // saving/printing/signing.
    // * While saving we need to remove the narrative node, therefore we
    // cannot redisplay
    // * until narrative node returned..
    // */
    // public void resumeRedisplay()
    // {
    // this.timer.resumeThread();
    // }
    //
    // /**
    // * FOP requires a url to lookup an image. The root url is passed to
    // the transform to retrieve
    // * the Court Service logo
    // */
    // private void setFOPBaseDir() {
    // URL url = this.getClass().getResource(BASE_IMAGE_URL);
    //
    // if (url != null)
    // {
    // log.debug("$$$ BASE URL " + url.toString() + " $$$");
    // String baseDir = url.toString();
    // int endPoint = baseDir.lastIndexOf("/");
    // baseDir = baseDir.substring(0, endPoint);
    // log.debug("$$$ Reduced BASEDIR " + baseDir + " $$$");
    // transformer.setParameter("basedir", baseDir);
    // }
    // }
}
