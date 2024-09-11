package components;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Leaderboard extends Component{
    public Leaderboard() {

    }
//
//    public void write(String name, long time) {
//        try {
//            String source = read();
//            for (String line : source.split("\n")) {
////                if (line.g) {}
//            }
////            FileWriter writer = new FileWriter();
////            for (long t : )
////            writer.write(name + ":" + time + "\n");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public String read() {
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("leaderboard.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inFile;
    }
}
