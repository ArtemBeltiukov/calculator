import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        
        Scanner scanner = new Scanner(System.in);
        while (true){
            String command = scanner.nextLine();
            if (command.equals("exit")){
                break;
            }else if (command.equals("print file list")){
                System.out.println("Enter directory name");
                String path = scanner.nextLine();
                printFileList(path);
            }else if (command.equals("delete file")){
                System.out.println("Enter file name");
                String path = scanner.nextLine();
                deleteFile(path);
            }else if (command.equals("create file")){
                System.out.println("Enter file name");
                String path = scanner.nextLine();
                try {
                    createFile(path);
                } catch (IOException e) {
                    System.out.println("can`t create file.");
                }
            }else if (command.equals("copy file with stream")){
                System.out.println("Enter file name, and path to copy");
                String path = scanner.nextLine();
                String path2 = scanner.nextLine();
                copyFileWithStream(path,path2);
            }else if (command.equals("copy file with channel")){
                System.out.println("Enter file name, and path to copy");
                String path = scanner.nextLine();
                String path2 = scanner.nextLine();
                copyFileWithFileChannel(path,path2);
            }else {
                System.out.println(command + " not supported \n list if supported commands\n" +
                        "print file list\ncreate file\ndelete file\ncopy file with stream\ncopy file with channel\nexit");
            }
        }


    }

    public static void printFileList(String path) {
        File directory = new File(path);
        File[] listFiles = directory.listFiles();
        long biggestFileSize = 0;
        File biggestFile = null;

        Arrays.stream(listFiles).sorted((Comparator.comparing(File::getName)));
        for (File file : listFiles
        ) {
            String extension = "";
            String fileName = file.getName();

            String[] partsOfFileName = fileName.split("\\.");
            if (partsOfFileName.length > 1) {
                extension = partsOfFileName[partsOfFileName.length - 1];
            }
            if (file.isFile() && file.length() > biggestFileSize) {
                biggestFileSize = file.length();
                biggestFile = file;
            }
            System.out.println(file.getName() + (!extension.isEmpty() ? " extension: " + extension : "") +
                    (!file.isDirectory() ? " file size: " + file.length() + " bytes" : ""));
        }

        System.out.println("Biggest file: " + biggestFile.getName() + " file size: " + biggestFile.length() + " bytes");
        System.out.println(listFiles.length + " files in directory");
        System.out.println("average file size: " + Arrays.stream(listFiles).filter(File::isFile).
                mapToLong(x-> x.length()).sum() / listFiles.length);
    }

    public static void deleteFile(String path){
        File file = new File(path);
        file.delete();
    }

    public static void createFile(String path) throws IOException {
        File file = new File(path);
        file.createNewFile();
    }
    public static void copyFileWithStream(String pathToFile, String pathToCopy) {

        try(InputStream is = new FileInputStream(pathToFile);OutputStream os = new FileOutputStream(pathToCopy)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFileWithFileChannel(String pathToFile, String pathToCopy){

        try(FileChannel sourceChannel = new FileInputStream(pathToFile).getChannel();
            FileChannel destChannel = new FileOutputStream(pathToCopy).getChannel();) {
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
