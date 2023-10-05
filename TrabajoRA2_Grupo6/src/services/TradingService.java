package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import models.Provider;
import models.Trading;

public class TradingService {
	
	static ResultSet resultSet = null;
	static Connection cnn = AzureSql.createConnection();
	
	//Method to autoIncrement the id when a new Transaction is created
	public static int getNextId() {
		int id = -1;
		String sql = "SELECT MAX(id) FROM Trading;";
		try {
			Statement statement = cnn.createStatement();
			resultSet = statement.executeQuery(sql);
			resultSet.next();
			id = resultSet.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id + 1;
	}
	
	public static boolean insertTrading(Trading t, String transactionType) {
		String sql = "INSERT INTO Trading VALUES("+t.getId()+", "+t.getId_product()+", "+t.getId_provider()
		+", "+t.getAmount()+", \'"+new SimpleDateFormat("yyyy-MM-dd").format(t.getDate())+"\', \'"+t.getType()+"\');";
		try {
			PreparedStatement statement = cnn.prepareStatement(sql);
			statement.execute();
			statement.setInt(1, t.getId());
            statement.setInt(2, t.getId_product());
            statement.setInt(3, t.getId_provider());
            statement.setInt(4, t.getAmount());
            statement.setString(5, new SimpleDateFormat("yyyy-MM-dd").format(t.getDate()));
            statement.setString(6, transactionType); //("sell" or "buy")
            statement.execute();
            
            //Here I update the stock after each transaction
            if (transactionType.equals("buy")) {
                updateProductStock(t.getId_product(), t.getAmount()); //Increment stock
            } else if (transactionType.equals("sell")) {
                updateProductStock(t.getId_product(), -t.getAmount()); //Decrement stock
            }
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static ArrayList<Trading> selectTrading(String field, Object value){
			
			ArrayList<Trading> trades = new ArrayList<Trading>();
			String sql = "SELECT * FROM Trading";
			if(field == null) {
				sql += ";";
			}else if(field.equalsIgnoreCase("id, id_provider, id_product, amount")){
				sql += " WHERE(" + field + " = " + value +");";
			}else {
				sql += " WHERE(" + field + " = \'" + value +"\');";
			}
			
			try {
				Statement statement = cnn.createStatement();
				resultSet = statement.executeQuery(sql);
				while(resultSet.next()) {
					trades.add(new Trading(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getString(5), resultSet.getString(6)));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return trades;
		}
	
	
	//Method to increment o decrement the stock of the products in the data base
	private static void updateProductStock(int productId, int amountChange) {
        String sql = "UPDATE Products SET stock = stock + ? WHERE id = ?";
        try {
            PreparedStatement statement = cnn.prepareStatement(sql);
            statement.setInt(1, amountChange);
            statement.setInt(2, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	
	




}
