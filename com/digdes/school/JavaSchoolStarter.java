package com.digdes.school;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class JavaSchoolStarter {
    public JavaSchoolStarter(){
  
    }

    List<Map<String,Object>> data = new ArrayList<>();
    
    public List<Map<String,Object>> execute(String request) throws Exception {
        String[] record = toArray(request);
        if (validate(record)) {
            if(record[0].equalsIgnoreCase("insert")) {
                data = insert(record,data);
            }
            if(record[0].equalsIgnoreCase("update")) {
                data = update(record,data);
            }
            if(record[0].equalsIgnoreCase("delete")) {
                data = delete(record, data);
            }
            if(record[0].equalsIgnoreCase("select")) {
                select(record,data);
            }
            if(!record[0].equalsIgnoreCase("insert") & 
            !record[0].equalsIgnoreCase("update") & 
            !record[0].equalsIgnoreCase("delete") & 
            !record[0].equalsIgnoreCase("select")) {
                throw new Exception ("Команда введена неверно");
            }
            return data;
        } else {throw new Exception("Запрос введен некорректно");}
    }
    
    

    private String[] toArray(String request) {
        request = request.replaceAll(",","");
        request = request.replaceAll("'"," ");
        request = request.replaceAll("<"," < ");
        request = request.replaceAll(">"," > ");
        request = request.replaceAll("="," = ");
        request = request.replaceAll("like"," like ");
        request = request.replaceAll("\\s+"," ");
        request = request.replaceAll("< ="," <= ");
        request = request.replaceAll("> ="," >= ");
        request = request.replaceAll("! ="," != ");
        request = request.replaceAll("i like"," ilike ");
        request = request.replaceAll("\\s+"," ");
        return request.split(" ");
    }

    private Boolean validate (String[] record) {
        List<String> recordList = new LinkedList<String>(Arrays.asList(record));
        if (recordList.size() == 1) {
            if (!recordList.get(0).equalsIgnoreCase("update") & 
            !recordList.get(0).equalsIgnoreCase("delete") & 
            !recordList.get(0).equalsIgnoreCase("select")) {
            return false;
            }
        } else {
            if (!(recordList.get(0).equalsIgnoreCase("insert") & recordList.get(1).equalsIgnoreCase("values")) &
            !(recordList.get(0).equalsIgnoreCase("update") & recordList.get(1).equalsIgnoreCase("values")) &
            !recordList.get(0).equalsIgnoreCase("delete") & !recordList.get(0).equalsIgnoreCase("select")) {
                return false; 
            }
        }
        recordList.remove(0);
        if (recordList.size() == 0) {
            return true;
        }

        if (recordList.get(0).equalsIgnoreCase("values")) {
            recordList.remove(0);
        }

        for (int i = 0; i < recordList.size(); i++) {
            if (recordList.get(i).equalsIgnoreCase("where") || 
            recordList.get(i).equalsIgnoreCase("and") || 
            recordList.get(i).equalsIgnoreCase("or")) {
                recordList.remove(i);
            }
        }
        if (recordList.size() < 3) {
            return false;
        }

        for (int i = 0; i < recordList.size(); i+=3) {
            String key = recordList.get(i);
            String sign = recordList.get(i + 1);
            String value = recordList.get(i + 2);
            if (!key.equals("id") & !key.equals("lastName") & 
            !key.equals("age") & !key.equals("cost") & !key.equals("active")) {
                return false;
            }

            if (key.equals("id") || key.equals("age")) {
                if (!sign.equals("=") && !sign.equals("!=") && !sign.equals(">=") && 
                !sign.equals("<=") && !sign.equals("<") && !sign.equals(">")) {
                    return false;
                }
                try {
                Long.parseLong(value);
                } catch (Exception ex) {
                    System.out.println("Не верный тип данных");
                }
            }

            if (key.equals("lastName")) {
                if (!sign.equals("=") && !sign.equals("like") && !sign.equals("ilike")) {
                    return false;
                }
            }

            if (key.equals("cost")) {
                if (!sign.equals("=") && !sign.equals("!=") && !sign.equals(">=") && 
                !sign.equals("<=") && !sign.equals("<") && !sign.equals(">")) {
                    return false;
                }
                try {
                Double.parseDouble(value);
                } catch (Exception ex) {
                    System.out.println("Не верный тип данных");
                }
            }

            if (key.equals("active")) {
                if (!sign.equals("=") && !sign.equals("!=")) {
                    return false;
                }
                try {
                Boolean.parseBoolean(value);
                } catch (Exception ex) {
                    System.out.println("Не верный тип данных");
                }
            }
        }
        return true;
    }

    public List<Map<String,Object>> insert (String[] record, List<Map<String,Object>> data) throws Exception {
        Map<String,Object> row = new HashMap<>();

        row.put("id", null);
        row.put("lastName", null);
        row.put("age", null);
        row.put("cost", null);
        row.put("active", null);

        for (int i = 2; i < record.length - 2; i++) {
             if(record[i].equalsIgnoreCase("id")) {
                row.put("id",Long.parseLong(record[i+2]));
             }
             if(record[i].equalsIgnoreCase("lastName")) {
                row.put("lastName",record[i+2]);
             }
             if(record[i].equalsIgnoreCase("age")) {
                row.put("age",Long.parseLong(record[i+2]));
             }
             if(record[i].equalsIgnoreCase("cost")) {
                row.put("cost",Double.parseDouble(record[i+2]));
             }
             if(record[i].equalsIgnoreCase("active")) {
                row.put("active",Boolean.parseBoolean(record[i+2]));
             }
        }

        if (row.get("id") == null & 
        row.get("lastName") == null & 
        row.get("age") == null & 
        row.get("cost") == null & 
        row.get("active") == null) {
            throw new Exception("Запрос выполнен неверно");
        }

        data.add(row);
        return data;
    }

    public List<Map<String,Object>> update (String[] record, List<Map<String,Object>> data) {
        if (confirmWhere(record)) {
            Set<Integer> selected = where(record, data);
            for (Integer index : selected) {
                Map<String,Object> row = data.get(index);
                for (int j = 2; j < record.length - indexWhere(record); j++) {
                    if (record[j+2].equalsIgnoreCase("null")) {
                        row.remove(record[j]);
                    }
                    if(record[j].equalsIgnoreCase("id")) {
                        row.put("id",Long.parseLong(record[j+2]));
                     }
                     if(record[j].equalsIgnoreCase("lastName")) {
                        row.put("lastName",record[j+2]);
                     }
                     if(record[j].equalsIgnoreCase("age")) {
                        row.put("age",Long.parseLong(record[j+2]));
                     }
                     if(record[j].equalsIgnoreCase("cost")) {
                        row.put("cost",Double.parseDouble(record[j+2]));
                     }
                     if(record[j].equalsIgnoreCase("active")) {
                        row.put("active",Boolean.parseBoolean(record[j+2]));
                     }
                     data.set(index, row);
                }
            }
        } else {
            for (int i = 0; i < data.size(); i++) {
                Map<String,Object> row = data.get(i);
                for (int j = 2; j < record.length - 2; j++) {
                    if (record[j+2].equalsIgnoreCase("null")) {
                        row.remove(record[j]);
                    }
                    if(record[j].equalsIgnoreCase("id")) {
                        row.put("id",Long.parseLong(record[j+2]));
                     }
                     if(record[j].equalsIgnoreCase("lastName")) {
                        row.put("lastName",record[j+2]);
                     }
                     if(record[j].equalsIgnoreCase("age")) {
                        row.put("age",Long.parseLong(record[j+2]));
                     }
                     if(record[j].equalsIgnoreCase("cost")) {
                        row.put("cost",Double.parseDouble(record[j+2]));
                     }
                     if(record[j].equalsIgnoreCase("active")) {
                        row.put("active",Boolean.parseBoolean(record[j+2]));
                     }
                     data.set(i, row);
                }
            }
        }
        return data;
    }

    public List<Map<String,Object>> delete (String[] record, List<Map<String,Object>> data) {
        if (confirmWhere(record)) {
            Set<Integer> selected = where(record, data);
            for (int index : selected) {
                data.remove(index);
            }
        } else {
            for (Map<String,Object> row : data) {
                data.remove(row);
            }
        }
        return data;
            
    }


    public void select (String[] record, List<Map<String,Object>> data) {
        if (confirmWhere(record)) {
            System.out.println("Выборка:");
            Set<Integer> selected = where(record, data);
            System.out.println("\tid\tlastName\tage\tcost\tactive");
            for (int index : selected) {
                String cost = costToString(index,data);
                System.out.printf("\t%d\t%s\t\t%d\t%s\t%b\n", 
                data.get(index).get("id"), data.get(index).get("lastName"), 
                data.get(index).get("age"), cost, data.get(index).get("active"));
            }
        } else {
            System.out.println("Вся таблица:");
            System.out.println("\tid\tlastName\tage\tcost\tactive");
            for (int i = 0; i < data.size(); i++) {
                String cost = costToString(i,data);
                System.out.printf("\t%d\t%s\t\t%d\t%s\t%b\n", 
                data.get(i).get("id"), data.get(i).get("lastName"), 
                data.get(i).get("age"), cost, data.get(i).get("active"));
            }
        }
    }

    
    public Set<Integer> where (String[] record, List<Map<String,Object>> data) {
        ArrayList<Set<Integer>> indexSets = new ArrayList<>();
        int index = indexWhere(record);
        Object value = null;

        for (int i = index; i < record.length; i+=4) {
            Set<Integer> tempSet = new LinkedHashSet<>();
            String key = record[i + 1].toLowerCase();
            String sign = record[i + 2];
            switch (key) {
                case ("id"):
                    value = Long.parseLong(record[i + 3]);
                    break;
                case ("lastName"):
                    value = record[i + 3];
                    break;
                case ("age"):
                    value = Long.parseLong(record[i + 3]);
                    break;
                case ("cost"):
                    value = Double.parseDouble(record[i + 3]);
                    break;
                case ("active"):
                    value = Boolean.parseBoolean(record[i + 3]);
                    break;
            }
            
            if (record[i].equalsIgnoreCase("and") || record[i].equalsIgnoreCase("or")) {
                if (record[i].equalsIgnoreCase("and")) {
                    tempSet.add(1);
                } else {
                tempSet.add(0);
                }
                indexSets.add(tempSet);
            }
            indexSets.add(condition(key,sign,value,data));
        }
        if (indexSets.size() > 1) {
            for (int i = 1; i < indexSets.size(); i+=2) {
                if (indexSets.get(i).contains(1)) {
                    indexSets.get(i-1).retainAll(indexSets.get(i+1));
                    indexSets.remove(i);
                    indexSets.remove(i);
                    i-=2;
                } else {
                    indexSets.get(i-1).addAll(indexSets.get(i+1));
                    indexSets.remove(i);
                    indexSets.remove(i);
                    i-=2;
                }
            }
        }
        return indexSets.get(0);
    }

    public Set<Integer> condition(String key, String sign, Object value, List<Map<String,Object>> data) {
        Set<Integer> indices = new TreeSet<>(Comparator.reverseOrder());
        for (int i = 0; i < data.size(); i++) {
            switch (sign) {
                case ("="):
                    if (data.get(i).get(key) == null) {
                        break;
                    }
                    if (value instanceof String) {
                        if (data.get(i).get(key).equals(value)) {
                            indices.add(i);
                        }
                    } else {
                        if (data.get(i).get(key) == value) {
                            indices.add(i);
                        }
                    }
                break;

                case ("!="):
                try {
                    if (data.get(i).get(key) != value) {
                        indices.add(i);
                    }
                }
                catch (NullPointerException e) {
                    indices.add(i);
                }
                break;
            
                case ("like"):
                    if(data.get(i).get(key) != null) {
                        if (like(data.get(i).get(key), value)) {
                            indices.add(i);
                        }
                    }
                break;

                case ("ilike"):
                    if(data.get(i).get(key) != null) {
                        if (ilike(data.get(i).get(key), value)) {
                            indices.add(i);
                        }
                    }
                break;
                
                case (">="):
                    if (data.get(i).get(key) == null) {
                        break;
                    }
                    if (value instanceof Long) {
                        if (Long.parseLong(data.get(i).get(key).toString()) >= Long.parseLong(value.toString())) {
                            indices.add(i);
                        }
                    }
                    if (value instanceof Double) {
                        if (Double.parseDouble(data.get(i).get(key).toString()) >= Double.parseDouble(value.toString())) {
                            indices.add(i);
                        }
                    }
                break;

                case ("<="):
                    if (data.get(i).get(key) == null) {
                        break;
                    }
                    if (value instanceof Long) {
                        if (Long.parseLong(data.get(i).get(key).toString()) <= Long.parseLong(value.toString())) {
                            indices.add(i);
                        }
                    }
                    if (value instanceof Double) {
                        if (Double.parseDouble(data.get(i).get(key).toString()) <= Double.parseDouble(value.toString())) {
                            indices.add(i);
                        }
                    }
                break;

                case ("<"):
                    if (data.get(i).get(key) == null) {
                        break;
                    }
                    if (value instanceof Long) {
                        if (Long.parseLong(data.get(i).get(key).toString()) < Long.parseLong(value.toString())) {
                            indices.add(i);
                        }
                    }
                    if (value instanceof Double) {
                        if (Double.parseDouble(data.get(i).get(key).toString()) < Double.parseDouble(value.toString())) {
                            indices.add(i);
                        }
                    }
                break;

                case (">"):
                    if (data.get(i).get(key) == null) {
                        break;
                    }
                    if (value instanceof Long) {
                        if (Long.parseLong(data.get(i).get(key).toString()) > Long.parseLong(value.toString())) {
                            indices.add(i);
                        }
                    }
                    if (value instanceof Double) {
                        if (Double.parseDouble(data.get(i).get(key).toString()) > Double.parseDouble(value.toString())) {
                            indices.add(i);
                        }
                    }
                break;
            }
        }
        return indices;
    }

    public Integer indexWhere (String[] record) {
        Integer indexWhere = null;
        for (int i = 0; i < record.length; i++) {
            if (record[i].equalsIgnoreCase("where")) {
                indexWhere = i;
            }
        }
        return indexWhere;
    }

    public Boolean confirmWhere (String[] record) {
        for (String word : record) {
            if (word.equalsIgnoreCase("where")) {
                return true;
            }
        }
        return false;
    }

    public Boolean like (Object record, Object value) {
        String str1 = record.toString();
        String str2 = value.toString();
        if (str2.startsWith("%") & str2.endsWith("%")) {
            if (str1.contains(str2.substring(1, str2.length()-1))) {
                return true;
            }
        } else if (str2.startsWith("%") & !str2.endsWith("%")) {
            if (str1.endsWith(str2.substring(1))) {
                return true;
            }
        } else if (!str2.startsWith("%") & str2.endsWith("%")) {
            if (str1.startsWith(str2.substring(0, -2))) {
                return true;
            }
        } else if (str1.equals(str2)) {
            return true;
        }
        return false;
    }


    public Boolean ilike (Object record, Object value) {
        String str1 = record.toString().toLowerCase();
        String str2 = value.toString().toLowerCase();
        if (str2.startsWith("%") & str2.endsWith("%")) {
            if (str1.contains(str2.substring(1, str2.length()-1))) {
                return true;
            }
        } else if (str2.startsWith("%") & !str2.endsWith("%")) {
            if (str1.endsWith(str2.substring(1)))  {
                return true;
            }
        } else if (!str2.startsWith("%") & str2.endsWith("%")) {
            if (str1.startsWith(str2.substring(0, str2.length()-1))) {
                return true;
            }
        } else if (str1.equals(str2)) {
            return true;
        }
        return false;
    }


    public String costToString (int index ,List<Map<String,Object>> data) {
        if (data.get(index).get("cost") == null) {
            return null;
        } else {
            return data.get(index).get("cost").toString();
        }
    }
    
    
}
