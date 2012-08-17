package baseball.data;

import bbct.data.BaseballCard;
import org.junit.*;

/**
 * TODO: JavaDoc
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardNominalTest {

    public BaseballCardNominalTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        this.instance = new BaseballCard(this.brand, this.year, this.num, this.val, this.count, this.name, this.pos);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test for {@link BaseballCard#getBrand}.
     */
    @Test
    public void testGetBrand() {
        Assert.assertEquals(this.brand, this.instance.getBrand());
    }

    /**
     * Test for {@link BaseballCard#getYear}.
     */
    @Test
    public void testGetYear() {
        Assert.assertEquals(this.year, this.instance.getYear());
    }

    /**
     * Test for {@link BaseballCard#getNumber}.
     */
    @Test
    public void testGetNumber() {
        Assert.assertEquals(this.num, this.instance.getNumber());
    }

    /**
     * Test for {@link BaseballCard#getValue}.
     */
    @Test
    public void testGetValue() {
        Assert.assertEquals(this.val, this.instance.getValue());
    }

    /**
     * Test for {@link BaseballCard#getCount}.
     */
    @Test
    public void testGetCount() {
        Assert.assertEquals(this.count, this.instance.getCount());
    }

    /**
     * Test for {@link BaseballCard#getPlayerName}.
     */
    @Test
    public void testGetPlayerName() {
        Assert.assertEquals(this.name, this.instance.getPlayerName());
    }

    /**
     * Test for {@link BaseballCard#getPlayerPosition}.
     */
    @Test
    public void testGetPlayerPosition() {
        Assert.assertEquals(this.pos, this.instance.getPlayerPosition());
    }
    private BaseballCard instance = null;
    private String brand = "Topps";
    private int year = 1991;
    private int num = 278;
    private int val = 10100;
    private int count = 1;
    private String name = "Alex Fernandez";
    private String pos = "Pitcher";
}
