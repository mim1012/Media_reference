package com.movies.player.utils;

import android.os.Environment;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class FileUtils {
    public static String getDownloadFolderParentPath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getParent();
    }

    public static File createKakaoTaxiFolder() {
        File file = new File(getDownloadFolderParentPath(), "KakaoTaxi");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File createDestinationFolder() {
        File file = new File(createKakaoTaxiFolder().getAbsolutePath(), "destination");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File createExceptionFolder() {
        File file = new File(createKakaoTaxiFolder().getAbsolutePath(), "exception");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File createDestinationSettingFile(String str) {
        File file = new File(createDestinationFolder(), str + ".st");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File createExceptionSettingFile(String str) {
        File file = new File(createExceptionFolder(), str + ".st");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File createPreferredExclusionPlacesFile() {
        File file = new File(createKakaoTaxiFolder(), "preferred_exclusions.st");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File createPreferredAcceptPlacesFile() {
        File file = new File(createKakaoTaxiFolder(), "preferred_accepts.st");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String loadDestinationFileToText(String str) {
        File file = new File(createDestinationFolder(), str + ".st");
        try {
            return !file.exists() ? "" : readTextFromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String loadExceptionFileToText(String str) {
        File file = new File(createExceptionFolder(), str + ".st");
        try {
            return !file.exists() ? "" : readTextFromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String loadPreferredExclusionPlacesFileToText() {
        File file = new File(createKakaoTaxiFolder(), "preferred_exclusions.st");
        try {
            return !file.exists() ? "" : readTextFromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String loadPreferredAcceptPlacesFileToText() {
        File file = new File(createKakaoTaxiFolder(), "preferred_accepts.st");
        try {
            return !file.exists() ? "" : readTextFromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void saveDestinationTextToFile(String str, String str2) {
        writeTextToFile(createDestinationSettingFile(str), str2);
    }

    public static void saveExceptionTextToFile(String str, String str2) {
        writeTextToFile(createExceptionSettingFile(str), str2);
    }

    public static void savePreferredExclusionPlacesToFile(String str) {
        writeTextToFile(createPreferredExclusionPlacesFile(), str);
    }

    public static void savePreferredAcceptPlacesToFile(String str) {
        writeTextToFile(createPreferredAcceptPlacesFile(), str);
    }

    public static void saveLogTextToFile(String str) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "log.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        appendTextToFile(file.getAbsolutePath(), str);
    }

    public static boolean isExistDestinationFile(String str) {
        return new File(createDestinationFolder(), new StringBuilder().append(str).append(".st").toString()).exists();
    }

    public static boolean isExistExceptionFile(String str) {
        return new File(createExceptionFolder(), new StringBuilder().append(str).append(".st").toString()).exists();
    }

    public static boolean isExistPreferredExclusionPlacesFile(String str) {
        return new File(createKakaoTaxiFolder(), "preferred_exclusions.st").exists();
    }

    public static boolean isExistPreferredAcceptPlacesFile(String str) {
        return new File(createKakaoTaxiFolder(), "preferred_accepts.st").exists();
    }

    public static ArrayList<String> getDestinationFileList() {
        ArrayList<String> arrayList = new ArrayList<>();
        File[] listFiles = createDestinationFolder().listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".st")) {
                    arrayList.add(file.getName().substring(0, file.getName().lastIndexOf(46)));
                }
            }
        }
        return arrayList;
    }

    public static ArrayList<String> getExceptionFileList() {
        ArrayList<String> arrayList = new ArrayList<>();
        File[] listFiles = createExceptionFolder().listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".st")) {
                    arrayList.add(file.getName().substring(0, file.getName().lastIndexOf(46)));
                }
            }
        }
        return arrayList;
    }

    public static void deleteDestinationFile(String str) {
        File file = new File(createDestinationFolder(), str + ".st");
        if (file.exists()) {
            file.delete();
        }
    }

    public static void deleteExceptionFile(String str) {
        File file = new File(createExceptionFolder(), str + ".st");
        if (file.exists()) {
            file.delete();
        }
    }

    public static void writeTextToFile(File file, String str) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(str);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendTextToFile(File file, String str) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.append((CharSequence) str);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendTextToFile(String str, String str2) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(str, true));
            bufferedWriter.write(str2);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readTextFromFile(File file) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine);
                sb.append("\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
