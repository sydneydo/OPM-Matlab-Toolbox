package gui.util;

/**
 * Represents an entry in the last-used-files. 
 * @author Eran Toch
 */
public class LastFileEntry {
    private String projectName = "";
    private String fileUrl = "";
    
    public LastFileEntry(String projectName, String fileUrl)	{
        this.projectName = projectName;
        this.fileUrl = fileUrl;
    }
    
    public String getProjectName()	{
        return this.projectName;
    }
    
    public String getFileUrl()	{
        return this.fileUrl;
    }
    
    /**
     * @return Only the file name (without the full path of the file. 
     */
    public String getFileNameWithoutPath()	{
        String fs = System.getProperty("file.separator");
        int lastFileSeperatorIndex = this.fileUrl.lastIndexOf(fs);
        String name = this.fileUrl.substring(lastFileSeperatorIndex+1, this.fileUrl.length());
        return name;
    }
}
