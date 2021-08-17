package security;

import java.io.IOException;

import org.primefaces.model.file.UploadedFile;
import org.primefaces.virusscan.VirusException;
import org.primefaces.virusscan.VirusScanner;

import log.WebApplicationLogger;

public class WebVirusScanner implements VirusScanner {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void scan(UploadedFile file) {

        WebApplicationLogger.debug("ウィルススキャンを実施");

        // ウィルススキャンを実施
        int exitValue = 0;
        if (exitValue == 1) {
            try {
                file.delete();
            } catch (IOException e) {
            }
            throw new VirusException();
        }
    }

}
