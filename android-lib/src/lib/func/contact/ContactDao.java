package lib.func.contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lib.func.contact.po.PhoneContact;
import lib.func.contact.po.PhoneContactData;
import lib.func.contact.po.PhoneContactList;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Groups;
import android.provider.ContactsContract.RawContacts;
import android.text.TextUtils;
import android.util.Log;
import config.Constants;

public class ContactDao {

	private static String TAG = ContactDao.class.getSimpleName();

	/**
	 * 得到手机通讯录联系人信息
	 * 
	 * @param context
	 * @param contactList
	 * @param contactPhoneSet
	 *            为null表示不考虑重复
	 */
	public static void getPhoneContacts(Context context,
			List<ContactVo> contactList, Set<String> contactPhoneSet) {
		ContentResolver resolver = context.getContentResolver();
		final String[] PHONES_PROJECTION = new String[] { Phone.NUMBER,
				Phone.DISPLAY_NAME, Phone.CONTACT_ID, Phone.SORT_KEY_PRIMARY,
				Phone.SORT_KEY_ALTERNATIVE };
		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				ContactVo cv = new ContactVo();
				// 得到手机号码
				String phoneNumber = phoneCursor.getString(0);

				// log.info("getPhoneContacts... phoneNumber:"+phoneNumber);
				if (contactPhoneSet != null) {
					if (contactPhoneSet.contains(phoneNumber)) {
						continue;
					}
					contactPhoneSet.add(phoneNumber);
				}

				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;

				String contactName = phoneCursor.getString(1);
				Long contactId = phoneCursor.getLong(2);

				// 得到联系人头像ID
				// Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

				// 得到联系人头像Bitamp
				Bitmap contactPhoto = null;

				// photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
				/*
				 * if (photoid > 0) { Uri uri =
				 * ContentUris.withAppendedId(ContactsContract
				 * .Contacts.CONTENT_URI, contactid); InputStream input =
				 * ContactsContract
				 * .Contacts.openContactPhotoInputStream(resolver, uri);
				 * contactPhoto = BitmapFactory.decodeStream(input); } else {
				 * contactPhoto = BitmapFactory.decodeResource(getResources(),
				 * R.drawable.contact_photo); }
				 */

				cv.displayName = contactName;
				cv.phone = phoneNumber;
				cv.contactId = contactId;
				// cv.firstChineseLetter = contactName.substring(0,1);
				String sortKey = phoneCursor.getString(3);
				String sortKeyAlt = phoneCursor.getString(4);
				Log.i(TAG, "phone contacts ----- displayName: " + contactName
						+ ",phone: " + phoneNumber + ",contactId: " + contactId
						+ ",sort_key: " + sortKey + ",sortKeyAlt:" + sortKeyAlt);
				contactList.add(cv);
			}
			phoneCursor.close();
		}
	}

	/**
	 * 得到手机SIM卡联系人人信息 注意：Sim卡中没有联系人头像
	 * 
	 * @param context
	 * @param contactList
	 * @param contactPhoneSet
	 *            为null表示不考虑重复
	 */
	public static void getSIMContacts(Context context,
			List<ContactVo> contactList, Set<String> contactPhoneSet) {
		/*
		 * ???????????? 数组次序不起作用.... final String[] PHONES_PROJECTION = new
		 * String[] {People.NUMBER,People.NAME,People._ID};
		 */

		ContentResolver resolver = context.getContentResolver();
		// 获取Sims卡联系人
		Uri uri = Uri.parse("content://icc/adn");
		Cursor phoneCursor = resolver.query(uri, null, null, null, null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				ContactVo cv = new ContactVo();

				String phoneNumber = phoneCursor.getString(phoneCursor
						.getColumnIndex("number"));
				// log.info("getSIMContacts... phoneNumber:"+phoneNumber);
				if (contactPhoneSet != null) {
					if (contactPhoneSet.contains(phoneNumber)) {
						continue;
					}
					contactPhoneSet.add(phoneNumber);
				}
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				String contactName = phoneCursor.getString(phoneCursor
						.getColumnIndex(People.NAME));
				Long contactId = phoneCursor.getLong(phoneCursor
						.getColumnIndex(People._ID));
				cv.displayName = contactName;
				cv.phone = phoneNumber;
				cv.contactId = contactId;
				Log.i(TAG, "SIM contacts ----- displayName: " + contactName
						+ ",phone: " + phoneNumber + ",contactId: " + contactId);
				contactList.add(cv);
			}

			phoneCursor.close();
		}
	}

	/**
	 * 根据recipientId, 从CanonicalAddresses表中取出电话号码(address)
	 * 
	 * @param context
	 * @param recipientId
	 * @return
	 */
	public static String getAddressFromCanonicalAddressesTable(Context context,
			String recipientId) {
		Cursor addressCursor = context.getContentResolver()
				.query(Uri.parse("content://mms-sms/canonical-address/"
						+ recipientId), null, null, null, null);
		StringBuilder address = new StringBuilder();
		while (addressCursor.moveToNext()) {
			String _address = addressCursor.getString(addressCursor
					.getColumnIndex("address"));
			address.append("address:" + _address);
		}
		addressCursor.close();
		Log.i(TAG, "address : " + address);
		return address.toString();
	}

	public static void printSIMContacts(Context context) {

		ContentResolver resolver = context.getContentResolver();
		// 获取Sims卡联系人
		Uri uri = Uri.parse("content://icc/adn");
		Cursor phoneCursor = resolver.query(uri, null, null, null, null);

		if (phoneCursor != null) {
			String[] cNames = phoneCursor.getColumnNames();
			;
			while (phoneCursor.moveToNext()) {
				StringBuilder sb = new StringBuilder();
				for (String cName : cNames) {
					sb.append(cName
							+ ":"
							+ phoneCursor.getString(phoneCursor
									.getColumnIndex(cName)) + ",");
				}
				Log.i(TAG, sb.toString());
			}
			phoneCursor.close();
		}
	}

	public static void printPhoneContacts(Context context) {

		ContentResolver resolver = context.getContentResolver();
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, null, null,
				null, null);

		if (phoneCursor != null) {
			String[] cNames = phoneCursor.getColumnNames();
			while (phoneCursor.moveToNext()) {
				StringBuilder sb = new StringBuilder();
				for (String cName : cNames) {
					sb.append(cName
							+ ":"
							+ phoneCursor.getString(phoneCursor
									.getColumnIndex(cName)) + ",");
				}
				Log.i("printPhoneContacts", sb.toString());
			}
			phoneCursor.close();
		}

	}
	 
	
	public static List<Long> getGroupIdByContactId(Context context,Long contactId){
        List<Long> groupIdList = new ArrayList<Long>();
        Uri uri = Data.CONTENT_URI;
        String where = String.format(
                "%s = ? AND %s = ?",
                Data.MIMETYPE,
                GroupMembership.CONTACT_ID);

        String[] whereParams = new String[] {
                   GroupMembership.CONTENT_ITEM_TYPE,
                   Long.toString(contactId),
        };

        String[] selectColumns = new String[]{
                GroupMembership.GROUP_ROW_ID,
        };


        Cursor groupIdCursor = context.getContentResolver().query(
                uri, 
                selectColumns, 
                where, 
                whereParams, 
                null);
        try{
            while (groupIdCursor.moveToNext()) {
                groupIdList.add(groupIdCursor.getLong(0));
            }
            return groupIdList; // Has no group ...
        }finally{
            groupIdCursor.close();
        }
    }
    
    public static String getGroupNameByGroupId(Context context,long groupId){
        Uri uri = Groups.CONTENT_URI;
        String where = String.format("%s = ?", Groups._ID);
        String[] whereParams = new String[]{Long.toString(groupId)};
        String[] selectColumns = {Groups.TITLE};
        Cursor c = context.getContentResolver().query(
                uri, 
                selectColumns,
                where, 
                whereParams, 
                null);

        try{
            if (c.moveToFirst()){
                return c.getString(0);  
            }
            return null;
        }finally{
            c.close();
        }
    }
    
    /**
     * 获取生日，仅一个
     * @param context
     * @param contactId
     * @return
     */
    public static String getBirthday(Context context,long contactId){
            
        String columns[] = {
             ContactsContract.CommonDataKinds.Event.START_DATE,
             ContactsContract.CommonDataKinds.Event.TYPE,
             ContactsContract.CommonDataKinds.Event.MIMETYPE,
        };

        String where = Event.TYPE + "=" + Event.TYPE_BIRTHDAY + 
                        " and " + Event.MIMETYPE + " = '" + Event.CONTENT_ITEM_TYPE + "' and "  
                + ContactsContract.Data.CONTACT_ID + " = " + contactId;
        String[] selectionArgs = null;
        Cursor birthdayCur = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, columns, where, selectionArgs, null); 
       String birthday = null;
        if (birthdayCur.getCount() > 0) {
            if (birthdayCur.moveToNext()) {
                 birthday = birthdayCur.getString(birthdayCur.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
            }
        }
        birthdayCur.close();
        return birthday;
    }
    
    /**
     * 获取该联系人地址
     * 
     * @param context
     * @param contactId
     * @param datas
     */
    public static void getContactAddress(Context context,long contactId,List<PhoneContactData> datas){
        /* 获取该联系人地址 */
        Cursor addressCursor = context
                .getContentResolver()
                .query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                + " = " + contactId, null, null);
        if (addressCursor.moveToFirst()) {
            do {
                String street = addressCursor
                        .getString(addressCursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                String city = addressCursor
                        .getString(addressCursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                String region = addressCursor
                        .getString(addressCursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                String postCode = addressCursor
                        .getString(addressCursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                String formatAddress = addressCursor
                        .getString(addressCursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
                String country = addressCursor.getString(addressCursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                String typeLabel = "";
                int type = addressCursor
                        .getInt(addressCursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
                if(type == ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM){
                    typeLabel = addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.LABEL));
                }else{
                    typeLabel = ContactsContract.CommonDataKinds.StructuredPostal.getTypeLabel(context.getResources(), type, null).toString();
                }
                PhoneContactData data = new PhoneContactData();
                data.setDataCategoryId(type);
                data.setDataCustomLabel(typeLabel);
                data.setDataTotalType(Constants.CONTACT_DATA_TOATAL_TYPE_ADDRESS);
                String dataValue = country+","+region+","+city+","+street+","+postCode;
                data.setDataValue(dataValue);
                datas.add(data);
                
                Log.i(TAG, "street:" + street + ",city：" + city
                        + ",region：" + region + ",postCode:" + postCode
                        + ",formatAddress:" + formatAddress+",typeLabel:"+typeLabel);

            } while (addressCursor.moveToNext());
            addressCursor.close();
        }
    }
    /**
     * 获取联系人邮箱
     * @param context
     * @param contactId
     * @param datas
     */
    public static void getContactEmail(Context context,long contactId,List<PhoneContactData> datas){
        Cursor emailCursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                        + " = " + contactId, null, null);
        
        if (emailCursor.moveToFirst()) {
            do {
                int emailType = emailCursor
                        .getInt(emailCursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                String emailValue = emailCursor
                        .getString(emailCursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                String emailTypeLabel  ="";
                if(emailType == Email.TYPE_CUSTOM){
                    emailTypeLabel = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.LABEL));
                }else{
                    emailTypeLabel = Email.getTypeLabel(context.getResources(), emailType, null).toString();
                }
                PhoneContactData data = new PhoneContactData();
                data.setDataCategoryId(emailType);
                data.setDataCustomLabel(emailTypeLabel);
                data.setDataTotalType(Constants.CONTACT_DATA_TOATAL_TYPE_EMAIL);
                data.setDataValue(emailValue);
                datas.add(data);
                Log.i(TAG, "emailType:" + emailType + ",emailValue"
                        + emailValue+",emailTypeLabel:"+emailTypeLabel);
            } while (emailCursor.moveToNext());
            emailCursor.close();
        }
    }
    
    /**
     * 获取IM
     * 暂不区分协议
     * @param context
     * @param contactId
     * @param datas
     */
    public static void getContactIm(Context context,long contactId,List<PhoneContactData> datas){
        Cursor IMCursor = context.getContentResolver().query(
                Data.CONTENT_URI,
                new String[] { Data._ID, Im.PROTOCOL, Im.DATA,Im.TYPE },
                Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"
                        + Im.CONTENT_ITEM_TYPE + "'",
                new String[] { String.valueOf(contactId) }, null);
        if (IMCursor.moveToFirst()) {
            do {
                int protocol = IMCursor.getInt(IMCursor
                        .getColumnIndex(Im.PROTOCOL));
                String dataValue = IMCursor.getString(IMCursor
                        .getColumnIndex(Im.DATA));
                String typeLabel = "";
                int type = IMCursor.getInt(IMCursor
                        .getColumnIndex(Im.TYPE));
                if(type == Im.TYPE_CUSTOM){
                    typeLabel = IMCursor.getString(IMCursor
                            .getColumnIndex(Im.LABEL));
                }else {
                    typeLabel = Im.getTypeLabel(context.getResources(), protocol, null).toString();
                }
                PhoneContactData data = new PhoneContactData();
                data.setDataCategoryId(type);
                data.setDataCustomLabel(typeLabel);
                data.setDataTotalType(Constants.CONTACT_DATA_TOATAL_TYPE_IM);
                data.setDataValue(dataValue);
                datas.add(data);
                Log.i(TAG,"protocol:"+protocol+",date:"+dataValue+",type:"+type+",typeLabel:"+typeLabel);
            } while (IMCursor.moveToNext());
            IMCursor.close();
        }
    }
    /**
     * 获取组织信息，用逗号分隔，如：阿里巴巴,开发经理
     *
     * @param context
     * @param contactId
     * @param datas
     */
    public static void getContactOrg(Context context,long contactId,List<PhoneContactData> datas){
        Cursor organizationCursor = context.getContentResolver().query(  
                Data.CONTENT_URI,  
                new String[] { Data._ID, Organization.COMPANY,  
                        Organization.TITLE,Organization.TYPE,Organization.LABEL},  
                Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"  
                        + Organization.CONTENT_ITEM_TYPE + "'",  
                new String[] { String.valueOf(contactId) }, null);  
        if (organizationCursor.moveToFirst()) {  
            do {  
                String company = organizationCursor.getString(organizationCursor  
                        .getColumnIndex(Organization.COMPANY));  
                String title = organizationCursor.getString(organizationCursor  
                        .getColumnIndex(Organization.TITLE));  
                int type = organizationCursor.getInt(organizationCursor  
                        .getColumnIndex(Organization.TYPE));
                String typeLabel = "";
                
                if (type == Organization.TYPE_CUSTOM) {
                    typeLabel = organizationCursor.getString(organizationCursor
                                .getColumnIndex(Organization.LABEL));
                }else {
                    typeLabel = Organization.getTypeLabel(context.getResources(), type, null).toString();
                }
                PhoneContactData data = new PhoneContactData();
                data.setDataCategoryId(type);
                data.setDataCustomLabel(typeLabel);
                data.setDataTotalType(Constants.CONTACT_DATA_TOATAL_TYPE_ORG);
                data.setDataValue(company+","+title);
                datas.add(data);
                Log.i(TAG,"company:"+company+",title:"+title+",type:"+type+",typeLabel:"+typeLabel);
            } while (organizationCursor.moveToNext());  
            organizationCursor.close();
        }  
    }
    
    /**
     * 获取备注，仅取一个
     * @param context
     * @param contactId
     * @return
     */
    public static String getContactNote(Context context,long contactId){
        Cursor noteCursor = context.getContentResolver().query(  
                Data.CONTENT_URI,  
                new String[] { Data._ID, Note.NOTE },  
                Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"  
                        + Note.CONTENT_ITEM_TYPE + "'",  
                new String[] { String.valueOf(contactId) }, null);  
        String noteinfo = null;
        if (noteCursor.moveToNext()) {  
            noteinfo = noteCursor.getString(noteCursor  
                    .getColumnIndex(Note.NOTE));  
            Log.i(TAG,"noteinfo:"+noteinfo);
        }
        noteCursor.close();
        return noteinfo;
    }
    /**
     * 获取昵称 仅取一个
     * @param context
     * @param contactId
     * @return
     */
    public static String getContactNickName(Context context,long contactId){
         Cursor nicknameCursor = context.getContentResolver().query(  
                 Data.CONTENT_URI,  
                 new String[] { Data._ID, Nickname.NAME},  
                 Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"  
                         + Nickname.CONTENT_ITEM_TYPE + "'",  
                 new String[] { String.valueOf(contactId) }, null); 
         String nickname_ = null;
         if (nicknameCursor.moveToFirst()) {  
             do {  
                 nickname_  = nicknameCursor.getString(nicknameCursor  
                         .getColumnIndex(Nickname.NAME));  
                 Log.i(TAG,"nickname:"+nickname_);  
             } while (nicknameCursor.moveToNext());  
             nicknameCursor.close();
         }  
         return nickname_;
    }
    
    /**
     * 获取联系电话
     * @param context
     * @param contactId
     * @param datas
     */
    public static void getContactTel(Context context,long contactId,List<PhoneContactData> datas){
        Cursor phoneCursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                        + " = " + contactId, null, null);
        if (phoneCursor != null && phoneCursor.moveToFirst()) {
            do {
                 
                String phoneNumber = phoneCursor  
                        .getString(phoneCursor  
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));  
                int phoneType = phoneCursor  
                        .getInt(phoneCursor  
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)); 
                String phoneTypeLabel = "";
                if(phoneType == CommonDataKinds.Phone.TYPE_CUSTOM){
                    phoneTypeLabel = phoneCursor  
                            .getString(phoneCursor  
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));  
                }else{
                    phoneTypeLabel = ContactsContract.CommonDataKinds.Phone
                            .getTypeLabel(context.getResources(), phoneType, null).toString();
                }
                PhoneContactData data = new PhoneContactData();
                data.setDataCategoryId(phoneType);
                data.setDataCustomLabel(phoneTypeLabel);
                data.setDataTotalType(Constants.CONTACT_DATA_TOATAL_TYPE_TEL);
                data.setDataValue(phoneNumber);
                datas.add(data);
                Log.i(TAG,"phoneNumber:"+phoneNumber+",phoneType:"+phoneType+",phoneTypeLabel:"+phoneTypeLabel);
            } while (phoneCursor.moveToNext());
            phoneCursor.close();
        }
    }
    /**
     * 获取联系人头像
     * 
     * @param people_id
     * @return
     */
    public static byte[] getPhoto(Context context,long contactId) {
            String photo_id = null;
            String selection1 = ContactsContract.Contacts._ID + " = " + contactId;
            Cursor cur1 = context.getContentResolver().query(
                            ContactsContract.Contacts.CONTENT_URI, null, selection1, null,
                            null);
            if (cur1.getCount() > 0) {
                    cur1.moveToFirst();
                    photo_id = cur1.getString(cur1
                                    .getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
                    Log.i(TAG, "photo_id:" + photo_id);   // 如果没有头像，这里为空值
            }
            
            String selection = null;
            if(photo_id == null){                        
                    return null;
            }else{
                    selection = ContactsContract.Data._ID + " = " + photo_id;
            }
            
            String[] projection = new String[] { ContactsContract.Data.DATA15 };
            Cursor cur = context.getContentResolver().query(
                            ContactsContract.Data.CONTENT_URI, projection, selection, null, null);
            cur.moveToFirst();
            byte[] contactIcon = cur.getBlob(0);
            Log.i(TAG, "conTactIcon:" + contactIcon);
            if (contactIcon == null) {
                    return null;
            } else {
                    return contactIcon;
            }
    }
    public static PhoneContactList<PhoneContact> getAllContactsInfo(Context context) {
        PhoneContactList<PhoneContact> phoneContacts = new PhoneContactList<PhoneContact>();
        
        Cursor cur = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cur != null && cur.moveToNext()) {
            PhoneContact phoneContact = new PhoneContact();
            String[] cNames = cur.getColumnNames();
            StringBuilder columns = new StringBuilder();
            for (String cName : cNames) {
                columns.append(cName + ",");
            }
            Log.i(TAG, columns.toString());

            int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);

            int displayNameColumn = cur
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int phoneCountColumn = cur
                    .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
            String customRingtone = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.CUSTOM_RINGTONE));
            
            StringBuilder contactInfo = new StringBuilder();
            long contactId = cur.getLong(idColumn);
            String disPlayName = cur.getString(displayNameColumn);
             
            int phoneCount = cur.getInt(phoneCountColumn);
            contactInfo.append(ContactsContract.Contacts.DISPLAY_NAME + ":"+ disPlayName+
                    ","+ContactsContract.Contacts.HAS_PHONE_NUMBER+ ":" + phoneCount+
                    ",customRingtone:"+customRingtone);
            
            contactInfo.append(",<->,");
            Log.i(TAG, contactInfo.toString());
            
            phoneContact.setName(disPlayName);
            phoneContact.setCustomRingtone(customRingtone);
            
            /* 
             * 获取头像 */
            
            byte[] photoBytes = ContactDao.getPhoto(context, contactId);
            if(photoBytes!=null){
                String photo = android.util.Base64.encodeToString(photoBytes, android.util.Base64.NO_WRAP);
                Log.i(TAG, "photo:"+photo);
                phoneContact.setPhoto(photo);
            }
            
            /* 获取生日 */
            String birthday = ContactDao.getBirthday(context, contactId);
            if(birthday!=null){
                phoneContact.setBirthday(birthday);
                Log.i(TAG,"birthday:"+birthday);
            }
            
            String groupIds = "";
            /*获取分组信息*/
            List<Long> groupIdList = ContactDao.getGroupIdByContactId(context, contactId);
            if(groupIdList.size()>0){
                for(Long groupId:groupIdList){
                    groupIds+=groupId+","; 
                    String groupName = ContactDao.getGroupNameByGroupId(context, groupId);
                    if(groupName!=null){
                        Log.i(TAG,"groupName:"+groupName+",groupId:"+groupId);
                    }
                }
            }
            phoneContact.setGroupIds(groupIds);
            
            List<PhoneContactData> datas = phoneContact.getPhoneContactDatas();
            /*获取联系人地址*/
            ContactDao.getContactAddress(context, contactId, datas);
            
            /* 获取该联系人邮箱 */
            ContactDao.getContactEmail(context, contactId, datas);

            /* 获取该联系人IM */
            ContactDao.getContactIm(context, contactId, datas);
            
            /* 获取该联系人组织 */  
            ContactDao.getContactOrg(context, contactId, datas);
            
            // 获取备注信息  
            phoneContact.setNote(ContactDao.getContactNote(context, contactId));
  
                /* 获取nickname信息  */ 
            phoneContact.setNickname(ContactDao.getContactNickName(context, contactId));
            
            /* 获取联系人所有电话号码 */
            if (phoneCount > 0) {
                ContactDao.getContactTel(context, contactId, datas);
            }
            phoneContacts.add(phoneContact);
        }
        cur.close();
        return phoneContacts;
    }
    
    public static void insertPhoneContact(Context context){
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();  
        
        int rawContactInsertIndex =ops.size();  
        ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)  
                .withValue(RawContacts.ACCOUNT_TYPE, null)  
                .withValue(RawContacts.ACCOUNT_NAME, null)  
                .build());  
        ops.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI) 
                .withValueBackReference(Data.RAW_CONTACT_ID,rawContactInsertIndex) 
                .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)  
                .withValue(StructuredName.GIVEN_NAME, "赵薇") 
                .build());  
        
         // 更新手机号码：Data.RAW_CONTACT_ID 获取上一条语句插入联系人时产生的 ID 
        ops.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI) 
                 .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex) 
                 .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)  
                 .withValue(Phone.NUMBER, "13671323809")  // "data1"  
                 .withValue(Phone.TYPE, Phone.TYPE_MOBILE)  
                 .withValue(Phone.LABEL, "手机号") 
                 .build());  
        ops.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI) 
                 .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex) 
                 .withValue(Data.MIMETYPE,Email.CONTENT_ITEM_TYPE)  
                 .withValue(Email.DATA, "liming@itcast.cn") 
                 .withValue(Email.TYPE, Email.TYPE_WORK) 
                 .build());  
           
         // 批量插入 --在同一个事务当中  
        ContentProviderResult[] results;
        try
        {
            results = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            for(ContentProviderResult result : results){
                Log.i(TAG,result.uri.toString());  
            }
        } catch (RemoteException e)
        {
            e.printStackTrace();
        } catch (OperationApplicationException e)
        {
            e.printStackTrace();
        }  
         
    }

}
