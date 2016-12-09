package libreoffice;

import java.io.File;
import java.nio.file.Files;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;

import com.sun.star.uno.XComponentContext;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.io.IOException;
import com.sun.star.lang.XComponent;
import com.sun.star.sdbc.XCloseable;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XText;

import ooo.connector.BootstrapSocketConnector;

import javax.swing.*;
/**
 *
 * @author hp
 */
public class LibreDesktop {
	public static void copyFile( File from, File to ) throws IOException, java.io.IOException {
	    Files.copy( from.toPath(), to.toPath() );
	}
    
    /** Creates a new instance of OfficeUNOClientApp_Desktop */
    public LibreDesktop() {
    }
    //Method definitions
    /*public static string showfilesaver()
	{
                String file="";
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile().getName();
		  System.out.println(file);
		}
                return file;
	}*/
    
    /**
     * @param args the command line arguments
     */
    public static void  addPresentation( String projectPath, String first,String openOfficePath, String templatePath)
    {
    	//System.out.println("sadasdadjkhkjdsahkjdhkaj");
			Logger logger = Logger.getLogger("OfficeUNOClientApp_Desktop.class.getName()");  
    		FileHandler fh; 
    		try {
    			String path=new File(projectPath,"logger.txt").getAbsolutePath();
				fh = new FileHandler(path);
				logger.addHandler(fh);
	            SimpleFormatter formatter = new SimpleFormatter();  
	            fh.setFormatter(formatter);  
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (java.io.IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  
            
			System.out.println(first);
			try
			{
    			String[] subDirs = projectPath.split(Pattern.quote(File.separator));
    			String newprojectpptpath=new File(projectPath,"presentation").getAbsolutePath();
    			newprojectpptpath=new File(newprojectpptpath,subDirs[subDirs.length-1]).getAbsolutePath();
    	                newprojectpptpath+=".pptx";
    	        System.out.println(newprojectpptpath);
    	        if(first.equals("first"))        
    	    		{launchfirstOfficeInstance(newprojectpptpath,openOfficePath,templatePath);}
    	        else
    	        	{launchOfficeInstance(newprojectpptpath,openOfficePath);}
			}
			catch(NullPointerException e)
			{
				logger.log(Level.SEVERE, e.getMessage(), e);
				e.printStackTrace();
			}

    	/*thread.setDaemon(true);
    	thread.start();
    	try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }
    //public static void main(String[] args) {
        //System.out.println("1");    

	public static void launchfirstOfficeInstance(String pptPath,String openOfficePath, String templatePath) {
		try {
            // get the remote office component context
            //System.out.println("2");
            //String oooExeFolder = "C:/Program Files (x86)/OpenOffice 4/program";
            String oooExeFolder = openOfficePath;
            //XComponentContext xContext = Bootstrap.bootstrap();
            XComponentContext xContext = BootstrapSocketConnector.bootstrap(oooExeFolder);
            //System.out.println("3");
            if (xContext == null) {
                System.err.println("ERROR: Could not bootstrap default Office.");
                
            }
            else{
                System.out.println("xContent not null");
            }    
            com.sun.star.lang.XMultiComponentFactory xMCF = xContext.getServiceManager();
            Object oDesktop = xMCF.createInstanceWithContext("com.sun.star.frame.Desktop", xContext);
            
            com.sun.star.frame.XComponentLoader xCompLoader =
                UnoRuntime.queryInterface(
                 com.sun.star.frame.XComponentLoader.class, oDesktop);
            File from=new File(templatePath);
            File to=new File(pptPath);
            copyFile(from,to);
            pptPath=pptPath.replaceAll("\\\\","/");
            String SaveURL="file:///"+pptPath;
            //XComponent document = xCompLoader.loadComponentFromURL("private:factory/simpress", "_blank", 0, new com.sun.star.beans.PropertyValue[0]);
            //XComponent document = xCompLoader.loadComponentFromURL("file:///C:/Users/hp/Documents/Custom%20Office%20Templates/SpokenTutorial.potx", "_blank", 0, new com.sun.star.beans.PropertyValue[0]);
            XComponent document = xCompLoader.loadComponentFromURL(SaveURL, "_blank", 0, new com.sun.star.beans.PropertyValue[0]);
            // Get the textdocument
            /*XTextDocument aTextDocument = ( XTextDocument )UnoRuntime.queryInterface(com.sun.star.text.XTextDocument.class, document);
            
            // Get its text
            XText xText = aTextDocument.getText();*/
            //String URL=showfilesaver();
            
            try {
                Thread.sleep(3000);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            
            
            //String SaveURL="file:///C:/Users/hp/Documents/trial1.pptx";
            
            /*System.out.println(SaveURL);
            com.sun.star.frame.XStorable xStorable =
                (com.sun.star.frame.XStorable)UnoRuntime.queryInterface(
                    com.sun.star.frame.XStorable.class, document ); 
            com.sun.star.beans.PropertyValue propertyValue[] = new com.sun.star.beans.PropertyValue[ 2 ];
           	propertyValue[0] = new com.sun.star.beans.PropertyValue();
            propertyValue[0].Name = "Overwrite";
            propertyValue[0].Value = new Boolean(true);
            propertyValue[1] = new com.sun.star.beans.PropertyValue();
            propertyValue[1].Name = "FilterName";
            //propertyValue[1].Value = "StarOffice XML (Impress)";
            propertyValue[1].Value = "MS PowerPoint 97 Vorlage";
            xStorable.storeToURL(SaveURL , propertyValue );
            
            XCloseable xClose = (XCloseable)UnoRuntime.queryInterface(XCloseable.class, document);
            xClose.close();
             document = xCompLoader.loadComponentFromURL(SaveURL, "_blank", 0, new com.sun.star.beans.PropertyValue[0]);
            //System.out.println("Hello");*/
        }

        catch (Exception e){
            e.printStackTrace();
        }

        finally {
        	
        }
	}
    
	
	public static void launchOfficeInstance(String pptPath, String openOfficePath) {
		try {
            // get the remote office component context
            //System.out.println("2");
            //String oooExeFolder = "C:/Program Files (x86)/OpenOffice 4/program";
            String oooExeFolder = openOfficePath;
            //XComponentContext xContext = Bootstrap.bootstrap();
            XComponentContext xContext = BootstrapSocketConnector.bootstrap(oooExeFolder);
            //System.out.println("3");
            if (xContext == null) {
                System.err.println("ERROR: Could not bootstrap default Office.");
                
            }
            else{
                System.out.println("xContent not null");
            }    
            com.sun.star.lang.XMultiComponentFactory xMCF = xContext.getServiceManager();
            Object oDesktop = xMCF.createInstanceWithContext("com.sun.star.frame.Desktop", xContext);
            
            com.sun.star.frame.XComponentLoader xCompLoader =
                UnoRuntime.queryInterface(
                 com.sun.star.frame.XComponentLoader.class, oDesktop);
            
                        //String SaveURL="file:///C:/Users/hp/Documents/trial1.pptx";
            pptPath=pptPath.replaceAll("\\\\","/");
            String SaveURL="file:///"+pptPath;
            System.out.println(SaveURL);
                        
            XComponent document = xCompLoader.loadComponentFromURL(SaveURL, "_blank", 0, new com.sun.star.beans.PropertyValue[0]);
            //System.out.println("Hello");
        }

        catch (Exception e){
            e.printStackTrace();
        }

        finally {
        	
        }
	}
    //}
    
}