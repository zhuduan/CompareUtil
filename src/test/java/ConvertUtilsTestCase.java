import org.junit.Test;
import org.zhuduan.convertor.ConvertUtils;
import org.zhuduan.convertor.common.NotConvert;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * purpose of this class
 *
 * @author Haifeng.Zhu
 *         created at 11/1/17
 */
public class ConvertUtilsTestCase {

    @Test
    public void testConvert4Single() throws Exception {
        ConvertSource source = new ConvertSource(1, "hello", "world", new ConvertOther(999));
        ConvertTarget realTarget = new ConvertTarget(1, null, null, new ConvertOther(999));
        ConvertTarget convertTarget = ConvertUtils.convert(source, ConvertTarget.class);
        assertEquals(realTarget.toString(), convertTarget.toString());
    }

    @Test
    public void testConvert4List() throws Exception {
        List<ConvertSource> sourceList = new ArrayList<>();
        List<ConvertTarget> realTargetList = new ArrayList<>();
        List<ConvertTarget> convertTargetList = ConvertUtils.convert(sourceList, ConvertTarget.class);
        assertEquals(realTargetList.size(), convertTargetList.size());
        
        sourceList.add(new ConvertSource(1, "hello", "world", new ConvertOther(999)));
        sourceList.add(new ConvertSource(2, "hello", "world", new ConvertOther(998)));
        realTargetList.add(new ConvertTarget(1, null, null, new ConvertOther(999)));
        realTargetList.add(new ConvertTarget(2, null, null, new ConvertOther(998)));
        convertTargetList = ConvertUtils.convert(sourceList, ConvertTarget.class);
        for ( int i=0; i<convertTargetList.size(); i++ ){
            assertEquals(realTargetList.get(i).toString(), convertTargetList.get(i).toString());
        }
    }
}
