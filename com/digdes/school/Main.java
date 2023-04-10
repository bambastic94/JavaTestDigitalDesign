package com.digdes.school;


import java.util.List;
import java.util.Map;


public class Main {
  public static void main(String[] args){
    JavaSchoolStarter starter = new JavaSchoolStarter();
      try {
        List<Map<String,Object>> result1 = starter.execute("INSERT VALUES 'lastName' = 'Fedorov' , 'id'=3, 'age'=40, 'cost'=11.7 'active'=true");
        
        List<Map<String,Object>> result2 = starter.execute("INSERT VALUES 'id'=2, 'lastName' = 'Petrov' 'active'= false, 'age' =33, 'cost'=8.5");

        List<Map<String,Object>> result3 = starter.execute("INSERT VALUES 'active'=true, 'id'= 1, 'lastName' = 'Egorov' 'cost'=10.1");
        
        List<Map<String,Object>> result4 = starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'cost'<12 or 'lastName' like '%rov'");
        
        List<Map<String,Object>> result5 = starter.execute("SELECT");
        
        List<Map<String,Object>> result6 = starter.execute("INSERT VALUES 'active'=true, 'id'= 4, 'age'=53");

        List<Map<String,Object>> result7 = starter.execute("SELECT");

        List<Map<String,Object>> result8 = starter.execute("UPDATE VALUES 'active'=false where 'age'!=0");
          
        List<Map<String,Object>> result9 = starter.execute("SELECT");

        List<Map<String,Object>> result10 = starter.execute("SELECT WHERE 'lastName' ilike 'pet%'");

        List<Map<String,Object>> result11 = starter.execute("DELETE WHERE 'age' <= 40");

        List<Map<String,Object>> result12 = starter.execute("SELECT");
        
      }catch (Exception ex){
           ex.printStackTrace();
      }
   }
}
