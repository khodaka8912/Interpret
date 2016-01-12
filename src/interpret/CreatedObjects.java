package interpret;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreatedObjects {
	
	private static final List<Object> CREATED_LIST = Collections.synchronizedList(new ArrayList<Object>());

	private CreatedObjects() {
		
	}
	
	public static List<Object> getList() {
		return CREATED_LIST;
	}
}
