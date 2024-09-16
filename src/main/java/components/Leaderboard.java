package components;
import imgui.ImGui;
import imgui.flag.ImGuiTableFlags;
import imgui.flag.ImGuiWindowFlags;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Leaderboard {

    public Leaderboard() {

    }

    public void displayLeaderboard() {
        ImGui.begin("Leaderboard");

        if (ImGui.beginTable("LeaderboardTable", 2, ImGuiTableFlags.Borders | ImGuiTableFlags.RowBg)) {
            // Заголовки таблицы
            ImGui.tableSetupColumn("Player Name");
            ImGui.tableSetupColumn("Completion Time");
            ImGui.tableHeadersRow();

            // Чтение данных из файла
            List<String[]> leaderboardData = readLeaderboardFromFile("leaderboard.txt");

            // Вывод данных в таблицу
            for (String[] record : leaderboardData) {
                ImGui.tableNextRow();
                ImGui.tableNextColumn();
                ImGui.text(record[0]);  // Имя игрока
                ImGui.tableNextColumn();
                ImGui.text(record[1]);  // Время прохождения
            }

            ImGui.endTable();
        }

        ImGui.end();
    }

    public void imgui() {
        displayLeaderboard();
    }

    // Метод для чтения файла leaderboard.txt
    private List<String[]> readLeaderboardFromFile(String fileName) {
        List<String[]> leaderboard = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    leaderboard.add(parts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return leaderboard;
    }
}
