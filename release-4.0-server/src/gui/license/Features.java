package gui.license;

import gui.controls.FileControl;

import java.io.File;

public class Features {
	
	private Features() {
		
	}
	
//	public static boolean getGridStatesDirectory() {
//
//		FileControl file = FileControl.getInstance() ; 
//		File reqfile = new File(file.getOPCATDirectory() + FileControl.fileSeparator + "doreq") ; 
//		return reqfile.exists() ; 
//	}	
	
	public static boolean hasReq() {

		FileControl file = FileControl.getInstance() ; 
		File reqfile = new File(file.getOPCATDirectory() + FileControl.fileSeparator + "doreq") ; 
		return reqfile.exists() ; 
	}
	
	public static String getReqFilePath() {

		FileControl file = FileControl.getInstance() ; 
		File reqfile = new File(file.getOPCATDirectory() + FileControl.fileSeparator + "doreq") ; 
		return reqfile.getPath() ; 
	}
	
	public static File getCSVExportDefFilePath() {

	    	File file = new File(FileControl.getInstance().getOPCATDirectory() + FileControl.fileSeparator + "docsv") ; 
	    	if(file.exists()) {
	    	    return file ; 
	    	} else {
	    	    return null ; 
	    	}
	}	
	
	public static boolean hasCSV() {
		FileControl file = FileControl.getInstance() ; 
		File reqfile = new File(file.getOPCATDirectory() + FileControl.fileSeparator + "docsv") ; 
		return reqfile.exists() ;		
	}
	
	public static boolean hasCSVThingsLoading() {
		FileControl file = FileControl.getInstance() ; 
		File reqfile = new File(file.getOPCATDirectory() + FileControl.fileSeparator + "docsvloading") ; 
		return reqfile.exists() ;		
	}
	
	public static boolean hasDeSecretOption() {
		FileControl file = FileControl.getInstance() ; 
		File reqfile = new File(file.getOPCATDirectory() + FileControl.fileSeparator + "desecret.ops") ; 
		return reqfile.exists() ;		
	}	
}
