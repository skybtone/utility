package com.xlx.utility.pojo;

import java.util.ArrayList;

public class REQ_BASE {
	private short	context_no;
	private int	app_no;
	private int	table_no;
	private char	byte_tag;
	private ArrayList<FIELD_STRU> seq_field = new ArrayList<FIELD_STRU>();
	
	public short getContext_no() {
		return context_no;
	}
	public void setContext_no(short context_no) {
		this.context_no = context_no;
	}
	public int getApp_no() {
		return app_no;
	}
	public void setApp_no(int app_no) {
		this.app_no = app_no;
	}
	public int getTable_no() {
		return table_no;
	}
	public void setTable_no(int table_no) {
		this.table_no = table_no;
	}
	public char getByte_tag() {
		return byte_tag;
	}
	public void setByte_tag(char byte_tag) {
		this.byte_tag = byte_tag;
	}
	public ArrayList<FIELD_STRU> getSeq_field() {
		return seq_field;
	}
	public void setSeq_field(ArrayList<FIELD_STRU> seq_field) {
		this.seq_field = seq_field;
	}
	
	
}
