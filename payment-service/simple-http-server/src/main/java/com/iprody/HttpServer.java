package com.iprody;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

public class HttpServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.printf("Server started at http://localhost:%s%n", PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            String systemTime = new Date().toString();
            System.out.println("New client connected: " + systemTime);

            var reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            while (!reader.ready());

            var inputLinesList = new ArrayList<String>();
            while (reader.ready()) {
                var line = reader.readLine();
                System.out.println(line);
                inputLinesList.add(line);
            }

            var mainParams = inputLinesList.isEmpty() ? null : inputLinesList.getFirst().split(" ");
            if (mainParams != null && mainParams.length == HttpRequestParams.values().length
                    && mainParams[HttpRequestParams.METHOD_NAME.ordinal()].equals("GET"))
            {
                var fileName = mainParams[HttpRequestParams.PATH.ordinal()];
                getFile(fileName, socket.getOutputStream());
            }

            socket.close();
        }

    }

    private static void getFile(String fileName, OutputStream out) throws IOException {
        System.out.println("getFile: " + fileName);
        var path = Path.of(fileName);
        var cleanFileName = path.getFileName();
        if (!path.equals(Path.of("/" + cleanFileName))) {
            writeNotFound(out);
        } else {
            String sourcePathString = "simple-http-server/static/" + cleanFileName;
            if (Files.exists(Paths.get(sourcePathString))) {
                var outputStreamWriter = new OutputStreamWriter(out);
                var writer = new PrintWriter(outputStreamWriter);


                writer.println("HTTP/1.1 200 OK");

                var extension = getFileExtension(sourcePathString);
                writer.printf("Content-Type: %s", extension.getContentType());
                writer.println();

                FileReader fileReader = new FileReader(sourcePathString);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                var fileLines = bufferedReader.lines().toList();

                var responseLength = 0L;
                for (String line : fileLines) {
                    responseLength += line.length();
                }
                writer.println("Content-Length: " + responseLength);
                writer.println();

                fileLines.forEach(writer::write);

                writer.flush();
            } else {
                writeNotFound(out);
            }
        }
    }

    private static void writeNotFound(OutputStream out) {
        var writer = new PrintWriter(
                new OutputStreamWriter(out));
        writer.println("HTTP/1.1 404 NOT FOUND");
        writer.flush();
    }

    private enum HttpRequestParams {
        METHOD_NAME,
        PATH,
        PROTOCOL
    }

    private static FileExtension getFileExtension(String sourcePathString) {
        var upperPath = sourcePathString.toUpperCase();
        for (FileExtension extension : FileExtension.values()) {
            if (upperPath.endsWith(extension.name()))
                return extension;
        }

        return FileExtension.HTML;
    }

    private enum FileExtension {
        HTML("text/html; charset=UTF-8"),
        JPEG("image/jpeg");

        private final String contentType;

        FileExtension(String contentType) {
            this.contentType = contentType;
        }

        public String getContentType() {
            return contentType;
        }
    }
}

