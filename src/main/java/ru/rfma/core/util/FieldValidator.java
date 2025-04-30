package ru.rfma.core.util;

public class FieldValidator {
    public static Integer validateId(String id) throws Exception {
        try {
            return Integer.parseInt(id);
        }
        catch (Exception e){
            throw new Exception("wrong id format");
        }
    }
}
