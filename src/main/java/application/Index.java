package application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import com.google.common.base.Objects;

import exception.WebApplicationException;
import faces.BaseBackingBean;
import log.WebAccessLogging;
import lombok.Getter;

@Named
@ViewScoped
@WebAccessLogging
public class Index extends BaseBackingBean {

    public void handleFileUpload(FileUploadEvent event) throws IOException {

        UploadedFile uploadedFile = event.getFile();
        String extention = FilenameUtils.getExtension(uploadedFile.getFileName());

        if (uploadedFile == null
                || uploadedFile.getContent() == null
                || uploadedFile.getContent().length <= 0
                || StringUtils.isBlank(uploadedFile.getFileName())
                || uploadedFile.getFileName().length() >= 256) {

            messageService().addMessage(FacesMessage.SEVERITY_ERROR, "不正なファイルがアップロードされました");
            return;
        }

        // ContentTypeのチェック
        TikaInputStream stream = TikaInputStream.get(uploadedFile.getInputStream());
        String detectedContentType = new Tika().detect(stream);
        if (!Objects.equal(uploadedFile.getContentType(), detectedContentType)) {
            messageService().addMessage(FacesMessage.SEVERITY_ERROR, "不正なファイルがアップロードされました");
            return;
        }

        try {
            ResourceBundle bundle = ResourceBundle.getBundle("ApplicationConfig");

            String strDir = bundle.getString("uploadfile.root.dir");
            File dir = new File(strDir);
            dir.mkdirs();

            File saveFile = File.createTempFile("tmp_", ".".concat(extention), dir);
            Files.copy(uploadedFile.getInputStream(), saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new WebApplicationException(e);
        }
    }

    @Getter
    private StreamedContent downloadedFile;

    public void downloadFile(ActionEvent actionEvent) throws UnsupportedEncodingException, IOException {

        response().addHeader("Content-Disposition", "attachement");

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            output.write("ダウンロードファイルデータ".getBytes("MS932"));

            this.downloadedFile = DefaultStreamedContent.builder()
                    .name("download.txt")
                    .contentType("text/plain")
                    .stream(() -> new ByteArrayInputStream(output.toByteArray()))
                    .build();
        }
    }

}
