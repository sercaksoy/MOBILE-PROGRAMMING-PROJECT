package serc.mphw.memoryapp;

import java.util.Calendar;
import java.util.Date;

public class MemoryBody {
    private String memoryName;
    private String memoryText;
    private String memoryPwd;
    private String memoryLocation;

    private Date memoryDate;
    private String memoryEmotion;

    public MemoryBody(String memoryName, String memoryText, String memoryPwd,
                      String memoryLocation, String memoryEmotion){
        this.memoryName = memoryName;
        this.memoryText = memoryText;
        this.memoryPwd = memoryPwd;
        this.memoryLocation = memoryLocation;
        this.memoryEmotion = memoryEmotion;
        this.memoryDate = Calendar.getInstance().getTime();
    }
    public String getMemoryName() {
        return memoryName;
    }

    public void setMemoryName(String memoryName) {
        this.memoryName = memoryName;
    }

    public String getMemoryText() {
        return memoryText;
    }

    public void setMemoryText(String memoryText) {
        this.memoryText = memoryText;
    }

    public String getMemoryPwd() {
        return memoryPwd;
    }

    public void setMemoryPwd(String memoryPwd) {
        this.memoryPwd = memoryPwd;
    }

    public String getMemoryLocation() {
        return memoryLocation;
    }

    public void setMemoryLocation(String memoryLocation) {
        this.memoryLocation = memoryLocation;
    }

    public Date getMemoryDate() {
        return memoryDate;
    }

    public void setMemoryDate(Date memoryDate) {
        this.memoryDate = memoryDate;
    }

    public String getMemoryEmotion() {
        return memoryEmotion;
    }

    public void setMemoryEmotion(String memoryEmotion) {
        this.memoryEmotion = memoryEmotion;
    }
}
