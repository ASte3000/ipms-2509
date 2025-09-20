package com.iprody;

import java.io.*;

/**
 * Writes HTTP response to provided {@link OutputStream}
 */
public class HttpResponsePrintWriter {
    public static final String STATUS_200_OK = "HTTP/1.1 200 OK";
    public static final String ERROR_400_BAD_REQUEST = "HTTP/1.1 400 Bad Request";
    public static final String ERROR_404_NOT_FOUND = "HTTP/1.1 404 Not Found";
    private static final int FILE_BUFFER_SIZE_BYTES = 10_000;

    private final OutputStream outputStream;

    public HttpResponsePrintWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeStatus(String status) {
        writeLines(status);
    }

    public void writeSuccessfulResponse(String contentType, long fileSize, String sourcePathString) throws IOException {
        writeSuccessfulResponseHeader(contentType, fileSize);
        writeFileContent(sourcePathString);
    }

    private void writeSuccessfulResponseHeader(String contentType, long fileSize) {
        writeLines(
                STATUS_200_OK,
                "Content-Type: %s".formatted(contentType),
                "Content-Length: %d".formatted(fileSize),
                ""
        );
    }

    private void writeLines(String... lines) {
        var writer = new PrintWriter(new OutputStreamWriter(outputStream));

        for (String line : lines) {
            System.out.println(line);
            writer.println(line);
        }

        writer.flush();
    }

    private void writeFileContent(String sourcePathString) throws IOException {
        try (var fileInputStream = new FileInputStream(sourcePathString)) {
            byte[] buffer = new byte[FILE_BUFFER_SIZE_BYTES];
            while (fileInputStream.available() > 0) {
                var bytesRead = fileInputStream.read(buffer, 0, FILE_BUFFER_SIZE_BYTES);
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        outputStream.flush();
    }

}
