package com.parsleyj.simplecsv;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * SimpleCSV class.
 * An instance of this class represents a CSV document and
 * gives methods to parse, format and edit CSV documents.
 * 
 * @author ParsleyJ
 * 
 */
public class SimpleCSV {
	
	private String separator;
	private String nullValue = "";
	protected List<List<String>> rows = new ArrayList<>();
	
	//TODO: javadoc
	public SimpleCSV(String separator){
		this.separator = separator;
	}
	
	//TODO: javadoc
	public SimpleCSV(List<List<String>> parsedCSV, String separator) {
		rows = new ArrayList<>(parsedCSV);
		this.separator = separator;
		
	}
	
	//TODO: javadoc
	public SimpleCSV(Scanner sc, String separator){
		this.separator = separator;
		rows = parse(sc);
	}
	
	//TODO: javadoc
	public SimpleCSV(String csv, String separator){
		this.separator = separator;
		rows = parse(csv);
	}
	
	//TODO: javadoc
	public SimpleCSV(SimpleCSV csv){
		this.rows = deepClone(csv.rows);
		this.separator = new String(csv.separator);
	}
	
	//TODO: javadoc
	public List<List<String>> parse(Scanner sc){
		return parse(sc,separator);
	}
	
	//TODO: javadoc
	public List<List<String>> parse(String csv){
		return parse(csv,separator);
	}
	
	//TODO: javadoc
	public static List<List<String>> parse(String csv, String separator) {
		List<List<String>> result = new ArrayList<>();
		Scanner sc = new Scanner(csv);
		result = parse(sc,separator);
		sc.close();
		return result;
	}
	
	//TODO: javadoc
	public static List<List<String>> parse(Scanner sc, String separator){
		List<List<String>> result = new ArrayList<>();
		List<String> lines = new ArrayList<>();
		while(sc.hasNext()){
			lines.add(sc.nextLine());
		}
		
		for(String l:lines){
			List<String> columns = parseRow(l,separator);
			result.add(columns);
		}
		
		
		return result;
	}
	
	//TODO: javadoc
	public List<String> parseRow(String l){
		return parseRow(l,getSeparator());
	}
	
	//TODO: javadoc
	public static List<String> parseRow(String l, String separator){
		List<String> columns = new ArrayList<>();
		for(String t: l.split(separator)) //TODO: test
			columns.add(t);
		return columns;
	}
	
	//TODO: javadoc
	public String toString(){
		String result = "";
		for(List<String> row: rows){
			if(row == null){
				//??
			}else{
				for(String token: row){
					if(token == null){
						//??
					}else{
						result += token + separator;
					}
				}
				result = result.substring(0, result.length()-separator.length()); //deletes last separator from row
				result+="\n";
			}	
		}
		return result;
	}
	
	//TODO: javadoc
	public void set(String csv){
		rows = parse(csv);
	}
	
	//TODO: javadoc
	public void set(List<List<String>> rows){
		this.rows = rows;
	}

	//TODO: javadoc
	public int getRowCount() {
		return rows.size();
	}
	
	//TODO: javadoc
	public void setRowCount(int x) {
		if (x<0)
			throw new IndexOutOfBoundsException("Invalid max row count: "+x);
		int s = rows.size();
		for(int i = 0; i < x-s; ++i){
			appendEmptyRow();
		}
		for(int i = 0; i < s-x; ++i){
			removeLastRow();
		}
		
	}

	//TODO: javadoc
	public int getMaxColumnCount() {
		int max = 0;
		for(List<String> row: rows){
			max = Math.max(row.size(), max);
		}
		return max;
	}
	
	//TODO: javadoc
	public void setMaxColumnCount(int x) {
		if (x<0)
			throw new IndexOutOfBoundsException("Invalid max column count: "+x);
		for(int i = 0; i < rows.size(); ++i){
			while(rows.get(i).size()>x){
				removeLastColumnFromRow(i);
			}
		}
	}

	//TODO: javadoc
	public int getMinColumnCount() {
		int min = 0;
		for(List<String> row: rows){
			min = Math.min(row.size(), min);
		}
		return min;
	}
	
	//TODO: javadoc
	public void setMinColumnCount(int x) {
		for(int i = 0; i < rows.size(); ++i){
			appendNullsToRow(x, x-rows.get(i).size());
		}
	}

	//TODO: javadoc
	public boolean isAligned() {
		return getMinColumnCount() == getMaxColumnCount();
	}

	//TODO: javadoc
	public List<String> getRow(int r) {
		return rows.get(r);
	}
	
	//TODO: javadoc
	public void insertRow(int r, List<String> row){
		rows.add(r, row);
	}
	
	//TODO: javadoc
	public void addRow(List<String> row){
		rows.add(row);
	}
	
	//TODO: javadoc
	public void insertRow(int r, String row){
		insertRow(r,parseRow(row));
	}
	
	//TODO: javadoc
	public void addRow(String row){
		addRow(parseRow(row));
	}
	
	//TODO: javadoc
	public List<String> getColumn(int c) {
		if(c>=getMaxColumnCount()) 
			throw new IndexOutOfBoundsException("No rows are found with column number "+c);
		List<String> result = new ArrayList<>();
		for(List<String> row : rows){
			String s = nullValue;
			try{
				s = row.get(c);
			}catch(IndexOutOfBoundsException e){
				//does nothing
			}
			result.add(s);
		}
		return result;
	}

	//TODO: javadoc
	public String getValue(int row, int column) {
		return rows.get(row).get(column);
	}

	//TODO: javadoc
	public String getSeparator() {
		return separator;
	}

	//TODO: javadoc
	public List<List<String>> getRows() {
		return rows;
	}
	
	//TODO: javadoc
	public void align(){
		int max = getMaxColumnCount();
		for(List<String> row: rows){
			while(row.size()<max){
				row.add(nullValue);
			}
		}
	}

	//TODO: javadoc
	public String getNullValue() {
		return nullValue;
	}

	//TODO: javadoc
	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}
	
	//TODO: javadoc
	public void appendNullsToRow(int row, int nulls){
		for(int i = 0; i < nulls; ++i){
			rows.get(row).add(nullValue);
		}
	}
	
	//TODO: javadoc
	public void appendEmptyRow(){
		rows.add(new ArrayList<String>());
	}
	
	//TODO: javadoc
	public void removeLastRow(){
		rows.remove(rows.size()-1);
	}
	
	//TODO: javadoc
	public void removeLastColumnFromRow(int r){
		rows.get(r).remove(rows.get(r).size()-1);
	}
	
	//TODO: javadoc
	public void appendValueToRow(int row, String value){
		rows.get(row).add(value);
	}
	
	//TODO: javadoc
	public void replaceValue(int row, int column, String v){
		rows.get(row).remove(column);
		rows.get(row).add(column,v);
	}
	
	//TODO: javadoc
	public void insertValue(int row, int column, String v){
		rows.get(row).add(column,v);
	}
	
	//TODO: javadoc
	public static List<List<String>> deepClone(List<List<String>> original){
		List<List<String>> result = new ArrayList<>();
		
		for(List<String> row: original){
			List<String> resultRow = new ArrayList<String>();
			
			for(String value: row){
				resultRow.add(new String(value));
			}
			
			result.add(resultRow);
		}
		
		return result;
	}
}
