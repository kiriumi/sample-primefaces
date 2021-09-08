package application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
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
import lombok.Getter;
import token.TokenCheck;

@Named
@ViewScoped
@TokenCheck
public class Fileload extends BaseBackingBean {

    public void handleFileUpload(FileUploadEvent event) {

        UploadedFile uploadedFile = event.getFile();
        String extention = FilenameUtils.getExtension(uploadedFile.getFileName());

        // 0byteファイルではないか＋ファイル名の長さチェック
        if (uploadedFile == null
                || uploadedFile.getContent() == null
                || uploadedFile.getContent().length <= 0
                || StringUtils.isBlank(uploadedFile.getFileName())
                || uploadedFile.getFileName().length() >= 256) {

            messageService().addGlobalMessageError("不正なファイルがアップロードされました");
            return;
        }

        try {
            // ContentTypeのチェック
            TikaInputStream stream = TikaInputStream.get(uploadedFile.getInputStream());
            String detectedContentType = new Tika().detect(stream);
            if (!Objects.equal(uploadedFile.getContentType(), detectedContentType)) {
                messageService().addGlobalMessageError("不正なファイルがアップロードされました");
                return;
            }

            // アップロードファイルの保存
            ResourceBundle bundle = ResourceBundle.getBundle("Environment");

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

        try (ByteArrayOutputStream output = new ByteArrayOutputStream();

                CSVPrinter printer = CSVFormat.Builder.create(CSVFormat.DEFAULT).setQuoteMode(QuoteMode.ALL)
                        .setHeader("id", "userName", "firstName", "lastName", "birthday")
                        .build().print(new OutputStreamWriter(output, "MS932"))) {

            printer.printRecord(1, "john73", "John", "Doe", LocalDate.of(1973, 9, 15));
            printer.printRecord(2, "mary", "Mary", "Meyer", LocalDate.of(1985, 3, 29));

            this.downloadedFile = DefaultStreamedContent.builder()
                    .name("download.csv")
                    .contentType("text/plain")
                    .stream(() -> new ByteArrayInputStream(output.toByteArray()))
                    .build();
        }
    }

    public void parseCsv() throws IOException {

        ResourceBundle bundle = ResourceBundle.getBundle("Environment");
        String dir = bundle.getString("csv.root.dir");
        Reader reader = new FileReader(new File(dir, "sample.csv"));

        CSVParser parser = CSVFormat.Builder.create(CSVFormat.DEFAULT).setQuoteMode(QuoteMode.ALL)
                .setHeader("id", "userName", "firstName", "lastName", "birthday").setSkipHeaderRecord(true).build()
                .parse(reader);

        List<CSVRecord> recoreds = parser.getRecords();

        recoreds.stream().forEach(record -> {
            System.out.println(record.get("id"));
            System.out.println(record.get("userName"));
        });
    }

}
