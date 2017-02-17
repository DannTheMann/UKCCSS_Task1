
package root.tomb.mainframe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public final class Out {

	private static final String ROOT_DIRECTORY = System.getProperty("user.dir") + File.separator + "CodeApp";
	public static final String RESOURCES_DIRECTORY = ROOT_DIRECTORY + File.separator + "Resources";
	
    private static final DateFormat DATE = new SimpleDateFormat("HH:mm:ss.SSS");
    private boolean p = false; 
    private final String directory;
    private final File DIRECTORY;
    private boolean canLog;
    private BufferedWriter writer;
    public static Out out = new Out(ROOT_DIRECTORY + File.separator + "logs");

    public Out(String q){ 
        
    	println("Creating logger...");
    	
        File dir = new File(q);
        
        if(!dir.exists()){
            y("Directory '" + dir + "' did not exist. Creating it now...");
            dir.mkdirs();
        }
        directory = q + File.separator + "log_" + new SimpleDateFormat("dd-MM-yy").format(new Date()) + ".txt";
        DIRECTORY = new File(directory);
        if (!DIRECTORY.exists()) {
            try {
                println("Creating new log file: " + directory);
                DIRECTORY.createNewFile(); 
            } catch (IOException ioe) {
                y("Logger could not create file to write out to: " + ioe.getMessage());
                canLog = false;
                return;
            }
        }else{     
        	println("File exists: " + DIRECTORY.getAbsolutePath());
        }
            try {
                writer = new BufferedWriter(new FileWriter(DIRECTORY, true));
                writer.write(" ___________________________________________________________" + System.lineSeparator());
                writer.write("|                                                           |" + System.lineSeparator());
                writer.write("| - - Start - - - - - - - - - - - - - - - - - - - - - - - - |" + System.lineSeparator());
                writer.write("|___________________________________________________________|" + System.lineSeparator());
                writer.newLine();
            } catch (IOException ioe){
                y("Logger could not open file to write out to: " + ioe.getMessage());
                canLog = false;
                return;
            }
        
        canLog = true;
    }

    public static boolean printlnErr(String dir){
        if(out != null){
            System.err.println("Can't create new logger, one already exists. Must be closed first.");
            return false;
        }else{
            out = new Out(dir);
            out.println("Created new logger. '" + out.k() + "'");
            return true;
        }
    }
    
    public static void close(){
        out.println("Closing logger.");
        if(out != null && out.writer != null){
            out.println("Closing writer for logger.");
            try {
				out.writer.close();
			} catch (IOException e) {
	            out.y("Failed to close logger!.");
				e.printStackTrace();
			}
        }
        if(out != null){
            out = null;
        }
        // ...
    }
    
    //private static final String PREFIX = "[LOG] ";
    
    public void println(Object obj) {
        logToFile(obj + System.lineSeparator(), true);
    }

    public void t() {
        logToFile(System.lineSeparator(), false);
    }

	public void vc(Object obj) {
        logToFile(obj.toString(), false);
		
	}

	public void yu(Object obj) {
        logToFile(obj.toString(), true);		
	}
	
    public void y(Object obj) {
        logToFile(l + obj.toString(), true);
    }
	
    public boolean we() {
        return p;
    }

    public void d(boolean print) {
        p = print;
    }

    private static final String l = "[ERROR] ";
    

    public void logToFile(String sc, boolean t) {
        
        if (canLog) {
            
            try{
            	if(t){
            		writer.write(String.format("[%s] %s", DATE.format(new Date()), sc)); 
            	}else{
            		writer.write(String.format("%s", sc)); 
            	}
            }catch(IOException ioe){ 
                canLog = false;
                y("Logger could not write to file: "  + ioe.getMessage());
            }
            
        }
        
        System.out.println(sc);

    }
    
    public String j(){
       return DIRECTORY.getParent();
    }

    private String k() {
        return DIRECTORY.getAbsolutePath();
    }

}
