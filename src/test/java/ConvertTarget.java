import org.zhuduan.convertor.common.NotConvert;

/**
 * purpose of this class
 *
 * @author Haifeng.Zhu
 *         created at 11/2/17
 */
public class ConvertTarget {

    private Integer id;

    private String noName;
    
    private int name = -1;

    @NotConvert
    private String title;

    private ConvertOther other;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("id=").append(this.id).append(",")
                .append("noName=").append(this.noName).append(",")
                .append("title=").append(this.title).append(",")
                .append("other=").append(this.other.toString()).append(",");
        return sb.toString();
    }

    public ConvertTarget(Integer id, String noName, String title, ConvertOther other) {
        this.id = id;
        this.noName = noName;
        this.title = title;
        this.other = other;
    }

    public ConvertTarget(){}
}
