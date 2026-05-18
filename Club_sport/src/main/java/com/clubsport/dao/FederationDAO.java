/**
 * 
 */
package com.clubsport.dao;

import java.sql.*;
import java.util.*;

import com.clubsport.model.*;
import com.clubsport.util.*;
/**
 * FederationDAO est la classe qui permet de recuperer la liste des federations
 * de France
 * 
 * @author bpenw
 * @version 1.0
 */
public class FederationDAO {
	
	public List<Federation> findAll() throws SQLException{
		List<Federation> list = new ArrayList<>();
		String sql = "SELECT* FROM federation ORDER BY nom_federation";
		try(Connection conn = ConnexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()){
			while(rs.next()) {
				list.add(new Federation(rs.getString("code_federation"), rs.getString("nom_federation")));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
