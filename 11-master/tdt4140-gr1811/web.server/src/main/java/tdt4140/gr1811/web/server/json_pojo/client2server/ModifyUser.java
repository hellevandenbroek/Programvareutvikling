package tdt4140.gr1811.web.server.json_pojo.client2server;

public class ModifyUser extends ClientJson {
	Integer weight;
	Integer height;
	String email;
	String phoneNum;
	
	public ModifyUser(Integer id, Integer weight, Integer height, String email, String phoneNum) {
		this.msgType = ClientMsgType.MODIFY_USER;
		this.weight = weight;
		this.height = height;
		this.email = email;
		this.phoneNum = phoneNum;
	}

	public final Integer getWeight() {
		return weight;
	}

	public final Integer getHeight() {
		return height;
	}

	public final String getPhoneNum() {
		return phoneNum;
	}

	public final String getEmail() {
		return email;
	}
	
}
