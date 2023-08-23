import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class MainForm {

    private JPanel panelMain;
    private JButton button1;
    private JTextArea textArea1;
    private JButton ClearInputButton;
    private JButton ClearOutputButton;
    private JButton findButton;
    private List<String> namesCsv = new ArrayList<>();

    public MainForm() {
        findFiles();

        button1.addActionListener(new ActionListener() { //По кнопке выполняем преобразование
            @Override
            public void actionPerformed(ActionEvent e) {
                //Создаём виртуальные файлы-объекты
                List<FileCsv> filesCsv = new ArrayList<>();
                for (String name: namesCsv) {
                    filesCsv.add(new FileCsv(name));
                }
                //Заполняем виртуальные файлы байтами
                for (FileCsv fileCsv: filesCsv) {
                    try {
                        fileCsv.takeAllBytes(Files.readAllBytes(Paths.get(".\\input\\" + fileCsv.getName()))); //byte[]
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                }
                textArea1.setText("");
                //Создаём новые файлы в \output на основе данных виртуальных файлов
                for (FileCsv fileCsv: filesCsv) {
                    try {
                        Files.write(Paths.get(".\\output\\" + fileCsv.getName()), fileCsv.giveAllBytes());
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                    textArea1.append(fileCsv.getName());
                    textArea1.append(": Удалено вхождений (FF FE) - ");
                    textArea1.append(Integer.toString(fileCsv.getCntDeleted() / 2));
                    textArea1.append("\n");
                }
            }
        });
        ClearInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File files = new File(".\\input\\"); //Все файлы и директории в папке \input
                List<String> namesCsv = new ArrayList<>();
                for (File f: files.listFiles()) { //Собираем только имена csv-файлов
                    if (f.isFile() && f.getName().toUpperCase().endsWith(".CSV")) {
                        f.delete();
                    }
                }
                namesCsv.clear();
                textArea1.setText("Программа удаляет из csv-файлов байты BOM csv (FF FE),\n");
                textArea1.append("ошибочно стоящие в середине файла.\n");
                textArea1.append("Исправленные файлы будут созданы в папке \\output\n");
                textArea1.append("\n");
                button1.setEnabled(false);
            }
        });
        ClearOutputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File files = new File(".\\output\\"); //Все файлы и директории в папке \output
                List<String> namesCsv = new ArrayList<>();
                for (File f: files.listFiles()) { //Собираем только имена csv-файлов
                    if (f.isFile() && f.getName().toUpperCase().endsWith(".CSV")) {
                        f.delete();
                    }
                }
            }
        });
        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            findFiles();
            }
        });
    }

    void findFiles() {
        namesCsv.clear();
        File files = new File(".\\input\\"); //Все файлы и директории в папке \input
        for (File f: files.listFiles()) { //Собираем только имена csv-файлов
            if (f.isFile() && f.getName().toUpperCase().endsWith(".CSV")) {
                namesCsv.add(f.getName());
            }
        }

        textArea1.setText("Программа удаляет из csv-файлов байты BOM csv (FF FE),\n");
        textArea1.append("ошибочно стоящие в середине файла.\n");
        textArea1.append("Исправленные файлы будут созданы в папке \\output\n");
        textArea1.append("\n");

        if (namesCsv.isEmpty()) {
            button1.setEnabled(false);
            textArea1.append("Не удалось найти csv-файлы. Поместите их в папку \\input\n");
        } else {
            button1.setEnabled(true);
        }
        for (String s: namesCsv) {
            textArea1.append(s + "\n");
        }
        textArea1.append("\n");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        frame.setLocation(400, 200);
        frame.setContentPane(new MainForm().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 700));
        frame.pack();
        frame.setVisible(true);
    }

//    private void createUIComponents() {
//        // TODO: place custom component creation code here
//    }
}
