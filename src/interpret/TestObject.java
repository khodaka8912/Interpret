package interpret;

public class TestObject {

	public static String publicStaticStr = "public static";
	public static String PUBLIC_STATIC_FINAL_STR = "public static final";
	private static String privateStaticStr = "private static";
	private static String PRIVATE_SATIC_FINAL_STR = "private static final";

	public String publicStr = "public";
	public String publicFinalStr = "public final";
	private String privateStr = "private";
	private String privateFinalStr = "private final";

	public static String getPublicStaticStr() {
		return publicStaticStr;
	}

	public static void setPublicStaticStr(String publicStaticStr) {
		TestObject.publicStaticStr = publicStaticStr;
	}

	public static String getPublicStaticFinalStr() {
		return PUBLIC_STATIC_FINAL_STR;
	}

	public static void setPublicStaticFinalStr(String publicStaticFinalStr) {
		PUBLIC_STATIC_FINAL_STR = publicStaticFinalStr;
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

	public static void setPrivateSaticFinalStr(String privateSaticFinalStr) {
		PRIVATE_SATIC_FINAL_STR = privateSaticFinalStr;
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

	public void setPublicFinalStr(String publicFinalStr) {
		this.publicFinalStr = publicFinalStr;
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

	public void setPrivateFinalStr(String privateFinalStr) {
		this.privateFinalStr = privateFinalStr;
	}

}
