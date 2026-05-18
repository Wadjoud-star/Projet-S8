package com.clubsport.util;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * CSV séparateur point-virgule (Excel FR) avec BOM UTF-8.
 */
public final class CsvExportUtil {

    private static final char SEP = ';';

    private CsvExportUtil() {
    }

    public static void writeUtf8Bom(Writer out) throws IOException {
        out.write('\uFEFF');
    }

    public static void writeLine(Writer out, String... cells) throws IOException {
        for (int i = 0; i < cells.length; i++) {
            if (i > 0) {
                out.write(SEP);
            }
            out.write(escape(cells[i]));
        }
        out.write("\r\n");
    }

    public static void writeLine(Writer out, List<String> cells) throws IOException {
        writeLine(out, cells.toArray(new String[0]));
    }

    public static void writeBlankLine(Writer out) throws IOException {
        out.write("\r\n");
    }

    /** Ligne clé / valeur (métadonnées ou synthèse). */
    public static void writePair(Writer out, String label, String value) throws IOException {
        writeLine(out, label, value == null ? "" : value);
    }

    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        boolean needsQuotes = value.indexOf(SEP) >= 0 || value.indexOf('"') >= 0
                || value.indexOf('\n') >= 0 || value.indexOf('\r') >= 0;
        if (!needsQuotes) {
            return value;
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
