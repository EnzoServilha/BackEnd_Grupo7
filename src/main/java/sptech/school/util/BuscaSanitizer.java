package sptech.school.util;

public final class BuscaSanitizer {

    private BuscaSanitizer() {
    }

    public static String escaparLike(String valor) {
        return valor
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
