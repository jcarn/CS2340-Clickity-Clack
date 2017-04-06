import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class JUnits {

    @Test(expected = IllegalArgumentException.class)
    public void testDate() {
        String badDate = "022317";
        SourceModel sourceMod = new SourceModel(badDate, "goodID", "Johny B. Good", "Georgia Tech", "Potable", "Lake");
    }
    
}