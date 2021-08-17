import java.sql.*;
final class Bank            //Immutable Data class
{
	final String bankName="STATE BANK OK INDIA";
	final String bankAddress="123,1st Main road,Mumbai";
	final String bankIFSCCode="SBI0012345";
	private static final String USERID="Manager@SBI";
	private final static String PASSWORD="Hello@123";
	public String getBankname() {
		return bankName;
	}
	public String getBankaddress() {
		return bankAddress;
	}
	public String getBankIFSCCode() {
		return bankIFSCCode;
	}
	protected final boolean login_Credentials(String userId,String password)
	{
		return ((USERID==userId) && (PASSWORD==password));
	}
}
class AccountHolder                                     //Dataclass
{
	private final int accHolderId;
	private String accHolderFirstName;
	private String accHolderLastName;
	private int accHolderAge;
	private String accHolderGender;
	private int accTypeId;
	private String accHolderAddress;
	private String accHolderCity;
	
	public AccountHolder(int accHolderId,String fName,String lName,int age,String gender,String city,String address) {
		
		 this.accHolderId=accHolderId;
		 accHolderFirstName=fName.toUpperCase();
		 accHolderLastName=lName.toUpperCase(); 
		 accHolderAge=age;
		 accHolderGender=gender.toUpperCase();
		 accHolderCity=city;
		 accHolderAddress=address;
	}

	public int getAccHolderId() {
		return accHolderId;
	}
	public String getAccHolderFirstName() {
		return accHolderFirstName;
	}
	public String getAccHolderLastName() {
		return accHolderLastName;
	}
	public int getAccHolderAge() {
		return accHolderAge;
	}
	public String getAccHolderGender() {
		return accHolderGender;
	}
	public int getAccTypeId() {
		return accTypeId;
	}
	public String getAccHolderAddress() {
		return accHolderAddress;
	}
	public String getAccHolderCity() {
		return accHolderCity;
	}


//	public void setAccHolderId(int accHolderId) {
//		this.accHolderId = accHolderId;
//	}
	public void setAccHolderFirstName(String accHolderFirstName) {
		this.accHolderFirstName = accHolderFirstName.toUpperCase();
	}
	public void setAccHolderLastName(String accHolderLastName) {
		this.accHolderLastName = accHolderLastName.toUpperCase();
	}
	public void setAccHolderAge(int accHolderAge) {
		this.accHolderAge = accHolderAge;
	}
	public void setAccHolderGender(String accHolderGender) {
		this.accHolderGender = accHolderGender.toUpperCase();
	}
	public void setAccTypeId(int accTypeId) {
		this.accTypeId = accTypeId;
	}
	public void setAccHolderAddress(String accHolderAddress) {
		this.accHolderAddress = accHolderAddress;
	}
	public void setAccHolderCity(String accHolderCity) {
		this.accHolderCity = accHolderCity;
	}
}

class TotalLog                         
{
	public static TotalLog first_instance;
	private long total_Acc;
	private TotalLog() {
	}
	public static TotalLog get_instance()
	{
		if(first_instance==null)
			first_instance=new TotalLog();
		return first_instance;
	}

	public final void total_Acc_Log()
	{
		total_Acc++;
	}
	public final void red_total_Acc_Log()
	{
		total_Acc--;
	}
	public final long get_total_Acc_Log()
	{
		return total_Acc++;
	}                                    
}
interface Coupon                   //interface
{
	public void createCoupon(double amount);
	public void displayCoupon();
	
}

abstract class Accounts implements Coupon      //Abstract class                       
{
	double mainBalance;
	AccountHolder accHolder=null;
	double discountAmount=0;
	public abstract void debit(int accNo,double amount);
	public abstract void credit(int accNo,double amount);
	
	public Accounts(AccountHolder obj) {
			accHolder=obj;
	}
	protected final void createAccount(Accounts user)                    //
	{
		String insert_sql1="INSERT INTO AccHolderDetails (AccHolderId,FirstName,LastName,Age,Gender) VALUES (?,?,?,?,?)";
		String insert_sql2="INSERT INTO AddressDetails (AccHolderId,City,AddressLine) VALUES (?,?,?)";
		String insert_sql3="INSERT INTO AccountDetails (AccHolderId,AccTyId) VALUES (?,?)";
		String insert_sql4="INSERT INTO Balance (AccNo) VALUES (?)";
		String db_URL=MainProject4.db_URL;
		int gen_accNo;
		Connection con=null;
		PreparedStatement pstmt=null;
		try
		{
			con=DriverManager.getConnection(db_URL);
			con.setAutoCommit(false);
			pstmt=con.prepareStatement(insert_sql1);
			pstmt.setInt(1, user.accHolder.getAccHolderId());
			pstmt.setString(2, user.accHolder.getAccHolderFirstName());
			pstmt.setString(3, user.accHolder.getAccHolderLastName());
			pstmt.setInt(4, user.accHolder.getAccHolderAge());
			pstmt.setString(5, user.accHolder.getAccHolderGender());
			pstmt.executeUpdate();
			con.commit();
			pstmt.close();
			
			pstmt=con.prepareStatement(insert_sql2);
			pstmt.setInt(1, user.accHolder.getAccHolderId());
			pstmt.setString(2, user.accHolder.getAccHolderCity());
			pstmt.setString(3, user.accHolder.getAccHolderAddress());
			pstmt.executeUpdate();
			con.commit();
			pstmt.close();
			
			
			pstmt=con.prepareStatement(insert_sql3);
			pstmt.setInt(1, user.accHolder.getAccHolderId());
			pstmt.setInt(2, user.accHolder.getAccTypeId());
			pstmt.executeUpdate();
			con.commit();
			pstmt.close();
			
			TotalLog.get_instance().total_Acc_Log();
			
			gen_accNo=user.getAccountNo(user.accHolder.getAccHolderId(), user.accHolder.getAccTypeId());
			pstmt=con.prepareStatement(insert_sql4);
			pstmt.setInt(1, gen_accNo);
			pstmt.executeUpdate();
			con.commit();
			pstmt.close();
		    con.setAutoCommit(true);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected final void updateAccountHolderDetails(Accounts user,int accHolderId,String fName,String lName,int age,String gender)
	{
		Connection con=null;
		PreparedStatement pstmt= null;
		String db_URL=MainProject4.db_URL;
		String update_sql="UPDATE AccHolderDetails SET  FirstName=?,LastName=?,Age=?,Gender=? WHERE AccHolderId=?";
		user.accHolder.setAccHolderFirstName(fName);
		user.accHolder.setAccHolderLastName(lName);
		user.accHolder.setAccHolderAge(age);
		user.accHolder.setAccHolderGender(gender);
		try
		{
			con=DriverManager.getConnection(db_URL);
			con.setAutoCommit(false);
			pstmt=con.prepareStatement(update_sql);
			pstmt.setString(1, user.accHolder.getAccHolderFirstName());
			pstmt.setString(2, user.accHolder.getAccHolderLastName());
			pstmt.setInt(3, user.accHolder.getAccHolderAge());
			pstmt.setString(4, user.accHolder.getAccHolderGender());
			
			pstmt.setInt(5, user.accHolder.getAccHolderId());
			pstmt.executeUpdate();
			con.commit();
			pstmt.close();
			
			con.setAutoCommit(true);
		}
		catch(Exception e) {
			System.out.println("Error at updateAccHolderDetails");
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	protected final void updateAddressDetails(Accounts user,int accHolderId,String city,String addressLine)
	{
		Connection con=null;
		PreparedStatement pstmt= null;
		String db_URL=MainProject4.db_URL;
		String update_sql="UPDATE AddressDetails SET  City=?,AddressLine=? WHERE AccHolderId=?";
		user.accHolder.setAccHolderCity(city);
		user.accHolder.setAccHolderAddress(addressLine);
		try
		{
			con=DriverManager.getConnection(db_URL);
			con.setAutoCommit(false);
			pstmt=con.prepareStatement(update_sql);
			pstmt.setString(1, user.accHolder.getAccHolderCity());
			pstmt.setString(2, user.accHolder.getAccHolderAddress());
			pstmt.setInt(3, user.accHolder.getAccHolderId());
			//System.out.println("commiting updateAddressDetails");
			pstmt.executeUpdate();
			con.commit();
			pstmt.close();
			con.setAutoCommit(true);		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	protected final void deleteAccount(int accHolderId)
	{
		Connection con=null;
		PreparedStatement pstmt= null;
		String db_URL=MainProject4.db_URL;
		//int gen_accNo[];
		String delete_sql="DELETE FROM AccHolderDetails WHERE AccHolderId=?";
		String delete_sql2="DELETE FROM AddressDetails WHERE AccHolderId=?";
		String delete_sql3="DELETE FROM AccountDetails WHERE AccHolderId=?";
		String delete_sql4="DELETE FROM Balance WHERE AccNo=?";
		String delete_sql5="SELECT AccNo FROM AccountDetails WHERE AccHolderId=?";
		try
		{
			con=DriverManager.getConnection(db_URL);
			con.setAutoCommit(false);

			pstmt=con.prepareStatement(delete_sql5);
     		pstmt.setInt(1, accHolderId);
     		ResultSet rs=pstmt.executeQuery();
     		con.commit();
     		while(rs.next()) {
     			pstmt=con.prepareStatement(delete_sql4);
     			pstmt.setInt(1, rs.getInt("AccNo"));
     			pstmt.executeUpdate();
     			con.commit();
     			pstmt.close();
     		}
     		pstmt.close();
			
			pstmt=con.prepareStatement(delete_sql3);
			pstmt.setInt(1, accHolderId);
			pstmt.executeUpdate();
			con.commit();
			pstmt.close();
			
			pstmt=con.prepareStatement(delete_sql2);
			pstmt.setInt(1, accHolderId);
			pstmt.executeUpdate();
			con.commit();
			pstmt.close();
			
			pstmt=con.prepareStatement(delete_sql);
			pstmt.setInt(1, accHolderId);
			pstmt.executeUpdate();
			con.commit();
			pstmt.close();
			
			
			con.setAutoCommit(true);
			TotalLog.get_instance().red_total_Acc_Log();
			System.out.println("Account having ID="+accHolderId+" is deleted");	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	protected final void deleteAccount(int accHolderId,int accTyId)
	{
		Connection con=null;
		PreparedStatement pstmt= null;
		String db_URL=MainProject4.db_URL;
		String delete_sql3="DELETE FROM AccountDetails WHERE (AccHolderId=? AND AccTyId=?)";
		String delete_sql4="DELETE FROM Balance WHERE AccNo=?";
		try
		{
			con=DriverManager.getConnection(db_URL);
			con.setAutoCommit(false);
			
			int current_accNo=getAccountNo(accHolderId,accTyId);
			pstmt=con.prepareStatement(delete_sql4);
			pstmt.setInt(1, current_accNo);
			pstmt.executeUpdate();
			con.commit();
			pstmt.close();
						
			pstmt=con.prepareStatement(delete_sql3);
			pstmt.setInt(1, accHolderId);
			pstmt.setInt(2, accTyId);
			pstmt.executeUpdate();
			con.commit();
			pstmt.close();
			con.setAutoCommit(true);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	protected final int getAccountNo(int accHolderId,int accTyId)
	{
		int accNo=0;
		Connection con=null;
		PreparedStatement pstmt=null;
		String db_URL=MainProject4.db_URL;
		String select_sql="SELECT AccNo from AccountDetails where (AccHolderId=? AND AccTyId=?)";
		try
		{
			con=DriverManager.getConnection(db_URL);
			pstmt=con.prepareStatement(select_sql);
			pstmt.setInt(1, accHolderId);
			pstmt.setInt(2, accTyId);
			ResultSet rs=pstmt.executeQuery();
			accNo=rs.getInt("AccNo");
			if(accNo==0)
			{
				System.out.println("Account doesn't exist");
			}
			rs.close();
			pstmt.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return accNo;
	}
	protected final double getMainBalance(int accNo)
	{
		double balance=0;
		Connection con=null;
		String db_URL=MainProject4.db_URL;
		PreparedStatement pstmt=null;
		String select_sql="SELECT MainBalance FROM Balance WHERE AccNo=?";
		try {
			con=DriverManager.getConnection(db_URL);
			pstmt=con.prepareStatement(select_sql);
			pstmt.setInt(1, accNo);
			ResultSet rs=pstmt.executeQuery();
			balance=rs.getDouble("MainBalance");	
			rs.close();
			pstmt.close();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return balance;
		
	}
	protected void changeMainBalance(int accNo,double mainBalance)
	{
		Connection con=null;
		PreparedStatement pstmt=null;
		String db_URL=MainProject4.db_URL;
		String update_sql="UPDATE Balance SET  MainBalance=? WHERE AccNo=?";
		try
		{
			con=DriverManager.getConnection(db_URL);
			con.setAutoCommit(false); 
			pstmt=con.prepareStatement(update_sql);
			pstmt.setDouble(1, mainBalance);
			pstmt.setInt(2, accNo);
			pstmt.executeUpdate();
			con.commit();
			con.setAutoCommit(true);
			System.out.println("Amount successfully added!");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try{
				pstmt.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void showBalance(int accNo)
	{
		System.out.println("Available Balance is: "+getMainBalance(accNo));
	}
	public void displayCoupon() {
		System.out.println("Congratulations!! You have won "+discountAmount+" worth of Coupon at KFC!!");
	}
}
class SavingAccount extends Accounts
{
	public SavingAccount(AccountHolder obj) {
		super(obj);
		obj.setAccTypeId(1);
	}
	
	public void debit(int accNo,double amount)
	{
		mainBalance=getMainBalance(accNo);
		if(amount<mainBalance)
		{
			mainBalance=mainBalance-amount;
			changeMainBalance(accNo,mainBalance);
			System.out.println(amount+" :Debited Successfully");
			System.out.println("Available Balance is: "+mainBalance);
			
		}
		else
		{
			System.out.println("No suffcient balance");
			System.out.println("Available Balance is: "+mainBalance);
		}
	}
	public void credit(int accNo,double amount)
	{
		mainBalance=getMainBalance(accNo);
		mainBalance=mainBalance+amount;
		changeMainBalance(accNo,mainBalance);
		System.out.println(amount+" :Credited Successfully");
		createCoupon(amount);
		System.out.println("Available Balance is: "+mainBalance);
	}
	public void createCoupon(double amount)
	{
		double discountPercentage=2;
		discountAmount=(double)(discountPercentage *amount)/100;
	}
	public void displayCoupon()
	{
		System.out.println("Congratulations!! You have won "+discountAmount+" worth of Coupon at KFC!!");
	}
}
class CurrentAccount extends Accounts
{

	public CurrentAccount(AccountHolder obj) {
		super(obj);
		obj.setAccTypeId(2);
	}
	public void debit(int accNo,double amount)
	{
		mainBalance=getMainBalance(accNo);
		if(amount<mainBalance)
		{
			mainBalance=mainBalance-amount;
			changeMainBalance(accNo,mainBalance);
			System.out.println(amount+" :Debited Successfully");
			System.out.println("Available Balance is: "+mainBalance);	
		}
		else
		{
			System.out.println("No suffcient balance");
			System.out.println("Available Balance is: "+mainBalance);
		}
		
	}
	public void credit(int accNo,double amount)
	{
		mainBalance=getMainBalance(accNo);
		mainBalance=mainBalance+amount;
		changeMainBalance(accNo,mainBalance);
		System.out.println(amount+" :Credited Successfully");
		createCoupon(amount);
		System.out.println("Available Balance is: "+mainBalance);
	}
	public void createCoupon(double amount)
	{
		double discountPercentage=3;
		discountAmount=(double)(discountPercentage *amount)/100;
	}
	public void displayCoupon()
	{
		System.out.println("Congratulations!! You have won "+discountAmount+" worth of Coupon at KFC!!");
	}
}

class CODAccount extends Accounts
{
	static int count=0;
	public CODAccount(AccountHolder obj) {
		super(obj);
		obj.setAccTypeId(4);
	}
	public void debit(int accNo,double amount)
	{
		System.out.println("Transaction Failed! Cant debit amount from this type of Account");
	}
	public void credit(int accNo,double amount)
	{
		if(count==0)
		{
		mainBalance=getMainBalance(accNo);
		mainBalance=mainBalance+amount;
		changeMainBalance(accNo,mainBalance);
		System.out.println(amount+" :Credited Successfully");
		createCoupon(amount);
		System.out.println("Available Balance is: "+mainBalance);
		count++;
		}
		else if(count>1)
		{
			System.out.println("Transaction Failed! Cant credit amount more than once in this type of Account");
		}
		else
		{
			System.out.println("Transaction Failed! Please visit your nearby branch");
		}
	}	
	public void createCoupon(double amount)
	{
		double discountPercentage=4;
		discountAmount=(double)(discountPercentage *amount)/100;
	}
	public void displayCoupon()
	{
		System.out.println("Congratulations!! You have won "+discountAmount+" worth of Coupon at KFC!!");
	}
}
class FDAccount extends Accounts
{
	static int count=0;
	public FDAccount(AccountHolder obj) {
		super(obj);
		obj.setAccTypeId(3);
	}
	public void debit(int accNo,double amount)
	{
		System.out.println("Transaction Failed! Cant debit amount in this type of Account");
	}
	public void credit(int accNo,double amount)
	{
		if(count==0)
		{
		mainBalance=getMainBalance(accNo);
		mainBalance=mainBalance+amount;
		changeMainBalance(accNo,mainBalance);
		System.out.println(amount+" :Credited Successfully");
		createCoupon(amount);
		System.out.println("Available Balance is: "+mainBalance);
		count++;
		}
		else if(count>1)
		{
			System.out.println("Transaction Failed! Cant credit amount more than once in this type of Account");
		}
		else
		{
			System.out.println("Transaction Failed! Please visit your nearby branch");
		}
	}
	public void createCoupon(double amount)
	{
		double discountPercentage=6;
		discountAmount=(double)(discountPercentage *amount)/100;
	}
	public void displayCoupon()
	{
		System.out.println("Congratulations!! You have won "+discountAmount+" worth of Coupon at KFC!!");
	}
}

public class MainProject4 {                                          //Main class
	public static final String db_URL="jdbc:sqlite:C:\\sqlite3\\Accounts.db";

	public static void main(String[] args) {
		Bank login=new Bank();
		if(!(login.login_Credentials("Manager@SBI","Hello@123"))) 
		{
			System.out.println("Invalid User Name or Password! Please try again later");
			System.exit(0);
		}
		Connection con=null;
		//Statement stmt=null;
		int accNo;
		try
		{
			Class.forName("org.sqlite.JDBC");
			con=DriverManager.getConnection(db_URL);
			System.out.println("Database connected");
			AccountHolder accHolder1=new AccountHolder(112,"Green","Widow",45,"female","Washington","1,2nd Main road,MMM Nagar");
			Accounts user1=new SavingAccount(accHolder1);
			//System.out.println("Creating Account");
			//user1.createAccount(user1);
			  //user1.updateAccountHolderDetails(user1, 101, "Captan", "America",50, "Male");
			  //user1.updateAddressDetails(user1, 101, "Pondy", "3,2nd Main road,Lawspet");
			//user1.deleteAccount(112,1);
//			accNo=user1.getAccountNo( 111, 1);
//			System.out.println("accNo: "+accNo);
//			user1.showBalance(accNo);	
//			user1.credit(accNo, 1000);
//			user1.debit(accNo, 999);
//         	user1.displayCoupon();
//			System.out.println();
			
			AccountHolder accHolder2=new AccountHolder(102,"Bruce","Wayne",50,"male","Los Angels","1,10th Main road,New Nagar");
			Accounts user2=new SavingAccount(accHolder2);
			//System.out.println("Creating Account");
			//user1.createAccount(user2);
			  //user1.updateAccountHolderDetails(user2, 102, "Bat", "Man",50, "Male");
			  //user1.updateAddressDetails(user2, 102, "Pondy", "3,2nd Main road,Gorimedu");
			  //user1.deleteAccount();
//			accNo=user1.getAccountNo( 102, 1);
//			System.out.println("accNo: "+accNo);
//			user2.showBalance(accNo);	
//			user2.credit(accNo, 1000);
//			user2.debit(accNo, 999);
//         	user2.displayCoupon();
//			System.out.println();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try{
				
				//stmt.close();
				con.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		
	}

}