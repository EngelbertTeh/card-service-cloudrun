package vn.cloud.cardservice.utils;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static vn.cloud.cardservice.utils.Constants.FOOD_POSTINGS_STATIC;
import static vn.cloud.cardservice.utils.Constants.ML_BUCKET_STATIC;

@Component
public class GoogleCloudBucketUtil {

    public void writeFileToGCSBucket(String csvData) throws IOException {

        // Create a GCS storage client
        Storage storage = StorageOptions.getDefaultInstance().getService();

        // Convert the CSV data to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(csvData.getBytes(StandardCharsets.UTF_8));
        byte[] bytes = outputStream.toByteArray();

        // Define the GCS blob object
        BlobInfo blobInfo = BlobInfo.newBuilder(ML_BUCKET_STATIC, FOOD_POSTINGS_STATIC).build();
        Blob blob = storage.create(blobInfo, bytes);

        // Print the URL of the newly created file
        String fileUrl = blob.getMediaLink();
        System.out.println("File uploaded to: " + fileUrl);
    }
}
