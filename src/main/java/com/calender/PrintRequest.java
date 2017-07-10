package com.calender;

/**
 * Created by NAVER on 2017-07-07.
 */
public class PrintRequest {
    private String in;
    private String out;
    private String name;

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "WkhtmlRequest{" +
                "in='" + in + '\'' +
                ", out='" + out + '\'' +
                '}';
    }
}

