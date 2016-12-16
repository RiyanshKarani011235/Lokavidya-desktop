package libreoffice;

import java.io.BufferedWriter;
import java.io.FileWriter;

import com.iitb.lokavidya.core.utils.GeneralUtils;
import com.sun.star.beans.PropertyValue;
import com.sun.star.io.IOException;
import com.sun.star.lang.XComponent;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

import ooo.connector.BootstrapSocketConnector;

public class LibreConvert {
	XComponent document;
	public LibreConvert() {
		// TODO Auto-generated constructor stub
	}

	public  void launchOfficeInstanceIncognito(String pptPath, String openOfficePath) {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter("librelog.txt",true));
			br.write("\nAcquired mutex lock for "+pptPath);
			br.close();
		} catch (java.io.IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
            
            com.sun.star.frame.XComponentLoader xCompLoader = UnoRuntime.queryInterface(
            		com.sun.star.frame.XComponentLoader.class, oDesktop
			);
            
                        //String SaveURL="file:///C:/Users/hp/Documents/trial1.pptx";
            pptPath=pptPath.replaceAll("\\\\","/");
            String SaveURL="file:///"+pptPath;
            System.out.println(SaveURL);
            com.sun.star.beans.PropertyValue[] parr=new com.sun.star.beans.PropertyValue[1];
            parr[0]=new PropertyValue();
            PropertyValue p = parr[0];
            p.Name="Hidden";
            p.Value=true;
            document = xCompLoader.loadComponentFromURL(SaveURL, "_blank", 0,parr);
            //System.out.println("Hello");
        }

        catch (Exception e){
            e.printStackTrace();
        }

        finally {
        	
        }
	}
	
	public void saveAsURL(String savePath) {
		savePath=savePath.replaceAll("\\\\","/");
        String SaveURL="file:///"+savePath;
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
	            propertyValue[1].Value = findType(savePath);
	            try {
					xStorable.storeToURL(SaveURL , propertyValue );
					/*XCloseable xClose = (XCloseable)UnoRuntime.queryInterface(XCloseable.class, document);
		            xClose.close();*/
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter("librelog.txt",true));
			br.write("\nReleased mutex lock for "+savePath);
			br.close();
		} catch (java.io.IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}      
	      return;      
	        
	}
	
	private String findType(String savePath) {
		// TODO Auto-generated method stub
		if(savePath.endsWith(".odp")) {
			System.out.println("Inside FindType"+savePath);
			return "Impress MS PowerPoint 2007 XML";
		}
		
		if(savePath.endsWith(".png"))
			return "impress_png_Export";
		if(savePath.endsWith(".jpeg")||savePath.endsWith(".jpg"))
			return "impress_jpg_Export";
		return null;
	}
	
}
