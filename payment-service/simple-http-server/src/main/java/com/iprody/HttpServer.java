package com.iprody;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Simple HTTP server - reads HTTP request and returns requested file (if found)
 */
public class HttpServer {
    private static final int PORT = 8080;
    private static final String HTTP_METHOD_GET = "GET";
    private static final String FILES_FOLDER = "simple-http-server/static/";

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.printf("Server started at http://localhost:%s%n", PORT);

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("New client connected: " + new Date());

                    var reader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

                    while (!reader.ready());    //waiting for request

                    var responseWriter = new HttpResponsePrintWriter(socket.getOutputStream());

                    var fileName = getFileNameFromRequestHeader(reader.readLine());
                    if (fileName != null) {
                        processFileRequest(fileName, responseWriter);
                    } else {
                        responseWriter.writeStatus(HttpResponsePrintWriter.ERROR_400_BAD_REQUEST);
                    }
                }
            }
        }
    }

    private static String getFileNameFromRequestHeader(String headerFirstLine) {
        var mainParams = headerFirstLine.split(" ");
        if (mainParams.length == HttpRequestParams.values().length
                && mainParams[HttpRequestParams.METHOD_NAME.ordinal()].equals(HTTP_METHOD_GET))
        {
            return mainParams[HttpRequestParams.PATH.ordinal()];
        } else {
            return null;
        }
    }

    private static void processFileRequest(String fileName, HttpResponsePrintWriter responseWriter) throws IOException {
        System.out.println("processFileRequest: " + fileName);

        var path = Path.of(fileName);
        if (!path.getRoot().equals(path.getParent())) {
            responseWriter.writeStatus(HttpResponsePrintWriter.ERROR_404_NOT_FOUND);    //requested file is not in the root folder
        } else {
            String sourcePathString = FILES_FOLDER + path.getFileName();
            var sourcePath = Paths.get(sourcePathString);
            if (Files.exists(sourcePath)) {
                responseWriter.writeSuccessfulResponse(FileExtension.find(sourcePathString).getContentType(),
                        Files.size(sourcePath), sourcePathString);
            } else {
                responseWriter.writeStatus(HttpResponsePrintWriter.ERROR_404_NOT_FOUND);
            }
        }
    }
}

