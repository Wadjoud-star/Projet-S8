/**
 * 
 */
package com.clubsport.dao;
import java.sql.*;
import java.util.*;

import com.clubsport.model.*;
import com.clubsport.util.*;
/**
 * RegionDAO est la classe qui permet de recuperer la liste des regions
 * de France
 * 
 * @author bpenw
 * @version 1.0
 */
public class RegionDAO {
	
	public List<Region> findAll() throws SQLException{
		List<Region> list = new ArrayList<>();
		String sql = "SELECT* FROM region ORDER BY nom_region";
		try(Connection conn = ConnexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()){
			while(rs.next()) {
				list.add(new Region(rs.getString("code_region"), rs.getString("nom_region")));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
