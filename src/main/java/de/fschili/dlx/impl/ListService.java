package de.fschili.dlx.impl;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import de.fschili.dlx.openapi.api.ListApiDelegate;
import de.fschili.dlx.openapi.model.DataItem;
import de.fschili.dlx.openapi.model.DataItems;

@Service
public class ListService implements ListApiDelegate {

    private final static Logger log = LoggerFactory.getLogger(ListService.class);

    public static final String PDF_UUID = "ee26fd29-fdcb-469d-a4f7-7c6de8607f71";
    public static final String PDF_MIME_TYPE = "application/pdf";
    public static final String PDF_DESCRIPTION = "Radiological report";
    public static final String PDF_FILENAME = "report_mustermann.pdf";

    public static final String JPEG_UUID = "1e418ee9-cd95-4037-a474-a07f3202276e";
    public static final String JPEG_MIME_TYPE = "image/jpeg";
    public static final String JPEG_DESCRIPTION = "Study overview";
    public static final String JPEG_FILENAME = "study_overview.jpg";

    public static final String ZIP_UUID = "2de7773a-09df-46f1-98fb-9eeaaaeba802";
    public static final String ZIP_MIME_TYPE = "application/zip";
    public static final String ZIP_DESCRIPTION = "DICOM Study as zip file";
    public static final String ZIP_FILENAME = "patcd.zip";

    public static final String STUDY_INSTANCE_UID = "1.2.276.0.23.60.1.2.14841915742092284318.1713520221786.0";

    @Override
    public ResponseEntity<DataItems> listGet() {

        String token = ServiceUtils.getTokenFromContext();
        if (token == null || token.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (TokenService.isValidToken(token)) {
            log.info("List data for token '" + token + "'");

            DataItems result = new DataItems();
            result.addDataItemItem(getReportItem());
            result.addDataItemItem(getImageItem());
            result.addDataItemItem(getZipItem());

            return new ResponseEntity<DataItems>(result, HttpStatus.OK);
        }
        else {
            log.error("Unknown token '" + token + "'");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static DataItem getReportItem() {
        DataItem dataItem = new DataItem(PDF_UUID, PDF_MIME_TYPE, PDF_DESCRIPTION);
        addMetaData(dataItem);
        return dataItem;
    }

    private static DataItem getImageItem() {
        DataItem dataItem = new DataItem(JPEG_UUID, JPEG_MIME_TYPE, JPEG_DESCRIPTION);
        addMetaData(dataItem);
        return dataItem;
    }

    private static DataItem getZipItem() {
        DataItem dataItem = new DataItem(ZIP_UUID, ZIP_MIME_TYPE, ZIP_DESCRIPTION);
        addMetaData(dataItem);

        // add study specific metadata
        dataItem.setStudyInstanceUID(STUDY_INSTANCE_UID);
        dataItem.setModalities("OT");
        dataItem.setFileCount(3);
        return dataItem;
    }

    private static void addMetaData(DataItem dataItem) {
        dataItem.setDate(LocalDate.of(2024, 04, 19));

        dataItem.setPatientsName("Mustermann,Max");
        dataItem.setPatientsSex("M");
        dataItem.setPatientsBirthdate(LocalDate.of(1970, 01, 01));
    }

}
