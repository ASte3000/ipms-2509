package com.iprody;

/**
 * Represents file extensions and corresponding HTTP Content-Type
 */
public enum ExtensionClass {
    TXT("text/plain"),
    HTML("text/html; charset=UTF-8"),
    CSS("text/css"),
    JS("text/javascript"),
    JPEG("image/jpeg"),
    PNG("image/png");

    private static final Character DOT = '.';
    private final String contentType;

    ExtensionClass(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public String getLowercaseExtension() {
        return name().toLowerCase();
    }

    public static ExtensionClass find(String sourcePathString) {
        var dotIndex = sourcePathString.lastIndexOf(DOT);
        var extensionIndex = dotIndex + 1;
        if (dotIndex > 0 && sourcePathString.length() > extensionIndex) {
            var sourceExtensionLower = sourcePathString.substring(extensionIndex).toLowerCase();
            for (ExtensionClass extClass : ExtensionClass.values()) {
                if (sourceExtensionLower.equals(extClass.getLowercaseExtension()))
                    return extClass;
            }
        }

        return ExtensionClass.TXT;
    }
}
