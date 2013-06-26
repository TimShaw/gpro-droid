package lib.func.contact.po;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import config.Constants;

public class PhoneContactList<E> extends ArrayList<E> {
	
	private static final long serialVersionUID = 1L;

	 
	public String toJSONStringForBackUp(){
		JSONArray contactsArray = new JSONArray();
		//StringBuffer content = new StringBuffer();
		//content.append("[");
		try {
			for(Object obj : this){
				
				PhoneContact contact = (PhoneContact)obj;
					 
					JSONObject contactJobj = new JSONObject();
					contactJobj.put("name", contact.getName());
					contactJobj.put("photo", contact.getPhoto());
					contactJobj.put("website", contact.getName());
					contactJobj.put("birthday", contact.getBirthday());
					contactJobj.put("nickname", contact.getNickname());
					contactJobj.put("customRingtone", contact.getCustomRingtone());
					contactJobj.put("groupIds", contact.getGroupIds());
					contactJobj.put("storeType", contact.getStoreType());
					
					JSONArray dataArray = new JSONArray();
					
					for(PhoneContactData data:contact.getPhoneContactDatas()){
						JSONObject dataObj = new JSONObject();
						dataObj.put("dataCategoryId", data.getDataCategoryId());
						dataObj.put("dataValue", data.getDataValue());
						if(data.getDataCategoryId()==Constants.CONTACT_DATA_TYPE_CUSTOM){
							dataObj.put("dataCustomLabel", data.getDataCustomLabel());
						}
						dataObj.put("dataTotalType", data.getDataTotalType());
						dataArray.put(dataObj);
					}
					contactJobj.put("datas", dataArray);
					//String contactJsonStr = contactJobj.toString();
					//content.append(contactJsonStr+",");
					contactsArray.put(contactJobj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//content.append("]");
		return contactsArray.toString();
	}
	 
}
