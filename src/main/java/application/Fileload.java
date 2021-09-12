package application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import domain.InvoiceDetail;
import exception.WebApplicationException;
import faces.BaseBackingBean;
import lombok.Getter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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

    @Getter
    private StreamedContent downloadedPdf;

    public void downloadPdfDynamic() throws JRException {

        // レイアウトの読込み
        InputStream input = this.getClass().getResourceAsStream("/reports/invoice.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(input);

        // パラメータの設定
        Map<String, Object> params = new HashMap<>() {
            {
                put("companyName", "株式会社サンプル");
            }
        };

        // 詳細（一覧）の情報設定
        List<InvoiceDetail> list = new ArrayList<>();

        // 商品１の情報設定
        InvoiceDetail detail1 = new InvoiceDetail();
        detail1.setItemName("商品１");
        detail1.setNum(10);
        detail1.setPrice(new BigDecimal("100.10"));

        BigDecimal amount1 = detail1.getPrice().multiply(new BigDecimal(detail1.getNum()))
                .setScale(0, RoundingMode.HALF_UP);
        detail1.setAmount(amount1.intValue());

        detail1.setNote("備考１");
        list.add(detail1);

        // 商品２の情報設定
        InvoiceDetail detail2 = new InvoiceDetail();
        detail2.setItemName("商品２");
        detail2.setNum(20);
        detail2.setPrice(new BigDecimal("200.20"));

        BigDecimal amount2 = detail2.getPrice().multiply(new BigDecimal(detail2.getNum()))
                .setScale(0, RoundingMode.HALF_UP);
        detail2.setAmount(amount2.intValue());

        detail2.setNote("備考２");
        list.add(detail2);

        // レイアウトにパラメータとデータソース（詳細一覧）を設定
        JasperPrint print = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(list));

        // PDF出力
        byte[] bytePdf = JasperExportManager.exportReportToPdf(print);

        // ダウンロード
        this.downloadedPdf = DefaultStreamedContent.builder()
                .name("download.pdf")
                .contentType("application/pdf")
                .stream(() -> new ByteArrayInputStream(bytePdf))
                .build();

    }

    public void makePdfStatic() throws JRException {

        // レイアウトファイルの読込み
        InputStream input = this.getClass().getResourceAsStream("/reports/static.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(input);

        // レイアウトにパラメータとデータソースを設定
        // ※パラメータやデータソースがなくても、空のHashMapやJREmptyDataSourceが必要なので、注意
        JasperPrint print = JasperFillManager.fillReport(jasperReport, new HashMap<>(), new JREmptyDataSource());

        ResourceBundle bundle = ResourceBundle.getBundle("Environment");
        String dir = bundle.getString("pdf.root.dir");

        // PDF出力
        File pdf = new File(dir, "static.pdf");
        JasperExportManager.exportReportToPdfFile(print, pdf.getAbsolutePath());
    }

}
