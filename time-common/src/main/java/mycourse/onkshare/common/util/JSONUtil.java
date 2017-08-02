package mycourse.onkshare.common.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mycourse.onkshare.model.result.Result;
import mycourse.onkshare.model.result.SearchResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultDefaultValueProcessor;
import net.sf.json.util.JSONUtils;

public class JSONUtil {
	
	public static String collectionToJSON(Collection<?> collection) {
		JSONArray array = JSONArray.fromObject(collection);
		return array.toString();
	}
	
	public static String mapToJSON(Map<String, ?> map) {
		JSONArray array = JSONArray.fromObject(map);
		return array.toString();
	}


	public static JSONArray JSONToResultData(String json) {
		boolean b = JSONUtils.mayBeJSON(json);
		if (b) {
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerDefaultValueProcessor(Object.class,new DefaultDefaultValueProcessor(){
				@Override
				public Object getDefaultValue(Class type) {
					return "";
				}
			});
			JSONObject jsonObject = JSONObject.fromObject(json, jsonConfig);
			JSONArray rows = jsonObject.getJSONArray("rows");
			return rows;
		}
		return null;
	}

	public static JSONObject JSONToResult(String json) {
		boolean b = JSONUtils.mayBeJSON(json);
		if (b) {
			JsonConfig jsonConfig = new JsonConfig();
			JSONObject jsonObject = JSONObject.fromObject(json, jsonConfig);
			return jsonObject;
		}
		return null;
	}

	public static String resultToJSON(Result result) {
		if (result != null) {
			JSONObject jsonObject = JSONObject.fromObject(result);
			String s = jsonObject.toString();
			return s;
		}
		return  null;
	}
}
