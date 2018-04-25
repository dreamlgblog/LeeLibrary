package zero.anytime.com.viewlibrary;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        int mCurrentStep = 200;
        int mStepMax = 5000;
        float m = (float) mCurrentStep/mStepMax >=100 ? 100 : (float) mCurrentStep/mStepMax;
        System.out.println(m);
        assertEquals(4, 2 + 2);
    }
}