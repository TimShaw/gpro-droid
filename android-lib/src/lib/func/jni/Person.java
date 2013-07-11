package lib.func.jni;

/**
 * @ClassName:  Person.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-7-11 下午3:03:35
 * @Copyright: 版权由 HundSun 拥有
 */
public class Person
{
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public int getAge()
    {
        return age;
    }
    
    public void setAge(int age)
    {
        this.age = age;
    }
    
    public float getHeight()
    {
        return height;
    }
    
    public void setHeight(float height)
    {
        this.height = height;
    }
    private String name;
    private int age;
    private float height;
    
}
