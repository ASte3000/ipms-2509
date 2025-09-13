package com.iprody;

/**
 * Represents file extensions and corresponding HTTP Content-Type
 */
public enum FileExtension {
    TXT("text/plain"),
    HTML("text/html; charset=UTF-8"),
    CSS("text/css"),
    JS("text/javascript"),
    JPEG("image/jpeg"),
    PNG("image/png");

    private final String contentType;

    FileExtension(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public String getLowercaseExtension() {
        return name().toLowerCase();
    }

    public static FileExtension find(String sourcePathString) {
        var lowerPath = sourcePathString.toLowerCase();
        for (FileExtension extension : FileExtension.values()) {
            if (lowerPath.endsWith(extension.getLowercaseExtension()))
                return extension;
        }

        return FileExtension.TXT;
    }
}
