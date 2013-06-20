package lib.func.contact;

import java.util.List;
import java.util.Set;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Contacts.People;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.text.TextUtils;
import android.util.Log;

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

	public static void printContacts(Context context) {

		Cursor cur = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if (cur != null && cur.moveToFirst()) {
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
			do {
				StringBuilder contactInfo = new StringBuilder();
				String contactId = cur.getString(idColumn);
				String disPlayName = cur.getString(displayNameColumn);
				contactInfo.append(ContactsContract.Contacts.DISPLAY_NAME + ":"
						+ disPlayName);
				int phoneCount = cur.getInt(phoneCountColumn);
				contactInfo.append(ContactsContract.Contacts.HAS_PHONE_NUMBER
						+ ":" + phoneCount);
				contactInfo.append(",<->,");
				Log.i(TAG, contactInfo.toString());

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
						Log.i(TAG, "street:" + street + ",city：" + city
								+ ",region：" + region + ",postCode:" + postCode
								+ ",formatAddress:" + formatAddress);

					} while (addressCursor.moveToNext());
					addressCursor.close();
				}
				/* 获取该联系人邮箱 */
				Cursor emailCursor = context.getContentResolver().query(
						ContactsContract.CommonDataKinds.Email.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				if (emailCursor.moveToFirst()) {
					do {
						String emailType = emailCursor
								.getString(emailCursor
										.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
						String emailValue = emailCursor
								.getString(emailCursor
										.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
						Log.i(TAG, "emailType:" + emailType + ",emailValue"
								+ emailValue);
					} while (emailCursor.moveToNext());
					emailCursor.close();
				}

				/* 获取该联系人IM */
				Cursor IMCursor = context.getContentResolver().query(
						Data.CONTENT_URI,
						new String[] { Data._ID, Im.PROTOCOL, Im.DATA },
						Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"
								+ Im.CONTENT_ITEM_TYPE + "'",
						new String[] { contactId }, null);
				if (IMCursor.moveToFirst()) {
					do {
						String protocol = IMCursor.getString(IMCursor
								.getColumnIndex(Im.PROTOCOL));
						String date = IMCursor.getString(IMCursor
								.getColumnIndex(Im.DATA));
						Log.i(TAG,"protocol:"+protocol+",date:"+date);
					} while (IMCursor.moveToNext());
					IMCursor.close();
				}
				
				/* 获取该联系人组织 */  
                Cursor organizationCursor = context.getContentResolver().query(  
                        Data.CONTENT_URI,  
                        new String[] { Data._ID, Organization.COMPANY,  
                                Organization.TITLE,Organization.TYPE},  
                        Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"  
                                + Organization.CONTENT_ITEM_TYPE + "'",  
                        new String[] { contactId }, null);  
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
                        	typeLabel = cur.getString(organizationCursor
                                		.getColumnIndex(Organization.LABEL));
                        }else if (type == Organization.TYPE_WORK){
                        	typeLabel = "公司";	
                        }else if(type == Organization.TYPE_OTHER){
                        	typeLabel = "其他工作";
                        }
                        Log.i(TAG,"company:"+company+",title:"+title+",type:"+type+",typeLabel:"+typeLabel);
                    } while (organizationCursor.moveToNext());  
                    organizationCursor.close();
                }  
             // 获取备注信息  
                Cursor noteCursor = context.getContentResolver().query(  
                        Data.CONTENT_URI,  
                        new String[] { Data._ID, Note.NOTE },  
                        Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"  
                                + Note.CONTENT_ITEM_TYPE + "'",  
                        new String[] { contactId }, null);  
                if (noteCursor.moveToFirst()) {  
                    do {  
                        String noteinfo = noteCursor.getString(noteCursor  
                                .getColumnIndex(Note.NOTE));  
                        Log.i(TAG,"noteinfo:"+noteinfo);
                    } while (noteCursor.moveToNext());  
                    noteCursor.close();
                }  
  
                /* 获取nickname信息  */ 
                Cursor nicknameCursor = context.getContentResolver().query(  
                        Data.CONTENT_URI,  
                        new String[] { Data._ID, Nickname.NAME },  
                        Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"  
                                + Nickname.CONTENT_ITEM_TYPE + "'",  
                        new String[] { contactId }, null);  
                if (nicknameCursor.moveToFirst()) {  
                    do {  
                        String nickname_ = nicknameCursor.getString(nicknameCursor  
                                .getColumnIndex(Nickname.NAME));  
                        Log.i(TAG,"nickname:"+nickname_);  
                    } while (nicknameCursor.moveToNext());  
                    nicknameCursor.close();
                }  
                
				/* 获取联系人所有电话号码 */
				if (phoneCount > 0) {
					Cursor phoneCursor = context.getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + contactId, null, null);
					String[] phoneCNames = phoneCursor.getColumnNames();
					if (phoneCursor != null && phoneCursor.moveToFirst()) {
						do {
							contactInfo = new StringBuilder();
							/*for (String cName : phoneCNames) {
								contactInfo.append(cName
										+ ":"
										+ phoneCursor.getString(phoneCursor
												.getColumnIndex(cName)) + ",");
							}*/
							//Log.i(TAG, contactInfo.toString());
							String phoneNumber = phoneCursor  
                                    .getString(phoneCursor  
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));  
                            String phoneType = phoneCursor  
                                    .getString(phoneCursor  
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)); 
                            Log.i(TAG,"phoneNumber:"+phoneNumber+",phoneType:"+phoneType);
						} while (phoneCursor.moveToNext());
						phoneCursor.close();
					}
				}

			} while (cur.moveToNext());
			cur.close();
		}
	}

}