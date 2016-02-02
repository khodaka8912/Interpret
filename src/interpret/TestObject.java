package interpret;

public class TestObject {

	public static String publicStaticStr = "public static";
	public static final String PUBLIC_STATIC_FINAL_STR = "public static final";
	private static String privateStaticStr = "private static";
	private static final String PRIVATE_SATIC_FINAL_STR = "private static final";

	public String publicStr = "public";
	public final String publicFinalStr = "public final";
	private String privateStr = "private";
	private final String privateFinalStr = "private final";

	public static String getPublicStaticStr() {
		return publicStaticStr;
	}

	public static void setPublicStaticStr(String publicStaticStr) {
		TestObject.publicStaticStr = publicStaticStr;
	}

	public static String getPublicStaticFinalStr() {
		return PUBLIC_STATIC_FINAL_STR;
	}

	public static String getPrivateStaticStr() {
		return privateStaticStr;
	}

	public static void setPrivateStaticStr(String privateStaticStr) {
		TestObject.privateStaticStr = privateStaticStr;
	}

	public static String getPrivateSaticFinalStr() {
		return PRIVATE_SATIC_FINAL_STR;
	}

	public String getPublicStr() {
		return publicStr;
	}

	public void setPublicStr(String publicStr) {
		this.publicStr = publicStr;
	}

	public String getPublicFinalStr() {
		return publicFinalStr;
	}

	public String getPrivateStr() {
		return privateStr;
	}

	public void setPrivateStr(String privateStr) {
		this.privateStr = privateStr;
	}

	public String getPrivateFinalStr() {
		return privateFinalStr;
	}
}
