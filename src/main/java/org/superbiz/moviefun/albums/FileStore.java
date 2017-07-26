package org.superbiz.moviefun.albums;

import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Optional;

@Component
public class FileStore implements BlobStore {

    @Override
    public void put(Blob blob) throws IOException {
        File file = new File(blob.name);
        saveUploadToFile(blob.inputStream, file);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {

        File coverFile = new File(name);

        if (coverFile.exists()) {
            Blob blob = new Blob(name, new FileInputStream(coverFile), new Tika().detect(coverFile));
            return Optional.of(blob);
        } else {
//            coverFilePath = Paths.get(getSystemResource("default-cover.jpg").toURI());
            return Optional.empty();
        }
    }

    private void saveUploadToFile(InputStream inputStream, File targetFile) throws IOException {

        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            IOUtils.copy(inputStream, outputStream);
        }
    }

}
