package ru.alishev.springcourse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.mysql.jdbc.Statement;

/**
 * @author Neil Alishev
 */
@Controller
public class HelloController {

	static String databaseName = "test"; //Данные для конекта к бд
	static String url = "jdbc:mysql://localhost:3306/" + databaseName;
	static String username = "root";
	static String password = "1303";
	ArrayList<String> nameArray = new ArrayList<String>();
	ArrayList<Double> priceArray = new ArrayList<Double>();
	Double cost = 0.0;
	@GetMapping("/ror")
    public String sayHello(@RequestParam(value = "name", required = false) String name, //переменные для сбора информации из url
    					   @RequestParam(value = "price", required = false) Double price, Model model) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		if (price != null) {
			Class.forName("com.mysql.jdbc.Driver").newInstance(); //Коннект драйвера sql
			
			try(Connection conn = DriverManager.getConnection(url, username, password); //Логин 
					Statement statement = (Statement) conn.createStatement()){ 
					statement.executeUpdate("insert into pharmacy (name, price) values('"+name+"', '"+price+"')"); //запрос в бд который добавляет данные о покупках
					
					ResultSet rs = statement.executeQuery("select * from pharmacy"); //Фрагмент когда который отвечает за сбор и вовод информации ид базы данных
					while (rs.next()) {
						nameArray.add(rs.getString("name"));
						cost += rs.getDouble("price");
					}
					
			}
		}
        return "hello_world";
       }
	
	@GetMapping("/admin") 
	public String admin(Model name, Model price){
		name.addAttribute("name", "" + nameArray); //Применение значений к переменным которые можно использовать в представлении
		price.addAttribute("price", "Full cost = " + cost);
		return "Admin";
	}
	
	@GetMapping("/login")
	public String login(@RequestParam(value = "login", required = false) String login, 
						@RequestParam(value = "password", required = false) String password) {
		if (login == "admin" && password == "admin") { //Фрагмент кода отвечающий за логин под админа
			return "Admin";
		}
			return "login";
	}
}



































