import java.util.ArrayList;
import java.util.List;

public class FileCsv {
    /*
    Виртуальные файлы, в которые записываются байты без BOM csv (FF FE) не в начале файла
    Для последующего создания исправленных файлов csv
     */
    String name;
    List<Byte> bytes = new ArrayList<>();//Буффер для хранения всех байтов файла
    int cntDeleted;//Счётчик пропущеных BOM csv (FF FE)

    public FileCsv(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCntDeleted() {
        return cntDeleted;
    }

    public void takeAllBytes(byte... bytes) {
        for(int i = 0; i < bytes.length; i++) {
            if((i < 2) || (bytes[i] != -1) && (bytes[i] != -2)) { //Записываем всё, пропуская FF FE не в начале файла
                this.bytes.add(bytes[i]);
            } else {
                this.cntDeleted++;
            }
        }
    }

    public byte[] giveAllBytes() { //Перекладываем всё из List<Byte> в byte[]
        byte[] outBytes = new byte[this.bytes.size()];
        for (int i = 0; i < outBytes.length; i++) {
            outBytes[i] = this.bytes.get(i);
        }
        return outBytes;
    }
}
