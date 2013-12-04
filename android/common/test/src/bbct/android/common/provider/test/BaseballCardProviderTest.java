package bbct.android.common.provider.test;

import junit.framework.Assert;
import android.test.ProviderTestCase2;
import bbct.android.common.provider.BaseballCardContract;
import bbct.android.common.provider.BaseballCardProvider;

public class BaseballCardProviderTest extends
        ProviderTestCase2<BaseballCardProvider> {

    public BaseballCardProviderTest() {
        super(BaseballCardProvider.class, BaseballCardContract.AUTHORITY);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testQuery() {
        Assert.fail("Stub");
    }

    public void testInsert() {
        Assert.fail("Stub");
    }

    public void testUpdate() {
        Assert.fail("Stub");
    }

    public void testDelete() {
        Assert.fail("Stub");
    }

}
