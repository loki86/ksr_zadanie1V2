import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cinnamon on 2018-04-08.
 */
public class Artykul {
    String kraj;
    String krajZKNN;

    Map mapaSlow = new HashMap<>();
    int dlugoscArt = 0;

    public Artykul(){

    }

    public Artykul(String kraj){
        this.kraj = kraj;
    }
}
