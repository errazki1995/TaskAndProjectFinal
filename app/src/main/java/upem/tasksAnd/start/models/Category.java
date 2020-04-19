package upem.tasksAnd.start.models;

public class Category {
    private int categoryid;
    private String categoryName;
    private String iconName;
    public Category(int categoryid, String categoryName) {
        this.categoryid = categoryid;
        this.categoryName = categoryName;
    }
    public Category(String categoryName,String iconName) {
        this.iconName=iconName;
        this.categoryName = categoryName;
    }
    public Category(int categoryid,String categoryName,String iconName) {
        this.categoryid= categoryid;
        this.iconName=iconName;
        this.categoryName = categoryName;
    }
    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
