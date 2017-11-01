import org.junit.Test;
import org.zhuduan.convertor.ConvertUtils;
import org.zhuduan.convertor.common.NotConvert;

import static org.junit.Assert.assertEquals;

/**
 * purpose of this class
 *
 * @author Haifeng.Zhu
 *         created at 11/1/17
 */
public class ConvertUtilsTestCase {

    class Other{
        private int id =0;

        public Other(int id) {
            this.id = id;
        }

        @Override
        public String toString(){
            return "otherID="+id;
        }
    }
    
    class Source {
        private Integer id;
        
        private String name;
        
        private String title;
        
        private Other other;

        public Source(Integer id, String name, String title, Other other) {
            this.id = id;
            this.name = name;
            this.title = title;
            this.other = other;
        }
    }
    
    class Target {
        private Integer id;
        
        private String noName;
        
        @NotConvert
        private String title;
        
        private Other other;
        
        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("id=").append(this.id).append(",")
                    .append("noName=").append(this.noName).append(",")
                    .append("title=").append(this.title).append(",")
                    .append("other=").append(this.other.toString()).append(",");
            return sb.toString();
        }

        public Target(Integer id, String noName, String title, Other other) {
            this.id = id;
            this.noName = noName;
            this.title = title;
            this.other = other;
        }
    }
    
    @Test
    public void testConvert4Single() throws Exception {
        Source source = new Source(1, "hello", "world", new Other(999));
        Target realTarget = new Target(1, null, null, new Other(999));
        Target convertTarget = ConvertUtils.convert(source, Target.class);
        assertEquals(realTarget.toString(), convertTarget.toString());
    }

    @Test
    public void testConvert4List() throws Exception {
    }
}
