package wancheng.com.servicetypegovernment.bean;


import java.io.Serializable;

public class UserDateBean implements Serializable {
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6767056857541088900L;
	/**
	 * 
	 */
	private String username="";// 用户名
	private String id = "0";// 用户ID
	private String name="";//真实姓名
    private String password="";//密码
    private int sex=1;//性别1 男 2 女
    private String phone="";//手机号码
    private String cardid="";//身份证号
	private String address="";//联系地址
	private String IMEI;//手机组号
    private String photoimage="";//头像
	private String email="";//邮箱
	private String no="";//工号
	private String mobile="";//电话
	private String  office="";
	private double  distance=500;
	private double  lng=0;
	private double  lat=0;

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	private static UserDateBean user;

	public static UserDateBean getInstance() {
		if (user == null) {
			user = new UserDateBean();
		}
		return user;
	}

	public String getIMEI() {
		return IMEI;
	}

	public void setIMEI(String IMEI) {
		this.IMEI = IMEI;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhotoimage() {
		return photoimage;
	}

	public void setPhotoimage(String photoimage) {
		this.photoimage = photoimage;
	}



	public static UserDateBean getUser() {
		return user;
	}

	public static void setUser(UserDateBean user) {
		UserDateBean.user = user;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}
}
