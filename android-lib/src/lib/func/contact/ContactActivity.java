package lib.func.contact;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lib.ui.R;
import android.app.Activity;
import android.os.Bundle;


/**
 * @ClassName:  ContactActivity.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-6-19 下午3:14:58
 * @Copyright: 版权由 HundSun 拥有
 */
public class ContactActivity extends Activity
{
    private List<ContactVo> contactList = new ArrayList<ContactVo>();
    private Set<String> contactPhoneSet = new HashSet<String>();
    
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.contact_main);  
        /*ContactDao.getPhoneContacts(this, contactList, contactPhoneSet);
        ContactDao.getSIMContacts(this, contactList, contactPhoneSet);*/
        
        
        //ContactDao.printPhoneContacts(this);
        //ContactDao.getAllContactsInfo(this);
        
        ContactDao.insertPhoneContact(this);
        
    }
}
