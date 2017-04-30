package com.ma.util;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.annotations.JsonAdapter;
import com.ma.model.ClassContent;
import com.ma.model.ClassTitle;
import com.ma.model.Discuss;
import com.ma.model.Exercise;
import com.ma.model.ExerciseContent;
import com.ma.model.Forum;
import com.ma.model.Project;
import com.ma.model.User;
import com.ma.model.UserInfo;

/**
 * @ClassName: JsonUtil  
 * @Description: json工具类  
 * @author MZ  
 * @2017年3月2日下午7:09:35
 */
public class JsonUtil {
	/**
	 * @Title: dateToJson  
	 * @Description: 将数据转换为json格式  
	 * @param @param user
	 * @param @return  
	 * @return String  
	 * @throws
	 */
	public static String dateToJson(User user){
		
		JSONArray array = new JSONArray();
		JSONObject jsonObjArr = new JSONObject();
		try {
			jsonObjArr.put("id", user.getId());
			jsonObjArr.put("username", user.getUsername());
			jsonObjArr.put("user_img", user.getImg());
			jsonObjArr.put("password", user.getPassword());
			array.put(jsonObjArr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return array.toString();
	}
	
	/**
	 * @Title: infoToJson  
	 * @Description: 将用户信息转换成json格式  
	 * @param @param userInfo
	 * @param @return  
	 * @return String  
	 * @throws
	 */
	public static String infoToJson(UserInfo userInfo){
		JSONArray array = new JSONArray();
		JSONObject jsonObjArr = new JSONObject();
		try {
			jsonObjArr.put("sex", userInfo.getSex());
			jsonObjArr.put("age", userInfo.getAge());
			jsonObjArr.put("profession", userInfo.getProfession());
			jsonObjArr.put("phone", userInfo.getPhone());
			jsonObjArr.put("email", userInfo.getEmail());
			jsonObjArr.put("constellation", userInfo.getConstellation());
			jsonObjArr.put("home", userInfo.getHome());
			jsonObjArr.put("description", userInfo.getDescription());
			array.put(jsonObjArr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return array.toString();
	}
	
	/**
	 * @Title: titleToJson  
	 * @Description: 将课程题数据转换成json数据  
	 * @param @param ct
	 * @param @return  
	 * @return String  
	 * @throws
	 */
	public static String titleToJson(List<ClassTitle> ct){
		JSONArray array = new JSONArray();
		try {
			for (int i = 0; i < ct.size(); i++) {
				JSONObject jsonObjArr = new JSONObject();
				jsonObjArr.put("id", ct.get(i).getId());
				jsonObjArr.put("c_name", ct.get(i).getC_name());
				jsonObjArr.put("c_img", ct.get(i).getC_img());
				//添加标志信息
				//jsonObjArr.put("msg",ct.get(i).getMsg());
				array.put(jsonObjArr);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "\""+"data"+"\""+":"+array.toString();
		//String s ="{"+"\""+"data"+"\""+":"+array.toString()+"}";
	}
	/**
	 * @Title: titleToJson2  
	 * @Description: 添加进度后的  
	 * @param @param ct
	 * @param @return  
	 * @return String  
	 * @throws
	 */
	public static String titleToJson2(List<ClassTitle> ct){
		JSONArray array = new JSONArray();
		try {
			for (int i = 0; i < ct.size(); i++) {
				JSONObject jsonObjArr = new JSONObject();
				jsonObjArr.put("id", ct.get(i).getId());
				jsonObjArr.put("c_name", ct.get(i).getC_name());
				jsonObjArr.put("c_img", ct.get(i).getC_img());
				jsonObjArr.put("curPro", ct.get(i).getCurPro());
				jsonObjArr.put("allPro", ct.get(i).getAllPro());
				//jsonObjArr.put("msg",ct.get(i).getMsg());
				array.put(jsonObjArr);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "\""+"data"+"\""+":"+array.toString();
		//String s ="{"+"\""+"data"+"\""+":"+array.toString()+"}";
	}
	
	/**
	 * @Title: disToJson  
	 * @Description: 将论坛信息转换成Json数据  
	 * @param @param dis
	 * @param @return  
	 * @return String  
	 * @throws
	 */
	public static String disToJson(List<Discuss> dis){
		JSONArray array = new JSONArray();
		try {
			for (int i = 0; i < dis.size(); i++) {
				JSONObject jsonObjArr = new JSONObject();
				jsonObjArr.put("id", dis.get(i).getId());
				jsonObjArr.put("username", dis.get(i).getUsername());
				jsonObjArr.put("user_img", dis.get(i).getUser_img());
				jsonObjArr.put("d_time", dis.get(i).getD_time());
				jsonObjArr.put("d_content", dis.get(i).getD_content());
				array.put(jsonObjArr);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return array.toString();
	}
	
	/**
	 * @Title: conToJson  
	 * @Description: 将课程内容转换成json格式  
	 * @param @param cc
	 * @param @return  
	 * @return String  
	 * @throws
	 */
	public static String conToJson(ClassContent cc){
		JSONArray array = new JSONArray();
		JSONObject jsonObjArr = new JSONObject();
		try {
			jsonObjArr.put("id", cc.getId());
			jsonObjArr.put("c_content", cc.getC_content());
			array.put(jsonObjArr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return array.toString();
	
	}
	
	/**
	 * @Title: proToJson  
	 * @Description: 将课程数据转换成json格式  
	 * @param @param pro
	 * @param @return  
	 * @return String  
	 * @throws
	 */
	public static String proToJson(List<Project> pro){
		JSONArray array = new JSONArray();
		
		try {
			for (int i = 0; i < pro.size(); i++) {
				JSONObject jsonObjArr = new JSONObject();
				jsonObjArr.put("id", pro.get(i).getId());
				jsonObjArr.put("pro_name", pro.get(i).getPro_name());
				jsonObjArr.put("pro_image", pro.get(i).getPro_image());
				array.put(jsonObjArr);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return array.toString();
	}
	
	/**
	 * @Title: ExeToJson  
	 * @Description: 将练习数据转换成json格式  
	 * @param @param exe
	 * @param @return  
	 * @return String  
	 * @throws
	 */
	public static String ExeToJson(List<Exercise> exe){
		JSONArray array = new JSONArray();
		try {
			for (int i = 0; i < exe.size(); i++) {
				JSONObject jsonObjArr = new JSONObject();
				jsonObjArr.put("id", exe.get(i).getId());
				jsonObjArr.put("e_name", exe.get(i).getE_name());
				jsonObjArr.put("e_image", exe.get(i).getE_image());
				array.put(jsonObjArr);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return array.toString();
	}
	
	/**
	 * @Title: forumToJson  
	 * @Description: 将发贴内容转换成json格式  
	 * @param @param list
	 * @param @return  
	 * @return String  
	 * @throws
	 */
	public static String forumToJson(List<Forum> list){
		JSONArray array = new JSONArray();
		try {
			for (int i = 0; i < list.size(); i++) {
				JSONObject jsonObjArr = new JSONObject();
				jsonObjArr.put("id", list.get(i).getId());
				jsonObjArr.put("username", list.get(i).getUsername());
				jsonObjArr.put("user_img", list.get(i).getUser_img());
				jsonObjArr.put("f_time", list.get(i).getF_time());
				jsonObjArr.put("f_title", list.get(i).getF_title());
				jsonObjArr.put("f_content", list.get(i).getF_content());
				jsonObjArr.put("f_times", list.get(i).getF_times());
				jsonObjArr.put("user_id", list.get(i).getUser_id());
				jsonObjArr.put("f_img", list.get(i).getF_img());
				array.put(jsonObjArr);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return array.toString();
	}
	
	/**
	 * @Title: flagToJson  
	 * @Description: TODO  
	 * @param @param classTitle
	 * @param @return  
	 * @return String  
	 * @throws
	 */
	public static String flagToJson(ClassTitle classTitle){
		JSONArray array = new JSONArray();
		JSONObject jsonObjArr = new JSONObject();
		try {
			jsonObjArr.put("flag", classTitle.getMsg());
			array.put(jsonObjArr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return array.toString();
	}
	
	public static String contentToJson(List<ExerciseContent> cont){
		JSONArray array = new JSONArray();
		try {
			for (int i = 0; i < cont.size(); i++) {
				JSONObject jsonObjArr = new JSONObject();
				jsonObjArr.put("id", cont.get(i).getId());
				jsonObjArr.put("quest", cont.get(i).getQuest());
				jsonObjArr.put("a_choose", cont.get(i).getA_choose());
				jsonObjArr.put("b_choose", cont.get(i).getB_choose());
				jsonObjArr.put("c_choose", cont.get(i).getC_choose());
				jsonObjArr.put("d_choose", cont.get(i).getD_choose());
				jsonObjArr.put("answer", cont.get(i).getAnswer());
				array.put(jsonObjArr);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return array.toString();
	}
	/**
	 * @Title: myForumToJson  
	 * @Description: 将我的帖子转换成json数据  
	 * @param @param forumList
	 * @param @return  
	 * @return String  
	 * @throws
	 */
	public static String myForumToJson(List<Forum> forumList){
		JSONArray jsonArray = new JSONArray();
		
		try {
			for (int i = 0; i < forumList.size(); i++) {
			JSONObject jsonObjArr = new JSONObject();
			jsonObjArr.put("id", forumList.get(i).getId());
			jsonObjArr.put("username", forumList.get(i).getUsername());
			jsonObjArr.put("user_img", forumList.get(i).getUser_img());
			jsonObjArr.put("f_time", forumList.get(i).getF_time());
			jsonObjArr.put("f_title", forumList.get(i).getF_title());
			jsonObjArr.put("f_content", forumList.get(i).getF_content());
			jsonObjArr.put("f_times", forumList.get(i).getF_times());
			//jsonObjArr.put("user_id", forumList.get(i).getUser_id());
			jsonObjArr.put("f_img", forumList.get(i).getF_img());
			jsonArray.put(jsonObjArr);
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonArray.toString();
	}
}
