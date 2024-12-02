package com.telros.users.services;

public class RegexService {

    //рефакторинг и проверка формата номера телефона
    public static String phoneNumberRefactor(String input) {
        String regex = "\\D";
        String regexFormat = "[7,8]\\d{10}";
        String output = input.replaceAll(regex, "");
        if (output.length() == 10) {
            return "7" + output;
        }
        if (output.matches(regexFormat)) {

            if (output.startsWith("7")) {
                return output;
            }
            if (output.startsWith("8")) {
                output = output.replaceFirst("8", "7");
            }
        } else {
            output = "Неверный формат номера";
        }
        return output;
    }

    public static String checkTheName(String input){
        boolean result = false;
        if(input.length() == 0){
            return "Данные отсутствуют";
        }
        else {
            String regex = "[1-9]";
            String output = input.replaceAll(regex, "");
            return output;
        }
    }


}
