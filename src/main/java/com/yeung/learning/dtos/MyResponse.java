package com.yeung.learning.dtos;

public class MyResponse {
  private String data = "";

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "MyResponse [data=" + data + "]";
  }

}
