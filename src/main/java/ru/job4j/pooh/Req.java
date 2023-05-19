package ru.job4j.pooh;

public record Req(String httpRequestType, String poohMode, String sourceName, String param) {

    public static Req of(String content) {
        String[] array = content.split(System.lineSeparator());
        String[] firstStr = array[0].split("/");
        if (firstStr.length < 4) {
            throw new IllegalArgumentException("Incorrect input of content");
        }
        String sourceName = null;
        String param = null;
        boolean lengthFour = firstStr.length == 4;
        if (lengthFour) {
            sourceName = firstStr[2].split(" ")[0];
            param = array[array.length - 1];
            if (array.length < 8) {
                param = "";
            }
        }
        if (!lengthFour) {
            sourceName = firstStr[2];
            param = firstStr[3].split(" ")[0];
        }
        return new Req(firstStr[0].trim(), firstStr[1], sourceName, param);
    }
}
